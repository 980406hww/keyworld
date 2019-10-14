package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.WebsiteCriteria;
import com.keymanager.ckadmin.entity.Advertising;
import com.keymanager.ckadmin.entity.FriendlyLink;
import com.keymanager.ckadmin.entity.Website;
import com.keymanager.ckadmin.vo.ExternalWebsiteCheckResultVO;
import com.keymanager.ckadmin.vo.ExternalWebsiteVO;
import com.keymanager.ckadmin.vo.WebsiteVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface WebsiteService extends IService<Website> {

    Page<WebsiteVO> searchWebsites(Page<WebsiteVO> page, WebsiteCriteria websiteCriteria);

    void saveWebsite(Website website);

    Website getWebsite(Long uuid);

    void deleteWebsite(Long uuid);

    void deleteWebsites(List<String> uuids);

    void resetWebsiteAccessFailCount(List<String> uuids);

    Map<String, Object> accessURL();

    List<Website> accessExpireTimeURL();

    void putSalesInfoToWebsite(List uuids);

    FriendlyLink initFriendlyLink(HttpServletRequest request) throws Exception;

    void batchSaveFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, List<String> uuids);

    void batchUpdateFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, List<String> uuids, String originalFriendlyLinkUrl);

    void batchDelFriendlyLink(Map map);

    void batchSaveAdvertising(Advertising advertising);

    void batchUpdateAdvertising(Advertising advertising);

    void batchDelAdvertising(Map map);

    void synchronousFriendlyLink(Map<String, Object> requestMap);

    void synchronousAdvertising(Map<String, Object> requestMap);

    List<ExternalWebsiteVO> getAllWebsiteForexternalCheckStauts();

    void updateWebsiteStatus(List<ExternalWebsiteCheckResultVO> websiteCheckResultVOS);
}
