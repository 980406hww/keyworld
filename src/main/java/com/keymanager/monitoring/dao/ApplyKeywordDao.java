package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ApplyKeywordCriteria;
import com.keymanager.monitoring.entity.ApplyKeyword;
import com.keymanager.monitoring.entity.ServerAddress;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
public interface ApplyKeywordDao extends BaseMapper<ApplyKeyword> {
    String[] getKeywordApplyUuid(@Param("applyUuid") Long applyUuid);

    ApplyKeyword selectApplyKeyword(@Param("applyUuid")Long applyUuid,@Param("keyword") String keyword);

    List<ApplyKeyword> searchApplyKeyword(Page<ApplyKeyword> page, @Param("applyKeywordCriteria")ApplyKeywordCriteria applyKeywordCriteria);
}
