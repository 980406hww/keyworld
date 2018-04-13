package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.NegativeRank;

import java.util.Date;
import java.util.List;

public class NegativeRankCriteria extends BaseCriteria {
    private String searchEngine;
    private Date searchDate;
    private List<NegativeRank> negativeRanks;

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
