package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.SnapshotHistoryCriteria;
import com.keymanager.monitoring.entity.SnapshotHistory;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SnapshotHistoryDao extends BaseMapper<SnapshotHistory> {

    void deleteSnapshotHistorys(@Param("snapshotHistory") SnapshotHistory snapshotHistory);

    List<SnapshotHistory> searchSnapshotHistories(@Param("searchDate") Date searchDate);

    List<String> searchRelatedCustomerInfos(@Param("searchDate") Date searchDate);

    List<String> searchRelatedEngineInfos(@Param("searchDate") Date searchDate);

    List<SnapshotHistory> searchCustomerNegativeLists(@Param("snapshotHistoryCriteria") SnapshotHistoryCriteria snapshotHistoryCriteria);
}
