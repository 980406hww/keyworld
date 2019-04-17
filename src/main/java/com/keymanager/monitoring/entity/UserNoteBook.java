package com.keymanager.monitoring.entity;

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

    @TableField(value = "fContent")
    private String content;

    public String getNotesPerson () {
        return notesPerson;
    }

    public void setNotesPerson (String notesPerson) {
        this.notesPerson = notesPerson;
    }

    public Long getCustomerUuid () {
        return customerUuid;
    }

    public void setCustomerUuid (Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }
}
