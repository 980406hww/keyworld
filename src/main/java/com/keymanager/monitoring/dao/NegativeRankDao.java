package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.NegativeRank;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface NegativeRankDao extends BaseMapper<NegativeRank> {

    List<NegativeRank> findNegativeRanks(@Param("searchEngine") String searchEngine, @Param("searchDate") Date searchDate);
}
