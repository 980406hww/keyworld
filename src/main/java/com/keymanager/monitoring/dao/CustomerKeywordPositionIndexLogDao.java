package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.entity.CustomerKeywordPositionIndexLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CustomerKeywordPositionIndexLogDao extends BaseMapper<CustomerKeywordPositionIndexLog> {

	List<CustomerKeywordPositionIndexLog> searchCustomerKeywordPositionIndexLogs(Page<CustomerKeywordPositionIndexLog> page, @Param("condition") Map<String,Object> condition);
}
