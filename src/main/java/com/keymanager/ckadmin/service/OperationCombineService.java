package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.OperationCombine;
import java.util.List;
import java.util.Map;

public interface OperationCombineService extends IService<OperationCombine> {

    String getOperationCombineName(String optimizeGroupName);

    List<Map<String, Object>> getOperationCombineNames(String terminalType);
}
