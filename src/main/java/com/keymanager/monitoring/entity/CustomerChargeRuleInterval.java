package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;

@TableName(value = "t_customer_charge_rule_interval")
public class CustomerChargeRuleInterval extends BaseEntity{
	protected static final long serialVersionUID = -1101942701283949852L;

	@TableField(value="fCustomerChargeTypeUuid")
	private int customerChargeTypeUuid;

	@TableField(value = "fOperationType")
	private String operationType;

	@TableField(value = "fStartIndex")
	private int startIndex;

	@TableField(value = "fEndIndex")
	private int endIndex;

	@TableField(value = "fPrice")
	private BigDecimal price;

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

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
