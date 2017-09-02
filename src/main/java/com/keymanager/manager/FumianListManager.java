package com.keymanager.manager;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.db.DBUtil;
import com.keymanager.util.Utils;
import com.keymanager.value.FumianListVO;
import com.keymanager.value.FumianListVOJson;
import com.keymanager.value.FumianListVOListJson;

public class FumianListManager {
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
	
	public boolean updateFumianListVO(String datasourceName, FumianListVO fumianRecordVO) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			return this.updateFumianListVO(conn, fumianRecordVO);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public boolean updateFumianListVO(Connection conn, FumianListVO fumianRecordVO) throws Exception {
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "update t_negative_list set fKeyword = ?, fUrl = ?, fTitle = ?, fDesc = ?, fPosition = ? where fUuid = ? ";
			ps = conn.prepareStatement(preSql);
			int i = 1;
			ps.setString(i++, fumianRecordVO.getKeyword());
			ps.setString(i++, fumianRecordVO.getUrl());
			ps.setString(i++, fumianRecordVO.getTitle());
			ps.setString(i++, fumianRecordVO.getDesc());
			ps.setInt(i++, fumianRecordVO.getPosition());
			ps.setInt(i++, fumianRecordVO.getUuid());
			ps.executeUpdate();
			int updatedCount = ps.getUpdateCount();
			return updatedCount == 1;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	public FumianListVO getFumianListByUuid(String datasourceName, String uuid) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			return this.getFumianListByUuid(conn, uuid);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public FumianListVO getFumianListByUuid(Connection conn, String uuid) throws Exception {
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = "";
		FumianListVO fumianRecordVO = null;
		try {
			sql = "select * from t_negative_list where fUuid = ? ";

			stmt = conn.prepareStatement(sql, 1003, 1007);
			stmt.setString(1, uuid);
			rs = stmt.executeQuery();
			if (rs.next()) {
				fumianRecordVO = getFumianListVO(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("login_error");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(stmt);
		}
		return fumianRecordVO;
	}

	private FumianListVO getFumianListVO(ResultSet rs) throws SQLException {
		FumianListVO fumianRecordVO = new FumianListVO();
		fumianRecordVO.setUuid(rs.getInt("fUuid"));
		fumianRecordVO.setKeyword(rs.getString("fKeyword"));
		fumianRecordVO.setUrl(rs.getString("fUrl"));
		fumianRecordVO.setTitle(rs.getString("fTitle"));
		fumianRecordVO.setDesc(rs.getString("fDesc"));
		fumianRecordVO.setPosition(rs.getInt("fPosition"));
		fumianRecordVO.setCreateTime(rs.getTimestamp("fCreateTime"));
		return fumianRecordVO;
	}

	public ArrayList searchFumianListVO(String dsName, int pageSize, int curPage, String condition, String order, int recCount) throws Exception {
		if (pageSize < 0) {
			pageSize = 20;
		}

		if (curPage < 0) {
			curPage = 1;
		}

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String sql = "";

		ArrayList fumianRecordVOs = new ArrayList();
		try {
			conn = DBUtil.getConnection(dsName);

			if (recCount != 0) {
				sql = " select count(1) as recordCount from t_negative_list where 1=1 " + condition;

				ps = conn.prepareStatement(sql, 1003, 1007);
				rs = ps.executeQuery();
				if (rs.next()) {
					recordCount = rs.getInt("recordCount");
				}

				pageCount = (int) Math.ceil((recordCount + pageSize - 1) / pageSize);
				if (curPage > pageCount) {
					curPage = 1;
				}
				currPage = curPage;
			}

			String sqlFields = " *  ";

			sql = " select " + sqlFields + " from t_negative_list where 1=1 " + condition + " " + order;
			sql = sql + " limit " + (curPage - 1) * pageSize + "," + pageSize;

			ps = conn.prepareStatement(sql, 1003, 1007);
			rs = ps.executeQuery();
			while (rs.next()) {
				FumianListVO value = getFumianListVO(rs);
				fumianRecordVOs.add(value);
			}
			return fumianRecordVOs;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("searchFumianListVO");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}

	public void addFumainRecordVO(String datasourceName, FumianListVO fumianListVO) throws Exception {
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			conn.setAutoCommit(false);
			addFumainRecordVO(conn, fumianListVO);
			CustomerKeywordManager customerKeywordManager = new CustomerKeywordManager();
			customerKeywordManager.deleteCustomerKeywordWhenAddFumianList(conn, fumianListVO.getTerminalType(), fumianListVO.getKeyword(),
					fumianListVO.getTitle());
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	public void addFumainRecordVO(Connection conn, FumianListVO fumianListVO) throws Exception {
		PreparedStatement ps = null;
		String preSql = null;
		try {
			preSql = "insert into t_negative_list(fKeyword, fTerminalType, fUrl, fTitle, fDesc, fPosition, fCreateTime) values(?, ?, ?, ?, ?, ?, now())";
			ps = conn.prepareStatement(preSql);
			int i = 1;

			ps.setString(i++, fumianListVO.getKeyword());
			ps.setString(i++, fumianListVO.getTerminalType());
			ps.setString(i++, fumianListVO.getUrl());
			ps.setString(i++, fumianListVO.getTitle());
			ps.setString(i++, fumianListVO.getDesc());
			ps.setInt(i++, fumianListVO.getPosition());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}
	
	public void deleteFumianListVO(String datasourceName, String uuid) throws Exception {
		Connection conn = null;
		PreparedStatement ps = null;

		String preSql = null;
		try {
			conn = DBUtil.getConnection(datasourceName);
			preSql = " delete from t_negative_list where fUuid = ? ";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, uuid);
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
			DBUtil.closeConnection(conn);
		}
	}
	
	public void saveFumianLists(String datasourceName, String terminalType, String json) throws Exception{
		if(!Utils.isNullOrEmpty(json)){
			ObjectMapper mapper = new ObjectMapper();
			Connection conn = null;
			try {
				conn = DBUtil.getConnection(datasourceName);
				FumianListVOListJson fumianListVOListJson = (FumianListVOListJson)mapper.readValue(json, FumianListVOListJson.class);
				if(fumianListVOListJson != null){
					conn.setAutoCommit(false);
					CustomerKeywordManager customerKeywordManager = new CustomerKeywordManager();

					for(FumianListVOJson fumianListVOJson : fumianListVOListJson.getFumianListVOJsons()){
						if(!checkFumianListExist(conn, terminalType, fumianListVOJson.getKeyword(), fumianListVOJson.getTitle(), fumianListVOJson
								.getUrl(), fumianListVOJson.getDesc())){
							FumianListVO fumianListVO = new FumianListVO();
							fumianListVO.setPosition(fumianListVOJson.getPosition());
							fumianListVO.setUrl(fumianListVOJson.getUrl());
							fumianListVO.setTitle(fumianListVOJson.getTitle());
							fumianListVO.setTerminalType(terminalType);
							fumianListVO.setKeyword(fumianListVOJson.getKeyword());
							fumianListVO.setDesc(fumianListVOJson.getDesc());
							customerKeywordManager.deleteCustomerKeywordWhenAddFumianList(conn, terminalType, fumianListVOJson.getKeyword(),
									fumianListVOJson.getTitle());
							addFumainRecordVO(conn, fumianListVO);
						}
					}
					conn.commit();
				}
			} catch (IOException e){
				e.printStackTrace();
				throw new Exception("saveCustomerKeywords error", e);
			} finally {
				DBUtil.closeConnection(conn);
			}
		}
	}
	
	public String getFumianLists(String datasourceName, String terminalType, String keyword) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		try {
			String condition = String.format(" and fKeyword = '%s' and fTerminalType = '%s' ", keyword, terminalType);
			List fumianList = this.searchFumianListVO(datasourceName, 1000, 1, condition, "", 0);
			if(fumianList != null){
				FumianListVOListJson fumianListVOListJson = new FumianListVOListJson();
				List<FumianListVOJson> fumianListVOJsons = new ArrayList<FumianListVOJson>();
				for(Object obj : fumianList){
					FumianListVO fumianListVO = (FumianListVO)obj;
					FumianListVOJson fumianListVOJson = new FumianListVOJson();
					fumianListVOJson.setUuid(fumianListVO.getUuid());
					fumianListVOJson.setUrl(fumianListVO.getUrl());
					fumianListVOJson.setTitle(fumianListVO.getTitle());
					fumianListVOJson.setPosition(fumianListVO.getPosition());
					fumianListVOJson.setKeyword(fumianListVO.getKeyword());
					fumianListVOJson.setDesc(fumianListVO.getDesc());
					fumianListVOJsons.add(fumianListVOJson);
				}
				fumianListVOListJson.setFumianListVOJsons(fumianListVOJsons);
				return mapper.writeValueAsString(fumianListVOListJson);
			}
			return "";
		} catch (IOException e){
			e.printStackTrace();
			throw new Exception("getFumianLists error", e);
		}
	}

	public boolean checkFumianListExist(Connection conn, String terminalType, String keyword, String title, String url, String desc) throws Exception{
		ObjectMapper mapper = new ObjectMapper();
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			String sql = " select * from t_negative_list where fKeyword = ? and fTitle = ? and fUrl = ? and fDesc = ? and fTerminalType = ? ";
			ps = conn.prepareStatement(sql, 1003, 1007);
			ps.setString(1, keyword);
			ps.setString(2, title);
			ps.setString(3, url);
			ps.setString(4, desc);
			ps.setString(5, terminalType);
			rs = ps.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("checkFumianListExist");
		} finally {
			DBUtil.closeResultSet(rs);
			DBUtil.closePreparedStatement(ps);
		}
		return false;
	}
}