package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.mail.MailHelper;
import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.dao.MachineInfoDao;
import com.keymanager.monitoring.entity.*;
import com.keymanager.monitoring.enums.ClientStartUpStatusEnum;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.vo.ClientStatusForOptimization;
import com.keymanager.monitoring.vo.CookieVO;
import com.keymanager.monitoring.vo.MachineInfoVO;
import com.keymanager.util.*;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.ClientStatusGroupSummaryVO;
import com.keymanager.value.ClientStatusSummaryVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

@Service
public class MachineInfoService extends ServiceImpl<MachineInfoDao, MachineInfo>{

    private static Logger logger = LoggerFactory.getLogger(MachineInfoService.class);

    @Autowired
    private MachineInfoDao machineInfoDao;

    @Autowired
    private CustomerKeywordService customerKeywordService;

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

    public void addSummaryMachineInfo(String terminalType, String clientID, String freeSpace, String version, String city){
        MachineInfo machineInfo = new MachineInfo();
        machineInfo.setTerminalType(terminalType);
        machineInfo.setClientID(clientID);
        machineInfo.setFreeSpace(StringUtil.isNumeric(freeSpace) ? Double.parseDouble(freeSpace) : 0);
        machineInfo.setVersion(version);
        machineInfo.setCity(city);
        machineInfo.setClientIDPrefix(Utils.removeDigital(clientID));
        supplementDefaultValue(machineInfo);
        machineInfoDao.insert(machineInfo);
    }

    public  void updateMachineVersion(String clientID, String version, boolean hasKeyword){
        machineInfoDao.updateMachineVersion(clientID, version, hasKeyword);
    }

