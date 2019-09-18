package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import java.util.Date;
import java.util.List;

@TableName(value = "t_qz_setting")
public class QZSetting extends BaseEntity {

    @TableField(value = "fCustomerUuid")
    private Long customerUuid;

    @TableField(exist = false)
    private String organizationName;

    @TableField(exist = false)
    private String contactPerson;

    @TableField(exist = false)
    private String userID;

    @TableField(value = "fDomain")
    private String domain;

    @TableField(value = "fSearchEngine")
    private String searchEngine;

    @TableField(value = "fBearPawNumber", strategy = FieldStrategy.IGNORED)
    private String bearPawNumber;

    @TableField(value = "fPcGroup")
    private String pcGroup;

    @TableField(value = "fPhoneGroup")
    private String phoneGroup;

    @TableField(value = "fType")
    private String type;

    @TableField(value = "fAutoCrawlKeywordFlag")
    private Boolean autoCrawlKeywordFlag;

    @TableField(value = "fPcKeywordExceedMaxCount")
    private Boolean pcKeywordExceedMaxCount;

    @TableField(value = "fPhoneKeywordExceedMaxCount")
    private Boolean phoneKeywordExceedMaxCount;

    @TableField(value = "fIgnoreNoIndex")
    private Boolean ignoreNoIndex;

    @TableField(value = "fIgnoreNoOrder")
    private Boolean ignoreNoOrder;

    @TableField(value = "fUpdateInterval")
    private Integer updateInterval;

    @TableField(value = "fUpdateStatus", strategy = FieldStrategy.IGNORED)
    private String updateStatus;

    @TableField(exist = false)
    private Boolean fIsMonitor;
    @TableField(value = "fUpdateStartTime")
    private Date updateStartTime;

    @TableField(value = "fUpdateEndTime")
    private Date updateEndTime;

    @TableField(value = "fCaptureCurrentKeywordCountTime")
    private Date captureCurrentKeywordCountTime;

    @TableField(value = "fCaptureCurrentKeywordStatus", strategy = FieldStrategy.IGNORED)
    private String captureCurrentKeywordStatus;

    @TableField(value = "fStatus")
    private Integer status;
    /**
     * 续费状态 1：续费  0：暂停续费
     */
    @TableField(value = "fRenewalStatus")
    private int renewalStatus;
    /**
     * qzOperationTypes为全站表子类  一对多
     */
    @TableField(exist = false)
    private List<QZOperationType> qzOperationTypes;

    @TableField(value = "fCrawlerTime")
    private Date crawlerTime;

    @TableField(value = "fCrawlerStatus")
    private String crawlerStatus;

    @TableField(value = "fCaptureTerminalType", strategy = FieldStrategy.IGNORED)
    private String captureTerminalType;
    /**
     * QZCategoryTag为全站表子类 一对多
     */
    @TableField(exist = false)
    private List<QZCategoryTag> qzCategoryTags;
    /**
     * 达标种类 (PC_aiZhan 或者 PC_5118), (Phone_aiZhan 或者 Phone_5118)
     */
    @TableField(exist = false)
    private List<String> standardSpecies;

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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

    public String getBearPawNumber() {
        return bearPawNumber;
    }

    public void setBearPawNumber(String bearPawNumber) {
        this.bearPawNumber = bearPawNumber;
    }

    public String getPcGroup() {
        return pcGroup;
    }

    public void setPcGroup(String pcGroup) {
        this.pcGroup = pcGroup;
    }

    public String getPhoneGroup() {
        return phoneGroup;
    }

    public void setPhoneGroup(String phoneGroup) {
        this.phoneGroup = phoneGroup;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getAutoCrawlKeywordFlag() {
        return autoCrawlKeywordFlag;
    }

    public void setAutoCrawlKeywordFlag(Boolean autoCrawlKeywordFlag) {
        this.autoCrawlKeywordFlag = autoCrawlKeywordFlag;
    }

    public Boolean getPcKeywordExceedMaxCount() {
        return pcKeywordExceedMaxCount;
    }

    public void setPcKeywordExceedMaxCount(Boolean pcKeywordExceedMaxCount) {
        this.pcKeywordExceedMaxCount = pcKeywordExceedMaxCount;
    }

    public Boolean getPhoneKeywordExceedMaxCount() {
        return phoneKeywordExceedMaxCount;
    }

    public void setPhoneKeywordExceedMaxCount(Boolean phoneKeywordExceedMaxCount) {
        this.phoneKeywordExceedMaxCount = phoneKeywordExceedMaxCount;
    }

    public Boolean getIgnoreNoIndex() {
        return ignoreNoIndex;
    }

    public void setIgnoreNoIndex(Boolean ignoreNoIndex) {
        this.ignoreNoIndex = ignoreNoIndex;
    }

    public Boolean getIgnoreNoOrder() {
        return ignoreNoOrder;
    }

    public void setIgnoreNoOrder(Boolean ignoreNoOrder) {
        this.ignoreNoOrder = ignoreNoOrder;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(Integer updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
    }

    public Boolean getfIsMonitor() {
        return fIsMonitor;
    }

    public void setfIsMonitor(Boolean fIsMonitor) {
        this.fIsMonitor = fIsMonitor;
    }

    public Date getUpdateStartTime() {
        return updateStartTime;
    }

    public void setUpdateStartTime(Date updateStartTime) {
        this.updateStartTime = updateStartTime;
    }

    public Date getUpdateEndTime() {
        return updateEndTime;
    }

    public void setUpdateEndTime(Date updateEndTime) {
        this.updateEndTime = updateEndTime;
    }

    public Date getCaptureCurrentKeywordCountTime() {
        return captureCurrentKeywordCountTime;
    }

    public void setCaptureCurrentKeywordCountTime(Date captureCurrentKeywordCountTime) {
        this.captureCurrentKeywordCountTime = captureCurrentKeywordCountTime;
    }

    public String getCaptureCurrentKeywordStatus() {
        return captureCurrentKeywordStatus;
    }

    public void setCaptureCurrentKeywordStatus(String captureCurrentKeywordStatus) {
        this.captureCurrentKeywordStatus = captureCurrentKeywordStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getRenewalStatus() {
        return renewalStatus;
    }

    public void setRenewalStatus(int renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    public List<QZOperationType> getQzOperationTypes() {
        return qzOperationTypes;
    }

    public void setQzOperationTypes(
        List<QZOperationType> qzOperationTypes) {
        this.qzOperationTypes = qzOperationTypes;
    }

    public Date getCrawlerTime() {
        return crawlerTime;
    }

    public void setCrawlerTime(Date crawlerTime) {
        this.crawlerTime = crawlerTime;
    }

    public String getCrawlerStatus() {
        return crawlerStatus;
    }

    public void setCrawlerStatus(String crawlerStatus) {
        this.crawlerStatus = crawlerStatus;
    }

    public List<QZCategoryTag> getQzCategoryTags() {
        return qzCategoryTags;
    }

    public void setQzCategoryTags(List<QZCategoryTag> qzCategoryTags) {
        this.qzCategoryTags = qzCategoryTags;
    }

    public String getCaptureTerminalType() {
        return captureTerminalType;
    }

    public void setCaptureTerminalType(String captureTerminalType) {
        this.captureTerminalType = captureTerminalType;
    }

    public List<String> getStandardSpecies() {
        return standardSpecies;
    }

    public void setStandardSpecies(List<String> standardSpecies) {
        this.standardSpecies = standardSpecies;
    }
}
