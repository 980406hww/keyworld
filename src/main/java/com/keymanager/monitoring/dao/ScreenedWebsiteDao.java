package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ScreenedWebsiteCriteria;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.ScreenedWebsite;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScreenedWebsiteDao extends BaseMapper<ScreenedWebsite> {

    List<ScreenedWebsite> searchCustomerKeywordListsPage(Page<ScreenedWebsite> page, @Param("ScreenedWebsiteCriteria") ScreenedWebsiteCriteria ScreenedWebsiteCriteria);

    String getScreenedWebsiteByOptimizeGroupName(@Param("optimizeGroupName") String optimizeGroupName);
}
