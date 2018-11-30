package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordRefreshStatInfoService;
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

    public void runTask(){
        logger.info("============= " + " Terminal Refresh Stat Info Task "+"===================");
        try{
            customerKeywordRefreshStatInfoService.updateCustomerKeywordStatInfo();
        } catch (Exception e) {
            logger.error("Terminal Refresh Stat Info is error" + e.getMessage());
        }
    }
}
