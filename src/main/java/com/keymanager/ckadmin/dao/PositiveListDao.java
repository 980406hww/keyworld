package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.PositiveListCriteria;
import com.keymanager.ckadmin.entity.PositiveList;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("positiveListDao2")
public interface PositiveListDao extends BaseMapper<PositiveList> {

    List<PositiveList> searchPositiveLists(Page<PositiveList> page, @Param("positiveListCriteria") PositiveListCriteria positiveListCriteria);

    List<PositiveList> searchPositiveListsFullMatching(@Param("positiveListCriteria") PositiveListCriteria positiveListCriteria);

    List<PositiveList> getSpecifiedKeywordPositiveLists(@Param("keyword") String keyword, @Param("terminalType") String terminalType);

}
