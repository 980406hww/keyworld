package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.PtKeywordPositionHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PtKeywordPositionHistoryDao extends BaseMapper<PtKeywordPositionHistory> {

    void insertKeywordPositionHistory(@Param("keywordId") Long keywordId,
                                      @Param("currentPosition") Integer currentPosition,
                                      @Param("currentDate") String currentDate);
}
