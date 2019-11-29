package com.keymanager.ckadmin.criteria;


import com.keymanager.ckadmin.criteria.base.BaseCriteria;
import com.keymanager.ckadmin.entity.NegativeRank;
import java.util.Date;
import java.util.List;

public class NegativeRankCriteria extends BaseCriteria {
    private  String keyword;
    private String searchEngine;
    private String createTime;
    private Date searchDate;
    private String userName;
    private String password;
    private List<NegativeRank> negativeRanks;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public List<NegativeRank> getNegativeRanks() {
        return negativeRanks;
    }

    public void setNegativeRanks(List<NegativeRank> negativeRanks) {
        this.negativeRanks = negativeRanks;
    }
}
