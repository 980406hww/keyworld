package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.entity.Customer;
import java.util.List;

/**
 * <p>
 * 客户 服务实现接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
public interface CustomerService extends IService<Customer> {

    Page<Customer> searchCustomers(Page<Customer> page, CustomerCriteria customerCriteria);

    void saveCustomer(Customer customer, String loginName);

    void deleteCustomer(long uuid);

    void deleteAll(List<String> uuids);

    void updateCustomerDailyReportIdentify(List<Integer> uuids);

    void changeCustomerDailyReportIdentify(long uuid, int identify);

    Customer getCustomerWithKeywordCount(String terminalType, String entryType, long customerUuid, String loginName);

    List<Customer> getActiveCustomerSimpleInfo(CustomerCriteria customerCriteria);

    Customer getCustomer(Long customerUuid);

    Customer getCustomerByCustomerUuid(String terminalType, String businessType, Long customerUuid);

    List<Long> getActiveDailyReportIdentifyCustomerUuids(String userID);

    List<String> getActiveDailyReportIdentifyUserIDs();

    void changeSaleRemark(Long uuid, String saleRemark);

    void changeRemark(Long uuid, String remark);
}
