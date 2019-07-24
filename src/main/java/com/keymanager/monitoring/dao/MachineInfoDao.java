package com.keymanager.monitoring.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.criteria.MachineInfoCriteria;
import com.keymanager.monitoring.entity.ClientUpgrade;
import com.keymanager.monitoring.entity.CustomerKeywordTerminalRefreshStatRecord;
import com.keymanager.monitoring.entity.MachineInfo;
import com.keymanager.monitoring.vo.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MachineInfoDao extends BaseMapper<MachineInfo> {

    void updateMachineInfoVersion(@Param("clientID") String clientID, @Param("version") String version, @Param("hasKeyword") boolean hasKeyword);

    void updateOptimizationResult(@Param("clientID") String clientID, @Param("status") String status, @Param("version") String version,
                                  @Param("freeSpace") String freeSpace, @Param("city") String city, @Param("count") int count, @Param("runningProgramType") String runningProgramType);

    List<MachineInfo> searchMachineInfosOrByHost(@Param("terminalType") String terminalType, @Param("comfirm") String comfirm);

    MachineInfo getMachineInfoByMachineID(@Param("clientID") String clientID, @Param("terminalType") String terminalType);

    void resetRestartStatusForProcessing();

    List<MachineInfo> searchMachineInfos(Page<MachineInfo> page, @Param("machineInfoCriteria") MachineInfoCriteria machineInfoCriteria);

    List<MachineInfo> getMachineInfosForSwitchGroup(@Param("terminalType") String terminalType);

    List<MachineInfo> getMachineInfosForRenewal();

    List<MachineInfo> searchBadMachineInfo(Page<MachineInfo> page, @Param("machineInfoCriteria") MachineInfoCriteria machineInfoCriteria);

    void updateMachineInfoTargetVersion(@Param("clientIDs") List<String> clientIDs, @Param("targetVersion") String targetVersion);

    void updateMachineInfoTargetVPSPassword(@Param("clientIDs") List<String> clientIDs, @Param("targetVPSPassword") String targetVPSPassword);

    void deleteMachineInfos(@Param("clientIDs") List<String> clientIDs);

    MachineInfo getMachineInfoForStartUp();

    Integer getDownloadingClientCount();

    Integer getUpgradingMachineCount(@Param("clientUpgrade") ClientUpgrade clientUpgrade);

    Integer getResidualMachineCount(@Param("clientUpgrade") ClientUpgrade clientUpgrade);

    void reopenMachineInfo(@Param("clientIDs") List<String> clientIDs, @Param("downloadProgramType") String downloadProgramType, @Param("switchGroupName") String switchGroupName);

    void updateStartUpStatusForCompleted(@Param("clientIDs") List<String> clientIDs);

    Integer selectMaxIdByMachineID(@Param("clientID") String clientID);

    void batchUpdateMachineInfo(@Param("clientIDs") String[] clientIDs, @Param("mi") MachineInfo mi, @Param("machineInfo") MachineInfo machineInfo);

    void batchChangeStatus(@Param("clientIds") String[] clientIds, @Param("valid") Boolean valid);

    void updateMachineTargetVersion(@Param("clientUpgrade") ClientUpgrade clientUpgrade);

    void batchChangeTerminalType(@Param("clientIds") String[] clientIds, @Param("terminalType") String terminalType);

    String getMachineInfoID(@Param("vpsBackendSystemComputerID") String vpsBackendSystemComputerID);

    void updateMachineInfoForCapturePosition(@Param("clientID") String clientID);

    void updateVersion(@Param("clientID") String clientID, @Param("version") String version);

    List<CustomerKeywordTerminalRefreshStatRecord> searchMachineInfoForRefreshStat(@Param("customerKeywordRefreshStatInfoCriteria") CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria);

    List<MachineInfoSummaryVO> searchMachineInfoSummaryVO(@Param("clientIDPrefix") String clientIDPrefix, @Param("city") String city, @Param("switchGroupName") String switchGroupName);

    List<MachineInfoGroupSummaryVO> searchMachineInfoGroupSummaryVO(@Param("group") String group, @Param("terminalType") String terminalType);

    List<MachineInfo> searchRestartingMachineInfos();

    List<MachineInfo> searchWaitingRestartingMachineInfos();

    void updatePageNo(@Param("clientID") String clientID, @Param("pageNo") int pageNo);

    void updateRestartStatus(@Param("clientID") String clientID, @Param("restartStatus") String restartStatus);

    void updateRemainingKeywordIndicator(@Param("groupNames") List<String> groupNames, @Param("indicator") int indicator);

    void updateAllRemainingKeywordIndicator(@Param("indicator") int indicator);

    List<CookieVO> searchClientForAllotCookie(@Param("clientCookieCount") int clientCookieCount, @Param("cookieGroupForBaidu") String cookieGroupForBaidu, @Param("cookieGroupFor360") String cookieGroupFor360);

    void resetOptimizationInfo();

    Integer getMachineCount(@Param("optimizeGroupName") String optimizeGroupName, @Param("terminalType") String terminalType);

    ClientStatusForOptimization getClientStatusForOptimization(@Param("clientID") String clientID);

    void batchUpdateMachineGroupByIds(@Param("clientIds") String[] clientIds, @Param("machineGroup") String machineGroup);

    void updateMachineGroup(@Param("machineInfoCriteria") MachineInfoCriteria machineInfoCriteria);
}
