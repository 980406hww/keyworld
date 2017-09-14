package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdjustOptimizationCountSchedule {
	private static Logger logger = LoggerFactory.getLogger(AdjustOptimizationCountSchedule.class);

	@Autowired
	private CustomerKeywordService customerKeywordService;

	public void runTask(){
		logger.info("============= "+" Adjust Optimization Count Task "+"===================");
		try {
			customerKeywordService.adjustOptimizationCount();
		} catch (Exception e) {
			logger.error("Adjust Optimization Count is error" + e.getMessage());
		}
	}
}
