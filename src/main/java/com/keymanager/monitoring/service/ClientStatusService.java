package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.ClientStatusRestartLog;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import com.keymanager.util.VNCAddressBookParser;
import com.keymanager.value.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

import java.util.List;

@Service
public class ClientStatusService extends ServiceImpl<ClientStatusDao, ClientStatus>{

	private static Logger logger = LoggerFactory.getLogger(ClientStatusService.class);
	
	@Autowired
	private ClientStatusDao clientStatusDao;

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private ClientStatusRestartLogService clientStatusRestartLogService;

	public void changeTerminalType(String clientID, String terminalType){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus != null){
			clientStatus.setTerminalType(terminalType);
			clientStatusDao.updateById(clientStatus);
		}
	}

	public void addSummaryClientStatus(String terminalType, String clientID, String freeSpace, String version, String city){
		ClientStatus clientStatus = new ClientStatus();
		clientStatus.setTerminalType(terminalType);
		clientStatus.setClientID(clientID);
		clientStatus.setFreeSpace(StringUtil.isNumeric(freeSpace) ? Double.parseDouble(freeSpace) : 0);
		clientStatus.setVersion(version);
		clientStatus.setCity(city);
		clientStatus.setClientIDPrefix(Utils.removeDigital(clientID));
		clientStatusDao.addSummaryClientStatus(clientStatus);
	}

	public void updatePageNo(String clientID, int pageNo){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus != null){
			clientStatus.setPageNo(pageNo);
			clientStatusDao.updateById(clientStatus);
		}
	}

	public  void updateClientVersion(String clientID, String version){
		clientStatusDao.updateClientVersion(clientID, version);
	}

	public void logClientStatusTime(String terminalType, String clientID, String status, String freeSpace, String version, String
			city, int updateCount){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus == null){
			addSummaryClientStatus(terminalType, clientID, freeSpace, version, city);
		}else{
			clientStatusDao.updateOptimizationResult(clientID, status, version, freeSpace, city, updateCount);
		}
	}

	public List<ClientStatus> searchClientStatusForRefreshStat(CustomerKeywordRefreshStatInfoCriteria customerKeywordRefreshStatInfoCriteria) {
		return clientStatusDao.searchClientStatusForRefreshStat(customerKeywordRefreshStatInfoCriteria);
	}

	public Page<ClientStatus> searchClientStatuses(Page<ClientStatus> page, ClientStatusCriteria clientStatusCriteria, boolean normalSearchFlag) {
		if(normalSearchFlag) {
			page.setRecords(clientStatusDao.searchClientStatuses(page, clientStatusCriteria));
		} else {
			page.setRecords(clientStatusDao.searchBadClientStatus(page, clientStatusCriteria));
		}
		return page;
	}

	public void updateClientStatus(ClientStatus clientStatus) {
		clientStatusDao.updateById(clientStatus);
	}

	public void updateClientStatusTargetVersion(List<String> clientIDs, String targetVersion) throws Exception {
		clientStatusDao.updateClientStatusTargetVersion(clientIDs, targetVersion);
	}

	public void updateRenewalDate(String clientIDs,String settingType,String renewalDate) throws Exception {
		String[] clientIDArray = clientIDs.split(",");

		for (String clientID : clientIDArray) {
			ClientStatus clientStatus = clientStatusDao.selectById(clientID);
			if("increaseOneMonth".equals(settingType)) {
				if(clientStatus.getRenewalDate() != null) {
					clientStatus.setRenewalDate(Utils.addMonth(clientStatus.getRenewalDate(), 1));
				} else {
					clientStatus.setRenewalDate(Utils.addMonth(Utils.getCurrentTimestamp(), 1));
				}
			} else {
				clientStatus.setRenewalDate(Utils.string2Timestamp(renewalDate));
			}
			clientStatusDao.updateById(clientStatus);
		}
	}

	public ClientStatus getClientStatus(String clientID, String terminalType) {
		ClientStatus clientStatus = clientStatusDao.getClientStatusByClientID(clientID, terminalType);
		return clientStatus;
	}

	public void deleteClientStatus(String clientID) {
		clientStatusDao.deleteById(clientID);
	}

	public void deleteAll(List<String> clientIDs) {
		clientStatusDao.deleteClientStatus(clientIDs);
	}

	public void saveClientStatus(ClientStatus clientStatus) {
		if (null != clientStatus.getClientID()) {
			ClientStatus oldClientStatus = clientStatusDao.selectById(clientStatus.getClientID());
			oldClientStatus.setGroup(clientStatus.getGroup());
			oldClientStatus.setOperationType(clientStatus.getOperationType());
			oldClientStatus.setPageSize(clientStatus.getPageSize());
			oldClientStatus.setPage(clientStatus.getPage());
			oldClientStatus.setDragPercent(clientStatus.getDragPercent());
			oldClientStatus.setZhanneiPercent(clientStatus.getZhanneiPercent());
			oldClientStatus.setKuaizhaoPercent(clientStatus.getKuaizhaoPercent());
			oldClientStatus.setBaiduSemPercent(clientStatus.getBaiduSemPercent());
			oldClientStatus.setMultiBrowser(clientStatus.getMultiBrowser());
			oldClientStatus.setClearCookie(clientStatus.getClearCookie());
			oldClientStatus.setAllowSwitchGroup(clientStatus.getAllowSwitchGroup());
			oldClientStatus.setDisableStatistics(clientStatus.getDisableStatistics());
			oldClientStatus.setHost(clientStatus.getHost());
			oldClientStatus.setPort(clientStatus.getPort());
			oldClientStatus.setUserName(clientStatus.getUserName());
			oldClientStatus.setPassword(clientStatus.getPassword());
			oldClientStatus.setBroadbandAccount(clientStatus.getBroadbandAccount());
			oldClientStatus.setBroadbandPassword(clientStatus.getBroadbandPassword());
			oldClientStatus.setVpsBackendSystemComputerID(clientStatus.getVpsBackendSystemComputerID());
			oldClientStatus.setVpsBackendSystemPassword(clientStatus.getVpsBackendSystemPassword());
			oldClientStatus.setEntryPageMinCount(clientStatus.getEntryPageMinCount());
			oldClientStatus.setEntryPageMaxCount(clientStatus.getEntryPageMaxCount());
			oldClientStatus.setDisableVisitWebsite(clientStatus.getDisableVisitWebsite());
			oldClientStatus.setPageRemainMinTime(clientStatus.getPageRemainMinTime());
			oldClientStatus.setPageRemainMaxTime(clientStatus.getPageRemainMaxTime());
			oldClientStatus.setInputDelayMinTime(clientStatus.getInputDelayMinTime());
			oldClientStatus.setInputDelayMaxTime(clientStatus.getInputDelayMaxTime());
			oldClientStatus.setSlideDelayMinTime(clientStatus.getSlideDelayMinTime());
			oldClientStatus.setSlideDelayMaxTime(clientStatus.getSlideDelayMaxTime());
			oldClientStatus.setTitleRemainMinTime(clientStatus.getTitleRemainMinTime());
			oldClientStatus.setTitleRemainMaxTime(clientStatus.getTitleRemainMaxTime());
			oldClientStatus.setWaitTimeAfterOpenBaidu(clientStatus.getWaitTimeAfterOpenBaidu());
			oldClientStatus.setWaitTimeBeforeClick(clientStatus.getWaitTimeBeforeClick());
			oldClientStatus.setWaitTimeAfterClick(clientStatus.getWaitTimeAfterClick());
			oldClientStatus.setMaxUserCount(clientStatus.getMaxUserCount());
			oldClientStatus.setOptimizeKeywordCountPerIP(clientStatus.getOptimizeKeywordCountPerIP());
			oldClientStatus.setOneIPOneUser(clientStatus.getOneIPOneUser());
			oldClientStatus.setRandomlyClickNoResult(clientStatus.getRandomlyClickNoResult());
			oldClientStatus.setJustVisitSelfPage(clientStatus.getJustVisitSelfPage());
			oldClientStatus.setSleepPer2Words(clientStatus.getSleepPer2Words());
			oldClientStatus.setSupportPaste(clientStatus.getSupportPaste());
			oldClientStatus.setMoveRandomly(clientStatus.getMoveRandomly());
			oldClientStatus.setParentSearchEntry(clientStatus.getParentSearchEntry());
			oldClientStatus.setClearLocalStorage(clientStatus.getClearLocalStorage());
			oldClientStatus.setLessClickAtNight(clientStatus.getLessClickAtNight());
			oldClientStatus.setSameCityUser(clientStatus.getSameCityUser());
			oldClientStatus.setLocateTitlePosition(clientStatus.getLocateTitlePosition());
			oldClientStatus.setBaiduAllianceEntry(clientStatus.getBaiduAllianceEntry());
			oldClientStatus.setJustClickSpecifiedTitle(clientStatus.getJustClickSpecifiedTitle());
			oldClientStatus.setRandomlyClickMoreLink(clientStatus.getRandomlyClickMoreLink());
			oldClientStatus.setMoveUp20(clientStatus.getMoveUp20());
			clientStatusDao.updateById(oldClientStatus);
		} else {
			clientStatusDao.insert(clientStatus);
		}
	}

	public void resetRestartStatusForProcessing() {
		clientStatusDao.resetRestartStatusForProcessing();
	}

	public void changeStatus(String clientID) {
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		clientStatus.setValid(!clientStatus.getValid());
		clientStatusDao.updateById(clientStatus);
	}

	public void uploadVNCFile(InputStream inputStream, String terminalType) throws Exception {
		if(inputStream != null){
			VNCAddressBookParser vncAddressBookParser = new VNCAddressBookParser();
			Map<String, String> vncInfoMap = vncAddressBookParser.extractVNCInfo(inputStream);
			List<ClientStatus> clientStatuses = clientStatusDao.searchClientStatusesOrByHost(terminalType,null);
			for (ClientStatus clientStatus : clientStatuses) {
				String vncInfo = vncInfoMap.get(clientStatus.getClientID());
				if (!Utils.isNullOrEmpty(vncInfo)) {
					String vncInfos[] = vncInfo.split(":");
					if (vncInfos.length == 3) {
						clientStatus.setHost(vncInfos[0]);
						clientStatus.setPort(vncInfos[1]);
						clientStatus.setVpsBackendSystemComputerID(vncInfos[2]);
					} else {
						System.out.println(vncInfo);
					}
					updateClientStatus(clientStatus);
					writeTxtFile(clientStatus);
				}
			}
		}
	}

	public void uploadVPSFile(File file, String terminalType) throws Exception {
		List<String> VPSInfos = FileUtil.readTxtFile(file,"UTF-8");
		for (String VPSInfo : VPSInfos) {
			String[] clientStatusInfo = VPSInfo.split("----");
			ClientStatus clientStatus = new ClientStatus();
			clientStatus.setTerminalType(terminalType);
			clientStatus.setClientID(clientStatusInfo[0]);
			clientStatus.setVpsBackendSystemComputerID(clientStatusInfo[1]);
			clientStatus.setHost(clientStatusInfo[2].substring(0,clientStatusInfo[2].indexOf(":")));
			clientStatus.setPort(clientStatusInfo[2].substring(clientStatusInfo[2].indexOf(":") + 1));
			clientStatus.setUserName(clientStatusInfo[3]);
			clientStatus.setPassword(clientStatusInfo[4]);
			clientStatus.setBroadbandAccount(clientStatusInfo[5]);
			clientStatus.setBroadbandPassword(clientStatusInfo[6]);
			clientStatus.setFreeSpace(500.00);
			clientStatus.setDisableStatistics(0);
			clientStatus.setLastVisitTime(Utils.getCurrentTimestamp());
			clientStatus.setClientIDPrefix(Utils.removeDigital(clientStatusInfo[0]));
			clientStatusDao.insert(clientStatus);
		}
	}

	public void getVNCFileInfo(String terminalType) throws Exception {
		List<ClientStatus> clientStatuses = clientStatusDao.searchClientStatusesOrByHost(terminalType,"yes");
		for (ClientStatus clientStatus : clientStatuses) {
			writeTxtFile(clientStatus);
		}
	}

	public void getFullVNCFileInfo(String terminalType) throws Exception {
		List<ClientStatus> clientStatuses = clientStatusDao.searchClientStatusesOrByHost(terminalType,null);
		writeFullTxtFile(clientStatuses);
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

	public void writeFullTxtFile(List<ClientStatus> clientStatuses) throws Exception {
		FileOutputStream o = null;
		Utils.createDir(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vncAll/");
		String fileName = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vncAll/vncAll.xml";
		o = new FileOutputStream(fileName);
		writeXMLDTD(o);
		o.write("<vncaddressbook password=\"cf73063bbf04432f43bba536d4a5f96017c5798fb8acc9xf67f8acf1945d85c\">".getBytes("UTF-8"));
		o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
		o.write("<folder name=\"263互联\">".getBytes("UTF-8"));
		o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
		for (ClientStatus clientStatus : clientStatuses) {
			o.write(String.format("<file name=\"%s--%s-%s--%s--%s\">",clientStatus.getClientID(),clientStatus.getHost(),clientStatus.getPort(),clientStatus.getPort(),clientStatus.getVpsBackendSystemComputerID()).getBytes("UTF-8"));
			o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write("<section name=\"Connection\">".getBytes("UTF-8"));
			o.write(((String) java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
			o.write(String.format("<param name=\"Host\" value=\"%s\" />",clientStatus.getHost()).getBytes("UTF-8"));
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
			o.write(String.format("<param name=\"UserName\" value=\"%s\" />",clientStatus.getUserName()).getBytes("UTF-8"));
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

	public void writeTxtFile(ClientStatus clientStatus) throws Exception {
		FileOutputStream o = null;
		Utils.createDir(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vnc/");
		String fileName = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + "vnc/" + clientStatus.getClientID() + ".vnc";
		o = new FileOutputStream(fileName);
		o.write("[Connection]".getBytes("UTF-8"));
		o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
		o.write(String.format("Host=%s", clientStatus.getHost()).getBytes("UTF-8"));
		o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
		o.write(String.format("Port=%s", clientStatus.getPort()).getBytes("UTF-8"));
		o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
		o.write(String.format("Username=%s", clientStatus.getUserName()).getBytes("UTF-8"));
		o.write(((String)java.security.AccessController.doPrivileged(new sun.security.action.GetPropertyAction("line.separator"))).getBytes("UTF-8"));
		o.write("Password=8e587919308fcab0c34af756358b9053".getBytes("UTF-8"));
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
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		clientStatus.setGroup(groupName);
		clientStatusDao.updateById(clientStatus);
	}

	public void updateOperationType(String clientID, String operationType) {
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		clientStatus.setOperationType(operationType);
		clientStatusDao.updateById(clientStatus);
	}

	public void updateUpgradeFailedReason(String clientID, String upgradeFailedReason) {
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		clientStatus.setUpgradeFailedReason(upgradeFailedReason);
		clientStatusDao.updateById(clientStatus);
	}


	public List<ClientStatusSummaryVO> searchClientStatusSummaryVO(String clientIDPrefix, String city) throws Exception {
		List<ClientStatusSummaryVO> pcClientStatusSummaryVOs = clientStatusDao.searchClientStatusSummaryVO(clientIDPrefix,city);
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
		return clientStatusDao.searchClientStatusGroupSummaryVO(group,terminalType);
	}

	public String checkUpgrade(String clientID){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus != null){
			if(clientStatus.getTargetVersion() != null){
				return clientStatus.getTargetVersion().equals(clientStatus.getVersion()) ? "" : clientStatus.getTargetVersion();
			}
		}
		return "0";
	}

	public ClientStatus getStoppedClientStatuses(String terminalType){
		ClientStatus tmpClientStatus = null;
		List<ClientStatus> clientStatuses = clientStatusDao.searchRestartingClientStatuses(terminalType);
		for(ClientStatus clientStatus : clientStatuses){
			if(customerKeywordService.haveCustomerKeywordForOptimization(terminalType, clientStatus.getClientID())){
				tmpClientStatus = clientStatus;
				updateRestartStatus(clientStatus, "Logging");
				break;
			}
		}
		clientStatuses = clientStatusDao.searchWaitingRestartingClientStatuses(terminalType);
		for(ClientStatus clientStatus : clientStatuses){
			if(customerKeywordService.haveCustomerKeywordForOptimization(terminalType, clientStatus.getClientID())){
				tmpClientStatus = clientStatus;
				updateRestartStatus(clientStatus, "Processing");
				break;
			}
		}
		return tmpClientStatus;
	}

	private void updateRestartStatus(ClientStatus clientStatus, String restartStatus){
		clientStatus.setRestartStatus(restartStatus);
		clientStatus.setRestartTime(Utils.getCurrentTimestamp());
		clientStatus.setRestartOrderingTime(Utils.getCurrentTimestamp());
		clientStatusDao.updateById(clientStatus);
	}

	public void updateClientStatusRestartStatus(String clientID, String restartStatus){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus != null){
			clientStatus.setRestartTime(Utils.getCurrentTimestamp());
			clientStatus.setRestartOrderingTime(Utils.getCurrentTimestamp());
			clientStatus.setRestartCount(clientStatus.getRestartCount() + 1);
			clientStatus.setRestartStatus(restartStatus);
			clientStatusDao.updateById(clientStatus);

			ClientStatusRestartLog clientStatusRestartLog = new ClientStatusRestartLog();
			clientStatusRestartLog.setClientID(clientStatus.getClientID());
			clientStatusRestartLog.setGroup(clientStatus.getGroup());
			clientStatusRestartLog.setRestartCount(clientStatus.getRestartCount() + 1);
			clientStatusRestartLog.setRestartStatus(restartStatus);
			clientStatusRestartLogService.insert(clientStatusRestartLog);
		}
	}
}
