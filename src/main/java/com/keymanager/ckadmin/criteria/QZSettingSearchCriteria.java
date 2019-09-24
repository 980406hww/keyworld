package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

public class QZSettingSearchCriteria extends BaseCriteria {

    private String loginName;
    private String customerUuid;
    private String customerUuids;
    private String customerInfo;
    private Long userInfoID;
    private Long organizationID;
    private String domain;
    private String searchEngine;
    private String group;
    private String updateStatus;
    private Integer dateRangeType;
    private Integer renewalStatus;
    private Integer checkStatus;
    private String terminalType;
    private String categoryTag;
    private String createTime;
    private String createTimePrefix;
    private String standardSpecies;
    private Integer optimizationType;
    private Boolean autoCrawlKeywordFlag;

    public String getCustomerUuids() {
        return customerUuids;
    }

    public void setCustomerUuids(String customerUuids) {
        this.customerUuids = customerUuids;
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

    public Long getUserInfoID() {
        return userInfoID;
    }

    public void setUserInfoID(Long userInfoID) {
        this.userInfoID = userInfoID;
    }

    public Long getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(Long organizationID) {
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

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getCategoryTag() {
        return categoryTag;
    }

    public void setCategoryTag(String categoryTag) {
        this.categoryTag = categoryTag;
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

    public String getStandardSpecies() {
        return standardSpecies;
    }

    public void setStandardSpecies(String standardSpecies) {
        this.standardSpecies = standardSpecies;
    }

    public Integer getOptimizationType() {
        return optimizationType;
    }

    public void setOptimizationType(Integer optimizationType) {
        this.optimizationType = optimizationType;
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
