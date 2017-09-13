package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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

	public void addSummaryClientStatus(String terminalType, String clientID, String freeSpace, String version, String city){
		ClientStatus clientStatus = new ClientStatus();
		clientStatus.setTerminalType(terminalType);
		clientStatus.setClientID(clientID);
		clientStatus.setFreeSpace(StringUtil.isNumeric(freeSpace) ? Double.parseDouble(freeSpace) : 0);
		clientStatus.setVersion(version);
		clientStatus.setCity(city);
		clientStatus.setClientIDPrefix(Utils.removeDigital(clientID));
		clientStatusDao.addSummaryClientStatus(clientStatus);
	}

	public void updatePageNo(String clientID, int pageNo){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus != null){
			clientStatus.setPageNo(pageNo);
			clientStatusDao.updateById(clientStatus);
		}
	}

	public  void updateClientVersion(String clientID, String version){
		clientStatusDao.updateClientVersion(clientID, version);
	}

	public void logClientStatusTime(String terminalType, String clientID, String status, String freeSpace, String version, String
			city, int updateCount){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus == null){
			addSummaryClientStatus(terminalType, clientID, freeSpace, version, city);
		}else{
			clientStatusDao.updateOptimizationResult(clientID, status, version, freeSpace, city, updateCount);
		}
	}

	public List<ClientStatus> getClientStatusList(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
		List<ClientStatus> clientStatuseList = clientStatusDao.getClientStatusList(customerKeywordRefreshStatInfoCriteria);
		return clientStatuseList;
	}
}
