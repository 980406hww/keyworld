package com.keymanager.value;

public class CustomerKeywordIPVO {
	private int uuid;
	private int customerKeywordUuid;
	private String ip;
	private String city;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public int getCustomerKeywordUuid() {
		return customerKeywordUuid;
	}

	public void setCustomerKeywordUuid(int customerKeywordUuid) {
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
}
