package com.keymanager.ckadmin.dao;

import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.entity.RefreshStatRecord;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository("customerKeywordRefreshStatInfoDao2")
public interface CustomerKeywordRefreshStatInfoDao {

    Integer selectTotalCount(@Param("criteria") RefreshStatisticsCriteria criteria);

    List<RefreshStatRecord> searchCustomerKeywordStatInfos(@Param("criteria") RefreshStatisticsCriteria criteria);
}
