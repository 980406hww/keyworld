package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.SnapshotHistoryCriteria;
import com.keymanager.monitoring.dao.SnapshotHistoryDao;
import com.keymanager.monitoring.entity.SnapshotHistory;
import com.keymanager.monitoring.vo.SnapshotHistoryVO;
import com.keymanager.util.Utils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public SnapshotHistoryVO getNegativeHistoryRanks(SnapshotHistoryCriteria snapshotHistoryCriteria) {
        int searchDays;
        Date beginDate;
        Date endDate = Utils.string2Timestamp(snapshotHistoryCriteria.getEndDate());
        // 获取排名表格的列数
        if(StringUtils.isBlank(snapshotHistoryCriteria.getBeginDate())) {
            searchDays = 7;
            Date date = Utils.addDay(Utils.string2Timestamp(Utils.getCurrentDate()), -7);
            snapshotHistoryCriteria.setBeginDate(Utils.formatDate(date, "yyyy-MM-dd"));
            beginDate = Utils.string2Timestamp(snapshotHistoryCriteria.getBeginDate());
        } else {
            beginDate = Utils.string2Timestamp(snapshotHistoryCriteria.getBeginDate());
            searchDays = Utils.getIntervalDays(beginDate, endDate);
        }

        List<String> customerInfos = new ArrayList<String>();
        List<String> searchEngineInfos = new ArrayList<String>();
        Long customerUuid = 0L;
        String contactPerson = "";
        for(SnapshotHistory snapshotHistory : snapshotHistoryCriteria.getSnapshotList()) { //ztree
            // 按客户查询负面排名
            if(!customerUuid.equals(snapshotHistory.getCustomerUuid())) {
                customerUuid = snapshotHistory.getCustomerUuid();
                contactPerson = snapshotHistory.getContactPerson();
                customerInfos.add(customerUuid + "," + contactPerson + "," + snapshotHistoryCriteria.getBeginDate() + "," + snapshotHistoryCriteria.getEndDate());

                snapshotHistoryCriteria.setCustomerUuid(snapshotHistory.getCustomerUuid());
                List<SnapshotHistory> customerNegativeLists = snapshotHistoryDao.searchCustomerNegativeLists(snapshotHistoryCriteria);
                // 组建一个客户表格的排名数据
                for (SnapshotHistory ztreeList : snapshotHistoryCriteria.getSnapshotList()) {
                    if(ztreeList.getCustomerUuid().equals(customerUuid)) {
                        String[] rankArray = new String[searchDays];
                        // 组建一行排名数据
                        for (SnapshotHistory customerNegativeList : customerNegativeLists) {
                            if(ztreeList.getKeyword().equals(customerNegativeList.getKeyword()) && ztreeList.getSearchEngine().equals(customerNegativeList.getSearchEngine())) {
                                int col = Utils.getIntervalDays(beginDate, customerNegativeList.getCreateTime());
                                rankArray[col] = (rankArray[col] == null ? "" : rankArray[col]) + customerNegativeList.getOrder() + ",";
                            }
                        }
                        String rankInfo = customerUuid + "-" + ztreeList.getKeyword() + "-" + ztreeList.getSearchEngine();
                        for (int i = 0; i < rankArray.length; i++) {
                            if(rankArray[i] == null) {
                                rankArray[i] = "";
                            } else {
                                rankArray[i] = rankArray[i].substring(0, rankArray[i].length() - 1);
                            }
                            rankInfo = rankInfo + "-" + rankArray[i];
                        }
                        searchEngineInfos.add(rankInfo);
                    }
                }
            }
        }
        SnapshotHistoryVO snapshotHistoryVO = new SnapshotHistoryVO();
        snapshotHistoryVO.setCustomerInfos(customerInfos);
        snapshotHistoryVO.setSearchEngineInfos(searchEngineInfos);
        return snapshotHistoryVO;
    }
}
