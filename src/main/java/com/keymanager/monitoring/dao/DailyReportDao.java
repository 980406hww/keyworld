package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.DailyReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DailyReportDao extends BaseMapper<DailyReport> {
    List<DailyReport> findByStatus(@Param("status") String status);

    List<DailyReport> searchCurrentDateCompletedReports(@Param("terminalType") String terminalType);

    long selectLastId();

    void deleteDailyReportFromAWeekAgo();

    Boolean hasDailyReportTriggeredInToday(@Param("triggerMode") String triggerMode);
}
