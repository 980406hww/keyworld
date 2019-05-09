package com.keymanager.monitoring.vo;

/**
 * @Author zhoukai
 * @Date 2019/4/29 20:04
 **/
public class GroupResultVO {

    private long uuid;

    private String operationType;

    public long getUuid () {
        return uuid;
    }

    public void setUuid (long uuid) {
        this.uuid = uuid;
    }

    public String getOperationType () {
        return operationType;
    }

    public void setOperationType (String operationType) {
        this.operationType = operationType;
    }
}
