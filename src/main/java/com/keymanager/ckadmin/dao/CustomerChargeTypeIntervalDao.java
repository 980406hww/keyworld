package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.CustomerChargeTypeInterval;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerChargeTypeIntervalDao2")
public interface CustomerChargeTypeIntervalDao extends BaseMapper<CustomerChargeTypeInterval> {

    void deleteByCustomerChargeTypeUuid(@Param("customerChargeTypeUuid") Long customerChargeTypeUuid);

    List<CustomerChargeTypeInterval> searchCustomerChargeTypeIntervals(@Param("uuid") Long uuid);
}
