package com.keymanager.ckadmin.criteria;

import com.keymanager.monitoring.criteria.BaseCriteria;

/**
 * @ClassName GroupCriteria
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/20 11:43
 * @Version 1.0
 */
public class GroupCriteria extends BaseCriteria{

    private Long uuid;

    private String groupName;

    private Long operationCombineId;

    private String createBy;

    public Long getUuid() {
        return uuid;
    }

    public void setUuid(Long uuid) {
        this.uuid = uuid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getOperationCombineId() {
        return operationCombineId;
    }

    public void setOperationCombineId(Long operationCombineId) {
        this.operationCombineId = operationCombineId;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }
}
