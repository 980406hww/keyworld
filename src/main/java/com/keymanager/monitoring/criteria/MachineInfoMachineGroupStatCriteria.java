package com.keymanager.monitoring.criteria;

public class MachineInfoMachineGroupStatCriteria extends BaseCriteria {
    private String machineGroup;
    private String terminalType;

    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }
}
