package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.WarnList;

import java.util.List;


public class WarnListCriteria extends BaseCriteria {
    private String keyword;
    private String title;
    private String url;
    private String desc;
    private Integer position;
    private String operationType;
    private String originalUrl;
    private List<WarnList> warnLists;

    public List<WarnList> getWarnLists() {
        return warnLists;
    }

    public void setWarnLists(List<WarnList> warnLists) {
        this.warnLists = warnLists;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }


}
