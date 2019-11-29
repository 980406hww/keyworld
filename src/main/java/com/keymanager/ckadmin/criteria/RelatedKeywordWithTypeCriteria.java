package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.ExternalBaseCriteria;

public class RelatedKeywordWithTypeCriteria extends ExternalBaseCriteria {

    private String mainKeyword;
    private String relatedKeyword;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
