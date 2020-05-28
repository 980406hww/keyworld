package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.PtKeywordPositionHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PtKeywordPositionHistoryDao extends BaseMapper<PtKeywordPositionHistory> {

    PtKeywordPositionHistory getTodayPositionSummary(@Param("uuid") Long uuid);

    void insertPositionHistory(@Param("positionHistory") PtKeywordPositionHistory positionHistory);
}
