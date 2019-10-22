package com.keymanager.ckadmin.vo;

/**
 * @Author zhoukai
 * @Date 2019/5/28 16:03
 **/
public class QZChargeRuleStandardInfoVO {
    private Integer achieveLevel;
    private String standardSpecies;
    private Integer startKeywordCount;
    private Integer endKeywordCount;
    private Integer amount;

    public Integer getAchieveLevel () {
        return achieveLevel;
    }

    public void setAchieveLevel (Integer achieveLevel) {
        this.achieveLevel = achieveLevel;
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
}
