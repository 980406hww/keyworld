package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.NegativeRankCriteria;
import com.keymanager.ckadmin.entity.NegativeRank;
import java.util.List;
import java.util.Map;


public interface NegativeRankService {


    void saveNegativeRanks(List<NegativeRank> negativeRanks);

    Map<String, Object> findInitialNegativeRanks(NegativeRankCriteria negativeRankCriteria);

    List<NegativeRank> findNegativeRanks(NegativeRankCriteria negativeRankCriteria);

    Page<NegativeRank> searchNegativeRanks(Page<NegativeRank> page, NegativeRankCriteria negativeRankCriteria);

    void updateNegativeRankKeyword(NegativeRank negativeRank);

    List<NegativeRank> getTodayNegativeRanks();
}