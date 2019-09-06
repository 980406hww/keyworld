package com.keymanager.monitoring.shedule;

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
	private MachineInfoService machineInfoService;

	@Autowired
	private PerformanceService performanceService;

	public void runTask(){
		logger.info("============= "+" Reset information Daily Task "+"===================");
		long startMilleSeconds = System.currentTimeMillis();
		try {
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting");
			logger.info("============= Reset information Daily Task starting ===================");
			//customerKeywordInvalidCountLogService.addCustomerKeywordInvalidCountLog();
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 2");
			logger.info("============= Reset information Daily Task starting 2===================");
			//configService.updateOptimizationDateAsToday();
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 3");
			logger.info("============= Reset information Daily Task starting 3===================");
			for(int i = 0; i < 20; i++) {
				customerKeywordService.resetOptimizationInfo();
				performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 3， --- " + i );
			}
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 4");
			logger.info("============= Reset information Daily Task starting 4===================");
			customerKeywordService.resetOptimizationInfoForNoOptimizeDate();
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 5");
			logger.info("============= Reset information Daily Task starting 5===================");
			machineInfoService.resetOptimizationInfo();

			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "ended");
			logger.info("============= "+" End Reset information Daily Task "+"===================");
		} catch (Exception e) {
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "error occured");
			e.printStackTrace();
			logger.error("Reset information Daily Task is error" + e.getMessage());
		}
	}
}
