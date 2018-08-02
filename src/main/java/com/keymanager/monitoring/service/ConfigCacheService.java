package com.keymanager.monitoring.service;

import com.keymanager.monitoring.entity.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

@Service
public class ConfigCacheService {

    private static Logger logger = LoggerFactory.getLogger(ConfigCacheService.class);

    @CacheEvict(value = "configList", key = "#config.configType + '' + #config.key")
    public void configCacheEvict(Config config) {
        logger.info("CacheEvict:" + config.getConfigType() + "-" + config.getKey());
    }
}
