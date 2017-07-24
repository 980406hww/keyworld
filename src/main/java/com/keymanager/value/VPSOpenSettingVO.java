package com.keymanager.value;


public class VPSOpenSettingVO {
	private String providerIds;
	private int providerId;
	private int count;
	private String operationType;

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public String getProviderIds() {
		return providerIds;
	}

	public void setProviderIds(String providerIds) {
		this.providerIds = providerIds;
	}
}
