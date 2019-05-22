package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.QZOperationTypeDao;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.entity.QZOperationType;
import com.keymanager.monitoring.vo.QZOperationTypeVO;
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

	public List<QZOperationTypeVO> findQZOperationTypes(Long qzSettingUuid, String operationType, String group){
		return qzOperationTypeDao.findQZOperationTypes(qzSettingUuid, operationType, group);
	}
}
