package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.DailyReportItem;
import java.util.List;

public interface DailyReportItemService {
    void createDailyReportItem(long dailyReportUuid, String terminalType, int customerUuid);
    void generateDailyReport(long dailyReportUuid, long dailyReportItemUuid) throws Exception;
    DailyReportItem findDailyReportItem(long dailyReportUuid, String status);
    void deleteDailyReportItemFromAWeekAgo();
    List<DailyReportItem> searchDailyReportItems(long dailyReportUuid);
    void deleteDailyReportItems(long dailyReportUuid);
}
