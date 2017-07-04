package com.keymanager.manager;

import com.keymanager.db.DBUtil;
import com.keymanager.mail.MailHelper;
import com.keymanager.util.Utils;
import com.keymanager.util.VNCAddressBookParser;
import com.keymanager.util.ZipCompressor;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.*;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientStatusRestartLogManager {
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

	public void addClientStatusResultLogVO(Connection conn, ClientStatusRestartLogVO clientStatusRestartLogVO) throws Exception{
		PreparedStatement ps = null;

		String preSql = null;
		try {
			preSql = "insert into t_client_status_restart_log(fClientID, fRestartCount, fRestartStatus, fGroup, fRestartTime) values(?,?,?,?, now())";
			ps = conn.prepareStatement(preSql);
			ps.setString(1, clientStatusRestartLogVO.getClientID());
			ps.setInt(2, clientStatusRestartLogVO.getRestartCount());
			ps.setString(3, clientStatusRestartLogVO.getRestartStatus());
			ps.setString(4, clientStatusRestartLogVO.getGroup());
			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closePreparedStatement(ps);
		}
	}

	private ClientStatusRestartLogVO getClientStatusRestartLogVO(Connection conn, ResultSet rs) throws Exception {
		ClientStatusRestartLogVO clientStatusRestartLogVO = new ClientStatusRestartLogVO();
		clientStatusRestartLogVO.setUuid(rs.getInt("fUuid"));
		clientStatusRestartLogVO.setClientID(rs.getString("fClientID"));
		clientStatusRestartLogVO.setGroup(rs.getString("fGroup"));
		clientStatusRestartLogVO.setRestartTime(rs.getTimestamp("fRestartTime"));
		clientStatusRestartLogVO.setRestartCount(rs.getInt("fRestartCount"));
		clientStatusRestartLogVO.setRestartStatus(rs.getString("fRestartStatus"));
		return clientStatusRestartLogVO;
	}

}