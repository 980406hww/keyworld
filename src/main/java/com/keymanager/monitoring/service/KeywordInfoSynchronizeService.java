package com.keymanager.monitoring.service;

import com.keymanager.monitoring.vo.KeywordInfoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

public class KeywordInfoSynchronizeService {
	private static Logger logger = LoggerFactory.getLogger(KeywordInfoSynchronizeService.class);
	public static final RestTemplate restTemplate = new RestTemplate();

	public KeywordInfoVO getKeywordList(String webPath, Map map) throws Exception {
		KeywordInfoVO keywords = restTemplate.postForObject(webPath + "/external/getCustomerKeyword", map, KeywordInfoVO.class);
		return keywords;
	}

	public Boolean deleteKeywordList(String webPath, Map map) throws Exception {
		return restTemplate.postForObject(webPath + "/external/delteCustomerKeyword", map, Boolean.class);
	}

}
