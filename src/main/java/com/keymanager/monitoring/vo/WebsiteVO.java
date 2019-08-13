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
    private String backendDomain;
    private String backendUserName;
    private String backendPassword;
    private String serverIP;
    private String serverUserName;
    private String serverPassword;
    private String serverPort;
    private Integer accessFailCount;
    private Date accessFailTime;
    private Date lastAccessTime;
    private String websiteType;
    private String updateSalesInfoSign;
    private int friendlyLinkCount;
    private int advertisingCount;
    private String friendlyLink;
    private String advertising;
    private Date updateTime;
    private Date createTime;
    private String synchronousFriendlyLinkSign;
    private String synchronousAdvertisingSign;
    private Integer backgroundLoginStatus;
    private Integer sftpStatus;
    private Integer indexFileStatus;
    private String databaseStatus;

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

    public String getServerPort() {
        return serverPort;
    }

    public void setServerPort(String serverPort) {
        this.serverPort = serverPort;
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

    public String getWebsiteType() {
        return websiteType;
    }

    public void setWebsiteType(String websiteType) {
        this.websiteType = websiteType;
    }

    public String getUpdateSalesInfoSign() {
        return updateSalesInfoSign;
    }

    public void setUpdateSalesInfoSign(String updateSalesInfoSign) {
        this.updateSalesInfoSign = updateSalesInfoSign;
    }

    public String getBackendDomain() {
        return backendDomain;
    }

    public void setBackendDomain(String backendDomain) {
        this.backendDomain = backendDomain;
    }

    public String getBackendUserName() {
        return backendUserName;
    }

    public void setBackendUserName(String backendUserName) {
        this.backendUserName = backendUserName;
    }

    public String getBackendPassword() {
        return backendPassword;
    }

    public void setBackendPassword(String backendPassword) {
        this.backendPassword = backendPassword;
    }

    public int getAdvertisingCount() {
        return advertisingCount;
    }

    public void setAdvertisingCount(int advertisingCount) {
        this.advertisingCount = advertisingCount;
    }

    public String getFriendlyLink() {
        return friendlyLink;
    }

    public void setFriendlyLink(String friendlyLink) {
        this.friendlyLink = friendlyLink;
    }

    public String getAdvertising() {
        return advertising;
    }

    public void setAdvertising(String advertising) {
        this.advertising = advertising;
    }

    public String getSynchronousFriendlyLinkSign() {
        return synchronousFriendlyLinkSign;
    }

    public void setSynchronousFriendlyLinkSign(String synchronousFriendlyLinkSign) {
        this.synchronousFriendlyLinkSign = synchronousFriendlyLinkSign;
    }

    public String getSynchronousAdvertisingSign() {
        return synchronousAdvertisingSign;
    }

    public void setSynchronousAdvertisingSign(String synchronousAdvertisingSign) {
        this.synchronousAdvertisingSign = synchronousAdvertisingSign;
    }

    public Integer getBackgroundLoginStatus() {
        return backgroundLoginStatus;
    }

    public void setBackgroundLoginStatus(Integer backgroundLoginStatus) {
        this.backgroundLoginStatus = backgroundLoginStatus;
    }

    public Integer getSftpStatus() {
        return sftpStatus;
    }

    public void setSftpStatus(Integer sftpStatus) {
        this.sftpStatus = sftpStatus;
    }

    public Integer getIndexFileStatus() {
        return indexFileStatus;
    }

    public void setIndexFileStatus(Integer indexFileStatus) {
        this.indexFileStatus = indexFileStatus;
    }

    public String getDatabaseStatus() {
        return databaseStatus;
    }

    public void setDatabaseStatus(String databaseStatus) {
        this.databaseStatus = databaseStatus;
    }
}
