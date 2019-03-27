package com.keymanager.monitoring.vo;

public class UserMessageVO {
    private long uuid;
    private long customerUuid;
    private String type;
    private String contactPerson;
    private String senderUserName;
    private String receiverUserName;
    private Integer status;

    public long getUuid () {
        return uuid;
    }

    public void setUuid (long uuid) {
        this.uuid = uuid;
    }

    public long getCustomerUuid () {
        return customerUuid;
    }

    public void setCustomerUuid (long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public String getContactPerson () {
        return contactPerson;
    }

    public void setContactPerson (String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getSenderUserName () {
        return senderUserName;
    }

    public void setSenderUserName (String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getReceiverUserName () {
        return receiverUserName;
    }

    public void setReceiverUserName (String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }

    public Integer getStatus () {
        return status;
    }

    public void setStatus (Integer status) {
        this.status = status;
    }
}
