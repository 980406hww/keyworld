package com.keymanager.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.db.DBUtil;
import com.keymanager.util.Utils;
import com.keymanager.value.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VPSOpenInfoManager {
	private static final String NO_MACHINE = "nomachine";
	private static int recordCount;
	private static int currPage = 0;
	private static int pageCount = 0;

	public static int getRecordCount() {
		return recordCount;
	}

	public static int getCurrPage() {
		return currPage;
	}

	public static int getPageCount() {
		return pageCount;
	}

	public List<VPSOpenInfoVO> searchVPSOpenInfos(String dsName, int pageSize, int curPage, String condition, String order,
										   int recCount) throws Exception {
		if (pageSize < 0) {
			pageSize = 20;
		}

		if (curPage < 0) {
			curPage = 1;
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		ArrayList<VPSOpenInfoVO> vpsOpenInfoVOs = new ArrayList<VPSOpenInfoVO>();
		try {
			conn = DBUtil.getConnection(dsName);

			if (recCount != 0) {
				sql = " select count(1) as recordCount from t_vps_open_info voi, t_vps_provider_info vpi WHERE vpi.fuuid = voi.fProviderInfoId " + condition;

				ps = conn.prepareStatement(sql, 1003, 1007);
				rs = ps.executeQuery();
				if (rs.next()) {
					recordCount = rs.getInt("recordCount");
				}

				pageCount = (int) Math.ceil((recordCount + pageSize - 1) / pageSize);

				if (curPage > pageCount) {
					curPage = 1;
				}
				currPage = curPage;
			}

			sql = " SELECT voi.* FROM t_vps_open_info voi, t_vps_provider_info vpi WHERE vpi.fuuid = voi.fProviderInfoId " + condition + " " + order;
			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			VPSProviderInfoManager vpsProviderInfoManager = new VPSProviderInfoManager();
			while (rs.next()) {
				VPSOpenInfoVO vpsOpenInfoVO = this.getVPSOpenInfoVO(rs);
				VPSProviderInfoVO vpsProviderInfoVO = vpsProviderInfoManager.getVPSProviderInfoVOById(conn, vpsOpenInfoVO.getVpsProviderInfoId());
				vpsOpenInfoVO.setProviderInfoVO(vpsProviderInfoVO);
				vpsOpenInfoVOs.add(vpsOpenInfoVO);
			}
			return vpsOpenInfoVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("searchVPSOpenInfos Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public VPSOpenInfoVO getVPSOpenInfoVO(Connection conn, int uuid) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT * FROM t_vps_open_info voi WHERE voi.fUuid = ?";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setInt(1, uuid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				VPSOpenInfoVO vpsOpenInfoVO = getVPSOpenInfoVO(rs);
				return vpsOpenInfoVO;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getVPSOpenInfoVO error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}
		return null;
	}

	public String getVPSForOpen(String dataSourceName) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			String sql = "SELECT * FROM t_vps_open_info voi WHERE voi.fStatus = 1 and voi.fOpenStatus IS NULL ORDER BY fCreateTime LIMIT 1";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			rs = stmt.executeQuery();
			if (rs.next()) {
				VPSOpenInfoVO vpsOpenInfoVO = getVPSOpenInfoVO(rs);
				if(vpsOpenInfoVO != null){
					VPSProviderInfoManager vpsProviderInfoManager = new VPSProviderInfoManager();
					VPSProviderInfoVO vpsProviderInfoVO = vpsProviderInfoManager.getVPSProviderInfoVOById(conn, vpsOpenInfoVO.getVpsProviderInfoId());
					vpsOpenInfoVO.setProviderInfoVO(vpsProviderInfoVO);
					ClientStatusManager clientStatusManager = new ClientStatusManager();
					int maxPCSequence = clientStatusManager.getMaxSequence(conn, vpsProviderInfoVO.getClientIDPrefix());

					Connection phoneConn = null;
					try {
						phoneConn = DBUtil.getConnection("dsKeywordShouji");
						int maxPhoneSequence = clientStatusManager.getMaxSequence(phoneConn, vpsProviderInfoVO.getClientIDPrefix());

						maxPCSequence = (maxPhoneSequence > maxPCSequence) ? maxPhoneSequence : maxPCSequence;

						if(maxPCSequence > vpsProviderInfoVO.getMaxSequence()){
							vpsProviderInfoVO.setMaxSequence(maxPCSequence + 1);
						}else{
							vpsProviderInfoVO.setMaxSequence(vpsProviderInfoVO.getMaxSequence() + 1);
						}
						vpsOpenInfoVO.setCurrentSequence(vpsProviderInfoVO.getMaxSequence());
						vpsProviderInfoManager.updateMaxSequence(conn, vpsProviderInfoVO.getClientIDPrefix(), vpsProviderInfoVO.getMaxSequence());
						this.updateStartOpenInfo(conn, "Processing", vpsProviderInfoVO.getMaxSequence(), vpsOpenInfoVO.getUuid());
						ObjectMapper mapper = new ObjectMapper();
						return mapper.writeValueAsString(vpsOpenInfoVO);
					}catch (Exception ex){
						ex.printStackTrace();
						throw new Exception("getVPSForOpen error");
					}finally {
						DBUtil.closeConnection(phoneConn);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getVPSForOpen error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
		return "";
	}

	public String getVPSForSetting(String dataSourceName) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			VPSOpenInfoVO vpsOpenInfoVO = null;
			String status = "";
			String sql = "SELECT * FROM t_vps_open_info voi WHERE voi.fStatus = 1 and voi.fOpenStatus = 'succ' and DATE_ADD(voi.fCompleteOpenTime, INTERVAL 10 MINUTE) < NOW() and voi.fSettingStatus is null ORDER BY fCompleteOpenTime LIMIT 1";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			rs = stmt.executeQuery();
			if (rs.next()) {
				vpsOpenInfoVO = getVPSOpenInfoVO(rs);
				status = "settingstarting";
			}
			if(vpsOpenInfoVO != null){
				VPSProviderInfoManager vpsProviderInfoManager = new VPSProviderInfoManager();
				VPSProviderInfoVO vpsProviderInfoVO = vpsProviderInfoManager.getVPSProviderInfoVOById(conn, vpsOpenInfoVO.getVpsProviderInfoId());
				vpsOpenInfoVO.setProviderInfoVO(vpsProviderInfoVO);
				this.updateStartSettingInfo(conn, status, vpsOpenInfoVO.getUuid());
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(vpsOpenInfoVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getVPSForSetting error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
		return "";
	}

	public String getVPSForSetup(String dataSourceName) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			String sql = "SELECT * FROM t_vps_open_info voi WHERE voi.fStatus = 1 and voi.fOpenStatus = 'succ' and DATE_ADD(voi.fCompleteSettingTime, INTERVAL 15 MINUTE) < NOW() and voi.fSettingStatus = 'downloadbegining' ORDER BY fCompleteSettingTime LIMIT 1";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			rs = stmt.executeQuery();
			VPSOpenInfoVO vpsOpenInfoVO = null;
			String status = "";
			if (rs.next()) {
				vpsOpenInfoVO = getVPSOpenInfoVO(rs);
				status = "downloadcompleted";
			}
			if(vpsOpenInfoVO != null){
				VPSProviderInfoManager vpsProviderInfoManager = new VPSProviderInfoManager();
				VPSProviderInfoVO vpsProviderInfoVO = vpsProviderInfoManager.getVPSProviderInfoVOById(conn, vpsOpenInfoVO.getVpsProviderInfoId());
				vpsOpenInfoVO.setProviderInfoVO(vpsProviderInfoVO);
				this.updateStartSettingInfo(conn, status, vpsOpenInfoVO.getUuid());
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(vpsOpenInfoVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getVPSForSetting error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
		return "";
	}

	private VPSOpenInfoVO getVPSOpenInfoVO(ResultSet rs) throws Exception {
		VPSOpenInfoVO vpsOpenInfoVO = new VPSOpenInfoVO();
		vpsOpenInfoVO.setUuid(rs.getInt("fUuid"));
		vpsOpenInfoVO.setConnPassword(rs.getString("fConnPassword"));
		vpsOpenInfoVO.setConnUser(rs.getString("fConnUser"));
		vpsOpenInfoVO.setCurrentSequence(rs.getInt("fCurrentSequence"));
		vpsOpenInfoVO.setOpenStatus(rs.getString("fOpenStatus"));
		vpsOpenInfoVO.setOperationType(rs.getString("fOperationType"));
		vpsOpenInfoVO.setSettingStatus(rs.getString("fSettingStatus"));
		vpsOpenInfoVO.setVncHost(rs.getString("fVNCHost"));
		vpsOpenInfoVO.setVncPassword(rs.getString("fVNCPassword"));
		vpsOpenInfoVO.setVncPort(rs.getString("fVNCPort"));
		vpsOpenInfoVO.setVncUser(rs.getString("fVNCUser"));
		vpsOpenInfoVO.setVpsUuid(rs.getString("fVPSUuid"));
		vpsOpenInfoVO.setVpsProviderInfoId(rs.getInt("fProviderInfoId"));
		vpsOpenInfoVO.setStatus(rs.getInt("fStatus"));
		vpsOpenInfoVO.setTrival(rs.getString("fTrial"));
		vpsOpenInfoVO.setCreateTime(rs.getTimestamp("fCreateTime"));
		vpsOpenInfoVO.setStartOpenTime(rs.getTimestamp("fStartOpenTime"));
		vpsOpenInfoVO.setCompleteOpenTime(rs.getTimestamp("fCompleteOpenTime"));
		vpsOpenInfoVO.setStartSettingTime(rs.getTimestamp("fStartSettingTime"));
		vpsOpenInfoVO.setCompleteSettingTime(rs.getTimestamp("fCompleteSettingTime"));
		return vpsOpenInfoVO;
	}

	public void updateStartOpenInfo(Connection conn, String status, int maxSequence, int uuid) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_vps_open_info SET fOpenStatus = ?, fCurrentSequence  = ?, fStartOpenTime = now() WHERE fUuid = ?";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, status);
			ps.setInt(2, maxSequence);
			ps.setInt(3, uuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public VPSOpenInfoVO getVPSOpenInfoVO(Connection conn, String clientID) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String preSql = null;
		try {
			String clientIDPrefix = Utils.removeDigital(clientID);
			String currentSequence = clientID.replace(clientIDPrefix, "");
			preSql = "SELECT voi.* FROM t_vps_open_info voi, t_vps_provider_info vpi WHERE voi.fProviderInfoId = vpi.fUuid AND voi.fCurrentSequence = ? AND vpi.fClientIDPrefix = ?";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, currentSequence);
			ps.setString(2, clientIDPrefix);
			rs = ps.executeQuery();
			if (rs.next()) {
				VPSOpenInfoVO vpsOpenInfoVO = getVPSOpenInfoVO(rs);
				if (vpsOpenInfoVO != null) {
					VPSProviderInfoManager vpsProviderInfoManager = new VPSProviderInfoManager();
					VPSProviderInfoVO vpsProviderInfoVO = vpsProviderInfoManager.getVPSProviderInfoVOById(conn, vpsOpenInfoVO.getVpsProviderInfoId());
					vpsOpenInfoVO.setProviderInfoVO(vpsProviderInfoVO);
				}
				return vpsOpenInfoVO;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateStartSettingInfo(Connection conn, String status, int uuid) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_vps_open_info SET fSettingStatus = ?, fStartSettingTime = NOW() WHERE fUuid = ?";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, status);
			ps.setInt(2, uuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void deleteVPSOpenInfoVO(String dataSourceName, String uuid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String preSql = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			preSql = "delete from t_vps_open_info where fUuid = ?";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, uuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void createVPSOpenInfoVO(Connection conn, int providerInfoId, String operationTye) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "insert into t_vps_open_info(fProviderInfoId, fOperationType, fStatus, fCreateTime) values(?, ?, 1, now())";
			ps = conn.prepareStatement(preSql);
			ps.setInt(1, providerInfoId);
			ps.setString(2, operationTye);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateCompleteSetting(String datasourceName, VPSOpenInfoVO vpsOpenInfoVO) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			updateCompleteSetting(conn, vpsOpenInfoVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("updateCompleteOpenInfo error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void updateCompleteOpenInfo(String datasourceName, VPSOpenInfoVO vpsOpenInfoVO) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			updateCompleteOpenInfo(conn, vpsOpenInfoVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("updateCompleteOpenInfo error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void updateCompleteSetting(Connection conn, VPSOpenInfoVO vpsOpenInfoVO) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_vps_open_info SET fCompleteSettingTime = now(), fSettingStatus = ? WHERE fUuid = ?";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, vpsOpenInfoVO.getSettingStatus());
			ps.setInt(2, vpsOpenInfoVO.getUuid());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateCompleteOpenInfo(Connection conn, VPSOpenInfoVO vpsOpenInfoVO) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			if(vpsOpenInfoVO.getOpenStatus() != null && "succ".equals(vpsOpenInfoVO.getOpenStatus())){
				preSql = "update t_vps_open_info set fOpenStatus = ?, fVNCHost = ?, fVNCPort = ?, fVNCUser = ?, fVNCPassword = ?, fConnUser = ?, fConnPassword = ?, fVPSUuid = ?, fTrial = ?, fCompleteOpenTime = now() where fUuid = ?";
				ps = conn.prepareStatement(preSql);
				int i = 1;
				ps.setString(i++, vpsOpenInfoVO.getOpenStatus());
				ps.setString(i++, vpsOpenInfoVO.getVncHost());
				ps.setString(i++, vpsOpenInfoVO.getVncPort());
				ps.setString(i++, vpsOpenInfoVO.getVncUser());
				ps.setString(i++, vpsOpenInfoVO.getVncPassword());
				ps.setString(i++, vpsOpenInfoVO.getConnUser());
				ps.setString(i++, vpsOpenInfoVO.getConnPassword());
				ps.setString(i++, vpsOpenInfoVO.getVpsUuid());
				ps.setString(i++, vpsOpenInfoVO.getTrival());
				ps.setInt(i++, vpsOpenInfoVO.getUuid());
				ps.executeUpdate();
			}else {
				preSql = "UPDATE t_vps_open_info SET fOpenStatus = ?, fCompleteOpenTime = NOW() WHERE fUuid = ?";
				ps = conn.prepareStatement(preSql);
				ps.setString(1, vpsOpenInfoVO.getOpenStatus());
				ps.setInt(2, vpsOpenInfoVO.getUuid());
				ps.executeUpdate();

				if(NO_MACHINE.equals(vpsOpenInfoVO.getOpenStatus())){
					VPSOpenInfoVO existVPSOpenInfoVO = this.getVPSOpenInfoVO(conn, vpsOpenInfoVO.getUuid());
					this.updateOpenStatusAsNomachine(conn, existVPSOpenInfoVO.getVpsProviderInfoId());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}


	public void updateVPSConfigInfo(Connection conn, VPSOpenInfoVO vpsOpenInfoVO) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_vps_open_info set fVNCHost = ?, fVNCPort = ?, fVNCUser = ?, fVNCPassword = ?, fConnUser = ?, fConnPassword = ?, fVPSUuid = ? where fUuid = ?";
			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, vpsOpenInfoVO.getVncHost());
			ps.setString(i++, vpsOpenInfoVO.getVncPort());
			ps.setString(i++, vpsOpenInfoVO.getVncUser());
			ps.setString(i++, vpsOpenInfoVO.getVncPassword());
			ps.setString(i++, vpsOpenInfoVO.getConnUser());
			ps.setString(i++, vpsOpenInfoVO.getConnPassword());
			ps.setString(i++, vpsOpenInfoVO.getVpsUuid());
			ps.setInt(i++, vpsOpenInfoVO.getUuid());
			ps.executeUpdate();

			preSql = "update t_vps_open_info set fOpenStatus = ?, fStartOpenTime = now(), fCompleteOpenTime = now() where fOpenStatus <> ? and fUuid = ?";
			ps = conn.prepareStatement(preSql);
			i = 1;
			ps.setString(i++, vpsOpenInfoVO.getOpenStatus());
			ps.setString(i++, vpsOpenInfoVO.getOpenStatus());
			ps.setInt(i++, vpsOpenInfoVO.getUuid());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}


	public void updateOpenStatusAsNomachine(Connection conn, int providerInfoId) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_vps_open_info SET fOpenStatus = ?, fCompleteOpenTime = NOW() WHERE fOpenStatus is NULL and fProviderInfoId = ?";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, NO_MACHINE);
			ps.setInt(2, providerInfoId);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateVPSOpenSetting(String datasourceName, String data) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		VPSOpenSettingVO vpsOpenSettingVO = (VPSOpenSettingVO)mapper.readValue(data, VPSOpenSettingVO.class);
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			if(vpsOpenSettingVO != null){
				while(vpsOpenSettingVO.getCount() > 0){
					int providerId = vpsOpenSettingVO.getProviderId();
					if(providerId > 0){
						createVPSOpenInfoVO(conn, providerId, vpsOpenSettingVO.getOperationType());
					}else{
						for(String providerIdStr : vpsOpenSettingVO.getProviderIds().split(",")) {
							createVPSOpenInfoVO(conn, Integer.parseInt(providerIdStr.trim()), vpsOpenSettingVO.getOperationType());
						}
					}
					vpsOpenSettingVO.setCount(vpsOpenSettingVO.getCount() - 1);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("updateVPSOpenSetting error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void changeVPSStatus(String dataSourceName, int uuid, boolean enable) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		String preSql = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			preSql = "update t_vps_open_info set fStatus = ? where fUuid = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setBoolean(1, enable);
			ps.setInt(2, uuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void resetVPSStatus(String dataSourceName, String uuids, String stage) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		String preSql = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			if("open".equals(stage)) {
				preSql = "update t_vps_open_info set fOpenStatus = null, fStartOpenTime = null, fCompleteOpenTime = null, fSettingStatus = null, fStartSettingTime = NULL, fCompleteSettingTime = NULL where fUuid in (%s) ";
			}else{
				preSql = "update t_vps_open_info set fSettingStatus = null, fStartSettingTime = NULL, fCompleteSettingTime = NULL where fUuid in (%s) ";
			}
			ps = conn.prepareStatement(String.format(preSql, uuids));
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

}