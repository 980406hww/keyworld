package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.WebsiteCriteria;
import com.keymanager.ckadmin.entity.Website;
import com.keymanager.ckadmin.vo.ExternalWebsiteCheckResultVO;
import com.keymanager.ckadmin.vo.ExternalWebsiteVO;
import com.keymanager.ckadmin.vo.WebsiteBackendInfoVO;
import com.keymanager.ckadmin.vo.WebsiteVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository(value = "websiteDao2")
public interface WebsiteDao extends BaseMapper<Website> {

    List<WebsiteVO> searchWebsites(Page<WebsiteVO> page, @Param("websiteCriteria") WebsiteCriteria websiteCriteria);

    List<Website> takeWebsitesForAccess();

    List<Website> searchExpireTime();

    List<WebsiteBackendInfoVO> selectBackEndInfoForUpdateSalesInfo(@Param("uuids") List uuids);

    List<ExternalWebsiteVO> selectAllWebsiteForExternalCheckStatus();

    void updateWebSiteStatus(@Param("websiteCheckResultVOS") List<ExternalWebsiteCheckResultVO> websiteCheckResultVOS);
}
