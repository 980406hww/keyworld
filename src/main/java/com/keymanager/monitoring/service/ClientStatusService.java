package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.dao.ClientStatusDao;
import com.keymanager.monitoring.entity.ClientStatus;
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
			clientStatus.setGroup(clientStatus.getGroup());
			clientStatus.setOperationType(clientStatus.getOperationType());
			clientStatus.setPage(clientStatus.getPage());
			clientStatus.setPageSize(clientStatus.getPageSize() == null ? null : clientStatus.getPageSize());
			clientStatus.setZhanneiPercent(clientStatus.getZhanneiPercent() == null ? null : clientStatus.getZhanneiPercent());
			clientStatus.setKuaizhaoPercent(clientStatus.getKuaizhaoPercent() == null ? null : clientStatus.getKuaizhaoPercent());
			clientStatus.setBaiduSemPercent(clientStatus.getBaiduSemPercent() == null ? null : clientStatus.getBaiduSemPercent());
			clientStatus.setDisableVisitWebsite(clientStatus.getDisableVisitWebsite());
			clientStatus.setEntryPageMinCount(clientStatus.getEntryPageMinCount());
			clientStatus.setEntryPageMaxCount(clientStatus.getEntryPageMaxCount());
			clientStatus.setPageRemainMinTime(clientStatus.getPageRemainMinTime());
			clientStatus.setPageRemainMaxTime(clientStatus.getPageRemainMaxTime());
			clientStatus.setInputDelayMinTime(clientStatus.getInputDelayMinTime());
			clientStatus.setInputDelayMaxTime(clientStatus.getInputDelayMaxTime());
			clientStatus.setSlideDelayMinTime(clientStatus.getSlideDelayMinTime());
			clientStatus.setSlideDelayMaxTime(clientStatus.getSlideDelayMaxTime());
			clientStatus.setTitleRemainMinTime(clientStatus.getTitleRemainMinTime());
			clientStatus.setTitleRemainMaxTime(clientStatus.getTitleRemainMaxTime());
			clientStatus.setOptimizeKeywordCountPerIP(clientStatus.getOptimizeKeywordCountPerIP());
			clientStatus.setOneIPOneUser(clientStatus.getOneIPOneUser());
			clientStatus.setRandomlyClickNoResult(clientStatus.getRandomlyClickNoResult());
			clientStatus.setJustVisitSelfPage(clientStatus.getJustVisitSelfPage());
			clientStatus.setSleepPer2Words(clientStatus.getSleepPer2Words());
			clientStatus.setSupportPaste(clientStatus.getSupportPaste());
			clientStatus.setMoveRandomly(clientStatus.getMoveRandomly());
			clientStatus.setParentSearchEntry(clientStatus.getParentSearchEntry());
			clientStatus.setClearLocalStorage(clientStatus.getClearLocalStorage());
			clientStatus.setLessClickAtNight(clientStatus.getLessClickAtNight());
			clientStatus.setSameCityUser(clientStatus.getSameCityUser());
			clientStatus.setLocateTitlePosition(clientStatus.getLocateTitlePosition());
			clientStatus.setBaiduAllianceEntry(clientStatus.getBaiduAllianceEntry());
			clientStatus.setJustClickSpecifiedTitle(clientStatus.getJustClickSpecifiedTitle());
			clientStatus.setRandomlyClickMoreLink(clientStatus.getRandomlyClickMoreLink());
			clientStatus.setMoveUp20(clientStatus.getMoveUp20());
			clientStatus.setWaitTimeAfterOpenBaidu(clientStatus.getWaitTimeAfterOpenBaidu());
			clientStatus.setWaitTimeBeforeClick(clientStatus.getWaitTimeBeforeClick());
			clientStatus.setWaitTimeAfterClick(clientStatus.getWaitTimeAfterClick());
			clientStatus.setMaxUserCount(clientStatus.getMaxUserCount());
			clientStatus.setMultiBrowser(clientStatus.getMultiBrowser());
			clientStatus.setClearCookie(clientStatus.getClearCookie());
			clientStatus.setDragPercent(clientStatus.getDragPercent() == null ? null : clientStatus.getDragPercent());
			clientStatus.setAllowSwitchGroup(clientStatus.getAllowSwitchGroup());
			clientStatus.setDisableStatistics(clientStatus.getDisableStatistics());
			clientStatus.setHost(clientStatus.getHost());
			clientStatus.setPort(clientStatus.getPort());
			clientStatus.setUserName(clientStatus.getUserName());
			clientStatus.setPassword(clientStatus.getPassword());
			clientStatus.setVpsBackendSystemComputerID(clientStatus.getVpsBackendSystemComputerID());
			clientStatus.setVpsBackendSystemPassword(clientStatus.getVpsBackendSystemPassword());
			clientStatusDao.updateById(oldClientStatus);
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

	public ClientStatus getClientStatus(String clientID) {
		ClientStatus clientStatus = clientStatusDao.selectById(clientID);
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
}
