package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.NegativeRelatedKeyword;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NegativeRelatedKeywordDao extends BaseMapper<NegativeRelatedKeyword> {

    List<String> findNegativeRelatedKeyword(@Param("mainKeyword") String mainKeyword);

    void deleteNegativeRelatedKeywords(@Param("relatedKeyword") String relatedKeyword);
}
