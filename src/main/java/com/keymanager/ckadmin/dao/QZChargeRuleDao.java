package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.QZChargeRule;
import com.keymanager.ckadmin.vo.QZChargeRuleStandardInfoVO;
import com.keymanager.ckadmin.vo.QZChargeRuleVO;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("qzChargeRuleDao2")
public interface QZChargeRuleDao extends BaseMapper<QZChargeRule> {

    /**
     * 通过qzOperationTypeUuid查询规则表
     * @param qzOperationTypeUuid
     * @return
     */
    List<QZChargeRule> searchQZChargeRuleByQZOperationTypeUuids(@Param("qzOperationTypeUuid") Long qzOperationTypeUuid);

    void deleteByQZOperationTypeUuid(@Param("qzOperationTypeUuid") Long qzOperationTypeUuid);

    List<QZChargeRuleVO> findQZChargeRules(@Param("qzSettingUuid") Long qzSettingUuid, @Param("operationType") String operationType, @Param("websiteType") String websiteType);

    List<QZChargeRuleStandardInfoVO> searchQZChargeRuleStandardInfoVos(@Param("qzSettingUuid") Long uuid, @Param("terminalType") String terminalType);
}
