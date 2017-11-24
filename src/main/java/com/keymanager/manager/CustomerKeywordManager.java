package com.keymanager.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.db.DBUtil;
import com.keymanager.enums.CollectMethod;
import com.keymanager.enums.CustomerKeywordStatus;
import com.keymanager.enums.KeywordType;
import com.keymanager.monitoring.excel.operator.AbstractExcelReader;
import com.keymanager.monitoring.service.CaptureRealUrlService;
import com.keymanager.monitoring.vo.CustomerKeywordForOptimization;
import com.keymanager.util.*;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;

public class CustomerKeywordManager {
	private int recordCount;
	private int currPage = 0;
	private int pageCount = 0;

	public int getRecordCount() {
		return recordCount;
	}

	public int getCurrPage() {
		return currPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public ArrayList searchCustomerKeywordAssociations(String datasourceName, int pageSize, int curPage,
													   String condition, String order, int recCount) throws Exception {
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

		ArrayList customerKeywords = new ArrayList();
		try {
			conn = DBUtil.getConnection(datasourceName);
			if (recCount != 0) {
				sql = " select count(1) as recordCount FROM t_customer c, t_customer_keyword ck where c.fUuid = ck.fCustomerUuid "
						+ condition;

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

			String sqlFields = " c.fContactPerson, ck.* ";

			sql = " select " + sqlFields + " FROM t_customer c, t_customer_keyword ck where c.fUuid = ck.fCustomerUuid "
					+ condition + " " + order;
			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				CustomerKeywordVO customerKeyword = getCustomerKeyword(conn, rs);
				customerKeyword.setContactPerson(rs.getString("fContactPerson"));
				customerKeywords.add(customerKeyword);
			}
			return customerKeywords;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}


	public String getGroups(String dataSourceName) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		List<String> groups = new ArrayList<String>();
		try {
			conn = DBUtil.getConnection(dataSourceName);
			sql = "SELECT DISTINCT ck.fOptimizeGroupName FROM t_customer_keyword ck WHERE ck.fStatus = 1 AND ck.fOptimizeGroupName IS NOT NULL AND ck.fOptimizeGroupName <> ''";
			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				groups.add(rs.getString("fOptimizeGroupName"));
			}
			ObjectMapper mapper = new ObjectMapper();
			return mapper.writeValueAsString(groups);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public ArrayList searchCustomerKeywords(String dataSourceName, int pageSize, int curPage, String condition,
											String order, int recCount) throws Exception {
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

		ArrayList customerKeywords = new ArrayList();
		try {
			conn = DBUtil.getConnection(dataSourceName);
			if (recCount != 0) {
				sql = " select count(1) as recordCount from t_customer_keyword ck where 1=1 " + condition;

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

			sql = " select " + sqlFields + " from t_customer_keyword ck where 1=1 " + condition + " " + order;
			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				CustomerKeywordVO customerKeyword = getCustomerKeyword(conn, rs);
				customerKeywords.add(customerKeyword);
			}
			return customerKeywords;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public int getCustomerKeywordCount(Connection conn, int customerUuid) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			sql = " select count(1) as recordCount from t_customer_keyword  where fCustomerUuid = ?";

			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setInt(1, customerUuid);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("recordCount");
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public boolean haveDuplicatedCustomerKeyword(Connection conn, String terminalType, int customerUuid, String keyword, String originalUrl) throws
			Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = " select * from t_customer_keyword where fKeyword = ? and fTerminalType = ? and fCustomerUuid = ? and ((fOriginalUrl <> '' and " +
					"fOriginalUrl like " +
					"?)) ";

			ps = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			ps.setString(i++, keyword);
			ps.setString(i++, terminalType);
			ps.setInt(i++, customerUuid);
			ps.setString(i++, "%" + originalUrl);

			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("haveDuplicatedCustomerKeyword error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public List searchCustomerKeywords(String datasourceName, String condition, String order) throws Exception {
		Connection conn = null;

		try {
			conn = DBUtil.getConnection(datasourceName);
			return searchCustomerKeywords(conn, condition, order);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public List<CustomerKeywordVO> searchCustomerKeywords(Connection conn, String condition, String order) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<CustomerKeywordVO> customerKeywords = new ArrayList<CustomerKeywordVO>();
		String sql;
		String sqlFields = " *  ";

		sql = " select " + sqlFields + " from t_customer_keyword where 1 = 1 ";
		sql = sql + condition + order;

		try{
			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				CustomerKeywordVO customerKeyword = getCustomerKeyword(conn, rs);
				customerKeywords.add(customerKeyword);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
		return customerKeywords;
	}

	public List<String> getCustomerUuids(Connection conn, String fTerminalType, String group) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> customerUuids = new ArrayList<String>();
		String sql = "SELECT DISTINCT fCustomerUuid FROM t_customer_keyword WHERE fTerminalType = ? AND fOptimizeGroupName = ? AND fStatus = 1";

		try{
			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				customerUuids.add(rs.getString("fCustomerUuid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getCustomerUuids");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
		return customerUuids;
	}

	public int getCustomerKeywordCurrentIndexCount(Connection conn, int uuid) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			sql = "select fCurrentIndexCount from t_customer_keyword where fUuid = ? ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setInt(1, uuid);

			rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("fCurrentIndexCount");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}

		return 0;
	}

	public CustomerKeywordVO getCustomerKeywordByUuid(String dataSourceName, String uuid) throws Exception {
		Connection conn = null;
		CustomerKeywordVO customerKeyword = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			customerKeyword = getCustomerKeywordByUuid(conn, uuid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeConnection(conn);
		}

		return customerKeyword;
	}

	public CustomerKeywordVO getCustomerKeywordByUuid(Connection conn, String uuid) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		CustomerKeywordVO customerKeyword = null;
		try {
			sql = " select ck.*, c.fContactPerson FROM t_customer c, t_customer_keyword ck where c.fUuid = ck.fCustomerUuid and ck.fUuid = ? ";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, uuid);

			rs = stmt.executeQuery();

			if (rs.next()) {
				customerKeyword = getCustomerKeyword(conn, rs);
				customerKeyword.setContactPerson(rs.getString("fContactPerson"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}
		return customerKeyword;
	}

	public List<KeywordPositionUrlVO> retrievePreviousNonBaiduWebsite(List<KeywordPositionUrlVO> keywordPositionUrlVOs,
																	  int position) {
		if (!Utils.isEmpty(keywordPositionUrlVOs)) {
			PreviousKeywordPositionCalculator calculator = new PreviousKeywordPositionCalculator();
			return calculator.calculate(keywordPositionUrlVOs, position);
		}
		return null;
	}

	public int fetchRemainingOptimizationCoount(Connection conn, String groupName, ConfigVO configVO) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			sql = "SELECT SUM(ck.fOptimizePlanCount - ck.fOptimizedCount) as remainingCount FROM t_customer_keyword ck WHERE ck.fOptimizeGroupName = ? "
					+ "  AND ck.fOptimizePlanCount > ck.fOptimizedCount AND ck.fInvalidRefreshCount < " + configVO.getValue();

			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setString(1, groupName);

			rs = ps.executeQuery();

			int remainingCount = 0;
			if (rs.next()) {
				remainingCount = rs.getInt("remainingCount");
			}
			return remainingCount;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("fetchRemainingOptimizationCoount");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public double resetBigKeywordIndicator(Connection conn, String groupName, ConfigVO configVO) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			cleanBigKeywordIndicator(conn, groupName);

			int remainingCount = fetchRemainingOptimizationCoount(conn, groupName, configVO);

			if (remainingCount == 0){
				return 0;
			}
			sql = "SELECT ck.fUuid, (ck.fOptimizePlanCount - ck.fOptimizedCount) AS remainingCount FROM t_customer_keyword ck WHERE ck.fOptimizeGroupName = ? "
					+ " AND ck.fOptimizePlanCount > ck.fOptimizedCount and ck.fInvalidRefreshCount < " + configVO.getValue()
					+ " ORDER BY (ck.fOptimizePlanCount - ck.fOptimizedCount) DESC";

			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setString(1, groupName);

			rs = ps.executeQuery();
			List<Integer> uuids = new ArrayList<Integer>();
			Map<Integer, Integer> remainingCountMap = new HashMap<Integer, Integer>();
			while (rs.next()) {
				int uuid = rs.getInt("fUuid");
				uuids.add(uuid);
				remainingCountMap.put(uuid, rs.getInt("remainingCount"));
			}

			int totalCount = uuids.size();
			int count = 0;
			int subTotal = 0;
			Set<Integer> bigKeywordUuids = new HashSet<Integer>();
			for(Integer uuid : uuids){
				subTotal = subTotal + remainingCountMap.get(uuid);
				bigKeywordUuids.add(uuid);
				count++;
				if((1.0 * count) / totalCount > 0.2){
					break;
				}
			}
			setBigKeywordIndicator(conn, bigKeywordUuids);
			return (subTotal*1.0) / remainingCount;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("resetBigKeywordIndicator");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void cleanInvalidRefreshCount(Connection conn) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_customer_keyword SET fInvalidRefreshCount = 0";
			ps = conn.prepareStatement(preSql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public List<String> getTypes(Connection conn, String groupName) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {

			sql = "SELECT distinct ck.fType as typeName FROM t_customer_keyword ck WHERE ck.fOptimizeGroupName = ? "
					+ "  AND ck.fStatus = 1";

			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setString(1, groupName);

			rs = ps.executeQuery();

			List types = new ArrayList<String>();
			if (rs.next()) {
				types.add(rs.getString("typeName"));
			}
			return types;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getTypes");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public String searchCustomerKeywordsForOptimization(String datasourceName, String terminalType, String clientID, String ip, boolean useHourRange,
														int retrycount) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		String returnValue = "";
		try {
			conn = DBUtil.getConnection(datasourceName);
			ClientStatusManager clientStatusManager = new ClientStatusManager();
			ClientStatusVO clientStatusVO = clientStatusManager.getClientStatusByClientID(conn, clientID, terminalType);
			if(clientStatusVO == null){
				clientStatusManager.addClientStatus(conn, terminalType, clientID, "500", null, null);
				return "No specify client";
			}
			clientStatusManager.updatePageNo(conn, clientID, 0);
			if(!clientStatusVO.isValid() || StringUtils.isEmpty(clientStatusVO.getGroup())){
				return clientStatusVO.getGroup();
			}
			List<String> types = getTypes(conn, clientStatusVO.getGroup());
			String typeName = "all";
			if(!Utils.isEmpty(types) && types.size() == 1){
				typeName = types.get(0);
			}
			ConfigManager configManager = new ConfigManager();
			ConfigVO configVO = configManager.getConfig(conn, ConfigManager.CONFIG_KEY_MAX_INVALID_COUNT, typeName);
			OptimizeDateManager optimizeDateManager = new OptimizeDateManager();
			if(!optimizeDateManager.checkOptimizeDateIsToday(conn)){
				optimizeDateManager.updateOptimizeDateAsToday(conn);
				cleanOptimizedCount(conn);
				cleanQueryCount(conn);
				cleanInvalidRefreshCount(conn);
			}
			String sqlFields = " * ";
			boolean anotherRound = false;
			KeywordFetchCountManager keywordFetchCountManager = new KeywordFetchCountManager();
			KeywordFetchCountVO keywordFetchCountVO = keywordFetchCountManager.getKeywordFetchCountVO(conn, clientStatusVO.getGroup());
			boolean operateBigKeyword = false;
			if(keywordFetchCountVO != null){
				//Normal Keyword
				if(keywordFetchCountVO.fetchNormalKeyword()){
					keywordFetchCountVO.setNormalKeywordFetchedCount(keywordFetchCountVO.getNormalKeywordFetchedCount() + 1);
				}else if(keywordFetchCountVO.fetchBigKeyword()){
					operateBigKeyword = true;
					keywordFetchCountVO.setBigKeywordFetchedCount(keywordFetchCountVO.getBigKeywordFetchedCount() + 1);
				}else{
					anotherRound = true;
				}
			}else{
				anotherRound = true;
				keywordFetchCountVO = keywordFetchCountManager.initKeywordFetchCountVO(conn, clientStatusVO.getGroup());
			}

			if(anotherRound){
				//Another round, normal keyword
				double bigKeywordPercentage = resetBigKeywordIndicator(conn, clientStatusVO.getGroup(), configVO);
				keywordFetchCountVO.setBigKeywordPercentage(bigKeywordPercentage);
				keywordFetchCountVO.setBigKeywordFetchedCount(0);
				keywordFetchCountVO.setNormalKeywordFetchedCount(1);
			}

			sql = "select " + sqlFields
					+ " from t_customer_keyword ck where ck.fOptimizeGroupName = ? and ck.fStatus = ? "
					+ " and (ck.fQueryTime is NULL or DATE_SUB(NOW(), INTERVAL ck.fQueryInterval MINUTE) > ck.fQueryTime) "
					+ " and ck.fUrl <> '' and ((ck.fCurrentPosition = 0 and ck.fInvalidRefreshCount < 2) or (ck.fCurrentPosition > 0 and ck" +
					".fInvalidRefreshCount <  " + configVO.getValue() + ")) ";
			if(useHourRange){
				sql = sql + " and ck.fOptimizedPercentage < ? ";
			}
			if(operateBigKeyword){
				sql = sql + " and ck.fBigKeyword = 1 " ;
			}
			sql = sql
					+ " and ((ck.fOptimizedCount < ck.fOptimizePlanCount) or (ck.fOptimizeDate is null) or (ck.fOptimizeDate < current_date())) "
					+ " order by ck.fQueryTime limit 1";



			keywordFetchCountManager.updateKeywordFetchCountVO(conn, keywordFetchCountVO);
			ps = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;

//			ps.setString(i++, ip);
			ps.setString(i++, clientStatusVO.getGroup().trim());
			ps.setInt(i++, CustomerKeywordStatus.Active.getCode());

			if(useHourRange){
				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				OptimizationHourPercentageManager optimizationHourPercentageManager = new OptimizationHourPercentageManager();
				OptimizationHourPercentageVO optimizationHourPercentageVO = optimizationHourPercentageManager.getOptimizationHourPercentageVO(conn, hour);
				ps.setDouble(i++, optimizationHourPercentageVO.getPercentage());
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				CustomerKeywordVO customerKeyword = getCustomerKeyword(conn, rs);
				CustomerKeywordForOptimization customerKeywordForOptimization = new CustomerKeywordForOptimization();
				customerKeywordForOptimization.setUuid(customerKeyword.getUuid());
				customerKeywordForOptimization.setKeyword(customerKeyword.getKeyword());
				customerKeywordForOptimization.setUrl(customerKeyword.getUrl());
				customerKeywordForOptimization.setEntryType(customerKeyword.getType());
				String relatedKeyword = "";

				Random rd = new Random();
				if (!Utils.isNullOrEmpty(customerKeyword.getRelatedKeywords())) {
					String[] keywords = customerKeyword.getRelatedKeywords().split(",");
					relatedKeyword = keywords[rd.nextInt(keywords.length)];
				}
				if (Utils.isNullOrEmpty(relatedKeyword)) {
					KeywordPositionUrlManager manager = new KeywordPositionUrlManager();

					relatedKeyword = manager.getKeywordPositionUrlVOString(datasourceName, customerKeyword.getSearchEngine(), null);
					customerKeywordForOptimization.setRelatedKeyword(relatedKeyword);
				}
				customerKeywordForOptimization.setCurrentPosition(customerKeyword.getCurrentPosition());
				customerKeywordForOptimization.setOriginalUrl(customerKeyword.getOriginalUrl());
				customerKeywordForOptimization.setTitle(customerKeyword.getTitle());

				KeywordExcludeTitleManager manager = new KeywordExcludeTitleManager();
				List<KeywordExcludeTitleVO> keywordExcludeTitleVOs = manager.searchKeywordExcludeTitleVOs(conn, customerKeyword.getKeyword());
				StringBuilder sbTitle = new StringBuilder();
				if(keywordExcludeTitleVOs != null){
					List<String> excludeTitles = new ArrayList<String>();
					customerKeywordForOptimization.setExcludeTitles(excludeTitles);
					for(KeywordExcludeTitleVO keywordExcludeTitleVO : keywordExcludeTitleVOs){
						excludeTitles.add(keywordExcludeTitleVO.getExcludeTitle());
					}
				}
				BaiduAdUrlManager baiduAdUrlManager = new BaiduAdUrlManager();
				String baiduAdUrl = baiduAdUrlManager.getBaiduAdUrl(conn);
				customerKeywordForOptimization.setBaiduAdUrl(baiduAdUrl);
				customerKeywordForOptimization.setGroup(clientStatusVO.getGroup());
				customerKeywordForOptimization.setOperationType(clientStatusVO.getOperationType());

				if("pc_pm_xiaowu".equals(customerKeyword.getOptimizeGroupName()) && (customerKeyword.getCurrentPosition() == 0 ||
						customerKeyword.getCurrentPosition() > 20)) {
					customerKeywordForOptimization.setPage(2);
				}else{
					customerKeywordForOptimization.setPage(clientStatusVO.getPage());
				}
				if(clientStatusVO.getPageSize() != null) {
					if("pc_pm_xiaowu".equals(customerKeyword.getOptimizeGroupName()) && (customerKeyword.getCurrentPosition() == 0 ||
							customerKeyword.getCurrentPosition() > 20)){
						customerKeywordForOptimization.setPageSize(50);
					}else {
						customerKeywordForOptimization.setPageSize(clientStatusVO.getPageSize());
					}
				}
				if(clientStatusVO.getZhanneiPercent() != null) {
					customerKeywordForOptimization.setZhanneiPercent(clientStatusVO.getZhanneiPercent());
				}
				if(clientStatusVO.getBaiduSemPercent() != null) {
					customerKeywordForOptimization.setBaiduSemPercent(clientStatusVO.getBaiduSemPercent());
				}
				customerKeywordForOptimization.setClearCookie(clientStatusVO.getClearCookie());
				if(clientStatusVO.getDragPercent() != null) {
					customerKeywordForOptimization.setDragPercent(clientStatusVO.getDragPercent());
				}
				if(clientStatusVO.getKuaizhaoPercent() != null) {
					customerKeywordForOptimization.setKuaizhaoPercent(clientStatusVO.getKuaizhaoPercent());
				}
				if(clientStatusVO.getMultiBrowser() != null) {
					customerKeywordForOptimization.setMultiBrowser(clientStatusVO.getMultiBrowser());
				}
				customerKeywordForOptimization.setOpenStatistics(clientStatusVO.getDisableStatistics() == 1 ? 0 : 1);
				customerKeywordForOptimization.setDisableStatistics(clientStatusVO.getDisableStatistics());
				customerKeywordForOptimization.setCurrentTime(Utils.formatDate(new Date(), Utils.TIME_FORMAT));

				customerKeywordForOptimization.setEntryPageMinCount(clientStatusVO.getEntryPageMinCount());
				customerKeywordForOptimization.setEntryPageMaxCount(clientStatusVO.getEntryPageMaxCount());
				customerKeywordForOptimization.setDisableVisitWebsite(clientStatusVO.getDisableVisitWebsite());
				customerKeywordForOptimization.setPageRemainMinTime(clientStatusVO.getPageRemainMinTime());
				customerKeywordForOptimization.setPageRemainMaxTime(clientStatusVO.getPageRemainMaxTime());
				customerKeywordForOptimization.setInputDelayMinTime(clientStatusVO.getInputDelayMinTime());
				customerKeywordForOptimization.setInputDelayMaxTime(clientStatusVO.getInputDelayMaxTime());
				customerKeywordForOptimization.setSlideDelayMinTime(clientStatusVO.getSlideDelayMinTime());
				customerKeywordForOptimization.setSlideDelayMaxTime(clientStatusVO.getSlideDelayMaxTime());
				customerKeywordForOptimization.setTitleRemainMinTime(clientStatusVO.getTitleRemainMinTime());
				customerKeywordForOptimization.setTitleRemainMaxTime(clientStatusVO.getTitleRemainMaxTime());
				customerKeywordForOptimization.setOptimizeKeywordCountPerIP(clientStatusVO.getOptimizeKeywordCountPerIP());
				customerKeywordForOptimization.setOneIPOneUser(clientStatusVO.getOneIPOneUser());
				customerKeywordForOptimization.setRandomlyClickNoResult(clientStatusVO.getRandomlyClickNoResult());
				customerKeywordForOptimization.setJustVisitSelfPage(clientStatusVO.getJustVisitSelfPage());
				customerKeywordForOptimization.setSleepPer2Words(clientStatusVO.getSleepPer2Words());
				customerKeywordForOptimization.setSupportPaste(clientStatusVO.getSupportPaste());
				customerKeywordForOptimization.setMoveRandomly(clientStatusVO.getMoveRandomly());
				customerKeywordForOptimization.setParentSearchEntry(clientStatusVO.getParentSearchEntry());
				customerKeywordForOptimization.setClearLocalStorage(clientStatusVO.getClearLocalStorage());
				customerKeywordForOptimization.setLessClickAtNight(clientStatusVO.getLessClickAtNight());
				customerKeywordForOptimization.setSameCityUser(clientStatusVO.getSameCityUser());
				customerKeywordForOptimization.setLocateTitlePosition(clientStatusVO.getLocateTitlePosition());
				customerKeywordForOptimization.setBaiduAllianceEntry(clientStatusVO.getBaiduAllianceEntry());
				customerKeywordForOptimization.setJustClickSpecifiedTitle(clientStatusVO.getJustClickSpecifiedTitle());
				customerKeywordForOptimization.setRandomlyClickMoreLink(clientStatusVO.getRandomlyClickMoreLink());
				customerKeywordForOptimization.setMoveUp20(clientStatusVO.getMoveUp20());
				customerKeywordForOptimization.setWaitTimeAfterOpenBaidu(clientStatusVO.getWaitTimeAfterOpenBaidu());
				customerKeywordForOptimization.setWaitTimeBeforeClick(clientStatusVO.getWaitTimeBeforeClick());
				customerKeywordForOptimization.setWaitTimeAfterClick(clientStatusVO.getWaitTimeAfterClick());
				customerKeywordForOptimization.setMaxUserCount(clientStatusVO.getMaxUserCount());

				updateQueryUpdateTime(conn, customerKeyword.getUuid());

				ObjectMapper mapper = new ObjectMapper();
				returnValue = mapper.writeValueAsString(customerKeywordForOptimization);
			}
			if("".equals(returnValue)){
				double bigKeywordPercentage = resetBigKeywordIndicator(conn, clientStatusVO.getGroup(), configVO);
				keywordFetchCountVO.setBigKeywordPercentage(bigKeywordPercentage);
				keywordFetchCountVO.setBigKeywordFetchedCount(0);
				keywordFetchCountVO.setNormalKeywordFetchedCount(1);
				keywordFetchCountManager.updateKeywordFetchCountVO(conn, keywordFetchCountVO);
				if(retrycount < 3) {
					returnValue = searchCustomerKeywordsForOptimization(datasourceName, terminalType, clientID, ip, useHourRange, ++retrycount);
				}
			}
			return returnValue;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error" + e.getMessage());
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public boolean haveCustomerKeywordsForOptimization(Connection conn, String clientID, String terminalType, boolean useHourRange) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		try {
			ClientStatusManager clientStatusManager = new ClientStatusManager();
			ClientStatusVO clientStatusVO = clientStatusManager.getClientStatusByClientID(conn, clientID, terminalType);
			if(clientStatusVO == null){
				return false;
			}
			if(!clientStatusVO.isValid()  || StringUtils.isEmpty(clientStatusVO.getGroup())){
				return false;
			}
			List<String> types = getTypes(conn, clientStatusVO.getGroup());
			String typeName = "all";
			if(!Utils.isEmpty(types) && types.size() == 1){
				typeName = types.get(0);
			}
			ConfigManager configManager = new ConfigManager();
			ConfigVO configVO = configManager.getConfig(conn, ConfigManager.CONFIG_KEY_MAX_INVALID_COUNT, typeName);
			OptimizeDateManager optimizeDateManager = new OptimizeDateManager();
			if(!optimizeDateManager.checkOptimizeDateIsToday(conn)){
				optimizeDateManager.updateOptimizeDateAsToday(conn);
				cleanOptimizedCount(conn);
				cleanQueryCount(conn);
				cleanInvalidRefreshCount(conn);
			}
			String sqlFields = " * ";
			boolean anotherRound = false;
			KeywordFetchCountManager keywordFetchCountManager = new KeywordFetchCountManager();
			KeywordFetchCountVO keywordFetchCountVO = keywordFetchCountManager.getKeywordFetchCountVO(conn, clientStatusVO.getGroup());
			boolean operateBigKeyword = false;
			if(keywordFetchCountVO != null){
				//Normal Keyword
				if(keywordFetchCountVO.fetchNormalKeyword()){
					keywordFetchCountVO.setNormalKeywordFetchedCount(keywordFetchCountVO.getNormalKeywordFetchedCount() + 1);
				}else if(keywordFetchCountVO.fetchBigKeyword()){
					operateBigKeyword = true;
					keywordFetchCountVO.setBigKeywordFetchedCount(keywordFetchCountVO.getBigKeywordFetchedCount() + 1);
				}else{
					anotherRound = true;
				}
			}else{
				anotherRound = true;
				keywordFetchCountVO = keywordFetchCountManager.initKeywordFetchCountVO(conn, clientStatusVO.getGroup());
			}

			if(anotherRound){
				//Another round, normal keyword
				double bigKeywordPercentage = resetBigKeywordIndicator(conn, clientStatusVO.getGroup(), configVO);
				keywordFetchCountVO.setBigKeywordPercentage(bigKeywordPercentage);
				keywordFetchCountVO.setBigKeywordFetchedCount(0);
				keywordFetchCountVO.setNormalKeywordFetchedCount(1);
			}

			sql = "select " + sqlFields
					+ " from t_customer_keyword ck where ck.fOptimizeGroupName = ? and ck.fStatus = ? "
					+ " and ck.fUrl <> '' and ((ck.fCurrentPosition = 0 and ck.fInvalidRefreshCount < 2) or (ck.fCurrentPosition > 0 and ck" +
					".fInvalidRefreshCount <  " + configVO.getValue() + ")) ";
			if(useHourRange){
				sql = sql + " and ck.fOptimizedPercentage < ? ";
			}
			if(operateBigKeyword){
				sql = sql + " and ck.fBigKeyword = 1 " ;
			}
			sql = sql
					+ " and ((ck.fOptimizedCount < ck.fOptimizePlanCount) or (ck.fOptimizeDate is null) or (ck.fOptimizeDate < current_date())) "
					+ " order by ck.fQueryTime limit 1";



			keywordFetchCountManager.updateKeywordFetchCountVO(conn, keywordFetchCountVO);
			ps = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			ps.setString(i++, clientStatusVO.getGroup().trim());
			ps.setInt(i++, CustomerKeywordStatus.Active.getCode());

			if(useHourRange){
				int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
				OptimizationHourPercentageManager optimizationHourPercentageManager = new OptimizationHourPercentageManager();
				OptimizationHourPercentageVO optimizationHourPercentageVO = optimizationHourPercentageManager.getOptimizationHourPercentageVO(conn, hour);
				ps.setDouble(i++, optimizationHourPercentageVO.getPercentage());
			}
			rs = ps.executeQuery();

			while (rs.next()) {
				CustomerKeywordVO customerKeyword = getCustomerKeyword(conn, rs);
				if(customerKeyword != null){
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public String searchCustomerKeywordsForClientCache(String datasourceName, String terminalType, String keyword) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		try {
			FumianListManager fumianListManager = new FumianListManager();
			List fumianList = fumianListManager.searchFumianListVO(datasourceName, 1000, 1, " and fTerminalType = '" + terminalType + "' and " +
					" fKeyword = '" + keyword.trim() + "'",	"",	0);
			StringBuilder sb = new StringBuilder(Constants.COLUMN_SPLITTOR);

			for(Object fumianListObj : fumianList) {
				FumianListVO fumianListVO = (FumianListVO)fumianListObj;
				sb.append(fumianListVO.getTitle());
				sb.append(Constants.COLUMN_SPLITTOR);
				sb.append(fumianListVO.getUrl());
				sb.append(Constants.COLUMN_SPLITTOR);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("searchCustomerKeywordsForClientCache error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public String fetchCustomerKeywordForAutoUpdateNegative(String dsName, String group) throws Exception
	{
		Connection conn = null;
		String returnStr = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		if(Utils.isNullOrEmpty(group)){
			sql = "SELECT DISTINCT ck.fKeyword, ck.fCustomerUuid, ck.fOptimizeGroupName FROM t_customer_keyword ck WHERE ck.fType = 'fm'AND " +
					"ck.fOptimizeGroupName LIKE 'pc_pm_laodu%' " +
					"AND ck.fStatus = 1 and (ck.fAutoUpdateNegativeDateTime is null or DATE_ADD(ck.fAutoUpdateNegativeDateTime, INTERVAL 3 MINUTE) < NOW()) ORDER BY ck.fAutoUpdateNegativeDateTime LIMIT 1";
		}else{
			sql = "SELECT DISTINCT ck.fKeyword, ck.fCustomerUuid, ck.fOptimizeGroupName FROM t_customer_keyword ck WHERE ck.fType = 'fm'AND " +
					"ck.fOptimizeGroupName = '" + group.trim() + "' AND ck.fStatus = 1 and (ck.fAutoUpdateNegativeDateTime is null or DATE_ADD(ck.fAutoUpdateNegativeDateTime, INTERVAL 3 MINUTE) < NOW())  " +
					" ORDER BY ck.fAutoUpdateNegativeDateTime LIMIT 1";
		}
		try
		{
			conn = DBUtil.getConnection(dsName);
			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			while (rs.next())
			{
				CustomerKeywordListJson customerKeywordListJson = new CustomerKeywordListJson();
				customerKeywordListJson.setKeyword(rs.getString("fKeyword"));
				customerKeywordListJson.setGroup(rs.getString("fOptimizeGroupName"));
				customerKeywordListJson.setCustomerUuid(rs.getInt("fCustomerUuid"));
				updateAutoUpdateNegativeTime(conn, customerKeywordListJson.getKeyword(), customerKeywordListJson
						.getGroup());
				ObjectMapper mapper = new ObjectMapper();
				returnStr = mapper.writeValueAsString(customerKeywordListJson);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("fetchCustomerKeywordForAutoUpdateNegative");
		}
		finally
		{
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
		return returnStr;
	}

	public String searchCustomerKeywordForCaptureTitle(String dsName, String terminalType, String groupName) throws Exception
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		try
		{
			conn = DBUtil.getConnection(dsName);
			sql = "SELECT * FROM t_customer_keyword ck WHERE ck.fTerminalType = ? and ck.fOptimizeGroupName = ? AND ck.fCapturedTitle = 0 AND ck.fCaptureTitleQueryTime IS NULL AND (ck.fTitle IS NULL OR ck.fTitle = '') limit 1";
			ps = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			ps.setString(i++, terminalType);
			ps.setString(i++, groupName);
			rs = ps.executeQuery();
			while (rs.next())
			{
				CustomerKeywordVO customerKeyword = getCustomerKeyword(conn, rs);
				CustomerKeywordForCaptureTitle captureTitle = new CustomerKeywordForCaptureTitle();
				captureTitle.setUuid(customerKeyword.getUuid());
				captureTitle.setKeyword(customerKeyword.getKeyword());
				captureTitle.setUrl(customerKeyword.getUrl());
				updateCaptureTitleQueryTime(conn, customerKeyword.getUuid());
				ObjectMapper mapper = new ObjectMapper();
				return mapper.writeValueAsString(captureTitle);
			}
			return "";
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		}
		finally
		{
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public CustomerKeywordForCaptureTitle searchCustomerKeywordForCaptureTitle(String dsName, String terminalType, String groupName,
																			   int customerUuid) throws Exception
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		try
		{
			conn = DBUtil.getConnection(dsName);
			sql = "SELECT * FROM t_customer_keyword ck WHERE ck.fTerminalType = ? and ck.fOptimizeGroupName = ? AND ck.fCustomerUuid = ? AND ck.fCapturedTitle = 0 AND ck.fCaptureTitleQueryTime IS NULL AND (ck.fTitle IS NULL OR ck.fTitle = '') LIMIT 1";
			ps = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			ps.setString(i++, terminalType);
			ps.setString(i++, groupName);
			ps.setInt(i++, customerUuid);

			rs = ps.executeQuery();
			while (rs.next())
			{
				CustomerKeywordVO customerKeyword = getCustomerKeyword(conn, rs);
				CustomerKeywordForCaptureTitle captureTitle = new CustomerKeywordForCaptureTitle();
				captureTitle.setUuid(customerKeyword.getUuid());
				captureTitle.setKeyword(customerKeyword.getKeyword());
				captureTitle.setUrl(customerKeyword.getUrl());
				updateCaptureTitleQueryTime(conn, customerKeyword.getUuid());
				return captureTitle;
			}
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		}
		finally
		{
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void updateCustomerKeywordForCaptureTitle(String datasourceName, String json) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		String sql = "";
		try
		{
			ObjectMapper mapper = new ObjectMapper();
			CustomerKeywordInfoJson customerKeywordInfoJson = (CustomerKeywordInfoJson)mapper.readValue(json, CustomerKeywordInfoJson.class);
			conn = DBUtil.getConnection(datasourceName);

			if(customerKeywordInfoJson.getUrl() == null){
				sql = "UPDATE t_customer_keyword SET fCapturedTitle = 1 WHERE fUuid = ?";
				ps = conn.prepareStatement(sql, 1003, 1007);
				ps.setInt(1, customerKeywordInfoJson.getUuid());
			}else {
				sql = "UPDATE t_customer_keyword SET fTitle = ?, fUrl = ?, fInitialPosition = ?, fCurrentPosition = ?, fCapturedTitle = 1 WHERE fUuid = ?";
				ps = conn.prepareStatement(sql, 1003, 1007);
				ps.setString(1, customerKeywordInfoJson.getTitle());
				ps.setString(2, customerKeywordInfoJson.getUrl());
				ps.setInt(3, customerKeywordInfoJson.getOrder());
				ps.setInt(4, customerKeywordInfoJson.getOrder());
				ps.setInt(5, customerKeywordInfoJson.getUuid());
			}
			ps.executeUpdate();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("updateCustomerKeywordForCaptureTitle error");
		}
		finally
		{
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public String searchCustomerKeywordForCaptureIndex(String dsName, int minutes, String type, int recCount) throws Exception
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		StringBuilder sb = new StringBuilder();
		try
		{
			conn = DBUtil.getConnection(dsName);

			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_customer_keyword ck WHERE ck.fUrl is not null and ck.fStatus = 1 and (ck.fCaptureIndexQueryTime is null or ck.fCaptureIndexQueryTime"
					+ " < ?) order by ck.fCaptureIndexQueryTime, fUpdateTime desc ";

			sql = sql + " limit " + recCount;

			ps = conn.prepareStatement(sql, 1003, 1007);
			Timestamp beforeNMinutes = new Timestamp(System.currentTimeMillis() - (minutes * 60 * 1000));
			ps.setTimestamp(1, beforeNMinutes);
			rs = ps.executeQuery();
			while (rs.next())
			{
				CustomerKeywordVO customerKeyword = getCustomerKeyword(conn, rs);
				if(sb.length() > 0){
					sb.append(Constants.ROW_SPLITTOR);
				}
				sb.append(customerKeyword.captureIndexString(type));
				updateCaptureIndexQueryTime(conn, type, customerKeyword.getUuid());
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		}
		finally
		{
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public String searchCustomerKeywordForCapturePosition(String dsName, int minutes, String optimizeGroupName, String customerName, String type, int recCount) throws Exception
	{
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		StringBuilder sb = new StringBuilder();
		try
		{
			conn = DBUtil.getConnection(dsName);

			String sqlFields = " ck.*  ";
			sql = " select " + sqlFields + " from t_customer_keyword ck ";
			if(!Utils.isNullOrEmpty(customerName)){
				sql = sql + " WHERE ck.fCustomerUuid = " + customerName.trim() + " and ";
			}else{
				sql = sql + " WHERE ";
			}

//      sql = sql + " ck.fUuid = 37559 and ";

			if(Utils.isNullOrEmpty(optimizeGroupName)){
				sql = sql + " ck.fOptimizeGroupName <> 'stop' and ck.fUrl "
						+ " is not null and ck.fStatus = 1 and (ck.fCapturePositionQueryTime"
						+ " is null or ck.fCapturePositionQueryTime < ?) order by ck.fCapturePositionQueryTime"
						+ " , fUpdateTime desc ";
			}else{
				String[] groupNames = optimizeGroupName.split(",");
				StringBuilder sbGroupName = new StringBuilder();
				for(String groupName : groupNames){
					if(sbGroupName.toString().equals("")){
						sbGroupName.append("'" + groupName + "'");
					}else{
						sbGroupName.append(", '" + groupName + "'");
					}
				}
				sql = sql + " ck.fOptimizeGroupName in (" + sbGroupName.toString() + ") and ck.fUrl "
						+ " is not null and ck.fStatus = 1 and (ck.fCapturePositionQueryTime"
						+ " is null or ck.fCapturePositionQueryTime < ?) order by ck.fCapturePositionQueryTime"
						+ " , fUpdateTime desc ";
			}

//      sql = " select " + sqlFields + " from t_customer_keyword ck WHERE ck.fUuid = 32128 ";
			sql = sql + " limit " + recCount;

			ps = conn.prepareStatement(sql, 1003, 1007);
			Timestamp beforeNMinutes = new Timestamp(System.currentTimeMillis() - (minutes * 60 * 1000));
			ps.setTimestamp(1, beforeNMinutes);
			rs = ps.executeQuery();
			while (rs.next())
			{
				CustomerKeywordVO customerKeyword = getCustomerKeyword(conn, rs);
				CustomerKeywordForCapturePosition customerKeywordForCapturePosition = new CustomerKeywordForCapturePosition();
				customerKeywordForCapturePosition.setUuid(new Long(customerKeyword.getUuid()));
				customerKeywordForCapturePosition.setKeyword(customerKeyword.getKeyword());
				customerKeywordForCapturePosition.setUrl(customerKeyword.getUrl());
				customerKeywordForCapturePosition.setTitle(customerKeyword.getTitle());

				ObjectMapper mapper = new ObjectMapper();
				sb.append(mapper.writeValueAsString(customerKeywordForCapturePosition));

				updateCapturePositionQueryTime(conn, type, customerKeyword.getUuid());
			}
			return sb.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		}
		finally
		{
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void updateCustomerKeywordPosition(String datasourceName, String data, String ip) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			conn.setAutoCommit(false);
			String[] keywordStrings = data.trim().split(Constants.ROW_SPLITTOR);
			for (String keywordString : keywordStrings) {
				if (!Utils.isNullOrEmpty(keywordString)) {
					String[] columns = keywordString.split(Constants.COLUMN_SPLITTOR);
					String customerKeywordUuid = columns[0];
					CustomerKeywordVO customerKeywordVO = getCustomerKeywordByUuid(conn, customerKeywordUuid);

					String type = "PC";
					int position = Integer.parseInt(columns[1]);

					updateCustomerKeywordPosition(conn, position, customerKeywordVO, type, ip);
				}
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	private CustomerKeywordVO getCustomerKeyword(Connection conn, ResultSet rs) throws Exception {
		CustomerKeywordVO customerKeyword = new CustomerKeywordVO();
		customerKeyword.setUuid(rs.getInt("fUuid"));
		customerKeyword.setCustomerUuid(rs.getInt("fCustomerUuid"));
		customerKeyword.setKeyword(rs.getString("fKeyword"));
		customerKeyword.setUrl(rs.getString("fUrl"));

		customerKeyword.setOriginalUrl(rs.getString("fOriginalUrl"));
		customerKeyword.setTerminalType(rs.getString("fTerminalType"));

		customerKeyword.setType(rs.getString("fType"));
		customerKeyword.setTitle(rs.getString("fTitle"));
		customerKeyword.setSnapshotDateTime(rs.getString("fSnapshotDateTime"));
		customerKeyword.setSearchEngine(rs.getString("fSearchEngine"));

		customerKeyword.setInitialIndexCount(rs.getInt("fInitialIndexCount"));
		customerKeyword.setInitialPosition(rs.getInt("fInitialPosition"));
		customerKeyword.setCurrentPosition(rs.getInt("fCurrentPosition"));


		customerKeyword.setCurrentIndexCount(rs.getInt("fCurrentIndexCount"));

		customerKeyword.setQueryTime(rs.getTimestamp("fQueryTime"));
		customerKeyword.setQueryDate(rs.getTimestamp("fQueryDate"));
		customerKeyword.setQueryCount(rs.getInt("fQueryCount"));
		customerKeyword.setQueryInterval(rs.getInt("fQueryInterval"));
		customerKeyword.setInvalidRefreshCount(rs.getInt("fInvalidRefreshCount"));

		customerKeyword.setSequence(rs.getInt("fSequence"));

		customerKeyword.setServiceProvider(rs.getString("fServiceProvider"));

		customerKeyword.setRelatedKeywords(rs.getString("fRelatedKeywords"));
		customerKeyword.setOptimizeGroupName(rs.getString("fOptimizeGroupName"));
		customerKeyword.setOptimizePlanCount(rs.getInt("fOptimizePlanCount"));
		customerKeyword.setOptimizedCount(rs.getInt("fOptimizedCount"));
		customerKeyword.setOptimizedPercentage(rs.getDouble("fOptimizedPercentage"));
		customerKeyword.setOptimizeDate(rs.getTimestamp("fOptimizeDate"));

		customerKeyword.setLastOptimizeDateTime(rs.getTimestamp("fLastOptimizeDateTime"));

		customerKeyword.setOptimizePositionFirstPercentage(rs.getInt("fOptimizePositionFirstPercentage"));
		customerKeyword.setOptimizePositionSecondPercentage(rs.getInt("fOptimizePositionSecondPercentage"));
		customerKeyword.setOptimizePositionThirdPercentage(rs.getInt("fOptimizePositionThirdPercentage"));

		customerKeyword.setPositionFirstCost(rs.getDouble("fPositionFirstCost"));
		customerKeyword.setPositionSecondCost(rs.getDouble("fPositionSecondCost"));
		customerKeyword.setPositionThirdCost(rs.getDouble("fPositionThirdCost"));
		customerKeyword.setPositionForthCost(rs.getDouble("fPositionForthCost"));
		customerKeyword.setPositionFifthCost(rs.getDouble("fPositionFifthCost"));

		customerKeyword.setPositionFirstFee(rs.getDouble("fPositionFirstFee"));
		customerKeyword.setPositionSecondFee(rs.getDouble("fPositionSecondFee"));
		customerKeyword.setPositionThirdFee(rs.getDouble("fPositionThirdFee"));
		customerKeyword.setPositionForthFee(rs.getDouble("fPositionForthFee"));
		customerKeyword.setPositionFifthFee(rs.getDouble("fPositionFifthFee"));
		customerKeyword.setPositionFirstPageFee(rs.getDouble("fPositionFirstPageFee"));

		customerKeyword.setCollectMethod(rs.getString("fCollectMethod"));

		customerKeyword.setStartOptimizedTime(rs.getTimestamp("fStartOptimizedTime"));
		customerKeyword.setEffectiveFromTime(rs.getTimestamp("fEffectiveFromTime"));
		customerKeyword.setEffectiveToTime(rs.getTimestamp("fEffectiveToTime"));

		customerKeyword.setPaymentEffectiveFromTime(rs.getTimestamp("fPaymentEffectiveFromTime"));
		customerKeyword.setPaymentEffectiveToTime(rs.getTimestamp("fPaymentEffectiveToTime"));

		customerKeyword.setStatus(rs.getInt("fStatus"));

		customerKeyword.setRemarks(rs.getString("fRemarks"));
		customerKeyword.setPaymentStatus(rs.getString("fPaymentStatus"));
		customerKeyword.setOrderNumber(rs.getString("fOrderNumber"));

		customerKeyword.setAutoUpdateNegativeTime(rs.getTimestamp("fAutoUpdateNegativeDateTime"));

		customerKeyword.setUpdateTime(rs.getTimestamp("fUpdateTime"));
		customerKeyword.setCreateTime(rs.getTimestamp("fCreateTime"));
//		CustomerKeywordAccountLogManager manager = new CustomerKeywordAccountLogManager();
//		customerKeyword.setReceiveStatus(manager.isNonPayAllCustomerKeywordAccountLogVO(conn, customerKeyword.getUuid()
//				+ ""));
		return customerKeyword;
	}

	public void saveCustomerKeywords(String datasourceName, String terminalType, String json) throws Exception{
		if(!Utils.isNullOrEmpty(json)){
			List<CustomerKeywordVO> customerKeywordVOs = convertToCustomerKeywords(json);
			if(!Utils.isEmpty(customerKeywordVOs)){
				for(CustomerKeywordVO customerKeywordVO : customerKeywordVOs){
					customerKeywordVO.setTerminalType(terminalType);
				}
				addCustomerKeywords(datasourceName, customerKeywordVOs);
			}
		}
	}

	private List<CustomerKeywordVO> convertToCustomerKeywords(String json) throws Exception {
		ObjectMapper mapper = new ObjectMapper(); // can reuse, share globally
		try{
			CustomerKeywordListJson customerKeywordListJson = (CustomerKeywordListJson)mapper.readValue(json, CustomerKeywordListJson.class);
			if(customerKeywordListJson != null){
				List<CustomerKeywordVO> customerKeywordVOs = new ArrayList<CustomerKeywordVO>();
				for(CustomerKeywordJson customerKeywordJson : customerKeywordListJson.getCapturedResults()){
					CustomerKeywordVO keywordVO = new CustomerKeywordVO();
					keywordVO.setCurrentPosition(customerKeywordJson.getOrder());
					keywordVO.setInitialPosition(customerKeywordJson.getOrder());
					keywordVO.setKeyword(customerKeywordJson.getKeyword());
					keywordVO.setOptimizeGroupName(customerKeywordListJson.getGroup());
					keywordVO.setOptimizePlanCount(customerKeywordJson.getClickCount());
					keywordVO.setStatus(1);
					keywordVO.setTitle(customerKeywordJson.getTitle());
					keywordVO.setType(customerKeywordJson.getType());
					keywordVO.setOriginalUrl(customerKeywordJson.getHref());
					keywordVO.setServiceProvider("baidutop123");
					keywordVO.setSearchEngine(Constants.SEARCH_ENGINE_BAIDU);
					keywordVO.setUrl(customerKeywordJson.getUrl());
					keywordVO.setStartOptimizedTime(Utils.getCurrentTimestamp());
					keywordVO.setCollectMethod(CollectMethod.PerMonth.getCode());
					keywordVO.setPositionFirstFee(0);
					keywordVO.setPositionSecondFee(0);
					keywordVO.setPositionThirdFee(0);
					keywordVO.setCurrentIndexCount(20);
					keywordVO.setCustomerUuid(customerKeywordListJson.getCustomerUuid());
					keywordVO.setAutoUpdateNegativeTime(Utils.getCurrentTimestamp());
					keywordVO.setCreateTime(Utils.getCurrentTimestamp());
					keywordVO.setUpdateTime(Utils.getCurrentTimestamp());
					customerKeywordVOs.add(keywordVO);
				}
				return customerKeywordVOs;
			}
		} catch (IOException e){
			e.printStackTrace();
			throw new Exception("convertToCustomerKeywords error", e);
		}
		return null;
	}

	public void updateCustomerKeywords(String datasourceName, String terminalType, String json) throws Exception{
		if(!Utils.isNullOrEmpty(json)){
			List<CustomerKeywordVO> customerKeywordVOs = convertToCustomerKeywords(json);
			if(!Utils.isEmpty(customerKeywordVOs)){
				for(CustomerKeywordVO customerKeywordVO : customerKeywordVOs){
					customerKeywordVO.setTerminalType(terminalType);
				}
				Connection conn = null;
				try {
					conn = DBUtil.getConnection(datasourceName);

					String keyword = customerKeywordVOs.get(0).getKeyword();
					String group = customerKeywordVOs.get(0).getOptimizeGroupName();
					FumianListManager fumianListManager = new FumianListManager();
					List fumianList = fumianListManager.searchFumianListVO(datasourceName, 1000, 1, " and fKeyword = '" + keyword.trim() + "'", "", 0);
					if (!Utils.isEmpty(fumianList)) {
						for (Object obj : fumianList) {
							FumianListVO fumianListVO = (FumianListVO) obj;
							CustomerKeywordVO matchedCustomerKeywordVO = null;
							for (CustomerKeywordVO customerKeywordVO : customerKeywordVOs) {
								if (Utils.isSameStr(customerKeywordVO.getKeyword(), fumianListVO.getKeyword()) &&
										Utils.isSameStr(customerKeywordVO.getTitle(), fumianListVO.getTitle()) &&
										Utils.isSameStr(customerKeywordVO.getUrl(), fumianListVO.getUrl())) {
									matchedCustomerKeywordVO = customerKeywordVO;
									break;
								}
							}
							if (matchedCustomerKeywordVO != null) {
								customerKeywordVOs.remove(matchedCustomerKeywordVO);
							}
						}
					}
					CaptureRealUrlService captureRealUrlService = new CaptureRealUrlService();
					for (CustomerKeywordVO customerKeywordVO : customerKeywordVOs) {
						if(!Utils.isNullOrEmpty(customerKeywordVO.getOriginalUrl())){
							String targetUrl = captureRealUrlService.fetchSingleRealUrl(customerKeywordVO.getOriginalUrl());
							if(!Utils.isNullOrEmpty(targetUrl)){
								customerKeywordVO.setOriginalUrl(targetUrl);
							}
						}
					}
					deleteCustomerKeywordForAutoUpdateNegative(conn, keyword, group);
					addCustomerKeywords(conn, customerKeywordVOs);
				}finally {
					if(conn != null){
						DBUtil.closeConnection(conn);
					}
				}
			}
		}
	}

	public void addCustomerKeywords(String datasourceName, List<CustomerKeywordVO> customerKeywords) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			addCustomerKeywords(conn, customerKeywords);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void addCustomerKeywords(Connection conn, List<CustomerKeywordVO> customerKeywords) throws Exception {
		for (CustomerKeywordVO customerKeywordVO : customerKeywords) {
			createCustomerKeywordVO(customerKeywordVO, conn);
		}
	}

	public void addCustomerKeyword(CustomerKeywordVO customerKeyword, String dsName) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(dsName);
			createCustomerKeywordVO(customerKeyword, conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void supplementInfo(List<CustomerKeywordVO> customerKeywordVOs, int customerUuid, String type, String terminalType) {
		for (CustomerKeywordVO customerKeywordVO : customerKeywordVOs) {
			customerKeywordVO.setCustomerUuid(customerUuid);
			customerKeywordVO.setType(type);
			customerKeywordVO.setCreateTime(Utils.getCurrentTimestamp());
			customerKeywordVO.setUpdateTime(Utils.getCurrentTimestamp());
//			int indexCount = IndexAndPositionHelper.getIndexCount(customerKeywordVO.getSearchEngine(),
//					customerKeywordVO.getKeyword());
//			int positionNumber = IndexAndPositionHelper.getPosition(customerKeywordVO.getSearchEngine(),
//					customerKeywordVO.getKeyword(), customerKeywordVO.getUrl());
//			customerKeywordVO.setInitialIndexCount(indexCount);
//			customerKeywordVO.setInitialPosition(positionNumber);
			customerKeywordVO.setStatus(1);
			customerKeywordVO.setTerminalType(terminalType);
		}
	}

	public boolean handleExcel(InputStream inputStream, String excelType, int customerUuid, String datasourceName, String type, String terminalType)
			throws Exception {
		AbstractExcelReader operator = AbstractExcelReader.createExcelOperator(inputStream, excelType);
		List customerKeywordVOs = operator.readDataFromExcel();
		supplementInfo(customerKeywordVOs, customerUuid, type, terminalType);
		addCustomerKeywords(datasourceName, customerKeywordVOs);
		return true;
	}

	private void createCustomerKeywordVO(CustomerKeywordVO customerKeyword, Connection conn)
			throws Exception {
		if(StringUtil.isNullOrEmpty(customerKeyword.getOriginalUrl())){
			customerKeyword.setOriginalUrl(customerKeyword.getUrl());
		}
		String originalUrl = customerKeyword.getOriginalUrl();
		if(!StringUtil.isNullOrEmpty(originalUrl)){
			if(originalUrl.indexOf("www.") == 0){
				originalUrl = originalUrl.substring(4);
			}else if(originalUrl.indexOf("m.") == 0){
				originalUrl = originalUrl.substring(2);
			}
		}else{
			originalUrl = null;
		}
		if(!"fm".equals(customerKeyword.getType()) && haveDuplicatedCustomerKeyword(conn, customerKeyword.getTerminalType(), customerKeyword
				.getCustomerUuid(), customerKeyword.getKeyword(), originalUrl)){
			return ;
		}
		PreparedStatement ps;
		String preSql;
		preSql = "insert into t_customer_keyword(fCustomerUuid,fType, fKeyword,fUrl,fTitle,fSnapshotDateTime,fSearchEngine,fInitialIndexCount,"
				+ "fTerminalType, fOriginalUrl, fPaymentStatus, fOrderNumber,"
				+ "fInitialPosition,fCurrentIndexCount,fCurrentPosition,"
				+ "fQueryTime,fServiceProvider,fOptimizeGroupName,"
				+ "fOptimizePlanCount,fOptimizedCount,fSequence,"

				+ "fRelatedKeywords,fPositionFirstCost,fPositionSecondCost,fPositionThirdCost,fPositionForthCost,fPositionFifthCost,"
				+ "fPositionFirstFee,fPositionSecondFee,fPositionThirdFee,fPositionForthFee,fPositionFifthFee,fPositionFirstPageFee,"
				+ "fCollectMethod,fStartOptimizedTime,fEffectiveFromTime,fEffectiveToTime,"
				+ "fStatus,fRemarks,fAutoUpdateNegativeDateTime,fUpdateTime,fCreateTime)"
				+ "values(?,?,?,?,?,?,?,?,"
				+ "?,?,?,?,"
				+ "?,?,?,"
				+ "?,?,?,"
				+ "?,?,?,"

				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,"
				+ "?,?, now(), now(), now())";
		ps = conn.prepareStatement(preSql);
		int i = 1;
		ps.setInt(i++, customerKeyword.getCustomerUuid());
		ps.setString(i++, customerKeyword.getType().trim());
		ps.setString(i++, customerKeyword.getKeyword().trim());
		ps.setString(i++, customerKeyword.getUrl() != null ? customerKeyword.getUrl().trim() : "");
		ps.setString(i++, customerKeyword.getTitle() != null ? customerKeyword.getTitle().trim() : "");
		ps.setString(i++, customerKeyword.getSnapshotDateTime());
		ps.setString(i++, customerKeyword.getSearchEngine());
		ps.setInt(i++, customerKeyword.getInitialIndexCount());


		ps.setString(i++, customerKeyword.getTerminalType());
		ps.setString(i++, customerKeyword.getOriginalUrl() != null ? customerKeyword.getOriginalUrl().trim() : "");
		ps.setString(i++, customerKeyword.getPaymentStatus() != null ? customerKeyword.getPaymentStatus().trim() : "");
		ps.setString(i++, customerKeyword.getOrderNumber() != null ? customerKeyword.getOrderNumber().trim() : "");

		ps.setInt(i++, customerKeyword.getInitialPosition());
		ps.setInt(i++, customerKeyword.getCurrentIndexCount());
		ps.setInt(i++, 10);

		ps.setTimestamp(i++, customerKeyword.getQueryTime());
		ps.setString(i++, customerKeyword.getServiceProvider());
		ps.setString(i++, customerKeyword.getOptimizeGroupName());

		ps.setInt(i++, customerKeyword.getOptimizePlanCount());
		ps.setInt(i++, customerKeyword.getOptimizedCount());
		ps.setInt(i++, customerKeyword.getSequence());

		ps.setString(i++, customerKeyword.getRelatedKeywords());
		ps.setDouble(i++, customerKeyword.getPositionFirstCost());
		ps.setDouble(i++, customerKeyword.getPositionSecondCost());
		ps.setDouble(i++, customerKeyword.getPositionThirdCost());
		ps.setDouble(i++, customerKeyword.getPositionForthCost());
		ps.setDouble(i++, customerKeyword.getPositionFifthCost());


		ps.setDouble(i++, customerKeyword.getPositionFirstFee());
		ps.setDouble(i++, customerKeyword.getPositionSecondFee());
		ps.setDouble(i++, customerKeyword.getPositionThirdFee());
		ps.setDouble(i++, customerKeyword.getPositionForthFee());
		ps.setDouble(i++, customerKeyword.getPositionFifthFee());
		ps.setDouble(i++, customerKeyword.getPositionFirstPageFee());

		ps.setString(i++, customerKeyword.getCollectMethod());
		ps.setTimestamp(i++, customerKeyword.getStartOptimizedTime());
		ps.setTimestamp(i++, customerKeyword.getEffectiveFromTime());
		ps.setTimestamp(i++, customerKeyword.getEffectiveToTime());

		ps.setInt(i++, customerKeyword.getStatus());
		ps.setString(i++, customerKeyword.getRemarks());
		ps.executeUpdate();
		DBUtil.closePreparedStatement(ps);
		KeywordManager manager = new KeywordManager();
		manager.addKeywordVOs(conn, customerKeyword.getRelatedKeywords(), customerKeyword.getSearchEngine(),
				KeywordType.RelatedKeyword.name());
		manager.addKeywordVOs(conn, customerKeyword.getKeyword(), customerKeyword.getSearchEngine(),
				KeywordType.CustomerKeyword.name());
	}

	public void updateCustomerKeyword(CustomerKeywordVO customerKeyword, String datasourceName) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			conn.setAutoCommit(false);

			preSql = "update t_customer_keyword set fCustomerUuid=?,fKeyword=?,fOptimizeGroupName =?,fUrl=?,fSearchEngine=?,"
					+ "fCurrentPosition=?,fCurrentIndexCount=?,fServiceProvider=?,"
					+ "fPositionFirstCost=?,fPositionSecondCost=?,fPositionThirdCost=?,fPositionForthCost=?,fPositionFifthCost=?,"
					+ "fPositionFirstFee=?,fPositionSecondFee=?,fPositionThirdFee=?,fPositionForthFee=?,fPositionFifthFee=?,fPositionFirstPageFee=?,"
					+ "fOptimizePositionFirstPercentage=?,fOptimizePositionSecondPercentage=?,fOptimizePositionThirdPercentage=?,"
					+ "fEffectiveFromTime=?,fEffectiveToTime=?,fTitle=?,"
					+ "fOriginalUrl=?,fPaymentStatus=?,fOrderNumber=?,fRemarks=?,"
					+ "fOptimizePlanCount=?,fCollectMethod=?,fStatus=?,fRelatedKeywords=?,"
					+ "fUpdateTime=?, fSequence = ? where fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setInt(i++, customerKeyword.getCustomerUuid());
			ps.setString(i++, customerKeyword.getKeyword());
			ps.setString(i++, customerKeyword.getOptimizeGroupName());
			ps.setString(i++, customerKeyword.getUrl() != null ? customerKeyword.getUrl().trim() : "");
			ps.setString(i++, customerKeyword.getSearchEngine());

			ps.setInt(i++, customerKeyword.getCurrentPosition());
			ps.setInt(i++, customerKeyword.getCurrentIndexCount());
			ps.setString(i++, customerKeyword.getServiceProvider());


			ps.setDouble(i++, customerKeyword.getPositionFirstCost());
			ps.setDouble(i++, customerKeyword.getPositionSecondCost());
			ps.setDouble(i++, customerKeyword.getPositionThirdCost());
			ps.setDouble(i++, customerKeyword.getPositionForthCost());
			ps.setDouble(i++, customerKeyword.getPositionFifthCost());


			ps.setDouble(i++, customerKeyword.getPositionFirstFee());
			ps.setDouble(i++, customerKeyword.getPositionSecondFee());
			ps.setDouble(i++, customerKeyword.getPositionThirdFee());
			ps.setDouble(i++, customerKeyword.getPositionForthFee());
			ps.setDouble(i++, customerKeyword.getPositionFifthFee());
			ps.setDouble(i++, customerKeyword.getPositionFirstPageFee());


			ps.setInt(i++, customerKeyword.getOptimizePositionFirstPercentage());
			ps.setInt(i++, customerKeyword.getOptimizePositionSecondPercentage());
			ps.setInt(i++, customerKeyword.getOptimizePositionThirdPercentage());


			ps.setTimestamp(i++, customerKeyword.getEffectiveFromTime());
			ps.setTimestamp(i++, customerKeyword.getEffectiveToTime());

			ps.setString(i++, customerKeyword.getTitle() != null ? customerKeyword.getTitle().trim() : "");

			ps.setString(i++, customerKeyword.getOriginalUrl() != null ? customerKeyword.getOriginalUrl().trim() : "");
			ps.setString(i++, customerKeyword.getPaymentStatus() != null ? customerKeyword.getPaymentStatus().trim() : "");
			ps.setString(i++, customerKeyword.getOrderNumber() != null ? customerKeyword.getOrderNumber().trim() : "");
			ps.setString(i++, customerKeyword.getRemarks() != null ? customerKeyword.getRemarks().trim() : "");

			ps.setInt(i++, customerKeyword.getOptimizePlanCount());

			ps.setString(i++, customerKeyword.getCollectMethod());
			ps.setInt(i++, customerKeyword.getStatus());
			ps.setString(i++, customerKeyword.getRelatedKeywords());

			ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
			ps.setInt(i++, customerKeyword.getSequence());
			ps.setInt(i++, customerKeyword.getUuid());

			ps.executeUpdate();

			KeywordManager manager = new KeywordManager();
			manager.addKeywordVOs(conn, customerKeyword.getRelatedKeywords(), customerKeyword.getSearchEngine(),
					KeywordType.RelatedKeyword.name());
			manager.addKeywordVOs(conn, customerKeyword.getKeyword(), customerKeyword.getSearchEngine(),
					KeywordType.CustomerKeyword.name());
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("updateCustomerKeyword" + e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void deleteCustomerKeyword(String dataSourceName, int customerKeywordUuid, String type) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = "";
		try {
			conn = DBUtil.getConnection(dataSourceName);

			sql = "delete from t_customer_keyword where fUuid = ? and fType = ? ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setInt(1, customerKeywordUuid);
			stmt.setString(2, type);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
	}

	public void deleteCustomerKeywordForAutoUpdateNegative(Connection conn, String keyword, String group) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "delete from t_customer_keyword where fType = 'fm' and fStatus = 1 and fOptimizeGroupName = ? and fKeyword = ? ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, group);
			stmt.setString(2, keyword);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("deleteCustomerKeywordForAutoUpdateNegative error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}

	public void deleteCustomerKeywordWhenAddFumianList(Connection conn, String terminalType, String keyword, String title) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "delete from t_customer_keyword where fType = 'fm' and fStatus = 1 and fTerminalType = ? and fKeyword = ? and fTitle = ? ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setString(i++, terminalType);
			stmt.setString(i++, keyword);
			stmt.setString(i++, title);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("deleteCustomerKeywordWhenAddFumianList error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}

	public void deleteEmptyTitleAndUrlCustomerKeyword(String dataSourceName, String terminalType, int customerUuid, String type) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = "";
		try {
			conn = DBUtil.getConnection(dataSourceName);

			sql = "delete from t_customer_keyword where fCustomerUuid = ? AND fTerminalType = ? AND fType = ? AND (fUrl = '' or fUrl IS NULL) AND (fTitle = '' or " +
					"fTitle IS null) ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setInt(i++, customerUuid);
			stmt.setString(i++, terminalType);
			stmt.setString(i++, type);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
	}

	public void deleteEmptyTitleCustomerKeyword(String dataSourceName, String terminalType, int customerUuid, String type) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = "";
		try {
			conn = DBUtil.getConnection(dataSourceName);

			sql = "delete from t_customer_keyword where fCustomerUuid = ? and fTerminalType = ? AND fType = ? AND (fTitle = '' or fTitle IS null) ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setInt(i++, customerUuid);
			stmt.setString(i++, terminalType);
			stmt.setString(i++, type);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
	}

	public void deleteEmptyTitleCustomerKeyword(String dataSourceName, String terminalType, int customerUuid, String type, String groupName) throws
			Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = "";
		try {
			conn = DBUtil.getConnection(dataSourceName);

			sql = "delete from t_customer_keyword where fCustomerUuid = ? and fTerminalType = ? AND fType = ? AND fOptimizeGroupName = ? AND (fTitle = '' or" +
					" fTitle IS null) ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setInt(i++, customerUuid);
			stmt.setString(i++, terminalType);
			stmt.setString(i++, type);
			stmt.setString(i++, groupName);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
	}

	public void resetTitle(String dataSourceName, String condition) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = "";
		try {
			conn = DBUtil.getConnection(dataSourceName);

			sql = "UPDATE t_customer_keyword SET fTitle = NULL, fCaptureTitleQueryTime = NULL, fCapturedTitle = 0 where 1 = 1 "
					+ condition;

			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
	}


	public int getTotalActiveCustomerKeywordCount(String dataSourceName, String keyword) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		try {
			conn = DBUtil.getConnection(dataSourceName);

			sql = "select count(1) as recordCount from t_customer_keyword where fStatus = 1 and fKeyword=? ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, keyword);

			rs = stmt.executeQuery();

			if (rs.next()) {
				int i = rs.getInt("recordCount");
				return i;
			}
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
	}

	public String getCurrentIndexCountFiledName(String type){
		return String.format("f%sCurrentIndexCount", Constants.BAIDU_TYPE_PC.equals(type) ? "" : "Phone");
	}

	public String getInitialPositionFiledName(String type){
		return String.format("f%sInitialPosition", Constants.BAIDU_TYPE_PC.equals(type) ? "" : type);
	}

	public String getCurrentPositionFiledName(String type){
		return String.format("f%sCurrentPosition", Constants.BAIDU_TYPE_PC.equals(type) ? "" : type);
	}

	public String getOptimizePlanCountFiledName(String type){
		return String.format("f%sOptimizePlanCount", Constants.BAIDU_TYPE_PC.equals(type) ? "" : "Phone");
	}

	public String getPagePercentage(String type){
		return Constants.BAIDU_TYPE_PC.equals(type) ? ConfigManager.CONFIG_KEY_PAGE_PC_PERCENTAGE : ConfigManager.CONFIG_KEY_PAGE_PHONE_PERCENTAGE;
	}

	public void updateSummaryInfoWhenCapturePosition(Connection conn, CustomerKeywordVO customerKeywordVO, String type)
			throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_customer_keyword set fInitialPosition = ?, fCurrentPosition = ?,fCapturePositionQueryTime"
					+ " = now(), fUpdateTime = now() where fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setInt(i++, customerKeywordVO.getInitialPosition() == 0 ? customerKeywordVO.getCurrentPosition() : customerKeywordVO.getInitialPosition());
			ps.setInt(i++, customerKeywordVO.getCurrentPosition());
			ps.setInt(i++, customerKeywordVO.getUuid());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateCustomerKeywordGroupName(String dataSourceName, String terminalType, String uuids, String groupName) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			updateCustomerKeywordGroup(conn, terminalType, uuids, groupName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void updateCustomerKeywordGroupName(String dataSourceName, String terminalType, int customerUuid, String groupName) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			updateCustomerKeywordGroup(conn, terminalType, customerUuid, groupName);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void stopCustomerKeyword(String dataSourceName, String terminalType, int customerUuid) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			updateCustomerKeywordGroup(conn, terminalType, customerUuid, "stop");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void updateCustomerKeywordStatus(String dataSourceName, String terminalType, int customerUuid, String status) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(dataSourceName);
			preSql = "UPDATE t_customer_keyword SET fStatus = ? WHERE fTerminalType = ? and fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, status);
			ps.setString(i++, terminalType);
			ps.setInt(i++, customerUuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void updateCustomerKeywordGroup(Connection conn, String terminalType, String uuids, String groupName) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_customer_keyword SET fOptimizeGroupName = ? WHERE fTerminalType = ? and fUuid in (" + uuids.trim() + ")";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, groupName);
			ps.setString(i++, terminalType);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateCustomerKeywordGroup(Connection conn, String terminalType, int customerUuid, String groupName) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_customer_keyword SET fOptimizeGroupName = ? WHERE fCustomerUuid = ? and fTerminalType = ? ";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, groupName);
			ps.setInt(i++, customerUuid);
			ps.setString(i++, terminalType);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateQueryUpdateTime(Connection conn, int customerKeywordUuid) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_customer_keyword set fQueryDate = CURRENT_DATE(), "
					+ "fQueryCount = IF(fQueryDate = CURRENT_DATE(), fQueryCount + 1, 1), "
					+ "fInvalidRefreshCount = fInvalidRefreshCount + 1, fQueryTime=now()" + " where fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setInt(i++, customerKeywordUuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateCapturePositionQueryTime(Connection conn, String type, int customerKeywordUuid) throws Exception {
		PreparedStatement ps = null;
		String capturePositionQueryTimeFieldName = "fCapturePositionQueryTime";
		String preSql = null;
		try {
			preSql = "update t_customer_keyword set " + capturePositionQueryTimeFieldName + "=now()" + " where fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setInt(i++, customerKeywordUuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateAutoUpdateNegativeTime(String datasourceName, String group) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		String preSql = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			preSql = "update t_customer_keyword set fAutoUpdateNegativeDateTime = DATE_ADD(now(), INTERVAL -4 MINUTE)" +
					" where fType = 'fm' AND fStatus = 1 and fOptimizeGroupName = ?";
			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, group);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void updateAutoUpdateNegativeTime(Connection conn, String keyword, String group) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_customer_keyword set fAutoUpdateNegativeDateTime = now()" +
					" where fType = 'fm'AND fStatus = 1 AND fKeyword = ? and fOptimizeGroupName = ?";
			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, keyword);
			ps.setString(i++, group);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateCaptureTitleQueryTime(Connection conn, int customerKeywordUuid) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_customer_keyword set fCaptureTitleQueryTime = now()" + " where fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setInt(i++, customerKeywordUuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateCaptureIndexQueryTime(Connection conn, String type, int customerKeywordUuid) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_customer_keyword set fCaptureIndexQueryTime = now()" + " where fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setInt(i++, customerKeywordUuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateInvalidRefreshCount(String datasourceName, String entryType, String groupName, String customerName)
			throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			conn.setAutoCommit(false);
			String condition = "";
			if(StringUtil.isNotNullNorEmpty(customerName)){
				condition = condition + "  AND EXISTS (SELECT 1 FROM t_customer c WHERE c.fUuid = fCustomerUuid AND c.fContactPerson LIKE '%"  + customerName.trim() + "%')";
			}
			if(StringUtil.isNotNullNorEmpty(groupName)){
				condition = condition + " AND fOptimizeGroupName like '%"  + groupName.trim() + "%'";
			}
			preSql = "UPDATE t_customer_keyword SET fInvalidRefreshCount = 0 WHERE fType = '" + entryType + "' " + condition;
			ps = conn.prepareStatement(preSql);
			ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void cleanQueryCount(Connection conn) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_customer_keyword SET fQueryCount = 0, fQueryDate = CURRENT_DATE() where fQueryDate != CURRENT_DATE()";
			ps = conn.prepareStatement(preSql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void cleanOptimizedCount(Connection conn) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "UPDATE t_customer_keyword SET fOptimizedCount = 0, fInvalidRefreshCount = 0, fOptimizeDate = CURRENT_DATE() where fOptimizeDate != CURRENT_DATE()";
			ps = conn.prepareStatement(preSql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateOptimizedCount(String datasourceName, String terminalType, int customerKeywordUuid, int count, String ip, String city, String
			cookieInfos, String clientID, String status, String freeSpace, String version) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			conn.setAutoCommit(false);
			OptimizeDateManager optimizeDateManager = new OptimizeDateManager();
			if(!optimizeDateManager.checkOptimizeDateIsToday(conn)){
				optimizeDateManager.updateOptimizeDateAsToday(conn);
				cleanOptimizedCount(conn);
				cleanQueryCount(conn);
				cleanInvalidRefreshCount(conn);
			}
			if(count > 0){
				preSql = "UPDATE t_customer_keyword SET fLastOptimizeDateTime = now(), fInvalidRefreshCount = 0, fOptimizedCount = IF(fOptimizeDate = CURRENT_DATE(), fOptimizedCount, 0) + ?,fOptimizedPercentage = fOptimizedCount/fOptimizePlanCount, fOptimizeDate = CURRENT_DATE(),fUpdateTime=NOW()"
						+ " where fUuid = ?";
			}else{
				preSql = "UPDATE t_customer_keyword SET fLastOptimizeDateTime = now(), fOptimizedCount = IF(fOptimizeDate = CURRENT_DATE(), fOptimizedCount, 0) + ?,fOptimizeDate = CURRENT_DATE(),fUpdateTime=NOW()"
						+ " where fUuid = ?";
			}
			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setInt(i++, count);
			ps.setInt(i++, customerKeywordUuid);
			ps.executeUpdate();

			if(count > 0){
				CustomerKeywordIPManager customerKeywordIPManager = new CustomerKeywordIPManager();
				CustomerKeywordIPVO customerKeywordIPVO = new CustomerKeywordIPVO();
				customerKeywordIPVO.setCity(city);
				customerKeywordIPVO.setCustomerKeywordUuid(customerKeywordUuid);
				customerKeywordIPVO.setIp(ip);
				customerKeywordIPManager.addCustomerKeywordIPVO(conn, customerKeywordIPVO);
			}
			ClientStatusManager clientStatusManager = new ClientStatusManager();
			clientStatusManager.logClientVisitTime(conn, terminalType, clientID, status, freeSpace, version, city, count);

			List<CookieInfoVO> cookieInfoVOs = CookieInfoUtil.toVO(ip, cookieInfos);
			if(!Utils.isEmpty(cookieInfoVOs)){
				CookieInfoManager cookieInfoManager = new CookieInfoManager();
				cookieInfoManager.updateCookieInfoVOs(conn, cookieInfoVOs);
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	private String getFieldPrefix(String type){
		if(Constants.BAIDU_TYPE_PC.equals(type)){
			return "";
		}else{
			return type;
		}
	}

	public void cleanBigKeywordIndicator(Connection conn, String groupName) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "UPDATE t_customer_keyword SET fBigKeyword = 0 WHERE fOptimizeGroupName = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, groupName);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void setBigKeywordIndicator(Connection conn, Set<Integer> uuids) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "UPDATE t_customer_keyword SET fBigKeyword = 1 WHERE fUuid in (" + uuids.toString().replace("[", "").replace("]", "") + ")";

			ps = conn.prepareStatement(preSql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateAccountRange(Connection conn, Timestamp effectiveFromTime, Timestamp effectiveToTime,
								   int customerKeywordUuid, String type) throws Exception {
		PreparedStatement ps = null;

		String filedPrefix = getFieldPrefix(type);
		String preSql = null;
		try {
			preSql = String.format("update t_customer_keyword set f%sEffectiveFromTime=?,f%sEffectiveToTime=?,fUpdateTime=now()", filedPrefix, filedPrefix)
					+ " where fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setTimestamp(i++, effectiveFromTime);
			ps.setTimestamp(i++, effectiveToTime);
			ps.setInt(i++, customerKeywordUuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void adjustOptimizationCount(String datasourceName) throws Exception {
		PreparedStatement ps = null;
		Connection conn = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			sql = "SELECT ck.fUuid, ck.fKeyword, ck.fUrl, ck.fCurrentIndexCount, "+
					"GROUP_CONCAT(CAST(ps.fPCPosition AS CHAR) ORDER BY ps.fCreateDate) AS positions " +
					"FROM t_customer_keyword ck JOIN t_ck_position_summary ps ON ck.fUuid = ps.fCustomerKeywordUuid " +
					" WHERE ps.fCreateDate >= DATE_SUB(CURRENT_DATE(), INTERVAL 3 DAY) " +
					" AND ck.fStatus = 1 AND ck.fOptimizeGroupName = 'pc_pm_xiaowu' " +
					" GROUP BY ck.fUuid, ck.fKeyword ";

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			while (rs.next()) {
				int uuid = rs.getInt("fUuid");
				int currentIndexCount = rs.getInt("fCurrentIndexCount");
				String positionStr = rs.getString("positions");
				String [] positionArray = positionStr.split(",");
				int optimizationPlanCount = currentIndexCount < 100 ? 150 : currentIndexCount;
				if(positionArray.length == 3){
					boolean lessFifth = true;
					for(String position : positionArray){
						int iPosition = Integer.parseInt(position);
						if(iPosition == 0 || iPosition >= 3){
							lessFifth = false;
							break;
						}
					}
					if(lessFifth) {
						optimizationPlanCount = 20 + currentIndexCount / 50;
					}
				}
				int queryInterval = (23 * 60) / optimizationPlanCount;

				sql = "UPDATE t_customer_keyword SET fOptimizePlanCount = ?, fQueryInterval = ? WHERE fUuid = ?";
				ps = conn.prepareStatement(sql);
				int i = 1;
				ps.setInt(i++, optimizationPlanCount);
				ps.setInt(i++, queryInterval);
				ps.setInt(i++, uuid);
				ps.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void updatePaymentRange(Connection conn, Timestamp effectiveFromTime, Timestamp effectiveToTime,
								   int customerKeywordUuid) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "update t_customer_keyword set fPaymentEffectiveFromTime=?,fPaymentEffectiveToTime=?,fUpdateTime=now()"
					+ " where fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setTimestamp(i++, effectiveFromTime);
			ps.setTimestamp(i++, effectiveToTime);
			ps.setInt(i++, customerKeywordUuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void setDefaultPaymentRange(Connection conn, int customerKeywordUuid) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "update t_customer_keyword set fPaymentEffectiveFromTime=now(),fPaymentEffectiveToTime=now(),fUpdateTime=now()"
					+ " where fPaymentEffectiveFromTime is null and fUuid = ?";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setInt(i++, customerKeywordUuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

//	public void updateCustomerKeywordIndex(Connection conn, int indexCount, CustomerKeywordVO customerKeywordVO, String type) throws Exception {
//		customerKeywordVO.setCurrentIndexCount(indexCount);
//		updateSummaryInfoWhenCaptureIndex(conn, customerKeywordVO, type);
//	}

	public CustomerKeywordPositionIndexLogVO updateCustomerKeywordPosition(Connection conn,
																		   int positionNumber, CustomerKeywordVO customerKeywordVO, String type, String ip) throws Exception {
		int indexCount = customerKeywordVO.getCurrentIndexCount();
		customerKeywordVO.setApplicableCurrentPosition(positionNumber, type);
		updateSummaryInfoWhenCapturePosition(conn, customerKeywordVO, type);
		CustomerKeywordPositionIndexLogManager positionIndexLogManager = new CustomerKeywordPositionIndexLogManager();
		CustomerKeywordPositionIndexLogVO customerKeywordPositionIndexLog = new CustomerKeywordPositionIndexLogVO();
		customerKeywordPositionIndexLog.setCustomerKeywordUuid(customerKeywordVO.getUuid());
		customerKeywordPositionIndexLog.setIndexCount(indexCount);
		customerKeywordPositionIndexLog.setPositionNumber(positionNumber);
		customerKeywordPositionIndexLog.setType(type);
		customerKeywordPositionIndexLog.setIp(ip);
		positionIndexLogManager.addCustomerKeywordPositionIndexLog(conn, customerKeywordPositionIndexLog);

		CustomerKeywordPositionSummaryManager customerKeywordPositionSummaryManager = new CustomerKeywordPositionSummaryManager();
		customerKeywordPositionSummaryManager.prepareCustomerKeywordPositionSummary(conn, customerKeywordPositionIndexLog);
		return customerKeywordPositionIndexLog;
	}
}