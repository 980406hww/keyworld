package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerChargeRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerChargeRemindSchedule {
	private static Logger logger = LoggerFactory.getLogger(CustomerChargeRemindSchedule.class);

	@Autowired
	private CustomerChargeRuleService customerChargeRuleService;

	public void runTask(){
		logger.info("============= "+" Customer Charge Remind Task "+"===================");
		try {
			customerChargeRuleService.customerChargeRemind();
		} catch (Exception e) {
			logger.error("Customer Charge Remind is error" + e.getMessage());
		}
	}
}
