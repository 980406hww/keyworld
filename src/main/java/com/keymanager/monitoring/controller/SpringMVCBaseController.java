package com.keymanager.monitoring.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.keymanager.monitoring.common.shiro.ShiroDbRealm.ShiroUser;

public abstract class SpringMVCBaseController {

	@Autowired
	private HttpServletRequest httpRequest;
	

	public Double getDouble(String key) {
		String value = httpRequest.getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return Double.parseDouble(value);
		}
		return null;
	}

	public Integer getInteger(String key) {
		String value = httpRequest.getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return Integer.parseInt(value);
		}
		return null;
	}

	public Long getLong(String key) {
		String value = httpRequest.getParameter(key);
		if (StringUtils.isNotBlank(value)) {
			return Long.parseLong(value);
		}
		return null;
	}

	public String getString(String key) {
		return httpRequest.getParameter(key);
	}

	public HttpServletRequest getHttpRequest() {
		return httpRequest;
	}

	public void setHttpRequest(HttpServletRequest httpRequest) {
		this.httpRequest = httpRequest;
	}
	
	public ShiroUser getCurrentUser(){
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		return user;
	}

}
