package com.keymanager.monitoring.vo;

import java.util.Date;

public class QZSettingVO {
    private Long uuid;

    private int customerUuid;

    private String contactPerson;

    private String domain;

    private String group;

    private int updateInterval;

    private String updateStatus;

    private Date updateStartTime;

    private Date updateEndTime;

    private Date updateTime;

    private Date createTime;

    private Date captureCurrentKeywordCountTime;

    private String captureCurrentKeywordStatus;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public int getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(int customerUuid) {
        this.customerUuid = customerUuid;
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

    public int getUpdateInterval() {
        return updateInterval;
    }

    public void setUpdateInterval(int updateInterval) {
        this.updateInterval = updateInterval;
    }

    public String getUpdateStatus() {
        return updateStatus;
    }

    public void setUpdateStatus(String updateStatus) {
        this.updateStatus = updateStatus;
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

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
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
}
