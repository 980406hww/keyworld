package com.keymanager.monitoring.criteria;

import java.util.List;

/**
 * @Author zhoukai
 * @Date 2019/5/16 11:08
 **/
public class GroupBatchCriteria {

    private List<GroupSettingCriteria> optimizationGroupList;

    public List<GroupSettingCriteria> getOptimizationGroupList () {
        return optimizationGroupList;
    }

    public void setOptimizationGroupList (List<GroupSettingCriteria> optimizationGroupList) {
        this.optimizationGroupList = optimizationGroupList;
    }
}
