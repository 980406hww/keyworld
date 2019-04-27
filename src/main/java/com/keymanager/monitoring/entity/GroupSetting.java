package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.keymanager.util.Utils;
import org.hibernate.validator.constraints.NotBlank;

import java.sql.Timestamp;


@TableName(value = "t_client_status")
public class GroupSetting {

	private static final long serialVersionUID = -7590694637780491359L;
	@NotBlank
	@TableId(value = "fClientID")
	private String clientID;

	@TableField(value = "fTerminalType")
	private String terminalType;

	@TableField(value = "fClientIDPrefix")
	private String clientIDPrefix;

	@TableField(value = "fVersion")
	private String version;

	@TableField(value = "fTargetVersion")
	private String targetVersion;

	@TableField(value = "fPageNo")
	private int pageNo;

	@TableField(value = "fGroup", strategy = FieldStrategy.IGNORED)
	private String group;

	@TableField(value = "fContinuousFailCount")
	private int continuousFailCount;

	@TableField(value = "fCity")
	private String city;

	@TableField(value = "fOperationType", strategy = FieldStrategy.IGNORED)
	private String operationType;

	@TableField(value = "fPage")
	private int page;

	@TableField(value = "fPageSize")
	private Integer pageSize;

	@TableField(value = "fZhanneiPercent")
	private Integer zhanneiPercent;

	@TableField(value = "fKuaizhaoPercent")
	private Integer kuaizhaoPercent;

	@TableField(value = "fBaiduSemPercent")
	private Integer baiduSemPercent;

	@TableField(value = "fDragPercent")
	private Integer dragPercent;

	@TableField(value = "fZhanwaiPercent")
	private Integer zhanwaiPercent;

	@TableField(value = "fSpecialCharPercent")
	private Integer specialCharPercent;

	@TableField(value = "fMultiBrowser")
	private Integer multiBrowser;

	@TableField(value = "fClearCookie")
	private int clearCookie;

	@TableField(value = "fAllowSwitchGroup")
	private int allowSwitchGroup;

	@TableField(value = "fDisableStatistics")
	private int disableStatistics;

	@TableField(value = "fSwitchGroupName")
	private String switchGroupName;

	@TableField(value = "fHost")
	private String host;

	@TableField(value = "fPort")
	private String port;

	@TableField(value = "fUserName")
	private String userName;

	@TableField(value = "fPassword")
	private String password;

	@TableField(value = "fBroadbandAccount")
	private String broadbandAccount;

	@TableField(value = "fBroadbandPassword")
	private String broadbandPassword;

	@TableField(value = "fFreeSpace")
	private Double freeSpace;

	@TableField(value = "fTenMinsLastVisitTime")
	private Timestamp tenMinsLastVisitTime;

	@TableField(value = "fLastVisitTime")
	private Timestamp lastVisitTime;

	@TableField(value = "fLastSendNotificationTime")
	private Timestamp lastSendNotificationTime;

	@TableField(value = "fRestartCount")
	private int restartCount;

	@TableField(value = "fRestartStatus")
	private String restartStatus;

	@TableField(value = "fRestartTime")
	private Timestamp restartTime;

	@TableField(value = "fThreeMinsRestartTime")
	private Timestamp threeMinsRestartTime;

	@TableField(value = "fTenMinsRestartTime")
	private Timestamp tenMinsRestartTime;

	@TableField(value = "fVPSBackendSystemComputerID")
	private String vpsBackendSystemComputerID;

	@TableField(value = "fVPSBackendSystemPassword")
	private String vpsBackendSystemPassword;

	@TableField(value = "fRestartOrderingTime")
	private Timestamp restartOrderingTime;

	@TableField(value = "fOptimizationStartDate")
	private Timestamp optimizationStartDate;

	@TableField(value = "fOptimizationTotalCount")
	private int optimizationTotalCount;

	@TableField(value = "fOptimizationSucceedCount")
	private int optimizationSucceedCount;

	@TableField(value = "fEntryPageMinCount")
	private int entryPageMinCount;

	@TableField(value = "fEntryPageMaxCount")
	private int entryPageMaxCount;

	@TableField(value = "fDisableVisitWebsite")
	private int disableVisitWebsite;

	@TableField(value = "fPageRemainMinTime")
	private int pageRemainMinTime;

	@TableField(value = "fPageRemainMaxTime")
	private int pageRemainMaxTime;

	@TableField(value = "fInputDelayMinTime")
	private int inputDelayMinTime;

	@TableField(value = "fInputDelayMaxTime")
	private int inputDelayMaxTime;

	@TableField(value = "fSlideDelayMinTime")
	private int slideDelayMinTime;

	@TableField(value = "fSlideDelayMaxTime")
	private int slideDelayMaxTime;

	@TableField(value = "fTitleRemainMinTime")
	private int titleRemainMinTime;

	@TableField(value = "fTitleRemainMaxTime")
	private int titleRemainMaxTime;

	@TableField(value = "fOptimizeKeywordCountPerIP")
	private int optimizeKeywordCountPerIP;

