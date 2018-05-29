package com.keymanager.monitoring.criteria;

import java.security.PrivateKey;
import java.util.Date;

/**
 * Created by Administrator on 2018/5/26.
 */
public class KeywordInfoCriteria {
       private String userName ; //名字
       private String monitoring; //监控类型
       private String platform;  //监控平台
       private String operationType; //操作类型
       private String keywordInfo; //关键字连接
       private String createTime; //创建时间

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(String monitoring) {
        this.monitoring = monitoring;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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
