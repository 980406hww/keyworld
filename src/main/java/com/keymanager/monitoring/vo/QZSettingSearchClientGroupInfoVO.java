package com.keymanager.monitoring.vo;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2018/12/18 11:22
 **/
public class QZSettingSearchClientGroupInfoVO {
    private int customerKeywordCount;
    private List<MachineInfoVO> machineInfoVos;
    private List<String> categoryTagNames;

    public int getCustomerKeywordCount () {
        return customerKeywordCount;
    }

    public void setCustomerKeywordCount (int customerKeywordCount) {
        this.customerKeywordCount = customerKeywordCount;
    }

    public List<MachineInfoVO> getMachineInfoVos () {
        return machineInfoVos;
    }

    public void setMachineInfoVos (List<MachineInfoVO> machineInfoVos) {
        this.machineInfoVos = machineInfoVos;
    }

    public List<String> getCategoryTagNames () {
        return categoryTagNames;
    }

    public void setCategoryTagNames (List<String> categoryTagNames) {
        this.categoryTagNames = categoryTagNames;
    }
}
