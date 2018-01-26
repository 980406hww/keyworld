package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ControlRemainingKeywordIndicatorSchedule {
    private static Logger logger = LoggerFactory.getLogger(ControlRemainingKeywordIndicatorSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask(){
        logger.info("============= "+" Control Remaining Keyword Indicator Task "+"===================");
        try {
            customerKeywordService.controlRemainingKeywordIndicator();
        } catch (Exception e) {
            logger.error(" Control Remaining Keyword Indicator is error" + e.getMessage());
        }
    }

}
