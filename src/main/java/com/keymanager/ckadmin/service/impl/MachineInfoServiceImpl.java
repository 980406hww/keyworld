package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.criteria.MachineInfoBatchUpdateCriteria;
import com.keymanager.ckadmin.criteria.MachineInfoCriteria;
import com.keymanager.ckadmin.dao.MachineInfoDao;
import com.keymanager.ckadmin.entity.ClientUpgrade;
import com.keymanager.ckadmin.entity.CustomerKeywordTerminalRefreshStatRecord;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import com.keymanager.ckadmin.entity.MachineInfo;
import com.keymanager.ckadmin.service.ClientStatusRestartLogService;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.GroupSettingService;
import com.keymanager.ckadmin.service.MachineInfoService;
import com.keymanager.ckadmin.vo.ClientStatusForOptimization;
import com.keymanager.ckadmin.vo.CookieVO;
import com.keymanager.ckadmin.vo.MachineInfoGroupSummaryVO;
import com.keymanager.ckadmin.vo.MachineInfoMachineGroupSummaryVO;
import com.keymanager.ckadmin.vo.MachineInfoSummaryVO;
import com.keymanager.util.DES;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("machineInfoService2")
public class MachineInfoServiceImpl extends ServiceImpl<MachineInfoDao, MachineInfo>
        implements MachineInfoService {

    @Resource(name = "machineInfoDao2")
    private MachineInfoDao machineInfoDao;

    @Resource(name = "groupSettingService2")
    private GroupSettingService groupSettingService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Resource(name = "clientStatusRestartLogService2")
    private ClientStatusRestartLogService clientStatusRestartLogService;

    @Override
    public Integer getUpgradingMachineCount(ClientUpgrade clientUpgrade) {
        return machineInfoDao.getUpgradingMachineCount(clientUpgrade);
    }

    @Override
    public Integer getResidualMachineCount(ClientUpgrade clientUpgrade) {
        return machineInfoDao.getResidualMachineCount(clientUpgrade);
    }

    @Override
    public Page<MachineInfo> searchMachineInfos(Page<MachineInfo> page, MachineInfoCriteria machineInfoCriteria, boolean normalSearchFlag) {
        if(normalSearchFlag) {
            page.setRecords(machineInfoDao.searchMachineInfos(page, machineInfoCriteria));
        } else {
            page.setRecords(machineInfoDao.searchBadMachineInfo(page, machineInfoCriteria));
        }
        Map<String, String> passwordMap = new HashMap<>();
        for(MachineInfo machineInfo : page.getRecords()) {
            String password = passwordMap.get(machineInfo.getPassword());
            if (password == null) {
                if (StringUtil.isNullOrEmpty(machineInfo.getPassword())) {
                    password = "";
                } else if (machineInfo.getPassword().equals("doshows123")) {
                    password = "8e587919308fcab0c34af756358b9053";
                } else {
                    password = DES.vncPasswordEncode(machineInfo.getPassword());
                }
                passwordMap.put(machineInfo.getPassword(), password);
            }
            machineInfo.setPassword(password);
        }
        return page;
    }

    @Override
    public void updateMachineTargetVersion(ClientUpgrade clientUpgrade) {
        machineInfoDao.updateMachineTargetVersion(clientUpgrade);
    }

    @Override
    public void changeTerminalType(String clientID, String terminalType) {

    }

    @Override
    public void addSummaryMachineInfo(String terminalType, String clientID, String freeSpace, String version, String city) {

    }

    @Override
    public void updateMachineInfoVersion(String clientID, String version, boolean hasKeyword) {

    }

    @Override
    public void logMachineInfoTime(String terminalType, String clientID, String status, String freeSpace, String version, String city,
            int updateCount, String runningProgramType, int cpuCount, int memory) {

    }

    @Override
    public void updateMachineInfo(MachineInfo machineInfo) {

    }

    @Override
    public void updateMachineInfoForCapturePosition(String clientID) {

    }

    @Override
    public void updateMachineInfoTargetVersion(List<String> clientIDs, String targetVersion) {

    }

    @Override
    public void updateMachineInfoTargetVPSPassword(List<String> clientIDs, String targetVPSPassword) {

    }

    @Override
    public void updateRenewalDate(String clientIDs, String settingType, String renewalDate) {

    }

    @Override
    public MachineInfo getMachineInfo(String clientID, String terminalType) {
        return null;
    }

    @Override
    public void deleteMachineInfo(String clientID) {

    }

    @Override
    public void deleteAll(List<String> clientIDs) {

    }

    @Override
    public void saveMachineInfo(MachineInfo machineInfo) {
        if (null != machineInfo.getClientID()) {
            MachineInfo oldMachineInfo = machineInfoDao.selectById(machineInfo.getClientID());
            oldMachineInfo.setMachineGroup(machineInfo.getMachineGroup());
            oldMachineInfo.setAllowSwitchGroup(machineInfo.getAllowSwitchGroup());
            oldMachineInfo.setHost(machineInfo.getHost());
            oldMachineInfo.setPort(machineInfo.getPort());
            oldMachineInfo.setUserName(machineInfo.getUserName());
            oldMachineInfo.setBroadbandAccount(machineInfo.getBroadbandAccount());
            oldMachineInfo.setBroadbandPassword(machineInfo.getBroadbandPassword());
            oldMachineInfo.setVpsBackendSystemComputerID(machineInfo.getVpsBackendSystemComputerID());
            oldMachineInfo.setVpsBackendSystemPassword(machineInfo.getVpsBackendSystemPassword());
            oldMachineInfo.setSwitchGroupName(machineInfo.getSwitchGroupName());
            oldMachineInfo.setUpdateSettingTime(Utils.getCurrentTimestamp());
            machineInfoDao.updateById(oldMachineInfo);
        } else {
            supplementAdditionalValue(machineInfo);
            machineInfoDao.insert(machineInfo);
        }
    }

    private void supplementAdditionalValue(MachineInfo machineInfo){
        machineInfo.setLastVisitTime(Utils.getCurrentTimestamp());
        machineInfo.setTenMinsLastVisitTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 10));
        machineInfo.setRestartTime(Utils.getCurrentTimestamp());
        machineInfo.setThreeMinsRestartTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 3));
        machineInfo.setTenMinsRestartTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 10));
    }

    @Override
    public void resetRestartStatusForProcessing() {

    }

    @Override
    public void changeStatus(String clientID) {

    }

    @Override
    public void uploadVNCFile(InputStream inputStream, String terminalType) {

    }

    @Override
    public void reopenMachineInfo(List<String> clientIDs, String downloadProgramType) {

    }

    @Override
    public void uploadVPSFile(String machineInfoType, String downloadProgramType, File file, String terminalType) {

    }

    @Override
    public void getFullVNCFileInfo(String terminalType) {

    }

    @Override
    public void writeXMLDTD(FileOutputStream o) {

    }

    @Override
    public void writeFullTxtFile(List<MachineInfo> machineInfos) {

    }

    @Override
    public void writeTxtFile(MachineInfo machineInfo, String password) {

    }

    @Override
    public void updateUpgradeFailedReason(String clientID, String upgradeFailedReason) {

    }

    @Override
    public String checkUpgrade(String clientID) {
        return null;
    }

    @Override
    public String checkPassword(String clientID) {
        return null;
    }

    @Override
    public String updatePassword(String clientID) {
        return null;
    }

    @Override
    public void updateMachineInfoRestartStatus(String clientID, String restartStatus) {

    }

    @Override
    public void switchGroup() {

    }

    @Override
    public void sendNotificationForRenewal() {

    }

    @Override
    public MachineInfo getMachineInfoForStartUp() {
        return null;
    }

    @Override
    public String getMachineStartUpStatus(String clientID) {
        return null;
    }

    @Override
    public String getMachineInfoID(String vpsBackendSystemComputerID) {
        return null;
    }

    @Override
    public void updateMachineStartUpStatus(String clientID, String status) {

    }

    @Override
    public Integer getDownloadingMachineCount() {
        return null;
    }

    @Override
    public void updateStartUpStatusForCompleted(List<String> clientIDs) {

    }

    @Override
    public void batchUpdateMachineInfo(MachineInfoBatchUpdateCriteria machineInfoBatchUpdateCriteria) {

    }

    @Override
    public void batchChangeStatus(String clientIDs, Boolean status) {

    }

    @Override
    public void batchChangeTerminalType(String[] clientIds, String terminalType) {

    }

    @Override
    public void updateVersion(String clientID, String version) {

    }

    @Override
    public void updatePageNo(String clientID, int pageNo) {

    }

    @Override
    public List<CustomerKeywordTerminalRefreshStatRecord> searchMachineInfoForRefreshStat(
            CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        return null;
    }

    @Override
    public List<MachineInfoSummaryVO> searchMachineInfoSummaryVO(String clientIDPrefix, String city, String switchGroupName) {
        return null;
    }

    @Override
    public List<MachineInfoGroupSummaryVO> searchMachineInfoGroupSummaryVO(String group, String terminalType) {
        return null;
    }

    @Override
    public MachineInfo getStoppedMachineInfo() {
        return null;
    }

    @Override
    public void updateRemainingKeywordIndicator(List<String> groupNames, int indicator) {

    }

    @Override
    public void updateAllRemainingKeywordIndicator(int indicator) {

    }

    @Override
    public List<CookieVO> searchClientForAllotCookie(int clientCookieCount, String cookieGroupForBaidu, String cookieGroupFor360) {
        return null;
    }

    @Override
    public void resetOptimizationInfo() {

    }

    @Override
    public ClientStatusForOptimization getClientStatusForOptimization(String clientID) {
        return null;
    }

    @Override
    public void updateMachineGroup(MachineInfoCriteria machineInfoCriteria) {

    }

    @Override
    public void updateMachineGroupById(String clientID, String machineGroup) {

    }

    @Override
    public List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria) {
        return null;
    }

    @Override
    public List<MachineInfoMachineGroupSummaryVO> searchMachineInfoMachineGroupSummaryVO(String machineGroup, String terminalType) {
        return null;
    }

    @Override
    public void updateMachine(String clientID, String city, String version, String freeSpace, String runningProgramType, int cpuCount,
            int memory) {

    }
}
