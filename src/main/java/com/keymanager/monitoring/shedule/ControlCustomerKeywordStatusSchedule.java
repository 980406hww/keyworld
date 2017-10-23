package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlCustomerKeywordStatusSchedule {
	private static Logger logger = LoggerFactory.getLogger(ControlCustomerKeywordStatusSchedule.class);

	@Autowired
	private CustomerKeywordService customerKeywordService;

	public void runTask(){
		logger.info("============= "+" Control Customer Keyword Status Task "+"===================");
		try {
			customerKeywordService.controlCustomerKeywordStatus();
		} catch (Exception e) {
			logger.error("Control Customer Keyword Status " + e.getMessage());
		}
	}
}
