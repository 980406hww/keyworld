package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ClientStatusDao extends BaseMapper<ClientStatus> {
    List<ClientStatus> getClientStatusList(@Param("customerKeywordRefreshStatInfoCriteria")CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria);

    void updateClientVersion(@Param("clientID") String clientID, @Param("version")String version);

    void addSummaryClientStatus(@Param("clientStatus") ClientStatus clientStatus);

    void updateOptimizationResult(@Param("clientID") String clientID, @Param("status")String status, @Param("version")String version,
                                  @Param("freeSpace")String freeSpace, @Param("city")String city, @Param("count")int count);
}
