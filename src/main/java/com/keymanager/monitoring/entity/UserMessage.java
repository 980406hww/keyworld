package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author zhoukai
 * @Date 2019/2/28 16:42
 **/
@TableName(value = "t_user_message")
public class UserMessage extends BaseEntity {

    @TableField(value = "fSenderUserName")
    private String senderUserName;

    @TableField(value = "fReceiverUserName")
    private String receiverUserName;

    @TableField(value = "fStatus")
    private Integer status;

    @TableField(value = "fContent")
    private String content;

    @TableField(value = "fType")
    private String type;

    @TableField(value = "fCustomerUuid")
    private Long customerUuid;

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

    public String getContent () {
        return content;
    }

    public void setContent (String content) {
        this.content = content;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    public Long getCustomerUuid () {
        return customerUuid;
    }

    public void setCustomerUuid (Long customerUuid) {
        this.customerUuid = customerUuid;
    }
}
