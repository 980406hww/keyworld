package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.dao.ConfigDao;

import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.service.ConfigService;

import com.keymanager.ckadmin.util.Constants;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service("configService2")
public class ConfigServiceImpl implements ConfigService {

    @Resource(name = "configDao2")
    private ConfigDao configDao;

    @Override
//    @Cacheable(value = "configList", key = "#configType + #key")
    public Config getConfig(String configType, String key) {
        return configDao.getConfig(configType, key);
    }

    @Override
    public Map<String, String> getSearchEngineMap(String terminalType) {
        Map<String, String> searchEngineMap = new HashMap<>();
        String[] searchEngines = getSearchEngines(terminalType);
        if (null != searchEngines) {
            for (String searchEngine : searchEngines) {
                searchEngineMap.put(searchEngine, searchEngine);
            }
            return searchEngineMap;
        }
        return null;
    }

    @Override
    public String[] getSearchEngines(String terminalType) {
        Config config = getConfig(Constants.CONFIG_TYPE_SEARCH_ENGINE, terminalType);
        if (null != config) {
            String[] searchEngines = config.getValue().split(",");
            Arrays.sort(searchEngines);
            return searchEngines;
        }
        return null;
    }
}
