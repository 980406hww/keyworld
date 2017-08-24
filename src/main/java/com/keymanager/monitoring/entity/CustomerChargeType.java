package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.util.List;

@TableName(value = "t_customer_charge_type")
public class CustomerChargeType extends BaseEntity{
	protected static final long serialVersionUID = -1101942701283949852L;


	@TableField(value="fCustomerUuid")
	private Integer customerUuid;

	@TableField(value = "fChargeType")
	private Integer chargeType;

	@TableField(exist = false)
	private List<CustomerChargeRuleCalculation> customerChargeRuleCalculations;

	@TableField(exist = false)
	private List<CustomerChargeRuleInterval> customerChargeRuleIntervals;

	public Integer getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(Integer customerUuid) {
		this.customerUuid = customerUuid;
	}

	public Integer getChargeType() {
		return chargeType;
	}

	public void setChargeType(Integer chargeType) {
		this.chargeType = chargeType;
	}

	public List<CustomerChargeRuleCalculation> getCustomerChargeRuleCalculations() {
		return customerChargeRuleCalculations;
	}

	public void setCustomerChargeRuleCalculations(List<CustomerChargeRuleCalculation> customerChargeRuleCalculations) {
		this.customerChargeRuleCalculations = customerChargeRuleCalculations;
	}

	public List<CustomerChargeRuleInterval> getCustomerChargeRuleIntervals() {
		return customerChargeRuleIntervals;
	}

	public void setCustomerChargeRuleIntervals(List<CustomerChargeRuleInterval> customerChargeRuleIntervals) {
		this.customerChargeRuleIntervals = customerChargeRuleIntervals;
	}
}
