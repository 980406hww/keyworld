package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.NegativeRankCriteria;
import com.keymanager.monitoring.dao.NegativeRankDao;
import com.keymanager.monitoring.entity.NegativeRank;
import com.keymanager.util.Utils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NegativeRankService extends ServiceImpl<NegativeRankDao, NegativeRank> {

    private static Logger logger = LoggerFactory.getLogger(NegativeRankService.class);

    @Autowired
    private NegativeRankDao negativeRankDao;

    public void saveNegativeRanks(List<NegativeRank> negativeRanks) {
        if(CollectionUtils.isNotEmpty(negativeRanks)) {
            NegativeRank firstNegativeRank = negativeRanks.get(0);
            Date date = DateUtils.setHours(new Date(), 0);
            negativeRankDao.deleteNegativeRanks(firstNegativeRank.getSearchEngine(), date);
            for (NegativeRank negativeRank : negativeRanks) {
                negativeRank.setCreateTime(new Date());
                negativeRankDao.insert(negativeRank);
            }
        }
    }

    public List<NegativeRank> findNegativeRanks(NegativeRankCriteria negativeRankCriteria) {
        negativeRankCriteria.setSearchDate(DateUtils.setHours(negativeRankCriteria.getSearchDate(), 0));
        return negativeRankDao.findNegativeRanks(negativeRankCriteria.getSearchEngine(), negativeRankCriteria.getSearchDate());
    }

    public Page<NegativeRank> searchNegativeRanks(Page page,NegativeRankCriteria negativeRankCriteria){
        page.setRecords(negativeRankDao.searchNegativeRanks(page,negativeRankCriteria));
        return page;
    }

    public Map<String, Object> findInitialNegativeRanks(NegativeRankCriteria negativeRankCriteria) {
        negativeRankCriteria.setSearchDate(DateUtils.setHours(negativeRankCriteria.getSearchDate(), 0));
        List<NegativeRank> negativeRanks = negativeRankDao.findInitialNegativeRanks(negativeRankCriteria.getSearchEngine(), negativeRankCriteria.getSearchDate());
        Map<String, Object> initialNegativeRankMap = new HashMap<String, Object>();
        for (NegativeRank negativeRank : negativeRanks) {
            initialNegativeRankMap.put(negativeRank.getKeyword(), negativeRank);
        }
        return initialNegativeRankMap;
    }

    public void updateNegativeRankKeyword(Map<String,Object> requestMap){
        NegativeRank negativeRank = new NegativeRank();
        Long uuid = Long.valueOf((String) requestMap.get("uuid"));
        Integer thisName = Integer.valueOf((String) requestMap.get("thisName"));
        String thisvalue = (String)requestMap.get("thisvalue");
        negativeRank.setUuid(uuid);
        negativeRank.setNegativeCount((Integer) requestMap.get("negativeCount"));
        switch (thisName){
            case 1:
                negativeRank.setFirstPageRanks(thisvalue);
                break;
            case 2:
                negativeRank.setSecondPageRanks(thisvalue);
                break;
            case 3:
                negativeRank.setThirdPageRanks(thisvalue);
                break;
            case 4:
                negativeRank.setFourthPageRanks(thisvalue);
                break;
            case 5:
                negativeRank.setFifthPageRanks(thisvalue);
                break;
            case 66:
                negativeRank.setOtherPageRanks(thisvalue);
                break;
        }
        negativeRankDao.updateById(negativeRank);
    }
}
