package com.keymanager.ckadmin.service.impl;

import com.alibaba.dcm.DnsCacheManipulator;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.WebsiteCriteria;
import com.keymanager.ckadmin.dao.WebsiteDao;
import com.keymanager.ckadmin.entity.Advertising;
import com.keymanager.ckadmin.entity.FriendlyLink;
import com.keymanager.ckadmin.entity.Website;
import com.keymanager.ckadmin.enums.WebsiteRemoteConnectionEnum;
import com.keymanager.ckadmin.service.AdvertisingService;
import com.keymanager.ckadmin.service.FriendlyLinkService;
import com.keymanager.ckadmin.service.SalesManageService;
import com.keymanager.ckadmin.service.WebsiteService;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.vo.*;
import com.keymanager.monitoring.enums.PutSalesInfoSignEnum;
import com.keymanager.util.AESUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service(value = "websiteService2")
public class WebsiteServiceImpl extends ServiceImpl<WebsiteDao, Website> implements WebsiteService {

    private static Logger logger = LoggerFactory.getLogger(WebsiteServiceImpl.class);

    @Resource(name = "websiteDao2")
    private WebsiteDao websiteDao;

    @Resource(name = "salesManageService2")
    private SalesManageService salesManageService;

    @Resource(name = "advertisingService2")
    private AdvertisingService advertisingService;

    @Resource(name = "friendlyLinkService2")
    private FriendlyLinkService friendlyLinkService;

    public Page<WebsiteVO> searchWebsites(Page<WebsiteVO> page, WebsiteCriteria websiteCriteria) {
        List<WebsiteVO> websiteVOS = websiteDao.searchWebsites(page, websiteCriteria);
        for (WebsiteVO websiteVO : websiteVOS) {
            websiteVO.setFriendlyLinkCount(friendlyLinkService.searchFriendlyLinkCount(websiteVO.getUuid()));
            websiteVO.setAdvertisingCount(advertisingService.searchAdvertisingCount(websiteVO.getUuid()));
        }
        page.setRecords(websiteVOS);
        return page;
    }

    public void saveWebsite(Website website) {
        if (!website.getBackendDomain().endsWith("/")) {
            website.setBackendDomain(website.getBackendDomain() + "/");
        }
        website.setUpdateTime(new Date());
        if (null != website.getUuid()) {
            websiteDao.updateById(website);
        } else {
            websiteDao.insert(website);
        }
    }

    public Website getWebsite(Long uuid) {
        return websiteDao.selectById(uuid);
    }

    public void deleteWebsite(Long uuid) {
        websiteDao.deleteById(uuid);
    }

    public void deleteWebsites(List<Integer> uuids) {
        for (Integer uuid : uuids) {
            deleteWebsite(Long.valueOf(uuid));
        }
    }

    public void resetWebsiteAccessFailCount(List<String> uuids) {
        for (String uuid : uuids) {
            Website website = websiteDao.selectById(Long.parseLong(uuid));
            website.setAccessFailCount(0);
            website.setUpdateTime(new Date());
            websiteDao.updateById(website);
        }
    }

    public Map<String, Object> accessURL() {
        Map<String, Object> dataBase = new HashMap<String, Object>();
        List<Website> accessFailWebsites = new ArrayList<Website>();
        List<Website> accessSuccessWebsites = new ArrayList<Website>();
        List<Website> websites = websiteDao.takeWebsitesForAccess();
        for (Website website : websites) {
            try {
                String address = "http://" + website.getDomain() + "?" + System.currentTimeMillis();
                int status = 404;
                URL url = new URL(address);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setUseCaches(false);
                httpURLConnection.setConnectTimeout(5000);
                status = httpURLConnection.getResponseCode();
                if (200 == status) {
                    if (website.getAccessFailCount() > 0) {
                        accessSuccessWebsites.add(website);
                    }
                    website.setAccessFailTime(null);
                    website.setAccessFailCount(0);
                } else {
                    recordAccessFailInfo(website, accessFailWebsites);
                }
            } catch (IOException e) {
                recordAccessFailInfo(website, accessFailWebsites);
            } finally {
                website.setLastAccessTime(new Date());
                websiteDao.updateById(website);
            }
        }
        dataBase.put("accessFailWebsites", accessFailWebsites);
        dataBase.put("accessSuccessWebsites", accessSuccessWebsites);
        return dataBase;
    }

