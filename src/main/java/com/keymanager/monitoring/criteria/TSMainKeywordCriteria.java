package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.TSMainKeyword;

public class TSMainKeywordCriteria extends BaseCriteria{
    private TSMainKeyword tsMainKeyword;
    private String keyword;
    private String ipCity;

    public TSMainKeyword getTsMainKeyword() {
        return tsMainKeyword;
    }

    public void setTsMainKeyword(TSMainKeyword tsMainKeyword) {
        this.tsMainKeyword = tsMainKeyword;
    }

    public String getIpCity() {
        return ipCity;
    }

    public void setIpCity(String ipCity) {
        this.ipCity = ipCity;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
