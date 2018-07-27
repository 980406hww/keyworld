package com.keymanager.monitoring.vo;

/**
 * Created by shunshikj08 on 2018/7/16.
 */
public class CustomerChargeStatVO {
    private int chargeTotal;
    private int planChargeAmount;
    private int actualChargeAmount;

    public int getChargeTotal() {
        return chargeTotal;
    }

    public void setChargeTotal(int chargeTotal) {
        this.chargeTotal = chargeTotal;
    }

    public int getPlanChargeAmount() {
        return planChargeAmount;
    }

    public void setPlanChargeAmount(int planChargeAmount) {
        this.planChargeAmount = planChargeAmount;
    }

    public int getActualChargeAmount() {
        return actualChargeAmount;
    }

    public void setActualChargeAmount(int actualChargeAmount) {
        this.actualChargeAmount = actualChargeAmount;
    }
}
