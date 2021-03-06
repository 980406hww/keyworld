package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerChargeRuleCriteria;
import com.keymanager.monitoring.entity.CustomerChargeRule;
import com.keymanager.monitoring.entity.UserInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CustomerChargeRuleDao extends BaseMapper<CustomerChargeRule> {

    List<CustomerChargeRule> searchCustomerChargeRules(Page<CustomerChargeRule> page, @Param("customerChargeRuleCriteria")CustomerChargeRuleCriteria customerChargeRuleCriteria);

    CustomerChargeRule findCustomerChargeRule(@Param("customerUuid")Integer customerUuid);

    List<CustomerChargeRule> getChargeRemindCustomer(@Param("customerChargeRuleCriteria")CustomerChargeRuleCriteria customerChargeRuleCriteria);

    void updateNextChargeDate(@Param("customerUuids")List<String> customerUuids, @Param("nextChargeDate")String nextChargeDate);

    List<CustomerChargeRule> getUpcomingCustomerChargeRule(@Param("userID")String userID);

    List<UserInfo> getCustomerChargeUser();

    Integer addUpCustomerChargeAmount(@Param("loginName")String loginName, @Param("month")String month);
}
