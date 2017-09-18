package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClientStatusRestartLogDao;
import com.keymanager.monitoring.entity.ClientStatusRestartLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientStatusRestartLogService extends ServiceImpl<ClientStatusRestartLogDao, ClientStatusRestartLog>{

	private static Logger logger = LoggerFactory.getLogger(ClientStatusRestartLogService.class);
	
	@Autowired
	private ClientStatusRestartLogDao clientStatusRestartLogDao;


}
