package com.keymanager.ckadmin.criteria;


import com.keymanager.ckadmin.criteria.base.BaseCriteria;

public class ScreenedWebsiteCriteria extends BaseCriteria {

    private String optimizeGroupName;//优化组名
    private String groupNameFuzzyQuery;

    public String getOptimizeGroupName() {
        return optimizeGroupName;
    }

    public void setOptimizeGroupName(String optimizeGroupName) {
        this.optimizeGroupName = optimizeGroupName;
    }

    public String getGroupNameFuzzyQuery() {
        return groupNameFuzzyQuery;
    }

    public void setGroupNameFuzzyQuery(String groupNameFuzzyQuery) {
        this.groupNameFuzzyQuery = groupNameFuzzyQuery;
    }
}
