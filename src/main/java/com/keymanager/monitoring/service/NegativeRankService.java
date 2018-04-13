package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.NegativeRankCriteria;
import com.keymanager.monitoring.dao.NegativeRankDao;
import com.keymanager.monitoring.entity.NegativeRank;
import com.keymanager.util.Utils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NegativeRankService extends ServiceImpl<NegativeRankDao, NegativeRank> {

    private static Logger logger = LoggerFactory.getLogger(NegativeRankService.class);

    @Autowired
    private NegativeRankDao negativeRankDao;

    public void saveNegativeRanks(List<NegativeRank> negativeRanks) {
        for (NegativeRank negativeRank : negativeRanks) {
            negativeRank.setCreateTime(new Date());
            negativeRankDao.insert(negativeRank);
        }
    }

    public List<NegativeRank> findNegativeRanks(NegativeRankCriteria negativeRankCriteria) {
        negativeRankCriteria.setSearchDate(DateUtils.setHours(negativeRankCriteria.getSearchDate(), 0));
        return negativeRankDao.findNegativeRanks(negativeRankCriteria.getSearchEngine(), negativeRankCriteria.getSearchDate());
    }
}