    private void recordAccessFailInfo(Website website, List<Website> accessFailWebsites) {
        Integer accessFailCount = website.getAccessFailCount();
        if (accessFailCount == 0) {
            website.setAccessFailTime(new Date());
        }
        website.setAccessFailCount(accessFailCount + 1);
        websiteDao.updateById(website);
        if (accessFailCount > 1 && Utils.isPower(accessFailCount)) {
            accessFailWebsites.add(website);
        }
    }

    public List<Website> accessExpireTimeURL() {
        List<Website> websites = websiteDao.searchExpireTime();
        return websites;
    }

    public void putSalesInfoToWebsite(List uuids) {
        final List<WebsiteBackendInfoVO> websites = websiteDao.selectBackEndInfoForUpdateSalesInfo(uuids);

        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        Map<String, Object> postMap = new HashMap<>();
        for (final WebsiteBackendInfoVO website : websites) {
            List<SalesManageVO> salesManages = salesManageService.getAllSalesInfo(website.getWebsiteType());
            postMap.put("sale_list", salesManages);
            postMap.put("username", website.getBackendUserName());
            postMap.put("password", website.getBackendPassword());
            String url = "http://" + website.getBackendDomain() + "sales_management.php";
            params.set("params", AESUtils.encrypt(postMap));
            HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(params, headers);
            ListenableFuture<ResponseEntity<String>> forEntity;
            if (website.getDnsAnalysisStatus() == 1) {
                DnsCacheManipulator.setDnsCache(website.getBackendDomain().split("/")[0], website.getServerIP());
                forEntity = asyncRestTemplate.postForEntity(url, requestEntity, String.class);
                DnsCacheManipulator.removeDnsCache(website.getBackendDomain().split("/")[0]);
            } else {
                forEntity = asyncRestTemplate.postForEntity(url, requestEntity, String.class);
            }
            forEntity.addCallback(new SuccessCallback<ResponseEntity<String>>() {
                @Override
                public void onSuccess(ResponseEntity<String> response) {
                    Website websiteInfo = new Website();
                    websiteInfo.setUuid(website.getUuid());
                    if (response.getStatusCode().toString().equals("200") || response.getStatusCode().toString().equals("301")) {
                        try {
                            Map map = JSON.parseObject(response.getBody());
                            String status = (String) map.get("status");
                            websiteInfo.setUpdateSalesInfoSign(
                                status.equals("success") ? PutSalesInfoSignEnum.Normal.getValue() : PutSalesInfoSignEnum.OperatingFail.getValue());
                        } catch (Exception e) {
                            websiteInfo.setUpdateSalesInfoSign(PutSalesInfoSignEnum.UpdateException.getValue());
                        }
                    } else {
                        websiteInfo.setUpdateSalesInfoSign(PutSalesInfoSignEnum.Refuse.getValue());
                        websiteDao.updateById(websiteInfo);
                    }
                    websiteInfo.setUpdateTime(new Date());
                    websiteDao.updateById(websiteInfo);
                }
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    Website websiteInfo = new Website();
                    websiteInfo.setUuid(website.getUuid());
                    websiteInfo.setUpdateSalesInfoSign(PutSalesInfoSignEnum.RequestException.getValue()); // 请求异常
                    websiteInfo.setUpdateTime(new Date());
                    websiteDao.updateById(websiteInfo);
                }
            });
        }
    }

    public FriendlyLink initFriendlyLink(HttpServletRequest request) throws Exception {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        FriendlyLink friendlyLink = new FriendlyLink();
        // friendlyLink.setCustomerUuid(Integer.valueOf(request.getParameter("customerUuid")));
        // friendlyLink.setCustomerInfo(request.getParameter("customerInfo"));
        friendlyLink.setFriendlyLinkWebName(request.getParameter("friendlyLinkWebName"));
        friendlyLink.setFriendlyLinkUrl(request.getParameter("friendlyLinkUrl"));
        friendlyLink.setFriendlyLinkIsCheck(Integer.parseInt(request.getParameter("friendlyLinkIsCheck")));
        friendlyLink.setFriendlyLinkSortRank(Integer.parseInt(request.getParameter("friendlyLinkSortRank")));
        friendlyLink.setFriendlyLinkType(request.getParameter("friendlyLinkType"));
        friendlyLink.setFriendlyLinkTypeId(Integer.parseInt(request.getParameter("friendlyLinkTypeId")));
        friendlyLink.setFriendlyLinkMsg(request.getParameter("friendlyLinkMsg"));
        friendlyLink.setExpirationTime(format.parse(request.getParameter("expirationTime")));
        friendlyLink.setFriendlyLinkEmail(request.getParameter("friendlyLinkEmail"));
        return friendlyLink;
    }

    public void batchSaveFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, List<String> uuids) {
        for (String uuidstr : uuids) {
            friendlyLink.setWebsiteUuid(Integer.parseInt(uuidstr));
            friendlyLinkService.saveOrUpdateConnectionCMS(friendlyLink, file, WebsiteRemoteConnectionEnum.add.name());
            if (friendlyLink.getFriendlyLinkSortRank() != -1) {
                friendlyLinkService.retreatSortRank(friendlyLink.getWebsiteUuid(), friendlyLink.getFriendlyLinkSortRank());
            } else {
                friendlyLink.setFriendlyLinkSortRank(friendlyLinkService.selectMaxSortRank(friendlyLink.getWebsiteUuid()) + 1);
            }
            friendlyLinkService.insertFriendlyLink(friendlyLink);
        }
    }

    public void batchUpdateFriendlyLink(MultipartFile file, FriendlyLink friendlyLink, String[] uuids, String originalFriendlyLinkUrl) {
        for (String uuidstr : uuids) {
            List<Map> friendlyLinkInfos = friendlyLinkService.searchOriginalSortRank(Integer.parseInt(uuidstr), originalFriendlyLinkUrl);
            for (Map friendlyLinkInfo : friendlyLinkInfos) {
                friendlyLink.setWebsiteUuid(Integer.parseInt(uuidstr));
                friendlyLink.setUuid(Long.valueOf((Integer) friendlyLinkInfo.get("uuid")));
                friendlyLink.setFriendlyLinkId((Integer) friendlyLinkInfo.get("friendlyLinkId"));
                friendlyLink.setUpdateTime(new Date());
                friendlyLinkService.saveOrUpdateConnectionCMS(friendlyLink, file, WebsiteRemoteConnectionEnum.saveedit.name());
                int originalSortRank = (Integer) friendlyLinkInfo.get("friendlyLinkSortRank");
                if (originalSortRank < friendlyLink.getFriendlyLinkSortRank()) {
                    friendlyLinkService.updateCentreSortRank(originalSortRank, friendlyLink.getFriendlyLinkSortRank(), friendlyLink.getWebsiteUuid());
                } else {
                    friendlyLinkService.updateCentreSortRank(friendlyLink.getFriendlyLinkSortRank(), originalSortRank, friendlyLink.getWebsiteUuid());
                }
                friendlyLinkService.updateFriendlyLinkById(friendlyLink);
            }
        }
    }

    public void batchDelFriendlyLink(Map map) {
        String friendlyLinkUrl = (String) map.get("friendlyLinkUrl");
        List<String> uuids = (List<String>) map.get("uuids");
        for (String uuidStr : uuids) {
            Long uuid = Long.valueOf(uuidStr);
            List<String> friendlyLinkids = friendlyLinkService.searchFriendlyLinkidsByUrl(uuid, friendlyLinkUrl);
            String[] uuidArrays = new String[friendlyLinkids.size()];
            friendlyLinkids.toArray(uuidArrays);
            friendlyLinkService.deleteConnectionCMS(uuid, uuidArrays);
        }
        friendlyLinkService.batchDeleteFriendlyLinkByUrl(friendlyLinkUrl, uuids);
    }

    public void batchSaveAdvertising(Advertising advertising) {
        String[] uuids = advertising.getUuids().split(",");
        for (String uuidstr : uuids) {
            advertising.setWebsiteUuid(Integer.parseInt(uuidstr));
            if (advertisingService.saveOrUpdateConnectionCMS(advertising, WebsiteRemoteConnectionEnum.add.name())) {
                advertisingService.insertAdvertising(advertising);
            }
        }
    }

    public void batchUpdateAdvertising(Advertising advertising) {
        String[] uuids = advertising.getUuids().split(",");
        for (String uuidStr : uuids) {
            advertising.setWebsiteUuid(Integer.parseInt(uuidStr));
            List<Map> advertisingIds = advertisingService.searchIdByOriginalAdvertisingTagname(Integer.parseInt(uuidStr), advertising.getOriginalAdvertisingTagname());
            for (Map advertisingIdMap : advertisingIds) {
                advertising.setAdvertisingId((Integer) advertisingIdMap.get("advertisingId"));
                advertising.setUuid(Long.valueOf((Integer) advertisingIdMap.get("uuid")));
                advertisingService.updateAdvertising(advertising);
            }
        }
    }

    public void batchDelAdvertising(Map map) {
        String advertisingTagname = (String) map.get("advertisingTagname");
        List<String> uuids = (List<String>) map.get("uuids");
        for (String uuidStr : uuids) {
            Long uuid = Long.valueOf(uuidStr);
            List<String> advertisingIds = advertisingService.searchAdvertisingidsByAdvertisingTagname(uuid, advertisingTagname);
            String[] uuidArrays = new String[advertisingIds.size()];
            advertisingIds.toArray(uuidArrays);
            advertisingService.deleteConnectionCMS(uuid, uuidArrays);
        }
        advertisingService.batchDeleteAdvertisingByAdvertisingTagname(advertisingTagname, uuids);
    }

    public void synchronousFriendlyLink(Map<String, Object> requestMap) {
        List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
        for (Integer uuid : uuids) {
            Website website = new Website();
            website.setUuid((long) uuid);
            try {
                List<FriendlyLinkVO> friendlyLinkVOS = friendlyLinkService.selectConnectionCMS((long) uuid);
                if (!friendlyLinkVOS.isEmpty()) {
                    List<Integer> friendlyLinkIds = friendlyLinkService.selectByWebsiteId((long) uuid);
                    for (FriendlyLinkVO friendlyLinkVO : friendlyLinkVOS) {
                        FriendlyLink friendlyLink = new FriendlyLink();
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        // friendlyLink.setCustomerUuid(Integer.valueOf((String) requestMap.get("customerUuid")));
                        friendlyLink.setFriendlyLinkId(friendlyLinkVO.getId());
                        friendlyLink.setExpirationTime(format.parse((String) requestMap.get("expirationTime")));
                        friendlyLink.setRenewTime(format.parse((String) requestMap.get("renewTime")));
                        friendlyLink.setWebsiteUuid(uuid);
                        friendlyLinkService.initSynchronousFriendlyLink(friendlyLink, friendlyLinkVO);
                        if (friendlyLinkIds.contains(friendlyLinkVO.getId())) {
                            friendlyLink.setUuid(friendlyLinkService.selectIdByFriendlyLinkId((long) uuid, friendlyLinkVO.getId()));
                            friendlyLinkService.updateById(friendlyLink);
                        } else {
                            friendlyLinkService.insertFriendlyLink(friendlyLink);
                        }
                    }
                }
                website.setSynchronousFriendlyLinkSign(PutSalesInfoSignEnum.Normal.getValue());
                websiteDao.updateById(website);
            } catch (Exception e) {
                website.setSynchronousFriendlyLinkSign(PutSalesInfoSignEnum.RequestException.getValue());
                websiteDao.updateById(website);
                continue;
            }
        }
    }

    public void synchronousAdvertising(Map<String, Object> requestMap) {
        List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
        for (Integer uuid : uuids) {
            Website website = new Website();
            website.setUuid((long) uuid);
            try {
                List<AdvertisingVO> advertisingVOS = advertisingService.selectConnectionCMS((long) uuid);
                if (!advertisingVOS.isEmpty()) {
                    List<Integer> advertisingIds = advertisingService.selectByWebsiteId((long) uuid);
                    for (AdvertisingVO advertisingVO : advertisingVOS) {
                        Advertising advertising = new Advertising();
                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        advertising.setRenewTime(format.parse((String) requestMap.get("renewTime")));
                        // advertising.setCustomerUuid(Integer.valueOf((String) requestMap.get("customerUuid")));
                        advertising.setAdvertisingId(advertisingVO.getAid());
                        advertising.setWebsiteUuid(uuid);
                        advertisingService.initSynchronousAdvertising(advertising, advertisingVO);
                        if (advertisingIds.contains(advertisingVO.getAid())) {
                            advertising.setUuid(advertisingService.selectIdByAdvertisingId((long) uuid, advertisingVO.getAid()));
                            advertisingService.updateById(advertising);
                        } else {
                            advertisingService.insertAdvertising(advertising);
                        }
                    }
                }
                website.setSynchronousAdvertisingSign(PutSalesInfoSignEnum.Normal.getValue());
                websiteDao.updateById(website);
            } catch (Exception e) {
                website.setSynchronousAdvertisingSign(PutSalesInfoSignEnum.RequestException.getValue());
                websiteDao.updateById(website);
                continue;
            }
        }
    }

    public List<ExternalWebsiteVO> getAllWebsiteForexternalCheckStauts() {
        return websiteDao.selectAllWebsiteForExternalCheckStatus();
    }

    public void updateWebsiteStatus(List<ExternalWebsiteCheckResultVO> websiteCheckResultVOS) {
        websiteDao.updateWebSiteStatus(websiteCheckResultVOS);
    }
}
