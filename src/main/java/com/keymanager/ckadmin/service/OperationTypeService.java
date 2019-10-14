package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.OperationType;
import java.util.List;
import java.util.Map;

public interface OperationTypeService extends IService<OperationType> {

    List<OperationType> getOperationTypes(OperationType operationType, Page<OperationType> page);

    OperationType getOperationType(Long uuid);

    Boolean deleteOperationType(Map<String, Object> map);

    List getOperationTypeValues(String terminalType);

    List<String> getOperationTypeValuesByRole(String terminalType);

    void clearOperationTypeCache(String terminalType);
}
