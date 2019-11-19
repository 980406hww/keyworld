package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.entity.CustomerKeywordPositionSummary;
import java.util.List;
import java.util.Map;

/**
 * @author shunshikj40
 */
public interface CustomerKeywordPositionSummaryService extends IService<CustomerKeywordPositionSummary> {

    Map<String, Object> getCustomerKeywordPositionSummaryData(Map<String, Object> condition);

    List<Map<String, Object>> getCKPositionSummaryDataInitTable(Map<String, Object> condition);
}
