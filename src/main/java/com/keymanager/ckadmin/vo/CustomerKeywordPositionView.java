package com.keymanager.ckadmin.vo;

import com.keymanager.ckadmin.enums.CollectMethod;
import com.keymanager.ckadmin.util.Utils;
import java.sql.Date;
import java.sql.Timestamp;

public class CustomerKeywordPositionView extends CustomerKeywordCommonPartVO {

    private int uuid;
    private int customerUuid;
    private String contactPerson;
    private String remarks;
    private int pcPosition;
    private String pcPositionLog;
    private int jisuPosition;
    private String jisuPositionLog;
    private int chupingPosition;
    private String chupingPositionLog;

    private String collectMethod;
    private Timestamp effectiveFromTime;
    private Timestamp effectiveToTime;
    private Timestamp jisuEffectiveFromTime;
    private Timestamp jisuEffectiveToTime;
    private Timestamp chupingEffectiveFromTime;
    private Timestamp chupingEffectiveToTime;

    private Date createDate;

    public String getApplicableUrl() {
        if (!Utils.isNullOrEmpty(this.getUrl())) {
            return Utils.isNullOrEmpty(this.getOriginalUrl()) ? this.getUrl() : this.getOriginalUrl();
        }
        return "";
    }

    public String getApplicablePhoneUrl() {
        if (!Utils.isNullOrEmpty(this.getPhoneUrl())) {
            return Utils.isNullOrEmpty(this.getOriginalPhoneUrl()) ? this.getPhoneUrl() : this.getOriginalPhoneUrl();
        }
        return "";
    }

    @Override
    public double getPcFee() {
        return getPcFee(this.getPcPosition());
    }

    @Override
    public double getJisuFee() {
        return this.getJisuFee(this.getJisuPosition());
    }

    @Override
    public double getChupingFee() {
        return this.getChupingFee(this.getChupingPosition());
    }

    public int getUuid() {
        return uuid;
    }

    public void setUuid(int uuid) {
        this.uuid = uuid;
    }

    public int getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(int customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public int getPcPosition() {
        return pcPosition;
    }

    public void setPcPosition(int pcPosition) {
        this.pcPosition = pcPosition;
    }

    public int getJisuPosition() {
        return jisuPosition;
    }

    public void setJisuPosition(int jisuPosition) {
        this.jisuPosition = jisuPosition;
    }

    public int getChupingPosition() {
        return chupingPosition;
    }

    public void setChupingPosition(int chupingPosition) {
        this.chupingPosition = chupingPosition;
    }

    public String getCollectMethod() {
        return collectMethod;
    }

    public void setCollectMethod(String collectMethod) {
        this.collectMethod = collectMethod;
    }

    public Timestamp getEffectiveFromTime() {
        return effectiveFromTime;
    }

    public void setEffectiveFromTime(Timestamp effectiveFromTime) {
        this.effectiveFromTime = effectiveFromTime;
    }

    public Timestamp getEffectiveToTime() {
        return effectiveToTime;
    }

    public void setEffectiveToTime(Timestamp effectiveToTime) {
        this.effectiveToTime = effectiveToTime;
    }

    public Timestamp getJisuEffectiveFromTime() {
        return jisuEffectiveFromTime;
    }

    public void setJisuEffectiveFromTime(Timestamp jisuEffectiveFromTime) {
        this.jisuEffectiveFromTime = jisuEffectiveFromTime;
    }

    public Timestamp getJisuEffectiveToTime() {
        return jisuEffectiveToTime;
    }

    public void setJisuEffectiveToTime(Timestamp jisuEffectiveToTime) {
        this.jisuEffectiveToTime = jisuEffectiveToTime;
    }

    public Timestamp getChupingEffectiveFromTime() {
        return chupingEffectiveFromTime;
    }

    public void setChupingEffectiveFromTime(Timestamp chupingEffectiveFromTime) {
        this.chupingEffectiveFromTime = chupingEffectiveFromTime;
    }

    public Timestamp getChupingEffectiveToTime() {
        return chupingEffectiveToTime;
    }

    public void setChupingEffectiveToTime(Timestamp chupingEffectiveToTime) {
        this.chupingEffectiveToTime = chupingEffectiveToTime;
    }

    public String getCollectMethodName() {
        return CollectMethod.findByCode(this.getCollectMethod()).getName();
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPcPositionLog() {
        return pcPositionLog;
    }

    public void setPcPositionLog(String pcPositionLog) {
        this.pcPositionLog = pcPositionLog;
    }

    public String getJisuPositionLog() {
        return jisuPositionLog;
    }

    public void setJisuPositionLog(String jisuPositionLog) {
        this.jisuPositionLog = jisuPositionLog;
    }

    public String getChupingPositionLog() {
        return chupingPositionLog;
    }

    public void setChupingPositionLog(String chupingPositionLog) {
        this.chupingPositionLog = chupingPositionLog;
    }
}