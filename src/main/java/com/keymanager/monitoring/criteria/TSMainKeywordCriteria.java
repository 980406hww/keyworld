package com.keymanager.monitoring.criteria;


import com.keymanager.monitoring.entity.TSMainKeyword;
import com.keymanager.monitoring.entity.TSNegativeKeyword;
import java.util.List;

public class TSMainKeywordCriteria extends BaseCriteria{
    private boolean downloadTimesUsed;
    private TSMainKeyword tsMainKeyword;
    private List<TSNegativeKeyword> tsNegativeKeywords;

    public TSMainKeyword getTsMainKeyword() {
        return tsMainKeyword;
    }

    public void setTsMainKeyword(TSMainKeyword tsMainKeyword) {
        this.tsMainKeyword = tsMainKeyword;
    }

    public List<TSNegativeKeyword> getTsNegativeKeywords() {
        return tsNegativeKeywords;
    }

    public void setTsNegativeKeywords(List<TSNegativeKeyword> tsNegativeKeywords) {
        this.tsNegativeKeywords = tsNegativeKeywords;
    }

    public boolean isDownloadTimesUsed() {
        return downloadTimesUsed;
    }

    public void setDownloadTimesUsed(boolean downloadTimesUsed) {
        this.downloadTimesUsed = downloadTimesUsed;
    }
}
