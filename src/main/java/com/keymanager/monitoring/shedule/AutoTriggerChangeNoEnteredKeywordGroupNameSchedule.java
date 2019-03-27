package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoTriggerChangeNoEnteredKeywordGroupNameSchedule {
    private static Logger logger = LoggerFactory.getLogger(AutoTriggerChangeNoEnteredKeywordGroupNameSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask(){
        logger.info("============= "+" Change No Entered Keyword Group Name Task  "+"===================");
        try {
            customerKeywordService.updateNoEnteredKeywordGroupName();
        } catch (Exception e) {
            logger.error(" Change No Entered Keyword Group Name is error" + e.getMessage());
        }
    }




}
