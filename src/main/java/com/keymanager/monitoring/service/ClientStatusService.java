package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class ClientStatusService extends ServiceImpl<ClientStatusDao, ClientStatus>{
	
	@Autowired
	private ClientStatusDao clientStatusDao;

	public void changeTerminalType(String clientID, String terminalType){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus != null){
			clientStatus.setTerminalType(terminalType);
			clientStatusDao.updateById(clientStatus);
		}
	}

	public void addClientStatus(String terminalType, String clientID, String freeSpace, String version, String city){
		ClientStatus clientStatus = new ClientStatus();
		clientStatus.setTerminalType(terminalType);
		clientStatus.setClientID(clientID);
		clientStatus.setFreeSpace(StringUtil.isNumeric(freeSpace) ? Double.parseDouble(freeSpace) : 0);
		clientStatus.setVersion(version);
		clientStatus.setCity(city);
		clientStatus.setClientIDPrefix(Utils.removeDigital(clientID));
		clientStatus.setLastVisitTime(Utils.getCurrentTimestamp());
		Timestamp nextMonth = Utils.addMonth(Utils.getCurrentTimestamp(), 1);
		clientStatus.setRenewalDate(Utils.addDay(nextMonth, -1));
		clientStatus.setCreateTime(Utils.getCurrentTimestamp());
		clientStatusDao.insert(clientStatus);
	}

	public void updatePageNo(String clientID, int pageNo){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus != null){
			clientStatus.setPageNo(pageNo);
			clientStatusDao.updateById(clientStatus);
		}
	}
}
