package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.TSMainKeyword;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

import java.util.List;

public interface TSMainKeywordDao extends BaseMapper<TSMainKeyword> {
    List<TSMainKeyword> findTSMainKeywords(Map<String,Object> items);

    List<TSMainKeyword> findTSMainKeywordByMainKeyword(@Param("mainKeyword") String mainKeyword);

    Integer getTSMainKeywordCount(TSMainKeyword tsMainKeyword);

    List<TSMainKeyword> getTSMainKeywordsForComplaints();

    Integer selectLastId();
}
