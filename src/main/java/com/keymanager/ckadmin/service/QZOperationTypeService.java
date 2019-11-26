package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZOperationType;
import java.util.Date;
import java.util.List;

public interface QZOperationTypeService extends IService<QZOperationType> {

    Date getStandardTime(long qzSettingUuid, String terminalType);

    List<QZOperationType> searchQZOperationTypesByQZSettingUuid(long uuid);

    List<QZOperationType> searchQZOperationTypesIsDelete(long uuid);

    long selectLastId();

    void deleteByQZSettingUuid(long uuid);

    List<String> getQZSettingStandardSpecie(long qzSettingUuid, String[] terminalTypes);

    QZOperationType searchQZOperationType(long qzSettingUuid, String terminalType);

    void updateQZOperationTypeStandardTime(long uuid, int isStandardFlag);

    String findQZChargeRuleStandardSpecies(long qzSettingUuid, String terminalType);

    QZOperationType searchQZOperationTypeByQZSettingAndTerminalType(long qzSettingUuid, String operationType);

    void updateStandardTimeByUuid(Long uuid, int updateFlag, int lastAchieve);
}
