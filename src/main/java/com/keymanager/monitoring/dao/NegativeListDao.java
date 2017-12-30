package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.NegativeListCriteria;
import com.keymanager.monitoring.entity.NegativeList;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NegativeListDao extends BaseMapper<NegativeList> {

    List<NegativeList> searchNegativeLists(Page<NegativeList> page, @Param("negativeListCriteria") NegativeListCriteria negativeListCriteria);

    List<NegativeList> searchNegativeListsFullMatching(@Param("negativeListCriteria") NegativeListCriteria negativeListCriteria);

    List<NegativeList> getSpecifiedKeywordNegativeLists(@Param("keyword") String keyword);

    int selectLastId();

    List<NegativeList> negativeListsSynchronizeOfDelete(@Param("negativeList") NegativeList negativeList);

}
