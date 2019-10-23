package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.criteria.QZRateStatisticsCountCriteria;
import com.keymanager.ckadmin.vo.QZRateStatisticsCountVO;
import java.util.List;
import java.util.Map;

public interface QZRateStatisticsService {

    void generateQZRateStatistics();

    List<QZRateStatisticsCountVO> getQZRateStatisticCount(QZRateStatisticsCountCriteria qzRateStatisticsCountCriteria);

    Map generateEchartsData(List<QZRateStatisticsCountVO> qzRateStatisticsCountVOS);
}
