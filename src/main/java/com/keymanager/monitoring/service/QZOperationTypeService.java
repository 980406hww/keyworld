package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.OperationTypeDao;
import com.keymanager.monitoring.dao.QZOperationTypeDao;
import com.keymanager.monitoring.entity.OperationType;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.entity.QZOperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
			qzOperationType.setQzChargeRules(qzChargeRules);
		}
		return  qzOperationTypes;
	}

	public int selectLastId(){
		return qzOperationTypeDao.selectLastId();
	}

	public  void deleteByQZSettingUuid(Long qzSettingUuid){
		qzOperationTypeDao.deleteByQZSettingUuid(qzSettingUuid);
	}

	public void updateQZOperationTypeStandardTime (Long qzSettingUuid, String operationType, int isStandardFlag) {
		qzOperationTypeDao.updateQZOperationTypeStandardTime(qzSettingUuid, operationType, isStandardFlag);
	}

    public QZOperationType searchQZOperationTypeByQZSettingAndTerminalType(Long qzSettingUuid, String operationType) {
        return qzOperationTypeDao.searchQZOperationTypeByQZSettingAndTerminalType(qzSettingUuid, operationType);
    }

    public void updateStandardTimeByUuid(Long uuid, int updateFlag, int lastAchieve) {
        qzOperationTypeDao.updateStandardTimeByUuid(uuid, updateFlag, lastAchieve);
    }
}
