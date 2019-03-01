package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author zhoukai
 * @Date 2019/2/28 16:42
 **/
@TableName(value = "t_user_message_list")
public class UserMessageList extends BaseEntity {

    @TableField(value = "fSenderUserName")
    private String fSenderUserName;

    @TableField(value = "fReceiverUserName")
    private String fReceiverUserName;

    @TableField(value = "fStatus")
    private String status;

    @TableField(value = "fContent")
    private String content;

    public String getfSenderUserName() {
        return fSenderUserName;
    }

    public void setfSenderUserName(String fSenderUserName) {
        this.fSenderUserName = fSenderUserName;
    }

    public String getfReceiverUserName() {
        return fReceiverUserName;
    }

    public void setfReceiverUserName(String fReceiverUserName) {
        this.fReceiverUserName = fReceiverUserName;
    }

    public String getStatus () {
        return status;
    }

    public void setStatus (String status) {
        this.status = status;
    }

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }
}
