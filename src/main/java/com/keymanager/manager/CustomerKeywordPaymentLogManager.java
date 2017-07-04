 package com.keymanager.manager;
 
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.keymanager.db.DBUtil;
import com.keymanager.value.CustomerKeywordPaymentLogVO;
 
 public class CustomerKeywordPaymentLogManager
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
   
   public List searchCustomerKeywordPaymentLogs(String datasourceName, int pageSize, int curPage, String condition, String order, int recCount)
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
 
     ArrayList customerKeywordPaymentLogs = new ArrayList();
     try
     {
       conn = DBUtil.getConnection(datasourceName);
       if (recCount != 0)
       {
         sql = " select count(1) as recordCount from t_ck_payment_log  where 1=1 " + condition;
 
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
 
       sql = " select " + sqlFields + " from t_ck_payment_log where 1=1 " + condition + " " + order;
       sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;
 
       ps = conn.prepareStatement(sql, 1003, 1007);
       rs = ps.executeQuery();
 
       while (rs.next())
       {
      	 CustomerKeywordPaymentLogVO customerKeywordPaymentLog = getCustomerKeywordPaymentLogVO(rs);
         customerKeywordPaymentLogs.add(customerKeywordPaymentLog);
       }
       return customerKeywordPaymentLogs;
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
 
   public CustomerKeywordPaymentLogVO getCustomerKeywordPaymentLogVOByUuid(String dataSourceName, String uuid)
     throws Exception
   {
     Connection conn = null;
     PreparedStatement stmt = null;
     ResultSet rs = null;
     String sql = "";
     CustomerKeywordPaymentLogVO customerKeywordPaymentLog = null;
     try
     {
       conn = DBUtil.getConnection(dataSourceName);
 
       sql = "select * from t_ck_payment_log where fUuid = ? ";
 
       stmt = conn.prepareStatement(sql, 1003, 1007);
       stmt.setString(1, uuid);
 
       rs = stmt.executeQuery();
 
       if (rs.next())
       {
      	 customerKeywordPaymentLog = getCustomerKeywordPaymentLogVO(rs);
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
 
     return customerKeywordPaymentLog;
   }
 
   private CustomerKeywordPaymentLogVO getCustomerKeywordPaymentLogVO(ResultSet rs) throws SQLException{
  	 CustomerKeywordPaymentLogVO customerKeywordAccountLog = new CustomerKeywordPaymentLogVO();
  	 customerKeywordAccountLog.setUuid(rs.getInt("fUuid"));
  	 customerKeywordAccountLog.setCustomerKeywordUuid(rs.getInt("fCustomerKeywordUuid"));
  	 customerKeywordAccountLog.setEffectiveFromTime(rs.getTimestamp("fEffectiveFromTime"));
  	 customerKeywordAccountLog.setEffectiveToTime(rs.getTimestamp("fEffectiveToTime"));
  	 customerKeywordAccountLog.setPayable(rs.getDouble("fPayable"));
  	 customerKeywordAccountLog.setRealPaid(rs.getDouble("fRealPaid"));
  	 customerKeywordAccountLog.setPaidTime(rs.getTimestamp("fPaidTime"));
  	 customerKeywordAccountLog.setRemarks(rs.getString("fRemarks"));
  	 customerKeywordAccountLog.setCreateTime(rs.getTimestamp("fCreateTime"));
  	 return customerKeywordAccountLog;
   }
   
   public void addCustomerKeywordPaymentLog(CustomerKeywordPaymentLogVO customerKeywordPaymentLog, String datasourceName)
       throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
       preSql = "insert into t_ck_payment_log(fCustomerKeywordUuid,fEffectiveFromTime,fEffectiveToTime,fPayable,fRealPaid,fPaidTime,fRemarks,fCreateTime)"
      		 + "values(?,?,?,?,?,?,?,now())";
       ps = conn.prepareStatement(preSql);
       int i = 1;
       ps.setInt(i++, customerKeywordPaymentLog.getCustomerKeywordUuid());
       ps.setTimestamp(i++, customerKeywordPaymentLog.getEffectiveFromTime());
       ps.setTimestamp(i++, customerKeywordPaymentLog.getEffectiveToTime());
       ps.setDouble(i++, customerKeywordPaymentLog.getPayable());
       ps.setDouble(i++, customerKeywordPaymentLog.getRealPaid());
       ps.setTimestamp(i++, customerKeywordPaymentLog.getPaidTime());
       ps.setString(i++, customerKeywordPaymentLog.getRemarks());
       ps.executeUpdate();
       CustomerKeywordManager manager = new CustomerKeywordManager();
       manager.updatePaymentRange(conn, customerKeywordPaymentLog.getEffectiveFromTime(), customerKeywordPaymentLog.getEffectiveToTime(), customerKeywordPaymentLog.getCustomerKeywordUuid());
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
 }