package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.keymanager.db.DBUtil;
import com.keymanager.value.ConfigVO;

public class ConfigManager {
	public final static String CONFIG_TYPE_PAGE_PERCENTAGE = "PagePercentage";
	public final static String CONFIG_KEY_PAGE_PC_PERCENTAGE = "PCPercentage";
	public final static String CONFIG_KEY_PAGE_PHONE_PERCENTAGE = "PhonePercentage";
	public final static String CONFIG_KEY_MAX_INVALID_COUNT = "MaxInvalidCount";
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

	public void updateConfigValue(String dataSource, String configType, String key, String value) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dataSource);

			preSql = "update t_config set fValue = ? where fConfigType = ? and fKey = ? ";
			ps = conn.prepareStatement(preSql);

			ps.setString(1, value);
			ps.setString(2, configType);
			ps.setString(3, key);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public ConfigVO getConfig(String dataSource, String configType, String key) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(dataSource);
			return getConfig(conn, configType, key);
		}finally{
			DBUtil.closeConnection(conn);
		}
	}

	public ConfigVO getConfig(Connection conn, String configType, String key) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from t_config where fConfigType = ? and fKey = ? ";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, configType);
			stmt.setString(2, key);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return getConfigVO(conn, rs);
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

	private ConfigVO getConfigVO(Connection conn, ResultSet rs) throws Exception {
		ConfigVO config = new ConfigVO();
		config.setConfigType(rs.getString("fConfigType"));
		config.setKey(rs.getString("fKey"));
		config.setValue(rs.getString("fValue"));
		return config;
	}
}