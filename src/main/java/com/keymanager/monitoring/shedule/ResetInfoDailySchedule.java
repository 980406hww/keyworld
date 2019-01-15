package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.entity.ClientCookie;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ResetInfoDailySchedule {
	private static Logger logger = LoggerFactory.getLogger(ResetInfoDailySchedule.class);

	@Autowired
	private CustomerKeywordInvalidCountLogService customerKeywordInvalidCountLogService;

	@Autowired
	private ConfigService configService;

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private ClientStatusService clientStatusService;

	@Autowired
	private PerformanceService performanceService;

	public void runTask(){
		logger.info("============= "+" Reset informaiton Daily Task "+"===================");
		try {
			long startMilleSeconds = System.currentTimeMillis();
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting");

			customerKeywordInvalidCountLogService.addCustomerKeywordInvalidCountLog();
			configService.updateOptimizationDateAsToday();
			customerKeywordService.resetOptimizationInfo();
			clientStatusService.resetOptimizationInfo();

			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "ended");
			logger.info("============= "+" End Reset informaiton Daily Task "+"===================");
		} catch (Exception e) {
			logger.error("Client Upgrade is error" + e.getMessage());
		}
	}
}
