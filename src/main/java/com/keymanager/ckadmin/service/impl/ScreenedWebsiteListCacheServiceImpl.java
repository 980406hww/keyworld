package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.service.ScreenedWebsiteListCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service("screenedWebsiteListCacheService2")
public class ScreenedWebsiteListCacheServiceImpl implements ScreenedWebsiteListCacheService {

    private static Logger logger = LoggerFactory.getLogger(ScreenedWebsiteListCacheServiceImpl.class);

    @Override
    @CacheEvict(value = "screenedWebsiteList", key = "#optimizeGroupName")
    public void screenedWebsiteListCacheEvict(String optimizeGroupName) {
        logger.info("CacheEvict:" + optimizeGroupName);
    }

    @Override
    @CacheEvict(value = "screenedWebsiteList", allEntries = true)
    public void evictAllScreenedWebsiteListCache() {
        logger.info("CacheEvict: all screenedWebsite cache");
    }
}
