package com.keymanager.monitoring.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class NegativeListCacheService {

    private static Logger logger = LoggerFactory.getLogger(NegativeListCacheService.class);

    @CacheEvict(value = "negativeList", key = "#keyword")
    public void negativeListCacheEvict(String keyword) {
        logger.info("CacheEvict:" + keyword);
    }

    @CacheEvict(value = "negativeList", allEntries = true)
    public void evictAllNegativeListCache() {
        logger.info("CacheEvict: all negativeList cache");
    }
}
