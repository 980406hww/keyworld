package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.NegativeRankCriteria;
import com.keymanager.ckadmin.entity.NegativeRank;
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("negativeRankDao2")
public interface NegativeRankDao extends BaseMapper<NegativeRank> {

    List<NegativeRank> findNegativeRanks(@Param("searchEngine") String searchEngine, @Param("searchDate") Date searchDate);

    List<NegativeRank> searchNegativeRanks(Page<NegativeRank> page, @Param("negativeRankCriteria") NegativeRankCriteria negativeRankCriteria);

    List<NegativeRank> findInitialNegativeRanks(@Param("searchEngine") String searchEngine, @Param("createTime") Date createTime);

    List<NegativeRank> getTodayNegativeRanks();
}
