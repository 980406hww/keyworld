package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.ClientUpgradeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClientUpgradeSchedule {
	private static Logger logger = LoggerFactory.getLogger(ClientUpgradeSchedule.class);

	@Autowired
	private ClientUpgradeService clientUpgradeService;

	public void runTask(){
		logger.info("============= "+" Client Upgrade Task "+"===================");
		try {
			clientUpgradeService.clientAutoUpgrade();
		} catch (Exception e) {
			logger.error("Client Upgrade is error" + e.getMessage());
		}
	}
}
