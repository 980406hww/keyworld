package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.QZSettingSearchCriteria;
import com.keymanager.ckadmin.dao.QZChargeRuleDao;
import com.keymanager.ckadmin.entity.QZChargeRule;
import com.keymanager.ckadmin.service.QZChargeRuleService;
import com.keymanager.ckadmin.vo.QZChargeRuleStandardInfoVO;
import com.keymanager.ckadmin.vo.QZChargeRuleVO;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("qzChargeRuleService2")
public class QZChargeRuleServiceImpl extends ServiceImpl<QZChargeRuleDao, QZChargeRule> implements QZChargeRuleService {

    @Resource(name = "qzChargeRuleDao2")
    private QZChargeRuleDao qzChargeRuleDao;

    @Override
    public List<QZChargeRule> searchQZChargeRuleByQZOperationTypeUuids(Long qzOperationTypeUuid) {
        return qzChargeRuleDao.searchQZChargeRuleByQZOperationTypeUuids(qzOperationTypeUuid);
    }

    @Override
    public void deleteByQZOperationTypeUuid(Long qzOperationTypeUuid) {
        qzChargeRuleDao.deleteByQZOperationTypeUuid(qzOperationTypeUuid);
    }

    @Override
    public List<QZChargeRuleVO> findQZChargeRules(long qzSettingUuid, String operationType, String websiteType) {
        return qzChargeRuleDao.findQZChargeRules(qzSettingUuid, operationType, websiteType);
    }

    @Override
    public List<QZChargeRuleStandardInfoVO> searchChargeRules(QZSettingSearchCriteria criteria) {
        return qzChargeRuleDao.searchQZChargeRuleStandardInfoVos(criteria.getUuid(), criteria.getTerminalType());
    }
}
