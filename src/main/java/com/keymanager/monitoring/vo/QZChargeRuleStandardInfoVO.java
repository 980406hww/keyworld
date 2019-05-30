package com.keymanager.monitoring.vo;

import java.util.Date;

/**
 * @Author zhoukai
 * @Date 2019/5/28 16:03
 **/
public class QZChargeRuleStandardInfoVO {
    private String standardType;
    private Integer achieveLevel;
    private Double differenceValue;
    private String standardSpecies;
    private Integer startKeywordCount;
    private Integer endKeywordCount;
    private Integer amount;
    private Date standardTime;

    public String getStandardType () {
        return standardType;
    }

    public void setStandardType (String standardType) {
        this.standardType = standardType;
    }

    public Integer getAchieveLevel () {
        return achieveLevel;
    }

    public void setAchieveLevel (Integer achieveLevel) {
        this.achieveLevel = achieveLevel;
    }

    public Double getDifferenceValue () {
        return differenceValue;
    }

    public void setDifferenceValue (Double differenceValue) {
        this.differenceValue = differenceValue;
    }

    public String getStandardSpecies () {
        return standardSpecies;
    }

    public void setStandardSpecies (String standardSpecies) {
        this.standardSpecies = standardSpecies;
    }

    public Integer getStartKeywordCount () {
        return startKeywordCount;
    }

    public void setStartKeywordCount (Integer startKeywordCount) {
        this.startKeywordCount = startKeywordCount;
    }

    public Integer getEndKeywordCount () {
        return endKeywordCount;
    }

    public void setEndKeywordCount (Integer endKeywordCount) {
        this.endKeywordCount = endKeywordCount;
    }

    public Integer getAmount () {
        return amount;
    }

    public void setAmount (Integer amount) {
        this.amount = amount;
    }

    public Date getStandardTime () {
        return standardTime;
    }

    public void setStandardTime (Date standardTime) {
        this.standardTime = standardTime;
    }
}
