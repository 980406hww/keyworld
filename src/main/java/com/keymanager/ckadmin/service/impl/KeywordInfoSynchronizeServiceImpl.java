package com.keymanager.ckadmin.service.impl;

import com.keymanager.ckadmin.service.KeywordInfoSynchronizeService;
import com.keymanager.ckadmin.vo.KeywordInfoVO;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service("keywordInfoSynchronizeService2")
public class KeywordInfoSynchronizeServiceImpl implements KeywordInfoSynchronizeService {
	private static Logger logger = LoggerFactory.getLogger(KeywordInfoSynchronizeServiceImpl.class);
	public static final RestTemplate restTemplate = new RestTemplate();

	@Override
	public KeywordInfoVO getKeywordList(String webPath, Map map) throws Exception {
		KeywordInfoVO keywords = restTemplate.postForObject(webPath + "/external/getCustomerKeyword", map, KeywordInfoVO.class);
		return keywords;
	}

	@Override
	public Boolean deleteKeywordList(String webPath, Map map) throws Exception {
		return restTemplate.postForObject(webPath + "/external/delteCustomerKeyword", map, Boolean.class);
	}

	@Override
	public String getUserReportInfo(String webPath, Map map) throws Exception {
		String reportPassword = restTemplate.postForObject(webPath + "/external/getUserReportInfo", map, String.class);
		return reportPassword;
	}

}
