package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.keymanager.db.DBUtil;
import com.keymanager.value.CustomerVO;
import com.keymanager.value.RelatedKeywordVO;

public class RelatedKeywordManager {
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

	public void updateRelatedKeywordCompletedCount(String dsName, int uuid, int count) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);

			preSql = "UPDATE t_related_keyword SET fCompletedCount = IF(fOperationDate = CURRENT_DATE(), fCompletedCount + ?, ?), "
					+ " fOperationDate = CURRENT_DATE(), fOperationTime = now() WHERE fUuid = ? ";
			ps = conn.prepareStatement(preSql);

			ps.setInt(1, count);
			ps.setInt(2, count);
			ps.setInt(3, uuid);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	private RelatedKeywordVO getRelatedKeywordVO(Connection conn, ResultSet rs) throws Exception {
		RelatedKeywordVO relatedKeyword = new RelatedKeywordVO();
		relatedKeyword.setUuid(rs.getInt("fUuid"));
		relatedKeyword.setCustomerUuid(rs.getInt("fCustomerUuid"));
		relatedKeyword.setKeyword(rs.getString("fKeyword"));
		relatedKeyword.setRelatedKeyword(rs.getString("fRelatedKeyword"));
		relatedKeyword.setKeywordIndex(rs.getInt("fKeywordIndex"));
		relatedKeyword.setPlannedCount(rs.getInt("fPlannedCount"));
		relatedKeyword.setCompletedCount(rs.getInt("fCompletedCount"));
		relatedKeyword.setOperationTime(rs.getTimestamp("fOperationTime"));
		relatedKeyword.setCaptureIndexTime(rs.getTimestamp("fCaptureIndexTime"));
		relatedKeyword.setCreateTime(rs.getTimestamp("fCreateTime"));
		return relatedKeyword;
	}
	
	public RelatedKeywordVO getRelatedKeywordByUuid(String dsName, String uuid) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			conn = DBUtil.getConnection(dsName);
			String sql = "select * from t_related_keyword where fUuid = ? ";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, uuid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				return getRelatedKeywordVO(conn, rs);
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
			String sql = "SELECT * FROM t_related_keyword rk WHERE rk.fPlannedCount > rk.fCompletedCount "
					+ " OR rk.fOperationDate < CURRENT_DATE() ORDER BY rk.fOperationTime LIMIT 1 ";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			rs = stmt.executeQuery();
			if (rs.next()) {
				RelatedKeywordVO rk = getRelatedKeywordVO(conn, rs);
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

	public List<RelatedKeywordVO> searchRelatedKeywordVOs(String dsName, String condition) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		ArrayList<RelatedKeywordVO> relatedKeywordVOs = new ArrayList<RelatedKeywordVO>();
		try {
			conn = DBUtil.getConnection(dsName);

			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_related_keyword where 1=1 " + condition + " ";

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			while (rs.next()) {
				relatedKeywordVOs.add(getRelatedKeywordVO(conn, rs));
			}
			return relatedKeywordVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search Customer Error!");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}
	
//	public void updateRelatedKeywordVO(String dsName, RelatedKeywordVO customer) throws Exception {
//		Connection conn = null;
//		PreparedStatement ps = null;
//
//		String preSql = null;
//		try {
//			conn = DBUtil.getConnection(dsName);
//
//			preSql = "update t_customer set fContactPerson=?,fQQ=?,fEmail=?,fTelphone=?,fAlipay=?,fPaidFee=?,fRemark=?,fType=?,fStatus=?,fUserID=?,fUpdateTime = now() where fUuid = ? ";
//			ps = conn.prepareStatement(preSql);
//			int i = 1;
//			ps.setString(i++, customer.getContactPerson());
//			ps.setString(i++, customer.getQq());
//			ps.setString(i++, customer.getEmail());
//			ps.setString(i++, customer.getTelphone());
//			ps.setString(i++, customer.getAlipay());
//			ps.setDouble(i++, customer.getPaidFee());
//			ps.setString(i++, customer.getRemark());
//			ps.setString(i++, customer.getType());
//			ps.setInt(i++, customer.getStatus());
//			ps.setString(i++, customer.getUserID());
//			ps.setInt(i++, customer.getUuid());
//
//			ps.executeUpdate();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e);
//		} finally {
//			DBUtil.closePreparedStatement(ps);
//			DBUtil.closeConnection(conn);
//		}
//	}

	public int addRelatedKeywordVO(String dsName, RelatedKeywordVO relatedKeywordVO) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);

			preSql = "INSERT t_related_keyword(fCustomerUuid, fKeywordIndex, fKeyword, fRelatatedKeyword, "
					+ " fPlannedCount, fCompletedCount, fOperationDate, fOperationTime, fCaptureIndexTime, fCreateTime) VALUES("
					+ " ?, ?, ?, ?, ?, ?,current_date(), now(), now(), now())";
			ps = conn.prepareStatement(preSql, Statement.RETURN_GENERATED_KEYS);

			int i = 1;
			ps.setInt(i++, relatedKeywordVO.getCustomerUuid());
			ps.setInt(i++, relatedKeywordVO.getKeywordIndex());
			ps.setString(i++, relatedKeywordVO.getKeyword());
			ps.setString(i++, relatedKeywordVO.getRelatedKeyword());
			ps.setInt(i++, relatedKeywordVO.getPlannedCount());
			ps.setInt(i++, relatedKeywordVO.getCompletedCount());
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

	public void deleteRelatedKeywordVO(String dsName, int uuid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);
			preSql = "delete from t_related_keyword where fUuid = ? ";
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