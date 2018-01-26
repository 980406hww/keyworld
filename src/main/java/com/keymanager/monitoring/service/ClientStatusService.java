package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.mail.MailHelper;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.criteria.CustomerKeywordRefreshStatInfoCriteria;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.ClientStatusRestartLog;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.util.Constants;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import com.keymanager.util.VNCAddressBookParser;
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
import java.util.*;

@Service
public class ClientStatusService extends ServiceImpl<ClientStatusDao, ClientStatus>{

	private static Logger logger = LoggerFactory.getLogger(ClientStatusService.class);
	
	@Autowired
	private ClientStatusDao clientStatusDao;

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private ConfigService configService;

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
		supplementDefaultValue(clientStatus);
		clientStatusDao.insert(clientStatus);
	}

	public void updatePageNo(String clientID, int pageNo){
		clientStatusDao.updatePageNo(clientID, pageNo);
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
			oldClientStatus.setZhanwaiPercent(clientStatus.getZhanwaiPercent());
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
			oldClientStatus.setSwitchGroupName(clientStatus.getSwitchGroupName());
			clientStatusDao.updateById(oldClientStatus);
		} else {
			supplementAdditionalValue(clientStatus);
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
		List<String> vpsInfos = FileUtil.readTxtFile(file,"UTF-8");
		for (String vpsInfo : vpsInfos) {
			String[] clientStatusInfo = vpsInfo.split("----");
			ClientStatus existingClientStatus = clientStatusDao.selectById(clientStatusInfo[0]);
			if(null != existingClientStatus) {
				saveClientStatusByVPSFile(existingClientStatus, clientStatusInfo);
				clientStatusDao.updateById(existingClientStatus);
			} else {
				ClientStatus clientStatus = new ClientStatus();
				clientStatus.setTerminalType(terminalType);
				clientStatus.setFreeSpace(500.00);
				clientStatus.setDisableStatistics(0);
				clientStatus.setValid(true);
				saveClientStatusByVPSFile(clientStatus, clientStatusInfo);
				supplementDefaultValue(clientStatus);
				clientStatusDao.insert(clientStatus);
			}
		}
	}

	private void saveClientStatusByVPSFile(ClientStatus clientStatus, String[] clientStatusInfo) {
		String[] vncInfos = clientStatusInfo[2].split(":");
		clientStatus.setClientID(clientStatusInfo[0]);
		clientStatus.setVpsBackendSystemComputerID(clientStatusInfo[1]);
		clientStatus.setHost(vncInfos[0]);
		clientStatus.setPort(vncInfos[1]);
		clientStatus.setUserName(clientStatusInfo[3]);
		clientStatus.setPassword(clientStatusInfo[4]);
		clientStatus.setBroadbandAccount(clientStatusInfo[5]);
		clientStatus.setBroadbandPassword(clientStatusInfo[6]);
		clientStatus.setClientIDPrefix(Utils.removeDigital(clientStatusInfo[0]));
	}

	private void supplementDefaultValue(ClientStatus clientStatus){
		clientStatus.setAllowSwitchGroup(0);
		clientStatus.setDisableStatistics(0);
		if(TerminalTypeEnum.PC.name().equalsIgnoreCase(clientStatus.getTerminalType())){
			clientStatus.setPage(5);
		}else{
			clientStatus.setPage(3);
		}
		clientStatus.setPageSize(0);
		clientStatus.setZhanneiPercent(0);
		clientStatus.setZhanwaiPercent(0);
		clientStatus.setKuaizhaoPercent(0);
		clientStatus.setBaiduSemPercent(0);
		clientStatus.setDragPercent(0);
		clientStatus.setMultiBrowser(1);
		clientStatus.setClearCookie(0);
		clientStatus.setEntryPageMinCount(0);
		clientStatus.setEntryPageMaxCount(0);
		clientStatus.setPageRemainMinTime(3000);
		clientStatus.setPageRemainMaxTime(5000);
		clientStatus.setInputDelayMinTime(50);
		clientStatus.setInputDelayMaxTime(80);
		clientStatus.setSlideDelayMinTime(700);
		clientStatus.setSlideDelayMaxTime(1500);
		clientStatus.setTitleRemainMinTime(1000);
		clientStatus.setTitleRemainMaxTime(3000);
		clientStatus.setOptimizeKeywordCountPerIP(1);
		clientStatus.setMaxUserCount(300);
		clientStatus.setWaitTimeAfterOpenBaidu(1000);
		clientStatus.setWaitTimeBeforeClick(1000);
		clientStatus.setWaitTimeAfterClick(5000);
//		clientStatus.setOneIPOneUser(1);
//		clientStatus.setRandomlyClickNoResult(1);
		clientStatus.setJustVisitSelfPage(1);
		clientStatus.setSleepPer2Words(1);
		clientStatus.setSupportPaste(1);
		clientStatus.setMoveRandomly(1);
		clientStatus.setClearLocalStorage(1);

		supplementAdditionalValue(clientStatus);
	}


	private void supplementAdditionalValue(ClientStatus clientStatus){
		clientStatus.setLastVisitTime(Utils.getCurrentTimestamp());
		clientStatus.setTenMinsLastVisitTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 10));
		clientStatus.setRestartTime(Utils.getCurrentTimestamp());
		clientStatus.setThreeMinsRestartTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 3));
		clientStatus.setTenMinsRestartTime(Utils.addMinutes(Utils.getCurrentTimestamp(), 10));
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

	public ClientStatus getStoppedClientStatuses(){
		ClientStatus tmpClientStatus = null;
		List<ClientStatus> clientStatuses = clientStatusDao.searchRestartingClientStatuses();
		for(ClientStatus clientStatus : clientStatuses){
//			if(customerKeywordService.haveCustomerKeywordForOptimization(clientStatus.getTerminalType(), clientStatus.getClientID())){
				tmpClientStatus = clientStatus;
				updateRestartStatus(clientStatus.getClientID(), "Logging");
				break;
//			}
		}
		if(tmpClientStatus == null) {
			clientStatuses = clientStatusDao.searchWaitingRestartingClientStatuses();
			for (ClientStatus clientStatus : clientStatuses) {
//				if (customerKeywordService.haveCustomerKeywordForOptimization(clientStatus.getTerminalType(), clientStatus.getClientID())) {
					tmpClientStatus = clientStatus;
					updateRestartStatus(clientStatus.getClientID(), "Processing");
					break;
//				}
			}
		}
		return tmpClientStatus;
	}

	private void updateRestartStatus(String clientID, String restartStatus){
		clientStatusDao.updateRestartStatus(clientID, restartStatus);
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

	public void switchGroup() throws Exception{
		switchGroup(TerminalTypeEnum.PC.name());
		switchGroup(TerminalTypeEnum.Phone.name());
	}

	private void switchGroup(String terminalType) throws Exception{
		List<ClientStatus> clientStatuses = clientStatusDao.getClientStatusesForSwitchGroup(terminalType);
		if(CollectionUtils.isNotEmpty(clientStatuses)) {
			Map<String, List<ClientStatus>> clientStatusMap = new HashMap<String, List<ClientStatus>>();
			for(ClientStatus clientStatus : clientStatuses){
				String key = clientStatus.getSwitchGroupName().toLowerCase();
				List<ClientStatus> tmpClientStatuses = clientStatusMap.get(key);
				if(tmpClientStatuses == null){
					tmpClientStatuses = new ArrayList<ClientStatus>();
					clientStatusMap.put(key, tmpClientStatuses);
				}
				tmpClientStatuses.add(clientStatus);
			}

			for(String key : clientStatusMap.keySet()){
				this.switchClientStatuses(clientStatusMap.get(key));
			}
		}
	}

	private void switchClientStatuses(List<ClientStatus> clientStatuses){
		List<ClientStatus> cloneClientStatuses = new ArrayList<ClientStatus>(clientStatuses);
		Collections.shuffle(clientStatuses);
		Collections.shuffle(clientStatuses);
		Collections.shuffle(cloneClientStatuses);
		Collections.shuffle(cloneClientStatuses);
		Collections.shuffle(cloneClientStatuses);
		for (int i = 0; i < clientStatuses.size(); i++) {
			ClientStatus sourceClientStatus = clientStatuses.get(i);
			ClientStatus targetClientStatus = cloneClientStatuses.get(i);
			switchClientStatusInfo(sourceClientStatus, targetClientStatus);
		}

		for (ClientStatus clientStatus : clientStatuses) {
			clientStatusDao.updateById(clientStatus);
		}
	}

	private void switchClientStatusInfo(ClientStatus sourceClientStatus, ClientStatus targetClientStatus){
		Integer baiduSemPercent = sourceClientStatus.getBaiduSemPercent();
		sourceClientStatus.setBaiduSemPercent(targetClientStatus.getBaiduSemPercent());
		targetClientStatus.setBaiduSemPercent(baiduSemPercent);

		int clearCookie = sourceClientStatus.getClearCookie();
		sourceClientStatus.setClearCookie(targetClientStatus.getClearCookie());
		targetClientStatus.setClearCookie(clearCookie);

		Integer dragPercent = sourceClientStatus.getDragPercent();
		sourceClientStatus.setDragPercent(targetClientStatus.getDragPercent());
		targetClientStatus.setDragPercent(dragPercent);

		Integer kuaizhaoPercent = sourceClientStatus.getKuaizhaoPercent();
		sourceClientStatus.setKuaizhaoPercent(targetClientStatus.getKuaizhaoPercent());
		targetClientStatus.setKuaizhaoPercent(kuaizhaoPercent);

		Integer multiBrowser = sourceClientStatus.getMultiBrowser();
		sourceClientStatus.setMultiBrowser(targetClientStatus.getMultiBrowser());
		targetClientStatus.setMultiBrowser(multiBrowser);

		String operationType = sourceClientStatus.getOperationType();
		sourceClientStatus.setOperationType(targetClientStatus.getOperationType());
		targetClientStatus.setOperationType(operationType);

		int page = sourceClientStatus.getPage();
		sourceClientStatus.setPage(targetClientStatus.getPage());
		targetClientStatus.setPage(page);

		Integer pageSize = sourceClientStatus.getPageSize();
		sourceClientStatus.setPageSize(targetClientStatus.getPageSize());
		targetClientStatus.setPageSize(pageSize);

		Integer zhanneiPercent = sourceClientStatus.getZhanneiPercent();
		sourceClientStatus.setZhanneiPercent(targetClientStatus.getZhanneiPercent());
		targetClientStatus.setZhanneiPercent(zhanneiPercent);

		Integer zhanwaiPercent = sourceClientStatus.getZhanwaiPercent();
		sourceClientStatus.setZhanwaiPercent(targetClientStatus.getZhanwaiPercent());
		targetClientStatus.setZhanwaiPercent(zhanwaiPercent);

		String group = sourceClientStatus.getGroup();
		sourceClientStatus.setGroup(targetClientStatus.getGroup());
		targetClientStatus.setGroup(group);

		int disableStatistics = sourceClientStatus.getDisableStatistics();
		sourceClientStatus.setDisableStatistics(targetClientStatus.getDisableStatistics());
		targetClientStatus.setDisableStatistics(disableStatistics);

		int disableVisiteWebsite = sourceClientStatus.getDisableVisitWebsite();
		sourceClientStatus.setDisableVisitWebsite(targetClientStatus.getDisableVisitWebsite());
		targetClientStatus.setDisableVisitWebsite(disableVisiteWebsite);

		int entryPageMinCount = sourceClientStatus.getEntryPageMinCount();
		sourceClientStatus.setEntryPageMinCount(targetClientStatus.getEntryPageMinCount());
		targetClientStatus.setEntryPageMinCount(entryPageMinCount);

		int entryPageMaxCount = sourceClientStatus.getEntryPageMaxCount();
		sourceClientStatus.setEntryPageMaxCount(targetClientStatus.getEntryPageMaxCount());
		targetClientStatus.setEntryPageMaxCount(entryPageMaxCount);

		int disableVisitWebsite = sourceClientStatus.getDisableVisitWebsite();
		sourceClientStatus.setDisableVisitWebsite(targetClientStatus.getDisableVisitWebsite());
		targetClientStatus.setDisableVisitWebsite(disableVisitWebsite);

		int pageRemainMinTime = sourceClientStatus.getPageRemainMinTime();
		sourceClientStatus.setPageRemainMinTime(targetClientStatus.getPageRemainMinTime());
		targetClientStatus.setPageRemainMinTime(pageRemainMinTime);

		int pageRemainMaxTime = sourceClientStatus.getPageRemainMaxTime();
		sourceClientStatus.setPageRemainMaxTime(targetClientStatus.getPageRemainMaxTime());
		targetClientStatus.setPageRemainMaxTime(pageRemainMaxTime);

		int inputDelayMinTime = sourceClientStatus.getInputDelayMinTime();
		sourceClientStatus.setInputDelayMinTime(targetClientStatus.getInputDelayMinTime());
		targetClientStatus.setInputDelayMinTime(inputDelayMinTime);

		int inputDelayMaxTime = sourceClientStatus.getInputDelayMaxTime();
		sourceClientStatus.setInputDelayMaxTime(targetClientStatus.getInputDelayMaxTime());
		targetClientStatus.setInputDelayMaxTime(inputDelayMaxTime);

		int slideDelayMinTime = sourceClientStatus.getSlideDelayMinTime();
		sourceClientStatus.setSlideDelayMinTime(targetClientStatus.getSlideDelayMinTime());
		targetClientStatus.setSlideDelayMinTime(slideDelayMinTime);

		int slideDelayMaxTime = sourceClientStatus.getSlideDelayMaxTime();
		sourceClientStatus.setSlideDelayMaxTime(targetClientStatus.getSlideDelayMaxTime());
		targetClientStatus.setSlideDelayMaxTime(slideDelayMaxTime);

		int titleRemainMinTime = sourceClientStatus.getTitleRemainMinTime();
		sourceClientStatus.setTitleRemainMinTime(targetClientStatus.getTitleRemainMinTime());
		targetClientStatus.setTitleRemainMinTime(titleRemainMinTime);

		int titleRemainMaxTime = sourceClientStatus.getTitleRemainMaxTime();
		sourceClientStatus.setTitleRemainMaxTime(targetClientStatus.getTitleRemainMaxTime());
		targetClientStatus.setTitleRemainMaxTime(titleRemainMaxTime);

		int optimizeKeywordCountPerIP = sourceClientStatus.getOptimizeKeywordCountPerIP();
		sourceClientStatus.setOptimizeKeywordCountPerIP(targetClientStatus.getOptimizeKeywordCountPerIP());
		targetClientStatus.setOptimizeKeywordCountPerIP(optimizeKeywordCountPerIP);

		int oneIPOneUser = sourceClientStatus.getOneIPOneUser();
		sourceClientStatus.setOneIPOneUser(targetClientStatus.getOneIPOneUser());
		targetClientStatus.setOneIPOneUser(oneIPOneUser);

		int randomlyClickNoResult = sourceClientStatus.getRandomlyClickNoResult();
		sourceClientStatus.setRandomlyClickNoResult(targetClientStatus.getRandomlyClickNoResult());
		targetClientStatus.setRandomlyClickNoResult(randomlyClickNoResult);

		int justVisitSelfPage = sourceClientStatus.getJustVisitSelfPage();
		sourceClientStatus.setJustVisitSelfPage(targetClientStatus.getJustVisitSelfPage());
		targetClientStatus.setJustVisitSelfPage(justVisitSelfPage);

		int sleepPer2Words = sourceClientStatus.getSleepPer2Words();
		sourceClientStatus.setSleepPer2Words(targetClientStatus.getSleepPer2Words());
		targetClientStatus.setSleepPer2Words(sleepPer2Words);

		int supportPaste = sourceClientStatus.getSupportPaste();
		sourceClientStatus.setSupportPaste(targetClientStatus.getSupportPaste());
		targetClientStatus.setSupportPaste(supportPaste);

		int moveRandomly = sourceClientStatus.getMoveRandomly();
		sourceClientStatus.setMoveRandomly(targetClientStatus.getMoveRandomly());
		targetClientStatus.setMoveRandomly(moveRandomly);

		int parentSearchEntry = sourceClientStatus.getParentSearchEntry();
		sourceClientStatus.setParentSearchEntry(targetClientStatus.getParentSearchEntry());
		targetClientStatus.setParentSearchEntry(parentSearchEntry);

		int clearLocalStorage = sourceClientStatus.getClearLocalStorage();
		sourceClientStatus.setClearLocalStorage(targetClientStatus.getClearLocalStorage());
		targetClientStatus.setClearLocalStorage(clearLocalStorage);

		int lessClickAtNight = sourceClientStatus.getLessClickAtNight();
		sourceClientStatus.setLessClickAtNight(targetClientStatus.getLessClickAtNight());
		targetClientStatus.setLessClickAtNight(lessClickAtNight);

		int sameCityUser = sourceClientStatus.getSameCityUser();
		sourceClientStatus.setSameCityUser(targetClientStatus.getSameCityUser());
		targetClientStatus.setSameCityUser(sameCityUser);

		int locateTitlePosition = sourceClientStatus.getLocateTitlePosition();
		sourceClientStatus.setLocateTitlePosition(targetClientStatus.getLocateTitlePosition());
		targetClientStatus.setLocateTitlePosition(locateTitlePosition);

		int baiduAllianceEntry = sourceClientStatus.getBaiduAllianceEntry();
		sourceClientStatus.setBaiduAllianceEntry(targetClientStatus.getBaiduAllianceEntry());
		targetClientStatus.setBaiduAllianceEntry(baiduAllianceEntry);

		int justClickSpecifiedTitle = sourceClientStatus.getJustClickSpecifiedTitle();
		sourceClientStatus.setJustClickSpecifiedTitle(targetClientStatus.getJustClickSpecifiedTitle());
		targetClientStatus.setJustClickSpecifiedTitle(justClickSpecifiedTitle);

		int randomlyClickMoreLink = sourceClientStatus.getRandomlyClickMoreLink();
		sourceClientStatus.setRandomlyClickMoreLink(targetClientStatus.getRandomlyClickMoreLink());
		targetClientStatus.setRandomlyClickMoreLink(randomlyClickMoreLink);

		int moveUp20 = sourceClientStatus.getMoveUp20();
		sourceClientStatus.setMoveUp20(targetClientStatus.getMoveUp20());
		targetClientStatus.setMoveUp20(moveUp20);

		int waitTimeAfterOpenBaidu = sourceClientStatus.getWaitTimeAfterOpenBaidu();
		sourceClientStatus.setWaitTimeAfterOpenBaidu(targetClientStatus.getWaitTimeAfterOpenBaidu());
		targetClientStatus.setWaitTimeAfterOpenBaidu(waitTimeAfterOpenBaidu);

		int waitTimeBeforeClick = sourceClientStatus.getWaitTimeBeforeClick();
		sourceClientStatus.setWaitTimeBeforeClick(targetClientStatus.getWaitTimeBeforeClick());
		targetClientStatus.setWaitTimeBeforeClick(waitTimeBeforeClick);

		int waitTimeAfterClick = sourceClientStatus.getWaitTimeAfterClick();
		sourceClientStatus.setWaitTimeAfterClick(targetClientStatus.getWaitTimeAfterClick());
		targetClientStatus.setWaitTimeAfterClick(waitTimeAfterClick);

		int maxUserCount = sourceClientStatus.getMaxUserCount();
		sourceClientStatus.setMaxUserCount(targetClientStatus.getMaxUserCount());
		targetClientStatus.setMaxUserCount(maxUserCount);
	}

	public void sendNotificationForRenewal() throws Exception{
		String condition = " and DATE_ADD(fRenewalDate, INTERVAL -3 DAY ) < NOW() and fValid = 1 ORDER BY fRenewalDate ";
		List<ClientStatus> clientStatuses = clientStatusDao.getClientStatusesForRenewal();

		if(!Utils.isEmpty(clientStatuses)){
			Config notificationEmail = configService.getConfig("NotificationEmail", "EmailAddress");
			StringBuilder sb = new StringBuilder();
			sb.append("<table><tr><td>客户端ID</td><td>续费日期</td></tr>");
			for(ClientStatus clientStatus : clientStatuses){
				sb.append(String.format("<tr><td>%s</td><td>%s</td>", clientStatus.getClientID(), Utils.formatDatetime(clientStatus.getRenewalDate(),
						"yyyy-MM-dd")));
			}
			sb.append("</table>");
			String[] emailAddresses = notificationEmail.getValue().split(";");
			for(String emailAddress : emailAddresses) {
				MailHelper.sendClientDownNotification(emailAddress, sb.toString(), "续费通知");
			}
		}
	}

	public String[] getOperationTypeValues(String terminalType) {
		Config config = configService.getConfig(Constants.CONFIG_TYPE_OPTIMIZATION_TYPE,terminalType);
		String [] operationTypeValues = config.getValue().split(",");
		return operationTypeValues;
	}

	public void updateRemainingKeywordIndicator(List<String> groupNames, int indicator){
		clientStatusDao.updateRemainingKeywordIndicator(groupNames, indicator);
	}

	public void updateAllRemainingKeywordIndicator(int indicator){
		clientStatusDao.updateAllRemainingKeywordIndicator(indicator);
	}
}
