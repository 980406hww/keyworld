package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.keymanager.db.DBUtil;
import com.keymanager.value.UserAccountLogCustomerKeywordVO;
import com.keymanager.value.UserAccountLogVO;
import com.keymanager.value.UserVO;

public class UserAccountLogManager {
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

	public ArrayList searchUserAccountLogs(String datasourceName, int pageSize, int curPage,
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
				sql = " select count(1) as recordCount FROM t_user_account_log where 1 = 1 "
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

			String sqlFields = " * ";

			sql = " select " + sqlFields + " FROM t_user_account_log where 1 = 1 "
					+ condition + " " + order;
			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				UserAccountLogVO customerKeyword = getUserAccountLog(conn, rs);
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


	private UserAccountLogVO getUserAccountLog(Connection conn, ResultSet rs) throws Exception {
		UserAccountLogVO userAccountLogVO = new UserAccountLogVO();
		userAccountLogVO.setUuid(rs.getInt("fUuid"));
		userAccountLogVO.setUserID(rs.getString("fUserID"));
		userAccountLogVO.setType(rs.getString("fType"));
		userAccountLogVO.setAmount(rs.getDouble("fAmount"));
		userAccountLogVO.setAccountAmount(rs.getDouble("fAccountAmount"));
		userAccountLogVO.setRemarks(rs.getString("fRemarks"));
		userAccountLogVO.setCreateTime(rs.getTimestamp("fCreateTime"));
		return userAccountLogVO;
	}

	public void addUserAccountLog(String datasourceName, UserAccountLogVO customerKeyword) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			createUserAccountLogVO(customerKeyword, conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public int createUserAccountLogVO(UserAccountLogVO customerKeyword, Connection conn)
			throws Exception {
		PreparedStatement ps = null;
		String preSql;
		try {
			preSql = "insert into t_user_account_log(fUserID,fAmount,fType,fAccountAmount,fRemarks,fCreateTime)"
					+ "values(?,?,?,?,?,now())";
			ps = conn.prepareStatement(preSql, Statement.RETURN_GENERATED_KEYS);
			int i = 1;
			ps.setString(i++, customerKeyword.getUserID());
			ps.setDouble(i++, customerKeyword.getAmount());
			ps.setString(i++, customerKeyword.getType());
			ps.setDouble(i++, customerKeyword.getAccountAmount());
			ps.setString(i++, customerKeyword.getRemarks());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}
	
	public void receive(String datasourceName) throws Exception{
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			conn.setAutoCommit(false);
			UserAccountLogCustomerKeywordManager userAccountLogCustomerKeywordManager = new UserAccountLogCustomerKeywordManager();
			ArrayList<UserAccountLogCustomerKeywordVO> userAccountLogCustomerKeywordVOs = userAccountLogCustomerKeywordManager.searchUserAccountLogCustomerKeywordVOs(conn);
			Map<String, ArrayList<UserAccountLogCustomerKeywordVO>> userAccountLogCustomerKeywordVOMap = new HashMap<String, ArrayList<UserAccountLogCustomerKeywordVO>>();
			Map<String, Double> userTotalCostMap = new HashMap<String, Double>();
			for(UserAccountLogCustomerKeywordVO userAccountLogCustomerKeywordVO : userAccountLogCustomerKeywordVOs){
				ArrayList<UserAccountLogCustomerKeywordVO> tmpUserAccountLogCustomerKeywordVOs = userAccountLogCustomerKeywordVOMap.get(userAccountLogCustomerKeywordVO.getUserID());
				if(tmpUserAccountLogCustomerKeywordVOs == null){
					tmpUserAccountLogCustomerKeywordVOs = new ArrayList<UserAccountLogCustomerKeywordVO>();
					userAccountLogCustomerKeywordVOMap.put(userAccountLogCustomerKeywordVO.getUserID(), tmpUserAccountLogCustomerKeywordVOs);
				}
				tmpUserAccountLogCustomerKeywordVOs.add(userAccountLogCustomerKeywordVO);
				Double totalCost = userTotalCostMap.get(userAccountLogCustomerKeywordVO.getUserID());
				if(totalCost == null){
					totalCost = userAccountLogCustomerKeywordVO.getSubTotal(userAccountLogCustomerKeywordVO.getUserCollectMethod());
				}else{
					totalCost = totalCost + userAccountLogCustomerKeywordVO.getSubTotal(userAccountLogCustomerKeywordVO.getUserCollectMethod());
				}
				userTotalCostMap.put(userAccountLogCustomerKeywordVO.getUserID(), totalCost);
			}
			
			UserManager userManager = new UserManager();
			for(Entry<String, Double> entry : userTotalCostMap.entrySet()){
				String userID = entry.getKey();
				Double totalCost = entry.getValue();
				UserVO userVO = userManager.getUserByUserID(conn, userID);
				UserAccountLogVO userAccountLogVO = new UserAccountLogVO();
				userAccountLogVO.setAmount(totalCost);
				userAccountLogVO.setUserID(userID);
				userAccountLogVO.setType("自动扣费");
				double accountAmount = userVO.getAccountAmount();
				accountAmount = accountAmount - totalCost;
				userAccountLogVO.setAccountAmount(accountAmount);
				int userAccountLogID = createUserAccountLogVO(userAccountLogVO, conn);
				userVO.setAccountAmount(accountAmount);
				userManager.updateUser(conn, userVO);
				
				ArrayList<UserAccountLogCustomerKeywordVO> tmpUserAccountLogCustomerKeywordVOs = userAccountLogCustomerKeywordVOMap.get(userID);
				for(UserAccountLogCustomerKeywordVO userAccountLogCustomerKeywordVO : tmpUserAccountLogCustomerKeywordVOs){
					userAccountLogCustomerKeywordVO.setUserAccountLogUuid(userAccountLogID);
					userAccountLogCustomerKeywordManager.createUserAccountLogCustomerKeywordVO(userAccountLogCustomerKeywordVO, conn);
				}
			}
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
}