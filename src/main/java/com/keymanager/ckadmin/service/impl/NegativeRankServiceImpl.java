package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.NegativeRankCriteria;
import com.keymanager.ckadmin.dao.NegativeRankDao;
import com.keymanager.ckadmin.entity.NegativeRank;
import com.keymanager.ckadmin.service.NegativeRankService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("negativeRankService2")
public class NegativeRankServiceImpl extends ServiceImpl<NegativeRankDao, NegativeRank> implements NegativeRankService {

    private static Logger logger = LoggerFactory.getLogger(NegativeRankServiceImpl.class);

    @Autowired
    private NegativeRankDao negativeRankDao;

    @Override
    public Map<String, Object> findInitialNegativeRanks(NegativeRankCriteria negativeRankCriteria) {
        negativeRankCriteria.setSearchDate(DateUtils.setHours(negativeRankCriteria.getSearchDate(), 0));
        List<NegativeRank> negativeRanks = negativeRankDao.findInitialNegativeRanks(negativeRankCriteria.getSearchEngine(), negativeRankCriteria.getSearchDate());
        Map<String, Object> initialNegativeRankMap = new HashMap<>();
        for (NegativeRank negativeRank : negativeRanks) {
            initialNegativeRankMap.put(negativeRank.getKeyword(), negativeRank);
        }
        return initialNegativeRankMap;
    }

    @Override
    public List<NegativeRank> findNegativeRanks(NegativeRankCriteria negativeRankCriteria) {
        negativeRankCriteria.setSearchDate(DateUtils.setHours(negativeRankCriteria.getSearchDate(), 0));
        return negativeRankDao.findNegativeRanks(negativeRankCriteria.getSearchEngine(), negativeRankCriteria.getSearchDate());
    }

    @Override
    public Page<NegativeRank> searchNegativeRanks(Page<NegativeRank> page,NegativeRankCriteria negativeRankCriteria){
        page.setRecords(negativeRankDao.searchNegativeRanks(page,negativeRankCriteria));
        return page;
    }

    @Override
    public void updateNegativeRankKeyword(NegativeRank negativeRank){
        negativeRankDao.updateById(negativeRank);
    }

    @Override
    public List<NegativeRank> getTodayNegativeRanks(){
        return negativeRankDao.getTodayNegativeRanks();
    }
}