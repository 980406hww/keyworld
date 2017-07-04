 package com.keymanager.manager;
 
 import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.keymanager.db.DBUtil;
import com.keymanager.util.Constants;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordAccountLogVO;
 
 public class CustomerKeywordAccountLogManager
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
 
	public CustomerKeywordAccountLogVO findLastNonPayAllCustomerKeywordAccountLogVO(String dataSourceName,
			String customerKeywordUuid) throws Exception {
		Connection conn = null;
    try
    {
      conn = DBUtil.getConnection(dataSourceName);
      List customerKeywordAccountLogVOs = findAllNonPayAllCustomerKeywordAccountLogVOs(conn, customerKeywordUuid);
  		if (!Utils.isEmpty(customerKeywordAccountLogVOs)){
  			return (CustomerKeywordAccountLogVO)customerKeywordAccountLogVOs.get(0);
  		}
    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new Exception("search_keyword_error");
    }
    finally
    {
      DBUtil.closeConnection(conn);
    }
		return null;
	}
	
	public boolean isNonPayAllCustomerKeywordAccountLogVO(Connection conn,
			String customerKeywordUuid) throws Exception {
		List customerKeywordAccountLogVOs = findAllNonPayAllCustomerKeywordAccountLogVOs(conn, customerKeywordUuid);
		return !Utils.isEmpty(customerKeywordAccountLogVOs);
	}
	
	public List findAllNonPayAllCustomerKeywordAccountLogVOs(Connection conn, String customerKeywordUuid) throws Exception {
		List customerKeywordAccountLogVOs = searchCustomerKeywordAccountLogs(conn, 1, 1, 
				String.format(" and fCustomerKeywordUuid = %s and fStatus in ('%s', '%s')", customerKeywordUuid, Constants.ACCOUNT_LOG_STATUS_UN_PAID, Constants.ACCOUNT_LOG_STATUS_PAID_PARTIALLY), 
				" order by fMonth desc ", 1);
		return customerKeywordAccountLogVOs;
	}
   
   public List searchCustomerKeywordAccountLogs(String dataSourceName, int pageSize, int curPage, String condition, String order, int recCount)
     throws Exception
   {
     Connection conn = null;
     try
     {
       conn = DBUtil.getConnection(dataSourceName);
       
       return searchCustomerKeywordAccountLogs(conn, pageSize, curPage, condition, order, recCount);
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception("search_keyword_error");
     }
     finally
     {
       DBUtil.closeConnection(conn);
     }
   }
   
   public List searchCustomerKeywordAccountLogs(Connection conn, int pageSize, int curPage, String condition, String order, int recCount)
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
       PreparedStatement ps = null;
       ResultSet rs = null;
       String sql = "";
   
       ArrayList customerKeywordAccounts = new ArrayList();
       try
       {
         if (recCount != 0)
         {
           sql = " select count(1) as recordCount from t_ck_account_log  where 1=1 " + condition;
   
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
   
         sql = " select " + sqlFields + " from t_ck_account_log where 1=1 " + condition + " " + order;
         sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;
   
         ps = conn.prepareStatement(sql, 1003, 1007);
         rs = ps.executeQuery();
   
         while (rs.next())
         {
           CustomerKeywordAccountLogVO customerKeyword = getCustomerKeywordAccountLog(rs);
           customerKeywordAccounts.add(customerKeyword);
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
       }
     }
   
	public List getAllCustomerKeywordAccountLogs(Connection conn, List<Integer> customerKeywordIDs) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		ArrayList customerKeywordAccounts = new ArrayList();
		try {
			StringBuilder sb = new StringBuilder();
			for (int id : customerKeywordIDs) {
				if (sb.length() > 0) {
					sb.append(",");
				}
				sb.append(id);
			}
			sql = " SELECT l.* FROM t_customer_keyword ck, t_ck_account_log l WHERE ck.fUuid = l.fCustomerKeywordUuid"
					+ " AND ck.fEffectiveFromTime = l.fEffectiveFromTime AND ck.fEffectiveToTime = l.fEffectiveToTime"
					+ " AND l.fStatus IN ('UnPaid', 'PaidPartially') AND l.fCustomerKeywordUuid IN (" + sb.toString() + ") ";
			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();

			while (rs.next()) {
				CustomerKeywordAccountLogVO customerKeyword = getCustomerKeywordAccountLog(rs);
				customerKeywordAccounts.add(customerKeyword);
			}
			return customerKeywordAccounts;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}
 
   public CustomerKeywordAccountLogVO getCustomerKeywordAccountLogByUuid(String dataSourceName, String uuid)
     throws Exception
   {
  	 CustomerKeywordAccountLogVO customerKeywordAccountLog;
     Connection conn = null;     
     try
     {
       conn = DBUtil.getConnection(dataSourceName);
 
       customerKeywordAccountLog = this.getCustomerKeywordAccountLogByUuid(conn, uuid);
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
 
     return customerKeywordAccountLog;
   }
 
   private CustomerKeywordAccountLogVO getCustomerKeywordAccountLogByUuid(Connection conn, String uuid)
       throws Exception
     {
       PreparedStatement stmt = null;
       ResultSet rs = null;
       String sql = "";
       CustomerKeywordAccountLogVO customerKeywordAccountLog = null;
       try
       {
         sql = "select * from t_ck_account_log where fUuid = ? ";
   
         stmt = conn.prepareStatement(sql, 1003, 1007);
         stmt.setString(1, uuid);
   
         rs = stmt.executeQuery();
   
         if (rs.next())
         {
        	 customerKeywordAccountLog = getCustomerKeywordAccountLog(rs);
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
       }
   
       return customerKeywordAccountLog;
     }
   
   private CustomerKeywordAccountLogVO getCustomerKeywordAccountLog(ResultSet rs) throws SQLException{
  	 CustomerKeywordAccountLogVO customerKeywordAccountLog = new CustomerKeywordAccountLogVO();
  	 customerKeywordAccountLog.setUuid(rs.getInt("fUuid"));
  	 customerKeywordAccountLog.setCustomerKeywordUuid(rs.getInt("fCustomerKeywordUuid"));
  	 customerKeywordAccountLog.setMonth(rs.getString("fMonth"));
  	 customerKeywordAccountLog.setEffectiveFromTime(rs.getTimestamp("fEffectiveFromTime"));
  	 customerKeywordAccountLog.setEffectiveToTime(rs.getTimestamp("fEffectiveToTime"));
  	 customerKeywordAccountLog.setReceivable(rs.getDouble("fReceivable"));
  	 customerKeywordAccountLog.setFirstReceivedTime(rs.getTimestamp("fFirstReceivedTime"));
  	 customerKeywordAccountLog.setFirstRealCollection(rs.getDouble("fFirstRealCollection"));
  	 customerKeywordAccountLog.setSecondReceivedTime(rs.getTimestamp("fSecondReceivedTime"));
  	 customerKeywordAccountLog.setSecondRealCollection(rs.getDouble("fSecondRealCollection"));
  	 customerKeywordAccountLog.setStatus(rs.getString("fStatus"));
  	 customerKeywordAccountLog.setRemarks(rs.getString("fRemarks"));
  	 customerKeywordAccountLog.setUpdateTime(rs.getTimestamp("fUpdateTime"));
  	 customerKeywordAccountLog.setCreateTime(rs.getTimestamp("fCreateTime"));
  	 return customerKeywordAccountLog;
   }
   
   public CustomerKeywordAccountLogVO payForNewAddedCustomerKeywordAccountLog(CustomerKeywordAccountLogVO customerKeywordAccountLog, String datasourceName)
       throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
       try
       {
         preSql = "insert into t_ck_account_log(fCustomerKeywordUuid,fMonth,fEffectiveFromTime,fEffectiveToTime,fReceivable,"
        		 + "fFirstRealCollection, fFirstReceivedTime, fSecondRealCollection, fSecondReceivedTime, fRemarks, fStatus,"
        		 + "fUpdateTime,fCreateTime) values(?,?,?,?,?,?,?,?,?,?,?, now(), now())";
         ps = conn.prepareStatement(preSql, Statement.RETURN_GENERATED_KEYS);
         int i = 1;
         ps.setInt(i++, customerKeywordAccountLog.getCustomerKeywordUuid());
         ps.setString(i++, customerKeywordAccountLog.getMonth());
         ps.setTimestamp(i++, customerKeywordAccountLog.getEffectiveFromTime());
         ps.setTimestamp(i++, customerKeywordAccountLog.getEffectiveToTime());
         ps.setDouble(i++, customerKeywordAccountLog.getReceivable());
         ps.setDouble(i++, customerKeywordAccountLog.getFirstRealCollection());
         ps.setTimestamp(i++, customerKeywordAccountLog.getFirstReceivedTime());
         ps.setDouble(i++, customerKeywordAccountLog.getSecondRealCollection());
         ps.setTimestamp(i++, customerKeywordAccountLog.getSecondReceivedTime());
         ps.setString(i++, customerKeywordAccountLog.getRemarks());
         ps.setString(i++, customerKeywordAccountLog.getStatus());
         ps.executeUpdate();
         ResultSet rs = ps.getGeneratedKeys();
   			 rs.next();
   			 int uuid = rs.getInt(1);
   			 
   			 CustomerKeywordManager manager = new CustomerKeywordManager();
         manager.updateAccountRange(conn, customerKeywordAccountLog.getEffectiveFromTime(), customerKeywordAccountLog.getEffectiveToTime(), customerKeywordAccountLog.getCustomerKeywordUuid(), Constants.BAIDU_TYPE_PC);
        
   			 return this.getCustomerKeywordAccountLogByUuid(conn, uuid + "");
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
 
   public void updateFirstCustomerKeywordAccountLog(CustomerKeywordAccountLogVO customerKeywordAccountLog, String datasourceName) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
 
       preSql = "update t_ck_account_log set fEffectiveFromTime=?,fEffectiveToTime=?,fReceivable=?,fFirstRealCollection=?,fFirstReceivedTime=?,fRemarks=?,"
      		 + "fStatus=?, fUpdateTime=now()"
      		 + " where fUuid = ?";
      		 
       ps = conn.prepareStatement(preSql);
       int i = 1;
       ps.setTimestamp(i++, customerKeywordAccountLog.getEffectiveFromTime());
       ps.setTimestamp(i++, customerKeywordAccountLog.getEffectiveToTime());
       ps.setDouble(i++, customerKeywordAccountLog.getReceivable());
       ps.setDouble(i++, customerKeywordAccountLog.getFirstRealCollection());
       ps.setTimestamp(i++, customerKeywordAccountLog.getFirstReceivedTime());
       ps.setString(i++, customerKeywordAccountLog.getRemarks());
       ps.setString(i++, customerKeywordAccountLog.getStatus());
       ps.setInt(i++, customerKeywordAccountLog.getUuid());
       ps.executeUpdate();
       CustomerKeywordManager manager = new CustomerKeywordManager();
       manager.updateAccountRange(conn, customerKeywordAccountLog.getEffectiveFromTime(), customerKeywordAccountLog.getEffectiveToTime(), customerKeywordAccountLog.getCustomerKeywordUuid(), Constants.BAIDU_TYPE_PC);
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
   
   public void updateSecondCustomerKeywordAccountLog(CustomerKeywordAccountLogVO customerKeywordAccountLog, String datasourceName) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
 
       preSql = "update t_ck_account_log set fEffectiveFromTime=?,fEffectiveToTime=?,fReceivable=?,fSecondRealCollection=?,fSecondReceivedTime=?,fRemarks=?,"
      		 + "fStatus=?, fUpdateTime=now()"
      		 + " where fUuid = ?";
      		 
       ps = conn.prepareStatement(preSql);
       int i = 1;
       ps.setTimestamp(i++, customerKeywordAccountLog.getEffectiveFromTime());
       ps.setTimestamp(i++, customerKeywordAccountLog.getEffectiveToTime());
       ps.setDouble(i++, customerKeywordAccountLog.getReceivable());
       ps.setDouble(i++, customerKeywordAccountLog.getSecondRealCollection());
       ps.setTimestamp(i++, customerKeywordAccountLog.getSecondReceivedTime());
       ps.setString(i++, customerKeywordAccountLog.getRemarks());
       ps.setString(i++, customerKeywordAccountLog.getStatus());
       ps.setInt(i++, customerKeywordAccountLog.getUuid());
       ps.executeUpdate();
       CustomerKeywordManager manager = new CustomerKeywordManager();
       manager.updateAccountRange(conn, customerKeywordAccountLog.getEffectiveFromTime(), customerKeywordAccountLog.getEffectiveToTime(), customerKeywordAccountLog.getCustomerKeywordUuid(), Constants.BAIDU_TYPE_PC);
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
   
   public CustomerKeywordAccountLogVO addCustomerKeywordAccountLog(Connection conn, CustomerKeywordAccountLogVO customerKeywordAccountLog)
       throws Exception
   {
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       preSql = "insert into t_ck_account_log(fType,fCustomerKeywordUuid,fMonth,fEffectiveFromTime,fEffectiveToTime,fReceivable,"
      		 + "fStatus,fUpdateTime,fCreateTime)"
      		 + " values(?,?,?,?,?,?,?,now(), now())";
       ps = conn.prepareStatement(preSql, Statement.RETURN_GENERATED_KEYS);
       int i = 1;
       ps.setString(i++, customerKeywordAccountLog.getType());
       ps.setInt(i++, customerKeywordAccountLog.getCustomerKeywordUuid());
       ps.setString(i++, customerKeywordAccountLog.getMonth());
       ps.setTimestamp(i++, customerKeywordAccountLog.getEffectiveFromTime());
       ps.setTimestamp(i++, customerKeywordAccountLog.getEffectiveToTime());
       ps.setDouble(i++, customerKeywordAccountLog.getReceivable());
       ps.setString(i++, customerKeywordAccountLog.getStatus());
       ps.executeUpdate();
       ResultSet rs = ps.getGeneratedKeys();
 			 rs.next();
 			 int uuid = rs.getInt(1);
 			 return this.getCustomerKeywordAccountLogByUuid(conn, uuid + "");
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