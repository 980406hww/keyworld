package com.keymanager.value;

public class ServiceProviderVO {
	public final static String DEFAULT_SERVICE_PROVIDER = "悟天";
	private int uuid;
	private String serviceProviderName;
	private int status;

	public int getUuid() {
		return uuid;
	}

	public void setUuid(int uuid) {
		this.uuid = uuid;
	}

	public String getServiceProviderName() {
		return serviceProviderName;
	}

	public void setServiceProviderName(String serviceProviderName) {
		this.serviceProviderName = serviceProviderName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
