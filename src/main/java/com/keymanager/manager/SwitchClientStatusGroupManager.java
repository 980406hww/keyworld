package com.keymanager.manager;

import com.keymanager.db.DBUtil;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.value.ClientStatusVO;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SwitchClientStatusGroupManager {
	public void switchGroup() throws Exception{
		Connection conn = null;
		try{
			conn = DBUtil.getConnection("keyword");
			switchGroup(conn, TerminalTypeEnum.PC.name());
			switchGroup(conn, TerminalTypeEnum.Phone.name());
		}catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		} finally {
			DBUtil.closeConnection(conn);
		}
	}

	private void switchGroup(Connection conn, String terminalType) throws Exception{
		ClientStatusManager csManager = new ClientStatusManager();
		List<ClientStatusVO> clientStatusVOList = csManager.searchClientStatusVOs(conn, " and fTerminalType = '" + terminalType + "' and fValid = 1" +
				" and fAllowSwitchGroup = 1 ");
		List<ClientStatusVO> cloneClientStatusVOList = new ArrayList<ClientStatusVO>(clientStatusVOList);
		Collections.shuffle(clientStatusVOList);
		Collections.shuffle(clientStatusVOList);
		Collections.shuffle(cloneClientStatusVOList);
		Collections.shuffle(cloneClientStatusVOList);
		Collections.shuffle(cloneClientStatusVOList);
		for(int i = 0; i < clientStatusVOList.size(); i++){
			ClientStatusVO sourceClientStatusVO = clientStatusVOList.get(i);
			ClientStatusVO targetClientStatusVO = cloneClientStatusVOList.get(i);
			csManager.switchClientStatusInfo(sourceClientStatusVO, targetClientStatusVO);
		}

		for(ClientStatusVO clientStatusVO : clientStatusVOList){
			csManager.updateCleintStatusSetting(conn, clientStatusVO);
		}
	}
}