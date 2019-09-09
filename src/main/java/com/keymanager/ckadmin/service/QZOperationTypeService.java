package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZOperationType;
import java.util.List;

public interface QZOperationTypeService extends IService<QZOperationType> {

    List<QZOperationType> searchQZOperationTypesByQZSettingUuid(Long uuid);
}
