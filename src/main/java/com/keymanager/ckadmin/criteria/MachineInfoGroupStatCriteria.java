package com.keymanager.ckadmin.criteria;


import com.keymanager.ckadmin.criteria.base.BaseCriteria;

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
