package com.keymanager.enums;

public enum KeywordType {
	CustomerKeyword("CustomerKeyword", 50, "Baidutop123优化的关键字"), 
	RelatedKeyword("RelatedKeyword", 20, "Baidutop123优化的关键字相关的那些关键字"), 
	PublicKeyword("PublicKeyword", 20, "一些通用的新闻关键字");
	
	private String code;
	private int fetchRecordCount;
	private String description;
	
	
	KeywordType(String code, int fetchRecoundCount, String description){
		this.code = code;
		this.fetchRecordCount = fetchRecoundCount;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public int getFetchRecordCount() {
		return fetchRecordCount;
	}

	public String getDescription() {
		return description;
	}

	public static KeywordType findByName(String name) {
		for (KeywordType keywordType : KeywordType.values()) {
			if (keywordType.name().equalsIgnoreCase(name)) {
				return keywordType;
			}
		}
		return null;
	}
	
}
