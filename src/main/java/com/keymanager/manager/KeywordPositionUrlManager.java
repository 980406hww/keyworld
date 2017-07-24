package com.keymanager.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.keymanager.db.DBUtil;
import com.keymanager.util.Constants;
import com.keymanager.util.NewsKeywordPositionCalculator;
import com.keymanager.util.Utils;
import com.keymanager.value.KeywordPositionUrlVO;
import com.keymanager.value.KeywordVO;
import com.keymanager.value.WebsiteSummaryVO;

public class KeywordPositionUrlManager {

	public List<KeywordPositionUrlVO> getKeywordPositionUrlVOs(String datasourceName, String keyword, String searchEngine)
			throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		List<KeywordPositionUrlVO> keywordPositionUrlVOs = new ArrayList<KeywordPositionUrlVO>();
		try {
			conn = DBUtil.getConnection(datasourceName);
			sql = " select * FROM t_keyword_position_url where fKeyword = ? and fSearchEngine = ? order by fPosition";

			ps = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			ps.setString(i++, keyword);
			ps.setString(i++, searchEngine);
			rs = ps.executeQuery();

			while (rs.next()) {
				KeywordPositionUrlVO serviceProviderVO = getKeywordPositionUrlVO(datasourceName, rs);
				keywordPositionUrlVOs.add(serviceProviderVO);
			}
			return keywordPositionUrlVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("search_keyword_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public String getKeywordPositionUrlVOString(String datasourceName, String searchEngine, String relatedKeyword) throws Exception{	
		List<KeywordPositionUrlVO> newsKeywordPositionUrlVOs = getRelatedNewsKeywordPositionUrlVOs(datasourceName, relatedKeyword, searchEngine);
		if(!Utils.isEmpty(newsKeywordPositionUrlVOs)){
			return newsKeywordPositionUrlVOs.get(0).getKeyword();
//			List<KeywordPositionUrlVO> keywordPositionUrlVOs = retrieveNewsNonBaiduWebsite(newsKeywordPositionUrlVOs, newsKeywordPositionUrlVOs.size() > 20 ? 20 : newsKeywordPositionUrlVOs.size());
//			StringBuilder sbNewsKeywordUrlAndPositions = new StringBuilder();
//			String headerPart = null;
//			for(KeywordPositionUrlVO keywordPositionUrlVO : keywordPositionUrlVOs){
//				if (sbNewsKeywordUrlAndPositions.length() > 0){
//					sbNewsKeywordUrlAndPositions.append(Constants.SUB_ROW_SPLITTOR);
//				}else{
//					headerPart = keywordPositionUrlVO.toNewsString();
//				}
//				
//				sbNewsKeywordUrlAndPositions.append(keywordPositionUrlVO.toUrlAndPositionString());
//			}
//			return String.format("%s__header__%s", headerPart, sbNewsKeywordUrlAndPositions.toString());
		}
		return null;
	}
	
	public List<KeywordPositionUrlVO> retrieveNewsNonBaiduWebsite(List<KeywordPositionUrlVO> keywordPositionUrlVOs, int position) {
		if (!Utils.isEmpty(keywordPositionUrlVOs)) {
			NewsKeywordPositionCalculator calculator = new NewsKeywordPositionCalculator();
			return calculator.calculate(keywordPositionUrlVOs, position);
		}
		return null;
	}
	
	public List<KeywordPositionUrlVO> getRelatedNewsKeywordPositionUrlVOs(String datasourceName, String relatedKeyword,
			String searchEngine) throws Exception {
		String currentKeyword = relatedKeyword;
		if (Utils.isNullOrEmpty(relatedKeyword)) {
			KeywordManager km = new KeywordManager();
			KeywordVO keywordVO = km.getRandomPublicKeyword(datasourceName);
			if (keywordVO != null) {
				currentKeyword = keywordVO.getKeyword();
			}
		}
		return getKeywordPositionUrlVOs(datasourceName, currentKeyword, searchEngine);
	}

	private KeywordPositionUrlVO getKeywordPositionUrlVO(String datasourceName, ResultSet rs) throws SQLException {
		KeywordPositionUrlVO keywordPositionUrlVO = new KeywordPositionUrlVO();
		keywordPositionUrlVO.setUuid(rs.getInt("fUuid"));
		keywordPositionUrlVO.setKeyword(rs.getString("fKeyword"));
		keywordPositionUrlVO.setSearchEngine(rs.getString("fSearchEngine"));
		keywordPositionUrlVO.setUrl(rs.getString("fUrl"));
		keywordPositionUrlVO.setPosition(rs.getInt("fPosition"));
		keywordPositionUrlVO.setSnapshotDateTime(rs.getString("fSnapshotDateTime"));
		keywordPositionUrlVO.setUpdateTime(rs.getTimestamp("fUpdateTime"));
		return keywordPositionUrlVO;
	}

	public void addKeywordPositionUrlVO(Connection conn, KeywordPositionUrlVO keywordPositionUrlVO) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "insert into t_keyword_position_url(fKeyword, fSearchEngine, fUrl, fPosition, fSnapshotDateTime, fUpdateTime)"
					+ " values(?, ?, ?, ?, ?,?)";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setString(i++, keywordPositionUrlVO.getKeyword());
			stmt.setString(i++, keywordPositionUrlVO.getSearchEngine());
			stmt.setString(i++, keywordPositionUrlVO.getUrl());
			stmt.setInt(i++, keywordPositionUrlVO.getPosition());
			stmt.setString(i++, keywordPositionUrlVO.getSnapshotDateTime());
			stmt.setTimestamp(i++, keywordPositionUrlVO.getUpdateTime());
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		}
	}

	public void addKeywordPositionUrlVOs(Connection conn, String keyword, String searchEngine, List<WebsiteSummaryVO> websiteSummaryVOs)
			throws Exception {
		int i = 1;
		Timestamp currentTimestamp = Utils.getCurrentTimestamp();
		for (WebsiteSummaryVO websiteSummaryVO : websiteSummaryVOs) {
			this.addKeywordPositionUrlVO(conn, new KeywordPositionUrlVO(keyword, searchEngine, websiteSummaryVO.getUrl(), i++, websiteSummaryVO.getSnapshotDateTime(), currentTimestamp));
		}
	}

	public void deleteKeywordPositionUrlVOs(Connection conn, String keyword, String searchEngine) throws Exception {
		PreparedStatement stmt = null;
		String sql = "";
		try {
			sql = "delete from t_keyword_position_url where fKeyword = ? and fSearchEngine = ?";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			int i = 1;
			stmt.setString(i++, keyword);
			stmt.setString(i++, searchEngine);
			stmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closePreparedStatement(stmt);
		}
	}

	public void refreshKeywordPositionUrlVOs(Connection conn, String keyword, String searchEngine, List<WebsiteSummaryVO> websiteSummaryVOs)
			throws Exception {
		deleteKeywordPositionUrlVOs(conn, keyword, searchEngine);
		addKeywordPositionUrlVOs(conn, keyword, searchEngine, websiteSummaryVOs);
	}
}
