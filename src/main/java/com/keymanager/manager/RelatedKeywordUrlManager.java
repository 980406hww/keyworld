package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.keymanager.db.DBUtil;
import com.keymanager.value.RelatedKeywordUrlVO;

public class RelatedKeywordUrlManager {
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

	public void updateRelatedKeywordRefreshedCount(String dsName, int uuid, String relatedKeywordUrl) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		int i = 1;
		try {
			conn = DBUtil.getConnection(dsName);
			RelatedKeywordUrlVO relatedKeywordUrlVO = getRelatedKeywordByUuid(dsName, uuid + "");
			if(relatedKeywordUrlVO != null){
				if(relatedKeywordUrlVO.getRelatedKeywordUrl() == null){
					preSql = "UPDATE t_related_keyword_url SET fRefreshedCount = fRefreshedCount + 1, fRelatedKeywordUrl = ?, "
							+ " fUpdateTime = now() WHERE fUuid = ? ";
					ps = conn.prepareStatement(preSql);
					ps.setString(i++, relatedKeywordUrl);
				}else{
					preSql = "UPDATE t_related_keyword_url SET fRefreshedCount = fRefreshedCount + 1, "
							+ " fUpdateTime = now() WHERE fUuid = ? ";
					ps = conn.prepareStatement(preSql);
				}
			}
			ps.setInt(i++, uuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	private RelatedKeywordUrlVO getRelatedKeywordUrlVO(Connection conn, ResultSet rs) throws Exception {
		RelatedKeywordUrlVO relatedKeyword = new RelatedKeywordUrlVO();
		relatedKeyword.setUuid(rs.getInt("fUuid"));
		relatedKeyword.setKeyword(rs.getString("fKeyword"));
		relatedKeyword.setRelatedKeyword(rs.getString("fRelatedKeyword"));
		relatedKeyword.setRelatedKeywordUrl(rs.getString("fRelatedKeywordUrl"));
		relatedKeyword.setPlannedCount(rs.getInt("fPlannedCount"));
		relatedKeyword.setRefreshedCount(rs.getInt("fRefreshedCount"));
		relatedKeyword.setUpdateTime(rs.getTimestamp("fUpdateTime"));
		relatedKeyword.setCreateTime(rs.getTimestamp("fCreateTime"));
		return relatedKeyword;
	}
	
	public RelatedKeywordUrlVO getRelatedKeywordByUuid(String dsName, String uuid) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(dsName);
			String sql = "select * from t_related_keyword_url where fUuid = ? ";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, uuid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return getRelatedKeywordUrlVO(conn, rs);
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
	
	public String getRelatedKeywordForOptimize(String dsName) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(dsName);
			String sql = "SELECT * FROM t_related_keyword_url rk WHERE rk.fPlannedCount > rk.fRefreshedCount "
					+ " ORDER BY rk.fUpdateTime LIMIT 1 ";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			rs = stmt.executeQuery();
			if (rs.next()) {
				RelatedKeywordUrlVO rk = getRelatedKeywordUrlVO(conn, rs);
				return rk.toOptimizeString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
		return "";
	}

}