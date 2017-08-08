package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.TSMainKeyword;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
import com.keymanager.monitoring.vo.TSMainKeywordVO;

import java.util.List;

public class TSMainKeywordCriteria extends BaseCriteria{
    private TSMainKeyword tsMainKeyword;

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
}
