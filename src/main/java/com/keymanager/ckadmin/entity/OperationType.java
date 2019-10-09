package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @author wjianwu 2019/4/22 17:11
 */
@TableName(value = "t_operation_type")
public class OperationType extends BaseEntity {

    @TableField(value = "fOperationTypeName")
    private String operationTypeName;

    @TableField(value = "fTerminalType")
    private String terminalType;

    @TableField(value = "fDescription")
    private String description;

    @TableField(value = "fRemark")
    private String remark;

    @TableField(value = "fStatus")
    private Integer status;

    public String getOperationTypeName() {
        return operationTypeName;
    }

    public void setOperationTypeName(String operationTypeName) {
        this.operationTypeName = operationTypeName;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
