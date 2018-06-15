package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;

import java.util.Date;
/**
 * Created by shunshikj08 on 2017/12/14.
 */
@TableName("t_website")
public class Website extends BaseEntity {

    @TableField(value = "fWebsiteName")
    private String websiteName;

    @TableField(value = "fDomain")
    private String domain;

    @TableField(value = "fIndustry", strategy = FieldStrategy.IGNORED)
    private String industry;

    @TableField(value = "fRegistrar")//注册商
    private String registrar;

    @TableField(value = "fAnalysis")//解析商
    private String analysis;


    @TableField(value = "fExpiryTime")//到期时间
    private Date expiryTime;

    @TableField(value = "fDatabaseName")//数据库名称
    private String databaseName;

    @TableField(value = "fDatabaseUserName")
    private String databaseUserName;

    @TableField(value = "fDatabasePassword")
    private String databasePassword;

    @TableField(value = "fServerIP")
    private String serverIP;

    @TableField(value = "fServerUserName")
    private String serverUserName;

    @TableField(value = "fServerPassword")
    private String serverPassword;

    @TableField(value = "fAccessFailCount")
    private Integer accessFailCount;

    @TableField(value = "fAccessFailTime", strategy = FieldStrategy.IGNORED)
    private Date accessFailTime;

    @TableField(value = "fLastAccessTime")
    private Date lastAccessTime;

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
}
