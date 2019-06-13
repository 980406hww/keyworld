package com.keymanager.monitoring.criteria;

public class AdvertisingCriteria extends BaseCriteria{

    private Long websiteUuid;
    private String customerInfo;
    private String advertisingAdName;
    private String expire;

    public Long getWebsiteUuid() {
        return websiteUuid;
    }

    public void setWebsiteUuid(Long websiteUuid) {
        this.websiteUuid = websiteUuid;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public String getAdvertisingAdName() {
        return advertisingAdName;
    }

    public void setAdvertisingAdName(String advertisingAdName) {
        this.advertisingAdName = advertisingAdName;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }
}
