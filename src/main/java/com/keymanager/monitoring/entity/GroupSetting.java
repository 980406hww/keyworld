package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;

@TableName(value = "t_group_setting")
public class GroupSetting extends BaseEntity {

	@TableField(value = "fGroupUuid")
	private long groupUuid;

	@TableField(value = "fOperationType", strategy = FieldStrategy.IGNORED)
	private String operationType;

	@TableField(value = "fMachineUsedPercent")
	private int machineUsedPercent;

	@TableField(value = "fPage")
	private int page;

	@TableField(value = "fPageSize")
	private Integer pageSize;

	@TableField(value = "fZhanneiPercent")
	private Integer zhanneiPercent;

	@TableField(value = "fZhanwaiPercent")
	private Integer zhanwaiPercent;

	@TableField(value = "fKuaizhaoPercent")
	private Integer kuaizhaoPercent;

	@TableField(value = "fBaiduSemPercent")
	private Integer baiduSemPercent;

	@TableField(value = "fDragPercent")
	private Integer dragPercent;

	@TableField(value = "fSpecialCharPercent")
	private Integer specialCharPercent;

	@TableField(value = "fMultiBrowser")
	private Integer multiBrowser;

	@TableField(value = "fClearCookie")
	private int clearCookie;

	@TableField(value = "fDisableStatistics")
	private int disableStatistics;

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

	@TableField(exist = false)
	private boolean red;

	@TableField(exist = false)
	private boolean yellow;

	@TableField(exist = false)
	private boolean orange;

	@TableField(exist = false)
	private int remainingAccount;

	@TableField(exist = false)
	private int maxInvalidCount;

	public long getGroupUuid () {
		return groupUuid;
	}

	public void setGroupUuid (long groupUuid) {
		this.groupUuid = groupUuid;
	}

	public String getOperationType () {
		return operationType;
	}

	public void setOperationType (String operationType) {
		this.operationType = operationType;
	}

	public int getMachineUsedPercent () {
		return machineUsedPercent;
	}

	public void setMachineUsedPercent (int machineUsedPercent) {
		this.machineUsedPercent = machineUsedPercent;
	}

	public int getPage () {
		return page;
	}

	public void setPage (int page) {
		this.page = page;
	}

	public Integer getPageSize () {
		return pageSize;
	}

