package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
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
    private CustomerKeywordService customerKeywordService;

    public void runTask() {
        logger.info("============= "+" Clean Sys Customer Keyword Create Over One Week Task "+"===================");
        try {
            customerKeywordService.cleanSysCustomerKeywordCreateOverOneWeek();
        } catch (Exception e) {
            logger.error("Clean Sys Customer Keyword Create Over One Week Task is error" + e.getMessage());
        }
    }

}
