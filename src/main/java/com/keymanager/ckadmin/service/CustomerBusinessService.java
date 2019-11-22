package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.CustomerBusiness;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lhc
 * @since 2019-09-16
 */
public interface CustomerBusinessService extends IService<CustomerBusiness> {
    void saveCustomerBusiness(Customer customer);

    void deleteByCustomerUuid(Long customerUuid);

    List<Map> getCustomerBusinessMapList(List<Long> customerUuids);

    List<String> getCustomerBusinessStrByCustomerUuid(long customerUuid);

    Map<String, Object> getCustomerBusinessCount(String loginName);
}
