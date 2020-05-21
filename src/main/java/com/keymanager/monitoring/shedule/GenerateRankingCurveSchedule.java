package com.keymanager.monitoring.shedule;

import com.keymanager.monitoring.service.QZSettingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GenerateRankingCurveSchedule {

    private static Logger logger = LoggerFactory.getLogger(GenerateRankingCurveSchedule.class);

    @Autowired
    private QZSettingService qzSettingService;

    /**
     * 每俩小时更新一次站点排名曲线
     */
    public void runTask() {
        logger.info("============= "+" Generate Ranking Curve  Task "+"===================");
        try {
            qzSettingService.generateRankingCurve();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(" Generate Ranking Curve Task is error  " + e.getMessage());
        }
    }
}
