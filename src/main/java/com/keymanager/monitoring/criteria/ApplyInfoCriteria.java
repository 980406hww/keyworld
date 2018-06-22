package com.keymanager.monitoring.criteria;

/**
 * Created by shunshikj22 on 2017/9/6.
 */
public class ApplyInfoCriteria extends BaseCriteria{
    private String appName;

    private String applicationMarketName;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApplicationMarketName() {
        return applicationMarketName;
    }

    public void setApplicationMarketName(String applicationMarketName) {
        this.applicationMarketName = applicationMarketName;
    }
}
