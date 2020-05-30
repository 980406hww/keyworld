package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.PtKeywordPositionHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PtKeywordPositionHistoryDao extends BaseMapper<PtKeywordPositionHistory> {

    /**
     * 更新历史排名 replace into
     */
    void insertKeywordPositionHistory(@Param("positionHistory") PtKeywordPositionHistory positionHistory);

    /**
     * 清理已移除关键词的历史排名数据
     */
    void cleanNotExistKeywordPositionHistory();
}
