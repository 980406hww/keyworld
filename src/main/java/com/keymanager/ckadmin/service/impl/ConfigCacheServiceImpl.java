package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.service.ConfigCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service("configCacheService2")
public class ConfigCacheServiceImpl implements ConfigCacheService {
    private static Logger logger = LoggerFactory.getLogger(ConfigCacheServiceImpl.class);

    @Override
    @CacheEvict(value = "configList", key = "#config.configType + '' + #config.key")
    public void configCacheEvict(Config config) {
        logger.info("CacheEvict:" + config.getConfigType() + "-" + config.getKey());
    }

    @Override
    @CacheEvict(value = "configList", allEntries = true)
    public void evictAllConfigCache() {
        logger.info("CacheEvict: all config cache");
    }
}
