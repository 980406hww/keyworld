package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.dao.MachineInfoDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.ClientStartUpStatusEnum;
import com.keymanager.util.*;
import com.keymanager.util.common.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

@Service
public class MachineInfoService extends ServiceImpl<MachineInfoDao, MachineInfo>{

    private static Logger logger = LoggerFactory.getLogger(MachineInfoService.class);

    @Autowired
    private MachineInfoDao machineInfoDao;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ClientStatusRestartLogService clientStatusRestartLogService;

    public void changeTerminalType(String clientID, String terminalType){
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        if(machineInfo != null){
            machineInfo.setTerminalType(terminalType);
            machineInfoDao.updateById(machineInfo);
        }
    }

    public Page<MachineInfo> searchMachineInfos(Page<MachineInfo> page, MachineInfoCriteria machineInfoCriteria, boolean normalSearchFlag) {
        if(normalSearchFlag) {
            page.setRecords(machineInfoDao.searchMachineInfos(page, machineInfoCriteria));
        } else {
            page.setRecords(machineInfoDao.searchBadMachineInfo(page, machineInfoCriteria));
        }
        Map<String, String> passwordMap = new HashMap<String, String>();
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

    public void updateMachineInfo(MachineInfo machineInfo) {
        machineInfoDao.updateById(machineInfo);
    }

    public void updateMachineInfoTargetVersion(List<String> clientIDs, String targetVersion) throws Exception {
        machineInfoDao.updateMachineInfoTargetVersion(clientIDs, targetVersion);
    }

    public void updateMachineInfoTargetVPSPassword(List<String> clientIDs, String targetVPSPassword) throws Exception {
        machineInfoDao.updateMachineInfoTargetVPSPassword(clientIDs, targetVPSPassword);
    }

    public void updateRenewalDate(String clientIDs,String settingType,String renewalDate) throws Exception {
        String[] clientIDArray = clientIDs.split(",");
        for (String clientID : clientIDArray) {
            MachineInfo machineInfo = machineInfoDao.selectById(clientID);
            if("increaseOneMonth".equals(settingType)) {
                if(machineInfo.getRenewalDate() != null) {
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

    public MachineInfo getMachineInfo(String clientID, String terminalType) {
        MachineInfo machineInfo = machineInfoDao.getMachineInfoByMachineID(clientID, terminalType);
        return machineInfo;
    }

    public void deleteMachineInfo(String clientID) {
        machineInfoDao.deleteById(clientID);
    }

    public void deleteAll(List<String> clientIDs) {
        machineInfoDao.deleteMachineInfos(clientIDs);
    }

    public void saveMachineInfo(MachineInfo machineInfo) {
        if (null != machineInfo.getClientID()) {
            MachineInfo oldMachineInfo = machineInfoDao.selectById(machineInfo.getClientID());
            oldMachineInfo.setGroup(machineInfo.getGroup());
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

    public void resetRestartStatusForProcessing() {
        machineInfoDao.resetRestartStatusForProcessing();
    }

    public void changeStatus(String clientID) {
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        machineInfo.setValid(!machineInfo.getValid());
        machineInfoDao.updateById(machineInfo);
    }

    public void uploadVNCFile(InputStream inputStream, String terminalType) throws Exception {
        if(inputStream != null){
            VNCAddressBookParser vncAddressBookParser = new VNCAddressBookParser();
            Map<String, String> vncInfoMap = vncAddressBookParser.extractVNCInfo(inputStream);
            Map<String, String> passwordMap = new HashMap<String, String>();
            List<MachineInfo> machineInfos = machineInfoDao.searchMachineInfosOrByHost(terminalType,null);
            for (MachineInfo machineInfo : machineInfos) {
                String vncInfo = vncInfoMap.get(machineInfo.getClientID());
                if (!Utils.isNullOrEmpty(vncInfo)) {
                    String vncInfos[] = vncInfo.split(":");
                    if (vncInfos.length == 3) {
                        machineInfo.setHost(vncInfos[0]);
                        machineInfo.setPort(vncInfos[1]);
                        machineInfo.setVpsBackendSystemComputerID(vncInfos[2]);
                    } else {
                        System.out.println(vncInfo);
                    }
                    updateMachineInfo(machineInfo);

                    String password = passwordMap.get(machineInfo.getPassword());
                    if(password == null) {
                        if (StringUtil.isNullOrEmpty(machineInfo.getPassword())) {
                            password = "";
                        } else if (machineInfo.getPassword().equals("doshows123")) {
                            password = "8e587919308fcab0c34af756358b9053";
                        } else {
                            password = DES.vncPasswordEncode(machineInfo.getPassword());
                        }
                        passwordMap.put(machineInfo.getPassword(), password);
                    }
                    writeTxtFile(machineInfo, password);
                }
            }
        }
    }

    public void reopenMachineInfo(List<String> clientIDs, String downloadProgramType) {
        if("New".equals(downloadProgramType)){
            machineInfoDao.reopenMachineInfo(clientIDs, downloadProgramType, "laodu");
        }else{
            machineInfoDao.reopenMachineInfo(clientIDs, downloadProgramType, "Default");
        }
    }

    public void uploadVPSFile(String machineInfoType, String downloadProgramType, File file, String terminalType) throws Exception {
        List<String> vpsInfos = FileUtil.readTxtFile(file,"UTF-8");
        for (String vpsInfo : vpsInfos) {
            String[] machineInfos = vpsInfo.split("===");
            MachineInfo existingMachineInfo = machineInfoDao.selectById(machineInfos[0]);
            if(null != existingMachineInfo) {
                saveMachineInfoByVPSFile(existingMachineInfo, machineInfos);
                if("New".equals(downloadProgramType)){
                    existingMachineInfo.setSwitchGroupName("laodu");
                }else{
                    existingMachineInfo.setSwitchGroupName("Default");
                }
                if(machineInfoType.equals("startUp")) {
                    existingMachineInfo.setStartUpStatus(ClientStartUpStatusEnum.New.name());
                    existingMachineInfo.setDownloadProgramType(downloadProgramType);
                } else {
                    existingMachineInfo.setStartUpStatus(ClientStartUpStatusEnum.Completed.name());
                    existingMachineInfo.setDownloadProgramType(null);
                }
                machineInfoDao.updateById(existingMachineInfo);
            } else {
                MachineInfo machineInfo = new MachineInfo();
                machineInfo.setTerminalType(terminalType);
                machineInfo.setFreeSpace(500.00);
                machineInfo.setValid(true);
                saveMachineInfoByVPSFile(machineInfo, machineInfos);
                if(!Character.isDigit(machineInfos[0].charAt(machineInfos[0].length() - 1))) {
                    Integer maxClientID = machineInfoDao.selectMaxIdByMachineID(machineInfos[0]);
                    maxClientID = maxClientID == null ? 1 : maxClientID + 1;
                    machineInfo.setClientID(machineInfos[0] + maxClientID);
                }
                supplementDefaultValue(machineInfo);
                if("New".equals(downloadProgramType)){
                    machineInfo.setSwitchGroupName("laodu");
                }else{
                    machineInfo.setSwitchGroupName("Default");
                }
                if(machineInfoType.equals("startUp")) {
                    machineInfo.setStartUpStatus(ClientStartUpStatusEnum.New.name());
                    machineInfo.setDownloadProgramType(downloadProgramType);
                }else{
                    machineInfo.setStartUpStatus(ClientStartUpStatusEnum.Completed.name());
                }
                machineInfoDao.insert(machineInfo);
            }
        }
    }

    private void saveMachineInfoByVPSFile(MachineInfo machineInfo, String[] MachineInfos) {
        String[] vncInfos = MachineInfos[2].split(":");
        machineInfo.setClientID(MachineInfos[0]);
        machineInfo.setVpsBackendSystemComputerID(MachineInfos[1]);
        machineInfo.setHost(vncInfos[0]);
        machineInfo.setPort(vncInfos[1]);
        machineInfo.setUserName(MachineInfos[3]);
        machineInfo.setPassword(MachineInfos[4]);
        machineInfo.setTargetVPSPassword(MachineInfos[4]);
        machineInfo.setBroadbandAccount(MachineInfos[5]);
        machineInfo.setBroadbandPassword(MachineInfos[6]);
        machineInfo.setClientIDPrefix(Utils.removeDigital(MachineInfos[0]));
    }

    private void supplementDefaultValue(MachineInfo machineInfo){
        machineInfo.setAllowSwitchGroup(0);
        supplementAdditionalValue(machineInfo);
    }


    private void supplementAdditionalValue(MachineInfo machineInfo){
        machineInfo.setLastVisitTime(Utils.getCurrentTimestamp());
        machineInfo.setTenMinsLastVisitTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 10));
        machineInfo.setRestartTime(Utils.getCurrentTimestamp());
        machineInfo.setThreeMinsRestartTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 3));
        machineInfo.setTenMinsRestartTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 10));
    }

    public void getVNCFileInfo(String terminalType) throws Exception {
        List<MachineInfo> machineInfos = machineInfoDao.searchMachineInfosOrByHost(terminalType,"yes");
        if(CollectionUtils.isNotEmpty(machineInfos)) {
            Utils.removeDir(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vnc");
            Utils.createDir(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vnc/");
            Map<String, String> passwordMap = new HashMap<String, String>();
            for (MachineInfo machineInfo : machineInfos) {
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
                writeTxtFile(machineInfo, password);
            }
        }
    }

    public void writeXMLDTD(FileOutputStream o) throws Exception {
        o.write("<?xml version=\"1.0\"?>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!DOCTYPE vncaddressbook [".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ENTITY COMPANY \"RealVNC Ltd.\">".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ELEMENT vncaddressbook (folder*,file*)>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ATTLIST vncaddressbook password CDATA #IMPLIED>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ELEMENT folder (folder*,file*)>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ATTLIST folder name CDATA #REQUIRED>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ELEMENT file (section*)>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ATTLIST file name CDATA #REQUIRED>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ELEMENT section (param*)>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ATTLIST section name CDATA #REQUIRED>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ELEMENT param (#PCDATA)>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ATTLIST param name CDATA #REQUIRED>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<!ATTLIST param value CDATA #REQUIRED>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("]>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
    }

    public void writeTxtFile(MachineInfo machineInfo, String password) throws Exception {
        FileOutputStream o = null;
        String fileName = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vnc/" + machineInfo.getClientID() + ".vnc";
        o = new FileOutputStream(fileName);
        o.write("[Connection]".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write(String.format("Host=%s", machineInfo.getHost()).getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write(String.format("Port=%s", machineInfo.getPort()).getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write(String.format("Username=%s", machineInfo.getUserName()).getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write(("Password="+password).getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("[Options]".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("UseLocalCursor=1".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("UseDesktopResize=1".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("FullScreen=0".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("FullColour=0".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("LowColourLevel=1".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("PreferredEncoding=ZRLE".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("AutoSelect=1".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("Shared=0".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("SendPtrEvents=1".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("SendKeyEvents=1".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("SendCutText=1".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("AcceptCutText=1".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("Emulate3=0".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("PointerEventInterval=0".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("Monitor=".getBytes("UTF-8"));
        o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("MenuKey=F8".getBytes("UTF-8"));
        o.close();
    }

    public void updateGroup(String clientID, String groupName) {
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        machineInfo.setGroup(groupName);
        machineInfoDao.updateById(machineInfo);
    }

    public void updateUpgradeFailedReason(String clientID, String upgradeFailedReason) {
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        machineInfo.setUpgradeFailedReason(upgradeFailedReason);
        machineInfoDao.updateById(machineInfo);
    }

    public void updateStartUpStatusForCompleted(List<String> clientIDs) {
        machineInfoDao.updateStartUpStatusForCompleted(clientIDs);
    }

    public void batchUpdateMachineInfo(MachineInfoBatchUpdateCriteria machineInfoBatchUpdateCriteria) {
        String[] clientIDs = machineInfoBatchUpdateCriteria.getMachineInfo().getClientID().split(",");
        machineInfoDao.batchUpdateMachineInfo(clientIDs, machineInfoBatchUpdateCriteria.getMi(), machineInfoBatchUpdateCriteria.getMachineInfo());
    }

    public void batchChangeStatus(String clientIDs,Boolean status) {
        String[] clientIds = clientIDs.split(",");
        machineInfoDao.batchChangeStatus(clientIds,status);
    }

    public void batchChangeTerminalType(String[] clientIds, String terminalType) {
        machineInfoDao.batchChangeTerminalType(clientIds, terminalType);
    }

}
