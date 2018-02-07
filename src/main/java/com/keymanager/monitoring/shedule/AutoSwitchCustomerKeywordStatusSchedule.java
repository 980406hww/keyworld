package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoSwitchCustomerKeywordStatusSchedule {
	private static Logger logger = LoggerFactory.getLogger(AutoSwitchCustomerKeywordStatusSchedule.class);

	@Autowired
	private CustomerService customerService;

	public void runTask(){
		logger.info("============= "+" Auto Switch Customer Keyword Status Task "+"===================");
		try {
			customerService.autoSwitchCustomerKeywordStatus();
		} catch (Exception e) {
			logger.error(" Auto Switch Customer Keyword Status is error" + e.getMessage());
		}
	}
}
