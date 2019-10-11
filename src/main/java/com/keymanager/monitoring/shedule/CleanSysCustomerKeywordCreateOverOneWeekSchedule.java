package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.SysCustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shunshikj40
 */
@Component
public class CleanSysCustomerKeywordCreateOverOneWeekSchedule {

    private static final Logger logger = LoggerFactory.getLogger(CleanSysCustomerKeywordCreateOverOneWeekSchedule.class);

    @Autowired
    private SysCustomerKeywordService sysCustomerKeywordService;

    public void runTask() {
        logger.info("============= "+" Clean Sys Customer Keyword Create Over One Week Task "+"===================");
        try {
            sysCustomerKeywordService.cleanSysCustomerKeywordCreateOverOneWeek();
        } catch (Exception e) {
            logger.error("Clean Sys Customer Keyword Create Over One Week Task is error" + e.getMessage());
        }
    }

}
