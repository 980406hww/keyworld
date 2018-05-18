package com.keymanager.monitoring.service;

import com.keymanager.monitoring.vo.KeywordInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

public class KeywordInfoSynchronizeService {
	private static Logger logger = LoggerFactory.getLogger(KeywordInfoSynchronizeService.class);
	public static final RestTemplate restTemplate = new RestTemplate();
	private String username;
	private String password;
	private String webPath;


	public KeywordInfoVO getKeywordList(){
		HashMap hashMap = new HashMap();
		hashMap.put("username", username);
		hashMap.put("password", password);
		try {
			KeywordInfoVO keywords = restTemplate.postForObject(webPath + "/external/getCustomerKeyword", hashMap, KeywordInfoVO.class);
			return keywords;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
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
