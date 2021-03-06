package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.QZSettingSearchCriteria;
import com.keymanager.ckadmin.entity.QZChargeRule;
import com.keymanager.ckadmin.vo.QZChargeRuleStandardInfoVO;
import com.keymanager.ckadmin.vo.QZChargeRuleVO;
import java.util.List;

public interface QZChargeRuleService extends IService<QZChargeRule> {

    List<QZChargeRule> searchQZChargeRuleByQZOperationTypeUuids(Long qzOperationTypeUuid);

    /**
     * 通过QZOperationTypeUuid删除
     * @param qzOperationTypeUuid
     */
    void deleteByQZOperationTypeUuid(Long qzOperationTypeUuid);

    List<QZChargeRuleVO> findQZChargeRules(long qzSettingUuid, String operationType, String websiteType);

    List<QZChargeRuleStandardInfoVO> searchChargeRules (QZSettingSearchCriteria criteria);

}
