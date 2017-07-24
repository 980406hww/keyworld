 package com.keymanager.manager;
 
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.keymanager.db.DBUtil;
import com.keymanager.enums.KeywordType;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.value.KeywordVO;
import com.keymanager.value.WebsiteSummaryVO;
 
 public class KeywordManager
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
   
   public KeywordVO getKeywordVO(ResultSet rs) throws SQLException{
  	 KeywordVO value = new KeywordVO();
     value.setUuid(rs.getInt("fUuid"));
     value.setKeyword(rs.getString("fKeyword"));
     value.setSearchEngine(rs.getString("fSearchEngine"));
     value.setType(rs.getString("fType"));
     value.setStatus(rs.getInt("fStatus"));
     value.setQuerySearchEngineTime(rs.getTimestamp("fQuerySearchEngineTime"));
     value.setCaptureIndexCountTime(rs.getTimestamp("fCaptureIndexCountTime"));
     value.setCaptureStatus(rs.getInt("fCaptureStatus"));
     value.setCreateTime(rs.getTimestamp("fCreateTime"));
     return value;
   }
   
   public KeywordVO getRandomPublicKeyword(String datasourceName) throws Exception {
  	 String condition = String.format(" and fType = '%s'", KeywordType.PublicKeyword.name());
  	 return getRandomKeyword(datasourceName, condition, "");
   }
   
 	public KeywordVO getRandomKeyword(String datasourceName, String condition, String order) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			conn = DBUtil.getConnection(datasourceName);			
				sql = " select count(1) as recordCount from t_keyword k WHERE EXISTS (SELECT 1 FROM t_keyword_position_url url WHERE url.fKeyword = k.fKeyword) AND k.fStatus = 1 " + condition;

				ps = conn.prepareStatement(sql, 1003, 1007);
				rs = ps.executeQuery();
				if (rs.next()) {
					recordCount = rs.getInt("recordCount");
				}
				Random rd = new Random();
				int startPosition = 0;
				if(recordCount > 0){
					startPosition = rd.nextInt(recordCount);
				}

			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_keyword k WHERE EXISTS (SELECT 1 FROM t_keyword_position_url url WHERE url.fKeyword = k.fKeyword) AND 1=1 " + condition + " " + order;
			sql = sql + " limit " + startPosition + ",1";

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				return getKeywordVO(rs);
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}
 
 	public List<KeywordVO> searchKeywords(String datasourceName, String condition, String order) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);										
			return searchKeywords(conn, condition, order);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}
 	
 	public List<KeywordVO> searchKeywords(Connection conn, String condition, String order) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_keyword WHERE 1=1 " + condition + " " + order;			

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			
			List<KeywordVO> keywordVOs = new ArrayList<KeywordVO>();
			while (rs.next()) {
				keywordVOs.add(getKeywordVO(rs));
			}
			return keywordVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}
 	
 	public List<KeywordVO> searchBeforeNDaysPublickKeywords(Connection conn, int days) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		try {
			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_keyword WHERE fCreateTime <= ? and fType = 'PublicKeyword'";			

			ps = conn.prepareStatement(sql, 1003, 1007);
			Timestamp beforeNDays = new Timestamp(System.currentTimeMillis() - (days * 24 * 60 * 60 * 1000));       
      ps.setTimestamp(1, beforeNDays);
			rs = ps.executeQuery();
			
			List<KeywordVO> keywordVOs = new ArrayList<KeywordVO>();
			while (rs.next()) {
				keywordVOs.add(getKeywordVO(rs));
			}
			return keywordVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}
   
   public void addKeywordVOs(Connection conn, String keywords, String searchEngine, String type) throws Exception{
  	 if (!Utils.isNullOrEmpty(keywords)){
  		 String[] keywordArrays = keywords.trim().split(",");
  		 for(String keyword : keywordArrays){
  			 if(!Utils.isNullOrEmpty(keyword) && !existKeyword(conn, keyword, searchEngine)){
	  			 KeywordVO keywordVO = new KeywordVO(keyword, searchEngine, type);
	  			 addKeywordVO(conn, keywordVO);
  			 }
  		 }
  	 }
   }
 
   public boolean existKeyword(Connection conn, String keyword, String searchEngine) throws Exception
   {
     PreparedStatement ps = null;
     ResultSet rs = null;
     String sql = "";
     try
     {
       String sqlFields = " *  ";
 
       sql = " select " + sqlFields + " from t_keyword where fKeyword = ? and fSearchEngine = ? ";
       sql = sql + " limit " + 1;
 
       ps = conn.prepareStatement(sql, 1003, 1007);
       int i = 1;
       ps.setString(i++, keyword);
       ps.setString(i++, searchEngine);
       rs = ps.executeQuery();
 
       while (rs.next())
       {
         return true;
       }
       return false;
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception("search_keyword_error");
     }
     finally
     {
       DBUtil.closeResultSet(rs);
       DBUtil.closePreparedStatement(ps);
     }
   }
   
   public void addKeywordVO(String dsName, KeywordVO keywordVO)
     throws Exception
   {
  	 Connection conn = null;
     try
     {
    	 conn = DBUtil.getConnection(dsName);
    	 addKeywordVO(conn, keywordVO);
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception("login_error");
     }
     finally
     {
       DBUtil.closeConnection(conn);
     }
   }
   

   public void addKeywordVO(Connection conn, KeywordVO keywordVO)
     throws Exception
   {
     PreparedStatement stmt = null;
     String sql = "";
     try
     {
       sql = "insert into t_keyword(fKeyword, fSearchEngine, fType, fQuerySearchEngineTime, "
      		 + "fCaptureIndexCountTime, fStatus, fCreateTime) values(?, ?, ?, ?, ?, ?, ?)";
 
       stmt = conn.prepareStatement(sql, 1003, 1007);
       int i = 1;
       stmt.setString(i++, keywordVO.getKeyword());
       stmt.setString(i++, keywordVO.getSearchEngine());
       stmt.setString(i++, keywordVO.getType());
       stmt.setTimestamp(i++, keywordVO.getQuerySearchEngineTime());       
       stmt.setTimestamp(i++, keywordVO.getCaptureIndexCountTime());
       stmt.setInt(i++, keywordVO.getStatus());
       stmt.setTimestamp(i++, Utils.getCurrentTimestamp());
 
       stmt.executeUpdate();
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception("login_error");
     }
     finally
     {
       DBUtil.closePreparedStatement(stmt);
     }
   }
   
   public KeywordVO getKeywordVOByUuid(Connection conn, int uuid) throws Exception
   {
     PreparedStatement ps = null;
     ResultSet rs = null;
     String sql = "";
     try
     {
       String sqlFields = " *  ";
 
       sql = " select " + sqlFields + " from t_keyword where fUuid = ?";
       sql = sql + " limit " + 1;
 
       ps = conn.prepareStatement(sql, 1003, 1007);
       int i = 1;
       ps.setInt(i++, uuid);
       rs = ps.executeQuery();
 
       while (rs.next())
       {
         return this.getKeywordVO(rs);
       }
       return null;
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception("search_keyword_error");
     }
     finally
     {
       DBUtil.closeResultSet(rs);
       DBUtil.closePreparedStatement(ps);
     }
   }
  
 	public void updateKeywordCaptureStatus(Connection conn, String uuids) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "update t_keyword set fCaptureStatus = ? where fuuid in (" + uuids + ")";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;			
			stmt.setInt(i++, 1);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}
 	
	public void updateKeywordStatus(String datasourceName) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		String sql = "";
		try {
			conn = DBUtil.getConnection(datasourceName);
			sql = "update t_keyword set fCaptureStatus = ? ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setInt(i++, 0);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		} finally {
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
	}
  
	public void updateKeywordQuerySearchEngineTime(Connection conn, int keywordUuid) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "update t_keyword set fQuerySearchEngineTime = ?, fCaptureStatus = ? where fuuid = ?";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;			
			stmt.setTimestamp(i++, Utils.getCurrentTimestamp());
			stmt.setInt(i++, 0);
			stmt.setInt(i++, keywordUuid);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}
   
	public void updateKeywordCaptureIndexCountTime(Connection conn, int keywordUuid) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "update t_keyword set fCaptureIndexCountTime = ? where fuuid = ?";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;			
			stmt.setTimestamp(i++, Utils.getCurrentTimestamp());
			stmt.setInt(i++, keywordUuid);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}
	
	public void removeKeyword(Connection conn, int keywordUuid) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "delete from t_keyword where fuuid = ?";

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
   
