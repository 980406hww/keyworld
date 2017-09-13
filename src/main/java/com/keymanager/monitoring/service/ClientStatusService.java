package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

	public List<ClientStatus> getClientStatusList(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
		List<ClientStatus> clientStatuseList = clientStatusDao.getClientStatusList(customerKeywordRefreshStatInfoCriteria);
		return clientStatuseList;
	}
}
