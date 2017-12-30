package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.PositiveListCriteria;
import com.keymanager.monitoring.entity.PositiveList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PositiveListDao extends BaseMapper<PositiveList> {

    List<PositiveList> searchPositiveLists(Page<PositiveList> page, @Param("positiveListCriteria") PositiveListCriteria positiveListCriteria);

    List<PositiveList> searchPositiveListsFullMatching(@Param("positiveListCriteria") PositiveListCriteria positiveListCriteria);

    List<PositiveList> getSpecifiedKeywordPositiveLists(@Param("keyword") String keyword);

}