//	public void refreshKeywordPosition(String datasourceName, String data) throws Exception{
//  	 Connection conn = null;
//     try
//     {
//    	 conn = DBUtil.getConnection(datasourceName);
//    	 conn.setAutoCommit(false);
//    	 String[] keywordStrings = data.trim().split(Constants.ROW_SPLITTOR);
//    	 for(String keywordString : keywordStrings){
//    		 if (!Utils.isNullOrEmpty(keywordString)){
//    			 String[] columns = keywordString.split(Constants.HEADER_SPLITTOR);
//    			 int keywordUuid = Integer.parseInt(columns[0]);
//    			 KeywordVO keywordVO = getKeywordVOByUuid(conn, keywordUuid);
//					 this.updateKeywordQuerySearchEngineTime(conn, keywordUuid);
//					 
//					 String[] websites = columns[1].split(Constants.SUB_ROW_SPLITTOR);
//					 List<WebsiteSummaryVO> websiteSummaryVOs = new ArrayList<WebsiteSummaryVO>();
//					 for(String websiteString : websites){
//						 String[] websiteColumns = websiteString.split(Constants.COLUMN_SPLITTOR, 3);
//						 websiteSummaryVOs.add(new WebsiteSummaryVO(websiteColumns[0], websiteColumns[1], websiteColumns[2]));
//					 }
//           if(!Utils.isEmpty(websiteSummaryVOs)){
//	    			 KeywordPositionUrlManager manager = new KeywordPositionUrlManager();
//	    			 manager.deleteKeywordPositionUrlVOs(conn, keywordVO.getKeyword(), keywordVO.getSearchEngine());
//	    			 manager.addKeywordPositionUrlVOs(conn, keywordVO.getKeyword(), keywordVO.getSearchEngine(), websiteSummaryVOs);  
//	    			 
//	    			 CustomerKeywordManager customerKeywordManager = new CustomerKeywordManager();
//	    			 customerKeywordManager.updateCustomerKeywordPositionAndIndex(datasourceName, conn, keywordVO.getKeyword(), websiteSummaryVOs);
//           }
//    		 }
//    	 }
//    	 conn.commit();
//     }
//     catch (Exception e)
//     {
//       e.printStackTrace();
//       throw new Exception(e.getMessage());
//     }
//     finally
//     {
//       DBUtil.closeConnection(conn);
//     }
//   }
   
