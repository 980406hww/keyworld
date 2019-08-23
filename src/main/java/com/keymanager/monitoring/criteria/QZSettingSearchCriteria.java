package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.QZCategoryTag;

import java.util.List;

public class QZSettingSearchCriteria{
    private String loginName;
    private String customerUuid;
    //uuid串，批量操作时用
    private String customerUuids;
    private String customerInfo;
    private Long userInfoID;
    private Long organizationID;
    private String domain;
    private String searchEngine;
    private String group;
    private String updateStatus;
    private Integer dateRangeType;
    private Integer status;
    private Integer renewalStatus;
    private Integer checkStatus;
    private String terminalType;
    private String categoryTag;
    private Integer openDialogStatus;
    private String operationType;
    private String createTime;
    private String createTimePrefix;
    private Boolean hasMonitor;
    private Boolean hasReady;
    private String standardSpecies;
    private Integer optimizationType;
    private boolean resetPagingParam;
    private Boolean autoCrawlKeywordFlag;
    private List<QZCategoryTag> targetQZCategoryTags;

    public String getCustomerUuids() {
        return customerUuids;
    }

    public void setCustomerUuids(String customerUuids) {
        this.customerUuids = customerUuids;
    }

    public List<QZCategoryTag> getTargetQZCategoryTags() {
        return targetQZCategoryTags;
    }

    public void setTargetQZCategoryTags(List<QZCategoryTag> targetQZCategoryTags) {
        this.targetQZCategoryTags = targetQZCategoryTags;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public Long getUserInfoID () {
        return userInfoID;
    }

    public void setUserInfoID (Long userInfoID) {
        this.userInfoID = userInfoID;
    }

    public Long getOrganizationID () {
        return organizationID;
    }

    public void setOrganizationID (Long organizationID) {
        this.organizationID = organizationID;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public Integer getDateRangeType() {
        return dateRangeType;
    }

    public void setDateRangeType(Integer dateRangeType) {
        this.dateRangeType = dateRangeType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCheckStatus () {
        return checkStatus;
    }

    public void setCheckStatus (Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public String getCategoryTag () {
        return categoryTag;
    }

    public void setCategoryTag (String categoryTag) {
        this.categoryTag = categoryTag;
    }

    public Integer getOpenDialogStatus () {
        return openDialogStatus;
    }

    public void setOpenDialogStatus (Integer openDialogStatus) {
        this.openDialogStatus = openDialogStatus;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimePrefix() {
        return createTimePrefix;
    }

    public void setCreateTimePrefix(String createTimePrefix) {
        this.createTimePrefix = createTimePrefix;
    }

    public Boolean getHasMonitor () {
        return hasMonitor;
    }

    public void setHasMonitor (Boolean hasMonitor) {
        this.hasMonitor = hasMonitor;
    }

    public Boolean getHasReady () {
        return hasReady;
    }

    public void setHasReady (Boolean hasReady) {
        this.hasReady = hasReady;
    }

    public String getStandardSpecies () {
        return standardSpecies;
    }

    public void setStandardSpecies (String standardSpecies) {
        this.standardSpecies = standardSpecies;
    }

    public Integer getOptimizationType () {
        return optimizationType;
    }

    public void setOptimizationType (Integer optimizationType) {
        this.optimizationType = optimizationType;
    }

    public boolean getResetPagingParam() {
        return resetPagingParam;
    }

    public void setResetPagingParam(boolean resetPagingParam) {
        this.resetPagingParam = resetPagingParam;
    }

    public Integer getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(Integer renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public Boolean getAutoCrawlKeywordFlag() {
        return autoCrawlKeywordFlag;
    }

    public void setAutoCrawlKeywordFlag(Boolean autoCrawlKeywordFlag) {
        this.autoCrawlKeywordFlag = autoCrawlKeywordFlag;
    }
}
