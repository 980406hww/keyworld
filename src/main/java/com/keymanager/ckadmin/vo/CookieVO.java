package com.keymanager.ckadmin.vo;

public class CookieVO {
    private String userName;
    private String password;
    private String clientID;
    private int cookieCount;
    private String cookieStr;
    private String switchGroupName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public int getCookieCount() {
        return cookieCount;
    }

    public void setCookieCount(int cookieCount) {
        this.cookieCount = cookieCount;
    }

    public String getCookieStr() {
        return cookieStr;
    }

    public void setCookieStr(String cookieStr) {
        this.cookieStr = cookieStr;
    }

    public String getSwitchGroupName() {
        return switchGroupName;
    }

    public void setSwitchGroupName(String switchGroupName) {
        this.switchGroupName = switchGroupName;
    }
}
