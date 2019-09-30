package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.DailyReportItem;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("dailyReportItemDao2")
public interface DailyReportItemDao extends BaseMapper<DailyReportItem> {
    public DailyReportItem findDailyReportItem(@Param("dailyReportUuid") long dailyReportUuid, @Param("status") String status);

    void deleteDailyReportItemFromAWeekAgo();

    public List<DailyReportItem> searchDailyReportItems(@Param("dailyReportUuid") long dailyReportUuid);

    public void deleteDailyReportItems(@Param("dailyReportUuid") long dailyReportUuid);
}
