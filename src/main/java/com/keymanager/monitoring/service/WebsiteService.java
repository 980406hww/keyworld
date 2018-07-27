package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.common.email.AccessWebsiteFailMailService;
import com.keymanager.monitoring.criteria.WebsiteCriteria;
import com.keymanager.monitoring.dao.WebsiteDao;
import com.keymanager.monitoring.entity.Website;
import com.keymanager.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Page<Website> searchWebsites(Page<Website> page, WebsiteCriteria websiteCriteria) {
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
}
