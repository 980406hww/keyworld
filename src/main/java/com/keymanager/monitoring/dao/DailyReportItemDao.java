package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.DailyReportItem;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DailyReportItemDao extends BaseMapper<DailyReportItem> {
    public DailyReportItem findDailyReportItem(@Param("dailyReportUuid") long dailyReportUuid, @Param("status") String status);
}
