package com.keymanager.ckadmin.service;

import java.util.Map;

public interface ConfigInterface {

    Map<String, String> getSearchEngineMap(String terminalType);

    String[] getSearchEngines(String terminalType);
}
