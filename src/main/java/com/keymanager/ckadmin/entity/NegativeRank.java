package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.keymanager.monitoring.entity.BaseEntity;

@TableName(value = "t_negative_rank")
public class NegativeRank extends BaseEntity {

    @TableField(value = "fKeyword")
    private String keyword;

    @TableField(value = "fSearchEngine")
    private String searchEngine;

    @TableField(value = "fNegativeCount")
    private Integer negativeCount;

    @TableField(value = "fFirstPageRanks")
    private String firstPageRanks;

    @TableField(value = "fSecondPageRanks")
    private String secondPageRanks;

    @TableField(value = "fThirdPageRanks")
    private String thirdPageRanks;

    @TableField(value = "fFourthPageRanks")
    private String fourthPageRanks;

    @TableField(value = "fFifthPageRanks")
    private String fifthPageRanks;

    @TableField(value = "fOtherPageRanks")
    private String otherPageRanks;

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

    public Integer getNegativeCount() {
        return negativeCount;
    }

    public void setNegativeCount(Integer negativeCount) {
        this.negativeCount = negativeCount;
    }

    public String getFirstPageRanks() {
        return firstPageRanks;
    }

    public void setFirstPageRanks(String firstPageRanks) {
        this.firstPageRanks = firstPageRanks;
    }

    public String getSecondPageRanks() {
        return secondPageRanks;
    }

    public void setSecondPageRanks(String secondPageRanks) {
        this.secondPageRanks = secondPageRanks;
    }

    public String getThirdPageRanks() {
        return thirdPageRanks;
    }

    public void setThirdPageRanks(String thirdPageRanks) {
        this.thirdPageRanks = thirdPageRanks;
    }

    public String getFourthPageRanks() {
        return fourthPageRanks;
    }

    public void setFourthPageRanks(String fourthPageRanks) {
        this.fourthPageRanks = fourthPageRanks;
    }

    public String getFifthPageRanks() {
        return fifthPageRanks;
    }

    public void setFifthPageRanks(String fifthPageRanks) {
        this.fifthPageRanks = fifthPageRanks;
    }

    public String getOtherPageRanks() {
        return otherPageRanks;
    }

    public void setOtherPageRanks(String otherPageRanks) {
        this.otherPageRanks = otherPageRanks;
    }
}
