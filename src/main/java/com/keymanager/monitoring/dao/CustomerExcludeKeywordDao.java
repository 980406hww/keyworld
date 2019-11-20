package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerExcludeKeyword;
import org.apache.ibatis.annotations.Param;

public interface CustomerExcludeKeywordDao extends BaseMapper<CustomerExcludeKeyword> {

    CustomerExcludeKeyword searchCustomerExcludeKeyword(@Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType);

    String getCustomerExcludeKeyword(@Param("customerUuid") Long customerUuid, @Param("qzSettingUuid") Long qzSettingUuid, @Param("terminalType") String terminalType, @Param("url") String url);

    void deleteByQZSettingUuid(@Param("qzSettingUuid") Long qzSettingUuid);
}
