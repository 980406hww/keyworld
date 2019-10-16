package com.keymanager.ckadmin.service;

public interface ScreenedWebsiteListCacheService {

    void screenedWebsiteListCacheEvict(String optimizeGroupName);

    void evictAllScreenedWebsiteListCache();
}
