package com.keymanager.monitoring.vo;

import java.util.Date;

public class WebsiteVO{

    private Long uuid;
    private String websiteName;
    private String domain;
    private String industry;
    private String registrar;
    private String analysis;
    private Date expiryTime;
    private String databaseName;
    private String databaseUserName;
    private String databasePassword;
    private String backgroundDomain;
    private String backgroundUserName;
    private String backgroundPassword;
    private String serverIP;
    private String serverUserName;
    private String serverPassword;
    private Integer accessFailCount;
    private Date accessFailTime;
    private Date lastAccessTime;
    private int friendlyLinkCount;
    private int advertisingCount;
    private Date updateTime;
    private Date createTime;

    public String getWebsiteName() {
        return websiteName;
    }

    public void setWebsiteName(String websiteName) {
        this.websiteName = websiteName;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Integer getAccessFailCount() {
        return accessFailCount;
    }

    public void setAccessFailCount(Integer accessFailCount) {
        this.accessFailCount = accessFailCount;
    }

    public Date getAccessFailTime() {
        return accessFailTime;
    }

    public void setAccessFailTime(Date accessFailTime) {
        this.accessFailTime = accessFailTime;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public String getRegistrar() {
        return registrar;
    }

    public void setRegistrar(String registrar) {
        this.registrar = registrar;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public Date getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(Date expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseUserName() {
        return databaseUserName;
    }

    public void setDatabaseUserName(String databaseUserName) {
        this.databaseUserName = databaseUserName;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getServerIP() {
        return serverIP;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerUserName() {
        return serverUserName;
    }

    public void setServerUserName(String serverUserName) {
        this.serverUserName = serverUserName;
    }

    public String getServerPassword() {
        return serverPassword;
    }

    public void setServerPassword(String serverPassword) {
        this.serverPassword = serverPassword;
    }

    public int getFriendlyLinkCount() {
        return friendlyLinkCount;
    }

    public void setFriendlyLinkCount(int friendlyLinkCount) {
        this.friendlyLinkCount = friendlyLinkCount;
    }

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
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

    public String getBackgroundDomain() {
        return backgroundDomain;
    }

    public void setBackgroundDomain(String backgroundDomain) {
        this.backgroundDomain = backgroundDomain;
    }

    public String getBackgroundUserName() {
        return backgroundUserName;
    }

    public void setBackgroundUserName(String backgroundUserName) {
        this.backgroundUserName = backgroundUserName;
    }

    public String getBackgroundPassword() {
        return backgroundPassword;
    }

    public void setBackgroundPassword(String backgroundPassword) {
        this.backgroundPassword = backgroundPassword;
    }

    public int getAdvertisingCount() {
        return advertisingCount;
    }

    public void setAdvertisingCount(int advertisingCount) {
        this.advertisingCount = advertisingCount;
    }
}
