package com.keymanager.monitoring.service;

import com.keymanager.monitoring.criteria.KeywordNegativeCriteria;
import com.keymanager.util.PropertiesLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;

@Service
public class NegativeListsSynchronizeService {
	public static final RestTemplate restTemplate = new RestTemplate();
	public static String username;
	public static String password;
	public static String webPath;

	static{
		PropertiesLoader propertiesLoader = new PropertiesLoader("classpath:/application.properties");
		username = propertiesLoader.getProperty("publicOpinion.username");
		password = propertiesLoader.getProperty("publicOpinion.password");
		webPath = propertiesLoader.getProperty("publicOpinion.webPath");
	}
	
	public Boolean negativeListsSynchronize(KeywordNegativeCriteria keywordNegativeCriteria){
		keywordNegativeCriteria.setUserName(username);
		keywordNegativeCriteria.setPassword(password);
	    return 	restTemplate.postForObject(webPath + "/external/negativeListsSynchronize", keywordNegativeCriteria, Boolean.class);
	}
}
