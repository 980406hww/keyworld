package com.keymanager.monitoring.service;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.email.AccessWebsiteFailMailService;
import com.keymanager.monitoring.criteria.SalesManageCriteria;
import com.keymanager.monitoring.criteria.WebsiteBackGroundInfoCriteria;
import com.keymanager.monitoring.criteria.WebsiteCriteria;
import com.keymanager.monitoring.dao.WebsiteDao;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.monitoring.vo.WebsiteVO;
import com.keymanager.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by shunshikj08 on 2017/12/14.
 */
@Service
public class WebsiteService  extends ServiceImpl<WebsiteDao, Website> {
    private static Logger logger = LoggerFactory.getLogger(WebsiteService.class);

    @Autowired
    private WebsiteDao websiteDao;

    @Autowired
    private KeywordInfoService keywordInfoService;

    @Autowired
    private SalesManageService salesManageService;

    public Page<WebsiteVO> searchWebsites(Page<WebsiteVO> page, WebsiteCriteria websiteCriteria) {
        page.setRecords(websiteDao.searchWebsites(page, websiteCriteria));
        return page;
    }

    public void saveWebsite(Website website) {
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

    public void deleteWebsites(List<String> uuids) {
        for (String uuid : uuids) {
            deleteWebsite(Long.parseLong(uuid));
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
                    if(website.getAccessFailCount() > 0){
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
        dataBase.put("accessFailWebsites",accessFailWebsites);
        dataBase.put("accessSuccessWebsites",accessSuccessWebsites);
        return dataBase;
    }

    private void recordAccessFailInfo(Website website, List<Website> accessFailWebsites) {
        Integer accessFailCount = website.getAccessFailCount();
        if(accessFailCount == 0) {
            website.setAccessFailTime(new Date());
        }
        website.setAccessFailCount(accessFailCount + 1);
        websiteDao.updateById(website);
        if(accessFailCount > 1 && Utils.isPower(accessFailCount)) {
            accessFailWebsites.add(website);
        }
    }

    public List<Website> accessExpireTimeURL() {
        List<Website> websites = websiteDao.searchExpireTime();
        return websites;
    }

    public void putSalesInfoToWebsite(List uuids){
        List<WebsiteBackGroundInfoCriteria> websites = websiteDao.selectBackGroundInfoForUpdateSalesInfo(uuids);

        AsyncRestTemplate asyncRestTemplate = new AsyncRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);

        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        Map<String, Object> postMap = new HashMap<>();
        for (final WebsiteBackGroundInfoCriteria website : websites) {
            List<SalesManageCriteria> salesManages = salesManageService.getAllSalesInfo(website.getWebsiteType());
            postMap.put("sale_list", salesManages);
            postMap.put("sign", website.getUuid());
            postMap.put("username", website.getBackgroundUserName());
            postMap.put("password", website.getBackgroundPassword());
            final String url = "http://" + website.getBackgroundDomain() + "sales_management.php";
            params.set("params", postMap);
            HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(params, headers);
            ListenableFuture<ResponseEntity<String>> forEntity = asyncRestTemplate.postForEntity(url, requestEntity, String.class);
            forEntity.addCallback(new SuccessCallback<ResponseEntity<String>>() {
                @Override
                public void onSuccess(ResponseEntity<String> stringResponseEntity) {
                    String body = stringResponseEntity.getBody();
                    Map map = JSON.parseObject(body);
                    String status = (String) map.get("status");
                    Integer uuid = (Integer) map.get("sign");
                    Website websiteInfo = new Website();
                    websiteInfo.setUuid(Long.valueOf(uuid));
                    websiteInfo.setUpdateSalesInfoSign(status.equals("success") ? 1 : 2);// 成功，失败
                    websiteInfo.setUpdateTime(new Date());
                    websiteDao.updateById(websiteInfo);
                }
            }, new FailureCallback() {
                @Override
                public void onFailure(Throwable throwable) {
                    Website websiteInfo = new Website();
                    websiteInfo.setUuid(website.getUuid());
                    websiteInfo.setUpdateSalesInfoSign(3); // 异常
                    websiteInfo.setUpdateTime(new Date());
                    websiteDao.updateById(websiteInfo);
                }
            });
        }
    }
}
