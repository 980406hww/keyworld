package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.controller.rest.internal.ScreenedWebsiteListCacheService;
import com.keymanager.monitoring.dao.ScreenedWebsiteDao;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.ScreenedWebsite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScreenedWebsiteService extends ServiceImpl<ScreenedWebsiteDao, ScreenedWebsite> {

    @Autowired
    private ScreenedWebsiteDao screenedWebsiteDao;
    @Autowired
    private ScreenedWebsiteListCacheService screenedWebsiteListCacheService;

    public ModelAndView constructSearchScreenedWebsiteListsModelAndView(int currentPageNumber, int pageSize, ScreenedWebsite screenedWebsite) {
        ModelAndView modelAndView = new ModelAndView("/screenedWebsite/screenedWebsite");
        Page<ScreenedWebsite> page = new Page<>();
        page.setRecords(screenedWebsiteDao.searchCustomerKeywordListsPage(new Page<CustomerKeyword>(currentPageNumber, pageSize), screenedWebsite));
        modelAndView.addObject("screenedWebsite", screenedWebsite);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    public void saveScreenedWebsite(ScreenedWebsite screenedWebsite, String userName, String password) {
        if (null != screenedWebsite.getUuid()) {
            screenedWebsite.setUpdateTime(new Date());
            screenedWebsiteDao.updateById(screenedWebsite);
            screenedWebsiteListCacheService.screenedWebsiteListCacheEvict(screenedWebsite.getOptimizeGroupName());
            postScreenedWebsiteRequest(screenedWebsite.getOptimizeGroupName(), userName, password);
        } else {
            screenedWebsiteDao.insert(screenedWebsite);
        }
    }

    public void deleteBatchScreenedWebsite(Map requestMap, String userName, String password) {
        List<String> uuids = (List<String>) requestMap.get("uuids");
        screenedWebsiteDao.deleteBatchIds(uuids);
        List<String> optimizeGroupNameList = (List<String>) requestMap.get("optimizeGroupNameList");
        for (String optimizeGroupName: optimizeGroupNameList) {
            screenedWebsiteListCacheService.screenedWebsiteListCacheEvict(optimizeGroupName);
            postScreenedWebsiteRequest(optimizeGroupName, userName, password);
        }
    }

    public ScreenedWebsite getScreenedWebsite(Long uuid){
        return screenedWebsiteDao.selectById(uuid);
    }

    public void delScreenedWebsite(Map map, String userName, String password){
        screenedWebsiteDao.deleteById(Long.valueOf((String)map.get("uuid")));
        screenedWebsiteListCacheService.screenedWebsiteListCacheEvict((String) map.get("optimizeGroupName"));
        postScreenedWebsiteRequest((String) map.get("optimizeGroupName"), userName, password);
    }

    @Cacheable(value = "screenedWebsiteList", key = "#optimizeGroupName")
    public String getScreenedWebsiteByOptimizeGroupName(String optimizeGroupName){
        return screenedWebsiteDao.getScreenedWebsiteByOptimizeGroupName(optimizeGroupName);
    }

    public void postScreenedWebsiteRequest(String optimizeGroupName, String userName, String password){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userName", userName);
        requestMap.put("password", password);
        requestMap.put("optimizeGroupName", optimizeGroupName);
        restTemplate.postForObject("http://pcsskj.shunshikj.com/external/screenedWebsite/evictScreenedWebsiteCache", requestMap, Boolean.class);
    }
}
