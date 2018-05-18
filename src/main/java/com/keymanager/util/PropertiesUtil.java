package com.keymanager.util;

import org.springframework.web.client.RestTemplate;

public class PropertiesUtil {
	public static final RestTemplate restTemplate = new RestTemplate();
	public static String pcWebPath;

	static{
		PropertiesLoader propertiesLoader = new PropertiesLoader("file:" + FileUtil.getFilePath() + "/application.properties");
		pcWebPath = propertiesLoader.getProperty("pcWebPath");
	}
}
