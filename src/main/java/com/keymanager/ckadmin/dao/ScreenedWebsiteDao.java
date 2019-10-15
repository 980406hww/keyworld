package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;

import com.keymanager.ckadmin.criteria.ScreenedWebsiteCriteria;
import com.keymanager.ckadmin.entity.ScreenedWebsite;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("screenedWebsiteDao2")
public interface ScreenedWebsiteDao extends BaseMapper<ScreenedWebsite> {

    List<ScreenedWebsite> searchCustomerKeywordListsPage(Page<ScreenedWebsite> page,
        @Param("ScreenedWebsiteCriteria") ScreenedWebsiteCriteria ScreenedWebsiteCriteria);

    String getScreenedWebsiteByOptimizeGroupName(@Param("optimizeGroupName") String optimizeGroupName);
}
