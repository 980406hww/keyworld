package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.entity.NegativeKeywordName;
import com.keymanager.ckadmin.criteria.NegativeKeywordNameCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("negativeKeywordNameDao2")
public interface NegativeKeywordNameDao extends BaseMapper<NegativeKeywordName> {

    List<String> getGroups();

    void insertBatchByList(@Param("group") String group, @Param("companyNames") List<String> companyNames);

    List<NegativeKeywordName> searchNegativeKeywordNames(Page<NegativeKeywordName> page, @Param("negativeKeywordNameCriteria") NegativeKeywordNameCriteria negativeKeywordNameCriteria);

    List<NegativeKeywordName> searchNegativeKeywordNameByGroup(@Param("negativeKeywordNameCriteria") NegativeKeywordNameCriteria negativeKeywordNameCriteria);

    List<String> getNegativeGroup();
}
