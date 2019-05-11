package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author zhoukai
 * @Date 2019/4/27 9:39
 **/
@TableName(value = "t_group")
public class Group extends BaseEntity {

    @TableField(value = "fGroupName")
    private String groupName;

    @TableField(value = "fCreateBy")
    private String createBy;

    @TableField(value = "fRemainingAccount")
    private int remainingAccount;

    public String getGroupName () {
        return groupName;
    }

    public void setGroupName (String groupName) {
        this.groupName = groupName;
    }

    public String getCreateBy () {
        return createBy;
    }

    public void setCreateBy (String createBy) {
        this.createBy = createBy;
    }

    public int getRemainingAccount () {
        return remainingAccount;
    }

    public void setRemainingAccount (int remainingAccount) {
        this.remainingAccount = remainingAccount;
    }
}
