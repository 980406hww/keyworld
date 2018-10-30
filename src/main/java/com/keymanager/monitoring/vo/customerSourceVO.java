package com.keymanager.monitoring.vo;

/**
 * Created by shunshikj08 on 2017/12/19.
 */
public class customerSourceVO {
    private Long uuid;
    private String contactPerson;
    private String keyword;

    public Long getUuid () {
        return uuid;
    }

    public void setUuid (Long uuid) {
        this.uuid = uuid;
    }

    public String getContactPerson () {
        return contactPerson;
    }

    public void setContactPerson (String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getKeyword () {
        return keyword;
    }

    public void setKeyword (String keyword) {
        this.keyword = keyword;
    }
}
