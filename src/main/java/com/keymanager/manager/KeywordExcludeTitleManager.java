package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.keymanager.db.DBUtil;
import com.keymanager.util.Utils;
import com.keymanager.value.KeywordExcludeTitleVO;

public class KeywordExcludeTitleManager {
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

	public KeywordExcludeTitleVO getKeywordExcludeTitleVO(ResultSet rs)
			throws SQLException {
		KeywordExcludeTitleVO value = new KeywordExcludeTitleVO();
		value.setUuid(rs.getInt("fUuid"));
		value.setKeyword(rs.getString("fKeyword"));
		value.setExcludeTitle(rs.getString("fExcludeTitle"));
		value.setCreateTime(rs.getTimestamp("fCreateTime"));
		return value;
	}

	public List<KeywordExcludeTitleVO> searchKeywordExcludeTitleVOs(
			String datasourceName, String keyword) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			return searchKeywordExcludeTitleVOs(conn, keyword);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public List<KeywordExcludeTitleVO> searchKeywordExcludeTitleVOs(
			Connection conn, String keyword) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			sql = " select * from t_keyword_exclude_title WHERE fKeyword = ?";
			ps = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			ps.setString(i++, keyword);
			rs = ps.executeQuery();
			List<KeywordExcludeTitleVO> keywordExcludeTitleVOs = new ArrayList<KeywordExcludeTitleVO>();
			while (rs.next()) {
				keywordExcludeTitleVOs.add(getKeywordExcludeTitleVO(rs));
			}
			return keywordExcludeTitleVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Search KeywordExcludeTitleVO erro");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void addKeywordExcludeTitleVO(String dsName, KeywordExcludeTitleVO keywordVO)
			throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(dsName);
			addKeywordExcludeTitleVO(conn, keywordVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void addKeywordExcludeTitleVO(Connection conn, KeywordExcludeTitleVO keywordVO)
			throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "insert into t_keyword_exclude_title(fKeyword, fExcludeTitle, fCreateTime) values(?, ?, ?)";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setString(i++, keywordVO.getKeyword());
			stmt.setString(i++, keywordVO.getExcludeTitle());
			stmt.setTimestamp(i++, Utils.getCurrentTimestamp());

			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Add KeywordExcludeTitleVO error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}

	public KeywordExcludeTitleVO getKeywordExcludeTitleVOByUuid(Connection conn, int uuid)
			throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";
		try {
			String sqlFields = " *  ";

			sql = " select " + sqlFields
					+ " from t_keyword_exclude_title where fUuid = ?";
			sql = sql + " limit " + 1;

			ps = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			ps.setInt(i++, uuid);
			rs = ps.executeQuery();

			while (rs.next()) {
				return this.getKeywordExcludeTitleVO(rs);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void removeKeyword(Connection conn, int keywordUuid)
			throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "delete from t_keyword_exclude_title where fuuid = ?";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setInt(i++, keywordUuid);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}
}