package com.keymanager.monitoring.vo;

import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.entity.UserMessage;

import java.util.List;

public class UserMessageVO {
    private Integer messageStatus;
    private List<Integer> status;
    private List<String> targetUserName;
    private Integer pageNumber;
    private Integer pageTotalNumber;
    private List<UserMessage> userMessages;
    private List<UserInfo> userInfoList;

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

    public List<UserMessage> getUserMessages () {
        return userMessages;
    }

    public void setUserMessages (List<UserMessage> userMessages) {
        this.userMessages = userMessages;
    }

    public List<UserInfo> getUserInfoList() {
        return userInfoList;
    }

    public void setUserInfoList(List<UserInfo> userInfoList) {
        this.userInfoList = userInfoList;
    }
}
