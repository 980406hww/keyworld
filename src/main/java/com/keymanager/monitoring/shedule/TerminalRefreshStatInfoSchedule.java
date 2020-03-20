package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordRefreshStatInfoService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.MachineGroupWorkInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author zhoukai
 * @Date 2018/11/28 15:26
 **/
@Component
public class TerminalRefreshStatInfoSchedule {
    private static Logger logger = LoggerFactory.getLogger(TerminalRefreshStatInfoSchedule.class);

    @Autowired
    private CustomerKeywordRefreshStatInfoService customerKeywordRefreshStatInfoService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private MachineGroupWorkInfoService machineGroupWorkInfoService;

    public void runTask(){
        logger.info("============= " + " Terminal Refresh Stat Info Task "+"===================");
        try{
            customerKeywordRefreshStatInfoService.updateCustomerKeywordStatInfo();
        } catch (Exception e) {
            logger.error("Terminal Refresh Stat Info is error" + e.getMessage());
        }

        logger.info("============= " + " machineGroup refresh WorkInfo Task "+"===================");
        try{
            machineGroupWorkInfoService.updateCustomerKeywordStatInfo();
        } catch (Exception e) {
            logger.error("machineGroup refresh WorkInfo is error" + e.getMessage());
        }
        logger.info("============= "+" Change Optimize Group Name Task "+"===================");
        try {
            customerKeywordService.changeOptimizeGroupName();
        } catch (Exception e) {
            logger.error(" Change Optimize Group Name is error" + e.getMessage());
        }
    }
}
