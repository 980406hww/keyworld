package com.keymanager.ckadmin.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.criteria.MachineInfoCriteria;
import com.keymanager.ckadmin.criteria.MachineInfoGroupStatCriteria;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import com.keymanager.ckadmin.entity.CustomerKeywordTerminalRefreshStatRecord;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import com.keymanager.ckadmin.entity.MachineInfo;
import com.keymanager.ckadmin.vo.*;

import java.util.Collection;
import java.util.Map;

import com.keymanager.monitoring.vo.UpdateOptimizedCountVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("machineInfoDao2")
public interface MachineInfoDao extends BaseMapper<MachineInfo> {

    void updateMachineInfoVersion(@Param("clientID") String clientID, @Param("version") String version, @Param("hasKeyword") boolean hasKeyword);

    void updateOptimizationResult(@Param("clientID") String clientID, @Param("status") String status, @Param("version") String version,
                                  @Param("freeSpace") String freeSpace, @Param("city") String city, @Param("count") int count, @Param("runningProgramType") String runningProgramType, @Param("cpuCount") int cpuCount, @Param("memory") int memory);

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

    void batchUpdateSwitchGroupName(@Param("clientIDs") List<String> clientIDs,@Param("switchGroupName")  String switchGroupName);

    void batchUpdateAllowSwitchGroup(@Param("clientIDs") List<String> clientIDs,@Param("allowSwitchGroup")  String allowSwitchGroup);

    Integer getDownloadingClientCount();

    Integer getUpgradingMachineCount(@Param("clientUpgrade") ClientUpgrade clientUpgrade);

    Integer getResidualMachineCount(@Param("clientUpgrade") ClientUpgrade clientUpgrade);

    void reopenMachineInfo(@Param("clientIDs") List<String> clientIDs, @Param("downloadProgramType") String downloadProgramType, @Param("switchGroupName") String switchGroupName);

    void batchUpdateUpgradeFailedReason(@Param("clientIDs") List<String> clientIDs, @Param("upgradeFailedReason") String upgradeFailedReason);

    void updateStartUpStatusForCompleted(@Param("clientIDs") List<String> clientIDs);

    void updateOptimizationResultFromCache(@Param("updateOptimizedCountVOs") Collection<UpdateOptimizedCountVO> updateOptimizedCountVOs);

    Integer selectMaxIdByMachineID(@Param("clientID") String clientID);

    void batchUpdateMachineInfo(@Param("clientIDs") String[] clientIDs, @Param("mi") MachineInfo mi, @Param("machineInfo") MachineInfo machineInfo);

    void batchUpdateMachine(@Param("machineInfoCriteria") MachineInfoCriteria machineInfoCriteria);

    void batchChangeStatus(@Param("clientIDs") List<String> clientIDs, @Param("valid") Boolean valid);

    void updateMachineTargetVersion(@Param("clientUpgrade") ClientUpgrade clientUpgrade);

    void batchChangeTerminalType(@Param("clientIDs") List<String> clientIDs, @Param("terminalType") String terminalType);

    String getMachineInfoID(@Param("vpsBackendSystemComputerID") String vpsBackendSystemComputerID);

    List<MachineInfo> getMachineInfoForVpsBackendSystemComputerID(@Param("vpsBackendSystemComputerID") String vpsBackendSystemComputerID);

    void updateMachineInfoForCapturePosition(@Param("clientID") String clientID);

    void updateVersion(@Param("clientID") String clientID, @Param("version") String version);

    List<CustomerKeywordTerminalRefreshStatRecord> searchMachineInfoForRefreshStat(@Param("criteria") RefreshStatisticsCriteria criteria);

    List<MachineInfoSummaryVO> searchMachineInfoSummaryVO(@Param("clientIDPrefix") String clientIDPrefix, @Param("city") String city, @Param("switchGroupName") String switchGroupName);

    List<MachineVersionVo> selectMachineVersionInfo(@Param("terminalType") String terminalType,@Param("programType") String programType);

    List<MachineInfoGroupSummaryVO> searchMachineInfoGroupSummaryVO(Page<MachineInfoGroupSummaryVO> page, @Param("machineInfoGroupStatCriteria") MachineInfoGroupStatCriteria machineInfoGroupStatCriteria);

    List<MachineInfo> searchRestartingMachineInfos();

    List<MachineInfo> searchWaitingRestartingMachineInfos();

    void updatePageNo(@Param("clientID") String clientID, @Param("pageNo") int pageNo);

    void updateRestartStatus(@Param("clientID") String clientID, @Param("restartStatus") String restartStatus);

    void updateRemainingKeywordIndicator(@Param("groupNames") List<String> groupNames, @Param("indicator") int indicator);

    void updateAllRemainingKeywordIndicator(@Param("indicator") int indicator);

    List<CookieVO> searchClientForAllotCookie(@Param("clientCookieCount") int clientCookieCount, @Param("cookieGroupForBaidu") String cookieGroupForBaidu, @Param("cookieGroupFor360") String cookieGroupFor360);

    void resetOptimizationInfo();

    void updateMachineGroup(@Param("machineInfoCriteria") MachineInfoCriteria machineInfoCriteria);

    List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(@Param("machineGroupWorkInfoCriteria") MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria);

    List<MachineInfoMachineGroupSummaryVO> searchMachineInfoMachineGroupSummaryVO(@Param("machineGroup") String machineGroup, @Param("terminalType") String terminalType);

    void updateMachine(@Param("clientID") String clientID, @Param("city") String city, @Param("version") String version, @Param("freeSpace") String freeSpace, @Param("runningProgramType") String runningProgramType, @Param("cpuCount") int cpuCount, @Param("memory") int memory);

    List<MachineInfoSummaryVO> getAllMachineInfo();

    List<MachineInfoSummaryVO> getMachineInfoBody(@Param("city") String city);

    Map<String,String> getMachineStatusCount();

    List<MachineInfo> searchMachineAndProductInfos(Page<MachineInfo> page, @Param("machineInfoCriteria") MachineInfoCriteria machineInfoCriteria);

    MachineInfo getMachineAndProductInfoByMachineID(@Param("clientID") String clientID, @Param("terminalType") String terminalType);
}
