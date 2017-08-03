package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.TSMainKeyword;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
public interface TSMainKeywordDao extends BaseMapper<TSMainKeyword> {
    List<TSMainKeyword> findTSMainKeywords(@Param("uuid")Long uuid,@Param("keyword") String keyword, @Param("group")String group);
    List<TSMainKeyword> getTsMainKeywordsForComplaints();
    Integer selectLastId();
}
