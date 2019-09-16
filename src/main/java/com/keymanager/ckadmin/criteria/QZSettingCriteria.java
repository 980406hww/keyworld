package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

import java.util.Date;
import java.util.List;

/**
 * @ClassName QZSettingCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/6 18:24
 * @Version 1.0
 */
public class QZSettingCriteria extends BaseCriteria {

    private Long customerUuid;
    private String autoCrawlKeywordFlag;
    private String categoryTag;
    private String createTime;
    private String createTimePrefix;
    private String customerInfo;
    private String domain;
    private String group;
    private String optimizationType;
    private String organizationID;
    private String renewalStatus;
    private String searchEngine;
    private String standardSpecies;
    private String status;
    private String terminalType;
    private String updateStatus;
    private String userInfoID;
    private String loginName;

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String isAutoCrawlKeywordFlag() {
        return autoCrawlKeywordFlag;
    }

    public void setAutoCrawlKeywordFlag(String autoCrawlKeywordFlag) {
        this.autoCrawlKeywordFlag = autoCrawlKeywordFlag;
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

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getOptimizationType() {
        return optimizationType;
    }

    public void setOptimizationType(String optimizationType) {
        this.optimizationType = optimizationType;
    }

    public String getOrganizationID() {
        return organizationID;
    }

    public void setOrganizationID(String organizationID) {
        this.organizationID = organizationID;
    }

    public String getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(String renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getStandardSpecies() {
        return standardSpecies;
    }

    public void setStandardSpecies(String standardSpecies) {
        this.standardSpecies = standardSpecies;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public String getUserInfoID() {
        return userInfoID;
    }

    public void setUserInfoID(String userInfoID) {
        this.userInfoID = userInfoID;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
