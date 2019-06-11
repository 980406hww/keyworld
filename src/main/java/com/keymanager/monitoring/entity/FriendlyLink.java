package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;

@TableName(value = "t_friendly_link")
public class FriendlyLink extends BaseEntity {

    @TableField(value = "fWebsiteUuid")
    private int websiteUuid;

    @TableField(value = "fCustomerUuid")
    private int customerUuid;

    @TableField(exist=false)
    private String customerInfo;

    @TableField(value = "fFriendlyLinkId")
    private int friendlyLinkId;

    @TableField(value = "fFriendlyLinkWebName")
    private String friendlyLinkWebName;

    @TableField(value = "fFriendlyLinkUrl")
    private String friendlyLinkUrl;

    @TableField(value = "fFriendlyLinkSortRank")
    private int friendlyLinkSortRank;

    @TableField(value = "fFriendlyLinkMsg")
    private String friendlyLinkMsg;

    @TableField(value = "fFriendlyLinkEmail")
    private String friendlyLinkEmail;

    @TableField(value = "fFriendlyLinkLogo")
    private String friendlyLinkLogo;

    @TableField(value = "fFriendlyLinkType")
    private String friendlyLinkType;

    @TableField(value = "fFriendlyLinkDtime")
    private Date friendlyLinkDtime;

    @TableField(value = "fFriendlyLinkIsCheck")
    private int friendlyLinkIsCheck;

    @TableField(value = "fExpirationTime")
    private Date expirationTime;

    @TableField(value = "fRenewTime")
    private Date renewTime;

    public int getWebsiteUuid() {
        return websiteUuid;
    }

    public void setWebsiteUuid(int websiteUuid) {
        this.websiteUuid = websiteUuid;
    }

    public int getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(int customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(String customerInfo) {
        this.customerInfo = customerInfo;
    }

    public int getFriendlyLinkId() {
        return friendlyLinkId;
    }

    public void setFriendlyLinkId(int friendlyLinkId) {
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

    public int getFriendlyLinkSortRank() {
        return friendlyLinkSortRank;
    }

    public void setFriendlyLinkSortRank(int friendlyLinkSortRank) {
        this.friendlyLinkSortRank = friendlyLinkSortRank;
    }

    public String getFriendlyLinkMsg() {
        return friendlyLinkMsg;
    }

    public void setFriendlyLinkMsg(String friendlyLinkMsg) {
        this.friendlyLinkMsg = friendlyLinkMsg;
    }

    public String getFriendlyLinkEmail() {
        return friendlyLinkEmail;
    }

    public void setFriendlyLinkEmail(String friendlyLinkEmail) {
        this.friendlyLinkEmail = friendlyLinkEmail;
    }

    public String getFriendlyLinkLogo() {
        return friendlyLinkLogo;
    }

    public void setFriendlyLinkLogo(String friendlyLinkLogo) {
        this.friendlyLinkLogo = friendlyLinkLogo;
    }

    public String getFriendlyLinkType() {
        return friendlyLinkType;
    }

    public void setFriendlyLinkType(String friendlyLinkType) {
        this.friendlyLinkType = friendlyLinkType;
    }

    public Date getFriendlyLinkDtime() {
        return friendlyLinkDtime;
    }

    public void setFriendlyLinkDtime(Date friendlyLinkDtime) {
        this.friendlyLinkDtime = friendlyLinkDtime;
    }

    public int getFriendlyLinkIsCheck() {
        return friendlyLinkIsCheck;
    }

    public void setFriendlyLinkIsCheck(int friendlyLinkIsCheck) {
        this.friendlyLinkIsCheck = friendlyLinkIsCheck;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Date getRenewTime() {
        return renewTime;
    }

    public void setRenewTime(Date renewTime) {
        this.renewTime = renewTime;
    }
}
