package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/6/24 13:50
 **/
@TableName(value = "t_operation_combine")
public class OperationCombine extends BaseEntity {

    @TableField(value = "fOperationCombineName")
    private String operationCombineName;

    @TableField(value = "fTerminalType")
    private String terminalType;

    @TableField(value = "fCreator")
    private String creator;

    @TableField(value = "fRemainingAccount")
    private int remainingAccount;

    @TableField(value = "fMaxInvalidCount")
    private int maxInvalidCount;

    @TableField(exist = false)
    private List<GroupSetting> groupSettings;

    public String getOperationCombineName () {
        return operationCombineName;
    }

    public void setOperationCombineName (String operationCombineName) {
        this.operationCombineName = operationCombineName;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }

    public String getCreator () {
        return creator;
    }

    public void setCreator (String creator) {
        this.creator = creator;
    }

    public int getRemainingAccount () {
        return remainingAccount;
    }

    public void setRemainingAccount (int remainingAccount) {
        this.remainingAccount = remainingAccount;
    }

    public int getMaxInvalidCount () {
        return maxInvalidCount;
    }

    public void setMaxInvalidCount (int maxInvalidCount) {
        this.maxInvalidCount = maxInvalidCount;
    }

    public List<GroupSetting> getGroupSettings() {
        return groupSettings;
    }

    public void setGroupSettings(List<GroupSetting> groupSettings) {
        this.groupSettings = groupSettings;
    }
}
