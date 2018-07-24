package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChangeOptimizeGroupNameSchedule {
    private static Logger logger = LoggerFactory.getLogger(ChangeOptimizeGroupNameSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask(){
        logger.info("============= "+" Change Optimize Group Name Task "+"===================");
        try {
            customerKeywordService.changeOptimizeGroupName();
        } catch (Exception e) {
            logger.error(" Change Optimize Group Name is error" + e.getMessage());
        }
    }


}
