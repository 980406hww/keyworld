package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;

import com.keymanager.db.DBUtil;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordIPVO;

public class CustomerKeywordIPManager {
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

	public void addCustomerKeywordIPVO(Connection conn, CustomerKeywordIPVO customerKeywordIPVO) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "insert into t_customer_keyword_ip(fCustomerKeywordUuid, fIP, fCity, fCreateTime) values(?, ?, ?, ?)";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setInt(i++, customerKeywordIPVO.getCustomerKeywordUuid());
			stmt.setString(i++, customerKeywordIPVO.getIp());
			stmt.setString(i++, customerKeywordIPVO.getCity());
			stmt.setTimestamp(i++, Utils.getCurrentTimestamp());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("addCustomerKeywordIPVO");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}
}