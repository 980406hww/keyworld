package com.keymanager.ckadmin.vo;

import com.keymanager.enums.CollectMethod;
import com.keymanager.util.Utils;
import java.sql.Date;
import java.sql.Timestamp;

public class CustomerKeywordPositionHistoryLogView {

    private int uuid;
    private int customerUuid;
    private String contactPerson;
    private String remarks;
    private String keyword;
    private String url;
    private String phoneUrl;
    private String originalUrl;
    private String originalPhoneUrl;
    private int pcPosition;
    private int jisuPosition;
    private int chupingPosition;

    private double positionFirstFee;
    private double positionSecondFee;
    private double positionThirdFee;
    private double positionForthFee;
    private double positionFifthFee;
    private double positionFirstPageFee;

    private double jisuPositionFirstFee;
    private double jisuPositionSecondFee;
    private double jisuPositionThirdFee;
    private double jisuPositionForthFee;
    private double jisuPositionFifthFee;
    private double jisuPositionFirstPageFee;

    private double chupingPositionFirstFee;
    private double chupingPositionSecondFee;
    private double chupingPositionThirdFee;
    private double chupingPositionForthFee;
    private double chupingPositionFifthFee;
    private double chupingPositionFirstPageFee;

    private String collectMethod;
    private Timestamp effectiveFromTime;
    private Timestamp effectiveToTime;
    private Timestamp jisuEffectiveFromTime;
    private Timestamp jisuEffectiveToTime;
    private Timestamp chupingEffectiveFromTime;
    private Timestamp chupingEffectiveToTime;

    private Date createDate;

    private String type;
    private String ip;
    private int positionNumber;
    private Timestamp createTime;

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

    public String getPcFeeString() {
        double pcFee = getPcFee();
        return pcFee > 0 ? Utils.removeDoubleZeros(pcFee) : "";
    }

    private double getPcFee() {
        double pcFee = 0;
        if (!Utils.isNullOrEmpty(getUrl()) && getPcPosition() > 0) {
            switch (getPcPosition()) {
                case 1:
                    pcFee = this.getPositionFirstFee();
                    break;
                case 2:
                    pcFee = this.getPositionSecondFee();
                    break;
                case 3:
                    pcFee = this.getPositionThirdFee();
                    break;
                case 4:
                    pcFee = this.getPositionForthFee();
                    break;
                case 5:
                    pcFee = this.getPositionFifthFee();
                    break;
                default:
                    if (getPcPosition() <= 10) {
                        pcFee = this.getPositionFirstPageFee();
                    } else {
                        pcFee = 0;
                    }
                    break;
            }
        }
        return pcFee;
    }

    public String getJisuFeeString() {
        double jisuFee = getJisuFee();
        return jisuFee > 0 ? Utils.removeDoubleZeros(jisuFee) : "";
    }

    private double getJisuFee() {
        double jisuFee = 0;
        if (!Utils.isNullOrEmpty(getPhoneUrl()) && getJisuPosition() > 0) {
            switch (getJisuPosition()) {
                case 1:
                    jisuFee = this.getJisuPositionFirstFee();
                    break;
                case 2:
                    jisuFee = this.getJisuPositionSecondFee();
                    break;
                case 3:
                    jisuFee = this.getJisuPositionThirdFee();
                    break;
                case 4:
                    jisuFee = this.getJisuPositionForthFee();
                    break;
                case 5:
                    jisuFee = this.getJisuPositionFifthFee();
                    break;
                default:
                    if (this.getJisuPosition() <= 10) {
                        jisuFee = this.getJisuPositionFirstPageFee();
                    } else {
                        jisuFee = 0;
                    }
                    break;
            }
        }
        return jisuFee;
    }

    public String getChupingFeeString() {
        double chupingFee = getChupingFee();
        return chupingFee > 0 ? Utils.removeDoubleZeros(chupingFee) : "";
    }

    private double getChupingFee() {
        double chupingFee = 0;
        if (!Utils.isNullOrEmpty(getPhoneUrl()) && getChupingPosition() > 0) {
            switch (getChupingPosition()) {
                case 1:
                    chupingFee = this.getChupingPositionFirstFee();
                    break;
                case 2:
                    chupingFee = this.getChupingPositionSecondFee();
                    break;
                case 3:
                    chupingFee = this.getChupingPositionThirdFee();
                    break;
                case 4:
                    chupingFee = this.getChupingPositionForthFee();
                    break;
                case 5:
                    chupingFee = this.getChupingPositionFifthFee();
                    break;
                default:
                    if (this.getChupingPosition() <= 10) {
                        chupingFee = this.getChupingPositionFirstPageFee();
                    } else {
                        chupingFee = 0;
                    }
                    break;
            }
        }
        return chupingFee;
    }

    public double getSubTotal() {
        return getChupingFee() + getJisuFee() + getPcFee();
    }

