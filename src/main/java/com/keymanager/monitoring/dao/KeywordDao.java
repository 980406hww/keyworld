package com.keymanager.monitoring.dao;

import java.security.Key;
import java.util.List;
import java.util.Map;

import com.keymanager.monitoring.entity.Keyword;
import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

public interface KeywordDao extends BaseMapper<Keyword> {
	List<Map> findKeywords(@Param("keyword") String keyword, @Param("type") String type, @Param("userId") Long userId);

	Map getKeywordForCollect(@Param("monitorType") String monitorType);

	List<Map> findKeywordsForAutoComplete(@Param("userId") Long userId);

	Keyword existKeyword(@Param("keyword") String keyword,@Param("searchEngine") String searchEngine);
}
