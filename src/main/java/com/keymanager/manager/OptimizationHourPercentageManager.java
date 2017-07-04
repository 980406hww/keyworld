package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.keymanager.db.DBUtil;
import com.keymanager.value.OptimizationHourPercentageVO;

public class OptimizationHourPercentageManager {
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

	public OptimizationHourPercentageVO getOptimizationHourPercentageVO(Connection conn, int hour) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from t_optimization_hour_percentage where fHour > ? order by fHour limit 1 ";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setInt(1, hour);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return getOptimizationHourPercentageVO(conn, rs);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}
	}

	private OptimizationHourPercentageVO getOptimizationHourPercentageVO(Connection conn, ResultSet rs) throws Exception {
		OptimizationHourPercentageVO optimizationHourPercentageVO = new OptimizationHourPercentageVO();
		optimizationHourPercentageVO.setUuid(rs.getInt("fUuid"));
		optimizationHourPercentageVO.setHour(rs.getInt("fHour"));
		optimizationHourPercentageVO.setPercentage(rs.getDouble("fPercentage"));
		return optimizationHourPercentageVO;
	}
}