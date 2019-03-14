package com.keymanager.monitoring.criteria;

import java.util.List;

public class UserMessageCriteria {
    private Integer uuid;
    private Integer messageStatus;
    private String userName;
    private String content;
    private String type;
    private long customerUuid;
    private Integer pageNumber;
    private Integer status;
    private List<String> targetUserNames;

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

    public Integer getStatus () {
        return status;
    }

    public void setStatus (Integer status) {
        this.status = status;
    }

    public List<String> getTargetUserNames () {
        return targetUserNames;
    }

    public void setTargetUserNames (List<String> targetUserNames) {
        this.targetUserNames = targetUserNames;
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

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public long getCustomerUuid () {
        return customerUuid;
    }

    public void setCustomerUuid (long customerUuid) {
        this.customerUuid = customerUuid;
    }
}
