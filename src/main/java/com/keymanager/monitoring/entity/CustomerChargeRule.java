package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldStrategy;

import java.util.Date;

@TableName(value = "t_customer_charge_rule")
public class CustomerChargeRule extends BaseEntity {

	@TableField(exist = false)
	private String contactPerson;

	@TableField(value = "fCustomerUuid")
	private int customerUuid;

	@TableField(value = "fChargeTotal")
	private int chargeTotal;

	@TableField(value = "fJanuaryFee")
	private int januaryFee;

	@TableField(value = "fFebruaryFee")
	private int februaryFee;

	@TableField(value = "fMarchFee")
	private int marchFee;

	@TableField(value = "fAprilFee")
	private int aprilFee;

	@TableField(value = "fMayFee")
	private int mayFee;

	@TableField(value = "fJuneFee")
	private int juneFee;

	@TableField(value = "fJulyFee")
	private int julyFee;

	@TableField(value = "fAugustFee")
	private int augustFee;

	@TableField(value = "fSeptemberFee")
	private int septemberFee;

	@TableField(value = "fOctoberFee")
	private int octoberFee;

	@TableField(value = "fNovemberFee")
	private int novemberFee;

	@TableField(value = "fDecemberFee")
	private int decemberFee;

	@TableField(value = "fJanuaryRate")
	private int januaryRate;

	@TableField(value = "fFebruaryRate")
	private int februaryRate;

	@TableField(value = "fMarchRate")
	private int marchRate;

	@TableField(value = "fAprilRate")
	private int aprilRate;

	@TableField(value = "fMayRate")
	private int mayRate;

	@TableField(value = "fJuneRate")
	private int juneRate;

	@TableField(value = "fJulyRate")
	private int julyRate;

	@TableField(value = "fAugustRate")
	private int augustRate;

	@TableField(value = "fSeptemberRate")
	private int septemberRate;

	@TableField(value = "fOctoberRate")
	private int octoberRate;

	@TableField(value = "fNovemberRate")
	private int novemberRate;

	@TableField(value = "fDecemberRate")
	private int decemberRate;

	@TableField(value = "fNextChargeDate")
	private Date nextChargeDate;

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public int getCustomerUuid() {
		return customerUuid;
	}

	public void setCustomerUuid(int customerUuid) {
		this.customerUuid = customerUuid;
	}

	public int getChargeTotal() {
		return chargeTotal;
	}

	public void setChargeTotal(int chargeTotal) {
		this.chargeTotal = chargeTotal;
	}

	public int getJanuaryFee() {
		return januaryFee;
	}

	public void setJanuaryFee(int januaryFee) {
		this.januaryFee = januaryFee;
	}

	public int getFebruaryFee() {
		return februaryFee;
	}

	public void setFebruaryFee(int februaryFee) {
		this.februaryFee = februaryFee;
	}

	public int getMarchFee() {
		return marchFee;
	}

	public void setMarchFee(int marchFee) {
		this.marchFee = marchFee;
	}

	public int getAprilFee() {
		return aprilFee;
	}

	public void setAprilFee(int aprilFee) {
		this.aprilFee = aprilFee;
	}

	public int getMayFee() {
		return mayFee;
	}

	public void setMayFee(int mayFee) {
		this.mayFee = mayFee;
	}

	public int getJuneFee() {
		return juneFee;
	}

	public void setJuneFee(int juneFee) {
		this.juneFee = juneFee;
	}

	public int getJulyFee() {
		return julyFee;
	}

	public void setJulyFee(int julyFee) {
		this.julyFee = julyFee;
	}

	public int getAugustFee() {
		return augustFee;
	}

	public void setAugustFee(int augustFee) {
		this.augustFee = augustFee;
	}

	public int getSeptemberFee() {
		return septemberFee;
	}

	public void setSeptemberFee(int septemberFee) {
		this.septemberFee = septemberFee;
	}

	public int getOctoberFee() {
		return octoberFee;
	}

	public void setOctoberFee(int octoberFee) {
		this.octoberFee = octoberFee;
	}

	public int getNovemberFee() {
		return novemberFee;
	}

	public void setNovemberFee(int novemberFee) {
		this.novemberFee = novemberFee;
	}

	public int getDecemberFee() {
		return decemberFee;
	}

	public void setDecemberFee(int decemberFee) {
		this.decemberFee = decemberFee;
	}

	public int getJanuaryRate() {
		return januaryRate;
	}

	public void setJanuaryRate(int januaryRate) {
		this.januaryRate = januaryRate;
	}

	public int getFebruaryRate() {
		return februaryRate;
	}

	public void setFebruaryRate(int februaryRate) {
		this.februaryRate = februaryRate;
	}

	public int getMarchRate() {
		return marchRate;
	}

	public void setMarchRate(int marchRate) {
		this.marchRate = marchRate;
	}

	public int getAprilRate() {
		return aprilRate;
	}

	public void setAprilRate(int aprilRate) {
		this.aprilRate = aprilRate;
	}

	public int getMayRate() {
		return mayRate;
	}

	public void setMayRate(int mayRate) {
		this.mayRate = mayRate;
	}

	public int getJuneRate() {
		return juneRate;
	}

	public void setJuneRate(int juneRate) {
		this.juneRate = juneRate;
	}

	public int getJulyRate() {
		return julyRate;
	}

	public void setJulyRate(int julyRate) {
		this.julyRate = julyRate;
	}

	public int getAugustRate() {
		return augustRate;
	}

	public void setAugustRate(int augustRate) {
		this.augustRate = augustRate;
	}

	public int getSeptemberRate() {
		return septemberRate;
	}

	public void setSeptemberRate(int septemberRate) {
		this.septemberRate = septemberRate;
	}

	public int getOctoberRate() {
		return octoberRate;
	}

	public void setOctoberRate(int octoberRate) {
		this.octoberRate = octoberRate;
	}

	public int getNovemberRate() {
		return novemberRate;
	}

	public void setNovemberRate(int novemberRate) {
		this.novemberRate = novemberRate;
	}

	public int getDecemberRate() {
		return decemberRate;
	}

	public void setDecemberRate(int decemberRate) {
		this.decemberRate = decemberRate;
	}

	public Date getNextChargeDate() {
		return nextChargeDate;
	}

	public void setNextChargeDate(Date nextChargeDate) {
		this.nextChargeDate = nextChargeDate;
	}
}
