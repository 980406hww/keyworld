package com.keymanager.monitoring.criteria;

public class GroupSettingCriteria extends BaseCriteria {
    private String optimizedGroupName; // 优化分组
    private String operationType; // 操作类型
    private String terminalType; // 终端类型
    private Boolean hasOperation = true; // 有/无 操作类型。 默认值true
    private Boolean hasRemainingAccount; // 分组下机器是否分配完成
    private Boolean optimizedGroupNameSearchSource; // 查询来源 关键字 true/全站 false

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

    public Boolean getHasOperation () {
        return hasOperation;
    }

    public void setHasOperation (Boolean hasOperation) {
        this.hasOperation = hasOperation;
    }

    public Boolean getHasRemainingAccount () {
        return hasRemainingAccount;
    }

    public void setHasRemainingAccount (Boolean hasRemainingAccount) {
        this.hasRemainingAccount = hasRemainingAccount;
    }

    public Boolean getOptimizedGroupNameSearchSource() {
        return optimizedGroupNameSearchSource;
    }

    public void setOptimizedGroupNameSearchSource(Boolean optimizedGroupNameSearchSource) {
        this.optimizedGroupNameSearchSource = optimizedGroupNameSearchSource;
    }
}
