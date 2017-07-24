package com.keymanager.value;

import com.keymanager.util.Utils;

import java.sql.Timestamp;

public class ClientStatusVO {
	private String clientID;
	private String clientIDPrefix;
	private String terminalType;
	private String group;
	private String version;
	private String targetVersion;
	private int pageNo;
	private int continuousFailCount;
	private String city;
	private String operationType;
	private int page;
	private Integer pageSize;
	private Integer zhanneiPercent;
	private Integer kuaizhaoPercent;
	private Integer baiduSemPercent;
	private Integer dragPercent;
	private Integer multiBrowser;
	private int clearCookie;
	private int allowSwitchGroup;
	private int disableStatistics;
	private String host;
	private String port;
	private String userName;
	private String password;
	private Double freeSpace;
	private Timestamp lastVisitTime;
	private Timestamp lastSendNotificationTime;
	private int restartCount;
	private String restartStatus;
	private Timestamp restartTime;
	private String vpsBackendSystemComputerID;
	private String vpsBackendSystemPassword;
	private Timestamp restartOrderingTime;
	private Timestamp optimizationStartDate;
	private int optimizationTotalCount;
	private int optimizationSucceedCount;
	private int entryPageMinCount;
	private int entryPageMaxCount;
	private int disableVisitWebsite;
	private int pageRemainMinTime;
	private int pageRemainMaxTime;
	private int inputDelayMinTime;
	private int inputDelayMaxTime;
	private int slideDelayMinTime;
	private int slideDelayMaxTime;
	private int titleRemainMinTime;
	private int titleRemainMaxTime;
	private int optimizeKeywordCountPerIP;
	private int oneIPOneUser;
	private int randomlyClickNoResult;
	private int justVisitSelfPage;
	private int sleepPer2Words;
	private int supportPaste;
	private int moveRandomly;
	private int parentSearchEntry;
	private int clearLocalStorage;
	private int lessClickAtNight;
	private int sameCityUser;
	private int locateTitlePosition;
	private int baiduAllianceEntry;
	private int justClickSpecifiedTitle;
	private int randomlyClickMoreLink;
	private int moveUp20;
	private int waitTimeAfterOpenBaidu;
	private int waitTimeBeforeClick;
	private int waitTimeAfterClick;
	private int maxUserCount;
	private Timestamp renewalDate;
	private String upgradeFailedReason;
	private String status;
	private boolean valid;

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public Timestamp getLastVisitTime() {
		return lastVisitTime;
	}

	public void setLastVisitTime(Timestamp lastVisitTime) {
		this.lastVisitTime = lastVisitTime;
	}

	public Timestamp getLastSendNotificationTime() {
		return lastSendNotificationTime;
	}

	public void setLastSendNotificationTime(Timestamp lastSendNotificationTime) {
		this.lastSendNotificationTime = lastSendNotificationTime;
	}
	
	public boolean isRed(){
		return (this.getContinuousFailCount() > 5) || (Utils.addMinutes(this.lastVisitTime, (10 > (this.getPageNo() * 3) ? (10 + 5) : (this.getPageNo() * 3 + 5)))
				.compareTo(Utils.getCurrentTimestamp()) <	0);
	}

