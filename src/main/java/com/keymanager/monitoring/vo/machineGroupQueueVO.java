package com.keymanager.monitoring.vo;

public class machineGroupQueueVO {
    private String machineGroupName;
    private int size;

    public String getMachineGroupName() {
        return machineGroupName;
    }

    public void setMachineGroupName(String machineGroupName) {
        this.machineGroupName = machineGroupName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public machineGroupQueueVO() {
    }

    public machineGroupQueueVO(String machineGroupName, int size) {
        this.machineGroupName = machineGroupName;
        this.size = size;
    }
}
