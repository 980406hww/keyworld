package com.keymanager.monitoring.criteria;

/**
 * @Author zhoukai
 * @Date 2019/7/5 20:37
 **/
public class IndustryDetailCriteria {

    private long industryID;
    private String website;
    private Integer weight;
    private String remark;

    public long getIndustryID() {
        return industryID;
    }

    public void setIndustryID(long industryID) {
        this.industryID = industryID;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
