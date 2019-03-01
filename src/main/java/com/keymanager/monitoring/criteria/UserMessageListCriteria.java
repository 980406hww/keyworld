package com.keymanager.monitoring.criteria;

import java.util.List;

public class UserMessageListCriteria {
    private Integer messageStatus;
    private String userName;
    private List<Integer> status;
    private List<String> targetUserName;
    private Integer pageNumber;
    private String content;

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
