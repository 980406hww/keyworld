package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.CustomerKeywordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ObserveOptimizationCountSchedule {
    private static Logger logger = LoggerFactory.getLogger(ObserveOptimizationCountSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    public void runTask(){
        logger.info("============= "+" Observe Optimization Count Task "+"===================");
        try {
            customerKeywordService.observeOptimizationCount();
        } catch (Exception e) {
            logger.error(" Observe Optimization Count is error" + e.getMessage());
        }
    }


}
