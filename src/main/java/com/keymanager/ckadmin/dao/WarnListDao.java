package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.WarnListCriteria;
import com.keymanager.ckadmin.entity.WarnList;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository(value = "warnListDao2")
public interface WarnListDao extends BaseMapper<WarnList> {

    List<WarnList> searchWarnLists(Page<WarnList> page, @Param("warnListCriteria") WarnListCriteria warnListCriteria);

    List<WarnList> searchWarnListsFullMatching(@Param("warnListCriteria") WarnListCriteria warnListCriteria);

    List<WarnList> getSpecifiedKeywordWarnLists(@Param("keyword") String keyword);

}
