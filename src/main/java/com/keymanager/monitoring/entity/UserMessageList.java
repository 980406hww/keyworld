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
    private String senderUserName;

    @TableField(value = "fReceiverUserName")
    private String receiverUserName;

    @TableField(value = "fStatus")
    private String status;

    @TableField(value = "fContent")
    private String content;

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
