package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;



/**
 * Created by ljj on 2018/6/28.
 */
@TableName(value = "t_negative_standard_setting")
public class NegativeStandardSetting  extends BaseEntity {
    @TableField(value = "fCustomerUuid")
    private  Long customerUuid;
    
    @TableField(value = "fKeyword")
    private String keyword;

    @TableField(value = "fSearchEngine")
    private String searchEngine;

    @TableField(exist = false)
    private String contactPerson;

    @TableField(value = "fTopOnePageNegativeCount")
    private int topOnePageNegativeCount;

    @TableField(value = "fTopTwoPageNegativeCount")
    private int topTwoPageNegativeCount;

    @TableField(value = "fTopThreePageNegativeCount")
    private int topThreePageNegativeCount;

    @TableField(value = "fTopFourPageNegativeCount")
    private int topFourPageNegativeCount;

    @TableField(value = "fTopFivePageNegativeCount")
    private int  topFivePageNegativeCount;

    @TableField(value = "fReachStandard")
    private boolean reachStandard;


    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public int getTopOnePageNegativeCount() {
        return topOnePageNegativeCount;
    }

    public void setTopOnePageNegativeCount(int topOnePageNegativeCount) {
        this.topOnePageNegativeCount = topOnePageNegativeCount;
    }

    public int getTopTwoPageNegativeCount() {
        return topTwoPageNegativeCount;
    }

    public void setTopTwoPageNegativeCount(int topTwoPageNegativeCount) {
        this.topTwoPageNegativeCount = topTwoPageNegativeCount;
    }

    public int getTopThreePageNegativeCount() {
        return topThreePageNegativeCount;
    }

    public void setTopThreePageNegativeCount(int topThreePageNegativeCount) {
        this.topThreePageNegativeCount = topThreePageNegativeCount;
    }

    public int getTopFourPageNegativeCount() {
        return topFourPageNegativeCount;
    }

    public void setTopFourPageNegativeCount(int topFourPageNegativeCount) {
        this.topFourPageNegativeCount = topFourPageNegativeCount;
    }

    public int getTopFivePageNegativeCount() {
        return topFivePageNegativeCount;
    }

    public void setTopFivePageNegativeCount(int topFivePageNegativeCount) {
        this.topFivePageNegativeCount = topFivePageNegativeCount;
    }

    public boolean getReachStandard() {
        return reachStandard;
    }

    public void setReachStandard(boolean reachStandard) {
        this.reachStandard = reachStandard;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }
}
