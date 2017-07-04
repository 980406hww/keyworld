package com.keymanager.manager;

import com.keymanager.db.DBUtil;
import com.keymanager.value.VPSOpenInfoVO;
import com.keymanager.value.VPSProviderInfoVO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class VPSProviderInfoManager {
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

	public List<VPSProviderInfoVO> getAllVPSProviderInfoVOs(String dsName) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		ArrayList<VPSOpenInfoVO> vpsOpenInfoVOs = new ArrayList<VPSOpenInfoVO>();
		try {
			conn = DBUtil.getConnection(dsName);
			sql = " SELECT * FROM t_vps_provider_info ORDER BY fCity ";
			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			List<VPSProviderInfoVO> vpsProviderInfoVOs = new ArrayList<VPSProviderInfoVO>();
			while (rs.next()) {
				VPSProviderInfoVO vpsProviderInfoVO = this.getVPSProviderInfoVO(rs);
				vpsProviderInfoVOs.add(vpsProviderInfoVO);
			}
			Collections.sort(vpsProviderInfoVOs);
			return vpsProviderInfoVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getAllVPSProviderInfoVOs Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public String getAllVPSProviderInfoString(String dsName) throws Exception {
		List<VPSProviderInfoVO> vpsProviderInfoVOs = this.getAllVPSProviderInfoVOs(dsName);
		Map<String, List<VPSProviderInfoVO>> groupProviderInfoMap = new HashMap<String, List<VPSProviderInfoVO>>();
		for(VPSProviderInfoVO vpsProviderInfoVO : vpsProviderInfoVOs){
			List<VPSProviderInfoVO> tmpVPSProviderInfoVOs = groupProviderInfoMap.get(vpsProviderInfoVO.categoryName());
			if(tmpVPSProviderInfoVOs == null){
				tmpVPSProviderInfoVOs = new ArrayList<VPSProviderInfoVO>();
				groupProviderInfoMap.put(vpsProviderInfoVO.categoryName(), tmpVPSProviderInfoVOs);
			}
			tmpVPSProviderInfoVOs.add(vpsProviderInfoVO);
		}
		StringBuilder stringBuilder = new StringBuilder();
		List<String> keys = new ArrayList<String>(groupProviderInfoMap.keySet());
		Collections.sort(keys);
		for(String optGroup : keys){
			stringBuilder.append("<optgroup label=\"" + optGroup + "\">");
			List<VPSProviderInfoVO> tmpVPSProviderInfoVOs = groupProviderInfoMap.get(optGroup);
			for(VPSProviderInfoVO vpsProviderInfoVO : tmpVPSProviderInfoVOs){
				stringBuilder.append("<option value=\"" + vpsProviderInfoVO.getUuid() + "\">" + vpsProviderInfoVO.optionName() + "</option>");
			}
			stringBuilder.append("</optgroup>");
		}
		return stringBuilder.toString();
	}

	public VPSProviderInfoVO getVPSProviderInfoVOById(Connection conn, int id) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from t_vps_provider_info vpi where vpi.fuuid = ?";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setInt(1, id);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return getVPSProviderInfoVO(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getVPSProviderInfoVOById error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}
		return null;
	}

	private VPSProviderInfoVO getVPSProviderInfoVO(ResultSet rs) throws Exception {
		VPSProviderInfoVO vpsOpenInfoVO = new VPSProviderInfoVO();
		vpsOpenInfoVO.setUuid(rs.getInt("fUuid"));
		vpsOpenInfoVO.setCityName(rs.getString("fCity"));
		vpsOpenInfoVO.setClientIDPrefix(rs.getString("fClientIDPrefix"));
		vpsOpenInfoVO.setMaxSequence(rs.getInt("fMaxSequence"));
		vpsOpenInfoVO.setPrice(rs.getInt("fPrice"));
		vpsOpenInfoVO.setDistrict(rs.getString("fDistrict"));
		vpsOpenInfoVO.setId(rs.getInt("fId"));
		return vpsOpenInfoVO;
	}

	public void updateMaxSequence(Connection conn, String clientIDPrefix, int maxSequence) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_vps_provider_info SET fMaxSequence = ? WHERE fClientIDPrefix = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setInt(1, maxSequence);
			ps.setString(2, clientIDPrefix);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}
}