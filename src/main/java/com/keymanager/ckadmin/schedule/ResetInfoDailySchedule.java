package com.keymanager.ckadmin.schedule;

import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.util.Constants;
import com.keymanager.monitoring.service.CustomerKeywordInvalidCountLogService;
import com.keymanager.monitoring.service.MachineInfoService;
import com.keymanager.monitoring.service.PerformanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ResetInfoDailySchedule {
	private static Logger logger = LoggerFactory.getLogger(ResetInfoDailySchedule.class);

	@Autowired
	private CustomerKeywordInvalidCountLogService customerKeywordInvalidCountLogService;

	@Resource(name = "configService2")
	private ConfigService configService;

	@Resource(name = "customerKeywordService2")
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private MachineInfoService machineInfoService;

	@Autowired
	private PerformanceService performanceService;

	public void runTask(){
		logger.info("============= "+" Reset information Daily Task "+"===================");
		long startMilleSeconds = System.currentTimeMillis();
		try {
			Config config = configService.getConfig(Constants.CONFIG_TYPE_INVALID_DAYS, Constants.CONFIG_KEY_INVALID_MAX_DAYS_NAME);
			int invalidMaxDays = Integer.parseInt(config.getValue());
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting");
			logger.info("============= Reset information Daily Task starting ===================");
			//customerKeywordInvalidCountLogService.addCustomerKeywordInvalidCountLog();
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 2");
			logger.info("============= Reset information Daily Task starting 2===================");
			//configService.updateOptimizationDateAsToday();
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 3");
			logger.info("============= Reset information Daily Task starting 3===================");
			customerKeywordService.updateCustomerKeywordInvalidDays(invalidMaxDays);
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 4");
			logger.info("============= Reset information Daily Task starting 4===================");
			int notResetKeywordCount = customerKeywordService.getNotResetKeywordCount();
			for(int i = 0; notResetKeywordCount > 0; notResetKeywordCount -= 200000, i++) {
				customerKeywordService.resetOptimizationInfo(invalidMaxDays);
				performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 4， --- " + i );
			}
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 5");
			logger.info("============= Reset information Daily Task starting 5===================");
			customerKeywordService.resetOptimizationInfoForNoOptimizeDate(invalidMaxDays);
			customerKeywordService.updateRepeatedCustomerKeywordOptimizeStatus(invalidMaxDays);
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "starting 6");
			logger.info("============= Reset information Daily Task starting 6===================");
			machineInfoService.resetOptimizationInfo();
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "ended");

			logger.info("============= "+" Change Optimize Group Name Task "+"===================");
			try {
				customerKeywordService.changeOptimizeGroupName();
			} catch (Exception e) {
				logger.error(" Change Optimize Group Name is error" + e.getMessage());
			}
			logger.info("============= "+" End Reset information Daily Task "+"===================");
		} catch (Exception e) {
			performanceService.addPerformanceLog("ResetInfoDailySchedule", System.currentTimeMillis() - startMilleSeconds, "error occured");
			e.printStackTrace();
			logger.error("Reset information Daily Task is error" + e.getMessage());
		}
	}
}
