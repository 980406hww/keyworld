package com.keymanager.monitoring.criteria;

public class GroupSettingCriteria extends BaseCriteria {
    private String optimizedGroupName; // 优化分组
    private String operationType; // 操作类型
    private String terminalType; // 终端类型

    public String getOptimizedGroupName () {
        return optimizedGroupName;
    }

    public void setOptimizedGroupName (String optimizedGroupName) {
        this.optimizedGroupName = optimizedGroupName;
    }

    public String getOperationType () {
        return operationType;
    }

    public void setOperationType (String operationType) {
        this.operationType = operationType;
    }

    public String getTerminalType () {
        return terminalType;
    }

    public void setTerminalType (String terminalType) {
        this.terminalType = terminalType;
    }
}
