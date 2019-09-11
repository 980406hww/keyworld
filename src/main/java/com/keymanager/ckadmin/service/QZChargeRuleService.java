package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZChargeRule;
import java.util.List;

public interface QZChargeRuleService extends IService<QZChargeRule> {

    List<QZChargeRule> searchQZChargeRuleByqzOperationTypeUuids(Long qzOperationTypeUuid);
}
