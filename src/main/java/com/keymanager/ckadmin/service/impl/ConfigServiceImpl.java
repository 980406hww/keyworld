package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.dao.ConfigDao;

import com.keymanager.ckadmin.entity.Config;
import com.keymanager.ckadmin.service.ConfigCacheService;
import com.keymanager.ckadmin.service.ConfigService;

import com.keymanager.ckadmin.util.Constants;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    @Resource(name = "configCacheService2")
    private ConfigCacheService configCacheService;

    @Override
//    @Cacheable(value = "configList", key = "#configType + #key")
    public Config getConfig(String configType, String key) {
        return configDao.getConfig(configType, key);
    }

    @Override
    public List<String> getRankJobCity() {
        Config config = getConfig(Constants.CONFIG_TYPE_RANK_JOB_CITY, "RankJobCity");
        if (config != null) {
            String[] cities = config.getValue().split(",");
            Arrays.sort(cities);
            return Arrays.asList(cities);
        }
        return null;
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

    @Override
    public void refreshWebsiteCheckSign(String websiteCheckSign) {
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_WEBSITE_CHECK_SIGN);
        config.setKey("WebsiteCheck");
        config.setValue(websiteCheckSign);
        updateConfig(config);
    }

    public void updateConfig(Config config) {
        configDao.updateConfig(config);
        configCacheService.configCacheEvict(config);
    }

    @Override
    public Set<String> getNegativeKeyword() {
        List<Config> configs = findConfigs(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD);
        Set<String> keywords = new HashSet<>();
        for (Config config : configs) {
            String[] ky = config.getValue().split(",");
            keywords.addAll(Arrays.asList(ky));
        }
        return keywords;
    }

    @Override
    public List<Config> findConfigs(String configType) {
        return configDao.findConfigs(configType);
    }

    @Override
    public void updateNegativeKeywordsFromConfig(String negativeKeywords) {
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_TJ_XG);
        config.setKey(Constants.CONFIG_KEY_NEGATIVE_KEYWORDS);
        config.setValue(negativeKeywords);
        updateConfig(config);
    }

    @Override
    public void refreshCustomerNegativeKeywords(String searchEngine, String negativeKeywords) {
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_NEGATIVE_KEYWORD);
        config.setKey(searchEngine);
        config.setValue(negativeKeywords);
        updateConfig(config);
    }

    @Override
    public void refreshWebsiteWhiteList(String websiteWhiteList) {
        Config config = new Config();
        config.setConfigType(Constants.CONFIG_TYPE_WEBSITE_WHITE_LIST);
        config.setKey(Constants.CONFIG_KEY_URL);
        config.setValue(websiteWhiteList);
        updateConfig(config);
    }
}
