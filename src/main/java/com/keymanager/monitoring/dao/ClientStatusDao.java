package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.value.ClientStatusGroupSummaryVO;
import com.keymanager.value.ClientStatusSummaryVO;
import com.sun.deploy.util.SessionState;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ClientStatusDao extends BaseMapper<ClientStatus> {
    List<ClientStatus> searchClientStatusForRefreshStat(@Param("customerKeywordRefreshStatInfoCriteria")CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria);

    void updateClientVersion(@Param("clientID") String clientID, @Param("version")String version);

    void addSummaryClientStatus(@Param("clientStatus") ClientStatus clientStatus);

    void updateOptimizationResult(@Param("clientID") String clientID, @Param("status")String status, @Param("version")String version,
                                  @Param("freeSpace")String freeSpace, @Param("city")String city, @Param("count")int count);

    List<ClientStatus> searchClientStatusesOrByHost(@Param("terminalType") String terminalType, @Param("comfirm") String comfirm);

    ClientStatus getClientStatusByClientID(@Param("clientID") String clientID, @Param("terminalType") String terminalType);

    void resetRestartStatusForProcessing();

    List<ClientStatusSummaryVO> searchClientStatusSummaryVO(@Param("clientIDPrefix") String clientIDPrefix, @Param("city") String city);

    List<ClientStatus> searchClientStatuses(Page<ClientStatus> page, @Param("clientStatusCriteria") ClientStatusCriteria clientStatusCriteria);

    List<ClientStatus> getClientStatusesForSwitchGroup(@Param("terminalType") String terminalType);

    List<ClientStatus> getClientStatusesForRenewal();

    List<ClientStatusGroupSummaryVO> searchClientStatusGroupSummaryVO(@Param("group") String group, @Param("terminalType") String terminalType);

    List<ClientStatus> searchBadClientStatus(Page<ClientStatus> page,  @Param("clientStatusCriteria") ClientStatusCriteria clientStatusCriteria);

    List<ClientStatus> searchRestartingClientStatuses();

    List<ClientStatus> searchWaitingRestartingClientStatuses();

    void updateClientStatusTargetVersion(@Param("clientIDs")List<String> clientIDs, @Param("targetVersion")String targetVersion);

    void deleteClientStatus(@Param("clientIDs")List<String> clientIDs);

    void updateRestartStatus(@Param("clientID")String clientID, @Param("restartStatus")String restartStatus);
}
