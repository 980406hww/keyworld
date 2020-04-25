package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

public class GroupSettingCriteria extends BaseCriteria {

    /**
     * 操作组合名称
     */
    private String operationCombineName;
    /**
     * 优化分组
     */
    private String optimizedGroupName;
    /**
     * 操作类型
     */
    /**
     * 用户id
     */
    private String searchEngine;

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    private long uuid;

    private String ownerSelect;

    public String getOwnerSelect() {
        return ownerSelect;
    }

    public void setOwnerSelect(String ownerSelect) {
        this.ownerSelect = ownerSelect;
    }

    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    private String operationType;
    /**
     * 终端类型
     */
    private String terminalType;
    /**
     * 分组下机器是否分配完成
     */
    private Boolean hasRemainingAccount;
    /**
     * 查询来源 关键字 true/全站 false
     */
    private Boolean optimizedGroupNameSearchSource;

    public String getOperationCombineName() {
        return operationCombineName;
    }

    public void setOperationCombineName(String operationCombineName) {
        this.operationCombineName = operationCombineName;
    }

    public String getOptimizedGroupName() {
        return optimizedGroupName;
    }

    public void setOptimizedGroupName(String optimizedGroupName) {
        this.optimizedGroupName = optimizedGroupName;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public Boolean getHasRemainingAccount() {
        return hasRemainingAccount;
    }

    public void setHasRemainingAccount(Boolean hasRemainingAccount) {
        this.hasRemainingAccount = hasRemainingAccount;
    }

    public Boolean getOptimizedGroupNameSearchSource() {
        return optimizedGroupNameSearchSource;
    }

    public void setOptimizedGroupNameSearchSource(Boolean optimizedGroupNameSearchSource) {
        this.optimizedGroupNameSearchSource = optimizedGroupNameSearchSource;
    }
}