//	public void updateKeywordIndex(String datasourceName, String data) throws Exception {
//		Connection conn = null;
//		try {
//			conn = DBUtil.getConnection(datasourceName);
//			conn.setAutoCommit(false);
//			String[] rowStrings = data.trim().split(Constants.ROW_SPLITTOR);
//			for (String rowString : rowStrings) {
//				if (!Utils.isNullOrEmpty(rowString)) {
//					String[] columns = rowString.split(Constants.COLUMN_SPLITTOR);
//					int keywordUuid = Integer.parseInt(columns[0]);
//					KeywordVO keywordVO = getKeywordVOByUuid(conn, keywordUuid);
//					this.updateKeywordCaptureIndexCountTime(conn, keywordUuid);
//					int baiduIndexCount = Integer.parseInt(columns[3]);
//					if (baiduIndexCount > 0) {
//						CustomerKeywordManager customerKeywordManager = new CustomerKeywordManager();
//						customerKeywordManager.updateCustomerKeywordIndex(datasourceName, conn, keywordVO.getKeyword(),
//								baiduIndexCount);
//					}
//				}
//			}
//			conn.commit();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new Exception(e.getMessage());
//		} finally {
//			DBUtil.closeConnection(conn);
//		}
//	}
	
   public void removeOldPublicKeywords(String datasourceName, int days) throws Exception{
  	 Connection conn = null;
     try
     {
    	 conn = DBUtil.getConnection(datasourceName);
    	 conn.setAutoCommit(false);
    	 List<KeywordVO> keywordVOs = this.searchBeforeNDaysPublickKeywords(conn, days);
    	 KeywordManager keywordManager = new KeywordManager();
    	 KeywordPositionUrlManager keywordPositionUrlManager = new KeywordPositionUrlManager();
    	 
    	 for(KeywordVO keywordVO : keywordVOs){
    		 keywordManager.removeKeyword(conn, keywordVO.getUuid());
    		 keywordPositionUrlManager.deleteKeywordPositionUrlVOs(conn, keywordVO.getKeyword(), keywordVO.getSearchEngine());
    	 }
    	 conn.commit();
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception(e.getMessage());
     }
     finally
     {
       DBUtil.closeConnection(conn);
     }
   }
   
   public void addPublicKeywords(String datasourceName, String data) throws Exception{
  	 Connection conn = null;
     try
     {
    	 conn = DBUtil.getConnection(datasourceName);
    	 conn.setAutoCommit(false);
    	 String[] keywords = data.trim().split(Constants.COLUMN_SPLITTOR);
    	 KeywordManager manager = new KeywordManager();
    	 for(String keyword : keywords){
    		 if(!Utils.isNullOrEmpty(keyword)){
    			 KeywordVO keywordVO = new KeywordVO(keyword, Constants.SEARCH_ENGINE_BAIDU, KeywordType.PublicKeyword.getCode());
    			 if(!manager.existKeyword(conn, keyword, Constants.SEARCH_ENGINE_BAIDU)){
    				 manager.addKeywordVO(conn, keywordVO);
    			 }
    		 }
    	 }
    	 conn.commit();
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception(e.getMessage());
     }
     finally
     {
       DBUtil.closeConnection(conn);
     }
   }
 }