    public String getSubTotalString() {
        double subTotal = getSubTotal();
        return subTotal > 0 ? Utils.removeDoubleZeros(subTotal) : "";
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

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPhoneUrl() {
        return phoneUrl;
    }

    public void setPhoneUrl(String phoneUrl) {
        this.phoneUrl = phoneUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getOriginalPhoneUrl() {
        return originalPhoneUrl;
    }

    public void setOriginalPhoneUrl(String originalPhoneUrl) {
        this.originalPhoneUrl = originalPhoneUrl;
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

    public double getPositionFirstFee() {
        return positionFirstFee;
    }

    public void setPositionFirstFee(double positionFirstFee) {
        this.positionFirstFee = positionFirstFee;
    }

    public double getPositionSecondFee() {
        return positionSecondFee;
    }

    public void setPositionSecondFee(double positionSecondFee) {
        this.positionSecondFee = positionSecondFee;
    }

    public double getPositionThirdFee() {
        return positionThirdFee;
    }

    public void setPositionThirdFee(double positionThirdFee) {
        this.positionThirdFee = positionThirdFee;
    }

    public double getPositionForthFee() {
        return positionForthFee;
    }

    public void setPositionForthFee(double positionForthFee) {
        this.positionForthFee = positionForthFee;
    }

    public double getPositionFifthFee() {
        return positionFifthFee;
    }

    public void setPositionFifthFee(double positionFifthFee) {
        this.positionFifthFee = positionFifthFee;
    }

    public double getJisuPositionFirstFee() {
        return jisuPositionFirstFee;
    }

    public void setJisuPositionFirstFee(double jisuPositionFirstFee) {
        this.jisuPositionFirstFee = jisuPositionFirstFee;
    }

    public double getJisuPositionSecondFee() {
        return jisuPositionSecondFee;
    }

    public void setJisuPositionSecondFee(double jisuPositionSecondFee) {
        this.jisuPositionSecondFee = jisuPositionSecondFee;
    }

    public double getJisuPositionThirdFee() {
        return jisuPositionThirdFee;
    }

    public void setJisuPositionThirdFee(double jisuPositionThirdFee) {
        this.jisuPositionThirdFee = jisuPositionThirdFee;
    }

    public double getChupingPositionFirstFee() {
        return chupingPositionFirstFee;
    }

    public void setChupingPositionFirstFee(double chupingPositionFirstFee) {
        this.chupingPositionFirstFee = chupingPositionFirstFee;
    }

    public double getChupingPositionSecondFee() {
        return chupingPositionSecondFee;
    }

    public void setChupingPositionSecondFee(double chupingPositionSecondFee) {
        this.chupingPositionSecondFee = chupingPositionSecondFee;
    }

    public double getChupingPositionThirdFee() {
        return chupingPositionThirdFee;
    }

    public void setChupingPositionThirdFee(double chupingPositionThirdFee) {
        this.chupingPositionThirdFee = chupingPositionThirdFee;
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

    public double getPositionFirstPageFee() {
        return positionFirstPageFee;
    }

    public void setPositionFirstPageFee(double positionFirstPageFee) {
        this.positionFirstPageFee = positionFirstPageFee;
    }

    public double getJisuPositionForthFee() {
        return jisuPositionForthFee;
    }

    public void setJisuPositionForthFee(double jisuPositionForthFee) {
        this.jisuPositionForthFee = jisuPositionForthFee;
    }

    public double getJisuPositionFifthFee() {
        return jisuPositionFifthFee;
    }

    public void setJisuPositionFifthFee(double jisuPositionFifthFee) {
        this.jisuPositionFifthFee = jisuPositionFifthFee;
    }

    public double getJisuPositionFirstPageFee() {
        return jisuPositionFirstPageFee;
    }

    public void setJisuPositionFirstPageFee(double jisuPositionFirstPageFee) {
        this.jisuPositionFirstPageFee = jisuPositionFirstPageFee;
    }

    public double getChupingPositionForthFee() {
        return chupingPositionForthFee;
    }

    public void setChupingPositionForthFee(double chupingPositionForthFee) {
        this.chupingPositionForthFee = chupingPositionForthFee;
    }

    public double getChupingPositionFifthFee() {
        return chupingPositionFifthFee;
    }

    public void setChupingPositionFifthFee(double chupingPositionFifthFee) {
        this.chupingPositionFifthFee = chupingPositionFifthFee;
    }

    public double getChupingPositionFirstPageFee() {
        return chupingPositionFirstPageFee;
    }

    public void setChupingPositionFirstPageFee(double chupingPositionFirstPageFee) {
        this.chupingPositionFirstPageFee = chupingPositionFirstPageFee;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPositionNumber() {
        return positionNumber;
    }

    public void setPositionNumber(int positionNumber) {
        this.positionNumber = positionNumber;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }
}