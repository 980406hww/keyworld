 package com.keymanager.manager;
 
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.keymanager.db.DBUtil;
import com.keymanager.value.KeywordContactVO;
 
 public class KeywordContactManager
 {
   private static int recordCount;
   private static int currPage = 0;
   private static int pageCount = 0;
 
   public static int getRecordCount()
   {
     return recordCount;
   }
 
   public static int getCurrPage()
   {
     return currPage;
   }
 
   public static int getPageCount()
   {
     return pageCount;
   }
   
   public List<KeywordContactVO> searchKeywordContactVOs(String datasourceName, int pageSize, int curPage,
 			String condition, String order, int recCount) throws Exception {
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

 		List keywordContactVOs = new ArrayList();
 		try {
 			conn = DBUtil.getConnection(datasourceName);
 			if (recCount != 0) {
 				sql = " select count(1) as recordCount FROM t_keyword_contact where 1 = 1 "
 						+ condition;

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

 			String sqlFields = " * ";

 			sql = " select " + sqlFields + " FROM t_keyword_contact where 1 = 1  "
 					+ condition + " " + order;
 			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;

 			ps = conn.prepareStatement(sql, 1003, 1007);
 			rs = ps.executeQuery();

 			while (rs.next()) {
 				KeywordContactVO keywordContactVO = getKeywordContactVO(rs);
 				keywordContactVOs.add(keywordContactVO);
 			}
 			return keywordContactVOs;
 		} catch (Exception e) {
 			e.printStackTrace();
 			throw new Exception("search_keyword_error");
 		} finally {
 			DBUtil.closeResultSet(rs);
 			DBUtil.closePreparedStatement(ps);
 			DBUtil.closeConnection(conn);
 		}
 	}
   
   public KeywordContactVO getKeywordContactVO(ResultSet rs) throws SQLException{
  	 KeywordContactVO keywordContactVO = new KeywordContactVO();
     keywordContactVO.setUuid(rs.getInt("fUuid"));
     keywordContactVO.setKeyword(rs.getString("fKeyword"));
     keywordContactVO.setSearchLink(rs.getString("fSearchLink"));
     keywordContactVO.setUrl(rs.getString("fUrl"));
     keywordContactVO.setContactPerson(rs.getString("fContactRen"));
     keywordContactVO.setQq(rs.getString("fQq"));
     keywordContactVO.setEmail(rs.getString("fEmail"));
     keywordContactVO.setPhone(rs.getString("fPhone"));
     keywordContactVO.setMobile(rs.getString("fMobile"));
     keywordContactVO.setUserName(rs.getString("fUserName"));
     keywordContactVO.setPassword(rs.getString("fPassword"));
     keywordContactVO.setCity(rs.getString("fCity"));
     keywordContactVO.setGroup(rs.getString("fGroup"));
     keywordContactVO.setClicked(rs.getBoolean("fIsClick"));
     keywordContactVO.setDomain(rs.getBoolean("fIsDomain"));
     keywordContactVO.setPosition(rs.getInt("fPosition"));
     keywordContactVO.setLastCaptureTime(rs.getTimestamp("fLastCaijiTime"));
     keywordContactVO.setCreateTime(rs.getTimestamp("fCreateTime"));
     return keywordContactVO;
   }
   
 	public List<KeywordContactVO> searchKeywordContactVOs(String datasourceName, String condition, String order) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);										
			return searchKeywordContactVOs(conn, condition, order);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
 	
 	public List<KeywordContactVO> searchKeywordContactVOs(Connection conn, String condition, String order) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_keyword_contact WHERE 1=1 " + condition + " " + order;			

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			
			List<KeywordContactVO> keywordContactVOs = new ArrayList<KeywordContactVO>();
			while (rs.next()) {
				keywordContactVOs.add(getKeywordContactVO(rs));
			}
			return keywordContactVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("searchKeywordContactVOs");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}
 	
	public void markAsClick(String dsName, int uuid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(dsName);
			preSql = "UPDATE t_keyword_contact SET fIsClick = 1 WHERE fUuid = ? ";
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