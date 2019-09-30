package com.keymanager.ckadmin.service.impl;

import com.keymanager.monitoring.criteria.KeywordNegativeCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("negativeListsSynchronizeService2")
public class NegativeListsSynchronizeService {
	private static Logger logger = LoggerFactory.getLogger(NegativeListsSynchronizeService.class);
	public static final RestTemplate restTemplate = new RestTemplate();
	private String username;
	private String password;
	private String webPath;


	public Boolean negativeListsSynchronize(KeywordNegativeCriteria keywordNegativeCriteria){
		keywordNegativeCriteria.setUserName(username);
		keywordNegativeCriteria.setPassword(password);
		return restTemplate.postForObject(webPath + "/external/negativeListsSynchronize", keywordNegativeCriteria, Boolean.class);
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
