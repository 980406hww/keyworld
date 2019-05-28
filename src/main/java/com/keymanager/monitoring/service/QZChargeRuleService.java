package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.QZSettingSearchChargeRuleCriteria;
import com.keymanager.monitoring.dao.QZChargeRuleDao;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.vo.QZChargeRuleStandardInfoVO;
import com.keymanager.monitoring.vo.QZChargeRuleStandardResultVO;
import com.keymanager.monitoring.vo.QZChargeRuleVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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

    public QZChargeRuleStandardResultVO searchChargeRules (QZSettingSearchChargeRuleCriteria qzSettingSearchChargeRuleCriteria) {
	    QZChargeRuleStandardResultVO qzChargeRuleStandardResultVo = new QZChargeRuleStandardResultVO();
        List<QZChargeRuleStandardInfoVO> qzChargeRuleStandardInfoVos = qzChargeRuleDao.searchQZChargeRuleStandardInfoVos(qzSettingSearchChargeRuleCriteria);
        if (CollectionUtils.isNotEmpty(qzChargeRuleStandardInfoVos)) {
            qzChargeRuleStandardResultVo.setStandardType(qzChargeRuleStandardInfoVos.get(0).getStandardType());
            Map<String, List<QZChargeRule>> qzChargeRuleMap = new HashMap<>();
            for (QZChargeRuleStandardInfoVO qzChargeRuleStandardInfoVo : qzChargeRuleStandardInfoVos) {
                QZChargeRule qzChargeRule = new QZChargeRule();
                qzChargeRule.setStandardSpecies(qzChargeRuleStandardInfoVo.getStandardSpecies());
                qzChargeRule.setAchieveLevel(qzChargeRuleStandardInfoVo.getAchieveLevel());
                qzChargeRule.setDifferenceValue(qzChargeRuleStandardInfoVo.getDifferenceValue());
                qzChargeRule.setStartKeywordCount(qzChargeRuleStandardInfoVo.getStartKeywordCount());
                qzChargeRule.setEndKeywordCount(qzChargeRuleStandardInfoVo.getEndKeywordCount());
                qzChargeRule.setAmount(qzChargeRuleStandardInfoVo.getAmount());

                List<QZChargeRule> qzChargeRules = qzChargeRuleMap.get(qzChargeRuleStandardInfoVo.getStandardSpecies());
                if (CollectionUtils.isNotEmpty(qzChargeRules)) {
                    qzChargeRules.add(qzChargeRule);
                } else {
                    qzChargeRules = new ArrayList<>();
                    qzChargeRules.add(qzChargeRule);
                    qzChargeRuleMap.put(qzChargeRuleStandardInfoVo.getStandardSpecies(), qzChargeRules);
                }
            }
            qzChargeRuleStandardResultVo.setQzChargeRuleMap(qzChargeRuleMap);
        }
        return qzChargeRuleStandardResultVo;
    }

    public int getChargeRuleTotalPrice (QZSettingSearchChargeRuleCriteria qzSettingSearchChargeRuleCriteria) {
        List<QZChargeRuleStandardInfoVO> qzChargeRuleStandardInfoVos = qzChargeRuleDao.searchQZChargeRuleStandardInfoVos(qzSettingSearchChargeRuleCriteria);
        int totalPrice = 0;
        if (CollectionUtils.isNotEmpty(qzChargeRuleStandardInfoVos)) {
            Map<String, Integer> qzChargeRuleStandardInfoMap = new HashMap<>();
            int index = 1;
            for (QZChargeRuleStandardInfoVO qzChargeRuleStandardInfoVo : qzChargeRuleStandardInfoVos) {
                Integer price = qzChargeRuleStandardInfoMap.get(qzChargeRuleStandardInfoVo.getStandardSpecies());
                if (null != price) {
                    if (null != qzChargeRuleStandardInfoVo.getAchieveLevel() && qzChargeRuleStandardInfoVo.getAchieveLevel() > index) {
                        qzChargeRuleStandardInfoMap.put(qzChargeRuleStandardInfoVo.getStandardSpecies(),qzChargeRuleStandardInfoVo.getAmount());
                        index++;
                    } else {
                        index = 1;
                    }
                } else {
                    index = 1;
                    if (null != qzChargeRuleStandardInfoVo.getAchieveLevel() && qzChargeRuleStandardInfoVo.getAchieveLevel() > 0) {
                        price = qzChargeRuleStandardInfoVo.getAmount();
                    } else {
                        price = 0;
                    }
                    qzChargeRuleStandardInfoMap.put(qzChargeRuleStandardInfoVo.getStandardSpecies(), price);
                }
            }
            for (Integer price : qzChargeRuleStandardInfoMap.values()) {
                totalPrice += price;
            }
        }
        return totalPrice;
    }

    public List<String> getAllStandardSpecies (Long qzSettingUuid) {
		return qzChargeRuleDao.getAllStandardSpecies(qzSettingUuid);
    }

	public List<QZChargeRuleVO> findQZChargeRules (Long qzSettingUuid, String operationType, String websiteType) {
		return qzChargeRuleDao.findQZChargeRules(qzSettingUuid, operationType, websiteType);
	}
}
