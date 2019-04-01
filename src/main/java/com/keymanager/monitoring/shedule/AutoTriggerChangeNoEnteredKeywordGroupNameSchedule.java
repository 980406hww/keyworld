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
public class AutoTriggerChangeNoEnteredKeywordGroupNameSchedule {
    private static Logger logger = LoggerFactory.getLogger(AutoTriggerChangeNoEnteredKeywordGroupNameSchedule.class);

    @Autowired
    private CustomerKeywordService customerKeywordService;

    @Autowired
    private ConfigService configService;

    public void runTask(){
        logger.info("============= "+" Change No Entered Keyword Group Name Task  "+"===================");
        try {
            Config config = configService.getConfig(Constants.CONFIG_TYPE_NOENTEREDKEYWORDSCHEDULE_SWITCH, Constants.CONFIG_KEY_SWITCHNUMBER);
            if (config != null && config.getValue().equals("1")) {
                customerKeywordService.updateNoEnteredKeywordGroupName();
            }
        } catch (Exception e) {
            logger.error(" Change No Entered Keyword Group Name is error" + e.getMessage());
        }
    }
}
