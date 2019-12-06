package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.criteria.CustomerTypeCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.vo.CustomerTypeVO;
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

    Customer getCustomerWithKeywordCount(long customerUuid, String loginName);

    List<Customer> getActiveCustomerSimpleInfo(CustomerCriteria customerCriteria);

    Customer getCustomer(Long customerUuid);

    Customer getCustomerByCustomerUuid(String terminalType, String businessType, Long customerUuid);

    List<Long> getActiveDailyReportIdentifyCustomerUuids(String userID);

    List<String> getActiveDailyReportIdentifyUserIDs();

    void changeSaleRemark(Long uuid, String saleRemark);

    void changeRemark(Long uuid, String remark);

    void changeExternalAccount(Long uuid, String externalAccount);

    void changeSearchEngine(Long uuid, String searchEngine);

    List<CustomerTypeVO> searchCustomerTypeCount(CustomerTypeCriteria customerTypeCriteria);

    List<Customer> searchCustomersWithKeyword(List<String> groupNames, String terminalType);

    void updateCustomerUserID(List<Integer> uuids, String userID);

    List<Customer> searchTargetCustomers(String entryType, String accountName);

    Customer selectByName(String name);
    
    void changeCustomerStatus(long uuid, int status);

    List<Customer> getCustomerListByUser(String username, String type);
}
