package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.QZChargeRule;
import com.keymanager.ckadmin.vo.QZChargeRuleVO;
import java.util.List;

public interface QZChargeRuleService extends IService<QZChargeRule> {

    List<QZChargeRule> searchQZChargeRuleByqzOperationTypeUuids(Long qzOperationTypeUuid);

    //通过QZOperationTypeUuid删除
    void deleteByQZOperationTypeUuid(Long QZOperationTypeUuid);

    List<QZChargeRuleVO> findQZChargeRules(Long qzSettingUuid, String operationType,
        String websiteType);
}