	public boolean isYellow(){
		Timestamp time = Utils.addMinutes(this.lastVisitTime, (10 > (this.getPageNo() * 3) ? 10 : (this.getPageNo() * 3)));
		return time.compareTo(Utils.getCurrentTimestamp()) < 0 && (Utils.addMinutes(time, 5)).compareTo(Utils.getCurrentTimestamp()) > 0;
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public Double getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(Double freeSpace) {
		this.freeSpace = freeSpace;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public Integer getMultiBrowser() {
		return multiBrowser;
	}

	public void setMultiBrowser(Integer multiBrowser) {
		this.multiBrowser = multiBrowser;
	}

	public int getClearCookie() {
		return clearCookie;
	}

	public void setClearCookie(int clearCookie) {
		this.clearCookie = clearCookie;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	public int getAllowSwitchGroup() {
		return allowSwitchGroup;
	}

	public void setAllowSwitchGroup(int allowSwitchGroup) {
		this.allowSwitchGroup = allowSwitchGroup;
	}

	public int getRestartCount() {
		return restartCount;
	}

	public void setRestartCount(int restartCount) {
		this.restartCount = restartCount;
	}

	public String getRestartStatus() {
		return restartStatus;
	}

	public void setRestartStatus(String restartStatus) {
		this.restartStatus = restartStatus;
	}

	public String getVpsBackendSystemComputerID() {
		return vpsBackendSystemComputerID;
	}

	public void setVpsBackendSystemComputerID(String vpsBackendSystemComputerID) {
		this.vpsBackendSystemComputerID = vpsBackendSystemComputerID;
	}

	public String getVpsBackendSystemPassword() {
		return vpsBackendSystemPassword;
	}

	public void setVpsBackendSystemPassword(String vpsBackendSystemPassword) {
		this.vpsBackendSystemPassword = vpsBackendSystemPassword;
	}

	public String getClientIDPrefix() {
		return clientIDPrefix;
	}

	public void setClientIDPrefix(String clientIDPrefix) {
		this.clientIDPrefix = clientIDPrefix;
	}

	public Timestamp getRestartTime() {
		return restartTime;
	}

	public void setRestartTime(Timestamp restartTime) {
		this.restartTime = restartTime;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getContinuousFailCount() {
		return continuousFailCount;
	}

	public void setContinuousFailCount(int continuousFailCount) {
		this.continuousFailCount = continuousFailCount;
	}

	public Timestamp getRestartOrderingTime() {
		return restartOrderingTime;
	}

	public void setRestartOrderingTime(Timestamp restartOrderingTime) {
		this.restartOrderingTime = restartOrderingTime;
	}

	public Timestamp getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Timestamp renewalDate) {
		this.renewalDate = renewalDate;
	}

	public Timestamp getOptimizationStartDate() {
		return optimizationStartDate;
	}

	public void setOptimizationStartDate(Timestamp optimizationStartDate) {
		this.optimizationStartDate = optimizationStartDate;
	}

	public int getOptimizationTotalCount() {
		return optimizationTotalCount;
	}

	public void setOptimizationTotalCount(int optimizationTotalCount) {
		this.optimizationTotalCount = optimizationTotalCount;
	}

	public int getOptimizationSucceedCount() {
		return optimizationSucceedCount;
	}

	public void setOptimizationSucceedCount(int optimizationSucceedCount) {
		this.optimizationSucceedCount = optimizationSucceedCount;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getZhanneiPercent() {
		return zhanneiPercent;
	}

	public void setZhanneiPercent(Integer zhanneiPercent) {
		this.zhanneiPercent = zhanneiPercent;
	}

	public Integer getKuaizhaoPercent() {
		return kuaizhaoPercent;
	}

	public void setKuaizhaoPercent(Integer kuaizhaoPercent) {
		this.kuaizhaoPercent = kuaizhaoPercent;
	}

	public Integer getBaiduSemPercent() {
		return baiduSemPercent;
	}

	public void setBaiduSemPercent(Integer baiduSemPercent) {
		this.baiduSemPercent = baiduSemPercent;
	}

	public Integer getDragPercent() {
		return dragPercent;
	}

	public void setDragPercent(Integer dragPercent) {
		this.dragPercent = dragPercent;
	}

	public int getDisableStatistics() {
		return disableStatistics;
	}

	public void setDisableStatistics(int disableStatistics) {
		this.disableStatistics = disableStatistics;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getEntryPageMinCount() {
		return entryPageMinCount;
	}

	public void setEntryPageMinCount(int entryPageMinCount) {
		this.entryPageMinCount = entryPageMinCount;
	}

	public int getEntryPageMaxCount() {
		return entryPageMaxCount;
	}

	public void setEntryPageMaxCount(int entryPageMaxCount) {
		this.entryPageMaxCount = entryPageMaxCount;
	}

	public int getDisableVisitWebsite() {
		return disableVisitWebsite;
	}

	public void setDisableVisitWebsite(int disableVisitWebsite) {
		this.disableVisitWebsite = disableVisitWebsite;
	}

	public int getPageRemainMinTime() {
		return pageRemainMinTime;
	}

	public void setPageRemainMinTime(int pageRemainMinTime) {
		this.pageRemainMinTime = pageRemainMinTime;
	}

	public int getPageRemainMaxTime() {
		return pageRemainMaxTime;
	}

	public void setPageRemainMaxTime(int pageRemainMaxTime) {
		this.pageRemainMaxTime = pageRemainMaxTime;
	}

	public int getInputDelayMinTime() {
		return inputDelayMinTime;
	}

	public void setInputDelayMinTime(int inputDelayMinTime) {
		this.inputDelayMinTime = inputDelayMinTime;
	}

	public int getInputDelayMaxTime() {
		return inputDelayMaxTime;
	}

	public void setInputDelayMaxTime(int inputDelayMaxTime) {
		this.inputDelayMaxTime = inputDelayMaxTime;
	}

	public int getSlideDelayMinTime() {
		return slideDelayMinTime;
	}

	public void setSlideDelayMinTime(int slideDelayMinTime) {
		this.slideDelayMinTime = slideDelayMinTime;
	}

	public int getSlideDelayMaxTime() {
		return slideDelayMaxTime;
	}

	public void setSlideDelayMaxTime(int slideDelayMaxTime) {
		this.slideDelayMaxTime = slideDelayMaxTime;
	}

	public int getTitleRemainMinTime() {
		return titleRemainMinTime;
	}

	public void setTitleRemainMinTime(int titleRemainMinTime) {
		this.titleRemainMinTime = titleRemainMinTime;
	}

	public int getTitleRemainMaxTime() {
		return titleRemainMaxTime;
	}

	public void setTitleRemainMaxTime(int titleRemainMaxTime) {
		this.titleRemainMaxTime = titleRemainMaxTime;
	}

	public int getOptimizeKeywordCountPerIP() {
		return optimizeKeywordCountPerIP;
	}

	public void setOptimizeKeywordCountPerIP(int optimizeKeywordCountPerIP) {
		this.optimizeKeywordCountPerIP = optimizeKeywordCountPerIP;
	}

	public int getOneIPOneUser() {
		return oneIPOneUser;
	}

	public void setOneIPOneUser(int oneIPOneUser) {
		this.oneIPOneUser = oneIPOneUser;
	}

	public int getRandomlyClickNoResult() {
		return randomlyClickNoResult;
	}

	public void setRandomlyClickNoResult(int randomlyClickNoResult) {
		this.randomlyClickNoResult = randomlyClickNoResult;
	}

	public int getJustVisitSelfPage() {
		return justVisitSelfPage;
	}

	public void setJustVisitSelfPage(int justVisitSelfPage) {
		this.justVisitSelfPage = justVisitSelfPage;
	}

	public int getSleepPer2Words() {
		return sleepPer2Words;
	}

	public void setSleepPer2Words(int sleepPer2Words) {
		this.sleepPer2Words = sleepPer2Words;
	}

	public int getSupportPaste() {
		return supportPaste;
	}

	public void setSupportPaste(int supportPaste) {
		this.supportPaste = supportPaste;
	}

	public int getMoveRandomly() {
		return moveRandomly;
	}

	public void setMoveRandomly(int moveRandomly) {
		this.moveRandomly = moveRandomly;
	}

	public int getParentSearchEntry() {
		return parentSearchEntry;
	}

	public void setParentSearchEntry(int parentSearchEntry) {
		this.parentSearchEntry = parentSearchEntry;
	}

	public int getClearLocalStorage() {
		return clearLocalStorage;
	}

	public void setClearLocalStorage(int clearLocalStorage) {
		this.clearLocalStorage = clearLocalStorage;
	}

	public int getLessClickAtNight() {
		return lessClickAtNight;
	}

	public void setLessClickAtNight(int lessClickAtNight) {
		this.lessClickAtNight = lessClickAtNight;
	}

	public int getSameCityUser() {
		return sameCityUser;
	}

	public void setSameCityUser(int sameCityUser) {
		this.sameCityUser = sameCityUser;
	}

	public int getLocateTitlePosition() {
		return locateTitlePosition;
	}

	public void setLocateTitlePosition(int locateTitlePosition) {
		this.locateTitlePosition = locateTitlePosition;
	}

	public int getBaiduAllianceEntry() {
		return baiduAllianceEntry;
	}

	public void setBaiduAllianceEntry(int baiduAllianceEntry) {
		this.baiduAllianceEntry = baiduAllianceEntry;
	}

	public int getJustClickSpecifiedTitle() {
		return justClickSpecifiedTitle;
	}

	public void setJustClickSpecifiedTitle(int justClickSpecifiedTitle) {
		this.justClickSpecifiedTitle = justClickSpecifiedTitle;
	}

	public int getRandomlyClickMoreLink() {
		return randomlyClickMoreLink;
	}

	public void setRandomlyClickMoreLink(int randomlyClickMoreLink) {
		this.randomlyClickMoreLink = randomlyClickMoreLink;
	}

	public int getMoveUp20() {
		return moveUp20;
	}

	public void setMoveUp20(int moveUp20) {
		this.moveUp20 = moveUp20;
	}

	public int getWaitTimeAfterOpenBaidu() {
		return waitTimeAfterOpenBaidu;
	}

	public void setWaitTimeAfterOpenBaidu(int waitTimeAfterOpenBaidu) {
		this.waitTimeAfterOpenBaidu = waitTimeAfterOpenBaidu;
	}

	public int getWaitTimeBeforeClick() {
		return waitTimeBeforeClick;
	}

	public void setWaitTimeBeforeClick(int waitTimeBeforeClick) {
		this.waitTimeBeforeClick = waitTimeBeforeClick;
	}

	public int getWaitTimeAfterClick() {
		return waitTimeAfterClick;
	}

	public void setWaitTimeAfterClick(int waitTimeAfterClick) {
		this.waitTimeAfterClick = waitTimeAfterClick;
	}

	public int getMaxUserCount() {
		return maxUserCount;
	}

	public void setMaxUserCount(int maxUserCount) {
		this.maxUserCount = maxUserCount;
	}

	public String getUpgradeFailedReason() {
		return upgradeFailedReason;
	}

	public void setUpgradeFailedReason(String upgradeFailedReason) {
		this.upgradeFailedReason = upgradeFailedReason;
	}
}