    public void logMachineInfoTime(String terminalType, String clientID, String status, String freeSpace, String version, String
            city, int updateCount, String runningProgramType){
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        if(machineInfo == null){
            addSummaryMachineInfo(terminalType, clientID, freeSpace, version, city);
        }else{
            machineInfoDao.updateOptimizationResult(clientID, status, version, freeSpace, city, updateCount, runningProgramType);
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

    public void updateMachineInfoForCapturePosition(String clientID) {
        machineInfoDao.updateMachineInfoForCapturePosition(clientID);
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

    public void getFullVNCFileInfo(String terminalType) throws Exception {
        List<MachineInfo> machineInfos = machineInfoDao.searchMachineInfosOrByHost(terminalType,null);
        writeFullTxtFile(machineInfos);
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

    public void writeFullTxtFile(List<MachineInfo> machineInfos) throws Exception {
        FileOutputStream o = null;
        Utils.createDir(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vncAll/");
        String fileName = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vncAll/vncAll.xml";
        o = new FileOutputStream(fileName);
        writeXMLDTD(o);
        o.write("<vncaddressbook password=\"cf73063bbf04432f43bba536d4a5f96017c5798fb8acc9xf67f8acf1945d85c\">".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("<folder name=\"263互联\">".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        for (MachineInfo machineInfo : machineInfos) {
            o.write(String.format("<file name=\"%s--%s-%s--%s--%s\">",machineInfo.getClientID(),machineInfo.getHost(),machineInfo.getPort(),machineInfo.getPort(),machineInfo.getVpsBackendSystemComputerID()).getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<section name=\"Connection\">".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write(String.format("<param name=\"Host\" value=\"%s\" />",machineInfo.getHost()).getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("</section>".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<section name=\"Options\">".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"MenuKey\" value=\"F8\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"SingleSignOn\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"ShareFiles\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"Monitor\" value=\"\\\\.\\DISPLAY1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"SendSpecialKeys\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"UseAllMonitors\" value=\"0\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"FullScreenChangeResolution\" value=\"0\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"VerifyId\" value=\"2\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"AutoReconnect\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"PreferredEncoding\" value=\"ZRLE\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"Encryption\" value=\"Server\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write(String.format("<param name=\"UserName\" value=\"%s\" />",machineInfo.getUserName()).getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"Scaling\" value=\"None\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"AcceptBell\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"ProtocolVersion\" value=\"\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"Emulate3\" value=\"0\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"PointerEventInterval\" value=\"0\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"ServerCutText\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"ClientCutText\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"SendKeyEvents\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"SendPointerEvents\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"Shared\" value=\"0\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"AutoSelect\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"ColorLevel\" value=\"rgb222\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"FullColor\" value=\"0\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"RelativePtr\" value=\"0\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"FullScreen\" value=\"0\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"UseLocalCursor\" value=\"1\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("</section>".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<section name=\"Signature\">".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("<param name=\"DotVncFileSignature\" value=\"d513e6046201f276d46513a72ecc7c74889ddacd90dd83c3ce8351f5f8c54d677a46ed7ce4c3c54e065f70bb3aef7e53da8e6882886581377365ff1f84efdff8f0971970b8b25fca736cf32fdf712bd50a6490c5fdfba5c79e951ee9b9b86e96e34076d44d0df456818509f050c8323e315429dcc675e012a003b4acx9f959c6822ca2133\" />".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("</section>".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
            o.write("</file>".getBytes("UTF-8"));
            o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        }
        o.write("</folder>".getBytes("UTF-8"));
        o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
        o.write("</vncaddressbook>".getBytes("UTF-8"));
        o.close();
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

    public String checkUpgrade(String clientID){
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        if(machineInfo != null){
            if(machineInfo.getTargetVersion() != null){
                return machineInfo.getTargetVersion().equals(machineInfo.getVersion()) ? "" : machineInfo.getTargetVersion();
            }else{
                return "New".equalsIgnoreCase(machineInfo.getStartUpStatus()) ? "reopen" : "";
            }
        }
        return "0";
    }

    public String checkPassword(String clientID){
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        if(machineInfo != null){
            if(machineInfo.getTargetVPSPassword() != null){
                return machineInfo.getTargetVPSPassword().equals(machineInfo.getPassword()) ? "" : machineInfo.getTargetVPSPassword();
            }
        }
        return "0";
    }

    public String updatePassword(String clientID){
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        if(machineInfo != null){
            if(machineInfo.getTargetVPSPassword() != null){
                machineInfo.setPassword(machineInfo.getTargetVPSPassword());
                machineInfoDao.updateById(machineInfo);
                return "1";
            }
        }
        return "0";
    }

    public void updateMachineInfoRestartStatus(String clientID, String restartStatus){
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        if(machineInfo != null){
            machineInfo.setRestartTime(Utils.getCurrentTimestamp());
            machineInfo.setRestartOrderingTime(Utils.getCurrentTimestamp());
            machineInfo.setRestartCount(machineInfo.getRestartCount() + 1);
            machineInfo.setRestartStatus(restartStatus);
            machineInfoDao.updateById(machineInfo);

            ClientStatusRestartLog clientStatusRestartLog = new ClientStatusRestartLog();
            clientStatusRestartLog.setClientID(machineInfo.getClientID());
            clientStatusRestartLog.setGroup(machineInfo.getGroup());
            clientStatusRestartLog.setRestartCount(machineInfo.getRestartCount() + 1);
            clientStatusRestartLog.setRestartStatus(restartStatus);
            clientStatusRestartLogService.insert(clientStatusRestartLog);
        }
    }

    public void switchGroup() throws Exception{
        switchGroup(TerminalTypeEnum.PC.name());
        switchGroup(TerminalTypeEnum.Phone.name());
    }

    private void switchGroup(String terminalType) throws Exception{
        List<MachineInfo> machineInfos = machineInfoDao.getMachineInfosForSwitchGroup(terminalType);
        if(CollectionUtils.isNotEmpty(machineInfos)) {
            Map<String, List<MachineInfo>> machineInfoMap = new HashMap<String, List<MachineInfo>>();
            for(MachineInfo machineInfo : machineInfos){
                String key = machineInfo.getSwitchGroupName().toLowerCase();
                List<MachineInfo> tmpMachineInfos = machineInfoMap.get(key);
                if(tmpMachineInfos == null){
                    tmpMachineInfos = new ArrayList<MachineInfo>();
                    machineInfoMap.put(key, tmpMachineInfos);
                }
                tmpMachineInfos.add(machineInfo);
            }

            for(String key : machineInfoMap.keySet()){
                this.switchMachineInfos(machineInfoMap.get(key));
            }
        }
    }

    private void switchMachineInfos(List<MachineInfo> machineInfos){
        List<MachineInfo> cloneMachineInfos = new ArrayList<MachineInfo>(machineInfos);
        Collections.shuffle(machineInfos);
        Collections.shuffle(machineInfos);
        Collections.shuffle(cloneMachineInfos);
        Collections.shuffle(cloneMachineInfos);
        Collections.shuffle(cloneMachineInfos);
        for (int i = 0; i < machineInfos.size(); i++) {
            MachineInfo sourceMachineInfo = machineInfos.get(i);
            MachineInfo targetMachineInfo = cloneMachineInfos.get(i);
            switchMachineInfoInfo(sourceMachineInfo, targetMachineInfo);
        }

        for (MachineInfo machineInfo : machineInfos) {
            machineInfo.setUpdateSettingTime(Utils.getCurrentTimestamp());
            machineInfoDao.updateById(machineInfo);
        }
    }

    private void switchMachineInfoInfo(MachineInfo sourceMachineInfo, MachineInfo targetMachineInfo){
        String group = sourceMachineInfo.getGroup();
        sourceMachineInfo.setGroup(targetMachineInfo.getGroup());
        targetMachineInfo.setGroup(group);

        Timestamp idleStartTime = sourceMachineInfo.getIdleStartTime();
        sourceMachineInfo.setIdleStartTime(targetMachineInfo.getIdleStartTime());
        targetMachineInfo.setIdleStartTime(idleStartTime);

        long idleTotalMinutes = sourceMachineInfo.getIdleTotalMinutes();
        sourceMachineInfo.setIdleTotalMinutes(targetMachineInfo.getIdleTotalMinutes());
        targetMachineInfo.setIdleTotalMinutes(idleTotalMinutes);
    }

    public void sendNotificationForRenewal() throws Exception{
        String condition = " and DATE_ADD(fRenewalDate, INTERVAL -3 DAY ) < NOW() and fValid = 1 ORDER BY fRenewalDate ";
        List<MachineInfo> machineInfos = machineInfoDao.getMachineInfosForRenewal();

        if(!Utils.isEmpty(machineInfos)){
            Config notificationEmail = configService.getConfig("NotificationEmail", "EmailAddress");
            StringBuilder sb = new StringBuilder();
            sb.append("<table><tr><td>客户端ID</td><td>续费日期</td></tr>");
            for(MachineInfo machineInfo : machineInfos){
                sb.append(String.format("<tr><td>%s</td><td>%s</td>", machineInfo.getClientID(), Utils.formatDatetime(machineInfo.getRenewalDate(),
                        "yyyy-MM-dd")));
            }
            sb.append("</table>");
            String[] emailAddresses = notificationEmail.getValue().split(";");
            for(String emailAddress : emailAddresses) {
                MailHelper.sendClientDownNotification(emailAddress, sb.toString(), "续费通知");
            }
        }
    }

    public MachineInfo getMachineInfoForStartUp() {
        MachineInfo machineInfo = machineInfoDao.getMachineInfoForStartUp();
        if(machineInfo != null) {
            machineInfo.setStartUpTime(Utils.getCurrentTimestamp());
            machineInfo.setStartUpStatus(ClientStartUpStatusEnum.Processing.name());
            machineInfoDao.updateById(machineInfo);
        }
        return machineInfo;
    }

    public String getMachineStartUpStatus(String clientID) {
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        return machineInfo.getStartUpStatus();
    }

    public String getMachineInfoID(String vpsBackendSystemComputerID) {
        return machineInfoDao.getMachineInfoID(vpsBackendSystemComputerID);
    }

    public void updateMachineStartUpStatus(String clientID, String status) {
        MachineInfo machineInfo = machineInfoDao.selectById(clientID);
        if(machineInfo != null) {
            machineInfo.setStartUpStatus(status);
            machineInfoDao.updateById(machineInfo);
        }
    }

    public Integer getDownloadingMachineCount() {
        return machineInfoDao.getDownloadingClientCount();
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

    public Integer getUpgradingMachineCount(ClientUpgrade clientUpgrade) {
        return machineInfoDao.getUpgradingMachineCount(clientUpgrade);
    }

    public void updateMachineTargetVersion(ClientUpgrade clientUpgrade) {
        machineInfoDao.updateMachineTargetVersion(clientUpgrade);
    }

    public Integer getResidualMachineCount(ClientUpgrade clientUpgrade) {
        return machineInfoDao.getResidualMachineCount(clientUpgrade);
    }

    public void updateVersion(String clientID, String version){
        machineInfoDao.updateVersion(clientID, version);
    }

    public void updatePageNo(String clientID, int pageNo){
        machineInfoDao.updatePageNo(clientID, pageNo);
    }

    public List<CustomerKeywordTerminalRefreshStatRecord> searchMachineInfoForRefreshStat(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
        return machineInfoDao.searchMachineInfoForRefreshStat(customerKeywordRefreshStatInfoCriteria);
    }

    public List<ClientStatusSummaryVO> searchClientStatusSummaryVO(String clientIDPrefix, String city, String switchGroupName) throws Exception {
        List<ClientStatusSummaryVO> pcClientStatusSummaryVOs = machineInfoDao.searchClientStatusSummaryVO(clientIDPrefix, city, switchGroupName);
        Collections.sort(pcClientStatusSummaryVOs);
        ClientStatusSummaryVO previousClientIDPrefix = null;
        ClientStatusSummaryVO previousType = null;
        for(ClientStatusSummaryVO clientStatusSummaryVO : pcClientStatusSummaryVOs){
            if(previousClientIDPrefix == null){
                previousClientIDPrefix = clientStatusSummaryVO;
                previousClientIDPrefix.setClientIDPrefixCount(previousClientIDPrefix.getClientIDPrefixCount() + 1);
                previousClientIDPrefix.setClientIDPrefixTotalCount(previousClientIDPrefix.getClientIDPrefixTotalCount() +
                        clientStatusSummaryVO.getCount());
            }else if(previousClientIDPrefix.getClientIDPrefix().equals(clientStatusSummaryVO.getClientIDPrefix())){
                previousClientIDPrefix.setClientIDPrefixCount(previousClientIDPrefix.getClientIDPrefixCount() + 1);
                previousClientIDPrefix.setClientIDPrefixTotalCount(previousClientIDPrefix.getClientIDPrefixTotalCount() +
                        clientStatusSummaryVO.getCount());
            }else{
                previousClientIDPrefix = clientStatusSummaryVO;
                previousClientIDPrefix.setClientIDPrefixCount(previousClientIDPrefix.getClientIDPrefixCount() + 1);
                previousClientIDPrefix.setClientIDPrefixTotalCount(previousClientIDPrefix.getClientIDPrefixTotalCount() +
                        clientStatusSummaryVO.getCount());

                previousType = null;
            }

            if(previousType == null){
                previousType = clientStatusSummaryVO;
                previousType.setTypeCount(previousType.getTypeCount() + 1);
                previousType.setTypeTotalCount(previousType.getTypeTotalCount() +
                        clientStatusSummaryVO.getCount());
            }else if(previousType.getType().equals(clientStatusSummaryVO.getType())){
                previousType.setTypeCount(previousType.getTypeCount() + 1);
                previousType.setTypeTotalCount(previousType.getTypeTotalCount() +
                        clientStatusSummaryVO.getCount());
            }else{
                previousType = clientStatusSummaryVO;
                previousType.setTypeCount(previousType.getTypeCount() + 1);
                previousType.setTypeTotalCount(previousType.getTypeTotalCount() +
                        clientStatusSummaryVO.getCount());
            }
        }
        return pcClientStatusSummaryVOs;
    }

    public List<ClientStatusGroupSummaryVO> searchClientStatusGroupSummaryVO(String group, String terminalType) {
        return machineInfoDao.searchClientStatusGroupSummaryVO(group,terminalType);
    }

    public MachineInfo getStoppedMachineInfo(){
        MachineInfo tmpMachineInfo = null;
//        List<ClientStatus> clientStatuses = clientStatusDao.searchRestartingClientStatuses();
//        for(ClientStatus clientStatus : clientStatuses){
////			if(customerKeywordService.haveCustomerKeywordForOptimization(clientStatus.getTerminalType(), clientStatus.getClientID())){
//            tmpMachineInfo = clientStatus;
//            updateRestartStatus(clientStatus.getClientID(), "Logging");
//            break;
////			}
//        }
        if(tmpMachineInfo == null) {
            List<MachineInfo> machineInfos = machineInfoDao.searchWaitingRestartingMachineInfos();
            for (MachineInfo machineInfo : machineInfos) {
//				if (customerKeywordService.haveCustomerKeywordForOptimization(machineInfo.getTerminalType(), machineInfo.getClientID())) {
                tmpMachineInfo = machineInfo;
                updateRestartStatus(machineInfo.getClientID(), "Processing");
                String vpsServiceProvideName = this.detectVPSServiceProvider(machineInfo.getVpsBackendSystemComputerID());
                Config vpsBackendAccount = configService.getConfig(Constants.CONFIG_TYPE_VPS_BACKEND_ACCOUNT, vpsServiceProvideName);
                if(vpsBackendAccount != null){
                    machineInfo.setUserName(vpsBackendAccount.getValue());
                }
                Config vpsBackendPassword = configService.getConfig(Constants.CONFIG_TYPE_VPS_BACKEND_PASSWORD, vpsServiceProvideName);
                if(vpsBackendPassword != null){
                    machineInfo.setPassword(vpsBackendPassword.getValue());
                }
                break;
//				}
            }
        }
        return tmpMachineInfo;
    }

    private String detectVPSServiceProvider(String backendComputerID){
        backendComputerID = backendComputerID.toLowerCase();
        if(backendComputerID.matches("^[0-9]*$")){
            return "nuobin";
        }else if(backendComputerID.indexOf("k") == 0){
            return "yongtian";
        }else if(backendComputerID.indexOf("y") == 0){
            return "yiyang";
        }else{
            return "263";
        }
    }

    private void updateRestartStatus(String clientID, String restartStatus){
        machineInfoDao.updateRestartStatus(clientID, restartStatus);
    }

    public void updateRemainingKeywordIndicator(List<String> groupNames, int indicator){
        machineInfoDao.updateRemainingKeywordIndicator(groupNames, indicator);
    }

    public void updateAllRemainingKeywordIndicator(int indicator){
        machineInfoDao.updateAllRemainingKeywordIndicator(indicator);
    }

    public MachineInfo getClientStatusForStartUp() {
        MachineInfo machineInfo = machineInfoDao.getMachineInfoForStartUp();
        if (machineInfo != null) {
            machineInfo.setStartUpTime(Utils.getCurrentTimestamp());
            machineInfo.setStartUpStatus(ClientStartUpStatusEnum.Processing.name());
            machineInfoDao.updateById(machineInfo);
        }
        return machineInfo;
    }

    public List<CookieVO> searchClientForAllotCookie(int clientCookieCount, String cookieGroupForBaidu, String cookieGroupFor360) {
        List<CookieVO> clientCookieCountList = machineInfoDao.searchClientForAllotCookie(clientCookieCount, cookieGroupForBaidu, cookieGroupFor360);
        return clientCookieCountList;
    }

    public void resetOptimizationInfo() {
        machineInfoDao.resetOptimizationInfo();
    }

    public List<MachineInfoVO> getClientStatusVOs (QZSettingSearchClientGroupInfoCriteria qzSettingSearchClientGroupInfoCriteria) {
        return machineInfoDao.getClientStatusVOs(qzSettingSearchClientGroupInfoCriteria);
    }

    public ClientStatusForOptimization getClientStatusForOptimization(String clientID) {
        ClientStatusForOptimization clientStatusForOptimization = machineInfoDao.getClientStatusForOptimization(clientID);
        if (clientStatusForOptimization != null) {
            clientStatusForOptimization.setOpenStatistics(clientStatusForOptimization.getDisableStatistics() == 1 ? 0 : 1);
            clientStatusForOptimization.setCurrentTime(Utils.formatDate(new Date(), Utils.TIME_FORMAT));
        }
        return clientStatusForOptimization;
    }
}
