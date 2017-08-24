package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.math.BigDecimal;

@TableName(value = "t_customer_charge_rule_calculation")
public class CustomerChargeRuleCalculation extends BaseEntity{
	protected static final long serialVersionUID = -1101942701283949852L;


	@TableField(value="fCustomerChargeTypeUuid")
	private int customerChargeTypeUuid;

	@TableField(value = "fChargeDataType")
	private String chargeDataType;

	@TableField(value = "fOperationType")
	private String operationType;

	@TableField(value = "fChargesOfFirst")
	private BigDecimal chargesOfFirst;

	@TableField(value = "fChargesOfSecond")
	private BigDecimal chargesOfSecond;

	@TableField(value = "fChargesOfThird")
	private BigDecimal chargesOfThird;

	@TableField(value = "fChargesOfFourth")
	private BigDecimal chargesOfFourth;

	@TableField(value = "fChargesOfFifth")
	private BigDecimal chargesOfFifth;

	@TableField(value = "fChargesOfFirstPage")
	private BigDecimal chargesOfFirstPage;

	public int getCustomerChargeTypeUuid() {
		return customerChargeTypeUuid;
	}

	public void setCustomerChargeTypeUuid(int customerChargeTypeUuid) {
		this.customerChargeTypeUuid = customerChargeTypeUuid;
	}

	public String getChargeDataType() {
		return chargeDataType;
	}

	public void setChargeDataType(String chargeDataType) {
		this.chargeDataType = chargeDataType;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public BigDecimal getChargesOfFirst() {
		return chargesOfFirst;
	}

	public void setChargesOfFirst(BigDecimal chargesOfFirst) {
		this.chargesOfFirst = chargesOfFirst;
	}

	public BigDecimal getChargesOfSecond() {
		return chargesOfSecond;
	}

	public void setChargesOfSecond(BigDecimal chargesOfSecond) {
		this.chargesOfSecond = chargesOfSecond;
	}

	public BigDecimal getChargesOfThird() {
		return chargesOfThird;
	}

	public void setChargesOfThird(BigDecimal chargesOfThird) {
		this.chargesOfThird = chargesOfThird;
	}

	public BigDecimal getChargesOfFourth() {
		return chargesOfFourth;
	}

	public void setChargesOfFourth(BigDecimal chargesOfFourth) {
		this.chargesOfFourth = chargesOfFourth;
	}

	public BigDecimal getChargesOfFifth() {
		return chargesOfFifth;
	}

	public void setChargesOfFifth(BigDecimal chargesOfFifth) {
		this.chargesOfFifth = chargesOfFifth;
	}

	public BigDecimal getChargesOfFirstPage() {
		return chargesOfFirstPage;
	}

	public void setChargesOfFirstPage(BigDecimal chargesOfFirstPage) {
		this.chargesOfFirstPage = chargesOfFirstPage;
	}
}
