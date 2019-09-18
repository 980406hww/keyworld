package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZOperationType;
import java.util.Date;
import java.util.List;

public interface QZOperationTypeService extends IService<QZOperationType> {

    Date getStandardTime(long qzSettingUuid, String terminalType);

    List<QZOperationType> searchQZOperationTypesByQZSettingUuid(Long uuid);

    List<QZOperationType> searchQZOperationTypesIsDelete(Long uuid);

    int selectLastId();

    void deleteByQZSettingUuid(Long uuid);

    List<String> getQZSettngStandardSpecie(long qzSettingUuid, String[] terminalTypes);
}
