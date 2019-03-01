package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.UserMessageList;

import java.util.List;

public class UserMessageListVO {
    private Integer messageStatus;
    private List<Integer> status;
    private List<String> targetUserName;
    private Integer pageNumber;
    private Integer pageTotalNumber;
    private List<UserMessageList> userMessageLists;

    public Integer getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(Integer messageStatus) {
        this.messageStatus = messageStatus;
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

    public Integer getPageTotalNumber() {
        return pageTotalNumber;
    }

    public void setPageTotalNumber(Integer pageTotalNumber) {
        this.pageTotalNumber = pageTotalNumber;
    }

    public List<UserMessageList> getUserMessageLists() {
        return userMessageLists;
    }

    public void setUserMessageLists(List<UserMessageList> userMessageLists) {
        this.userMessageLists = userMessageLists;
    }
}
