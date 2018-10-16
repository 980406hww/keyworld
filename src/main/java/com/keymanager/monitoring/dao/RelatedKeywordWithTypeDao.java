package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.RelatedKeyWordWithType;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RelatedKeywordWithTypeDao extends BaseMapper<RelatedKeyWordWithType>{
    List<RelatedKeyWordWithType> findRelatedKeywordWithType(@Param("mainKeyword") String mainKeyword);
}
