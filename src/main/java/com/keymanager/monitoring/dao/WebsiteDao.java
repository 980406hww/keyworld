package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.WebsiteCriteria;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.monitoring.vo.WebsiteVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WebsiteDao extends BaseMapper<Website> {

    List<WebsiteVO> searchWebsites(Page<WebsiteVO> page, @Param("websiteCriteria") WebsiteCriteria websiteCriteria);

    List<Website> takeWebsitesForAccess();

    List<Website> searchExpireTime();
}
