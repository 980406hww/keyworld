package com.keymanager.enums;

public enum CustomerKeywordStatus {
	Active("激活", 1), Inactive("过期", 0), New("新增", 2);

	private String name;
	private int code;

	CustomerKeywordStatus(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public int getCode() {
		return code;
	}

	public static CustomerKeywordStatus findByName(String name) {
		for (CustomerKeywordStatus customerKeywordStatus : CustomerKeywordStatus.values()) {
			if (customerKeywordStatus.getName().equalsIgnoreCase(name)) {
				return customerKeywordStatus;
			}
		}
		return null;
	}

	public static CustomerKeywordStatus findByCode(int code) {
		for (CustomerKeywordStatus customerKeywordStatus : CustomerKeywordStatus.values()) {
			if (customerKeywordStatus.getCode() == code) {
				return customerKeywordStatus;
			}
		}
		return null;
	}
}
