package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.TSMainKeyword;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
public interface TSMainKeywordDao extends BaseMapper<TSMainKeyword> {
    List<TSMainKeyword> findTSMainKeywords(Page<TSMainKeyword> page,@Param("keyword") String keyword,@Param("group")  String group);

    List<TSMainKeyword> findTSMainKeywordByMainKeyword(@Param("mainKeyword") String mainKeyword);

    List<TSMainKeyword> getTSMainKeywordsForComplaints();

    List<TSMainKeyword> getTSMainKeywordsByCity(@Param("group") String ipCity);

    Integer selectLastId();
}
