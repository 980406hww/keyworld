package com.keymanager.monitoring.criteria;

public class CustomerChargeRuleCriteria extends BaseCriteria {
    private String customerUuid;
    private String customerInfo;
    private String loginName;
    private Integer expiredChargeCount;
    private Integer nowChargeCount;
    private Integer threeDayChargeCount;
    private Integer sevenChargeCount;
    private Integer chargeDays;

    public String getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(String customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getExpiredChargeCount() {
        return expiredChargeCount;
    }

    public void setExpiredChargeCount(Integer expiredChargeCount) {
        this.expiredChargeCount = expiredChargeCount;
    }

    public Integer getNowChargeCount() {
        return nowChargeCount;
    }

    public void setNowChargeCount(Integer nowChargeCount) {
        this.nowChargeCount = nowChargeCount;
    }

    public Integer getThreeDayChargeCount() {
        return threeDayChargeCount;
    }

    public void setThreeDayChargeCount(Integer threeDayChargeCount) {
        this.threeDayChargeCount = threeDayChargeCount;
    }

    public Integer getSevenChargeCount() {
        return sevenChargeCount;
    }

    public void setSevenChargeCount(Integer sevenChargeCount) {
        this.sevenChargeCount = sevenChargeCount;
    }

    public Integer getChargeDays() {
        return chargeDays;
    }

    public void setChargeDays(Integer chargeDays) {
        this.chargeDays = chargeDays;
    }
}
