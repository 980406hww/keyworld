package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.TSMainKeyword;

import java.util.Map;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
public interface TSMainKeywordDao extends BaseMapper<TSMainKeyword> {
    List<TSMainKeyword> findTSMainKeywords(Map<String,Object> items);

    Integer getTSmainKeywordCount(TSMainKeyword tsMainKeyword);

    List<TSMainKeyword> getTsMainKeywordsForComplaints();

    Integer selectLastId();
}
