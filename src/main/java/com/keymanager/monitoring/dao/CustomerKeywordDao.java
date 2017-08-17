package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.entity.CustomerKeyword;
import org.apache.ibatis.annotations.Param;

public interface CustomerKeywordDao extends BaseMapper<CustomerKeyword> {
    public void clearTitleByUuids(String[] uuids);

    public void clearTitleByCustomerUuidAndTerminalType(@Param("terminalType") String terminalType, @Param("customerUuid") String customerUuid);

    public int getCustomerKeywordCount(@Param("customerUuid") long customerUuid);
}
