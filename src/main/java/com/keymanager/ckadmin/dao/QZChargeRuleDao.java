package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.QZChargeRule;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("qzChargeRuleDao2")
public interface QZChargeRuleDao extends BaseMapper<QZChargeRule> {

    //通过qzOperationTypeUuid查询规则表
    List<QZChargeRule> searchQZChargeRuleByqzOperationTypeUuids(
        @Param("qzOperationTypeUuid") Long qzOperationTypeUuid);

    void deleteByQZOperationTypeUuid(@Param("qzOperationTypeUuid") Long QZOperationTypeUuid);
}
