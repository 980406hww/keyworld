package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.util.Utils;
import com.keymanager.util.VNCAddressBookParser;
import com.keymanager.util.ZipCompressor;
import com.keymanager.value.ClientStatusForUpdateRenewalDate;
import com.keymanager.value.ClientStatusForUpdateTargetVersion;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class ClientStatusService extends ServiceImpl<ClientStatusDao, ClientStatus>{

	private static Logger logger = LoggerFactory.getLogger(ClientStatusService.class);
	
	@Autowired
	private ClientStatusDao clientStatusDao;

	public void changeTerminalType(String clientID, String terminalType){
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
		if(clientStatus != null){
			clientStatus.setTerminalType(terminalType);
			clientStatusDao.updateById(clientStatus);
		}
	}

	public Page<ClientStatus> searchClientStatuses(Page<ClientStatus> page, ClientStatusCriteria clientStatusCriteria) {
		page.setRecords(clientStatusDao.searchClientStatuses(page, clientStatusCriteria));
		return page;
	}

	public void updateClientStatus(ClientStatus clientStatus) {
		ClientStatus oldClientStatus = clientStatusDao.selectById(clientStatus.getClientID());
		if (oldClientStatus != null) {
			oldClientStatus.setGroup(clientStatus.getGroup());
			oldClientStatus.setOperationType(clientStatus.getOperationType());
			oldClientStatus.setPage(clientStatus.getPage());
			oldClientStatus.setPageSize(clientStatus.getPageSize() == null ? null : clientStatus.getPageSize());
			oldClientStatus.setZhanneiPercent(clientStatus.getZhanneiPercent() == null ? null : clientStatus.getZhanneiPercent());
			oldClientStatus.setKuaizhaoPercent(clientStatus.getKuaizhaoPercent() == null ? null : clientStatus.getKuaizhaoPercent());
			oldClientStatus.setBaiduSemPercent(clientStatus.getBaiduSemPercent() == null ? null : clientStatus.getBaiduSemPercent());
			oldClientStatus.setDisableVisitWebsite(clientStatus.getDisableVisitWebsite());
			oldClientStatus.setEntryPageMinCount(clientStatus.getEntryPageMinCount());
			oldClientStatus.setEntryPageMaxCount(clientStatus.getEntryPageMaxCount());
			oldClientStatus.setPageRemainMinTime(clientStatus.getPageRemainMinTime());
			oldClientStatus.setPageRemainMaxTime(clientStatus.getPageRemainMaxTime());
			oldClientStatus.setInputDelayMinTime(clientStatus.getInputDelayMinTime());
			oldClientStatus.setInputDelayMaxTime(clientStatus.getInputDelayMaxTime());
			oldClientStatus.setSlideDelayMinTime(clientStatus.getSlideDelayMinTime());
			oldClientStatus.setSlideDelayMaxTime(clientStatus.getSlideDelayMaxTime());
			oldClientStatus.setTitleRemainMinTime(clientStatus.getTitleRemainMinTime());
			oldClientStatus.setTitleRemainMaxTime(clientStatus.getTitleRemainMaxTime());
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
			oldClientStatus.setWaitTimeAfterOpenBaidu(clientStatus.getWaitTimeAfterOpenBaidu());
			oldClientStatus.setWaitTimeBeforeClick(clientStatus.getWaitTimeBeforeClick());
			oldClientStatus.setWaitTimeAfterClick(clientStatus.getWaitTimeAfterClick());
			oldClientStatus.setMaxUserCount(clientStatus.getMaxUserCount());
			oldClientStatus.setMultiBrowser(clientStatus.getMultiBrowser());
			oldClientStatus.setClearCookie(clientStatus.getClearCookie());
			oldClientStatus.setDragPercent(clientStatus.getDragPercent() == null ? null : clientStatus.getDragPercent());
			oldClientStatus.setAllowSwitchGroup(clientStatus.getAllowSwitchGroup());
			oldClientStatus.setDisableStatistics(clientStatus.getDisableStatistics());
			oldClientStatus.setHost(clientStatus.getHost());
			oldClientStatus.setPort(clientStatus.getPort());
			oldClientStatus.setUserName(clientStatus.getUserName());
			oldClientStatus.setPassword(clientStatus.getPassword());
			oldClientStatus.setVpsBackendSystemComputerID(clientStatus.getVpsBackendSystemComputerID());
			oldClientStatus.setVpsBackendSystemPassword(clientStatus.getVpsBackendSystemPassword());
			clientStatusDao.updateById(oldClientStatus);
		}
	}

	public void updateClientStatusTargetVersion(String data) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ClientStatusForUpdateTargetVersion clientStatusForUpdateTargetVersion = (ClientStatusForUpdateTargetVersion)mapper.readValue(data, ClientStatusForUpdateTargetVersion.class);
		String[] clientIDs = clientStatusForUpdateTargetVersion.getClientIDs().split(",");
		for (String clientID : clientIDs) {
			ClientStatus clientStatus = clientStatusDao.selectById(clientID);
			clientStatus.setTargetVersion(clientStatusForUpdateTargetVersion.getTargetVersion());
			clientStatusDao.updateById(clientStatus);
		}
	}

	public void updateRenewalDate(String data) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		ClientStatusForUpdateRenewalDate clientStatusForUpdateRenewalDate = (ClientStatusForUpdateRenewalDate)mapper.readValue(data, ClientStatusForUpdateRenewalDate.class);
		String[] clientIDs = clientStatusForUpdateRenewalDate.getClientIDs().split(",");

		for (String clientID : clientIDs) {
			ClientStatus clientStatus = clientStatusDao.selectById(clientID);
			if("increaseOneMonth".equals(clientStatusForUpdateRenewalDate.getSettingType())) {
				clientStatus.setRenewalDate(Utils.addMonth(clientStatus.getRenewalDate(), 1));
			} else {
				clientStatus.setRenewalDate(Utils.string2Timestamp(clientStatusForUpdateRenewalDate.getRenewalDate()));
			}
			clientStatusDao.updateById(clientStatus);
		}
	}

	public void addClientStatus(ClientStatus clientStatus) {
		if (null != clientStatus.getClientID()) {
			updateClientStatus(clientStatus);
		} else {
			clientStatusDao.insert(clientStatus);
		}
	}

	public void saveClientStatuss(List<ClientStatus> clientStatuses) {
		if (CollectionUtils.isNotEmpty(clientStatuses)) {
			for (ClientStatus clientStatus : clientStatuses) {
				clientStatusDao.insert(clientStatus);
			}
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
		for (String clientID : clientIDs) {
			deleteClientStatus(clientID);
		}
	}

	public void resetRestartStatusForProcessing() {
		clientStatusDao.resetRestartStatusForProcessing();
	}

	public void changeMonitorType(String clientID) {
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

	public String getDownloadVNCInfo(String terminalType) throws Exception {
		List<ClientStatus> clientStatuses = clientStatusDao.searchClientStatusesOrByHost(terminalType,"yes");
		for (ClientStatus clientStatus : clientStatuses) {
			updateClientStatus(clientStatus);
			writeTxtFile(clientStatus);
		}
		String path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
		String zipFileName = path + "vnc.zip";
		ZipCompressor.zipMultiFile(path + "vnc/", zipFileName);
		return zipFileName;
	}

	public void writeTxtFile(ClientStatus clientStatus) throws Exception {
		RandomAccessFile mm = null;
		FileOutputStream o = null;
		try {
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
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (mm != null) {
				mm.close();
			}
		}
	}

	public void updateGroup(ClientStatus clientStatus) {
		ClientStatus oldClientStatus = clientStatusDao.selectById(clientStatus.getClientID());
		oldClientStatus.setGroup(clientStatus.getGroup());
		clientStatusDao.updateById(oldClientStatus);
	}

	public void updateOperationType(ClientStatus clientStatus) {
		ClientStatus oldClientStatus = clientStatusDao.selectById(clientStatus.getClientID());
		oldClientStatus.setOperationType(clientStatus.getOperationType());
		clientStatusDao.updateById(oldClientStatus);
	}

	public void updateUpgradeFailedReason(ClientStatus clientStatus) {
		ClientStatus oldClientStatus = clientStatusDao.selectById(clientStatus.getClientID());
		oldClientStatus.setUpgradeFailedReason(clientStatus.getUpgradeFailedReason());
		clientStatusDao.updateById(oldClientStatus);
	}
}
