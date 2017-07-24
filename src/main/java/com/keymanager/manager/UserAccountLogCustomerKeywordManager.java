package com.keymanager.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.keymanager.db.DBUtil;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordPositionHistoryLogView;
import com.keymanager.value.UserAccountLogCustomerKeywordVO;

public class UserAccountLogCustomerKeywordManager {
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

	public ArrayList searchUserAccountLogCustomerKeywords(String datasourceName, int pageSize, int curPage,
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
				sql = " select count(1) as recordCount FROM t_user_account_log_customer_keyword where 1 = 1 "
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

			sql = " select " + sqlFields + " FROM t_user_account_log_customer_keyword where 1 = 1 "
					+ condition + " " + order;
			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				UserAccountLogCustomerKeywordVO customerKeyword = getUserAccountLogCustomerKeyword(conn, rs);
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


	private UserAccountLogCustomerKeywordVO getUserAccountLogCustomerKeyword(Connection conn, ResultSet rs) throws Exception {
		UserAccountLogCustomerKeywordVO customerKeyword = new UserAccountLogCustomerKeywordVO();
		customerKeyword.setUuid(rs.getInt("fUuid"));
		customerKeyword.setUserAccountLogUuid(rs.getInt("fUserAccountLogUuid"));
		customerKeyword.setCustomerKeywordUuid(rs.getInt("fCustomerKeywordUuid"));
		prepareCommonData(rs, customerKeyword);
		customerKeyword.setCreateTime(rs.getTimestamp("fCreateTime"));
		return customerKeyword;
	}

	private void prepareCommonData(ResultSet rs, UserAccountLogCustomerKeywordVO customerKeyword) throws SQLException {
		customerKeyword.setKeyword(rs.getString("fKeyword"));
		customerKeyword.setUrl(rs.getString("fUrl"));
		customerKeyword.setPhoneUrl(rs.getString("fPhoneUrl"));
		customerKeyword.setOriginalUrl(rs.getString("fOriginalUrl"));
		customerKeyword.setOriginalPhoneUrl(rs.getString("fOriginalPhoneUrl"));
		
		customerKeyword.setCurrentPosition(rs.getInt("fCurrentPosition"));
		customerKeyword.setJisuCurrentPosition(rs.getInt("fJisuCurrentPosition"));
		customerKeyword.setChupingCurrentPosition(rs.getInt("fChupingCurrentPosition"));

		customerKeyword.setPositionFirstFee(rs.getDouble("fPositionFirstFee"));
		customerKeyword.setPositionSecondFee(rs.getDouble("fPositionSecondFee"));
		customerKeyword.setPositionThirdFee(rs.getDouble("fPositionThirdFee"));
		customerKeyword.setPositionForthFee(rs.getDouble("fPositionForthFee"));
		customerKeyword.setPositionFifthFee(rs.getDouble("fPositionFifthFee"));
		customerKeyword.setPositionFirstPageFee(rs.getDouble("fPositionFirstPageFee"));

		customerKeyword.setJisuPositionFirstFee(rs.getDouble("fJisuPositionFirstFee"));
		customerKeyword.setJisuPositionSecondFee(rs.getDouble("fJisuPositionSecondFee"));
		customerKeyword.setJisuPositionThirdFee(rs.getDouble("fJisuPositionThirdFee"));
		customerKeyword.setJisuPositionForthFee(rs.getDouble("fJisuPositionForthFee"));
		customerKeyword.setJisuPositionFifthFee(rs.getDouble("fJisuPositionFifthFee"));
		customerKeyword.setJisuPositionFirstPageFee(rs.getDouble("fJisuPositionFirstPageFee"));
		
		customerKeyword.setChupingPositionFirstFee(rs.getDouble("fChupingPositionFirstFee"));
		customerKeyword.setChupingPositionSecondFee(rs.getDouble("fChupingPositionSecondFee"));
		customerKeyword.setChupingPositionThirdFee(rs.getDouble("fChupingPositionThirdFee"));
		customerKeyword.setChupingPositionForthFee(rs.getDouble("fChupingPositionForthFee"));
		customerKeyword.setChupingPositionFifthFee(rs.getDouble("fChupingPositionFifthFee"));
		customerKeyword.setChupingPositionFirstPageFee(rs.getDouble("fChupingPositionFirstPageFee"));

		customerKeyword.setStatus(rs.getInt("fStatus"));
	}

	private UserAccountLogCustomerKeywordVO getUserAccountLogCustomerKeywordForCKQuery(ResultSet rs, Connection conn) throws Exception {
		UserAccountLogCustomerKeywordVO customerKeyword = new UserAccountLogCustomerKeywordVO();
		customerKeyword.setCustomerKeywordUuid(rs.getInt("fUuid"));
		customerKeyword.setUserID(rs.getString("fUserID"));
		customerKeyword.setUserCollectMethod(rs.getString("fCollectMethod"));
		prepareCommonData(rs, customerKeyword);
		return customerKeyword;
	}
	
	public void addUserAccountLogCustomerKeyword(String datasourceName, UserAccountLogCustomerKeywordVO customerKeyword) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			createUserAccountLogCustomerKeywordVO(customerKeyword, conn);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void createUserAccountLogCustomerKeywordVO(UserAccountLogCustomerKeywordVO customerKeyword, Connection conn)
			throws Exception {
		PreparedStatement ps;
		String preSql;
		preSql = "insert into t_user_account_log_customer_keyword(fCustomerKeywordUuid,fUserAccountLogUuid,fKeyword,fUrl,fPhoneUrl,"
				+ "fOriginalUrl, fOriginalPhoneUrl,"
				+ "fCurrentPosition,fJisuCurrentPosition,fChupingCurrentPosition,"
				+ "fPositionFirstFee,fPositionSecondFee,fPositionThirdFee,fPositionForthFee,fPositionFifthFee,fPositionFirstPageFee,"
				+ "fJisuPositionFirstFee,fJisuPositionSecondFee,fJisuPositionThirdFee,fJisuPositionForthFee,fJisuPositionFifthFee,fJisuPositionFirstPageFee,"
				+ "fChupingPositionFirstFee,fChupingPositionSecondFee,fChupingPositionThirdFee,fChupingPositionForthFee,fChupingPositionFifthFee,fChupingPositionFirstPageFee,"
				+ "fStatus,fCreateTime)"
				+ "values(?,?,?,?,?,"
				+ "?,?,"
				+ "?,?,?,"
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,"
				+ "?,?,?,?,?,?,"
				+ "?,now())";
		ps = conn.prepareStatement(preSql);
		int i = 1;
		ps.setInt(i++, customerKeyword.getCustomerKeywordUuid());
		ps.setInt(i++, customerKeyword.getUserAccountLogUuid());
		ps.setString(i++, customerKeyword.getKeyword().trim());
		ps.setString(i++, customerKeyword.getUrl() != null ? customerKeyword.getUrl().trim() : "");
		ps.setString(i++, customerKeyword.getPhoneUrl() != null ? customerKeyword.getPhoneUrl().trim() : "");
		
		ps.setString(i++, customerKeyword.getOriginalUrl() != null ? customerKeyword.getOriginalUrl().trim() : "");
		ps.setString(i++, customerKeyword.getOriginalPhoneUrl() != null ? customerKeyword.getOriginalPhoneUrl().trim() : "");

		ps.setInt(i++, customerKeyword.getCurrentPosition());
		ps.setInt(i++, customerKeyword.getJisuCurrentPosition());
		ps.setInt(i++, customerKeyword.getChupingCurrentPosition());
		
		ps.setDouble(i++, customerKeyword.getPositionFirstFee());
		ps.setDouble(i++, customerKeyword.getPositionSecondFee());
		ps.setDouble(i++, customerKeyword.getPositionThirdFee());
		ps.setDouble(i++, customerKeyword.getPositionForthFee());
		ps.setDouble(i++, customerKeyword.getPositionFifthFee());
		ps.setDouble(i++, customerKeyword.getPositionFirstPageFee());
		
		ps.setDouble(i++, customerKeyword.getJisuPositionFirstFee());
		ps.setDouble(i++, customerKeyword.getJisuPositionSecondFee());
		ps.setDouble(i++, customerKeyword.getJisuPositionThirdFee());
		ps.setDouble(i++, customerKeyword.getJisuPositionForthFee());
		ps.setDouble(i++, customerKeyword.getJisuPositionFifthFee());
		ps.setDouble(i++, customerKeyword.getJisuPositionFirstPageFee());
		
		ps.setDouble(i++, customerKeyword.getChupingPositionFirstFee());
		ps.setDouble(i++, customerKeyword.getChupingPositionSecondFee());
		ps.setDouble(i++, customerKeyword.getChupingPositionThirdFee());
		ps.setDouble(i++, customerKeyword.getChupingPositionForthFee());
		ps.setDouble(i++, customerKeyword.getChupingPositionFifthFee());
		ps.setDouble(i++, customerKeyword.getChupingPositionFirstPageFee());
		
		ps.setInt(i++, customerKeyword.getStatus());
		ps.executeUpdate();
		DBUtil.closePreparedStatement(ps);
	}
	
	public ArrayList<UserAccountLogCustomerKeywordVO> searchUserAccountLogCustomerKeywordVOs(Connection conn) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		ArrayList<UserAccountLogCustomerKeywordVO> customerKeywordPositionHistoryLogViews = new ArrayList<UserAccountLogCustomerKeywordVO>();
		try {
			String sqlTemplate = " SELECT %s "
					+ " FROM t_customer_keyword ck, t_customer c, t_user u "
					+ " WHERE u.fUserID = c.fUserID "
					+ "     AND ck.fCustomerUuid = c.fUuid "
					+ "     AND ck.fStatus = 1 "
					+ "     AND u.fAutoPay = 1 "
					+ "     AND ((ck.fUrl IS NOT NULL "
					+ "           AND ck.fCurrentPosition > 0 "
					+ "           AND ck.fCurrentPosition < 11) "
					+ "           OR (ck.fPhoneUrl IS NOT NULL "
					+ "               AND ((ck.fJisuCurrentPosition > 0 "
					+ "                     AND ck.fJisuCurrentPosition < 11) "
					+ "                     OR (ck.fChupingCurrentPosition > 0 "
					+ "                         AND ck.fChupingCurrentPosition < 11)))) ";

			String sqlFields = " c.fUserID, u.fCollectMethod, ck.fUuid, ck.fCustomerUuid, ck.fKeyword, ck.furl, ck.fPhoneUrl, ck.fOriginalUrl, "
					+ "ck.fOriginalPhoneUrl, ck.fPositionFirstFee, ck.fPositionSecondFee, ck.fPositionThirdFee, ck.fPositionForthFee, "
					+ "ck.fPositionFifthFee, ck.fPositionFirstPageFee, ck.fJisuPositionFirstFee, ck.fJisuPositionSecondFee, ck.fJisuPositionThirdFee, "
					+ "ck.fJisuPositionForthFee, ck.fJisuPositionFifthFee, ck.fJisuPositionFirstPageFee, "
					+ "ck.fChupingPositionFirstFee, ck.fChupingPositionSecondFee, ck.fChupingPositionThirdFee, "
					+ "ck.fChupingPositionForthFee, ck.fChupingPositionFifthFee, ck.fChupingPositionFirstPageFee, ck.fCurrentPosition, "
					+ "ck.fJisuCurrentPosition, ck.fChupingCurrentPosition, ck.fStatus  ";
			
			sql = String.format(sqlTemplate, sqlFields);
			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				UserAccountLogCustomerKeywordVO userAccountLogCustomerKeywordVO = getUserAccountLogCustomerKeywordForCKQuery(rs, conn);
				if(userAccountLogCustomerKeywordVO.getSubTotal(userAccountLogCustomerKeywordVO.getUserCollectMethod()) > 0){
					customerKeywordPositionHistoryLogViews.add(userAccountLogCustomerKeywordVO);
				}
			}
			recordCount = customerKeywordPositionHistoryLogViews.size();
			return customerKeywordPositionHistoryLogViews;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}
}