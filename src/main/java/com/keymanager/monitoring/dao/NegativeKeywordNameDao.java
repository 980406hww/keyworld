package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.NegativeKeywordNameCriteria;
import com.keymanager.monitoring.entity.NegativeKeywordName;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NegativeKeywordNameDao extends BaseMapper<NegativeKeywordName> {

    List<NegativeKeywordName> searchNegativeKeywordNames(Page<NegativeKeywordName> page, @Param("negativeKeywordNameCriteria")NegativeKeywordNameCriteria negativeKeywordNameCriteria);

    List<NegativeKeywordName> searchNegativeKeywordNames(@Param("negativeKeywordNameCriteria")NegativeKeywordNameCriteria negativeKeywordNameCriteria);

    NegativeKeywordName getNegativeKeywordName(@Param("type") String type, @Param("group") String group);

    NegativeKeywordName getHasUrlNegativeKeywordName(@Param("group") String group);

    List<String> getNegativeGroup();

    void insertBatchByList(@Param("group")String group, @Param("companyNames")List<String> companyNames);
}
