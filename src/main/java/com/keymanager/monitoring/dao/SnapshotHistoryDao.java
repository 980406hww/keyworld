package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.SnapshotHistoryCriteria;
import com.keymanager.monitoring.entity.SnapshotHistory;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

public interface SnapshotHistoryDao extends BaseMapper<SnapshotHistory> {

    void deleteSnapshotHistorys(@Param("snapshotHistory") SnapshotHistory snapshotHistory);

    List<SnapshotHistory> searchSnapshotHistories(@PathVariable("searchDate") Date searchDate);

    List<String> searchRelatedCustomerInfos(@PathVariable("searchDate") Date searchDate);

    List<String> searchRelatedEngineInfos(@PathVariable("searchDate") Date searchDate);
}
