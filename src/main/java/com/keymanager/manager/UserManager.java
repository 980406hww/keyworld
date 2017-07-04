 package com.keymanager.manager;
 
   import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.keymanager.db.DBUtil;
import com.keymanager.util.Utils;
import com.keymanager.value.UserAccountLogVO;
import com.keymanager.value.UserVO;
 
 public class UserManager
 {
   private int recordCount;
   private int currPage = 0;
   private int pageCount = 0;
 
   public int getRecordCount()
   {
     return recordCount;
   }
 
   public int getCurrPage()
   {
     return currPage;
   }
 
   public int getPageCount()
   {
     return pageCount;
   }
   
   
   public void uploadData(String data) throws Exception{
	   
	   if(!Utils.isNullOrEmpty(data)){
		   System.out.println(data);
		   Connection conn = null;
		   PreparedStatement stmt = null;
		   ResultSet rs = null;
		   String sql = "";
		   
		   try
		   {
			   conn = DBUtil.getConnection("weibo");
			   String[] rowArrays = data.trim().split("--row--");
			   for(String row : rowArrays){
				   String[] colArrays = row.trim().split("--col--");
				   if(colArrays.length < 3){
					   continue;
				   }
				   String group = colArrays[0];
				   String name = colArrays[1];
				   String guanWangUrl = colArrays[2];
				   
				   sql = "SELECT * FROM t_fumian_name WHERE fGroup = ? AND fName = ? LIMIT 1 ";
				   
				   stmt = conn.prepareStatement(sql, 1003, 
						   1007);
				   stmt.setString(1, group);
				   stmt.setString(2, name);
				   
				   rs = stmt.executeQuery();
				   
				   if (!rs.next())
				   {
					   sql = " INSERT INTO t_fumian_name(fGroup, fName, fGuanwangUrl, fUpdateTime, fCreateTime) VALUES(?, ?, ? ,now(), now())";
					   stmt = conn.prepareStatement(sql);
					   
					   stmt.setString(1, group);
					   stmt.setString(2, name);
					   stmt.setString(3, guanWangUrl);
					   stmt.executeUpdate();
				   }
			   }
		   }
		   catch (Exception e)
		   {
			   e.printStackTrace();
			   throw new Exception("uploadData");
		   }
		   finally
		   {
			   DBUtil.closeResultSet(rs);
			   DBUtil.closePreparedStatement(stmt);
			   DBUtil.closeConnection(conn);
		   }
	   }
   }
   
   public UserVO login(String datasourceName, String userID, String password)
       throws Exception
     {
       Connection conn = null;
       PreparedStatement stmt = null;
       ResultSet rs = null;
       String sql = "";
       UserVO value = null;
   
       try
       {
         conn = DBUtil.getConnection(datasourceName);
   
         sql = "select * from t_user where fstatus=1 and fUserID=? and fPassword=? ";
   
         stmt = conn.prepareStatement(sql, 1003, 
           1007);
         stmt.setString(1, userID);
         stmt.setString(2, password);
   
         rs = stmt.executeQuery();
   
         if (rs.next())
         {
           value = getUserVO(rs);
   
           sql = " update t_user set fUpdateTime=now() where fUserID=? and fPassword= ? ";
           stmt = conn.prepareStatement(sql);
             
           stmt.setString(1, userID);
           stmt.setString(2, password);
           stmt.executeUpdate();
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
   
       return value;
     }
 
   public UserVO login(String datasourceName, String userID, String password, String clientIP)
     throws Exception
   {
     Connection conn = null;
     PreparedStatement stmt = null;
     ResultSet rs = null;
     String sql = "";
     UserVO value = null;
 
     try
     {
       conn = DBUtil.getConnection(datasourceName);
 
       sql = "select * from t_user where fstatus=1 and fUserID=? and fPassword=? ";
 
       stmt = conn.prepareStatement(sql, 1003, 
         1007);
       stmt.setString(1, userID);
       stmt.setString(2, password);
 
       rs = stmt.executeQuery();
 
       if (rs.next())
       {
         value = getUserVO(rs);
 
         sql = " update t_user set fUpdateTime=now(), fClientIp=? where fUserID=? and fPassword= ? ";
         stmt = conn.prepareStatement(sql);
 
         stmt.setString(1, clientIP);
         stmt.setString(2, userID);
         stmt.setString(3, password);
         stmt.executeUpdate();
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
 
     return value;
   }
 
   
   public List getAllUserNames(String datasourceName)
       throws Exception
     {
       Connection conn = null;
       PreparedStatement stmt = null;
       ResultSet rs = null;
       String sql = "";
   
       List userNames = new ArrayList();
       try
       {
         conn = DBUtil.getConnection(datasourceName);
   
         sql = "select distinct fUserName from t_user";
   
         stmt = conn.prepareStatement(sql, 1003, 
           1007);   
         rs = stmt.executeQuery();
         
         while (rs.next())
         {
           userNames.add(rs.getString("fUserName"));
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
   
       return userNames;
     }
   
   public void updateUserStatus(String dsName, String userID, int status) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(dsName);
 
       preSql = "update t_user set fStatus=? where fUserID = ? ";
       ps = conn.prepareStatement(preSql);
 
       ps.setInt(1, status);
       ps.setString(2, userID);
 
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
 
   public boolean updatePassword(String dsName, String userID, String newPassword, String oldPassword) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(dsName);
 
       preSql = "update t_user set fPassword=? where fUserID = ? and fPassword=? ";
       ps = conn.prepareStatement(preSql);
 
       ps.setString(1, newPassword);
       ps.setString(2, userID);
       ps.setString(3, oldPassword);
 
       ps.executeUpdate();
       
       int updatedCount = ps.getUpdateCount();
       return updatedCount == 1;
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
   
   public boolean recharge(String datasourceName, UserVO user, String amount, String remarks) throws Exception
   {
     Connection conn = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
       conn.setAutoCommit(false);
       UserAccountLogVO userAccountLogVO = new UserAccountLogVO();
       userAccountLogVO.setAmount(Double.parseDouble(amount));
       userAccountLogVO.setType("充值");
       userAccountLogVO.setUserID(user.getUserID());
       userAccountLogVO.setRemarks(remarks);
       userAccountLogVO.setAccountAmount(user.getAccountAmount() + userAccountLogVO.getAmount());
       user.setAccountAmount(userAccountLogVO.getAccountAmount());
       UserAccountLogManager userAccountLogManager = new UserAccountLogManager();
       userAccountLogManager.createUserAccountLogVO(userAccountLogVO, conn);
       boolean updated = this.updateUser(conn, user);
       conn.commit();
       return updated;
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception(e);
     }
     finally
     {
       DBUtil.closeConnection(conn);
     }
   }
   
   public boolean refund(String datasourceName, UserVO user, String amount, String remarks) throws Exception
   {
     Connection conn = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
       conn.setAutoCommit(false);
       UserAccountLogVO userAccountLogVO = new UserAccountLogVO();
       userAccountLogVO.setAmount(Double.parseDouble(amount));
       userAccountLogVO.setType("退款");
       userAccountLogVO.setUserID(user.getUserID());
       userAccountLogVO.setRemarks(remarks);
       userAccountLogVO.setAccountAmount(user.getAccountAmount() - userAccountLogVO.getAmount());
       user.setAccountAmount(userAccountLogVO.getAccountAmount());
       UserAccountLogManager userAccountLogManager = new UserAccountLogManager();
       userAccountLogManager.createUserAccountLogVO(userAccountLogVO, conn);
       boolean updated = this.updateUser(conn, user);
       conn.commit();
       return updated;
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception(e);
     }
     finally
     {
       DBUtil.closeConnection(conn);
     }
   }
   
   public boolean updateUser(String datasourceName, UserVO user) throws Exception
   {
     Connection conn = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
       return this.updateUser(conn, user);
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception(e);
     }
     finally
     {
       DBUtil.closeConnection(conn);
     }
   }
   
   public boolean updateUser(Connection conn, UserVO user) throws Exception
   {
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       preSql = "update t_user set fUserName=?,fGender=?,fQQ=?,fPhone=?,fPercentage=?,fUserLevel=?,fStatus=?, fAccountAmount=?, fAutoPay=?, fCollectMethod=? where fUserID = ? ";
       ps = conn.prepareStatement(preSql);
       int i=1;
       ps.setString(i++, user.getUserName());
       ps.setString(i++, user.getGender());
       ps.setString(i++, user.getQq());
       ps.setString(i++, user.getPhone());
       ps.setInt(i++, user.getPercentage());
       ps.setInt(i++, user.getUserLevel());
       ps.setInt(i++, user.getStatus());
       ps.setDouble(i++, user.getAccountAmount());
       ps.setBoolean(i++, user.isAutoPay());
       ps.setString(i++, user.getCollectMethod());
       ps.setString(i++, user.getUserID());
 
       ps.executeUpdate();
       int updatedCount = ps.getUpdateCount();
       return updatedCount == 1;
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
   
   public boolean updatePasswordWithSerial(String dsName, String userID, String newPassword, String serialNumber) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(dsName);
 
       preSql = "update t_user set fPassword=? where fUserID = ? and fPasswordSerial=? ";
       ps = conn.prepareStatement(preSql);
 
       ps.setString(1, newPassword);
       ps.setString(2, userID);
       ps.setString(3, serialNumber);
 
       ps.executeUpdate();
       
       int updatedCount = ps.getUpdateCount();
       return updatedCount == 1;
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
 
   public String updatePasswordSerial(String dsName, String userID) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(dsName);
 
       preSql = "update t_user set fPasswordSerial=? where fUserID = ? ";
       ps = conn.prepareStatement(preSql);
 
       long passwordSerial = System.currentTimeMillis();
 
       ps.setString(1, String.valueOf(passwordSerial));
       ps.setString(2, userID);
 
       ps.executeUpdate();
 
       String str1 = String.valueOf(passwordSerial);
       return str1;
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
   
   public boolean existPasswordSerial(String dsName, String userID, String passwordSerial)
       throws Exception
     {
       Connection conn = null;
       PreparedStatement stmt = null;
       ResultSet rs = null;
       String sql = "";
       try
       {
         conn = DBUtil.getConnection(dsName);
   
         sql = "select fUserID from t_user where  fUserID=? and fPasswordSerial=? ";
   
         stmt = conn.prepareStatement(sql, 1003, 
           1007);
         stmt.setString(1, userID);
         stmt.setString(2, passwordSerial);
   
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
   
   public void updateUserPercentage(String datasourceName, String userID, int percentage) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
 
       preSql = "update t_user set fPercentage=? where fUserID = ? ";
       ps = conn.prepareStatement(preSql);
 
       ps.setInt(1, percentage);
       ps.setString(2, userID);
 
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
 
	public UserVO getUserByUserID(String datasourceName, String userID) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			return this.getUserByUserID(conn, userID);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public UserVO getUserByUserID(Connection conn, String userID) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		UserVO value = null;
		try {
			sql = "select * from t_user where fUserID=? ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, userID);
			rs = stmt.executeQuery();
			if (rs.next()) {
				value = getUserVO(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}
		return value;
	}
	
	private UserVO getUserVO(ResultSet rs) throws SQLException {
		UserVO user = new UserVO();
		user.setUserID(rs.getString("fUserID"));
		user.setUserName(rs.getString("fUserName"));
		user.setPassword(rs.getString("fPassword"));
		user.setGender(rs.getString("fGender"));
		user.setQq(rs.getString("fQQ"));
		user.setPhone(rs.getString("fPhone"));
		user.setPercentage(rs.getInt("fPercentage"));
		user.setStatus(rs.getInt("fStatus"));
		user.setUserLevel(rs.getInt("fUserLevel"));
		user.setVipType(rs.getBoolean("fVipType"));
		user.setClientIp(rs.getString("fClientIp"));
//		user.setAccountAmount(rs.getDouble("fAccountAmount"));
//		user.setAutoPay(rs.getBoolean("fAutoPay"));
//		user.setCollectMethod(rs.getString("fCollectMethod"));
		user.setUpdateTime(rs.getTimestamp("fUpdateTime"));
		user.setCreateTime(rs.getTimestamp("fCreateTime"));
		return user;
	}
 
   public ArrayList searchUser(String dsName, int pageSize, int curPage, String condition, String order, int recCount) throws Exception
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
 
     ArrayList users = new ArrayList();
     try
     {
       conn = DBUtil.getConnection(dsName);
 
       if (recCount != 0)
       {
         sql = " select count(1) as recordCount from t_user  where 1=1 " + condition;
 
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
 
       sql = " select " + sqlFields + " from t_user where 1=1 " + condition + " " + order;
       sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;
 
       ps = conn.prepareStatement(sql, 1003, 1007);
       rs = ps.executeQuery();
       while (rs.next())
       {
         UserVO value = getUserVO(rs); 
         users.add(value);
       }
 
       return users;
     }
     catch (Exception e)
     {
       e.printStackTrace();
       throw new Exception("searchUser");
     }
     finally
     {
       DBUtil.closeResultSet(rs);
       DBUtil.closePreparedStatement(ps);
       DBUtil.closeConnection(conn);
     }
   }
 
   public void addUser(UserVO user, String datasourceName) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
 
       preSql = "insert into t_user(fUserID,fPassword,fUserName,fGender,fQQ,fPhone,fPercentage,fStatus,fUserLevel,fVipType,fClientIp,fAccountAmount,fAutoPay,fCollectMethod,fUpdateTime,fCreateTime) values(?,?,?,?,?,?,?,?,?,?,?,?,?,now(),now())";
       ps = conn.prepareStatement(preSql);
       int i = 1;
 
       ps.setString(i++, user.getUserID());
       ps.setString(i++, user.getPassword());
       ps.setString(i++, user.getUserName());
       ps.setString(i++, user.getGender());
       ps.setString(i++, user.getQq());
       ps.setString(i++, user.getPhone());
       ps.setInt(i++, user.getPercentage());
       ps.setInt(i++, user.getStatus());
       ps.setInt(i++, user.getUserLevel());
       ps.setBoolean(i++, user.isVipType());
       ps.setString(i++, user.getClientIp());
       ps.setDouble(i++, user.getAccountAmount());
       ps.setBoolean(i++, user.isAutoPay());
       ps.setString(i++, user.getCollectMethod());
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
 
   public void delUser(String datasourceName, String userName) throws Exception
   {
     Connection conn = null;
     PreparedStatement ps = null;
 
     String preSql = null;
     try
     {
       conn = DBUtil.getConnection(datasourceName);
 
       preSql = " delete from t_user where fUserID=?";
       ps = conn.prepareStatement(preSql);
       ps.setString(1, userName);
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
 }