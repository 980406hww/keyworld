package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerKeywordPositionSummary;
import org.apache.ibatis.annotations.Param;

public interface CustomerKeywordPositionSummaryDao extends BaseMapper<CustomerKeywordPositionSummary> {

    void addPositionSummary(@Param("customerKeywordPositionSummary") CustomerKeywordPositionSummary customerKeywordPositionSummary);

    CustomerKeywordPositionSummary getTodayPositionSummary(@Param("customerKeywordUuid") Long customerKeywordUuid);

    void deletePositionSummaryFromThreeMonthAgo();
}
