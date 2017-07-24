package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.keymanager.db.DBUtil;

public class OptimizeDateManager {
	public void updateOptimizeDateAsToday(Connection conn) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "update t_optimize_date set fOptimizeDate = current_date() ";
			ps = conn.prepareStatement(preSql);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public boolean checkOptimizeDateIsToday(Connection conn) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from t_optimize_date where fOptimizeDate = current_date()";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}
	}
}