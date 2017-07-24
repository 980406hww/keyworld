package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.keymanager.db.DBUtil;
import com.keymanager.value.UserSessionVO;

public class UserSessionManager {
	private int recordCount;
	private int currPage = 0;
	private int pageCount = 0;

	public int getRecordCount() {
		return recordCount;
	}

	public int getCurrPage() {
		return currPage;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int queryUserSessionIndex(String datasourceName, String computerID,
			String ip) throws Exception {
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		UserSessionVO value = null;

		try {
			conn = DBUtil.getConnection(datasourceName);

			sql = "select * from t_user_session where fComputerID=? and fIp=? order by fCreateTime desc";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, computerID);
			stmt.setString(2, ip);

			rs = stmt.executeQuery();

			if (rs.next()) {
				value = getUserSessionVO(rs);
				value.setCount(value.getCount() + 1);
				updateUserSession(value, conn);
				return value.getIndex();
			}

			sql = "select * from t_user_session where fComputerID=? order by fCreateTime desc";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, computerID);
			rs = stmt.executeQuery();
			int index = 1;
			if (rs.next()) {
				value = getUserSessionVO(rs);
				index = value.getIndex() + 1;
			}
			UserSessionVO userSession = new UserSessionVO();
			userSession.setComputerID(computerID);
			userSession.setIP(ip);
			userSession.setIndex(index);
			userSession.setCount(1);
			this.addUserSession(userSession, conn);
			return index;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
			DBUtil.closeConnection(conn);
		}
	}

	private UserSessionVO getUserSessionVO(ResultSet rs) throws SQLException {
		UserSessionVO userSession = new UserSessionVO();
		userSession.setID(rs.getInt("fID"));
		userSession.setComputerID(rs.getString("fComputerID"));
		userSession.setIP(rs.getString("fIp"));
		userSession.setIndex(rs.getInt("fIndex"));
		userSession.setCount(rs.getInt("fCount"));
		userSession.setUpdateTime(rs.getTimestamp("fUpdateTime"));
		userSession.setCreateTime(rs.getTimestamp("fCreateTime"));
		return userSession;
	}

	public void addUserSession(UserSessionVO userSession, Connection conn)
			throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "insert into t_user_session(fComputerID,fIP,fIndex,fCount,fUpdateTime,fCreateTime) values(?,?,?,?,now(),now())";
			ps = conn.prepareStatement(preSql);
			int i = 1;

			ps.setString(i++, userSession.getComputerID());
			ps.setString(i++, userSession.getIP());
			ps.setInt(i++, userSession.getIndex());
			ps.setInt(i++, userSession.getCount());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateUserSession(UserSessionVO userSession, Connection conn)
			throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "update t_user_session set fComputerID=?,fIP=?,fIndex=?,fCount=?,fUpdateTime=now() where fID=?";
			ps = conn.prepareStatement(preSql);
			int i = 1;

			ps.setString(i++, userSession.getComputerID());
			ps.setString(i++, userSession.getIP());
			ps.setInt(i++, userSession.getIndex());
			ps.setInt(i++, userSession.getCount());
			ps.setInt(i++, userSession.getID());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

}