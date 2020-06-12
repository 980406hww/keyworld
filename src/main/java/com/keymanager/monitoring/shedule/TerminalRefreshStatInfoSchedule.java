package com.keymanager.monitoring.shedule;

import com.keymanager.ckadmin.service.CustomerKeywordRefreshStatInfoService;
import com.keymanager.ckadmin.service.MachineGroupWorkInfoService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author zhoukai
 * @Date 2018/11/28 15:26
 **/
@Component
public class TerminalRefreshStatInfoSchedule {
    private static Logger logger = LoggerFactory.getLogger(TerminalRefreshStatInfoSchedule.class);

    @Resource(name = "customerKeywordRefreshStatInfoService2")
    private CustomerKeywordRefreshStatInfoService customerKeywordRefreshStatInfoService;

    @Resource(name = "machineGroupWorkInfoService2")
    private MachineGroupWorkInfoService machineGroupWorkInfoService;

    @Autowired
    private CustomerKeywordService customerKeywordService;

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
