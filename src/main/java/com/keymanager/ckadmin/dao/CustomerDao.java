package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.criteria.CustomerTypeCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.vo.CustomerTypeVO;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 算法测试任务表 Mapper 接口
 * </p>
 *
 * @author lhc
 * @since 2019-08-16
 */
@Repository("customerDao2")
public interface CustomerDao extends BaseMapper<Customer> {

    List<Customer> searchCustomers(Page<Customer> page, @Param("customerCriteria") CustomerCriteria customerCriteria);

    void updateCustomerDailyReportIdentify(@Param("uuids") List<Integer> uuids);

    List<Customer> getActiveCustomerSimpleInfo(@Param("customerCriteria") CustomerCriteria customerCriteria);

    Long lastInsertID();

    Customer getCustomerByCustomerUuid(@Param("customerUuid") Long customerUuid);

    List<Long> getActiveDailyReportIdentifyCustomerUuids(@Param("userID") String userID);

    List<String> getActiveDailyReportIdentifyUserIDs();

    void changeSaleRemark(@Param("uuid") Long uuid, @Param("saleRemark") String SaleRemark);

    void changeRemark(@Param("uuid") Long uuid, @Param("remark") String remark);

    void changeExternalAccount(@Param("uuid") Long uuid, @Param("externalAccount") String externalAccount);

    void changeSearchEngine(@Param("uuid") Long uuid, @Param("searchEngine") String searchEngine);

    List<CustomerTypeVO> searchCustomerTypes(@Param("customerTypeCriteria") CustomerTypeCriteria customerTypeCriteria);

    List<Customer> searchCustomerWithKeyword(@Param("groupNames") List<String> groupNames, @Param("terminalType") String terminalType);

    void saveExternalCustomer(Customer customer);

    void updateCustomerUserID(@Param("uuids") List<Integer> uuids, @Param("userID") String userID);

    List<Customer> searchTargetCustomers(@Param("entryType") String entryType, @Param("loginName") String loginName);

    Customer selectByName(@Param("name") String name);

    List<Customer> getCustomerListByUser(@Param("username") String username, @Param("type") String type);
}
