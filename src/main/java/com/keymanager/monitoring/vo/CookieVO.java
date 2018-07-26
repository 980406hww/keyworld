package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.Cookie;

import java.util.List;

/**
 * Created by shunshikj08 on 2018/7/25.
 */
public class CookieVO {
    private String userName;
    private String password;
    private String clientID;
    private int cookieCount;
    private List<Cookie> cookies;

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

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }
}
