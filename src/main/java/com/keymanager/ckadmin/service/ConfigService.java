package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.Config;
import java.util.List;
import java.util.Map;

public interface ConfigService {

    Map<String, String> getSearchEngineMap(String terminalType);

    String[] getSearchEngines(String terminalType);

    Config getConfig(String configType, String key);

    List<String> getRankJobCity();
}
