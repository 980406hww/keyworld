package com.keymanager.monitoring.entity;

import com.keymanager.monitoring.common.shiro.ShiroUser;

import java.io.Serializable;
import java.util.Date;

public class UserOnline implements Serializable {

    private static final long serialVersionUID = 3828664348416633856L;

    private String id;  // session id

    private String host;  // 用户主机地址

    private String systemHost;  // 用户登录时系统IP

    private String terminalType;  // 终端

    private String entryType;  // 入口

    private String status;  // 状态

    private Date startTimestamp;  // session创建时间

    private Date lastAccessTime;  // session最后访问时间

    private Long timeout;  // 超时时间

    private ShiroUser shiroUser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSystemHost() {
        return systemHost;
    }

    public void setSystemHost(String systemHost) {
        this.systemHost = systemHost;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTimestamp() {
        return startTimestamp;
    }

    public void setStartTimestamp(Date startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public Date getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(Date lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public Long getTimeout() {
        return timeout;
    }

    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    public ShiroUser getShiroUser() {
        return shiroUser;
    }

    public void setShiroUser(ShiroUser shiroUser) {
        this.shiroUser = shiroUser;
    }
}
