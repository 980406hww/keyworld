package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerKeywordPositionSummary;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface CustomerKeywordPositionSummaryDao extends BaseMapper<CustomerKeywordPositionSummary> {

    void addPositionSummary(@Param("customerKeywordPositionSummary") CustomerKeywordPositionSummary customerKeywordPositionSummary);

    CustomerKeywordPositionSummary getTodayPositionSummary(@Param("customerKeywordUuid") Long customerKeywordUuid);

    void deletePositionSummaryFromAWeekAgo();

    /**
     * 根据传入的关键词ID查询关键词一周历史排名
     * @param customerKeywordUuid
     * @return
     */
    List<Integer> searchOneWeekPositionByCustomerUuid(@Param("customerKeywordUuid") Long customerKeywordUuid);
}
