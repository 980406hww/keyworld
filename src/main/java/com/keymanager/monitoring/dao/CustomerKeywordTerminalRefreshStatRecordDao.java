package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.entity.CustomerKeywordTerminalRefreshStatRecord;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/11/28 17:55
 **/
public interface CustomerKeywordTerminalRefreshStatRecordDao extends BaseMapper<CustomerKeywordTerminalRefreshStatRecord> {

    List<Long> findMostDistantCustomerKeywordTerminalRefreshStatRecord ();

    List<CustomerKeywordTerminalRefreshStatRecord> getHistoryTerminalRefreshStatRecord (@Param("customerKeywordRefreshStatInfoCriteria") CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria);
}
