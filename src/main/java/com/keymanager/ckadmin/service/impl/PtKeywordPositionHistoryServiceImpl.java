package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.PtKeywordPositionHistoryDao;
import com.keymanager.ckadmin.entity.PtKeywordPositionHistory;
import com.keymanager.ckadmin.service.PtKeywordPositionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service(value = "ptKeywordPositionHistoryService2")
public class PtKeywordPositionHistoryServiceImpl extends ServiceImpl<PtKeywordPositionHistoryDao, PtKeywordPositionHistory> implements PtKeywordPositionHistoryService {

    @Autowired
    private PtKeywordPositionHistoryDao ptKeywordPositionHistoryDao;

    @Override
    public void savePositionHistory(Long uuid, String searchEngine, String terminalType, int position, Double todayFee) {
        PtKeywordPositionHistory positionHistory = ptKeywordPositionHistoryDao.getTodayPositionSummary(uuid);
        if (null != positionHistory) {
            boolean updFlag = (positionHistory.getSystemPosition() == null || positionHistory.getSystemPosition() <= 0) ||
                    (position > 0 && positionHistory.getSystemPosition() > position);
            if (updFlag) {
                positionHistory.setSystemPosition(position);
                ptKeywordPositionHistoryDao.updateById(positionHistory);
            } else {
                positionHistory = new PtKeywordPositionHistory();
                positionHistory.setKeywordId(uuid);
                positionHistory.setSystemPosition(position);
                positionHistory.setSearchEngine(searchEngine);
                positionHistory.setTerminalType(terminalType);
                positionHistory.setTodayFee(todayFee);
                ptKeywordPositionHistoryDao.insertPositionHistory(positionHistory);
            }
        }
    }
}
