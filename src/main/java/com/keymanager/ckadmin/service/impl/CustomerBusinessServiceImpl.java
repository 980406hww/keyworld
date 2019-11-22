package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.CustomerBusiness;
import com.keymanager.ckadmin.dao.CustomerBusinessDao;
import com.keymanager.ckadmin.service.CustomerBusinessService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lhc
 * @since 2019-09-16
 */
@Service("customerBusinessService2")
public class CustomerBusinessServiceImpl extends ServiceImpl<CustomerBusinessDao, CustomerBusiness> implements CustomerBusinessService {

    @Resource(name = "customerBusinessDao2")
    private CustomerBusinessDao customerBusinessDao;

    @Override
    public void saveCustomerBusiness(Customer customer) {
        customerBusinessDao.saveCustomerBusiness(customer);
    }

    @Override
    public void deleteByCustomerUuid(Long customerUuid) {
        customerBusinessDao.deleteByCustomerUuid(customerUuid);
    }

    @Override
    public List<Map> getCustomerBusinessMapList(List<Long> customerUuids) {
        return customerBusinessDao.getCustomerBusinessMap(customerUuids);
    }

    @Override
    public List<String> getCustomerBusinessStrByCustomerUuid(long customerUuid) {
        return customerBusinessDao.getCustomerBusinessStrByCustomerUuid(customerUuid);
    }

    @Override
    public Map<String, Object> getCustomerBusinessCount(String loginName) {
        return customerBusinessDao.getCustomerBusinessCount(loginName);
    }
}
