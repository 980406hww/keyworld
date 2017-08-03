package com.keymanager.manager;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.util.FileUtil;
import com.keymanager.value.*;

import com.keymanager.db.DBUtil;
import com.keymanager.mail.MailHelper;
import com.keymanager.util.Utils;
import com.keymanager.util.VNCAddressBookParser;
import com.keymanager.util.ZipCompressor;
import com.keymanager.util.common.StringUtil;

public class ClientStatusManager {
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

	public void updatePageNo(String dsName, String clientID, int pageNo)throws
			Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);
			updatePageNo(conn, clientID, pageNo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void updatePageNo(Connection conn, String clientID, int pageNo) throws
			Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_client_status set fPageNo = ? where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setInt(1, pageNo);
			ps.setString(2, clientID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void addClientStatus(Connection conn, String terminalType, String clientID, String freeSpace, String version, String city) throws
			Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			if (!checkClientIDExist(conn, clientID, terminalType)){
				VPSOpenInfoManager vpsOpenInfoManager = new VPSOpenInfoManager();
				VPSOpenInfoVO vpsOpenInfoVO = vpsOpenInfoManager.getVPSOpenInfoVO(conn, clientID);
				int i = 1;
				if(vpsOpenInfoVO == null) {
					preSql = "insert into t_client_status(fTerminalType, fFreeSpace, fVersion, fCity, fClientID, fClientIDPrefix, fLastVisitTime, fLastSendNotificationTime, fRenewalDate, fCreateTime) " +
							"values(?,?,?,?,?,?, now(), null, DATE_ADD(CURRENT_DATE(), INTERVAL -1 DAY), now())";
					ps = conn.prepareStatement(preSql);
					ps.setString(i++, terminalType);
					ps.setDouble(i++, StringUtil.isNumeric(freeSpace) ? Double.parseDouble(freeSpace) : 0);
					ps.setString(i++, version);
					ps.setString(i++, city);
					ps.setString(i++, clientID);
					ps.setString(i++, Utils.removeDigital(clientID));
				}else{
					preSql = "insert into t_client_status(fTerminalType, fFreeSpace, fVersion, fCity, fClientID, fClientIDPrefix, fHost, fPort, fUserName, " +
							"fPassword, fVPSBackendSystemComputerID, fLastVisitTime, fLastSendNotificationTime, fRenewalDate, fCreateTime) " +
							"values(?,?,?,?,?,?,?,?,?,?,?,now(), null, DATE_ADD(CURRENT_DATE(), INTERVAL -1 DAY), now())";
					ps = conn.prepareStatement(preSql);
					ps.setString(i++, terminalType);
					ps.setDouble(i++, StringUtil.isNumeric(freeSpace) ? Double.parseDouble(freeSpace) : 0);
					ps.setString(i++, version);
					ps.setString(i++, city);
					ps.setString(i++, clientID);
					ps.setString(i++, Utils.removeDigital(clientID));
					ps.setString(i++, vpsOpenInfoVO.getVncHost());
					ps.setString(i++, vpsOpenInfoVO.getVncPort());
					ps.setString(i++, vpsOpenInfoVO.getVncUser());
					ps.setString(i++, vpsOpenInfoVO.getVncPassword());
					ps.setString(i++, vpsOpenInfoVO.getVpsUuid());
				}
			}
			ps.executeUpdate();
			sendNotification(conn, terminalType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void logClientVisitTime(Connection conn, String terminalType, String clientID, String status, String freeSpace, String version, String
			city, int updateCount) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			if (checkClientIDExist(conn, clientID, terminalType)){
				if(updateCount == 0) {
					preSql = "update t_client_status set fRestartOrderingTime = now(), fOptimizationStartDate = IF(fOptimizationStartDate IS NULL, CURRENT_DATE(),fOptimizationStartDate), fOptimizationTotalCount = fOptimizationTotalCount + 1, fLastVisitTime = now(), fContinuousFailCount = fContinuousFailCount + 1, fLastSendNotificationTime = null, fStatus = ?, fFreeSpace = ?, fVersion = ?, fCity = ? where fClientID = ? ";
				}else{
					preSql = "update t_client_status set fRestartOrderingTime = now(), fOptimizationStartDate = IF(fOptimizationStartDate IS NULL, CURRENT_DATE(),fOptimizationStartDate), fOptimizationTotalCount = fOptimizationTotalCount + 1, fOptimizationSucceedCount = fOptimizationSucceedCount + 1, fLastVisitTime = now(), fContinuousFailCount = 0, fLastSendNotificationTime = null, fStatus = ?, fFreeSpace = ?, fVersion = ?, fCity = ? where fClientID = ? ";
				}
				ps = conn.prepareStatement(preSql);
				int i = 1;
				ps.setString(i++, status);
				ps.setDouble(i++, StringUtil.isNumeric(freeSpace) ? Double.parseDouble(freeSpace) : 0);
				ps.setString(i++, version);
				ps.setString(i++, city);
				ps.setString(i++, clientID);
				ps.executeUpdate();
				sendNotification(conn, terminalType);
			}else{
				addClientStatus(conn, terminalType, clientID, freeSpace, version, city);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateClientVersion(String dsName, String terminalType, String clientID, String version) throws Exception {
		if(Utils.isNullOrEmpty(clientID) || Utils.isNullOrEmpty(version)){
			return;
		}
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);
			if (checkClientIDExist(conn, clientID, terminalType)){
				preSql = "update t_client_status set fVersion = ?, fRestartCount = 0, fRestartStatus = null where fClientID = ? ";
				ps = conn.prepareStatement(preSql);
				ps.setString(1, version);
				ps.setString(2, clientID);
				ps.executeUpdate();
			}
			sendNotification(conn, terminalType);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void updateLastSendNotificationTime(Connection conn, String clientID) throws Exception {
		if(Utils.isNullOrEmpty(clientID)){
			return;
		}
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "update t_client_status set fLastSendNotificationTime = now() where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, clientID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	private ClientStatusVO getClientStatusVO(Connection conn, ResultSet rs) throws Exception {
		ClientStatusVO clientStatusVO = new ClientStatusVO();
		clientStatusVO.setClientID(rs.getString("fClientID"));
		clientStatusVO.setClientIDPrefix(rs.getString("fClientIDPrefix"));
		clientStatusVO.setGroup(rs.getString("fGroup"));
		clientStatusVO.setVersion(rs.getString("fVersion"));
		clientStatusVO.setTargetVersion(rs.getString("fTargetVersion"));
		clientStatusVO.setHost(rs.getString("fHost"));
		clientStatusVO.setPort(rs.getString("fPort"));
		clientStatusVO.setUserName(rs.getString("fUserName"));
		clientStatusVO.setPassword(rs.getString("fPassword"));
		clientStatusVO.setPageNo(rs.getInt("fPageNo"));
		clientStatusVO.setTerminalType(rs.getString("fTerminalType"));
		clientStatusVO.setContinuousFailCount(rs.getInt("fContinuousFailCount"));
		clientStatusVO.setCity(rs.getString("fCity"));
		clientStatusVO.setOperationType(rs.getString("fOperationType"));
		clientStatusVO.setFreeSpace(rs.getDouble("fFreeSpace"));
		clientStatusVO.setLastVisitTime(rs.getTimestamp("fLastVisitTime"));
		BigDecimal zhanneiPercent = rs.getBigDecimal("fZhanneiPercent");
		if(zhanneiPercent != null) {
			clientStatusVO.setZhanneiPercent(zhanneiPercent.intValue());
		}
		BigDecimal dragPercent = rs.getBigDecimal("fDragPercent");
		if(dragPercent != null) {
			clientStatusVO.setDragPercent(rs.getInt("fDragPercent"));
		}
		clientStatusVO.setClearCookie(rs.getInt("fClearCookie"));
		BigDecimal baiduSemPercent = rs.getBigDecimal("fBaiduSemPercent");
		if(baiduSemPercent != null) {
			clientStatusVO.setBaiduSemPercent(baiduSemPercent.intValue());
		}
		BigDecimal kuaizhaoPercent = rs.getBigDecimal("fKuaizhaoPercent");
		if(kuaizhaoPercent != null) {
			clientStatusVO.setKuaizhaoPercent(kuaizhaoPercent.intValue());
		}
		BigDecimal multiBrowser = rs.getBigDecimal("fMultiBrowser");
		if(multiBrowser != null) {
			clientStatusVO.setMultiBrowser(multiBrowser.intValue());
		}
		clientStatusVO.setAllowSwitchGroup(rs.getInt("fAllowSwitchGroup"));
		clientStatusVO.setDisableStatistics(rs.getInt("fDisableStatistics"));
		clientStatusVO.setPage(rs.getInt("fPage"));
		BigDecimal pageSize = rs.getBigDecimal("fPageSize");
		if(pageSize != null) {
			clientStatusVO.setPageSize(pageSize.intValue());
		}
		clientStatusVO.setLastSendNotificationTime(rs.getTimestamp("fLastSendNotificationTime"));
		clientStatusVO.setRestartCount(rs.getInt("fRestartCount"));
		clientStatusVO.setRestartStatus(rs.getString("fRestartStatus"));
		clientStatusVO.setRestartTime(rs.getTimestamp("fRestartTime"));
		clientStatusVO.setVpsBackendSystemComputerID(rs.getString("fVPSBackendSystemComputerID"));
		clientStatusVO.setVpsBackendSystemPassword(rs.getString("fVPSBackendSystemPassword"));
		clientStatusVO.setRestartOrderingTime(rs.getTimestamp("fRestartOrderingTime"));
		clientStatusVO.setRenewalDate(rs.getTimestamp("fRenewalDate"));
		clientStatusVO.setOptimizationTotalCount(rs.getInt("fOptimizationTotalCount"));
		clientStatusVO.setOptimizationSucceedCount(rs.getInt("fOptimizationSucceedCount"));
		clientStatusVO.setStatus(rs.getString("fStatus"));


		clientStatusVO.setEntryPageMinCount(rs.getInt("fEntryPageMinCount"));
		clientStatusVO.setEntryPageMaxCount(rs.getInt("fEntryPageMaxCount"));
		clientStatusVO.setDisableVisitWebsite(rs.getInt("fDisableVisitWebsite"));
		clientStatusVO.setPageRemainMinTime(rs.getInt("fPageRemainMinTime"));
		clientStatusVO.setPageRemainMaxTime(rs.getInt("fPageRemainMaxTime"));
		clientStatusVO.setInputDelayMinTime(rs.getInt("fInputDelayMinTime"));
		clientStatusVO.setInputDelayMaxTime(rs.getInt("fInputDelayMaxTime"));
		clientStatusVO.setSlideDelayMinTime(rs.getInt("fSlideDelayMinTime"));
		clientStatusVO.setSlideDelayMaxTime(rs.getInt("fSlideDelayMaxTime"));

		clientStatusVO.setTitleRemainMinTime(rs.getInt("fTitleRemainMinTime"));
		clientStatusVO.setTitleRemainMaxTime(rs.getInt("fTitleRemainMaxTime"));
		clientStatusVO.setOptimizeKeywordCountPerIP(rs.getInt("fOptimizeKeywordCountPerIP"));
		clientStatusVO.setOneIPOneUser(rs.getInt("fOneIPOneUser"));
		clientStatusVO.setRandomlyClickNoResult(rs.getInt("fRandomlyClickNoResult"));
		clientStatusVO.setJustVisitSelfPage(rs.getInt("fJustVisitSelfPage"));
		clientStatusVO.setSleepPer2Words(rs.getInt("fSleepPer2Words"));
		clientStatusVO.setSupportPaste(rs.getInt("fSupportPaste"));
		clientStatusVO.setMoveRandomly(rs.getInt("fMoveRandomly"));
		clientStatusVO.setParentSearchEntry(rs.getInt("fParentSearchEntry"));

		clientStatusVO.setClearLocalStorage(rs.getInt("fClearLocalStorage"));
		clientStatusVO.setLessClickAtNight(rs.getInt("fLessClickAtNight"));
		clientStatusVO.setSameCityUser(rs.getInt("fSameCityUser"));
		clientStatusVO.setLocateTitlePosition(rs.getInt("fLocateTitlePosition"));
		clientStatusVO.setBaiduAllianceEntry(rs.getInt("fBaiduAllianceEntry"));
		clientStatusVO.setJustClickSpecifiedTitle(rs.getInt("fJustClickSpecifiedTitle"));
		clientStatusVO.setRandomlyClickMoreLink(rs.getInt("fRandomlyClickMoreLink"));
		clientStatusVO.setMoveUp20(rs.getInt("fMoveUp20"));
		clientStatusVO.setWaitTimeAfterOpenBaidu(rs.getInt("fWaitTimeAfterOpenBaidu"));
		clientStatusVO.setWaitTimeBeforeClick(rs.getInt("fWaitTimeBeforeClick"));
		clientStatusVO.setWaitTimeAfterClick(rs.getInt("fWaitTimeAfterClick"));
		clientStatusVO.setMaxUserCount(rs.getInt("fMaxUserCount"));
		clientStatusVO.setUpgradeFailedReason(rs.getString("fUpgradeFailedReason"));
		clientStatusVO.setValid(rs.getBoolean("fValid"));
		return clientStatusVO;
	}

	public void updateGroup(String dsName, String clientID, String group) throws Exception {
		Connection conn = null;
		try{
			conn = DBUtil.getConnection(dsName);
			updateGroup(conn, clientID, group);
		}finally {
			if(conn != null){
				DBUtil.closeConnection(conn);
			}
		}
	}

	public void updateUpgradeFailedReason(String dsName, String clientID, String upgradeFailedReason) throws Exception {
		Connection conn = null;
		try{
			conn = DBUtil.getConnection(dsName);
			updateUpgradeFailedReason(conn, clientID, upgradeFailedReason);
		}finally {
			if(conn != null){
				DBUtil.closeConnection(conn);
			}
		}
	}

	private void updateOperationType(Connection conn, String clientID, String operationType)
			throws Exception {
		if(Utils.isNullOrEmpty(clientID)){
			return;
		}
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_client_status set fOperationType = ? where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, operationType);
			ps.setString(2, clientID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	private void updateGroup(Connection conn, String clientID, String group)
			throws Exception {
		if(Utils.isNullOrEmpty(clientID)){
			return;
		}
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "update t_client_status set fGroup = ? where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, group);
			ps.setString(2, clientID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	private void updateUpgradeFailedReason(Connection conn, String clientID, String upgradeFailedReason)
			throws Exception {
		if(Utils.isNullOrEmpty(clientID)){
			return;
		}
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "update t_client_status set fUpgradeFailedReason = ? where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, upgradeFailedReason);
			ps.setString(2, clientID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateCleintStatusSetting(Connection conn, ClientStatusVO clientStatusVO)
			throws Exception {
		if(Utils.isNullOrEmpty(clientStatusVO.getClientID())){
			return;
		}
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "update t_client_status set fGroup = ?, fOperationType = ?, fPage = ?, fPageSize = ?,"
					+ "fZhanneiPercent = ?, fKuaizhaoPercent = ?, fBaiduSemPercent = ?, "
					+ "fDisableVisitWebsite = ?, fEntryPageMinCount = ?, "
					+ "fEntryPageMaxCount = ?, fPageRemainMinTime = ?, fPageRemainMaxTime = ?, fInputDelayMinTime = ?, fInputDelayMaxTime = ?, "
					+ "fSlideDelayMinTime = ?, fSlideDelayMaxTime = ?, fTitleRemainMinTime = ?, fTitleRemainMaxTime = ?, fOptimizeKeywordCountPerIP = ?, "
					+ "fOneIPOneUser = ?, fRandomlyClickNoResult = ?, fJustVisitSelfPage = ?, fSleepPer2Words = ?, fSupportPaste = ?, "
					+ "fMoveRandomly = ?, fParentSearchEntry = ?, fClearLocalStorage = ?, fLessClickAtNight = ?, fSameCityUser = ?, "
					+ "fLocateTitlePosition = ?, fBaiduAllianceEntry = ?, fJustClickSpecifiedTitle = ?, fRandomlyClickMoreLink = ?, fMoveUp20 = ?, "
					+ "fWaitTimeAfterOpenBaidu = ?, fWaitTimeBeforeClick = ?, fWaitTimeAfterClick = ?, fMaxUserCount = ?, "

					+ "fMultiBrowser = ?, fClearCookie = ?, fDragPercent = ?, fAllowSwitchGroup = ?, fDisableStatistics = ?, "
					+ "fHost = ?, fPort = ?, fUserName = ?, fPassword = ?, fVPSBackendSystemComputerID = ?, fVPSBackendSystemPassword = ? where fClientID = ? ";

			int i = 1;
			ps = conn.prepareStatement(preSql);
			ps.setString(i++, clientStatusVO.getGroup());
			ps.setString(i++, clientStatusVO.getOperationType());
			ps.setInt(i++, clientStatusVO.getPage());
			ps.setBigDecimal(i++, clientStatusVO.getPageSize() == null ? null : new BigDecimal(clientStatusVO.getPageSize()));
			ps.setBigDecimal(i++, clientStatusVO.getZhanneiPercent() == null ? null : new BigDecimal(clientStatusVO.getZhanneiPercent()));
			ps.setBigDecimal(i++, clientStatusVO.getKuaizhaoPercent() == null ? null : new BigDecimal(clientStatusVO.getKuaizhaoPercent()));
			ps.setBigDecimal(i++, clientStatusVO.getBaiduSemPercent() == null ? null : new BigDecimal(clientStatusVO.getBaiduSemPercent()));
			ps.setInt(i++, clientStatusVO.getDisableVisitWebsite());
			ps.setInt(i++, clientStatusVO.getEntryPageMinCount());

			ps.setInt(i++, clientStatusVO.getEntryPageMaxCount());
			ps.setInt(i++, clientStatusVO.getPageRemainMinTime());
			ps.setInt(i++, clientStatusVO.getPageRemainMaxTime());
			ps.setInt(i++, clientStatusVO.getInputDelayMinTime());
			ps.setInt(i++, clientStatusVO.getInputDelayMaxTime());

			ps.setInt(i++, clientStatusVO.getSlideDelayMinTime());
			ps.setInt(i++, clientStatusVO.getSlideDelayMaxTime());
			ps.setInt(i++, clientStatusVO.getTitleRemainMinTime());
			ps.setInt(i++, clientStatusVO.getTitleRemainMaxTime());
			ps.setInt(i++, clientStatusVO.getOptimizeKeywordCountPerIP());

			ps.setInt(i++, clientStatusVO.getOneIPOneUser());
			ps.setInt(i++, clientStatusVO.getRandomlyClickNoResult());
			ps.setInt(i++, clientStatusVO.getJustVisitSelfPage());
			ps.setInt(i++, clientStatusVO.getSleepPer2Words());
			ps.setInt(i++, clientStatusVO.getSupportPaste());

			ps.setInt(i++, clientStatusVO.getMoveRandomly());
			ps.setInt(i++, clientStatusVO.getParentSearchEntry());
			ps.setInt(i++, clientStatusVO.getClearLocalStorage());
			ps.setInt(i++, clientStatusVO.getLessClickAtNight());
			ps.setInt(i++, clientStatusVO.getSameCityUser());

			ps.setInt(i++, clientStatusVO.getLocateTitlePosition());
			ps.setInt(i++, clientStatusVO.getBaiduAllianceEntry());
			ps.setInt(i++, clientStatusVO.getJustClickSpecifiedTitle());
			ps.setInt(i++, clientStatusVO.getRandomlyClickMoreLink());
			ps.setInt(i++, clientStatusVO.getMoveUp20());

			ps.setInt(i++, clientStatusVO.getWaitTimeAfterOpenBaidu());
			ps.setInt(i++, clientStatusVO.getWaitTimeBeforeClick());
			ps.setInt(i++, clientStatusVO.getWaitTimeAfterClick());
			ps.setInt(i++, clientStatusVO.getMaxUserCount());

			ps.setInt(i++, clientStatusVO.getMultiBrowser());
			ps.setInt(i++, clientStatusVO.getClearCookie());
			ps.setBigDecimal(i++, clientStatusVO.getDragPercent() == null ? null : new BigDecimal(clientStatusVO.getDragPercent()));
			ps.setInt(i++, clientStatusVO.getAllowSwitchGroup());
			ps.setInt(i++, clientStatusVO.getDisableStatistics());
			ps.setString(i++, clientStatusVO.getHost());
			ps.setString(i++, clientStatusVO.getPort());
			ps.setString(i++, clientStatusVO.getUserName());
			ps.setString(i++, clientStatusVO.getPassword());
			ps.setString(i++, clientStatusVO.getVpsBackendSystemComputerID());
			ps.setString(i++, clientStatusVO.getVpsBackendSystemPassword());
			ps.setString(i++, clientStatusVO.getClientID());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateGroups(String dsName, String data) throws Exception {
		Connection conn = null;
		try{
			conn = DBUtil.getConnection(dsName);
			String[] pairs = data.replace("\"",  "").split("--row--");
			for(String pair : pairs){
				String[] elements = pair.split("=");
				updateGroup(conn, elements[0], elements[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void updateOperationType(String dsName, String data) throws Exception {
		Connection conn = null;
		try{
			conn = DBUtil.getConnection(dsName);
			String[] pairs = data.replace("\"", "").split("--row--");
			for(String pair : pairs){
				String[] elements = pair.split("=");
				updateOperationType(conn, elements[0], elements[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void updateClientStatus(String dsName, String data) throws Exception {
		Connection conn = null;
		try{
			conn = DBUtil.getConnection(dsName);
			ObjectMapper mapper = new ObjectMapper();
			ClientStatusVO clientStatusVO = (ClientStatusVO)mapper.readValue(data, ClientStatusVO.class);
			updateCleintStatusSetting(conn, clientStatusVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void updateClientStatusTargetVersion(String dsName, String data) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		String preSql = null;
		try{
			conn = DBUtil.getConnection(dsName);
			ObjectMapper mapper = new ObjectMapper();
			ClientStatusForUpdateTargetVersion clientStatusForUpdateTargetVersion = (ClientStatusForUpdateTargetVersion)mapper.readValue(data, ClientStatusForUpdateTargetVersion.class);
			preSql = String.format("update t_client_status set fTargetVersion = ? where fClientID IN (%s)", clientStatusForUpdateTargetVersion.getClientIDs());
			ps = conn.prepareStatement(preSql);
			ps.setString(1, clientStatusForUpdateTargetVersion.getTargetVersion());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void updateRenewalDate(String dsName, String data) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		String preSql = null;
		try{
			conn = DBUtil.getConnection(dsName);
			ObjectMapper mapper = new ObjectMapper();
			ClientStatusForUpdateRenewalDate clientStatusForUpdateRenewalDate = (ClientStatusForUpdateRenewalDate)mapper.readValue(data,
					ClientStatusForUpdateRenewalDate.class);
			if("increaseOneMonth".equals(clientStatusForUpdateRenewalDate.getSettingType())){
				preSql = String.format("update t_client_status set fRenewalDate = DATE_ADD(fRenewalDate, INTERVAL 1 MONTH) where fClientID IN (%s)",
						clientStatusForUpdateRenewalDate.getClientIDs());
				ps = conn.prepareStatement(preSql);
			}else {
				preSql = String.format("update t_client_status set fRenewalDate = ? where fClientID IN (%s)", clientStatusForUpdateRenewalDate.getClientIDs());
				ps = conn.prepareStatement(preSql);
				ps.setTimestamp(1, Utils.string2Timestamp(clientStatusForUpdateRenewalDate.getRenewalDate()));
			}
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}


	public boolean checkClientIDExist(Connection conn, String clientID, String terminalType) throws Exception {
		ClientStatusVO clientStatusVO = getClientStatusByClientID(conn, clientID, terminalType);
		return clientStatusVO != null;
	}

	public String getClientGroup(String dsName, String terminalType, String clientID) throws Exception {
		Connection conn = null;
		ClientStatusVO clientStatusVO = null;
		try{
			conn = DBUtil.getConnection(dsName);
			clientStatusVO = getClientStatusByClientID(conn, clientID, terminalType);
			if(clientStatusVO == null){
				addClientStatus(conn, terminalType, clientID, "500", null, null);
			}
		}finally {
			if(conn != null){
				DBUtil.closeConnection(conn);
			}
		}
		return clientStatusVO == null ? null : clientStatusVO.getGroup();
	}

	public ClientStatusVO getClientStatusByClientID(Connection conn, String clientID, String terminalType) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		ClientStatusVO clientStatusVO = null;

		try {
			sql = " select * from t_client_status  where fClientID = ? and fTerminalType = ? ";

			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setString(1, clientID);
			ps.setString(2, terminalType);
			rs = ps.executeQuery();

			if (rs.next()) {
				clientStatusVO = getClientStatusVO(conn, rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search Customer Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
		return clientStatusVO;
	}

	public String getClientStatusByClientID(String datasourceName, String clientID, String terminalType) throws Exception{
		Connection conn = null;
		String returnStr = "";
		try{
			conn = DBUtil.getConnection(datasourceName);
			ClientStatusVO clientStatusVO = this.getClientStatusByClientID(conn, clientID, terminalType);
			ObjectMapper mapper = new ObjectMapper();
			returnStr = mapper.writeValueAsString(clientStatusVO);
		}finally {
			if(conn != null){
				DBUtil.closeConnection(conn);
			}
		}
		return returnStr;
	}

	public String getClientStatusTargetVersion(String datasourceName, String clientID, String terminalType) throws Exception{
		Connection conn = null;
		String returnStr = "";
		try {
			conn = DBUtil.getConnection(datasourceName);
			ClientStatusVO clientStatusVO = this.getClientStatusByClientID(conn, clientID, terminalType);
			if (clientStatusVO.getTargetVersion() != null) {
				if (!clientStatusVO.getTargetVersion().equals(clientStatusVO.getVersion())) {
					returnStr = clientStatusVO.getTargetVersion();
				}
			}
		}finally {
			if(conn != null){
				DBUtil.closeConnection(conn);
			}
		}
		return returnStr;
	}

	public List<ClientStatusVO> searchClientStatusVOs(String dsName, int pageSize, int curPage, String condition, String order,
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

		ArrayList<ClientStatusVO> clientStatusVOs = new ArrayList<ClientStatusVO>();
		try {
			conn = DBUtil.getConnection(dsName);

			if (recCount != 0) {
				sql = " select count(1) as recordCount from t_client_status  where 1=1 " + condition;

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

			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_client_status where 1=1 " + condition + " " + order;
			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			while (rs.next()) {
				clientStatusVOs.add(getClientStatusVO(conn, rs));
			}
			return clientStatusVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Search Client Status Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public List<ClientStatusVO> getClientStatusVOs(Connection conn, String type, String groupName, String customerName) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = " SELECT DISTINCT cs.* FROM t_customer_keyword ck, t_customer c, t_client_status cs "
					+ " WHERE ck.fType = ? AND cs.fValid = 1 AND ck.fCustomerUuid = c.fUuid AND ck.fOptimizeGroupName = cs.fGroup ";
			if (StringUtil.isNotNullNorEmpty(groupName)) {
				sql = sql + " AND ck.fOptimizeGroupName like '%" + groupName.trim() + "%' ";
			}
			if (StringUtil.isNotNullNorEmpty(customerName)) {
				sql = sql + " AND c.fContactPerson like '%" + customerName.trim() + "%' ";
			}
			sql =   sql
					+ " ORDER BY ck.fOptimizeGroupName ";
			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setString(1, type);
			rs = ps.executeQuery();
			List<ClientStatusVO> clientStatusVOs = new ArrayList<ClientStatusVO>();
			while (rs.next()) {
				clientStatusVOs.add(getClientStatusVO(conn, rs));
			}
			return clientStatusVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getClientStatusVOs");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void sendNotification(Connection conn, String terminalType) throws Exception{
		ConfigManager configManager = new ConfigManager();
		ConfigVO notificationEmail = configManager.getConfig(conn, "NotificationEmail", "EmailAddress");
		String condition = " and fTerminalType = '" + terminalType + "' and fValid = 1 and "
				+ " ((fRestartCount > 5) or (fFreeSpace > 0 and fFreeSpace < 100) or (fLastVisitTime < DATE_ADD(NOW(), INTERVAL -15 MINUTE))) ORDER" +
				" BY fLastSendNotificationTime DESC ";
		List<ClientStatusVO> clientStatusVOs = searchClientStatusVOs(conn, condition);
		if(!Utils.isEmpty(clientStatusVOs)){
			boolean alreadySent = false;
			Timestamp now = Utils.getCurrentTimestamp();
			Timestamp fifteenMinutesAgo = Utils.addMinutes(now, -15);
			StringBuilder sb = new StringBuilder();
			for(ClientStatusVO clientStatusVO : clientStatusVOs){
				if(clientStatusVO.getLastSendNotificationTime() != null && clientStatusVO.getLastSendNotificationTime().after(fifteenMinutesAgo)){
					alreadySent = true;
					break;
				}
			}
			if(!alreadySent){
				sb.append("<table><tr><td>客户端ID</td><td>最后访问时间</td></tr>");
				for(ClientStatusVO clientStatusVO : clientStatusVOs){
					sb.append(String.format("<tr><td>%s</td><td>%s</td>", clientStatusVO.getClientID(), Utils.formatDatetime(clientStatusVO.getLastVisitTime(), "yyyy-MM-dd HH:mm")));
					updateLastSendNotificationTime(conn, clientStatusVO.getClientID());
				}
				sb.append("</table>");
				String[] emailAddresses = notificationEmail.getValue().split(";");
				for(String emailAddress : emailAddresses) {
					MailHelper.sendClientDownNotification(emailAddress, sb.toString(), terminalType + " 下面的客户端有效空间少于100M，快去重启");
				}
			}
		}
	}


	public void sendNotificationForRenewal() throws Exception{
		Connection pcConn = null;
		Connection phoneConn = null;
		ConfigVO notificationEmail = null;
		List<ClientStatusVO> clientStatusVOs = new ArrayList<ClientStatusVO>();
		try {
			pcConn = DBUtil.getConnection("keyword");
			String condition = " and DATE_ADD(fRenewalDate, INTERVAL -3 DAY ) < NOW() and fValid = 1 ORDER BY fRenewalDate ";
			clientStatusVOs.addAll(searchClientStatusVOs(pcConn, condition));

			ConfigManager configManager = new ConfigManager();
			notificationEmail = configManager.getConfig(pcConn, "NotificationEmail", "EmailAddress");
		}catch (Exception ex){
			ex.printStackTrace();
			throw new Exception("sendNotificationForRenewal error");
		}finally {
			DBUtil.closeConnection(pcConn);
			DBUtil.closeConnection(phoneConn);
		}
		if(!Utils.isEmpty(clientStatusVOs)){
			StringBuilder sb = new StringBuilder();
			sb.append("<table><tr><td>客户端ID</td><td>续费日期</td></tr>");
			for(ClientStatusVO clientStatusVO : clientStatusVOs){
				sb.append(String.format("<tr><td>%s</td><td>%s</td>", clientStatusVO.getClientID(), Utils.formatDatetime(clientStatusVO.getRenewalDate(),
						"yyyy-MM-dd")));
			}
			sb.append("</table>");
			String[] emailAddresses = notificationEmail.getValue().split(";");
			for(String emailAddress : emailAddresses) {
				MailHelper.sendClientDownNotification(emailAddress, sb.toString(), "续费通知");
			}
		}
	}

	public List<ClientStatusVO> searchClientStatusVOs(String dsName, String condition) throws Exception {
		Connection conn = DBUtil.getConnection(dsName);
		try {
			List<ClientStatusVO> clientStatusVOs = searchClientStatusVOs(conn, condition);
			return clientStatusVOs;
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public List<ClientStatusVO> searchClientStatusVOs(Connection conn, String condition) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		ArrayList<ClientStatusVO> clientStatusVOs = new ArrayList<ClientStatusVO>();
		try {

			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_client_status where 1=1 " + condition + " ";

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			while (rs.next()) {
				clientStatusVOs.add(getClientStatusVO(conn, rs));
			}
			return clientStatusVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search client status Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public int getMaxSequence(Connection conn, String clientIDPrefix) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = String.format("SELECT MAX(REPLACE(cs.fClientID, '%s', '')) max_sequence FROM t_client_status cs WHERE cs.fClientIDPrefix " +
					"= '%s'" , clientIDPrefix, clientIDPrefix);

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getInt("max_sequence");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getMaxSequence Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
		return 0;
	}

	public List<ClientStatusSummaryVO> getClientStatusSummary(String dataSourceName) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			List<ClientStatusSummaryVO> pcClientStatusSummaryVOs = this.getClientStatusSummary(conn);
			Collections.sort(pcClientStatusSummaryVOs);
			ClientStatusSummaryVO previousClientIDPrefix = null;
			ClientStatusSummaryVO previousType = null;
			for(ClientStatusSummaryVO clientStatusSummaryVO : pcClientStatusSummaryVOs){
				if(previousClientIDPrefix == null){
					previousClientIDPrefix = clientStatusSummaryVO;
					previousClientIDPrefix.setClientIDPrefixCount(previousClientIDPrefix.getClientIDPrefixCount() + 1);
					previousClientIDPrefix.setClientIDPrefixTotalCount(previousClientIDPrefix.getClientIDPrefixTotalCount() +
							clientStatusSummaryVO.getCount());
				}else if(previousClientIDPrefix.getClientIDPrefix().equals(clientStatusSummaryVO.getClientIDPrefix())){
					previousClientIDPrefix.setClientIDPrefixCount(previousClientIDPrefix.getClientIDPrefixCount() + 1);
					previousClientIDPrefix.setClientIDPrefixTotalCount(previousClientIDPrefix.getClientIDPrefixTotalCount() +
							clientStatusSummaryVO.getCount());
				}else{
					previousClientIDPrefix = clientStatusSummaryVO;
					previousClientIDPrefix.setClientIDPrefixCount(previousClientIDPrefix.getClientIDPrefixCount() + 1);
					previousClientIDPrefix.setClientIDPrefixTotalCount(previousClientIDPrefix.getClientIDPrefixTotalCount() +
							clientStatusSummaryVO.getCount());

					previousType = null;
				}

				if(previousType == null){
					previousType = clientStatusSummaryVO;
					previousType.setTypeCount(previousType.getTypeCount() + 1);
					previousType.setTypeTotalCount(previousType.getTypeTotalCount() +
							clientStatusSummaryVO.getCount());
				}else if(previousType.getType().equals(clientStatusSummaryVO.getType())){
					previousType.setTypeCount(previousType.getTypeCount() + 1);
					previousType.setTypeTotalCount(previousType.getTypeTotalCount() +
							clientStatusSummaryVO.getCount());
				}else{
					previousType = clientStatusSummaryVO;
					previousType.setTypeCount(previousType.getTypeCount() + 1);
					previousType.setTypeTotalCount(previousType.getTypeTotalCount() +
							clientStatusSummaryVO.getCount());
				}
			}
			return pcClientStatusSummaryVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getClientStatusSummary error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
	}

	public List<ClientStatusSummaryVO> getClientStatusSummary(Connection conn) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT cs.fClientIDPrefix, cs.fTerminalType, cs.fCity, COUNT(1) subCount FROM t_client_status cs WHERE cs.fValid = 1 GROUP BY cs.fClientIDPrefix, cs.fTerminalType, cs.fCity ORDER BY cs.fClientIDPrefix";

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			List<ClientStatusSummaryVO> clientStatusSummaryVOs = new ArrayList<ClientStatusSummaryVO>();
			while (rs.next()) {
				ClientStatusSummaryVO clientStatusSummaryVO = new ClientStatusSummaryVO();
				clientStatusSummaryVO.setClientIDPrefix(rs.getString("fClientIDPrefix"));
				clientStatusSummaryVO.setCity(rs.getString("fCity"));
				clientStatusSummaryVO.setCount(rs.getInt("subCount"));
				clientStatusSummaryVO.setType(rs.getString("fTerminalType"));
				clientStatusSummaryVOs.add(clientStatusSummaryVO);
			}
			return clientStatusSummaryVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getMaxSequence Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public List<ClientStatusGroupSummaryVO> getClientStatusGroupSummary(String dataSourceName) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			return this.getClientStatusGroupSummary(conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getClientStatusGroupSummary error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public List<ClientStatusGroupSummaryVO> getClientStatusGroupSummary(Connection conn) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = "SELECT cs.fGroup, cs.fTerminalType, COUNT(1) AS subCount FROM t_client_status cs GROUP BY cs.fGroup, cs.fTerminalType ORDER BY cs.fGroup";

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			List<ClientStatusGroupSummaryVO> clientStatusGroupSummaryVOs = new ArrayList<ClientStatusGroupSummaryVO>();
			while (rs.next()) {
				ClientStatusGroupSummaryVO clientStatusGroupSummaryVO = new ClientStatusGroupSummaryVO();
				clientStatusGroupSummaryVO.setGroup(rs.getString("fGroup"));
				clientStatusGroupSummaryVO.setTerminalType(rs.getString("fTerminalType"));
				clientStatusGroupSummaryVO.setCount(rs.getInt("subCount"));
				clientStatusGroupSummaryVOs.add(clientStatusGroupSummaryVO);
			}
			return clientStatusGroupSummaryVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getClientStatusGroupSummary Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void changeClientStatus(String dataSourceName, String clientID, boolean valid) throws Exception {
		if(Utils.isNullOrEmpty(clientID)){
			return;
		}
		PreparedStatement ps = null;
		Connection conn = null;
		String preSql = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			preSql = "update t_client_status set fValid = ? where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setBoolean(1, valid);
			ps.setString(2, clientID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void deleteClientStatus(String dataSourceName, String clientID) throws Exception {
		if(Utils.isNullOrEmpty(clientID)){
			return;
		}
		PreparedStatement ps = null;
		Connection conn = null;
		String preSql = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			preSql = "delete from t_client_status where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, clientID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void deleteClientStatuses(String dataSourceName, String clientIDs) throws Exception {
		if(Utils.isNullOrEmpty(clientIDs)){
			return;
		}
		PreparedStatement ps = null;
		Connection conn = null;
		String preSql = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			preSql = String.format("delete from t_client_status where fClientID IN (%s)", clientIDs);
			ps = conn.prepareStatement(preSql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void updateVNCInfo(Connection conn, ClientStatusVO clientStatusVO) throws Exception {
		if(clientStatusVO == null || Utils.isNullOrEmpty(clientStatusVO.getClientID())){
			return;
		}
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_client_status set fHost = ?, fPort = ?, fPassword = ?, fVPSBackendSystemComputerID = ? where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, clientStatusVO.getHost());
			ps.setString(2, clientStatusVO.getPort());
			ps.setString(3, clientStatusVO.getPassword());
			ps.setString(4, clientStatusVO.getVpsBackendSystemComputerID());
			ps.setString(5, clientStatusVO.getClientID());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void writeTxtFile(ClientStatusVO clientStatusVO) throws Exception {
		RandomAccessFile mm = null;
		FileOutputStream o = null;
		try {
			Utils.createDir(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vnc/");
			String fileName = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vnc/" + clientStatusVO.getClientID() + ".vnc";
			o = new FileOutputStream(fileName);
			o.write("[Connection]".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write(String.format("Host=%s", clientStatusVO.getHost()).getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write(String.format("Port=%s", clientStatusVO.getPort()).getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write(String.format("Username=%s", clientStatusVO.getUserName()).getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("Password=8e587919308fcab0c34af756358b9053".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("[Options]".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("UseLocalCursor=1".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("UseDesktopResize=1".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("FullScreen=0".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("FullColour=0".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("LowColourLevel=1".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("PreferredEncoding=ZRLE".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("AutoSelect=1".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("Shared=0".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("SendPtrEvents=1".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("SendKeyEvents=1".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("SendCutText=1".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("AcceptCutText=1".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("Emulate3=0".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("PointerEventInterval=0".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("Monitor=".getBytes("UTF-8"));
			o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("MenuKey=F8".getBytes("UTF-8"));
			o.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mm != null) {
				mm.close();
			}
		}
	}

	public void parseVNCAddressBook(InputStream inputStream, String datasourceName)
			throws Exception {
		if(inputStream != null){
			Connection conn = null;
			try {
				VNCAddressBookParser vncAddressBookParser = new VNCAddressBookParser();
				Map<String, String> vncInfoMap = vncAddressBookParser.extractVNCInfo(inputStream);
				conn = DBUtil.getConnection(datasourceName);
				List<ClientStatusVO> clientStatusVOs = searchClientStatusVOs(conn, "");
				List<ClientStatusVO> changedClientStatusVOs = new ArrayList<ClientStatusVO>();
				for (ClientStatusVO clientStatusVO : clientStatusVOs) {
					String vncInfo = vncInfoMap.get(clientStatusVO.getClientID());
					if (!Utils.isNullOrEmpty(vncInfo)) {
						String vncInfos[] = vncInfo.split(":");
						if (vncInfos.length == 3) {
							clientStatusVO.setHost(vncInfos[0]);
							clientStatusVO.setPort(vncInfos[1]);
							clientStatusVO.setVpsBackendSystemComputerID(vncInfos[2]);
						} else {
							System.out.println(vncInfo);
						}
						updateVNCInfo(conn, clientStatusVO);
						writeTxtFile(clientStatusVO);
						changedClientStatusVOs.add(clientStatusVO);
					}
				}
			}finally {
				if(conn != null){
					DBUtil.closeConnection(conn);
				}
			}
		}
	}

	public String downloadVNCInfo(String datasourceName) throws Exception {
		Connection conn = null;
		String zipFileName = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			List<ClientStatusVO> clientStatusVOs = searchClientStatusVOs(conn, " and (fHost is not null or fHost <> '') ");
			for (ClientStatusVO clientStatusVO : clientStatusVOs) {
				updateVNCInfo(conn, clientStatusVO);
				writeTxtFile(clientStatusVO);
			}
			String path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
			zipFileName = path + "vnc.zip";
			ZipCompressor.zipMultiFile(path + "vnc/", zipFileName);
		}catch (Exception ex){
			ex.printStackTrace();
			throw new Exception("downloadVNCInfo error");
		}finally {
			DBUtil.closeConnection(conn);
		}
		return zipFileName;
	}

	public String getStoppedClientStatuses(String datasourceName, String terminalType) throws Exception {
		String returnStr = "";
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			ClientStatusVO tmpClientStatusVO = null;
			CustomerKeywordManager customerKeywordManager = new CustomerKeywordManager();

			String sql = " and (DATE_ADD(fLastVisitTime, INTERVAL 10 MINUTE) < NOW() and fValid = 1 " +
					" and (fRestartStatus = 'restarting' AND DATE_ADD(fRestartTime, INTERVAL 3 MINUTE) < NOW())" +
					" and (fHost is not null and fHost <> '')) order by fRestartOrderingTime ";
			List<ClientStatusVO> clientStatusVOs = searchClientStatusVOs(conn, sql);
			for(ClientStatusVO clientStatusVO : clientStatusVOs){
				if(customerKeywordManager.haveCustomerKeywordsForOptimization(conn, clientStatusVO.getClientID(), terminalType, false)){
					tmpClientStatusVO = clientStatusVO;
					updateRestartStatus(conn, "Logging", tmpClientStatusVO.getClientID());
					break;
				}
			}
			if(tmpClientStatusVO == null) {
				sql = " AND (fContinuousFailCount > 5 OR DATE_ADD(fLastVisitTime, INTERVAL IF((10 > (fPageNo*3)), 10, (fPageNo * 3)) MINUTE) < NOW()) " +
						" AND (fRestartTime IS NULL OR DATE_ADD(fRestartTime, INTERVAL 10 MINUTE) < NOW()) " +
						" AND fValid = 1 " +
						" and (fHost is not null and fHost <> '') order by fRestartOrderingTime";
				clientStatusVOs = searchClientStatusVOs(conn, sql);
				for(ClientStatusVO clientStatusVO : clientStatusVOs){
					if(customerKeywordManager.haveCustomerKeywordsForOptimization(conn, clientStatusVO.getClientID(), terminalType, false)){
						tmpClientStatusVO = clientStatusVO;
						updateRestartStatus(conn, "Processing", tmpClientStatusVO.getClientID());
						break;
					}
				}
			}
			if(tmpClientStatusVO != null) {
				ClientStatusVOListJson clientStatusVOListJson = new ClientStatusVOListJson();
				List<ClientStatusVO> clientStatusVOList = new ArrayList<ClientStatusVO>();
				clientStatusVOList.add(tmpClientStatusVO);
				clientStatusVOListJson.setClientStatusVOs(clientStatusVOList);
				ObjectMapper mapper = new ObjectMapper();
				returnStr = mapper.writeValueAsString(clientStatusVOListJson);
			}
		}finally {
			if(conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
		return  returnStr;
	}

	private void updateRestartStatus(Connection conn, String restartStatus, String clientID) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_client_status set fRestartTime = now(), fRestartOrderingTime = now(), fRestartStatus = ? where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, restartStatus);
			ps.setString(2, clientID);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void resetRestartStatusForProcessing(String datasourceName) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			preSql = "UPDATE t_client_status SET fRestartStatus = NULL WHERE (fRestartStatus = 'Processing' or fRestartStatus = 'Logging') ";
			ps = conn.prepareStatement(preSql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			if(conn != null) {
				DBUtil.closeConnection(conn);
			}
		}
	}

	public void updateClientStatusRestartCount(String dataSourceName, String terminalType, String clientID, String status) throws Exception {
		String content = String.format("\r\n%s ------ terminalType = %s, clientID = %s, status = %s", Utils.formatDatetime(Utils
						.getCurrentTimestamp(),
				"yyyy-MM-dd HH:mm:ss"), terminalType, clientID, status);
		FileUtil.writeTxtFile(content, "utf-8", Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() +
				"/updateRestartingStatus.log");
		if(Utils.isNullOrEmpty(clientID)){
			return;
		}
		PreparedStatement ps = null;
		Connection conn = null;
		String preSql = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			conn.setAutoCommit(false);
			ClientStatusVO clientStatusVO = this.getClientStatusByClientID(conn, clientID, terminalType);
			preSql = "update t_client_status set fRestartTime = now(), fRestartOrderingTime = now(), fRestartCount = fRestartCount + 1, fRestartStatus = ? " +
					"where fClientID = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, status);
			ps.setString(2, clientID);
			ps.executeUpdate();
			ClientStatusRestartLogVO clientStatusRestartLogVO = new ClientStatusRestartLogVO();
			clientStatusRestartLogVO.setClientID(clientID);
			clientStatusRestartLogVO.setGroup(clientStatusVO.getGroup());
			clientStatusRestartLogVO.setRestartCount(clientStatusVO.getRestartCount() + 1);
			clientStatusRestartLogVO.setRestartStatus(status);
			ClientStatusRestartLogManager clientStatusRestartLogManager = new ClientStatusRestartLogManager();
			clientStatusRestartLogManager.addClientStatusResultLogVO(conn, clientStatusRestartLogVO);
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void switchClientStatusInfo(ClientStatusVO sourceClientStatusVO, ClientStatusVO targetClientStatusVO){
		Integer baiduSemPercent = sourceClientStatusVO.getBaiduSemPercent();
		sourceClientStatusVO.setBaiduSemPercent(targetClientStatusVO.getBaiduSemPercent());
		targetClientStatusVO.setBaiduSemPercent(baiduSemPercent);

		int clearCookie = sourceClientStatusVO.getClearCookie();
		sourceClientStatusVO.setClearCookie(targetClientStatusVO.getClearCookie());
		targetClientStatusVO.setClearCookie(clearCookie);

		Integer dragPercent = sourceClientStatusVO.getDragPercent();
		sourceClientStatusVO.setDragPercent(targetClientStatusVO.getDragPercent());
		targetClientStatusVO.setDragPercent(dragPercent);

		Integer kuaizhaoPercent = sourceClientStatusVO.getKuaizhaoPercent();
		sourceClientStatusVO.setKuaizhaoPercent(targetClientStatusVO.getKuaizhaoPercent());
		targetClientStatusVO.setKuaizhaoPercent(kuaizhaoPercent);

		Integer multiBrowser = sourceClientStatusVO.getMultiBrowser();
		sourceClientStatusVO.setMultiBrowser(targetClientStatusVO.getMultiBrowser());
		targetClientStatusVO.setMultiBrowser(multiBrowser);

		String operationType = sourceClientStatusVO.getOperationType();
		sourceClientStatusVO.setOperationType(targetClientStatusVO.getOperationType());
		targetClientStatusVO.setOperationType(operationType);

		int page = sourceClientStatusVO.getPage();
		sourceClientStatusVO.setPage(targetClientStatusVO.getPage());
		targetClientStatusVO.setPage(page);

		Integer pageSize = sourceClientStatusVO.getPageSize();
		sourceClientStatusVO.setPageSize(targetClientStatusVO.getPageSize());
		targetClientStatusVO.setPageSize(pageSize);

		Integer zhanneiPercent = sourceClientStatusVO.getZhanneiPercent();
		sourceClientStatusVO.setZhanneiPercent(targetClientStatusVO.getZhanneiPercent());
		targetClientStatusVO.setZhanneiPercent(zhanneiPercent);

		String group = sourceClientStatusVO.getGroup();
		sourceClientStatusVO.setGroup(targetClientStatusVO.getGroup());
		targetClientStatusVO.setGroup(group);

		int disableStatistics = sourceClientStatusVO.getDisableStatistics();
		sourceClientStatusVO.setDisableStatistics(targetClientStatusVO.getDisableStatistics());
		targetClientStatusVO.setDisableStatistics(disableStatistics);

		int disableVisiteWebsite = sourceClientStatusVO.getDisableVisitWebsite();
		sourceClientStatusVO.setDisableVisitWebsite(targetClientStatusVO.getDisableVisitWebsite());
		targetClientStatusVO.setDisableVisitWebsite(disableVisiteWebsite);

		int entryPageMinCount = sourceClientStatusVO.getEntryPageMinCount();
		sourceClientStatusVO.setEntryPageMinCount(targetClientStatusVO.getEntryPageMinCount());
		targetClientStatusVO.setEntryPageMinCount(entryPageMinCount);

		int entryPageMaxCount = sourceClientStatusVO.getEntryPageMaxCount();
		sourceClientStatusVO.setEntryPageMaxCount(targetClientStatusVO.getEntryPageMaxCount());
		targetClientStatusVO.setEntryPageMaxCount(entryPageMaxCount);

		int disableVisitWebsite = sourceClientStatusVO.getDisableVisitWebsite();
		sourceClientStatusVO.setDisableVisitWebsite(targetClientStatusVO.getDisableVisitWebsite());
		targetClientStatusVO.setDisableVisitWebsite(disableVisitWebsite);

		int pageRemainMinTime = sourceClientStatusVO.getPageRemainMinTime();
		sourceClientStatusVO.setPageRemainMinTime(targetClientStatusVO.getPageRemainMinTime());
		targetClientStatusVO.setPageRemainMinTime(pageRemainMinTime);

		int pageRemainMaxTime = sourceClientStatusVO.getPageRemainMaxTime();
		sourceClientStatusVO.setPageRemainMaxTime(targetClientStatusVO.getPageRemainMaxTime());
		targetClientStatusVO.setPageRemainMaxTime(pageRemainMaxTime);

		int inputDelayMinTime = sourceClientStatusVO.getInputDelayMinTime();
		sourceClientStatusVO.setInputDelayMinTime(targetClientStatusVO.getInputDelayMinTime());
		targetClientStatusVO.setInputDelayMinTime(inputDelayMinTime);

		int inputDelayMaxTime = sourceClientStatusVO.getInputDelayMaxTime();
		sourceClientStatusVO.setInputDelayMaxTime(targetClientStatusVO.getInputDelayMaxTime());
		targetClientStatusVO.setInputDelayMaxTime(inputDelayMaxTime);

		int slideDelayMinTime = sourceClientStatusVO.getSlideDelayMinTime();
		sourceClientStatusVO.setSlideDelayMinTime(targetClientStatusVO.getSlideDelayMinTime());
		targetClientStatusVO.setSlideDelayMinTime(slideDelayMinTime);

		int slideDelayMaxTime = sourceClientStatusVO.getSlideDelayMaxTime();
		sourceClientStatusVO.setSlideDelayMaxTime(targetClientStatusVO.getSlideDelayMaxTime());
		targetClientStatusVO.setSlideDelayMaxTime(slideDelayMaxTime);

		int titleRemainMinTime = sourceClientStatusVO.getTitleRemainMinTime();
		sourceClientStatusVO.setTitleRemainMinTime(targetClientStatusVO.getTitleRemainMinTime());
		targetClientStatusVO.setTitleRemainMinTime(titleRemainMinTime);

		int titleRemainMaxTime = sourceClientStatusVO.getTitleRemainMaxTime();
		sourceClientStatusVO.setTitleRemainMaxTime(targetClientStatusVO.getTitleRemainMaxTime());
		targetClientStatusVO.setTitleRemainMaxTime(titleRemainMaxTime);

		int optimizeKeywordCountPerIP = sourceClientStatusVO.getOptimizeKeywordCountPerIP();
		sourceClientStatusVO.setOptimizeKeywordCountPerIP(targetClientStatusVO.getOptimizeKeywordCountPerIP());
		targetClientStatusVO.setOptimizeKeywordCountPerIP(optimizeKeywordCountPerIP);

		int oneIPOneUser = sourceClientStatusVO.getOneIPOneUser();
		sourceClientStatusVO.setOneIPOneUser(targetClientStatusVO.getOneIPOneUser());
		targetClientStatusVO.setOneIPOneUser(oneIPOneUser);

		int randomlyClickNoResult = sourceClientStatusVO.getRandomlyClickNoResult();
		sourceClientStatusVO.setRandomlyClickNoResult(targetClientStatusVO.getRandomlyClickNoResult());
		targetClientStatusVO.setRandomlyClickNoResult(randomlyClickNoResult);

		int justVisitSelfPage = sourceClientStatusVO.getJustVisitSelfPage();
		sourceClientStatusVO.setJustVisitSelfPage(targetClientStatusVO.getJustVisitSelfPage());
		targetClientStatusVO.setJustVisitSelfPage(justVisitSelfPage);

		int sleepPer2Words = sourceClientStatusVO.getSleepPer2Words();
		sourceClientStatusVO.setSleepPer2Words(targetClientStatusVO.getSleepPer2Words());
		targetClientStatusVO.setSleepPer2Words(sleepPer2Words);

		int supportPaste = sourceClientStatusVO.getSupportPaste();
		sourceClientStatusVO.setSupportPaste(targetClientStatusVO.getSupportPaste());
		targetClientStatusVO.setSupportPaste(supportPaste);

		int moveRandomly = sourceClientStatusVO.getMoveRandomly();
		sourceClientStatusVO.setMoveRandomly(targetClientStatusVO.getMoveRandomly());
		targetClientStatusVO.setMoveRandomly(moveRandomly);

		int parentSearchEntry = sourceClientStatusVO.getParentSearchEntry();
		sourceClientStatusVO.setParentSearchEntry(targetClientStatusVO.getParentSearchEntry());
		targetClientStatusVO.setParentSearchEntry(parentSearchEntry);

		int clearLocalStorage = sourceClientStatusVO.getClearLocalStorage();
		sourceClientStatusVO.setClearLocalStorage(targetClientStatusVO.getClearLocalStorage());
		targetClientStatusVO.setClearLocalStorage(clearLocalStorage);

		int lessClickAtNight = sourceClientStatusVO.getLessClickAtNight();
		sourceClientStatusVO.setLessClickAtNight(targetClientStatusVO.getLessClickAtNight());
		targetClientStatusVO.setLessClickAtNight(lessClickAtNight);

		int sameCityUser = sourceClientStatusVO.getSameCityUser();
		sourceClientStatusVO.setSameCityUser(targetClientStatusVO.getSameCityUser());
		targetClientStatusVO.setSameCityUser(sameCityUser);

		int locateTitlePosition = sourceClientStatusVO.getLocateTitlePosition();
		sourceClientStatusVO.setLocateTitlePosition(targetClientStatusVO.getLocateTitlePosition());
		targetClientStatusVO.setLocateTitlePosition(locateTitlePosition);

		int baiduAllianceEntry = sourceClientStatusVO.getBaiduAllianceEntry();
		sourceClientStatusVO.setBaiduAllianceEntry(targetClientStatusVO.getBaiduAllianceEntry());
		targetClientStatusVO.setBaiduAllianceEntry(baiduAllianceEntry);

		int justClickSpecifiedTitle = sourceClientStatusVO.getJustClickSpecifiedTitle();
		sourceClientStatusVO.setJustClickSpecifiedTitle(targetClientStatusVO.getJustClickSpecifiedTitle());
		targetClientStatusVO.setJustClickSpecifiedTitle(justClickSpecifiedTitle);

		int randomlyClickMoreLink = sourceClientStatusVO.getRandomlyClickMoreLink();
		sourceClientStatusVO.setRandomlyClickMoreLink(targetClientStatusVO.getRandomlyClickMoreLink());
		targetClientStatusVO.setRandomlyClickMoreLink(randomlyClickMoreLink);

		int moveUp20 = sourceClientStatusVO.getMoveUp20();
		sourceClientStatusVO.setMoveUp20(targetClientStatusVO.getMoveUp20());
		targetClientStatusVO.setMoveUp20(moveUp20);

		int waitTimeAfterOpenBaidu = sourceClientStatusVO.getWaitTimeAfterOpenBaidu();
		sourceClientStatusVO.setWaitTimeAfterOpenBaidu(targetClientStatusVO.getWaitTimeAfterOpenBaidu());
		targetClientStatusVO.setWaitTimeAfterOpenBaidu(waitTimeAfterOpenBaidu);

		int waitTimeBeforeClick = sourceClientStatusVO.getWaitTimeBeforeClick();
		sourceClientStatusVO.setWaitTimeBeforeClick(targetClientStatusVO.getWaitTimeBeforeClick());
		targetClientStatusVO.setWaitTimeBeforeClick(waitTimeBeforeClick);

		int waitTimeAfterClick = sourceClientStatusVO.getWaitTimeAfterClick();
		sourceClientStatusVO.setWaitTimeAfterClick(targetClientStatusVO.getWaitTimeAfterClick());
		targetClientStatusVO.setWaitTimeAfterClick(waitTimeAfterClick);

		int maxUserCount = sourceClientStatusVO.getMaxUserCount();
		sourceClientStatusVO.setMaxUserCount(targetClientStatusVO.getMaxUserCount());
		targetClientStatusVO.setMaxUserCount(maxUserCount);

	}
}