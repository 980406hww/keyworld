package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.DailyReport;
import java.util.List;

public interface DailyReportService {
    void autoTriggerDailyReport();
    void generateReport() throws Exception;
    List<DailyReport> searchCurrentDateCompletedReports(String userName);
    void resetDailyReportExcel(String terminalType, String customerUuids);
    void deleteDailyReportFromAWeekAgo();
    void removeDailyReportInToday(String userID);
}
