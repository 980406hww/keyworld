package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.DailyReport;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DailyReportDao extends BaseMapper<DailyReport> {
    DailyReport findByStatus(@Param("status") String status);

    List<DailyReport> searchCurrentDateCompletedReports(@Param("userName") String userName);

    long selectLastId();

    void deleteDailyReportFromAWeekAgo();

    DailyReport fetchDailyReportTriggeredInToday(@Param("triggerMode") String triggerMode);
}
