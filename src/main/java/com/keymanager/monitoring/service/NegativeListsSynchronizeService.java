package com.keymanager.monitoring.service;

import com.keymanager.monitoring.criteria.KeywordNegativeCriteria;
import com.keymanager.util.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;

@Service
public class NegativeListsSynchronizeService {
	private static Logger logger = LoggerFactory.getLogger(NegativeListsSynchronizeService.class);
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
		try {
			return restTemplate.postForObject(webPath + "/external/negativeListsSynchronize", keywordNegativeCriteria, Boolean.class);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}
}
