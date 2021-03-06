package com.keymanager.monitoring.criteria;

public class MachineInfoGroupStatCriteria extends BaseCriteria {
    private String groupName;
    private String terminalType;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }
}
