package com.keymanager.ckadmin.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.ckadmin.criteria.*;
import com.keymanager.ckadmin.dao.MachineInfoDao;
import com.keymanager.ckadmin.entity.*;
import com.keymanager.ckadmin.enums.ClientStartUpStatusEnum;
import com.keymanager.ckadmin.enums.TerminalTypeEnum;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.MachineInfoService;
import com.keymanager.ckadmin.service.ProductInfoService;
import com.keymanager.ckadmin.vo.*;
import com.keymanager.util.DES;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("machineInfoService2")
public class MachineInfoServiceImpl extends ServiceImpl<MachineInfoDao, MachineInfo>
    implements MachineInfoService {

    @Resource(name = "machineInfoDao2")
    private MachineInfoDao machineInfoDao;

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource
    private ProductInfoService productInfoService;

    @Override
    public Integer getUpgradingMachineCount(ClientUpgrade clientUpgrade) {
        return machineInfoDao.getUpgradingMachineCount(clientUpgrade);
    }

    @Override
    public Integer getResidualMachineCount(ClientUpgrade clientUpgrade) {
        return machineInfoDao.getResidualMachineCount(clientUpgrade);
    }

    public void updateOptimizationResultFromCache(Collection updateOptimizedCountVOs){
        machineInfoDao.updateOptimizationResultFromCache(updateOptimizedCountVOs);
    }

    @Override
    public Page<MachineInfo> searchMachineInfos(Page<MachineInfo> page, MachineInfoCriteria machineInfoCriteria, boolean normalSearchFlag) {
        if (normalSearchFlag) {
            page.setRecords(machineInfoDao.searchMachineAndProductInfos(page, machineInfoCriteria));
        } else {
            page.setRecords(machineInfoDao.searchBadMachineInfo(page, machineInfoCriteria));
        }
        Map<String, String> passwordMap = new HashMap<>();
        for (MachineInfo machineInfo : page.getRecords()) {
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
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        if (machineInfo != null) {
            machineInfo.setTerminalType(terminalType);
            machineInfoDao.updateById(machineInfo);
        }
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
    }

    @Override
    public void updateMachineInfoForCapturePosition(String clientID) {
        machineInfoDao.updateMachineInfoForCapturePosition(clientID);
    }

    @Override
    public void updateMachineInfoTargetVersion(List<String> clientIDs, String targetVersion) throws Exception {
        machineInfoDao.updateMachineInfoTargetVersion(clientIDs, targetVersion);
    }

    @Override
    public void updateMachineInfoTargetVPSPassword(List<String> clientIDs, String targetVPSPassword) throws Exception {
        machineInfoDao.updateMachineInfoTargetVPSPassword(clientIDs, targetVPSPassword);
    }

    @Override
    public void updateRenewalDate(List<String> clientIDs, String settingType, String renewalDate) throws Exception {
        for (String clientID : clientIDs) {
            MachineInfo machineInfo = machineInfoDao.selectById(clientID);
            if ("increaseOneMonth".equals(settingType)) {
                if (machineInfo.getRenewalDate() != null) {
                    machineInfo.setRenewalDate(Utils.addMonth(machineInfo.getRenewalDate(), 1));
                } else {
                    machineInfo.setRenewalDate(Utils.addMonth(Utils.getCurrentTimestamp(), 1));
                }
            } else {
                machineInfo.setRenewalDate(Utils.string2Timestamp(renewalDate));
            }
            machineInfoDao.updateById(machineInfo);
        }
    }

    @Override
    public MachineInfo getMachineInfo(String clientID, String terminalType) {
        MachineInfo machineInfo = machineInfoDao.getMachineAndProductInfoByMachineID(clientID, terminalType);
        return machineInfo;
    }

    @Override
    public void deleteMachineInfo(String clientID) {
        machineInfoDao.deleteById(clientID);
    }

    @Override
    public void deleteAll(List<String> clientIDs) {
        machineInfoDao.deleteMachineInfos(clientIDs);
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

    private void supplementAdditionalValue(MachineInfo machineInfo) {
        machineInfo.setLastVisitTime(Utils.getCurrentTimestamp());
        machineInfo.setTenMinsLastVisitTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 10));
        machineInfo.setRestartTime(Utils.getCurrentTimestamp());
        machineInfo.setThreeMinsRestartTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 3));
        machineInfo.setTenMinsRestartTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 10));
    }

    @Override
    public void resetRestartStatusForProcessing() {
        machineInfoDao.resetRestartStatusForProcessing();
    }

    @Override
    public void changeStatus(String clientID) {
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        machineInfo.setValid(!machineInfo.getValid());
        machineInfoDao.updateById(machineInfo);
    }

    @Override
    public void uploadVNCFile(InputStream inputStream, String terminalType) {

    }

    @Override
    public void reopenMachineInfo(List<String> clientIDs, String downloadProgramType) {
        if ("New".equals(downloadProgramType)) {
            machineInfoDao.reopenMachineInfo(clientIDs, downloadProgramType, "laodu");
        } else {
            machineInfoDao.reopenMachineInfo(clientIDs, downloadProgramType, "Default");
        }
    }

    private void saveMachineInfoByVPSFile(MachineInfo machineInfo, String[] MachineInfos) throws ParseException {
        String[] vncInfos = MachineInfos[3].split(":");
        machineInfo.setClientID(MachineInfos[0]);
        ProductInfo product = productInfoService.getProductByName(MachineInfos[1]);
        if (product == null){
            ProductInfo newProduct = new ProductInfo();
            newProduct.setProductName(MachineInfos[1]);
            newProduct.setCreateDate(new Date());
            productInfoService.addProduct(newProduct);
            product = newProduct;
        }
        machineInfo.setProductId(product.getUuid());
        machineInfo.setVpsBackendSystemComputerID(MachineInfos[2]);
        machineInfo.setHost(vncInfos[0]);
        machineInfo.setPort(vncInfos[1]);
        machineInfo.setUserName(MachineInfos[4]);
        machineInfo.setPassword(MachineInfos[5]);
        machineInfo.setTargetVPSPassword(MachineInfos[5]);
        machineInfo.setBroadbandAccount(MachineInfos[6]);
        machineInfo.setBroadbandPassword(MachineInfos[7]);
        machineInfo.setClientIDPrefix(Utils.removeDigital(MachineInfos[0]));
        String date = MachineInfos[8].replace("开通时间","");
        machineInfo.setOpenDate(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date));
    }

    private void supplementDefaultValue(MachineInfo machineInfo) {
        machineInfo.setAllowSwitchGroup(0);
        supplementAdditionalValue(machineInfo);
    }

    @Override
    public void uploadVPSFile(String machineInfoType, String downloadProgramType, File file, String terminalType) throws ParseException {
        List<String> vpsInfos = FileUtil.readTxtFile(file, "UTF-8");
        for (String vpsInfo : vpsInfos) {
            String[] machineInfos = vpsInfo.split("===");
            MachineInfo existingMachineInfo = machineInfoDao.selectById(machineInfos[0]);
            if (null != existingMachineInfo) {
                saveMachineInfoByVPSFile(existingMachineInfo, machineInfos);
                if ("New".equals(downloadProgramType)) {
                    existingMachineInfo.setSwitchGroupName("laodu");
                } else {
                    existingMachineInfo.setSwitchGroupName("Default");
                }
                if (machineInfoType.equals("startUp")) {
                    existingMachineInfo.setStartUpStatus(ClientStartUpStatusEnum.New.name());
                    existingMachineInfo.setDownloadProgramType(downloadProgramType);
                } else {
                    existingMachineInfo.setStartUpStatus(ClientStartUpStatusEnum.Completed.name());
                    existingMachineInfo.setDownloadProgramType(null);
                }
                machineInfoDao.updateById(existingMachineInfo);
            } else {
                if (machineInfoType.equals("updateProduct")){
                    continue;
                }
                MachineInfo machineInfo = new MachineInfo();
                machineInfo.setTerminalType(terminalType);
                machineInfo.setFreeSpace(500.00);
                machineInfo.setValid(true);
                saveMachineInfoByVPSFile(machineInfo, machineInfos);
                if (!Character.isDigit(machineInfos[0].charAt(machineInfos[0].length() - 1))) {
                    Integer maxClientID = machineInfoDao.selectMaxIdByMachineID(machineInfos[0]);
                    maxClientID = maxClientID == null ? 1 : maxClientID + 1;
                    machineInfo.setClientID(machineInfos[0] + maxClientID);
                }
                supplementDefaultValue(machineInfo);
                if ("New".equals(downloadProgramType)) {
                    machineInfo.setSwitchGroupName("laodu");
                } else {
                    machineInfo.setSwitchGroupName("Default");
                }
                if (machineInfoType.equals("startUp")) {
                    machineInfo.setStartUpStatus(ClientStartUpStatusEnum.New.name());
                    machineInfo.setDownloadProgramType(downloadProgramType);
                } else {
                    machineInfo.setStartUpStatus(ClientStartUpStatusEnum.Completed.name());
                }
                machineInfo.setUpdateSettingTime(Utils.getCurrentTimestamp());
                machineInfoDao.insert(machineInfo);
            }
        }
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
    public void batchUpdateUpgradeFailedReason(List<String> clientIDs, String upgradeFailedReason) {
        machineInfoDao.batchUpdateUpgradeFailedReason(clientIDs, upgradeFailedReason);
    }

    @Override
    public void batchUpdateSwitchGroupName(List<String> clientIDs, String switchGroupName) {
        machineInfoDao.batchUpdateSwitchGroupName(clientIDs, switchGroupName);
    }

    @Override
    public void batchUpdateAllowSwitchGroup(List<String> clientIDs, String allowSwitchGroup) {
        machineInfoDao.batchUpdateAllowSwitchGroup(clientIDs, allowSwitchGroup);
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
        machineInfoDao.updateStartUpStatusForCompleted(clientIDs);
    }

    @Override
    public void batchUpdateMachineInfo(MachineInfoBatchUpdateCriteria machineInfoBatchUpdateCriteria) {
        String[] clientIDs = machineInfoBatchUpdateCriteria.getMachineInfo().getClientID().split(",");
        machineInfoDao.batchUpdateMachineInfo(clientIDs, machineInfoBatchUpdateCriteria.getMi(), machineInfoBatchUpdateCriteria.getMachineInfo());
    }

    @Override
    public void batchChangeStatus(List<String> clientIDs, Boolean status) {
        machineInfoDao.batchChangeStatus(clientIDs, status);
    }

    @Override
    public void batchUpdateMachine(MachineInfoCriteria machineInfoCriteria) {
        machineInfoDao.batchUpdateMachine(machineInfoCriteria);
    }

    @Override
    public void batchChangeTerminalType(List<String> clientIDs, String terminalType) {
        machineInfoDao.batchChangeTerminalType(clientIDs, terminalType);
    }

    @Override
    public void updateVersion(String clientID, String version) {

    }

    @Override
    public void updatePageNo(String clientID, int pageNo) {

    }

    @Override
    public List<CustomerKeywordTerminalRefreshStatRecord> searchMachineInfoForRefreshStat(RefreshStatisticsCriteria criteria) {
        return null;
    }

    @Override
    public List<MachineInfoSummaryVO> searchMachineInfoSummaryVO(String clientIDPrefix, String city, String switchGroupName) {
        List<MachineInfoSummaryVO> pcMachineInfoSummaryVos = machineInfoDao.searchMachineInfoSummaryVO(clientIDPrefix, city, switchGroupName);
        Collections.sort(pcMachineInfoSummaryVos);
        MachineInfoSummaryVO previousClientIDPrefix = null;
        MachineInfoSummaryVO previousType = null;
        for (MachineInfoSummaryVO machineInfoSummaryVo : pcMachineInfoSummaryVos) {
            if (previousClientIDPrefix == null) {
                previousClientIDPrefix = machineInfoSummaryVo;
                previousClientIDPrefix.setClientIDPrefixCount(previousClientIDPrefix.getClientIDPrefixCount() + 1);
                previousClientIDPrefix.setClientIDPrefixTotalCount(previousClientIDPrefix.getClientIDPrefixTotalCount() + machineInfoSummaryVo.getCount());
            } else if (previousClientIDPrefix.getClientIDPrefix().equals(machineInfoSummaryVo.getClientIDPrefix())) {
                previousClientIDPrefix.setClientIDPrefixCount(previousClientIDPrefix.getClientIDPrefixCount() + 1);
                previousClientIDPrefix.setClientIDPrefixTotalCount(previousClientIDPrefix.getClientIDPrefixTotalCount() + machineInfoSummaryVo.getCount());
            } else {
                previousClientIDPrefix = machineInfoSummaryVo;
                previousClientIDPrefix.setClientIDPrefixCount(previousClientIDPrefix.getClientIDPrefixCount() + 1);
                previousClientIDPrefix.setClientIDPrefixTotalCount(previousClientIDPrefix.getClientIDPrefixTotalCount() + machineInfoSummaryVo.getCount());
                previousType = null;
            }

            if (previousType == null) {
                previousType = machineInfoSummaryVo;
                previousType.setTypeCount(previousType.getTypeCount() + 1);
                previousType.setTypeTotalCount(previousType.getTypeTotalCount() + machineInfoSummaryVo.getCount());
            } else if (previousType.getType().equals(machineInfoSummaryVo.getType())) {
                previousType.setTypeCount(previousType.getTypeCount() + 1);
                previousType.setTypeTotalCount(previousType.getTypeTotalCount() + machineInfoSummaryVo.getCount());
            } else {
                previousType = machineInfoSummaryVo;
                previousType.setTypeCount(previousType.getTypeCount() + 1);
                previousType.setTypeTotalCount(previousType.getTypeTotalCount() + machineInfoSummaryVo.getCount());
            }
        }
        return pcMachineInfoSummaryVos;
    }

    @Override
    public List<MachineVersionVo> getMachineVersion(String terminal, String programType) {
        List<MachineVersionVo> machineVersionVos= machineInfoDao.selectMachineVersionInfo(terminal,programType);
        Collections.sort(machineVersionVos);
        MachineVersionVo versionVo=null;
        MachineVersionVo terminalVo=null;
        MachineVersionVo programTypeVo=null;
        //将终端类型版本号的总数统计出来用于显示数据以及合并表格
        for(MachineVersionVo machineVersionVo : machineVersionVos){
            if(versionVo == null){
                versionVo=machineVersionVo;
                versionVo.setVersionCount(machineVersionVo.getVersionCount()+1);
                versionVo.setVersionTotalCount(versionVo.getVersionTotalCount()+machineVersionVo.getCount());
            }else if((versionVo.getVersion() !=null && machineVersionVo.getVersion() !=null) && versionVo.getVersion().equals(machineVersionVo.getVersion())){
                versionVo.setVersionCount(versionVo.getVersionCount()+1);
                versionVo.setVersionTotalCount(versionVo.getVersionTotalCount()+machineVersionVo.getCount());
            }else if(versionVo.getVersion() == null ){
                if(machineVersionVo.getVersion() ==null){
                    versionVo.setVersionCount(versionVo.getVersionCount()+1);
                    versionVo.setVersionTotalCount(versionVo.getVersionTotalCount()+machineVersionVo.getCount());
                }else{
                    versionVo=machineVersionVo;
                    versionVo.setVersionCount(machineVersionVo.getVersionCount()+1);
                    versionVo.setVersionTotalCount(versionVo.getVersionTotalCount()+machineVersionVo.getCount());
                    terminalVo=null;
                    programTypeVo=null;
                }
            }else{
                versionVo=machineVersionVo;
                versionVo.setVersionCount(machineVersionVo.getVersionCount()+1);
                versionVo.setVersionTotalCount(versionVo.getVersionTotalCount()+machineVersionVo.getCount());
                terminalVo=null;
                programTypeVo=null;
            }
            if(terminalVo == null){
                terminalVo=machineVersionVo;
                terminalVo.setTerminalCount(machineVersionVo.getTerminalCount()+1);
                terminalVo.setTerminalTotalCount(machineVersionVo.getCount()+terminalVo.getTerminalTotalCount());
            }else if(terminalVo.getTerminal().equals(machineVersionVo.getTerminal())){
                terminalVo.setTerminalCount(terminalVo.getTerminalCount()+1);
                terminalVo.setTerminalTotalCount(terminalVo.getTerminalTotalCount()+machineVersionVo.getCount());
            }else{
                terminalVo=machineVersionVo;
                terminalVo.setTerminalCount(machineVersionVo.getTerminalCount()+1);
                terminalVo.setTerminalTotalCount(machineVersionVo.getCount()+terminalVo.getTerminalTotalCount());
                programTypeVo=null;
            }
            if(programTypeVo == null){
                programTypeVo=machineVersionVo;
                programTypeVo.setProgramTypeCount(machineVersionVo.getProgramTypeCount()+1);
                programTypeVo.setProgramTypeTotalCount(machineVersionVo.getCount()+programTypeVo.getProgramTypeCount());
            }else if((programTypeVo.getProgramType() !=null && machineVersionVo.getProgramType() !=null ) && programTypeVo.getProgramType().equals(machineVersionVo.getProgramType())){
                programTypeVo.setProgramTypeCount(programTypeVo.getProgramTypeCount()+1);
                programTypeVo.setProgramTypeTotalCount(programTypeVo.getProgramTypeTotalCount()+machineVersionVo.getCount());
            }else if(programTypeVo.getProgramType() == null){
                if(machineVersionVo.getProgramType() == null){
                    programTypeVo.setProgramTypeCount( programTypeVo.getProgramTypeCount()+1);
                    programTypeVo.setTerminalTotalCount( programTypeVo.getProgramTypeTotalCount()+machineVersionVo.getCount());
                }else{
                    programTypeVo=machineVersionVo;
                    programTypeVo.setProgramTypeCount(machineVersionVo.getProgramTypeCount()+1);
                    programTypeVo.setProgramTypeTotalCount(machineVersionVo.getCount()+programTypeVo.getProgramTypeTotalCount());
                }
            }else{
                programTypeVo=machineVersionVo;
                programTypeVo.setProgramTypeCount(machineVersionVo.getProgramTypeCount()+1);
                programTypeVo.setProgramTypeTotalCount(machineVersionVo.getCount()+programTypeVo.getProgramTypeTotalCount());
            }
        }
        return machineVersionVos;
    }


    @Override
    public Page<MachineInfoGroupSummaryVO> searchMachineInfoGroupSummaryVO(Page<MachineInfoGroupSummaryVO> page,
        MachineInfoGroupStatCriteria machineInfoGroupStatCriteria) {
        List<MachineInfoGroupSummaryVO> machineInfoGroupSummaryVOS = machineInfoDao.searchMachineInfoGroupSummaryVO(page, machineInfoGroupStatCriteria);
        page.setRecords(machineInfoGroupSummaryVOS);
        return page;
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
        machineInfoDao.updateMachineGroup(machineInfoCriteria);
    }

    @Override
    public void updateMachineGroupById(String clientID, String machineGroup) {

    }

    @Override
    public List<MachineInfoMachineGroupSummaryVO> searchMachineInfoMachineGroupSummaryVO(String machineGroup, String terminalType) {
        return null;
    }

    @Override
    public void updateMachine(String clientID, String city, String version, String freeSpace, String runningProgramType, int cpuCount,
        int memory) {
    }

    @Override
    public List<MachineGroupWorkInfo> searchMachineInfoFormMachineGroupWorkInfo(MachineGroupWorkInfoCriteria criteria) {
        return machineInfoDao.searchMachineInfoFormMachineGroupWorkInfo(criteria);
    }

    @Override
    public List<Map<String, Object>> getMachineInfos() {
        String[] strings = {"北京", "天津", "上海", "重庆", "河北", "河南", "云南", "辽宁", "黑龙江", "湖南", "安徽",
            "山东", "新疆", "江苏", "浙江", "江西", "湖北", "广西", "甘肃", "山西", "内蒙古", "陕西", "吉林", "福建", "贵州",
            "广东", "青海", "西藏", "四川", "宁夏", "海南", "台湾", "香港", "澳门", "南海诸岛"};
        List<Map<String, Object>> list = new ArrayList<>(strings.length);
        for (String str : strings) {
            list.add(getCityMap(str));
        }
        List<MachineInfoSummaryVO> machineInfos = machineInfoDao.getAllMachineInfo();
        for (MachineInfoSummaryVO machineInfo : machineInfos) {
            int index = getIndex(machineInfo.getCity(), strings);
            if (index == -1) {
                continue;
            }
            Map<String, Object> map = list.get(index);
            if (null != map.get("value")) {
                map.put("value", (int) map.get("value") + machineInfo.getCount());
            } else {
                map.put("value", machineInfo.getCount());
            }
        }
        return list;
    }

    @Override
    public Map<String, Object> getMachineInfoBody(String cityName) {
        List<MachineInfoSummaryVO> machineInfos = machineInfoDao.getMachineInfoBody(cityName);
        int unicom = 0, telecom = 0, other = 0;
        for (MachineInfoSummaryVO machineInfo : machineInfos) {
            if (null == machineInfo.getCity()) {
                other += machineInfo.getCount();
            } else if (machineInfo.getCity().contains("联通")) {
                unicom += machineInfo.getCount();
            } else if (machineInfo.getCity().contains("电信")) {
                telecom += machineInfo.getCount();
            } else {
                other += machineInfo.getCount();
            }
        }
        Map<String, Object> map = new HashMap<>(3);
        map.put("unicom", unicom);
        map.put("telecom", telecom);
        map.put("other", other);
        Map<String, Object> data = new HashMap<>(2);
        data.put("data", machineInfos);
        data.put("total", map);
        return data;
    }

    @Override
    public Map<String, String> getMachineStatusCount(String username) {
        Map<String, String> machineCountMap = machineInfoDao.getMachineStatusCount();
        if (username == null) {
            return machineCountMap;
        }
        Map<String, Object> useMachinePropMap = customerKeywordService.getUseMachineProportion(username);
        if (null == useMachinePropMap || useMachinePropMap.isEmpty()) {
            return machineCountMap;
        }
        Set<Entry<String, Object>> entries = useMachinePropMap.entrySet();
        for (Entry<String, Object> entry : entries) {
            if (entry.getKey().equals(TerminalTypeEnum.PC.name())) {
                String pcAll = machineCountMap.get("pcAll");
                double newPcAll = Double.parseDouble(pcAll) * (Double) entry.getValue();
                BigDecimal bd = new BigDecimal(newPcAll);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                machineCountMap.put("pcAll", bd.toString());
            }
            if (entry.getKey().equals(TerminalTypeEnum.Phone.name())) {
                String phoneAll = machineCountMap.get("phoneAll");
                double newPhoneAll = Double.parseDouble(phoneAll) * (Double) entry.getValue();
                BigDecimal bd = new BigDecimal(newPhoneAll);
                bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
                machineCountMap.put("phoneAll", bd.toString());
            }
        }
        return machineCountMap;
    }

    private Map<String, Object> getCityMap(String cityName) {
        Map<String, Object> map = new HashMap<>(2);
        map.put("name", cityName);
        return map;
    }

    private int getIndex(String city, String[] names) {
        if (null == city || "".equals(city)) {
            return -1;
        }
        int i = 0;
        for (String str : names) {
            if (city.contains(str)) {
                return i;
            }
            i++;
        }
        return -1;
    }


}
