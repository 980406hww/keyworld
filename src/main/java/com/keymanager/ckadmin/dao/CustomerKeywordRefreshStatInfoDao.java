package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.entity.RefreshStatRecord;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerKeywordRefreshStatInfoDao2")
public interface CustomerKeywordRefreshStatInfoDao extends BaseMapper<RefreshStatRecord> {

    List<RefreshStatRecord> searchCustomerKeywordStatInfos(@Param("criteria") RefreshStatisticsCriteria criteria);

    List<RefreshStatRecord> getHistoryTerminalRefreshStatRecord(@Param("criteria") RefreshStatisticsCriteria criteria);

    void deleteOverOneWeekData();
}
