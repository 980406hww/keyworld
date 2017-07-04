package com.keymanager.enums;

public enum CollectMethod {
	PerDay("按天", "PerDay", 1),
	PerTenDay("十天", "PerTenDay", 10),
	PerSevenDay("七天", "PerSevenDay", 7),
	PerMonth("按月", "PerMonth", 30);
	
	private String name;
	private String code;
	private int duration;
	
	CollectMethod(String name, String code, int duration){
		this.name = name;
		this.code = code;
		this.duration = duration;
	}
	public String getName() {
		return name;
	}
	public String getCode() {
		return code;
	}
	public int getDuration() {
		return duration;
	}
	
	public static CollectMethod findByName(String name){
		for (CollectMethod collectMethod : CollectMethod.values()){
			if (collectMethod.getName().equalsIgnoreCase(name)){
				return collectMethod;
			}			
		}
		return null;
	}
	
	public static CollectMethod findByCode(String code){
		for (CollectMethod collectMethod : CollectMethod.values()){
			if (collectMethod.getCode().equalsIgnoreCase(code)){
				return collectMethod;
			}			
		}
		return null;
	}
}