	public void setPageSize (Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getZhanneiPercent () {
		return zhanneiPercent;
	}

	public void setZhanneiPercent (Integer zhanneiPercent) {
		this.zhanneiPercent = zhanneiPercent;
	}

	public Integer getZhanwaiPercent () {
		return zhanwaiPercent;
	}

	public void setZhanwaiPercent (Integer zhanwaiPercent) {
		this.zhanwaiPercent = zhanwaiPercent;
	}

	public Integer getKuaizhaoPercent () {
		return kuaizhaoPercent;
	}

	public void setKuaizhaoPercent (Integer kuaizhaoPercent) {
		this.kuaizhaoPercent = kuaizhaoPercent;
	}

	public Integer getBaiduSemPercent () {
		return baiduSemPercent;
	}

	public void setBaiduSemPercent (Integer baiduSemPercent) {
		this.baiduSemPercent = baiduSemPercent;
	}

	public Integer getDragPercent () {
		return dragPercent;
	}

	public void setDragPercent (Integer dragPercent) {
		this.dragPercent = dragPercent;
	}

	public Integer getSpecialCharPercent () {
		return specialCharPercent;
	}

	public void setSpecialCharPercent (Integer specialCharPercent) {
		this.specialCharPercent = specialCharPercent;
	}

	public Integer getMultiBrowser () {
		return multiBrowser;
	}

	public void setMultiBrowser (Integer multiBrowser) {
		this.multiBrowser = multiBrowser;
	}

	public int getClearCookie () {
		return clearCookie;
	}

	public void setClearCookie (int clearCookie) {
		this.clearCookie = clearCookie;
	}

	public int getDisableStatistics () {
		return disableStatistics;
	}

	public void setDisableStatistics (int disableStatistics) {
		this.disableStatistics = disableStatistics;
	}

	public int getEntryPageMinCount () {
		return entryPageMinCount;
	}

	public void setEntryPageMinCount (int entryPageMinCount) {
		this.entryPageMinCount = entryPageMinCount;
	}

	public int getEntryPageMaxCount () {
		return entryPageMaxCount;
	}

	public void setEntryPageMaxCount (int entryPageMaxCount) {
		this.entryPageMaxCount = entryPageMaxCount;
	}

	public int getDisableVisitWebsite () {
		return disableVisitWebsite;
	}

	public void setDisableVisitWebsite (int disableVisitWebsite) {
		this.disableVisitWebsite = disableVisitWebsite;
	}

	public int getPageRemainMinTime () {
		return pageRemainMinTime;
	}

	public void setPageRemainMinTime (int pageRemainMinTime) {
		this.pageRemainMinTime = pageRemainMinTime;
	}

	public int getPageRemainMaxTime () {
		return pageRemainMaxTime;
	}

	public void setPageRemainMaxTime (int pageRemainMaxTime) {
		this.pageRemainMaxTime = pageRemainMaxTime;
	}

	public int getInputDelayMinTime () {
		return inputDelayMinTime;
	}

	public void setInputDelayMinTime (int inputDelayMinTime) {
		this.inputDelayMinTime = inputDelayMinTime;
	}

	public int getInputDelayMaxTime () {
		return inputDelayMaxTime;
	}

	public void setInputDelayMaxTime (int inputDelayMaxTime) {
		this.inputDelayMaxTime = inputDelayMaxTime;
	}

	public int getSlideDelayMinTime () {
		return slideDelayMinTime;
	}

	public void setSlideDelayMinTime (int slideDelayMinTime) {
		this.slideDelayMinTime = slideDelayMinTime;
	}

	public int getSlideDelayMaxTime () {
		return slideDelayMaxTime;
	}

	public void setSlideDelayMaxTime (int slideDelayMaxTime) {
		this.slideDelayMaxTime = slideDelayMaxTime;
	}

	public int getTitleRemainMinTime () {
		return titleRemainMinTime;
	}

	public void setTitleRemainMinTime (int titleRemainMinTime) {
		this.titleRemainMinTime = titleRemainMinTime;
	}

	public int getTitleRemainMaxTime () {
		return titleRemainMaxTime;
	}

	public void setTitleRemainMaxTime (int titleRemainMaxTime) {
		this.titleRemainMaxTime = titleRemainMaxTime;
	}

	public int getOptimizeKeywordCountPerIP () {
		return optimizeKeywordCountPerIP;
	}

	public void setOptimizeKeywordCountPerIP (int optimizeKeywordCountPerIP) {
		this.optimizeKeywordCountPerIP = optimizeKeywordCountPerIP;
	}

	public int getOneIPOneUser () {
		return oneIPOneUser;
	}

	public void setOneIPOneUser (int oneIPOneUser) {
		this.oneIPOneUser = oneIPOneUser;
	}

	public int getRandomlyClickNoResult () {
		return randomlyClickNoResult;
	}

	public void setRandomlyClickNoResult (int randomlyClickNoResult) {
		this.randomlyClickNoResult = randomlyClickNoResult;
	}

	public int getJustVisitSelfPage () {
		return justVisitSelfPage;
	}

	public void setJustVisitSelfPage (int justVisitSelfPage) {
		this.justVisitSelfPage = justVisitSelfPage;
	}

	public int getSleepPer2Words () {
		return sleepPer2Words;
	}

	public void setSleepPer2Words (int sleepPer2Words) {
		this.sleepPer2Words = sleepPer2Words;
	}

	public int getSupportPaste () {
		return supportPaste;
	}

	public void setSupportPaste (int supportPaste) {
		this.supportPaste = supportPaste;
	}

	public int getMoveRandomly () {
		return moveRandomly;
	}

	public void setMoveRandomly (int moveRandomly) {
		this.moveRandomly = moveRandomly;
	}

	public int getParentSearchEntry () {
		return parentSearchEntry;
	}

	public void setParentSearchEntry (int parentSearchEntry) {
		this.parentSearchEntry = parentSearchEntry;
	}

	public int getClearLocalStorage () {
		return clearLocalStorage;
	}

	public void setClearLocalStorage (int clearLocalStorage) {
		this.clearLocalStorage = clearLocalStorage;
	}

	public int getLessClickAtNight () {
		return lessClickAtNight;
	}

	public void setLessClickAtNight (int lessClickAtNight) {
		this.lessClickAtNight = lessClickAtNight;
	}

	public int getSameCityUser () {
		return sameCityUser;
	}

	public void setSameCityUser (int sameCityUser) {
		this.sameCityUser = sameCityUser;
	}

	public int getLocateTitlePosition () {
		return locateTitlePosition;
	}

	public void setLocateTitlePosition (int locateTitlePosition) {
		this.locateTitlePosition = locateTitlePosition;
	}

	public int getBaiduAllianceEntry () {
		return baiduAllianceEntry;
	}

	public void setBaiduAllianceEntry (int baiduAllianceEntry) {
		this.baiduAllianceEntry = baiduAllianceEntry;
	}

	public int getJustClickSpecifiedTitle () {
		return justClickSpecifiedTitle;
	}

	public void setJustClickSpecifiedTitle (int justClickSpecifiedTitle) {
		this.justClickSpecifiedTitle = justClickSpecifiedTitle;
	}

	public int getRandomlyClickMoreLink () {
		return randomlyClickMoreLink;
	}

	public void setRandomlyClickMoreLink (int randomlyClickMoreLink) {
		this.randomlyClickMoreLink = randomlyClickMoreLink;
	}

	public int getMoveUp20 () {
		return moveUp20;
	}

	public void setMoveUp20 (int moveUp20) {
		this.moveUp20 = moveUp20;
	}

	public int getWaitTimeAfterOpenBaidu () {
		return waitTimeAfterOpenBaidu;
	}

	public void setWaitTimeAfterOpenBaidu (int waitTimeAfterOpenBaidu) {
		this.waitTimeAfterOpenBaidu = waitTimeAfterOpenBaidu;
	}

	public int getWaitTimeBeforeClick () {
		return waitTimeBeforeClick;
	}

	public void setWaitTimeBeforeClick (int waitTimeBeforeClick) {
		this.waitTimeBeforeClick = waitTimeBeforeClick;
	}

	public int getWaitTimeAfterClick () {
		return waitTimeAfterClick;
	}

	public void setWaitTimeAfterClick (int waitTimeAfterClick) {
		this.waitTimeAfterClick = waitTimeAfterClick;
	}

	public int getMaxUserCount () {
		return maxUserCount;
	}

	public void setMaxUserCount (int maxUserCount) {
		this.maxUserCount = maxUserCount;
	}

	public int getOptimizeRelatedKeyword () {
		return optimizeRelatedKeyword;
	}

	public void setOptimizeRelatedKeyword (int optimizeRelatedKeyword) {
		this.optimizeRelatedKeyword = optimizeRelatedKeyword;
	}

	public boolean isRed () {
		return red;
	}

	public void setRed (boolean red) {
		this.red = red;
	}

	public boolean isYellow () {
		return yellow;
	}

	public void setYellow (boolean yellow) {
		this.yellow = yellow;
	}

	public boolean isOrange () {
		return orange;
	}

	public void setOrange (boolean orange) {
		this.orange = orange;
	}

	public int getRemainingAccount () {
		return remainingAccount;
	}

	public void setRemainingAccount (int remainingAccount) {
		this.remainingAccount = remainingAccount;
	}

    public int getMaxInvalidCount() {
        return maxInvalidCount;
    }

    public void setMaxInvalidCount(int maxInvalidCount) {
        this.maxInvalidCount = maxInvalidCount;
    }
}