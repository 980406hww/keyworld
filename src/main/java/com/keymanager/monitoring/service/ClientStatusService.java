package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.value.ClientStatusForUpdateTargetVersion;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
			ClientStatus clientStatus = clientStatusDao.selectById(clientID.substring(1, clientID.length() - 1));
			clientStatus.setClientID(clientID.substring(1, clientID.length() - 1));
			clientStatus.setTargetVersion(clientStatusForUpdateTargetVersion.getTargetVersion());
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
}
