package com.keymanager.monitoring.criteria;

public class FriendlyLinkCriteria extends BaseCriteria{

    private Long websiteUuid;
    private String customerInfo;
    private Long friendlyLinkId;
    private String friendlyLinkWebName;
    private String friendlyLinkUrl;
    private int friendlyLinkIsCheck;
    private String expire;

    public Long getWebsiteUuid() {
        return websiteUuid;
    }

    public void setWebsiteUuid(Long websiteUuid) {
        this.websiteUuid = websiteUuid;
    }

    public Long getFriendlyLinkId() {
        return friendlyLinkId;
    }

    public void setFriendlyLinkId(Long friendlyLinkId) {
        this.friendlyLinkId = friendlyLinkId;
    }

    public String getFriendlyLinkWebName() {
        return friendlyLinkWebName;
    }

    public void setFriendlyLinkWebName(String friendlyLinkWebName) {
        this.friendlyLinkWebName = friendlyLinkWebName;
    }

    public String getFriendlyLinkUrl() {
        return friendlyLinkUrl;
    }

    public void setFriendlyLinkUrl(String friendlyLinkUrl) {
        this.friendlyLinkUrl = friendlyLinkUrl;
    }

    public int getFriendlyLinkIsCheck() {
        return friendlyLinkIsCheck;
    }

    public void setFriendlyLinkIsCheck(int friendlyLinkIsCheck) {
        this.friendlyLinkIsCheck = friendlyLinkIsCheck;
    }

    public String getExpire() {
        return expire;
    }

    public void setExpire(String expire) {
        this.expire = expire;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }
}
