package com.keymanager.ckadmin.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.keymanager.ckadmin.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.criteria.MachineInfoBatchUpdateCriteria;
import com.keymanager.ckadmin.criteria.MachineInfoCriteria;
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

public interface MachineInfoService extends IService<MachineInfo> {
    public void changeTerminalType(String clientID, String terminalType);

    public void addSummaryMachineInfo(String terminalType, String clientID, String freeSpace, String version, String city);

    public  void updateMachineInfoVersion(String clientID, String version, boolean hasKeyword);

    public void logMachineInfoTime(String terminalType, String clientID, String status, String freeSpace, String version, String
            city, int updateCount, String runningProgramType,int cpuCount,int memory);

    public Page<MachineInfo> searchMachineInfos(Page<MachineInfo> page, MachineInfoCriteria machineInfoCriteria, boolean normalSearchFlag);

    public void updateMachineInfo(MachineInfo machineInfo);

    public void updateMachineInfoForCapturePosition(String clientID);

    public void updateMachineInfoTargetVersion(List<String> clientIDs, String targetVersion);

    public void updateMachineInfoTargetVPSPassword(List<String> clientIDs, String targetVPSPassword);

    public void updateRenewalDate(String clientIDs,String settingType,String renewalDate);

    public MachineInfo getMachineInfo(String clientID, String terminalType);

    public void deleteMachineInfo(String clientID);

    public void deleteAll(List<String> clientIDs);

    public void saveMachineInfo(MachineInfo machineInfo);

    public void resetRestartStatusForProcessing();

    public void changeStatus(String clientID);

    public void uploadVNCFile(InputStream inputStream, String terminalType);

    public void reopenMachineInfo(List<String> clientIDs, String downloadProgramType);

    public void uploadVPSFile(String machineInfoType, String downloadProgramType, File file, String terminalType);

    public void getFullVNCFileInfo(String terminalType);

    public void writeXMLDTD(FileOutputStream o);

    public void writeFullTxtFile(List<MachineInfo> machineInfos);

    public void writeTxtFile(MachineInfo machineInfo, String password);

    public void updateUpgradeFailedReason(String clientID, String upgradeFailedReason);

    public String checkUpgrade(String clientID);

    public String checkPassword(String clientID);

    public String updatePassword(String clientID);

    public void updateMachineInfoRestartStatus(String clientID, String restartStatus);

    public void switchGroup();

    public void sendNotificationForRenewal();

    public MachineInfo getMachineInfoForStartUp();

    public String getMachineStartUpStatus(String clientID);

    public String getMachineInfoID(String vpsBackendSystemComputerID);

    public void updateMachineStartUpStatus(String clientID, String status);

    public Integer getDownloadingMachineCount();

    public void updateStartUpStatusForCompleted(List<String> clientIDs);

    public void batchUpdateMachineInfo(MachineInfoBatchUpdateCriteria machineInfoBatchUpdateCriteria);

    public void batchChangeStatus(String clientIDs,Boolean status);

    public void batchChangeTerminalType(String[] clientIds, String terminalType);

    public Integer getUpgradingMachineCount(ClientUpgrade clientUpgrade);

    public void updateMachineTargetVersion(ClientUpgrade clientUpgrade);

    public Integer getResidualMachineCount(ClientUpgrade clientUpgrade);

    public void updateVersion(String clientID, String version);

    public void updatePageNo(String clientID, int pageNo);

    public List<CustomerKeywordTerminalRefreshStatRecord> searchMachineInfoForRefreshStat(
            CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria);

    public List<MachineInfoSummaryVO> searchMachineInfoSummaryVO(String clientIDPrefix, String city, String switchGroupName);

    public List<MachineInfoGroupSummaryVO> searchMachineInfoGroupSummaryVO(String group, String terminalType);

    public MachineInfo getStoppedMachineInfo();

    public void updateRemainingKeywordIndicator(List<String> groupNames, int indicator);

    public void updateAllRemainingKeywordIndicator(int indicator);

    public List<CookieVO> searchClientForAllotCookie(int clientCookieCount, String cookieGroupForBaidu, String cookieGroupFor360);

    public void resetOptimizationInfo();

    public ClientStatusForOptimization getClientStatusForOptimization(String clientID);

    public void updateMachineGroup(MachineInfoCriteria machineInfoCriteria);

    public void updateMachineGroupById(String clientID, String machineGroup);

    public List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria);

    public List<MachineInfoMachineGroupSummaryVO> searchMachineInfoMachineGroupSummaryVO(String machineGroup, String terminalType);

    public void updateMachine(String clientID, String city, String version, String freeSpace, String runningProgramType,int cpuCount,int memory);
}
