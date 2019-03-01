package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author zhoukai
 * @Date 2019/2/28 16:42
 **/
@TableName(value = "t_user_message_list")
public class UserMessageList extends BaseEntity {

    @TableField(value = "fSenderID")
    private Integer senderID;

    @TableField(value = "fReceiverID")
    private Integer receiverID;

    @TableField(value = "fTerminalType")
    private String terminalType;

    @TableField(value = "fStatus")
    private String status;

    @TableField(value = "fContent")
    private String content;

    public Integer getSenderID () {
        return senderID;
    }

    public void setSenderID (Integer senderID) {
        this.senderID = senderID;
    }

    public Integer getReceiverID () {
        return receiverID;
    }

    public void setReceiverID (Integer receiverID) {
        this.receiverID = receiverID;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
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
