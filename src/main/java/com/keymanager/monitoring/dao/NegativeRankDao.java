package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.NegativeRankCriteria;
import com.keymanager.monitoring.entity.NegativeRank;
import org.apache.ibatis.annotations.Param;
import java.util.Date;
import java.util.List;

public interface NegativeRankDao extends BaseMapper<NegativeRank> {

    List<NegativeRank> findNegativeRanks(@Param("searchEngine") String searchEngine, @Param("searchDate") Date searchDate);

    void deleteNegativeRanks(@Param("searchEngine") String searchEngine, @Param("deleteDate") Date deleteDate);

    List<NegativeRank> searchNegativeRanks(Page<NegativeRank> page,@Param("negativeRankCriteria") NegativeRankCriteria negativeRankCriteria);

    List<NegativeRank> findInitialNegativeRanks(@Param("searchEngine")String searchEngine, @Param("createTime")Date createTime);

    List<NegativeRank> getTodayNegativeRanks();
}
