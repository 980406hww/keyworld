package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.QZSettingSearchChargeRuleCriteria;
import com.keymanager.monitoring.dao.QZChargeRuleDao;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.vo.QZChargeRuleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QZChargeRuleService extends ServiceImpl<QZChargeRuleDao, QZChargeRule> {
	
	@Autowired
	private QZChargeRuleDao qzChargeRuleDao;

	public List<QZChargeRule>  searchQZChargeRuleByqzOperationTypeUuids(Long qzOperationTypeUuid) {
		return qzChargeRuleDao.searchQZChargeRuleByqzOperationTypeUuids(qzOperationTypeUuid);
	}

	//通过QZOperationTypeUuid删除
	public void  deleteByQZOperationTypeUuid (Long QZOperationTypeUuid ){
		qzChargeRuleDao.deleteByQZOperationTypeUuid(QZOperationTypeUuid);
	}

    public List<QZChargeRule> searchChargeRules (QZSettingSearchChargeRuleCriteria qzSettingSearchChargeRuleCriteria) {
		return qzChargeRuleDao.searchQZChargeRules(qzSettingSearchChargeRuleCriteria);
    }

    public List<String> getAllStandardSpecies (Long qzSettingUuid) {
		return qzChargeRuleDao.getAllStandardSpecies(qzSettingUuid);
    }

	public List<QZChargeRuleVO> findQZChargeRules (Long qzSettingUuid, String operationType, String websiteType) {
		return qzChargeRuleDao.findQZChargeRules(qzSettingUuid, operationType, websiteType);
	}
}
