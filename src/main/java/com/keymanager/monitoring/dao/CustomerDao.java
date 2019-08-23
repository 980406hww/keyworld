package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.vo.customerSourceVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerDao extends BaseMapper<Customer> {
    List<Customer> searchCustomers(Page<Customer> page, @Param("customerCriteria")CustomerCriteria customerCriteria);

    List<Customer> getActiveCustomerSimpleInfo(@Param("customerCriteria")CustomerCriteria customerCriteria);

    int selectLastId();

    List<Customer> searchCustomerWithKeyword(@Param("groupNames") List<String> groupNames,@Param("terminalType") String terminalType);

    List<Map> searchCustomerTypes(@Param("customerCriteria")CustomerCriteria customerCriteria);

    void setCustomerKeywordStatusSwitchTime(@Param("uuids")List<String> uuids, @Param("activeHour")String activeHour, @Param("inActiveHour")String inActiveHour);

    List<Customer> searchNeedSwitchCustomer();

    List<String> searchContactPersonList(@Param("uuids")String[] uuids);

    Customer findCustomerByExternalAccountInfo(@Param("externalAccount")String externalAccount, @Param("searchEngine")String searchEngine);

    List<Customer> searchTargetCustomers(@Param("entryType")String entryType,@Param("loginName")String loginName);

    void setCustomerUpdateInterval(@Param("uuids")List<String> uuids, @Param("updateInterval")String updateInterval);

    List<customerSourceVO> findCustomerKeywordSource ();

    void updateCustomerUserID(@Param("uuids")List<String> uuids, @Param("userID")String userID);

    List<Long> getActiveDailyReportIdentifyCustomerUuids ();

    void updateCustomerDailyReportIdentify (@Param("uuids") String[] list);

    void saveExternalCustomer(Customer customer);
}
