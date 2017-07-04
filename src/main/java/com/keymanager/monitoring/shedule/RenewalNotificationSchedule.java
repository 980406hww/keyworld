package com.keymanager.monitoring.shedule;

import com.keymanager.manager.ClientStatusManager;
import com.keymanager.manager.CustomerKeywordManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RenewalNotificationSchedule {
	
	private static Logger logger = LoggerFactory.getLogger(RenewalNotificationSchedule.class);

	public void runTask(){
		logger.info("============= " + " Renewal Notification Task " + "===================");
		ClientStatusManager manager = new ClientStatusManager();
		try {
			manager.sendNotificationForRenewal();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" Renewal Notification is error" + e.getMessage());
		}
	}
}
