package com.keymanager.ckadmin.criteria;

import com.keymanager.ckadmin.criteria.base.BaseCriteria;

/**
 * @ClassName AlgorithmTestCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/2 9:29
 * @Version 1.0
 */
public class AlgorithmTestCriteria extends BaseCriteria {
    private String algorithmTestPlanName;
    private String operationCombineName;
    private String machineGroup;

    private String terminalType;

    private String searchEngine;

    public String getTerminalType() {
        return terminalType;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getAlgorithmTestPlanName() {
        return algorithmTestPlanName;
    }

    public void setAlgorithmTestPlanName(String algorithmTestPlanName) {
        this.algorithmTestPlanName = algorithmTestPlanName;
    }

    public String getOperationCombineName() {
        return operationCombineName;
    }

    public void setOperationCombineName(String operationCombineName) {
        this.operationCombineName = operationCombineName;
    }

    public String getMachineGroup() {
        return machineGroup;
    }

    public void setMachineGroup(String machineGroup) {
        this.machineGroup = machineGroup;
    }
}
