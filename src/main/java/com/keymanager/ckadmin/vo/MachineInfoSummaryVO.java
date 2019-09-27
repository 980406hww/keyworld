package com.keymanager.ckadmin.vo;

public class MachineInfoSummaryVO implements Comparable {

    private String clientIDPrefix;
    private int clientIDPrefixCount;
    private int clientIDPrefixTotalCount;

    private String type;
    private int typeCount;
    private int typeTotalCount;

    private String city;
    private String switchGroupName;
    private int count;

    public String getClientIDPrefix() {
        return clientIDPrefix;
    }

    public void setClientIDPrefix(String clientIDPrefix) {
        this.clientIDPrefix = clientIDPrefix;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getClientIDPrefixCount() {
        return clientIDPrefixCount;
    }

    public void setClientIDPrefixCount(int clientIDPrefixCount) {
        this.clientIDPrefixCount = clientIDPrefixCount;
    }

    public int getTypeCount() {
        return typeCount;
    }

    public void setTypeCount(int typeCount) {
        this.typeCount = typeCount;
    }

    public int getClientIDPrefixTotalCount() {
        return clientIDPrefixTotalCount;
    }

    public void setClientIDPrefixTotalCount(int clientIDPrefixTotalCount) {
        this.clientIDPrefixTotalCount = clientIDPrefixTotalCount;
    }

    public int getTypeTotalCount() {
        return typeTotalCount;
    }

    public void setTypeTotalCount(int typeTotalCount) {
        this.typeTotalCount = typeTotalCount;
    }

    public String getSwitchGroupName() {
        return switchGroupName;
    }

    public void setSwitchGroupName(String switchGroupName) {
        this.switchGroupName = switchGroupName;
    }

    @Override
    public int compareTo(Object o) {
        int result = this.getClientIDPrefix().compareTo(((MachineInfoSummaryVO) o).getClientIDPrefix());
        if (result == 0) {
            result = this.getType().compareTo(((MachineInfoSummaryVO) o).getType());
            if (result == 0) {
                if (this.getCity() != null && ((MachineInfoSummaryVO) o).getCity() != null) {
                    result = this.getCity().compareTo(((MachineInfoSummaryVO) o).getCity());
                } else {
                    result = -1;
                }
            }
        }
        return result;
    }
}
