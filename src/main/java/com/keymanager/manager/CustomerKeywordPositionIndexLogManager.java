 package com.keymanager.manager;
 
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.keymanager.db.DBUtil;
import com.keymanager.value.CustomerKeywordPositionIndexLogVO;
import com.keymanager.value.CustomerKeywordVO;
 
 public class CustomerKeywordPositionIndexLogManager
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
 
   public ArrayList searchCustomerKeywordPositionIndexLogs(String datasourceName, String type, String customerKeywordUuid, int pageSize, int curPage, int recCount)
       throws Exception
   {
  	 return searchCustomerKeywordPositionIndexLogs(datasourceName, pageSize, curPage, " and fType = '" + type + "' and fCustomerKeywordUuid = " + customerKeywordUuid, " order by fCreateTime desc", recCount);
   }
   
   public ArrayList searchCustomerKeywordPositionIndexLogs(String dataSourceName, int pageSize, int curPage, String condition, String order, int recCount)
     throws Exception
   {
     if (pageSize < 0)
     {
       pageSize = 20;
     }
 
     if (curPage < 0)
     {
       curPage = 1;
     }
 
     Connection conn = null;
     PreparedStatement ps = null;
     ResultSet rs = null;
     String sql = "";
 
     ArrayList customerKeywordAccounts = new ArrayList();
     try
     {
       conn = DBUtil.getConnection(dataSourceName);
       if (recCount != 0)
       {
         sql = " select count(1) as recordCount from t_ck_position_index_log  where 1=1 " + condition;
 
         ps = conn.prepareStatement(sql, 1003, 1007);
         rs = ps.executeQuery();
         if (rs.next())
         {
           recordCount = rs.getInt("recordCount");
         }
 
         pageCount = (int)Math.ceil((recordCount + pageSize - 1) / pageSize);
 
         if (curPage > pageCount)
         {
           curPage = 1;
         }
         currPage = curPage;
       }
 
       String sqlFields = " *  ";
 
       sql = " select " + sqlFields + " from t_ck_position_index_log where 1=1 " + condition + " " + order;
       sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;
 
       ps = conn.prepareStatement(sql, 1003, 1007);
       rs = ps.executeQuery();
 
       while (rs.next())
       {
      	 CustomerKeywordPositionIndexLogVO customerKeywordPositionIndexLog = getCustomerKeywordPositionIndexLog(rs);
         customerKeywordAccounts.add(customerKeywordPositionIndexLog);
       }
       return customerKeywordAccounts;
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
       DBUtil.closeConnection(conn);
     }
   }
 
   public CustomerKeywordPositionIndexLogVO getCustomerKeywordPositionIndexLogByUuid(String dataSourceName, String uuid)
     throws Exception
   {
     Connection conn = null;
     PreparedStatement stmt = null;
     ResultSet rs = null;
     String sql = "";
     CustomerKeywordPositionIndexLogVO customerKeywordPositionIndexLog = null;
     try
     {
       conn = DBUtil.getConnection(dataSourceName);
 
       sql = "select * from t_ck_position_index_log where fUuid = ? ";
 
       stmt = conn.prepareStatement(sql, 1003, 1007);
       stmt.setString(1, uuid);
 
       rs = stmt.executeQuery();
 
       if (rs.next())
       {
      	 customerKeywordPositionIndexLog = getCustomerKeywordPositionIndexLog(rs);
       }
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception("login_error");
     }
     finally
     {
       DBUtil.closeResultSet(rs);
       DBUtil.closePreparedStatement(stmt);
       DBUtil.closeConnection(conn);
     }
 
     return customerKeywordPositionIndexLog;
   }
 
   private CustomerKeywordPositionIndexLogVO getCustomerKeywordPositionIndexLog(ResultSet rs) throws SQLException{
  	 CustomerKeywordPositionIndexLogVO customerKeywordPositionIndexLog = new CustomerKeywordPositionIndexLogVO();
  	 customerKeywordPositionIndexLog.setUuid(rs.getInt("fUuid"));
  	 customerKeywordPositionIndexLog.setType(rs.getString("fType"));
  	 customerKeywordPositionIndexLog.setIp(rs.getString("fIP"));
  	 customerKeywordPositionIndexLog.setCustomerKeywordUuid(rs.getInt("fCustomerKeywordUuid"));
  	 customerKeywordPositionIndexLog.setPositionNumber(rs.getInt("fPositionNumber"));
  	 customerKeywordPositionIndexLog.setIndexCount(rs.getInt("fIndexCount"));
  	 customerKeywordPositionIndexLog.setCreateTime(rs.getTimestamp("fCreateTime"));
  	 return customerKeywordPositionIndexLog;
   }
   
   public void addCustomerKeywordPositionIndexLog(CustomerKeywordPositionIndexLogVO ustomerKeywordPositionIndexLog, String dsName)
       throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(dsName);
       preSql = "insert into t_ck_position_index_log(fCustomerKeywordUuid,fType,fPositionNumber,fIndexCount,fIp,fCreateTime)"
      		 + "values(?,?,?,?,?,now())";
       ps = conn.prepareStatement(preSql);
       int i = 1;
       ps.setInt(i++, ustomerKeywordPositionIndexLog.getCustomerKeywordUuid());
       ps.setString(i++, ustomerKeywordPositionIndexLog.getType());
       ps.setInt(i++, ustomerKeywordPositionIndexLog.getPositionNumber());
       ps.setInt(i++, ustomerKeywordPositionIndexLog.getIndexCount());
       ps.setString(i++, ustomerKeywordPositionIndexLog.getIp());
       ps.executeUpdate();
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception(e);
     }
     finally
     {
       DBUtil.closePreparedStatement(ps);
       DBUtil.closeConnection(conn);
     }
   }
   
   public void addCustomerKeywordPositionIndexLog(Connection conn, CustomerKeywordPositionIndexLogVO customerKeywordPositionIndexLog)
       throws Exception
   {
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       preSql = "insert into t_ck_position_index_log(fType,fCustomerKeywordUuid,fPositionNumber,fIndexCount,fIp,fCreateTime)"
      		 + "values(?,?,?,?,?,now())";
       ps = conn.prepareStatement(preSql);
       int i = 1;
       ps.setString(i++, customerKeywordPositionIndexLog.getType());
       ps.setInt(i++, customerKeywordPositionIndexLog.getCustomerKeywordUuid());
       ps.setInt(i++, customerKeywordPositionIndexLog.getPositionNumber());
       ps.setInt(i++, customerKeywordPositionIndexLog.getIndexCount());
       ps.setString(i++, customerKeywordPositionIndexLog.getIp());
       ps.executeUpdate();
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception(e);
     }
     finally
     {
       DBUtil.closePreparedStatement(ps);
     }
   }
 }