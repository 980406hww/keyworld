package com.keymanager.monitoring.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.NamedThreadLocal;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class SpringMVCInterceptor extends HandlerInterceptorAdapter implements HandlerInterceptor {

	private static final Logger log = LoggerFactory.getLogger(SpringMVCInterceptor.class);
	
	private NamedThreadLocal<Map> requestInfoThreadLocal = new NamedThreadLocal<Map>("requestInfo");  

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		long beginTime = System.currentTimeMillis();
		String url = "";
		url = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()+request.getServletPath();
		if (request.getQueryString() != null) {
			url += "?" + request.getQueryString();
		}
		log.info("Request URL: " + url + "  Request Method: " + request.getMethod());
		Map<String, String[]> requestMap = request.getParameterMap();
		if(!requestMap.isEmpty()){
			StringBuilder requestData = new StringBuilder("{");
			for(Entry<String, String[]> entry : requestMap.entrySet()){
				requestData.append("'"+entry.getKey()+"': "+Arrays.deepToString(entry.getValue())+"  ");
			}
			requestData.append("}");
			log.info("Request Data: " + requestData.toString());
		}
		Map<String,Object> requestInfo = new HashMap<String, Object>();
		requestInfo.put("url", url);
		requestInfo.put("beginTime", beginTime);
		requestInfoThreadLocal.set(requestInfo);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		Map<String,Object> requestInfo = requestInfoThreadLocal.get();
		log.info("Request URL: " + requestInfo.get("url")+ "  Request Method: " + request.getMethod() + "  Response Time: " + (System.currentTimeMillis()-(Long)requestInfo.get("beginTime")+"ms"));
	}

}
