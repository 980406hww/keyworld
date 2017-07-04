package com.keymanager.manager;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.keymanager.db.DBUtil;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordPositionHistoryLogView;
import com.keymanager.value.CustomerKeywordPositionView;

public class CustomerKeywordPositionViewManager {
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

	public ArrayList<CustomerKeywordPositionView> searchCustomerKeywordPositionViews(String datasourceName, int pageSize, int curPage,
			String customerUuid, String customerName, String date, String statType, int recCount) throws Exception {
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

		String condition = Utils.isNullOrEmpty(customerUuid) ? (Utils.isNullOrEmpty(customerName) ? "" : (" AND c.fContactPerson like '" + customerName + "%'")) : " AND ck.fCustomerUuid = " + customerUuid + " ";
		String dateCondition = Utils.isNullOrEmpty(date) ? Utils.getCurrentDate() : date.trim();
		ArrayList<CustomerKeywordPositionView> customerKeywordPositionViews = new ArrayList();
		try {
			conn = DBUtil.getConnection(datasourceName);
			String sqlTemplate = " SELECT %s "
					+ "FROM t_customer_keyword ck, t_ck_position_summary ps, t_customer c "
					+ "WHERE ps.fCustomerKeywordUuid = ck.fUuid "
					+ "%s"
					+ "AND ck.fCustomerUuid = c.fUuid "
					+ "AND ck.fStatus = 1 "
					+ "AND ps.fCreateDate = ? "
					+ "AND ((ck.fUrl IS NOT NULL "
					+ "AND ps.fPCPosition > 0 "
					+ "AND ps.fPCPosition < 11) "
					+ "OR (ck.fPhoneUrl IS NOT NULL "
					+ "AND ((ps.fJisuPosition > 0 "
					+ "        AND ps.fJisuPosition < 11) "
					+ "        OR (ps.fChupingPosition > 0 "
					+ "            AND ps.fChupingPosition < 11)))) "
					+ "            ORDER BY c.fContactPerson, ck.fCustomerUuid ";
//					sql = String.format(sqlTemplate, "count(1) as recordCount", condition);
			
			if ("All".equals(statType)){
				sqlTemplate = "SELECT %s "
						+ "FROM t_customer_keyword ck INNER JOIN t_customer c ON ck.fCustomerUuid = c.fUuid "
						+ " LEFT JOIN (SELECT * "
						+ "             FROM t_ck_position_summary tmp "
						+ "             WHERE tmp.fCreateDate = ?) AS ps "
						+ "   ON (ps.fCustomerKeywordUuid = ck.fUuid) "
						+ "WHERE 1 = 1 "
						+ "%s "
						+ "ORDER BY c.fContactPerson, ck.fCustomerUuid ";	
//					sql = String.format(sqlTemplate, "count(1) as recordCount", condition);
			}
		
//			if (recCount != 0) {
//				ps = conn.prepareStatement(sql, 1003, 1007);
//				ps.setDate(1, Date.valueOf(dateCondition.trim()));
//				rs = ps.executeQuery();
//				if (rs.next()) {
//					recordCount = rs.getInt("recordCount");
//				}
//
//				pageCount = (int) Math.ceil((recordCount + pageSize - 1) / pageSize);
//
//				if (curPage > pageCount) {
//					curPage = 1;
//				}
//				currPage = curPage;
//			}

			String sqlFields = " ck.fUuid, ck.fCustomerUuid, c.fContactPerson, ck.fRemarks, ck.fKeyword, ck.furl, ck.fPhoneUrl, ck.fOriginalUrl, "
						+ "ck.fOriginalPhoneUrl, ck.fPositionFirstFee, ck.fPositionSecondFee, ck.fPositionThirdFee, ck.fPositionForthFee, "
						+ "ck.fPositionFifthFee, ck.fPositionFirstPageFee, ck.fJisuPositionFirstFee, ck.fJisuPositionSecondFee, ck.fJisuPositionThirdFee, "
						+ "ck.fJisuPositionForthFee, ck.fJisuPositionFifthFee, ck.fJisuPositionFirstPageFee, "
						+ "ck.fChupingPositionFirstFee, ck.fChupingPositionSecondFee, ck.fChupingPositionThirdFee, "
						+ "ck.fChupingPositionForthFee, ck.fChupingPositionFifthFee, ck.fChupingPositionFirstPageFee, ps.fPcPosition, "
						+ "ps.fJisuPosition, ps.fChupingPosition, ck.fCollectMethod, ps.fCreateDate, ck.fCurrentPosition, ck.fChupingCurrentPosition, ck.fJisuCurrentPosition ";

			sql = String.format(sqlTemplate, sqlFields, condition);
//			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;
			
			if ("All".equals(statType)){
					sql = String.format(sqlTemplate, sqlFields, condition);
			}

			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setDate(1, Date.valueOf(dateCondition.trim()));
			rs = ps.executeQuery();

			while (rs.next()) {
				CustomerKeywordPositionView customerKeywordPositionView = getCustomerKeywordPositionView(conn, rs);
				if("All".equals(statType) || customerKeywordPositionView.getSubTotal() > 0){
					customerKeywordPositionViews.add(customerKeywordPositionView);
				}
			}
			recordCount = customerKeywordPositionViews.size();
			return customerKeywordPositionViews;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	private CustomerKeywordPositionView getCustomerKeywordPositionView(Connection conn, ResultSet rs) throws Exception {
		CustomerKeywordPositionView customerKeywordPositionView = new CustomerKeywordPositionView();
		customerKeywordPositionView.setUuid(rs.getInt("fUuid"));
		customerKeywordPositionView.setCustomerUuid(rs.getInt("fCustomerUuid"));
		customerKeywordPositionView.setContactPerson(rs.getString("fContactPerson"));
		customerKeywordPositionView.setKeyword(rs.getString("fKeyword"));
		customerKeywordPositionView.setRemarks(rs.getString("fRemarks"));
		customerKeywordPositionView.setUrl(rs.getString("fUrl"));
		customerKeywordPositionView.setPhoneUrl(rs.getString("fPhoneUrl"));
		
		customerKeywordPositionView.setOriginalUrl(rs.getString("fOriginalUrl"));
		customerKeywordPositionView.setOriginalPhoneUrl(rs.getString("fOriginalPhoneUrl"));
		customerKeywordPositionView.setPcPosition(rs.getInt("fPcPosition"));
		
		customerKeywordPositionView.setJisuPosition(rs.getInt("fJisuPosition"));
		customerKeywordPositionView.setChupingPosition(rs.getInt("fChupingPosition"));

		customerKeywordPositionView.setCurrentPosition(rs.getInt("fCurrentPosition"));
		customerKeywordPositionView.setJisuCurrentPosition(rs.getInt("fJisuCurrentPosition"));
		customerKeywordPositionView.setChupingCurrentPosition(rs.getInt("fChupingCurrentPosition"));

		customerKeywordPositionView.setCollectMethod(rs.getString("fCollectMethod"));
		
		customerKeywordPositionView.setPositionFirstFee(rs.getDouble("fPositionFirstFee"));
		customerKeywordPositionView.setPositionSecondFee(rs.getDouble("fPositionSecondFee"));
		customerKeywordPositionView.setPositionThirdFee(rs.getDouble("fPositionThirdFee"));
		customerKeywordPositionView.setPositionForthFee(rs.getDouble("fPositionForthFee"));
		customerKeywordPositionView.setPositionFifthFee(rs.getDouble("fPositionFifthFee"));
		customerKeywordPositionView.setPositionFirstPageFee(rs.getDouble("fPositionFirstPageFee"));

		customerKeywordPositionView.setJisuPositionFirstFee(rs.getDouble("fJisuPositionFirstFee"));
		customerKeywordPositionView.setJisuPositionSecondFee(rs.getDouble("fJisuPositionSecondFee"));
		customerKeywordPositionView.setJisuPositionThirdFee(rs.getDouble("fJisuPositionThirdFee"));
		customerKeywordPositionView.setJisuPositionForthFee(rs.getDouble("fJisuPositionForthFee"));
		customerKeywordPositionView.setJisuPositionFifthFee(rs.getDouble("fJisuPositionFifthFee"));
		customerKeywordPositionView.setJisuPositionFirstPageFee(rs.getDouble("fJisuPositionFirstPageFee"));
		
		customerKeywordPositionView.setChupingPositionFirstFee(rs.getDouble("fChupingPositionFirstFee"));
		customerKeywordPositionView.setChupingPositionSecondFee(rs.getDouble("fChupingPositionSecondFee"));
		customerKeywordPositionView.setChupingPositionThirdFee(rs.getDouble("fChupingPositionThirdFee"));
		customerKeywordPositionView.setChupingPositionForthFee(rs.getDouble("fChupingPositionForthFee"));
		customerKeywordPositionView.setChupingPositionFifthFee(rs.getDouble("fChupingPositionFifthFee"));
		customerKeywordPositionView.setChupingPositionFirstPageFee(rs.getDouble("fChupingPositionFirstPageFee"));
		customerKeywordPositionView.setCreateDate(rs.getDate("fCreateDate"));
		
		return customerKeywordPositionView;
	}
	
	private CustomerKeywordPositionHistoryLogView getCustomerKeywordPositionHistoryLogView(Connection conn, ResultSet rs) throws Exception {
		CustomerKeywordPositionHistoryLogView customerKeywordPositionView = new CustomerKeywordPositionHistoryLogView();
		customerKeywordPositionView.setUuid(rs.getInt("fUuid"));
		customerKeywordPositionView.setCustomerUuid(rs.getInt("fCustomerUuid"));
		customerKeywordPositionView.setContactPerson(rs.getString("fContactPerson"));
		customerKeywordPositionView.setKeyword(rs.getString("fKeyword"));
		customerKeywordPositionView.setRemarks(rs.getString("fRemarks"));
		customerKeywordPositionView.setUrl(rs.getString("fUrl"));
		customerKeywordPositionView.setPhoneUrl(rs.getString("fPhoneUrl"));
		
		customerKeywordPositionView.setOriginalUrl(rs.getString("fOriginalUrl"));
		customerKeywordPositionView.setOriginalPhoneUrl(rs.getString("fOriginalPhoneUrl"));
		customerKeywordPositionView.setPcPosition(rs.getInt("fPcPosition"));
		
		customerKeywordPositionView.setJisuPosition(rs.getInt("fJisuPosition"));
		customerKeywordPositionView.setChupingPosition(rs.getInt("fChupingPosition"));

		customerKeywordPositionView.setCollectMethod(rs.getString("fCollectMethod"));
		
		customerKeywordPositionView.setPositionFirstFee(rs.getDouble("fPositionFirstFee"));
		customerKeywordPositionView.setPositionSecondFee(rs.getDouble("fPositionSecondFee"));
		customerKeywordPositionView.setPositionThirdFee(rs.getDouble("fPositionThirdFee"));
		customerKeywordPositionView.setPositionForthFee(rs.getDouble("fPositionForthFee"));
		customerKeywordPositionView.setPositionFifthFee(rs.getDouble("fPositionFifthFee"));
		customerKeywordPositionView.setPositionFirstPageFee(rs.getDouble("fPositionFirstPageFee"));

		customerKeywordPositionView.setJisuPositionFirstFee(rs.getDouble("fJisuPositionFirstFee"));
		customerKeywordPositionView.setJisuPositionSecondFee(rs.getDouble("fJisuPositionSecondFee"));
		customerKeywordPositionView.setJisuPositionThirdFee(rs.getDouble("fJisuPositionThirdFee"));
		customerKeywordPositionView.setJisuPositionForthFee(rs.getDouble("fJisuPositionForthFee"));
		customerKeywordPositionView.setJisuPositionFifthFee(rs.getDouble("fJisuPositionFifthFee"));
		customerKeywordPositionView.setJisuPositionFirstPageFee(rs.getDouble("fJisuPositionFirstPageFee"));
		
		customerKeywordPositionView.setChupingPositionFirstFee(rs.getDouble("fChupingPositionFirstFee"));
		customerKeywordPositionView.setChupingPositionSecondFee(rs.getDouble("fChupingPositionSecondFee"));
		customerKeywordPositionView.setChupingPositionThirdFee(rs.getDouble("fChupingPositionThirdFee"));
		customerKeywordPositionView.setChupingPositionForthFee(rs.getDouble("fChupingPositionForthFee"));
		customerKeywordPositionView.setChupingPositionFifthFee(rs.getDouble("fChupingPositionFifthFee"));
		customerKeywordPositionView.setChupingPositionFirstPageFee(rs.getDouble("fChupingPositionFirstPageFee"));
		
		customerKeywordPositionView.setCreateDate(rs.getDate("fCreateDate"));
		customerKeywordPositionView.setType(rs.getString("fType"));
		customerKeywordPositionView.setIp(rs.getString("fIp"));
		customerKeywordPositionView.setPositionNumber(rs.getInt("fPositionNumber"));
		customerKeywordPositionView.setCreateTime(rs.getTimestamp("fCreateTime"));
		return customerKeywordPositionView;
	}
	
	public ArrayList<CustomerKeywordPositionHistoryLogView> searchCustomerKeywordPositionHistoryLogViews(String datasourceName, int pageSize, int curPage,
			String customerUuid, String customerName, String date, String statType, int recCount) throws Exception {
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

		String condition = Utils.isNullOrEmpty(customerUuid) ? (Utils.isNullOrEmpty(customerName) ? "" : (" AND c.fContactPerson like '" + customerName + "%'")) : " AND ck.fCustomerUuid = " + customerUuid + " ";
		String dateCondition = Utils.isNullOrEmpty(date) ? Utils.getCurrentDate() : date.trim();
		ArrayList<CustomerKeywordPositionHistoryLogView> customerKeywordPositionHistoryLogViews = new ArrayList();
		try {
			conn = DBUtil.getConnection(datasourceName);
			String sqlTemplate = " SELECT %s "
					+ "FROM t_customer_keyword ck, t_ck_position_summary ps, t_customer c, t_ck_position_index_log l "
					+ "WHERE ps.fCustomerKeywordUuid = ck.fUuid AND l.fCustomerKeywordUuid = ck.fUuid "
					+ "%s"
					+ "AND ck.fCustomerUuid = c.fUuid "
					+ "AND l.fCreateTime > DATE_ADD(NOW(), INTERVAL -1 DAY) "
					+ "AND ck.fStatus = 1 "
					+ "AND ps.fCreateDate = ? "
					+ "AND ((ck.fUrl IS NOT NULL "
					+ "AND ps.fPCPosition > 0 "
					+ "AND ps.fPCPosition < 11) "
					+ "OR (ck.fPhoneUrl IS NOT NULL "
					+ "AND ((ps.fJisuPosition > 0 "
					+ "        AND ps.fJisuPosition < 11) "
					+ "        OR (ps.fChupingPosition > 0 "
					+ "            AND ps.fChupingPosition < 11)))) "
					+ "            ORDER BY c.fContactPerson, ck.fCustomerUuid, ck.fUuid, l.fType, l.fCreateTime ";

			String sqlFields = " ck.fUuid, ck.fCustomerUuid, c.fContactPerson, ck.fRemarks, ck.fKeyword, ck.furl, ck.fPhoneUrl, ck.fOriginalUrl, "
					+ "ck.fOriginalPhoneUrl, ck.fPositionFirstFee, ck.fPositionSecondFee, ck.fPositionThirdFee, ck.fPositionForthFee, "
					+ "ck.fPositionFifthFee, ck.fPositionFirstPageFee, ck.fJisuPositionFirstFee, ck.fJisuPositionSecondFee, ck.fJisuPositionThirdFee, "
					+ "ck.fJisuPositionForthFee, ck.fJisuPositionFifthFee, ck.fJisuPositionFirstPageFee, "
					+ "ck.fChupingPositionFirstFee, ck.fChupingPositionSecondFee, ck.fChupingPositionThirdFee, "
					+ "ck.fChupingPositionForthFee, ck.fChupingPositionFifthFee, ck.fChupingPositionFirstPageFee, ps.fPcPosition, "
					+ "ps.fJisuPosition, ps.fChupingPosition, ck.fCollectMethod, ps.fCreateDate, l.fType, l.fPositionNumber, l.fIp,l.fCreateTime   ";
			
			sql = String.format(sqlTemplate, sqlFields, condition);
//			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;
			
			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setDate(1, Date.valueOf(dateCondition.trim()));
			rs = ps.executeQuery();

			while (rs.next()) {
				CustomerKeywordPositionHistoryLogView customerKeywordPositionView = getCustomerKeywordPositionHistoryLogView(conn, rs);
				if(customerKeywordPositionView.getSubTotal() > 0){
					customerKeywordPositionHistoryLogViews.add(customerKeywordPositionView);
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
	
	public void supplementPositionLog(List<CustomerKeywordPositionView> customerKeywordPositionViews, List<CustomerKeywordPositionHistoryLogView> customerKeywordPositionHistoryLogViews){
		if(!Utils.isEmpty(customerKeywordPositionViews) && !Utils.isEmpty(customerKeywordPositionHistoryLogViews)){
			for(CustomerKeywordPositionView customerKeywordPositionView : customerKeywordPositionViews){
				if(customerKeywordPositionView.getPcFee() > 0 || customerKeywordPositionView.getChupingFee() > 0 || customerKeywordPositionView.getJisuFee() > 0){
					StringBuilder pcPositionLog = new StringBuilder();
					StringBuilder chupingPositionLog = new StringBuilder();
					StringBuilder jisuPositionLog = new StringBuilder();
					for(CustomerKeywordPositionHistoryLogView logView : customerKeywordPositionHistoryLogViews){
						if(customerKeywordPositionView.getPcFee() > 0 && customerKeywordPositionView.getUuid() == logView.getUuid() && "PC".equals(logView.getType())){
							if(pcPositionLog.toString().equals("")){
								pcPositionLog.append(logView.getPositionNumber());
							}else{
								pcPositionLog.append("," + logView.getPositionNumber());
							}
						}
						if(customerKeywordPositionView.getChupingFee() > 0 && customerKeywordPositionView.getUuid() == logView.getUuid() && "Chuping".equals(logView.getType())){
							if(chupingPositionLog.toString().equals("")){
								chupingPositionLog.append(logView.getPositionNumber());
							}else{
								chupingPositionLog.append("," + logView.getPositionNumber());
							}
						}
						if(customerKeywordPositionView.getJisuFee() > 0 && customerKeywordPositionView.getUuid() == logView.getUuid() && "Jisu".equals(logView.getType())){
							if(jisuPositionLog.toString().equals("")){
								jisuPositionLog.append(logView.getPositionNumber());
							}else{
								jisuPositionLog.append("," + logView.getPositionNumber());
							}
						}
					}
					customerKeywordPositionView.setPcPositionLog(pcPositionLog.toString());
					customerKeywordPositionView.setChupingPositionLog(chupingPositionLog.toString());
					customerKeywordPositionView.setJisuPositionLog(jisuPositionLog.toString());
				}
			}
		}
	}
}