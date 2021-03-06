package com.keymanager.ckadmin.vo;

public class MachineGroupQueueVO {
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

    public MachineGroupQueueVO() {
    }

    public MachineGroupQueueVO(String machineGroupName, int size) {
        this.machineGroupName = machineGroupName;
        this.size = size;
    }
}
