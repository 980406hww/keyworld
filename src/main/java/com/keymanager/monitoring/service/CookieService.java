package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.CookieDao;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.Cookie;
import com.keymanager.monitoring.enums.CookieStatusEnum;
import com.keymanager.util.Constants;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CookieService extends ServiceImpl<CookieDao, Cookie> {
	private static Logger logger = LoggerFactory.getLogger(CookieService.class);

	@Autowired
	private CookieDao cookieDao;

	@Autowired
	private ConfigService configService;

	@Autowired
	private ClientCookieService clientCookieService;

	@Autowired
	private ClientStatusService clientStatusService;

	public List<Cookie> updateInvalidCookies(String clientID, List<Cookie> invalidCookies) {
		List<Cookie> cookieList = null;
		if(CollectionUtils.isNotEmpty(invalidCookies)) {
			cookieDao.updateCookieStatus(invalidCookies, CookieStatusEnum.invalid.name());
			// 获取cookie类型
			String searchEngine = getSearchEngineClientID(clientID);
			// 删除无效cookie映射
			clientCookieService.deleteClientCookies(CookieStatusEnum.invalid.name());
			// 分配新的cookie到终端
			Config config = configService.getConfig(Constants.CONFIG_KEY_CLIENT_COOKIE_COUNT, Constants.CONFIG_KEY_CLIENT_COOKIE_COUNT);
			int total = clientCookieService.findCookieCountByClientID(clientID);
			total = Integer.parseInt(config.getValue()) - total;
			if(total > 0) {
				cookieList = cookieDao.findCookies(total, searchEngine, CookieStatusEnum.normal.name());
				if(CollectionUtils.isNotEmpty(cookieList)) {
					cookieDao.updateCookieStatus(cookieList, CookieStatusEnum.inUse.name());
					clientCookieService.batchInsertClientCookie(clientID, cookieList);
					clientStatusService.updateCookieCount(clientID);
				}
			}
		}
		return cookieList;
	}

	public List<Cookie> getCookieList(String clientID, int cookieCount) {
		String searchEngine = getSearchEngineClientID(clientID);
		List<Cookie> cookieList = cookieDao.findCookies(cookieCount, searchEngine, CookieStatusEnum.normal.name());
		if(CollectionUtils.isNotEmpty(cookieList)) {
			cookieDao.updateCookieStatus(cookieList, CookieStatusEnum.inUse.name());
			clientCookieService.batchInsertClientCookie(clientID, cookieList);
			clientStatusService.updateCookieCount(clientID);
		}
		return cookieList;
	}

	private String getSearchEngineClientID(String clientID) {
		ClientStatus clientStatus = clientStatusService.selectById(clientID);
		String searchEngine = Constants.SEARCH_ENGINE_BAIDU;
		if(clientStatus.getOperationType().contains(Constants.CONFIG_KEY_360)) {
			searchEngine = Constants.CONFIG_KEY_360;
		}
		return searchEngine;
	}
}
