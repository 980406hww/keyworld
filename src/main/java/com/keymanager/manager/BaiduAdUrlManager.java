package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.keymanager.db.DBUtil;
import com.keymanager.value.BaiduAdUrlVO;

public class BaiduAdUrlManager {
	public static List<BaiduAdUrlVO> baiduAdUrlVOs = null;

	public static String getBaiduAdUrl(Connection conn) throws Exception {
		if(baiduAdUrlVOs == null){
			baiduAdUrlVOs = getBaiduAdUrlVO(conn);
		}
		if(baiduAdUrlVOs != null && baiduAdUrlVOs.size() > 0){
			Random random = new Random();
			BaiduAdUrlVO baiduAdUrlVO = baiduAdUrlVOs.get(random.nextInt(baiduAdUrlVOs.size()));
			return baiduAdUrlVO.getUrl();
		}
		return "";
	}

	public static List<BaiduAdUrlVO> getBaiduAdUrlVO(Connection conn) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			List<BaiduAdUrlVO> baiduAdUrlVOs = new ArrayList<BaiduAdUrlVO>();
			String sql = "select * from t_baidu_ad_url";
			stmt = conn.prepareStatement(sql, 1003, 1007);
			rs = stmt.executeQuery();			
			while (rs.next()) {
				baiduAdUrlVOs.add(getBaiduAdUrlVO(conn, rs));
			}
			return baiduAdUrlVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("getBaiduAdUrlVO");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}
	}

	private static BaiduAdUrlVO getBaiduAdUrlVO(Connection conn, ResultSet rs) throws Exception {
		BaiduAdUrlVO baiduAdUrlVO = new BaiduAdUrlVO();
		baiduAdUrlVO.setUrl(rs.getString("fUrl"));
		baiduAdUrlVO.setCreateTime(rs.getTimestamp("fCreateTime"));
		return baiduAdUrlVO;
	}
}