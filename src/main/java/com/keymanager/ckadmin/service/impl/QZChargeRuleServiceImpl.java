package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.dao.QZChargeRuleDao;
import com.keymanager.ckadmin.entity.QZChargeRule;
import com.keymanager.ckadmin.service.QZChargeRuleService;
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
}