	@TableField(value = "fOneIPOneUser")
	private int oneIPOneUser;

	@TableField(value = "fRandomlyClickNoResult")
	private int randomlyClickNoResult;

	@TableField(value = "fJustVisitSelfPage")
	private int justVisitSelfPage;

	@TableField(value = "fSleepPer2Words")
	private int sleepPer2Words;

	@TableField(value = "fSupportPaste")
	private int supportPaste;

	@TableField(value = "fMoveRandomly")
	private int moveRandomly;

	@TableField(value = "fParentSearchEntry")
	private int parentSearchEntry;

	@TableField(value = "fClearLocalStorage")
	private int clearLocalStorage;

	@TableField(value = "fLessClickAtNight")
	private int lessClickAtNight;

	@TableField(value = "fSameCityUser")
	private int sameCityUser;

	@TableField(value = "fLocateTitlePosition")
	private int locateTitlePosition;

	@TableField(value = "fBaiduAllianceEntry")
	private int baiduAllianceEntry;

	@TableField(value = "fJustClickSpecifiedTitle")
	private int justClickSpecifiedTitle;

	@TableField(value = "fRandomlyClickMoreLink")
	private int randomlyClickMoreLink;

	@TableField(value = "fMoveUp20")
	private int moveUp20;

	@TableField(value = "fWaitTimeAfterOpenBaidu")
	private int waitTimeAfterOpenBaidu;

	@TableField(value = "fWaitTimeBeforeClick")
	private int waitTimeBeforeClick;

	@TableField(value = "fWaitTimeAfterClick")
	private int waitTimeAfterClick;

	@TableField(value = "fMaxUserCount")
	private int maxUserCount;

	@TableField(value = "fOptimizeRelatedKeyword")
	private int optimizeRelatedKeyword;

	@TableField(value = "fRenewalDate")
	private Timestamp renewalDate;

	@TableField(value = "fUpgradeFailedReason", strategy = FieldStrategy.IGNORED)
	private String upgradeFailedReason;

	@TableField(value = "fStatus")
	private String status;

	@TableField(value = "fValid")
	private boolean valid;

	@TableField(exist=false)
	private boolean red;

	@TableField(exist=false)
	private boolean yellow;

	@TableField(exist=false)
	private boolean orange;

	@TableField(value = "fRemainingKeyword")
	private int remainingKeyword;

	@TableField(value = "fStartUpTime")
	private Timestamp startUpTime;

	@TableField(value = "fStartUpStatus")
	private String startUpStatus;

	@TableField(value = "fDownloadProgramType")
	private String downloadProgramType;

	@TableField(value = "fRunningProgramType")
	private String runningProgramType;
	
	@TableField(value = "fTargetVPSPassword")
	private String targetVPSPassword;

	@TableField(value = "fIdleStartTime")
	private Timestamp idleStartTime;

	@TableField(value = "fIdleTotalMinutes")
	private long idleTotalMinutes;

	@TableField(value = "fUpdateSettingTime")
	private Timestamp updateSettingTime;

	public String getClientID() {
		return clientID;
	}

	public void setClientID(String clientID) {
		this.clientID = clientID;
	}

	public String getTerminalType() {
		return terminalType;
	}

	public void setTerminalType(String terminalType) {
		this.terminalType = terminalType;
	}

	public String getClientIDPrefix() {
		return clientIDPrefix;
	}

