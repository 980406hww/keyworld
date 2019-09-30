package com.keymanager.ckadmin.service;

import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.entity.RefreshStatRecord;
import java.util.List;

public interface CustomerKeywordTerminalRefreshStatRecordService {

    List<RefreshStatRecord> getHistoryTerminalRefreshStatRecord(RefreshStatisticsCriteria criteria);
}
