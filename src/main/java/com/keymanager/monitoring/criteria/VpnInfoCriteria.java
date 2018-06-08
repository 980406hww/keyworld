package com.keymanager.monitoring.criteria;

/**
 * Created by shunshikj22 on 2017/9/6.
 */
public class VpnInfoCriteria extends BaseCriteria{
    private String userName;

    private String imei;

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
