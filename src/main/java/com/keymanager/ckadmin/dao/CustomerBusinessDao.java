package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.CustomerBusiness;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-09-16
 */
@Repository("customerBusinessDao2")
public interface CustomerBusinessDao extends BaseMapper<CustomerBusiness> {

    void saveCustomerBusiness(@Param("customer") Customer customer);

    void deleteByCustomerUuid(@Param("customerUuid") Long customerUuid);

    List<Map> getCustomerBusinessMap(@Param("customerUuids") List<Long> customerUuids);

    List<String> getCustomerBusinessStrByCustomerUuid(@Param("customerUuid") Long customerUuid);
}
