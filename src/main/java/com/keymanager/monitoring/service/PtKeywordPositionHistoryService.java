package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.PtKeywordPositionHistoryDao;
import com.keymanager.monitoring.entity.PtKeywordPositionHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PtKeywordPositionHistoryService extends ServiceImpl<PtKeywordPositionHistoryDao, PtKeywordPositionHistory> {

    @Autowired
    private PtKeywordPositionHistoryDao ptKeywordPositionHistoryDao;

    /**
     * 更新历史排名 replace into
     */
    public void insertKeywordPositionHistory(Long keywordId, Integer currentPosition, String currentDate) {
        ptKeywordPositionHistoryDao.insertKeywordPositionHistory(keywordId, currentPosition, currentDate);
    }
}
