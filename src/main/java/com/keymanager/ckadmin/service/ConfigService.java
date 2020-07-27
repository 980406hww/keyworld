package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.Config;

import java.util.*;

public interface ConfigService {

    Map<String, String> getSearchEngineMap(String terminalType);

    String[] getSearchEngines(String terminalType);

    Config getConfig(String configType, String key);

    List<String> getRankJobCity();

    void refreshWebsiteCheckSign(String websiteCheckSign);

    Set<String> getNegativeKeyword();

    void updateConfig(Config config);

    List<Config> findConfigs(String configType);

    void updateNegativeKeywordsFromConfig(String negativeKeywords);

    void refreshCustomerNegativeKeywords(String searchEngine, String negativeKeywords);

    void refreshWebsiteWhiteList(String websiteWhiteList);

    void evictAllConfigCache();

    Integer getOnceGetKeywordNum();

    List<String> getMonitorOptimizeGroupName(String configType);

}
