package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName(value = "t_customer_charge_log")
public class CustomerChargeLog extends BaseEntity {

	@TableField(value = "fCustomerUuid")
	private int customerUuid;

	@TableField(value = "fPlanChargeAmount")
	private int planChargeAmount;

	@TableField(value = "fActualChargeAmount")
	private int actualChargeAmount;

	@TableField(value = "fCashier")
	private String cashier;

	public int getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(int customerUuid) {
		this.customerUuid = customerUuid;
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

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}
}
