package com.keymanager.ckadmin.dao;

import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.entity.RefreshStatRecord;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerKeywordTerminalRefreshStatRecordDao2")
public interface CustomerKeywordTerminalRefreshStatRecordDao {

    List<RefreshStatRecord> getHistoryTerminalRefreshStatRecord(@Param("criteria") RefreshStatisticsCriteria criteria);
}
