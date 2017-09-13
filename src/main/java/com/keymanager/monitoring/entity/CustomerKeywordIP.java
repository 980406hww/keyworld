package com.keymanager.monitoring.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;

import java.sql.Timestamp;

@TableName(value = "t_customer_keyword_ip")
public class CustomerKeywordIP {
	@TableField(value = "fUuid")
	private int uuid;

	@TableField(value = "fCustomerKeywordUuid")
	private Long customerKeywordUuid;

	@TableField(value = "fIP")
	private String ip;

	@TableField(value = "fCity")
	private String city;

	@TableField(value = "fCreateTime")
	private Timestamp createTime;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public Long getCustomerKeywordUuid() {
		return customerKeywordUuid;
	}

	public void setCustomerKeywordUuid(Long customerKeywordUuid) {
		this.customerKeywordUuid = customerKeywordUuid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Timestamp getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Timestamp createTime) {
		this.createTime = createTime;
	}
}
