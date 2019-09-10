package com.keymanager.ckadmin.service;

import java.util.List;
import java.util.Map;

public interface OperationCombineService {

    String getOperationCombineName(String optimizeGroupName);

    List<Map<String, Object>> getOperationCombineNames(String terminalType);
}
