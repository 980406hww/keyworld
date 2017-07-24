package com.keymanager.util;


public class DomainPercentageUtils {
	public static String randomUrl(String urlPercentage) throws Exception{
		if(!Utils.isNullOrEmpty(urlPercentage)){
			long randomValue = Math.round(Math.random() * 100); 
			String[] recordArray = urlPercentage.trim().split(";");
			long totalValue = 0;
			for(String record : recordArray){
				String[] partArray = record.split("=");
				if(partArray.length == 2 && !Utils.isNullOrEmpty(partArray[0]) && !Utils.isNullOrEmpty(partArray[1])){
					try{
						int percentage = Integer.parseInt(partArray[1]);
						totalValue = totalValue + percentage;
						if(randomValue < totalValue){
							return partArray[0];
						}
					}catch(Exception ex){
						ex.printStackTrace();
						throw new Exception("search Customer Error!");
					}
				}
			}
		}
		return "Error config value";
	}
}
