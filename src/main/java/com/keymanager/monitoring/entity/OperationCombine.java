package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * @Author zhoukai
 * @Date 2019/6/24 13:50
 **/
@TableName(value = "t_operation_combine")
public class OperationCombine extends BaseEntity {

    @TableField(value = "fOperationCombineName")
    private String operationCombineName;

    public String getOperationCombineName () {
        return operationCombineName;
    }

    public void setOperationCombineName (String operationCombineName) {
        this.operationCombineName = operationCombineName;
    }
}
