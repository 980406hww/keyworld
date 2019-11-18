package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.CustomerChargeType;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerChargeTypeDao2")
public interface CustomerChargeTypeDao extends BaseMapper<CustomerChargeType> {

    CustomerChargeType getCustomerChargeType(@Param("customerUuid") Long customerUuid);
}
