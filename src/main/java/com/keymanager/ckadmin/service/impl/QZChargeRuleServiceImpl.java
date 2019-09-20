package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.QZChargeRuleDao;
import com.keymanager.ckadmin.entity.QZChargeRule;
import com.keymanager.ckadmin.service.QZChargeRuleService;
import com.keymanager.ckadmin.vo.QZChargeRuleVO;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("qzChargeRuleService2")
public class QZChargeRuleServiceImpl extends ServiceImpl<QZChargeRuleDao, QZChargeRule> implements QZChargeRuleService {

    @Resource(name = "qzChargeRuleDao2")
    private QZChargeRuleDao qzChargeRuleDao;

    @Override
    public List<QZChargeRule> searchQZChargeRuleByqzOperationTypeUuids(Long qzOperationTypeUuid) {
        return qzChargeRuleDao.searchQZChargeRuleByqzOperationTypeUuids(qzOperationTypeUuid);
    }

    @Override
    //通过QZOperationTypeUuid删除
    public void deleteByQZOperationTypeUuid(Long QZOperationTypeUuid) {
        qzChargeRuleDao.deleteByQZOperationTypeUuid(QZOperationTypeUuid);
    }

    @Override
    public List<QZChargeRuleVO> findQZChargeRules(long qzSettingUuid, String operationType, String websiteType) {
        return qzChargeRuleDao.findQZChargeRules(qzSettingUuid, operationType, websiteType);
    }
}
