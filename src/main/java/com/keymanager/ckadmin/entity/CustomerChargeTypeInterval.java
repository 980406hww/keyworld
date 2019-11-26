package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import java.math.BigDecimal;

@TableName(value = "t_customer_charge_type_interval")
public class CustomerChargeTypeInterval extends BaseEntity {

    protected static final long serialVersionUID = -1101942701283949852L;

    @TableField(value = "fCustomerChargeTypeUuid")
    private long customerChargeTypeUuid;

    @TableField(value = "fOperationType")
    private String operationType;

    @TableField(value = "fStartIndex")
    private Integer startIndex;

    @TableField(value = "fEndIndex")
    private Integer endIndex;

    @TableField(value = "fPrice")
    private BigDecimal price;

    public long getCustomerChargeTypeUuid() {
        return customerChargeTypeUuid;
    }

    public void setCustomerChargeTypeUuid(long customerChargeTypeUuid) {
        this.customerChargeTypeUuid = customerChargeTypeUuid;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public Integer getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

    public Integer getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(Integer endIndex) {
        this.endIndex = endIndex;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
