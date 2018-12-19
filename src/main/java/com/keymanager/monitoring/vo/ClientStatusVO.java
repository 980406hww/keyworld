package com.keymanager.monitoring.vo;

/**
 * @Author zhoukai
 * @Date 2018/12/18 15:09
 **/
public class ClientStatusVO {
    private String operationType;
    private int operationTypeCount;

    public String getOperationType () {
        return operationType;
    }

    public void setOperationType (String operationType) {
        this.operationType = operationType;
    }

    public int getOperationTypeCount () {
        return operationTypeCount;
    }

    public void setOperationTypeCount (int operationTypeCount) {
        this.operationTypeCount = operationTypeCount;
    }
}
