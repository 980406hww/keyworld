package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.*;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import com.keymanager.ckadmin.entity.CustomerKeywordTerminalRefreshStatRecord;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import com.keymanager.ckadmin.entity.MachineInfo;
import com.keymanager.ckadmin.vo.ClientStatusForOptimization;
import com.keymanager.ckadmin.vo.CookieVO;
import com.keymanager.ckadmin.vo.MachineInfoGroupSummaryVO;
import com.keymanager.ckadmin.vo.MachineInfoMachineGroupSummaryVO;
import com.keymanager.ckadmin.vo.MachineInfoSummaryVO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface MachineInfoService extends IService<MachineInfo> {

    void changeTerminalType(String clientID, String terminalType);

    void addSummaryMachineInfo(String terminalType, String clientID, String freeSpace, String version, String city);

    void updateMachineInfoVersion(String clientID, String version, boolean hasKeyword);

    void logMachineInfoTime(String terminalType, String clientID, String status, String freeSpace, String version, String
        city, int updateCount, String runningProgramType, int cpuCount, int memory);

    Page<MachineInfo> searchMachineInfos(Page<MachineInfo> page, MachineInfoCriteria machineInfoCriteria, boolean normalSearchFlag);

    void batchUpdateUpgradeFailedReason(List<String> clientIDs, String upgradeFailedReason);

    void batchUpdateSwitchGroupName(List<String> clientIDs, String switchGroupName);

    void batchUpdateAllowSwitchGroup(List<String> clientIDs, String allowSwitchGroup);

    void updateMachineInfo(MachineInfo machineInfo);

    void updateMachineInfoForCapturePosition(String clientID);

    void updateMachineInfoTargetVersion(List<String> clientIDs, String targetVersion) throws Exception ;

    void updateMachineInfoTargetVPSPassword(List<String> clientIDs, String targetVPSPassword) throws Exception ;

    void updateRenewalDate(List<String> clientIDs, String settingType, String renewalDate) throws Exception;

    MachineInfo getMachineInfo(String clientID, String terminalType);

    void deleteMachineInfo(String clientID);

    void deleteAll(List<String> clientIDs);

    void saveMachineInfo(MachineInfo machineInfo);

    void resetRestartStatusForProcessing();

    void changeStatus(String clientID);

    void uploadVNCFile(InputStream inputStream, String terminalType);

    void reopenMachineInfo(List<String> clientIDs, String downloadProgramType);

    void uploadVPSFile(String machineInfoType, String downloadProgramType, File file, String terminalType);

    void getFullVNCFileInfo(String terminalType);

    void writeXMLDTD(FileOutputStream o);

    void writeFullTxtFile(List<MachineInfo> machineInfos);

    void writeTxtFile(MachineInfo machineInfo, String password);

    void updateUpgradeFailedReason(String clientID, String upgradeFailedReason);

    String checkUpgrade(String clientID);

    String checkPassword(String clientID);

    String updatePassword(String clientID);

    void updateMachineInfoRestartStatus(String clientID, String restartStatus);

    void switchGroup();

    void sendNotificationForRenewal();

    MachineInfo getMachineInfoForStartUp();

    String getMachineStartUpStatus(String clientID);

    String getMachineInfoID(String vpsBackendSystemComputerID);

    void updateMachineStartUpStatus(String clientID, String status);

    Integer getDownloadingMachineCount();

    void updateStartUpStatusForCompleted(List<String> clientIDs);

    void batchUpdateMachineInfo(MachineInfoBatchUpdateCriteria machineInfoBatchUpdateCriteria);

    void batchUpdateMachine(MachineInfoCriteria machineInfoCriteria);

    void batchChangeStatus(List<String> clientIDs, Boolean status);

    void batchChangeTerminalType(List<String> clientIDs, String terminalType);

    Integer getUpgradingMachineCount(ClientUpgrade clientUpgrade);

    void updateMachineTargetVersion(ClientUpgrade clientUpgrade);

    Integer getResidualMachineCount(ClientUpgrade clientUpgrade);

    void updateVersion(String clientID, String version);

    void updatePageNo(String clientID, int pageNo);

    List<CustomerKeywordTerminalRefreshStatRecord> searchMachineInfoForRefreshStat(RefreshStatisticsCriteria criteria);

    List<MachineInfoSummaryVO> searchMachineInfoSummaryVO(String clientIDPrefix, String city, String switchGroupName);

    Page<MachineInfoGroupSummaryVO> searchMachineInfoGroupSummaryVO(Page<MachineInfoGroupSummaryVO> page, MachineInfoGroupStatCriteria machineInfoGroupStatCriteria);

    MachineInfo getStoppedMachineInfo();

    void updateRemainingKeywordIndicator(List<String> groupNames, int indicator);

    void updateAllRemainingKeywordIndicator(int indicator);

    List<CookieVO> searchClientForAllotCookie(int clientCookieCount, String cookieGroupForBaidu, String cookieGroupFor360);

    void resetOptimizationInfo();

    ClientStatusForOptimization getClientStatusForOptimization(String clientID);

    void updateMachineGroup(MachineInfoCriteria machineInfoCriteria);

    void updateMachineGroupById(String clientID, String machineGroup);

    List<MachineInfoMachineGroupSummaryVO> searchMachineInfoMachineGroupSummaryVO(String machineGroup, String terminalType);

    void updateMachine(String clientID, String city, String version, String freeSpace, String runningProgramType, int cpuCount, int memory);

    List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria);

    List<Map<String, Object>> getMachineInfos();

    Map<String, Object> getMachineInfoBody(String cityName);
}
