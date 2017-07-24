package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.db.DBUtil;
import com.keymanager.value.CustomerSimpleVO;
import com.keymanager.value.CustomerVO;

public class CustomerManager {
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

	public void updateCustomerStatus(String dsName, int uuid, int status) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);

			preSql = "update t_customer set fStatus=? where fUuid = ? ";
			ps = conn.prepareStatement(preSql);

			ps.setInt(1, status);
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

	public void updatePaidFee(String dsName, int uuid, double paidFee) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);

			preSql = "update t_customer set fPaidFee = fPaidFee + ? where fUuid = ? ";
			ps = conn.prepareStatement(preSql);

			ps.setDouble(1, paidFee);
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

	public CustomerVO getCustomerByUuid(String dsName, String uuid) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(dsName);
			String sql = "select * from t_customer where fUuid = ? ";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, uuid);
			rs = stmt.executeQuery();
			CustomerKeywordManager manager = new CustomerKeywordManager();
			if (rs.next()) {
				return getCustomer(conn, rs, manager);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
		return null;
	}

	private CustomerVO getCustomer(Connection conn, ResultSet rs, CustomerKeywordManager manager) throws Exception {
		CustomerVO customer = new CustomerVO();
		customer.setUuid(rs.getInt("fUuid"));
		customer.setUserID(rs.getString("fUserID"));
		customer.setContactPerson(rs.getString("fContactPerson"));
		customer.setQq(rs.getString("fQQ"));
		customer.setEntryType(rs.getString("fEntryType"));
		customer.setEmail(rs.getString("fEmail"));
		customer.setTelphone(rs.getString("fTelphone"));
		customer.setAlipay(rs.getString("fAlipay"));
		customer.setPaidFee(rs.getInt("fPaidFee"));
		customer.setRemark(rs.getString("fRemark"));
		customer.setType(rs.getString("fType"));
		customer.setStatus(rs.getInt("fStatus"));
		customer.setCreateTime(rs.getTimestamp("fCreateTime"));
		customer.setUpdateTime(rs.getTimestamp("fUpdateTime"));
		customer.setKeywordCount(manager.getCustomerKeywordCount(conn, customer.getUuid()));
		return customer;
	}
	
	public int getCustomerCount(String datasourceName, String condition) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			conn = DBUtil.getConnection(datasourceName);

			sql = " select count(1) as recordCount from t_customer  where 1=1 " + condition;

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("recordCount");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search Customer Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
		return 0;
	}

	public List<CustomerVO> searchCustomer(String dsName, int pageSize, int curPage, String condition, String order,
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

		ArrayList<CustomerVO> customers = new ArrayList<CustomerVO>();
		try {
			conn = DBUtil.getConnection(dsName);

			if (recCount != 0) {
				sql = " select count(1) as recordCount from t_customer  where 1=1 " + condition;

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

			sql = " select " + sqlFields + " from t_customer where 1=1 " + condition + " " + order;
			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			CustomerKeywordManager manager = new CustomerKeywordManager();
			while (rs.next()) {
				customers.add(getCustomer(conn, rs, manager));
			}
			return customers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search Customer Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public String getCustomers(String dsName) throws Exception {
		List<CustomerVO> customerVOs = searchCustomer(dsName, " and fUserID = 'keyadmin' AND fStatus = 1 order by fContactPerson");
		List<CustomerSimpleVO> customerSimpleVOs = new ArrayList<CustomerSimpleVO>();
		for(CustomerVO customerVO : customerVOs){
			CustomerSimpleVO customerSimpleVO = new CustomerSimpleVO();
			customerSimpleVO.setUuid(customerVO.getUuid());
			customerSimpleVO.setContactPerson(customerVO.getContactPerson());
			customerSimpleVOs.add(customerSimpleVO);
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(customerSimpleVOs);
	}
	
	public List<CustomerVO> searchCustomer(String dsName, String condition) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		ArrayList<CustomerVO> customers = new ArrayList<CustomerVO>();
		try {
			conn = DBUtil.getConnection(dsName);

			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_customer where 1=1 " + condition + " ";

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			CustomerKeywordManager manager = new CustomerKeywordManager();
			while (rs.next()) {
				customers.add(getCustomer(conn, rs, manager));
			}
			return customers;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search Customer Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}
	
	public void updateCustomer(String dsName, CustomerVO customer) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);

			preSql = "update t_customer set fContactPerson=?,fQQ=?,fEmail=?,fTelphone=?,fAlipay=?,fPaidFee=?,fRemark=?,fType=?,fStatus=?,fUserID=?,fUpdateTime = now() where fUuid = ? ";
			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, customer.getContactPerson());
			ps.setString(i++, customer.getQq());
			ps.setString(i++, customer.getEmail());
			ps.setString(i++, customer.getTelphone());
			ps.setString(i++, customer.getAlipay());
			ps.setDouble(i++, customer.getPaidFee());
			ps.setString(i++, customer.getRemark());
			ps.setString(i++, customer.getType());
			ps.setInt(i++, customer.getStatus());
			ps.setString(i++, customer.getUserID());
			ps.setInt(i++, customer.getUuid());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public int addCustomer(CustomerVO customer, String dsName) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);

			preSql = "insert into t_customer(fUserID, fContactPerson,fQQ,fEmail,fTelphone,fAlipay,fPaidFee,fRemark,fType,fStatus,fEntryType,fUpdateTime,fCreateTime) values(?,?,?,?,?,?,?,?,?,?,?,now(),now())";
			ps = conn.prepareStatement(preSql, Statement.RETURN_GENERATED_KEYS);

			int i = 1;
			ps.setString(i++, customer.getUserID());
			ps.setString(i++, customer.getContactPerson());
			ps.setString(i++, customer.getQq());
			ps.setString(i++, customer.getEmail());
			ps.setString(i++, customer.getTelphone());
			ps.setString(i++, customer.getAlipay());
			ps.setDouble(i++, customer.getPaidFee());
			ps.setString(i++, customer.getRemark());
			ps.setString(i++, customer.getType());
			ps.setInt(i++, customer.getStatus());
			ps.setString(i++, customer.getEntryType());
			ps.executeUpdate();
			ResultSet rs = ps.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void deleteCustomer(String dsName, int uuid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);

			preSql = "delete from t_customer where fUuid=? ";
			ps = conn.prepareStatement(preSql);
			ps.setInt(1, uuid);
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