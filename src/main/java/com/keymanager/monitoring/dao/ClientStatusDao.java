package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.value.ClientStatusGroupSummaryVO;
import com.keymanager.value.ClientStatusSummaryVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ClientStatusDao extends BaseMapper<ClientStatus> {
    List<ClientStatus> searchClientStatusForRefreshStat(@Param("customerKeywordRefreshStatInfoCriteria")CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria);

    void updateClientVersion(@Param("clientID") String clientID, @Param("version")String version);

    void addSummaryClientStatus(@Param("clientStatus") ClientStatus clientStatus);

    void updateOptimizationResult(@Param("clientID") String clientID, @Param("status")String status, @Param("version")String version,
                                  @Param("freeSpace")String freeSpace, @Param("city")String city, @Param("count")int count);
    List<ClientStatus> searchClientStatuses(Page<ClientStatus> page, @Param("clientStatusCriteria") ClientStatusCriteria clientStatusCriteria);

    List<ClientStatus> searchClientStatusesOrByHost(@Param("terminalType") String terminalType, @Param("comfirm") String comfirm);

    ClientStatus getClientStatusByClientID(@Param("clientID") String clientID, @Param("terminalType") String terminalType);

    void resetRestartStatusForProcessing();

    List<ClientStatusSummaryVO> searchClientStatusSummaryVO(Page<ClientStatusSummaryVO> page, @Param("clientIDPrefix") String clientIDPrefix, @Param("city") String city);

    List<ClientStatusGroupSummaryVO> searchClientStatusGroupSummaryVO(Page<ClientStatusGroupSummaryVO> page, @Param("group") String group, @Param("terminalType") String terminalType);
}
