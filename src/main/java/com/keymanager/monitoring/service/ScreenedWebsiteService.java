package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.controller.rest.internal.ScreenedWebsiteListCacheService;
import com.keymanager.monitoring.dao.ScreenedWebsiteDao;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.ScreenedWebsite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
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

    public void saveScreenedWebsite(ScreenedWebsite screenedWebsite) {
        if (null != screenedWebsite.getUuid()) {
            screenedWebsite.setUpdateTime(new Date());
            screenedWebsiteDao.updateById(screenedWebsite);
            screenedWebsiteListCacheService.screenedWebsiteListCacheEvict(screenedWebsite.getOptimizeGroupName());
        } else {
            screenedWebsiteDao.insert(screenedWebsite);
        }
    }

    public void deleteBatchScreenedWebsite(Map requestMap) {
        List<String> uuids = (List<String>) requestMap.get("uuids");
        screenedWebsiteDao.deleteBatchIds(uuids);
        List<String> optimizeGroupNameList = (List<String>) requestMap.get("optimizeGroupNameList");
        for (String optimizeGroupName: optimizeGroupNameList) {
            screenedWebsiteListCacheService.screenedWebsiteListCacheEvict(optimizeGroupName);
        }
    }

    public ScreenedWebsite getScreenedWebsite(Long uuid){
        return screenedWebsiteDao.selectById(uuid);
    }

    public void delScreenedWebsite(Map map){
        screenedWebsiteDao.deleteById(Long.valueOf((String)map.get("uuid")));
        screenedWebsiteListCacheService.screenedWebsiteListCacheEvict((String) map.get("optimizeGroupName"));
    }

    @Cacheable(value = "screenedWebsiteList", key = "#optimizeGroupName")
    public String getScreenedWebsiteByOptimizeGroupName(String optimizeGroupName){
        return screenedWebsiteDao.getScreenedWebsiteByOptimizeGroupName(optimizeGroupName);
    }
}
