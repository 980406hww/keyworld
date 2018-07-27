package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.WarnListCriteria;
import com.keymanager.monitoring.entity.WarnList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WarnListDao extends BaseMapper<WarnList> {

    List<WarnList> searchWarnLists(Page<WarnList> page, @Param("warnListCriteria") WarnListCriteria warnListCriteria);

    List<WarnList> searchWarnListsFullMatching(@Param("warnListCriteria") WarnListCriteria warnListCriteria);

    List<WarnList> getSpecifiedKeywordWarnLists(@Param("keyword") String keyword);

}
