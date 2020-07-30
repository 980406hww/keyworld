package com.keymanager.monitoring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class ScreenedWebsiteListCacheService {

    private static Logger logger = LoggerFactory.getLogger(ScreenedWebsiteListCacheService.class);

    @CacheEvict(value = "screenedWebsiteList", key = "#optimizeGroupName")
    public void screenedWebsiteListCacheEvict(String optimizeGroupName) {
        logger.info("CacheEvict:" + optimizeGroupName);
    }

    @CacheEvict(value = "screenedWebsiteList", allEntries = true)
    public void evictAllScreenedWebsiteListCache() {
        logger.info("CacheEvict: all screenedWebsite cache");
    }
}
