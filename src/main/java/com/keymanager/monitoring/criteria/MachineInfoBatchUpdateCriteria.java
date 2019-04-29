package com.keymanager.monitoring.criteria;

import com.keymanager.monitoring.entity.MachineInfo;

public class MachineInfoBatchUpdateCriteria {
    private MachineInfo mi;
    private MachineInfo machineInfo;

    public MachineInfo getMi() {
        return mi;
    }

    public void setMi(MachineInfo mi) {
        this.mi = mi;
    }

    public MachineInfo getMachineInfo() {
        return machineInfo;
    }

    public void setMachineInfo(MachineInfo machineInfo) {
        this.machineInfo = machineInfo;
    }
}
