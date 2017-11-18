package com.keymanager.monitoring.service;

import com.keymanager.monitoring.criteria.KeywordNegativeCriteria;
import com.keymanager.util.PropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;

public class NegativeListsSynchronizeService {
	private static Logger logger = LoggerFactory.getLogger(NegativeListsSynchronizeService.class);
	public static final RestTemplate restTemplate = new RestTemplate();
	private String username;
	private String password;
	private String webPath;


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

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setWebPath(String webPath) {
		this.webPath = webPath;
	}
}
