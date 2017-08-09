package com.keymanager.monitoring.service;

import com.keymanager.monitoring.common.email.ComplaintsReportMailService;
import com.keymanager.monitoring.vo.TSMainKeywordVO;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComplaintsReportService {

    @Autowired
    private ComplaintsReportMailService complaintsReportMailService;

    @Autowired
    private TSNegativeKeywordService tsNegativeKeywordService;

    public void sendComplaintsReports() {
        //判断什么时候发送邮件,以及发送的内容
        List<TSMainKeywordVO> PCOver2WeekstSmainKeywordVOS = tsNegativeKeywordService.complaintsReportContentPC2weeks();
        List<TSMainKeywordVO> phoneOver2WeekstSmainKeywordVOS = tsNegativeKeywordService.complaintsReportContentPhone2weeks();
        List<TSMainKeywordVO> PCOver3TimestSmainKeywordVOS = tsNegativeKeywordService.complaintsReportContentPC3times();
        List<TSMainKeywordVO> phoneOver3TimestSmainKeywordVOS = tsNegativeKeywordService.complaintsReportContentPhone3times();

        if (CollectionUtils.isNotEmpty(PCOver2WeekstSmainKeywordVOS) || CollectionUtils.isNotEmpty(
            phoneOver2WeekstSmainKeywordVOS) ) {
            complaintsReportMailService.sendComplaintsReportOver2weeks("郑远", "929339036@qq.com,179472582@qq.com",
                PCOver2WeekstSmainKeywordVOS,
                phoneOver2WeekstSmainKeywordVOS);
        }

        if (CollectionUtils.isNotEmpty(PCOver3TimestSmainKeywordVOS) || CollectionUtils.isNotEmpty(
            phoneOver3TimestSmainKeywordVOS)) {
            complaintsReportMailService.sendComplaintsReportOver3Times("郑远", "929339036@qq.com,179472582@qq.com",
                PCOver3TimestSmainKeywordVOS,
                phoneOver3TimestSmainKeywordVOS);
        }
    }
}

