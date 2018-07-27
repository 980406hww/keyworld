package com.keymanager.monitoring.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.keymanager.monitoring.dao.ClientCookieDao;
import com.keymanager.monitoring.entity.ClientCookie;
import com.keymanager.monitoring.entity.Cookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientCookieService extends ServiceImpl<ClientCookieDao, ClientCookie> {
	private static Logger logger = LoggerFactory.getLogger(ClientCookieService.class);

	@Autowired
	private ClientCookieDao clientCookieDao;

	public ClientCookie findClientCookieByClientID(String clientID) {
		return clientCookieDao.findClientCookieByClientID(clientID);
	}
}
