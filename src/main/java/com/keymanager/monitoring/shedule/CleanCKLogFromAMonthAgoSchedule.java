package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CleanCKLogFromAMonthAgoSchedule {
	private static Logger logger = LoggerFactory.getLogger(CleanCKLogFromAMonthAgoSchedule.class);

	@Autowired
	private CustomerKeywordService customerKeywordService;

	public void runTask(){
		logger.info("============= "+" Clean Customer Keyword Log From A Week Ago Task "+"===================");
		try {
			customerKeywordService.cleanCKLogFromAMonthAgo();
		} catch (Exception e) {
			logger.error("Clean Customer Keyword Log From A Week Ago is error" + e.getMessage());
		}
	}
}
