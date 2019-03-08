package com.keymanager.monitoring.criteria;

import java.util.List;

public class UserMessageCriteria {
    private Integer uuid;
    private Integer messageStatus;
    private String userName;
    private String content;
    private Integer pageNumber;
    private List<Integer> status;
    private List<String> targetUserName;

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public Integer getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Integer> getStatus() {
        return status;
    }

    public void setStatus(List<Integer> status) {
        this.status = status;
    }

    public List<String> getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(List<String> targetUserName) {
        this.targetUserName = targetUserName;
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
