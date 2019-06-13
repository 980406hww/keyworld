package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.Date;
import java.util.Map;

@TableName(value = "t_advertising")
public class Advertising extends BaseEntity {

    @TableField(value = "fWebsiteUuid")
    private int websiteUuid;

    @TableField(value = "fCustomerUuid")
    private int customerUuid;

    @TableField(exist=false)
    private String customerInfo;

    @TableField(value = "fAdvertisingId")
    private int advertisingId;

    @TableField(value = "fAdvertisingAdName")
    private String advertisingAdName;

    @TableField(value = "fAdvertisingTagname")
    private String advertisingTagname;

    @TableField(value = "fAdvertisingType")
    private String advertisingType;

    @TableField(exist=false)
    private int advertisingTypeId;

    @TableField(value = "fAdvertisingArcType")
    private String advertisingArcType;

    @TableField(exist=false)
    private int advertisingArcTypeId;

    @TableField(value = "fAdvertisingTimeSet")
    private int advertisingTimeSet;

    @TableField(value = "fAdvertisingStarttime")
    private Date advertisingStarttime;

    @TableField(value = "fAdvertisingEndtime")
    private Date advertisingEndtime;

    @TableField(value = "fAdvertisingNormbody")
    private String advertisingNormbody;

    @TableField(exist=false)
    private Map normbody;

    @TableField(exist=false)
    private int advertisingBodyChecked;

    @TableField(value = "fAdvertisingExpbody")
    private String advertisingExpbody;

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

    public int getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(int advertisingId) {
        this.advertisingId = advertisingId;
    }

    public String getAdvertisingAdName() {
        return advertisingAdName;
    }

    public void setAdvertisingAdName(String advertisingAdName) {
        this.advertisingAdName = advertisingAdName;
    }

    public int getAdvertisingTimeSet() {
        return advertisingTimeSet;
    }

    public void setAdvertisingTimeSet(int advertisingTimeSet) {
        this.advertisingTimeSet = advertisingTimeSet;
    }

    public Date getAdvertisingStarttime() {
        return advertisingStarttime;
    }

    public void setAdvertisingStarttime(Date advertisingStarttime) {
        this.advertisingStarttime = advertisingStarttime;
    }

    public Date getAdvertisingEndtime() {
        return advertisingEndtime;
    }

    public void setAdvertisingEndtime(Date advertisingEndtime) {
        this.advertisingEndtime = advertisingEndtime;
    }

    public Date getRenewTime() {
        return renewTime;
    }

    public void setRenewTime(Date renewTime) {
        this.renewTime = renewTime;
    }

    public String getAdvertisingTagname() {
        return advertisingTagname;
    }

    public void setAdvertisingTagname(String advertisingTagname) {
        this.advertisingTagname = advertisingTagname;
    }

    public String getAdvertisingType() {
        return advertisingType;
    }

    public void setAdvertisingType(String advertisingType) {
        this.advertisingType = advertisingType;
    }

    public String getAdvertisingArcType() {
        return advertisingArcType;
    }

    public void setAdvertisingArcType(String advertisingArcType) {
        this.advertisingArcType = advertisingArcType;
    }

    public String getAdvertisingNormbody() {
        return advertisingNormbody;
    }

    public void setAdvertisingNormbody(String advertisingNormbody) {
        this.advertisingNormbody = advertisingNormbody;
    }

    public String getAdvertisingExpbody() {
        return advertisingExpbody;
    }

    public void setAdvertisingExpbody(String advertisingExpbody) {
        this.advertisingExpbody = advertisingExpbody;
    }

    public Map getNormbody() {
        return normbody;
    }

    public void setNormbody(Map normbody) {
        this.normbody = normbody;
    }

    public int getAdvertisingBodyChecked() {
        return advertisingBodyChecked;
    }

    public void setAdvertisingBodyChecked(int advertisingBodyChecked) {
        this.advertisingBodyChecked = advertisingBodyChecked;
    }

    public int getAdvertisingTypeId() {
        return advertisingTypeId;
    }

    public void setAdvertisingTypeId(int advertisingTypeId) {
        this.advertisingTypeId = advertisingTypeId;
    }

    public int getAdvertisingArcTypeId() {
        return advertisingArcTypeId;
    }

    public void setAdvertisingArcTypeId(int advertisingArcTypeId) {
        this.advertisingArcTypeId = advertisingArcTypeId;
    }
}
