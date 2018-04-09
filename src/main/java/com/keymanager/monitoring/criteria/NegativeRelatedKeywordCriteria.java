package com.keymanager.monitoring.criteria;

public class NegativeRelatedKeywordCriteria extends BaseCriteria{
    private String mainKeyword;
    private String relatedKeyword;

    public String getMainKeyword() {
        return mainKeyword;
    }

    public void setMainKeyword(String mainKeyword) {
        this.mainKeyword = mainKeyword;
    }

    public String getRelatedKeyword() {
        return relatedKeyword;
    }

    public void setRelatedKeyword(String relatedKeyword) {
        this.relatedKeyword = relatedKeyword;
    }
}
