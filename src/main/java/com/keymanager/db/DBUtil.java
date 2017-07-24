 package com.keymanager.db;
 
 import java.sql.CallableStatement;
 import java.sql.Connection;
 import java.sql.PreparedStatement;
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.sql.Statement;
 import javax.naming.Context;
 import javax.naming.InitialContext;
 import javax.naming.NamingException;
 import javax.sql.DataSource;
 
 public class DBUtil
 {
   private static DataSource dsCommon = null;
 
   public static void closeConnection(Connection conn)
   {
     try
     {
       if ((conn != null) && (!conn.isClosed()))
       {
         conn.close();
       }
     }
     catch (Exception localException)
     {
     }
   }
 
   public static void closePreparedStatement(PreparedStatement ps)
   {
     try
     {
       if (ps != null)
       {
         ps.close();
         ps = null;
       }
     }
     catch (Exception localException)
     {
     }
   }
 
   public static void closeStatement(Statement stmt)
   {
     try
     {
       if (stmt != null)
       {
         stmt.close();
         stmt = null;
       }
     }
     catch (Exception localException)
     {
     }
   }
 
   public static void closeResultSet(ResultSet rs)
   {
     try
     {
       if (rs != null)
       {
         rs.close();
         rs = null;
       }
     }
     catch (Exception localException)
     {
     }
   }
 
   public static void closeCallableStatement(CallableStatement cs)
   {
     try
     {
       if (cs != null)
       {
         cs.close();
         cs = null;
       }
     }
     catch (Exception localException)
     {
     }
   }
 
   public static Connection getConnection()
     throws SQLException, NamingException
   {
     if (dsCommon == null)
     {
       Context envContext = (Context)new InitialContext().lookup("java:comp/env");
       dsCommon = (DataSource)envContext.lookup("jdbc/wyc");
     }
     return dsCommon.getConnection();
   }
 
   public static Connection getConnection(String dsName) throws SQLException, NamingException
   {
     if ((dsName == null) || (dsName.equals("")))
     {
       return null;
     }
 
     DataSource ds = null;
     Context envContext = (Context)new InitialContext().lookup("java:comp/env");
     ds = (DataSource)envContext.lookup("jdbc/" + dsName);
     return ds.getConnection();
   }
 }