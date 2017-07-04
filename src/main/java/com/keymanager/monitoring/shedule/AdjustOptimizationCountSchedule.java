package com.keymanager.monitoring.shedule;

import com.keymanager.manager.CustomerKeywordManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdjustOptimizationCountSchedule {
	
	private static Logger logger = LoggerFactory.getLogger(AdjustOptimizationCountSchedule.class);

	public void runTask(){
		logger.info("============= "+" Adjust Optimization Count Task "+"===================");
		CustomerKeywordManager manager = new CustomerKeywordManager();
		try {
			manager.adjustOptimizationCount("keyword");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Adjust Optimization Count is error" + e.getMessage());
		}
	}
}
