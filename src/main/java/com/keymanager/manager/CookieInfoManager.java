package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.keymanager.db.DBUtil;
import com.keymanager.util.Utils;
import com.keymanager.value.CookieInfoVO;

public class CookieInfoManager {
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

	public void updateCookieInfoVOs(Connection conn, List<CookieInfoVO> cookieInfoVOs) throws Exception {
		if(!Utils.isEmpty(cookieInfoVOs)){
			String ip = cookieInfoVOs.get(0).getIp();
			deleteCookieInfoVOs(conn, ip);
			Set<String> cookieKeySet = new HashSet<String>();
			for(CookieInfoVO cookieInfoVO : cookieInfoVOs){
				if(!cookieKeySet.contains(cookieInfoVO.toKey())){
					cookieKeySet.add(cookieInfoVO.toKey());
					this.createCookieInfoVO(conn, cookieInfoVO);
				}
			}
		}
	}
	
	public List<CookieInfoVO> searchCookieInfoVOs(Connection conn, String ip) throws Exception {
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		List<CookieInfoVO> cookieInfoVOs = new ArrayList<CookieInfoVO>();
		try {
			sql = " select * from t_cookie_info  where fIp = ? ";

			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setString(1, ip);
			rs = ps.executeQuery();

			while (rs.next()) {
				CookieInfoVO customerKeyword = getCookieInfoVO(rs);
				cookieInfoVOs.add(customerKeyword);
			}
			return cookieInfoVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
	}

	private CookieInfoVO getCookieInfoVO(ResultSet rs) throws SQLException {
		CookieInfoVO cookieInfoVO = new CookieInfoVO();
		cookieInfoVO.setIp(rs.getString("fIp"));
		cookieInfoVO.setDomain(rs.getString("fDomain"));
		cookieInfoVO.setKey(rs.getString("fKey"));
		cookieInfoVO.setValue(rs.getString("fValue"));
		cookieInfoVO.setUpdateTime(rs.getTimestamp("fUpdateTime"));
		cookieInfoVO.setCreateTime(rs.getTimestamp("fCreateTime"));
		return cookieInfoVO;
	}

	public void createCookieInfoVO(Connection conn, CookieInfoVO cookieInfoVO) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {

			preSql = "insert t_cookie_info(fIp, fDomain, fKey, fValue, fUpdateTime, fCreateTime) values(?, ?, ?, ?, now(), now()) ";
			ps = conn.prepareStatement(preSql);

			ps.setString(1, cookieInfoVO.getIp());
			ps.setString(2, cookieInfoVO.getDomain());
			ps.setString(3, cookieInfoVO.getKey());
			ps.setString(4, cookieInfoVO.getValue());

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public void updateCookieInfoVO(Connection conn, CookieInfoVO cookieInfoVO) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "update t_cookie_info set fValue=?,fUpdateTime=now() where fIp = ? and fDomain = ? and fKey = ? ";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, cookieInfoVO.getValue());
			ps.setString(i++, cookieInfoVO.getIp());
			ps.setString(i++, cookieInfoVO.getDomain());
			ps.setString(i++, cookieInfoVO.getKey());
			ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}
	
	public void deleteCookieInfoVOs(Connection conn, String ip) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "delete from t_cookie_info where fIp = ? ";

			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, ip);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}
}