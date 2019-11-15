package com.keymanager.ckadmin.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_customer_charge_type_percentage")
public class CustomerChargeTypePercentage extends BaseEntity {

    @TableField(value = "fCustomerChargeTypeUuid")
    private int customerChargeTypeUuid;

    @TableField(value = "fOperationType")
    private String operationType;

    @TableField(value = "fFirstChargePercentage")
    private int firstChargePercentage;

    @TableField(value = "fSecondChargePercentage")
    private int secondChargePercentage;

    @TableField(value = "fThirdChargePercentage")
    private int thirdChargePercentage;

    @TableField(value = "fFourthChargePercentage")
    private int fourthChargePercentage;

    @TableField(value = "fFifthChargePercentage")
    private int fifthChargePercentage;

    @TableField(value = "fFirstPageChargePercentage")
    private int firstPageChargePercentage;

    public int getCustomerChargeTypeUuid() {
        return customerChargeTypeUuid;
    }

    public void setCustomerChargeTypeUuid(int customerChargeTypeUuid) {
        this.customerChargeTypeUuid = customerChargeTypeUuid;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public int getFirstChargePercentage() {
        return firstChargePercentage;
    }

    public void setFirstChargePercentage(int firstChargePercentage) {
        this.firstChargePercentage = firstChargePercentage;
    }

    public int getSecondChargePercentage() {
        return secondChargePercentage;
    }

    public void setSecondChargePercentage(int secondChargePercentage) {
        this.secondChargePercentage = secondChargePercentage;
    }

    public int getThirdChargePercentage() {
        return thirdChargePercentage;
    }

    public void setThirdChargePercentage(int thirdChargePercentage) {
        this.thirdChargePercentage = thirdChargePercentage;
    }

    public int getFourthChargePercentage() {
        return fourthChargePercentage;
    }

    public void setFourthChargePercentage(int fourthChargePercentage) {
        this.fourthChargePercentage = fourthChargePercentage;
    }

    public int getFifthChargePercentage() {
        return fifthChargePercentage;
    }

    public void setFifthChargePercentage(int fifthChargePercentage) {
        this.fifthChargePercentage = fifthChargePercentage;
    }

    public int getFirstPageChargePercentage() {
        return firstPageChargePercentage;
    }

    public void setFirstPageChargePercentage(int firstPageChargePercentage) {
        this.firstPageChargePercentage = firstPageChargePercentage;
    }
}
