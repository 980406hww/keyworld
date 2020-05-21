package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.QZSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateQZSettingKeywordCountCurveSchedule {

    private static final Logger logger = LoggerFactory.getLogger(GenerateQZSettingKeywordCountCurveSchedule.class);

    @Autowired
    private QZSettingService qzSettingService;

    /**
     * 每天的8点，22点 更新一次站点操作词数曲线
     */
    public void runTask() {
        logger.info("============= "+" Generate QZSetting Keyword Count Curve Task "+"===================");
        try {
            qzSettingService.generateQZSettingKeywordCountCurve();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Generate QZSetting Keyword Count Curve Task is error " + e.getMessage());
        }
    }
}
