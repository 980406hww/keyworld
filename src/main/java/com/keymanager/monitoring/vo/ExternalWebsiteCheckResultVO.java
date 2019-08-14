package com.keymanager.monitoring.vo;

public class ExternalWebsiteCheckResultVO {

    private Long uuid;
    private Integer backgroundLoginStatus;
    private Integer sftpStatus;
    private Integer indexFileStatus;
    private String databaseStatus;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
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
