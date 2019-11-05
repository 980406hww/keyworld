package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.vo.QZChargeRuleStandardInfoVO;
import com.keymanager.monitoring.vo.QZChargeRuleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface QZChargeRuleDao extends BaseMapper<QZChargeRule> {

    //通过qzOperationTypeUuid查询规则表
    List<QZChargeRule> searchQZChargeRuleByqzOperationTypeUuids(@Param("qzOperationTypeUuid") Long qzOperationTypeUuid);

    //返回上一及的主键
    Long selectLastId();

    void  deleteByQZOperationTypeUuid(@Param("qzOperationTypeUuid") Long QZOperationTypeUuid);

    List<QZChargeRuleStandardInfoVO> searchQZChargeRuleStandardInfoVos (@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType);

    List<QZChargeRuleVO> findQZChargeRules (@Param("qzSettingUuid")Long qzSettingUuid, @Param("operationType")String operationType, @Param("websiteType")String websiteType);
}
