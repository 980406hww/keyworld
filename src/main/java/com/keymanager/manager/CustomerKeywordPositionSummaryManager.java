package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.keymanager.db.DBUtil;
import com.keymanager.util.Constants;
import com.keymanager.value.CustomerKeywordPositionIndexLogVO;
import com.keymanager.value.CustomerKeywordPositionSummaryVO;

public class CustomerKeywordPositionSummaryManager {
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

	public void prepareCustomerKeywordPositionSummary(Connection conn, CustomerKeywordPositionIndexLogVO positionIndexLogVO) throws Exception{
		CustomerKeywordPositionSummaryVO customerKeywordPositionSummaryVO = findCustomerKeywordPositionSummaryVO(conn, positionIndexLogVO.getCustomerKeywordUuid());
		if(customerKeywordPositionSummaryVO != null){
			updateCustomerKeywordPositionSummaryVO(customerKeywordPositionSummaryVO, positionIndexLogVO);
			this.updateCustomerKeywordPositionSummaryVO(conn, customerKeywordPositionSummaryVO);
		}else{
			customerKeywordPositionSummaryVO = createCustomerKeywordPositionSummaryVO(positionIndexLogVO);
			this.addCustomerKeywordPositionSummaryVO(conn, customerKeywordPositionSummaryVO);
		}
	}
	
	public void updateCustomerKeywordPositionSummaryVO(CustomerKeywordPositionSummaryVO customerKeywordPositionSummaryVO, CustomerKeywordPositionIndexLogVO positionIndexLogVO){
		if(Constants.BAIDU_TYPE_PC.equals(positionIndexLogVO.getType())){
			if(customerKeywordPositionSummaryVO.getPcPosition() <= 0 || (positionIndexLogVO.getPositionNumber() > 0 && customerKeywordPositionSummaryVO.getPcPosition() > positionIndexLogVO.getPositionNumber())){
				customerKeywordPositionSummaryVO.setPcPosition(positionIndexLogVO.getPositionNumber());
			}
		}else if(Constants.BAIDU_TYPE_JISU.equals(positionIndexLogVO.getType())){
			if(customerKeywordPositionSummaryVO.getJisuPosition() <= 0 || (positionIndexLogVO.getPositionNumber() > 0 && customerKeywordPositionSummaryVO.getJisuPosition() > positionIndexLogVO.getPositionNumber())){
				customerKeywordPositionSummaryVO.setJisuPosition(positionIndexLogVO.getPositionNumber());
			}
		}else{
			if(customerKeywordPositionSummaryVO.getChupingPosition() <= 0 || (positionIndexLogVO.getPositionNumber() > 0 && customerKeywordPositionSummaryVO.getChupingPosition() > positionIndexLogVO.getPositionNumber())){
				customerKeywordPositionSummaryVO.setChupingPosition(positionIndexLogVO.getPositionNumber());
			}
		}
	}
	
	public CustomerKeywordPositionSummaryVO createCustomerKeywordPositionSummaryVO(CustomerKeywordPositionIndexLogVO positionIndexLogVO){
		CustomerKeywordPositionSummaryVO customerKeywordPositionSummaryVO = new CustomerKeywordPositionSummaryVO();
		customerKeywordPositionSummaryVO.setCustomerKeywordUuid(positionIndexLogVO.getCustomerKeywordUuid());
		customerKeywordPositionSummaryVO.setChupingPosition(-1);
		customerKeywordPositionSummaryVO.setJisuPosition(-1);
		customerKeywordPositionSummaryVO.setPcPosition(-1);
		if(Constants.BAIDU_TYPE_PC.equals(positionIndexLogVO.getType())){
			customerKeywordPositionSummaryVO.setPcPosition(positionIndexLogVO.getPositionNumber());
		}else if(Constants.BAIDU_TYPE_JISU.equals(positionIndexLogVO.getType())){
			customerKeywordPositionSummaryVO.setJisuPosition(positionIndexLogVO.getPositionNumber());
		}else{
			customerKeywordPositionSummaryVO.setChupingPosition(positionIndexLogVO.getPositionNumber());
		}
		return customerKeywordPositionSummaryVO;
	}
	
	public CustomerKeywordPositionSummaryVO getCustomerKeywordPositionSummaryVO(ResultSet rs) throws SQLException {
		CustomerKeywordPositionSummaryVO customerKeywordPositionSummaryVO = new CustomerKeywordPositionSummaryVO();
		customerKeywordPositionSummaryVO.setCustomerKeywordUuid(rs.getInt("fCustomerKeywordUuid"));
		customerKeywordPositionSummaryVO.setPcPosition(rs.getInt("fPCPosition"));
		customerKeywordPositionSummaryVO.setJisuPosition(rs.getInt("fJisuPosition"));
		customerKeywordPositionSummaryVO.setChupingPosition(rs.getInt("fChupingPosition"));
		customerKeywordPositionSummaryVO.setCreateDate(rs.getDate("fCreateDate"));
		return customerKeywordPositionSummaryVO;
	}

	public CustomerKeywordPositionSummaryVO findCustomerKeywordPositionSummaryVO(Connection conn, int customerKeywordUuid) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			sql = " select * from t_ck_position_summary ps WHERE ps.fCustomerKeywordUuid = ? and ps.fCreateDate = Current_Date()";

			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setInt(1, customerKeywordUuid);
			rs = ps.executeQuery();

			while (rs.next()) {
				return getCustomerKeywordPositionSummaryVO(rs);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error" + e.getMessage());
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void addCustomerKeywordPositionSummaryVO(Connection conn, CustomerKeywordPositionSummaryVO customerKeywordPositionSummaryVO) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "insert into t_ck_position_summary(fCustomerKeywordUuid, fPCPosition, fJisuPosition, fChupingPosition, fCreateDate) " + "values(?, ?, ?, ?, CURRENT_DATE())";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setInt(i++, customerKeywordPositionSummaryVO.getCustomerKeywordUuid());
			stmt.setInt(i++, customerKeywordPositionSummaryVO.getPcPosition());
			stmt.setInt(i++, customerKeywordPositionSummaryVO.getJisuPosition());
			stmt.setInt(i++, customerKeywordPositionSummaryVO.getChupingPosition());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}

	public void updateCustomerKeywordPositionSummaryVO(Connection conn, CustomerKeywordPositionSummaryVO customerKeywordPositionSummaryVO) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "update t_ck_position_summary set fPcPosition = ?, fJisuPosition = ?, fChupingPosition = ? where fCustomerKeywordUuid = ? and fCreateDate = CURRENT_DATE() ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setInt(i++, customerKeywordPositionSummaryVO.getPcPosition());
			stmt.setInt(i++, customerKeywordPositionSummaryVO.getJisuPosition());
			stmt.setInt(i++, customerKeywordPositionSummaryVO.getChupingPosition());
			stmt.setInt(i++, customerKeywordPositionSummaryVO.getCustomerKeywordUuid());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}

}