package com.keymanager.monitoring.criteria;


/**
 * Created by Administrator on 2018/5/26.
 */
public class KeywordInfoCriteria {
       private String userName ;
       private String searchEngine;
       private String terminalType;
       private String operationType;
       private String keywordInfo;
       private String createTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getKeywordInfo() {
        return keywordInfo;
    }

    public void setKeywordInfo(String keywordInfo) {
        this.keywordInfo = keywordInfo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
