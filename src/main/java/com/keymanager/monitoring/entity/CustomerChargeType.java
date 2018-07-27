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
	private String chargeType;

	@TableField(exist = false)
	private List<CustomerChargeTypeCalculation> customerChargeTypeCalculations;

	@TableField(exist = false)
	private List<CustomerChargeTypeInterval> customerChargeTypeIntervals;

	@TableField(exist = false)
	private List<CustomerChargeTypePercentage> customerChargeTypePercentages;

	public Integer getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(Integer customerUuid) {
		this.customerUuid = customerUuid;
	}

	public String getChargeType() {
		return chargeType;
	}

	public void setChargeType(String chargeType) {
		this.chargeType = chargeType;
	}

	public List<CustomerChargeTypeCalculation> getCustomerChargeTypeCalculations() {
		return customerChargeTypeCalculations;
	}

	public void setCustomerChargeTypeCalculations(List<CustomerChargeTypeCalculation> customerChargeTypeCalculations) {
		this.customerChargeTypeCalculations = customerChargeTypeCalculations;
	}

	public List<CustomerChargeTypeInterval> getCustomerChargeTypeIntervals() {
		return customerChargeTypeIntervals;
	}

	public void setCustomerChargeTypeIntervals(List<CustomerChargeTypeInterval> customerChargeTypeIntervals) {
		this.customerChargeTypeIntervals = customerChargeTypeIntervals;
	}

	public List<CustomerChargeTypePercentage> getCustomerChargeTypePercentages() {
		return customerChargeTypePercentages;
	}

	public void setCustomerChargeTypePercentages(List<CustomerChargeTypePercentage> customerChargeTypePercentages) {
		this.customerChargeTypePercentages = customerChargeTypePercentages;
	}
}
