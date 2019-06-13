package com.keymanager.monitoring.vo;

/**
 * @Author zhoukai
 * @Date 2018/12/18 15:09
 **/
public class MachineInfoVO {
    private int machineCount;
    private String operationType;
    private int machineUserPercent;

    public String getOperationType () {
        return operationType;
    }

    public void setOperationType (String operationType) {
        this.operationType = operationType;
    }

    public int getMachineCount () {
        return machineCount;
    }

    public void setMachineCount (int machineCount) {
        this.machineCount = machineCount;
    }

    public int getMachineUserPercent () {
        return machineUserPercent;
    }

    public void setMachineUserPercent (int machineUserPercent) {
        this.machineUserPercent = machineUserPercent;
    }
}
