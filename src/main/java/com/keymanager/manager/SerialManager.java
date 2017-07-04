 package com.keymanager.manager;
 
  import com.keymanager.db.DBUtil;
import com.keymanager.value.SerialVO;

 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
import java.util.ArrayList;
 
 public class SerialManager
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
 
   public ArrayList searchSerial(String dsName, int pageSize, int curPage, String condition, String order, int recCount) throws Exception
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
 
     ArrayList values = new ArrayList();
     try
     {
       conn = DBUtil.getConnection(dsName);
 
       if (recCount != 0)
       {
         sql = " select count(1) as recordCount from t_serial  where 1=1 " + condition;
 
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
 
       String sqlOrder = "";
       String sqlFields = " *  ";
 
       sql = " select " + sqlFields + " from t_serial where 1=1 " + condition + " " + order;
       sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;
 
       ps = conn.prepareStatement(sql, 1003, 1007);
       rs = ps.executeQuery();
 
       while (rs.next())
       {
         SerialVO value = new SerialVO();
         value.setUuid(rs.getInt("fUuid"));
         value.setSerial(rs.getString("fSerial"));
         value.setCreateTime(rs.getTimestamp("fCreateTime"));
         values.add(value);
       }
 
       ArrayList localArrayList1 = values;
       return localArrayList1;
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception("search_serial_error");
     }
     finally
     {
       DBUtil.closeResultSet(rs);
       DBUtil.closePreparedStatement(ps);
       DBUtil.closeConnection(conn);
     }
   }
 
   public void addSerial(SerialVO value, String dsName) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(dsName);
 
       preSql = "insert into t_serial(fSerial,fCreateTime) values(?,now())";
       ps = conn.prepareStatement(preSql);
       int i = 1;
       ps.setString(i++, value.getSerial());
 
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
 
   public boolean existSerial(String dsName, String serial)
     throws Exception
   {
     Connection conn = null;
     PreparedStatement stmt = null;
     ResultSet rs = null;
     String sql = "";
     try
     {
       conn = DBUtil.getConnection(dsName);
 
       sql = "select * from t_serial where  fSerial=? ";
 
       stmt = conn.prepareStatement(sql, 1003, 
         1007);
       stmt.setString(1, serial);
 
       rs = stmt.executeQuery();
       return rs.next();
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
   }
 }

/* Location:           D:\baidutop123.com\baiduup.jar
 * Qualified Name:     com.baiduup.manager.SerialManager
 * JD-Core Version:    0.6.0
 */