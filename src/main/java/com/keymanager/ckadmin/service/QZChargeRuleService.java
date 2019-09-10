package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.entity.QZChargeRule;
import java.util.List;

public interface QZChargeRuleService {

    List<QZChargeRule> searchQZChargeRuleByqzOperationTypeUuids(Long qzOperationTypeUuid);
}
