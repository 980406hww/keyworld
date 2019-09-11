package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.entity.CustomerExcludeKeyword;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerExcludeKeywordDao2")
public interface CustomerExcludeKeywordDao extends BaseMapper<CustomerExcludeKeyword> {

    CustomerExcludeKeyword searchCustomerExcludeKeyword(@Param("qzSettingUuid") Long qzSettingUuid,
        @Param("terminalType") String terminalType);

    String getCustomerExcludeKeyword(@Param("customerUuid") Long customerUuid,
        @Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType,
        @Param("url") String url);
}
