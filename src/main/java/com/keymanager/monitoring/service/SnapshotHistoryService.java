package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.SnapshotHistoryDao;
import com.keymanager.monitoring.entity.SnapshotHistory;
import com.keymanager.monitoring.vo.SnapshotHistoryVO;
import com.keymanager.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SnapshotHistoryService extends ServiceImpl<SnapshotHistoryDao, SnapshotHistory> {
    private static Logger logger = LoggerFactory.getLogger(SnapshotHistoryService.class);

    @Autowired
    private SnapshotHistoryDao snapshotHistoryDao;

    public void saveSnapshotInfos(List<SnapshotHistory> snapshotList) {
        SnapshotHistory sh = new SnapshotHistory();
        sh.setCreateTime(Utils.parseDate(Utils.getCurrentDate(), "yyyy-MM-dd"));
        for (SnapshotHistory snapshotHistory : snapshotList) {
            if(snapshotHistory.getOrder() == 1) {
                sh.setCustomerUuid(snapshotHistory.getCustomerUuid());
                sh.setKeyword(snapshotHistory.getKeyword());
                sh.setSearchEngine(snapshotHistory.getSearchEngine());
                snapshotHistoryDao.deleteSnapshotHistorys(sh);
            }
            snapshotHistoryDao.insert(snapshotHistory);
        }
    }

    public SnapshotHistoryVO searchSnapshotHistories(String searchDate) {
        Date date = Utils.parseDate(searchDate, "yyyy-MM-dd");
        List<String> customerInfos = snapshotHistoryDao.searchRelatedCustomerInfos(date);
        List<String> searchEngineInfos = snapshotHistoryDao.searchRelatedEngineInfos(date);
        List<SnapshotHistory> snapshotHistories = snapshotHistoryDao.searchSnapshotHistories(date);
        SnapshotHistoryVO snapshotHistoryVO = new SnapshotHistoryVO();
        snapshotHistoryVO.setCustomerInfos(customerInfos);
        snapshotHistoryVO.setSearchEngineInfos(searchEngineInfos);
        snapshotHistoryVO.setSnapshotHistories(snapshotHistories);
        return snapshotHistoryVO;
    }
}
