package com.keymanager.ckadmin.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.ScreenedWebsiteCriteria;
import com.keymanager.ckadmin.dao.ScreenedWebsiteDao;
import com.keymanager.ckadmin.entity.ScreenedWebsite;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.ScreenedWebsiteListCacheService;
import com.keymanager.ckadmin.service.ScreenedWebsiteService;

import com.keymanager.util.Constants;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("screenedWebsiteService2")
public class ScreenedWebsiteServiceImpl extends ServiceImpl<ScreenedWebsiteDao, ScreenedWebsite> implements ScreenedWebsiteService {

    @Resource(name = "screenedWebsiteDao2")
    private ScreenedWebsiteDao screenedWebsiteDao;

    @Resource(name = "screenedWebsiteListCacheService2")
    private ScreenedWebsiteListCacheService screenedWebsiteListCacheService;
    @Resource(name = "configService2")
    private ConfigService configService;


    @Override
    public Page<ScreenedWebsite> searchCustomerKeywordListsPage(Page<ScreenedWebsite> page, ScreenedWebsiteCriteria screenedWebsiteCriteria) {
        List<ScreenedWebsite> screenedWebsites = screenedWebsiteDao.searchCustomerKeywordListsPage(page, screenedWebsiteCriteria);
        page.setRecords(screenedWebsites);
        return page;
    }

    @Override
    public Boolean saveScreenedWebsite(ScreenedWebsite screenedWebsite, String userName, String password) {
        Boolean result = true;
        if (null != screenedWebsite.getUuid()) {
            screenedWebsite.setUpdateTime(new Date());
            screenedWebsiteDao.updateById(screenedWebsite);
            screenedWebsiteListCacheService.screenedWebsiteListCacheEvict(screenedWebsite.getOptimizeGroupName());
            result = postScreenedWebsiteRequest(screenedWebsite.getOptimizeGroupName(), userName, password);
        } else {
            screenedWebsiteDao.insert(screenedWebsite);
        }
        return result;
    }

    @Override
    public ScreenedWebsite getScreenedWebsite(Long uuid) {
        return screenedWebsiteDao.selectById(uuid);
    }

    @Override
    public Boolean delScreenedWebsite(Map<String, Object> map, String userName, String password) {
        Boolean result = true;
        if (null == map.get("deleteType")) {
            List<String> uuids = (List<String>) map.get("uuids");
            screenedWebsiteDao.deleteBatchIds(uuids);
            List<String> optimizeGroupNameList = (List<String>) map.get("optimizeGroupNameList");
            for (String optimizeGroupName : optimizeGroupNameList) {
                screenedWebsiteListCacheService.screenedWebsiteListCacheEvict(optimizeGroupName);
                Boolean type = postScreenedWebsiteRequest(optimizeGroupName, userName, password);
                if (!type) {
                    result = false;
                }
            }
        } else {
            screenedWebsiteDao.deleteById(((Integer) map.get("uuid")).longValue());
            screenedWebsiteListCacheService.screenedWebsiteListCacheEvict((String) map.get("optimizeGroupName"));
            result = postScreenedWebsiteRequest((String) map.get("optimizeGroupName"), userName, password);
        }
        return result;
    }

    @Override
    @Cacheable(value = "screenedWebsiteList", key = "#optimizeGroupName")
    public String getScreenedWebsiteByOptimizeGroupName(String optimizeGroupName) {
        return screenedWebsiteDao.getScreenedWebsiteByOptimizeGroupName(optimizeGroupName);
    }

    @Override
    public Boolean postScreenedWebsiteRequest(String optimizeGroupName, String userName, String password) {
        String webPath = configService.getConfig(Constants.CONFIG_TYPE_SCREENED_WEBSITE, Constants.CONFIG_KEY_WEBPATH).getValue();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userName", userName);
        requestMap.put("password", password);
        requestMap.put("optimizeGroupName", optimizeGroupName);
        return restTemplate.postForObject(webPath + "/external/screenedWebsite/evictScreenedWebsiteCache", requestMap, Boolean.class);
    }

    @Override
    public Integer checkGroupExist(ScreenedWebsiteCriteria screenedWebsiteCriteria) {
        return screenedWebsiteDao.checkGroupExist(screenedWebsiteCriteria);
    }
}
