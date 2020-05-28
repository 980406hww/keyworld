package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.PtKeywordPositionHistory;

public interface PtKeywordPositionHistoryService extends IService<PtKeywordPositionHistory> {

    /**
     * 记录关键词排名
     */
    void savePositionHistory(Long uuid, String searchEngine, String terminalType, int position, Double todayFee);
}
