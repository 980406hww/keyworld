package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;
import com.keymanager.monitoring.entity.BaseEntity;

/**
 * @ClassName CustomerExcludeKeyword
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/10 14:42
 * @Version 1.0
 */
@TableName(value = "t_exclude_keyword")
public class CustomerExcludeKeyword extends BaseEntity {

    @TableField(value = "fQZSettingUuid")
    private Long qzSettingUuid;

    @TableField(value = "fCustomerUuid")
    private Long customerUuid;

    @TableField(value = "fKeyword")
    private String keyword;

    @TableField(value = "fTerminalType")
    private String terminalType;

    public Long getQzSettingUuid() {
        return qzSettingUuid;
    }

    public void setQzSettingUuid(Long qzSettingUuid) {
        this.qzSettingUuid = qzSettingUuid;
    }

    public Long getCustomerUuid() {
        return customerUuid;
    }

    public void setCustomerUuid(Long customerUuid) {
        this.customerUuid = customerUuid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

}
