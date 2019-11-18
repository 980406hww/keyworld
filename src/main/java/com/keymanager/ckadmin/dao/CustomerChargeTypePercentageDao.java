package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.CustomerChargeTypePercentage;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerChargeTypePercentageDao2")
public interface CustomerChargeTypePercentageDao extends BaseMapper<CustomerChargeTypePercentage> {

    void deleteByCustomerChargeTypeUuid(@Param("customerChargeTypeUuid") Long customerChargeTypeUuid);

    List<CustomerChargeTypePercentage> searchCustomerChargeTypePercentages(@Param("customerChargeTypeUuid") Long customerChargeTypeUuid);
}
