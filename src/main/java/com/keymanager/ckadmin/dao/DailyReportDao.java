package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.DailyReport;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("dailyReportDao2")
public interface DailyReportDao extends BaseMapper<DailyReport> {

    DailyReport findByStatus(@Param("status") String status);

    List<DailyReport> searchCurrentDateCompletedReports(@Param("userName") String userName);

    long selectLastId();

    void deleteDailyReportFromAWeekAgo();

    List<DailyReport> fetchDailyReportTriggeredInToday(@Param("userID") String userID, @Param("triggerMode") String triggerMode);
}
