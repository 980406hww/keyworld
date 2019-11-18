package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.CustomerChargeTypeCalculation;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerChargeTypeCalculationDao2")
public interface CustomerChargeTypeCalculationDao extends BaseMapper<CustomerChargeTypeCalculation> {

    void deleteByCustomerChargeTypeUuid(@Param("customerChargeTypeUuid") Long customerChargeTypeUuid);

    List<CustomerChargeTypeCalculation> searchCustomerChargeTypeCalculations(@Param("uuid") Long uuid);
}
