package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author zhoukai
 * @Date 2019/4/16 13:55
 **/
@TableName(value = "t_user_notebook")
public class UserNoteBook extends BaseEntity {

    @TableField(value = "fNotesPerson")
    private String notesPerson;

    @TableField(value = "fCustomerUuid")
    private Long customerUuid;

    @TableField(value = "fQzUuid")
    private Long qzUuid;

    @TableField(value = "fTerminalType")
    private String terminalType;

    @TableField(value = "fContent")
    private String content;

    public String getNotesPerson() {
        return notesPerson;
    }

    public void setNotesPerson(String notesPerson) {
        this.notesPerson = notesPerson;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getQzUuid() {
        return qzUuid;
    }

    public void setQzUuid(Long qzUuid) {
        this.qzUuid = qzUuid;
    }
}
