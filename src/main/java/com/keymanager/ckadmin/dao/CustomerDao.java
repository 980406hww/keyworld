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

    List<Customer> searchCustomers(Page<Customer> page,
        @Param("customerCriteria") CustomerCriteria customerCriteria);

    void updateCustomerDailyReportIdentify(@Param("uuids") List<Integer> uuids);

    List<Customer> getActiveCustomerSimpleInfo(
        @Param("customerCriteria") CustomerCriteria customerCriteria);

    Long lastInsertID();

    Customer getCustomerByCustomerUuid(@Param("customerUuid") Long customerUuid);

    List<Long> getActiveDailyReportIdentifyCustomerUuids(@Param("userID") String userID);

    List<String> getActiveDailyReportIdentifyUserIDs();

    void changeSaleRemark(@Param("uuid") Long uuid, @Param("saleRemark") String SaleRemark);

    void changeRemark(@Param("uuid") Long uuid, @Param("remark") String remark);

    List<CustomerTypeVO> searchCustomerTypes(@Param("customerTypeCriteria") CustomerTypeCriteria customerTypeCriteria);
}
