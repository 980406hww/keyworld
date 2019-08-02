package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CacheCheckingEnteredCustomerKeywordSchedule {

    private static Logger logger = LoggerFactory.getLogger(CacheCheckingEnteredCustomerKeywordSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private ConfigService configService;

    public void runTask() {
        logger.info("============= " + " Cache checking entered Customer Keyword Task " + "===================");
        try {
            Config config = configService.getConfig(Constants.CONFIG_TYPE_NO_ENTERED_KEYWORD_SCHEDULE_SWITCH, Constants.CONFIG_KEY_SWITCH_NUMBER);
            if (config != null && config.getValue().equals("1")) {
                customerKeywordService.cacheCheckingEnteredCustomerKeywords();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Cache checking entered Customer Keyword is error" + e.getMessage());
        }
    }
}
