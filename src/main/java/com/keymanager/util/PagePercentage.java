package com.keymanager.util;

import java.util.HashMap;
import java.util.Map;

public class PagePercentage {
	private int minPercentage;
	private int maxPercentage;
	private int minDefaultValue;
	private int maxDefaultValue;
	private int pageNumber;

	public static PagePercentage fetchPagePercentage(String pagePercentageSetting, int pageNumber) {
		Map<Integer, PagePercentage> pagePercentageSettingMap = parsePagePercentageString(pagePercentageSetting);
		return pagePercentageSettingMap.get(pageNumber);
	}
	
	public PagePercentage(int pageNumber, int minPercentage, int maxPercentage, int minDefaultValue, int maxDefaultValue){
		this.pageNumber = pageNumber;
		this.minPercentage = minPercentage;
		this.maxPercentage = maxPercentage;
		this.minDefaultValue = minDefaultValue;
		this.maxDefaultValue = maxDefaultValue;
	}

	private static Map<Integer, PagePercentage> parsePagePercentageString(String wholePagePercentageSetting){
		Map<Integer, PagePercentage> pagePercentageSettingMap = new HashMap<Integer, PagePercentage>();
		if(!Utils.isNullOrEmpty(wholePagePercentageSetting)){
			String[] eachPagePercentageSettings = wholePagePercentageSetting.split(";");
			for(String eachPagePercentageSetting : eachPagePercentageSettings){
				String [] elements = eachPagePercentageSetting.split(":");
				if(elements.length == 5){
					pagePercentageSettingMap.put(Integer.parseInt(elements[0]), new PagePercentage(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]), Integer.parseInt(elements[2]), Integer.parseInt(elements[3]), Integer.parseInt(elements[4])));
				}else{
					throw new RuntimeException("Wrong page percentage setting!!!" + wholePagePercentageSetting);
				}
			}
		}
		return pagePercentageSettingMap;
	}
	
	public int getMinDefaultValue() {
		return minDefaultValue;
	}

	public int getMaxDefaultValue() {
		return maxDefaultValue;
	}
	
	public int getPageNumber() {
		return pageNumber;
	}

	public int getMinPercentage() {
		return minPercentage;
	}

	public int getMaxPercentage() {
		return maxPercentage;
	}
}
