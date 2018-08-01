package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.criteria.CookieCriteria;
import com.keymanager.monitoring.dao.CookieDao;
import com.keymanager.monitoring.entity.ClientCookie;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.Cookie;
import com.keymanager.monitoring.vo.CookieVO;
import com.keymanager.util.Constants;
import com.keymanager.util.FileUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	public ClientCookie getCookieStrForClient(String clientID) throws Exception {
		ClientCookie clientCookie = clientCookieService.findClientCookieByClientID(clientID);
		return clientCookie;
	}

	public void allotCookieStr(String searchEngine, ClientCookie clientCookie, String type, int count) {
		StringBuilder stringBuilder = new StringBuilder();
		while (count > 0) {
			Cookie cookie = cookieDao.getCookie(searchEngine);
			if(cookie != null) {
				if(cookie.getCookieCount() > count) {
					int index = StringUtils.ordinalIndexOf(cookie.getCookieStr(), "\r\n", count);
					String cookieSub = cookie.getCookieStr().substring(0, index + 2);
					stringBuilder.append(cookieSub);
					cookie.setCookieStr(cookie.getCookieStr().substring(index + 2));
					cookie.setCookieCount(cookie.getCookieCount() - count);
					cookieDao.updateById(cookie);
					count = 0;
				} else {
					// 数量不满足，需要再次循环
					stringBuilder.append(cookie.getCookieStr());
					cookieDao.deleteById(cookie);
					count = count - cookie.getCookieCount();
				}
			} else {
				count = 0;
			}
        }
		if(type.equals("update")) {
            clientCookie.setCookieStr(clientCookie.getCookieStr() + stringBuilder.toString());
			clientCookie.setCookieCount(StringUtils.countMatches(clientCookie.getCookieStr(), "\r\n"));
            clientCookieService.updateById(clientCookie);
        } else {
            clientCookie.setCookieStr(stringBuilder.toString());
			clientCookie.setCookieCount(StringUtils.countMatches(clientCookie.getCookieStr(), "\r\n"));
            clientCookieService.insert(clientCookie);
        }
	}

	private String getSearchEngineForClientID(String clientID) {
		ClientStatus clientStatus = clientStatusService.selectById(clientID);
		Config cookieGroupForBaiduConfig = configService.getConfig(Constants.CONFIG_TYPE_COOKIE_GROUP, Constants.SEARCH_ENGINE_BAIDU);
		Config cookieGroupFor360Config = configService.getConfig(Constants.CONFIG_TYPE_COOKIE_GROUP, Constants.SEARCH_ENGINE_360);
		String searchEngine = null;
		if(clientStatus.getSwitchGroupName().contains(cookieGroupForBaiduConfig.getValue())) {
			searchEngine = Constants.SEARCH_ENGINE_BAIDU;
		} else if (clientStatus.getSwitchGroupName().contains(cookieGroupFor360Config.getValue())) {
			searchEngine = Constants.SEARCH_ENGINE_360;
		}
		return searchEngine;
	}

	public Page<Cookie> searchCookies(Page<Cookie> page, CookieCriteria cookieCriteria) {
		page.setRecords(cookieDao.searchCookies(page, cookieCriteria));
		return page;
	}

	public void saveCookieByFile(String filePath, String searchEngine) throws Exception {
		String cookieStr = FileUtil.readTxtFile(filePath, "UTF-8");
		if(StringUtils.isNotBlank(cookieStr)) {
			int subCount = 1000;
			int total = StringUtils.countMatches(cookieStr, "\r\n");
			for(int i = 0; i < total / subCount; i++) {
				Cookie cookie = new Cookie();
				cookie.setCookieCount(subCount);
				int index = StringUtils.ordinalIndexOf(cookieStr, "\r\n", subCount);
				cookie.setCookieStr(cookieStr.substring(0, index + 2));
				cookieStr = cookieStr.substring(index + 2);
				cookie.setSearchEngine(searchEngine);
				cookie.setCreateTime(new Date());
				cookieDao.insert(cookie);
			}
			if (StringUtils.isNotBlank(cookieStr)) {
				Cookie cookie = new Cookie();
				cookie.setCookieCount(StringUtils.countMatches(cookieStr, "\r\n"));
				cookie.setCookieStr(cookieStr);
				cookie.setSearchEngine(searchEngine);
				cookie.setCreateTime(new Date());
				cookieDao.insert(cookie);
			}
		}
	}

	public void allotCookieForClient() {
		synchronized (Cookie.class) {
			Config clientCookieCountConfig = configService.getConfig(Constants.CONFIG_KEY_CLIENT_COOKIE_COUNT, Constants.CONFIG_KEY_CLIENT_COOKIE_COUNT);
			Config cookieGroupForBaiduConfig = configService.getConfig(Constants.CONFIG_TYPE_COOKIE_GROUP, Constants.SEARCH_ENGINE_BAIDU);
			Config cookieGroupFor360Config = configService.getConfig(Constants.CONFIG_TYPE_COOKIE_GROUP, Constants.SEARCH_ENGINE_360);
			int clientCookieCount = Integer.parseInt(clientCookieCountConfig.getValue());
			// 获取需要分配cookie的机器
			List<CookieVO> clientList = clientStatusService.searchClientForAllotCookie(clientCookieCount, cookieGroupForBaiduConfig.getValue(), cookieGroupFor360Config.getValue());
			for (CookieVO client : clientList) {
				int count = clientCookieCount - client.getCookieCount();
				String searchEngine = Constants.SEARCH_ENGINE_BAIDU;
				if (client.getSwitchGroupName().contains(Constants.CONFIG_KEY_360)) {
					searchEngine = Constants.CONFIG_KEY_360;
				}
				ClientCookie clientCookie = clientCookieService.findClientCookieByClientID(client.getClientID());
				String type = "update";
				if (clientCookie == null) {
					type = "insert";
					clientCookie = new ClientCookie();
					clientCookie.setClientID(client.getClientID());
					clientCookie.setCreateTime(new Date());
				}
				allotCookieStr(searchEngine, clientCookie, type, count);
			}
		}
	}
}
