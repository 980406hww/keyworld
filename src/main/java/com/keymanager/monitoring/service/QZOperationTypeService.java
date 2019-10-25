package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.QZOperationTypeDao;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.entity.QZOperationType;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class QZOperationTypeService extends ServiceImpl<QZOperationTypeDao, QZOperationType> {
	
	@Autowired
	private QZOperationTypeDao qzOperationTypeDao;

	@Autowired
	private QZChargeRuleService qzChargeRuleService;

	public List<QZOperationType> searchQZOperationTypesByQZSettingUuid(Long uuid){
		return  qzOperationTypeDao.searchQZOperationTypesByQZSettingUuid(uuid);
	}

	public List<QZOperationType> searchQZOperationTypesIsDelete(Long uuid){
		List<QZOperationType> qzOperationTypes = qzOperationTypeDao.searchQZOperationTypesIsDelete(uuid);
		for (QZOperationType qzOperationType : qzOperationTypes) {
			List<QZChargeRule> qzChargeRules = qzChargeRuleService.searchQZChargeRuleByqzOperationTypeUuids(qzOperationType.getUuid());
			if (CollectionUtils.isNotEmpty(qzChargeRules)) {
				qzOperationType.setQzChargeRules(qzChargeRules);
			}
		}
		return  qzOperationTypes;
	}

	public int selectLastId(){
		return qzOperationTypeDao.selectLastId();
	}

	public  void deleteByQZSettingUuid(Long qzSettingUuid){
		qzOperationTypeDao.deleteByQZSettingUuid(qzSettingUuid);
	}

	public void updateQZOperationTypeStandardTime (long uuid, int isStandardFlag) {
		qzOperationTypeDao.updateQZOperationTypeStandardTime(uuid, isStandardFlag);
	}

    public QZOperationType searchQZOperationTypeByQZSettingAndTerminalType(long qzSettingUuid, String operationType) {
        return qzOperationTypeDao.searchQZOperationTypeByQZSettingAndTerminalType(qzSettingUuid, operationType);
    }

    public void updateStandardTimeByUuid(Long uuid, int updateFlag, int lastAchieve) {
        qzOperationTypeDao.updateStandardTimeByUuid(uuid, updateFlag, lastAchieve);
    }

    public Date getStandardTime(long qzSettingUuid, String terminalType) {
		return qzOperationTypeDao.getStandardTime(qzSettingUuid, terminalType);
    }


	public List<String> getQZSettingStandardSpecies(Long uuid) {
		return qzOperationTypeDao.getQZSettingStandardSpecies(uuid);
	}
}
