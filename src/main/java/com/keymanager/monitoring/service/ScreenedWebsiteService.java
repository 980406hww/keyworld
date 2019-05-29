package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.controller.rest.internal.ScreenedWebsiteListCacheService;
import com.keymanager.monitoring.criteria.ScreenedWebsiteCriteria;
import com.keymanager.monitoring.dao.ScreenedWebsiteDao;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.ScreenedWebsite;
import com.keymanager.util.Constants;
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
    @Autowired
    private ConfigService configService;

    public ModelAndView constructSearchScreenedWebsiteListsModelAndView(int currentPageNumber, int pageSize, ScreenedWebsiteCriteria screenedWebsiteCriteria) {
        ModelAndView modelAndView = new ModelAndView("/screenedWebsite/screenedWebsite");
        Page<ScreenedWebsite> page = new Page<>();
        page.setRecords(screenedWebsiteDao.searchCustomerKeywordListsPage(new Page<ScreenedWebsite>(currentPageNumber, pageSize), screenedWebsiteCriteria));
        modelAndView.addObject("screenedWebsiteCriteria", screenedWebsiteCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

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


    public ScreenedWebsite getScreenedWebsite(Long uuid){
        return screenedWebsiteDao.selectById(uuid);
    }

    public Boolean delScreenedWebsite(Map map, String userName, String password){
        Boolean result = true;
        if (null == map.get("deleteType")){
            List<String> uuids = (List<String>) map.get("uuids");
            screenedWebsiteDao.deleteBatchIds(uuids);
            List<String> optimizeGroupNameList = (List<String>) map.get("optimizeGroupNameList");
            for (String optimizeGroupName: optimizeGroupNameList) {
                screenedWebsiteListCacheService.screenedWebsiteListCacheEvict(optimizeGroupName);
                Boolean type = postScreenedWebsiteRequest(optimizeGroupName, userName, password);
                if (false == type){
                    result = false;
                }
            }
        }else {
            screenedWebsiteDao.deleteById(Long.valueOf((String)map.get("uuid")));
            screenedWebsiteListCacheService.screenedWebsiteListCacheEvict((String) map.get("optimizeGroupName"));
            result = postScreenedWebsiteRequest((String) map.get("optimizeGroupName"), userName, password);
        }
        return result;
    }

    @Cacheable(value = "screenedWebsiteList", key = "#optimizeGroupName")
    public String getScreenedWebsiteByOptimizeGroupName(String optimizeGroupName){
        return screenedWebsiteDao.getScreenedWebsiteByOptimizeGroupName(optimizeGroupName);
    }

    public Boolean postScreenedWebsiteRequest(String optimizeGroupName, String userName, String password){
        String webPath = configService.getConfig(Constants.CONFIG_TYPE_SCREENED_WEBSITE, Constants.CONFIG_KEY_WEBPATH).getValue();
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("utf-8")));
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("userName", userName);
        requestMap.put("password", password);
        requestMap.put("optimizeGroupName", optimizeGroupName);
        return restTemplate.postForObject(webPath + "/external/screenedWebsite/evictScreenedWebsiteCache", requestMap, Boolean.class);
    }
}
