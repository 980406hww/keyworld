package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.QZOperationTypeDao;
import com.keymanager.ckadmin.entity.QZChargeRule;
import com.keymanager.ckadmin.entity.QZOperationType;
import com.keymanager.ckadmin.service.QZChargeRuleService;
import com.keymanager.ckadmin.service.QZOperationTypeService;
import com.keymanager.util.common.StringUtil;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

@Service("qzOperationTypeService2")
public class QZOperationTypeServiceImpl extends ServiceImpl<QZOperationTypeDao, QZOperationType> implements QZOperationTypeService {

    @Resource(name = "qzOperationTypeDao2")
    private QZOperationTypeDao qzOperationTypeDao;

    @Resource(name = "qzChargeRuleService2")
    private QZChargeRuleService qzChargeRuleService;

    @Override
    public Date getStandardTime(long qzSettingUuid, String terminalType) {
        return qzOperationTypeDao.getStandardTime(qzSettingUuid, terminalType);
    }

    @Override
    public List<QZOperationType> searchQZOperationTypesByQZSettingUuid(long uuid) {
        return qzOperationTypeDao.searchQZOperationTypesByQZSettingUuid(uuid);
    }

    @Override
    public List<QZOperationType> searchQZOperationTypesIsDelete(long uuid) {
        List<QZOperationType> qzOperationTypes = qzOperationTypeDao.searchQZOperationTypesIsDelete(uuid);
        for (QZOperationType qzOperationType : qzOperationTypes) {
            List<QZChargeRule> qzChargeRules = qzChargeRuleService.searchQZChargeRuleByQZOperationTypeUuids(qzOperationType.getUuid());
            if (CollectionUtils.isNotEmpty(qzChargeRules)) {
                qzOperationType.setQzChargeRules(qzChargeRules);
            }
        }
        return qzOperationTypes;
    }

    @Override
    public long selectLastId() {
        return qzOperationTypeDao.selectLastId();
    }

    @Override
    public void deleteByQZSettingUuid(long qzSettingUuid) {
        qzOperationTypeDao.deleteByQZSettingUuid(qzSettingUuid);
    }

    @Override
    public List<String> getQZSettingStandardSpecie(long qzSettingUuid, String[] terminalTypes) {
        List<String> standardSpecieList = new ArrayList<>(terminalTypes.length);
        for (String terminalType : terminalTypes) {
            String standardSpecie = qzOperationTypeDao.getQZStandardSpecieAndMaxKeywordCount(qzSettingUuid, terminalType);
            if (StringUtil.isNotNullNorEmpty(standardSpecie)) {
                standardSpecieList.add(terminalType + "_" + standardSpecie);
            }
        }
        return standardSpecieList;
    }

    @Override
    public QZOperationType searchQZOperationType(long qzSettingUuid, String operationType) {
        return qzOperationTypeDao.searchQZOperationType(qzSettingUuid, operationType);
    }

    @Override
    public void updateQZOperationTypeStandardTime(long uuid, int isStandardFlag) {
        qzOperationTypeDao.updateQZOperationTypeStandardTime(uuid, isStandardFlag);
    }

    @Override
    public String findQZChargeRuleStandardSpecies(long qzSettingUuid, String terminalType) {
        return qzOperationTypeDao.getQZSettingStandardSpecie(qzSettingUuid, terminalType);
    }

    @Override
    public QZOperationType searchQZOperationTypeByQZSettingAndTerminalType(long qzSettingUuid, String operationType) {
        return qzOperationTypeDao.searchQZOperationTypeByQZSettingAndTerminalType(qzSettingUuid, operationType);
    }

    @Override
    public void updateStandardTimeByUuid(Long uuid, int updateFlag, int lastAchieve) {
        qzOperationTypeDao.updateStandardTimeByUuid(uuid, updateFlag, lastAchieve);
    }
}
