package com.keymanager.ckadmin.criteria;

public class MachineGroupWorkInfoCriteria {

    private String entryType;

    private String machineGroup;

    private String customerName;

    private String terminalType;

    private int dayNum;

    private String machineGroupFuzzyQuery;

    private String userName;

    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public String getMachineGroupFuzzyQuery() {
        return machineGroupFuzzyQuery;
    }

    public void setMachineGroupFuzzyQuery(String machineGroupFuzzyQuery) {
        this.machineGroupFuzzyQuery = machineGroupFuzzyQuery;
    }
}
