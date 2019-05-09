package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.ClientStatusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RenewalNotificationSchedule {
	private static Logger logger = LoggerFactory.getLogger(RenewalNotificationSchedule.class);

	@Autowired
	private ClientStatusService clientStatusService;

	public void runTask(){
		logger.info("============= " + " Renewal Notification Task " + "===================");
		try {
			clientStatusService.sendNotificationForRenewal();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" Renewal Notification is error" + e.getMessage());
		}
	}
}
