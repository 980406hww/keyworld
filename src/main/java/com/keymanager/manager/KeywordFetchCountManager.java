package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.keymanager.db.DBUtil;
import com.keymanager.value.KeywordFetchCountVO;

public class KeywordFetchCountManager {
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

	public KeywordFetchCountVO initKeywordFetchCountVO(Connection conn, String groupName) throws Exception {
		KeywordFetchCountVO keywordFetchCountVO = new KeywordFetchCountVO();
		keywordFetchCountVO.setGroup(groupName);
		keywordFetchCountVO.setNormalKeywordFetchedCount(0);
		keywordFetchCountVO.setBigKeywordPercentage(0);
		keywordFetchCountVO.setBigKeywordFetchedCount(0);
		createKeywordFetchCountVO(conn, keywordFetchCountVO);
		return keywordFetchCountVO;
	}
	   
	public void createKeywordFetchCountVO(Connection conn, KeywordFetchCountVO keywordFetchCountVO) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "INSERT INTO t_keyword_fetch_count(fGroup, fNormalKeywordFetchedCount, fBigKeywordPercentage, fBigKeywordFetchedCount) values(?, ?, ?, ?)";
			ps = conn.prepareStatement(preSql);

			ps.setString(1, keywordFetchCountVO.getGroup());
			ps.setInt(2, keywordFetchCountVO.getNormalKeywordFetchedCount());
			ps.setDouble(3, keywordFetchCountVO.getBigKeywordPercentage());
			ps.setInt(4, keywordFetchCountVO.getBigKeywordFetchedCount());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("createKeywordFetchCountVO");
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}
	
	public void updateKeywordFetchCountVO(Connection conn, KeywordFetchCountVO keywordFetchCountVO) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_keyword_fetch_count set fGroup = ?, fNormalKeywordFetchedCount = ?, "
					+ "fBigKeywordPercentage = ?, fBigKeywordFetchedCount =? where fUuid = ? ";
			ps = conn.prepareStatement(preSql);

			ps.setString(1, keywordFetchCountVO.getGroup());
			ps.setInt(2, keywordFetchCountVO.getNormalKeywordFetchedCount());
			ps.setDouble(3, keywordFetchCountVO.getBigKeywordPercentage());
			ps.setInt(4, keywordFetchCountVO.getBigKeywordFetchedCount());
			ps.setInt(5, keywordFetchCountVO.getUuid());
			
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("updateKeywordFetchCountVO");
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public KeywordFetchCountVO getKeywordFetchCountVO(Connection conn, String groupName) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			String sql = "select * from t_keyword_fetch_count where fGroup = ? ";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, groupName);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return getKeywordFetchCountVO(conn, rs);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getKeywordFetchCountVO");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}
	}

	private KeywordFetchCountVO getKeywordFetchCountVO(Connection conn, ResultSet rs) throws Exception {
		KeywordFetchCountVO keywordFetchCountVO = new KeywordFetchCountVO();
		keywordFetchCountVO.setUuid(rs.getInt("fUuid"));
		keywordFetchCountVO.setGroup(rs.getString("fGroup"));
		keywordFetchCountVO.setNormalKeywordFetchedCount(rs.getInt("fNormalKeywordFetchedCount"));
		keywordFetchCountVO.setBigKeywordPercentage(rs.getDouble("fBigKeywordPercentage"));
		keywordFetchCountVO.setBigKeywordFetchedCount(rs.getInt("fBigKeywordFetchedCount"));
		return keywordFetchCountVO;
	}
}