	public void setClientIDPrefix(String clientIDPrefix) {
		this.clientIDPrefix = clientIDPrefix;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getTargetVersion() {
		return targetVersion;
	}

	public void setTargetVersion(String targetVersion) {
		this.targetVersion = targetVersion;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public int getContinuousFailCount() {
		return continuousFailCount;
	}

	public void setContinuousFailCount(int continuousFailCount) {
		this.continuousFailCount = continuousFailCount;
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

	public int getAllowSwitchGroup() {
		return allowSwitchGroup;
	}

	public void setAllowSwitchGroup(int allowSwitchGroup) {
		this.allowSwitchGroup = allowSwitchGroup;
	}

	public int getDisableStatistics() {
		return disableStatistics;
	}

	public void setDisableStatistics(int disableStatistics) {
		this.disableStatistics = disableStatistics;
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

	public Double getFreeSpace() {
		return freeSpace;
	}

	public void setFreeSpace(Double freeSpace) {
		this.freeSpace = freeSpace;
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

	public Timestamp getRestartTime() {
		return restartTime;
	}

	public void setRestartTime(Timestamp restartTime) {
		this.restartTime = restartTime;
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

	public Timestamp getRestartOrderingTime() {
		return restartOrderingTime;
	}

	public void setRestartOrderingTime(Timestamp restartOrderingTime) {
		this.restartOrderingTime = restartOrderingTime;
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

	public Timestamp getRenewalDate() {
		return renewalDate;
	}

	public void setRenewalDate(Timestamp renewalDate) {
		this.renewalDate = renewalDate;
	}

	public String getUpgradeFailedReason() {
		return upgradeFailedReason;
	}

	public void setUpgradeFailedReason(String upgradeFailedReason) {
		this.upgradeFailedReason = upgradeFailedReason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean getValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public boolean getRed(){
		return (Utils.addMinutes(this.lastVisitTime, (10 > (this.getPageNo() * 3) ? (10 + 5) : (this.getPageNo() * 3 + 5)))
				.compareTo(Utils.getCurrentTimestamp()) < 0);
	}

	public boolean getYellow(){
		Timestamp time = Utils.addMinutes(this.lastVisitTime, (10 > (this.getPageNo() * 3) ? 10 : (this.getPageNo() * 3)));
		return time.compareTo(Utils.getCurrentTimestamp()) < 0 && (Utils.addMinutes(time, 5)).compareTo(Utils.getCurrentTimestamp()) > 0;
	}

	public boolean getOrange() {
		return this.getContinuousFailCount() > 5;
	}

	public void setOrange(boolean orange) {
		this.orange = orange;
	}

	public void setRed(boolean red) {
		this.red = red;
	}

	public void setYellow(boolean yellow) {
		this.yellow = yellow;
	}

	public String getBroadbandAccount() {
		return broadbandAccount;
	}

	public void setBroadbandAccount(String broadbandAccount) {
		this.broadbandAccount = broadbandAccount;
	}

	public String getBroadbandPassword() {
		return broadbandPassword;
	}

	public void setBroadbandPassword(String broadbandPassword) {
		this.broadbandPassword = broadbandPassword;
	}

	public Integer getZhanwaiPercent() {
		return zhanwaiPercent;
	}

	public void setZhanwaiPercent(Integer zhanwaiPercent) {
		this.zhanwaiPercent = zhanwaiPercent;
	}

	public String getSwitchGroupName() {
		return switchGroupName;
	}

	public void setSwitchGroupName(String switchGroupName) {
		this.switchGroupName = switchGroupName;
	}

	public Timestamp getTenMinsLastVisitTime() {
		return tenMinsLastVisitTime;
	}

	public void setTenMinsLastVisitTime(Timestamp tenMinsLastVisitTime) {
		this.tenMinsLastVisitTime = tenMinsLastVisitTime;
	}

	public Timestamp getThreeMinsRestartTime() {
		return threeMinsRestartTime;
	}

	public void setThreeMinsRestartTime(Timestamp threeMinsRestartTime) {
		this.threeMinsRestartTime = threeMinsRestartTime;
	}

	public Timestamp getTenMinsRestartTime() {
		return tenMinsRestartTime;
	}

	public void setTenMinsRestartTime(Timestamp tenMinsRestartTime) {
		this.tenMinsRestartTime = tenMinsRestartTime;
	}

	public int getRemainingKeyword() {
		return remainingKeyword;
	}

	public void setRemainingKeyword(int remainingKeyword) {
		this.remainingKeyword = remainingKeyword;
	}

	public int getOptimizeRelatedKeyword() {
		return optimizeRelatedKeyword;
	}

	public void setOptimizeRelatedKeyword(int optimizeRelatedKeyword) {
		this.optimizeRelatedKeyword = optimizeRelatedKeyword;
	}

	public Timestamp getStartUpTime() {
		return startUpTime;
	}

	public void setStartUpTime(Timestamp startUpTime) {
		this.startUpTime = startUpTime;
	}

	public String getStartUpStatus() {
		return startUpStatus;
	}

	public void setStartUpStatus(String startUpStatus) {
		this.startUpStatus = startUpStatus;
	}

	public String getDownloadProgramType() {
		return downloadProgramType;
	}

	public void setDownloadProgramType(String downloadProgramType) {
		this.downloadProgramType = downloadProgramType;
	}

	public Integer getSpecialCharPercent() {
		return specialCharPercent;
	}

	public void setSpecialCharPercent(Integer specialCharPercent) {
		this.specialCharPercent = specialCharPercent;
	}

	public String getRunningProgramType() {
		return runningProgramType;
	}

	public void setRunningProgramType(String runningProgramType) {
		this.runningProgramType = runningProgramType;
	}

	public String getTargetVPSPassword() {
		return targetVPSPassword;
	}

	public void setTargetVPSPassword(String targetVPSPassword) {
		this.targetVPSPassword = targetVPSPassword;
	}

	public Timestamp getIdleStartTime() {
		return idleStartTime;
	}

	public void setIdleStartTime(Timestamp idleStartTime) {
		this.idleStartTime = idleStartTime;
	}

	public long getIdleTotalMinutes() {
		return idleTotalMinutes;
	}

	public void setIdleTotalMinutes(long idleTotalMinutes) {
		this.idleTotalMinutes = idleTotalMinutes;
	}

	public Timestamp getUpdateSettingTime() {
		return updateSettingTime;
	}

	public void setUpdateSettingTime(Timestamp updateSettingTime) {
		this.updateSettingTime = updateSettingTime;
	}
}
