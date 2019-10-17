package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.Config;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ConfigService {

    Map<String, String> getSearchEngineMap(String terminalType);

    String[] getSearchEngines(String terminalType);

    Config getConfig(String configType, String key);

    List<String> getRankJobCity();

    void refreshWebsiteCheckSign(String websiteCheckSign);

    Set<String> getNegativeKeyword();

    List<Config> findConfigs(String configType);

    void updateNegativeKeywordsFromConfig(String negativeKeywords);

    void refreshCustomerNegativeKeywords(String searchEngine, String negativeKeywords);

    void refreshWebsiteWhiteList(String websiteWhiteList);
}
