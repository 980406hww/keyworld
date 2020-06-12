package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.entity.RefreshStatRecord;
import java.util.List;

public interface CustomerKeywordRefreshStatInfoService {

    List<RefreshStatRecord> generateCustomerKeywordStatInfo(RefreshStatisticsCriteria criteria);

    List<RefreshStatRecord> getHistoryTerminalRefreshStatRecord(RefreshStatisticsCriteria criteria);

    void setCountCustomerKeywordRefreshStatInfo(List<RefreshStatRecord> refreshStatRecords);

    void updateCustomerKeywordStatInfo();
}
