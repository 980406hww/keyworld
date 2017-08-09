package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.TSMainKeyword;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
public interface TSMainKeywordDao extends BaseMapper<TSMainKeyword> {
    List<TSMainKeyword> findTSMainKeywords(Map<String, Object> items);

    List<TSMainKeyword> findTSMainKeywordByMainKeyword(@Param("mainKeyword") String mainKeyword);

    Integer getTSMainKeywordCount(TSMainKeyword tsMainKeyword);

    List<TSMainKeyword> getTSMainKeywordsForComplaints();

    List<TSMainKeyword> getTSMainKeywordsByCity(@Param("group") String ipCity);

    Integer selectLastId();
}
