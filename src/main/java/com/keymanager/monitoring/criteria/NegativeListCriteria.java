package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.NegativeList;

import java.util.Date;
import java.util.List;

/**
 * Created by shunshikj08 on 2017/8/29.
 */
public class NegativeListCriteria extends BaseCriteria {
    private String terminalType;
    private String keyword;
    private String title;
    private String url;
    private String desc;
    private Integer position;
    private String operationType;
    private String originalUrl;
    private Date complainTime;
    private List<NegativeList> negativeLists;

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
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

    public Date getComplainTime() {
        return complainTime;
    }

    public void setComplainTime(Date complainTime) {
        this.complainTime = complainTime;
    }

    public List<NegativeList> getNegativeLists() {
        return negativeLists;
    }

    public void setNegativeLists(List<NegativeList> negativeLists) {
        this.negativeLists = negativeLists;
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
