package com.keymanager.monitoring.vo;

import org.apache.shiro.authc.UsernamePasswordToken;

public class ExtendedUsernamePasswordToken extends UsernamePasswordToken {

    private String entryType;
    private String terminalType;
    private String version;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ExtendedUsernamePasswordToken(String userName, String password) {
        super(userName, password);
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }
}
