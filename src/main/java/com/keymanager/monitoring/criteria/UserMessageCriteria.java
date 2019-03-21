package com.keymanager.monitoring.criteria;

import java.util.Date;
import java.util.List;

public class UserMessageCriteria {
    private Integer uuid;
    private boolean updateStatus;
    private String userName;
    private String content;
    private String type;
    private long customerUuid;
    private Integer pageNumber;
    private Integer status;
    private List<String> senderUserNames;
    private List<String> receiverUserNames;
    private Date date;
    private String contactPerson;

    public Integer getUuid() {
        return uuid;
    }

    public void setUuid(Integer uuid) {
        this.uuid = uuid;
    }

    public boolean isUpdateStatus () {
        return updateStatus;
    }

    public void setUpdateStatus (boolean updateStatus) {
        this.updateStatus = updateStatus;
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

    public List<String> getSenderUserNames () {
        return senderUserNames;
    }

    public void setSenderUserNames (List<String> senderUserNames) {
        this.senderUserNames = senderUserNames;
    }

    public List<String> getReceiverUserNames () {
        return receiverUserNames;
    }

    public void setReceiverUserNames (List<String> receiverUserNames) {
        this.receiverUserNames = receiverUserNames;
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

    public Date getDate () {
        return date;
    }

    public void setDate (Date date) {
        this.date = date;
    }

    public String getContactPerson () {
        return contactPerson;
    }

    public void setContactPerson (String contactPerson) {
        this.contactPerson = contactPerson;
    }
}
