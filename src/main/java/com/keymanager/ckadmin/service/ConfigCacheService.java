package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.Config;
import org.springframework.stereotype.Service;

public interface ConfigCacheService {

    void configCacheEvict(Config config);

    void evictAllConfigCache();
}
