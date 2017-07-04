/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package com.keymanager.monitoring.controller.web;

import java.awt.Color;
import java.io.IOException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.keymanager.monitoring.common.shiro.ShiroDbRealm;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.patchca.color.ColorFactory;
import org.patchca.filter.predefined.CurvesRippleFilterFactory;
import org.patchca.filter.predefined.DiffuseRippleFilterFactory;
import org.patchca.filter.predefined.DoubleRippleFilterFactory;
import org.patchca.filter.predefined.MarbleRippleFilterFactory;
import org.patchca.filter.predefined.WobbleRippleFilterFactory;
import org.patchca.service.ConfigurableCaptchaService;
import org.patchca.utils.encoder.EncoderHelper;
import org.patchca.word.RandomWordFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 * LoginController负责打开登录页面(GET请求)和登录出错页面(POST请求)，
 * 
 * 真正登录的POST请求由Filter完成,
 * 
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController extends SpringMVCBaseController {

	private static ConfigurableCaptchaService cs = new ConfigurableCaptchaService();
	private static Random random = new Random();
	static {
		cs.setColorFactory(new ColorFactory() {
			@Override
			public Color getColor(int x) {
				int[] c = new int[3];
				int i = random.nextInt(c.length);
				for (int fi = 0; fi < c.length; fi++) {
					if (fi == i) {
						c[fi] = random.nextInt(71);
					} else {
						c[fi] = random.nextInt(256);
					}
				}
				return new Color(c[0], c[1], c[2]);
			}
		});
		RandomWordFactory wf = new RandomWordFactory();
		wf.setCharacters("23456789abcdefghigkmnpqrstuvwxyzABCDEFGHIGKLMNPQRSTUVWXYZ");
		wf.setMaxLength(4);
		wf.setMinLength(4);
		cs.setWordFactory(wf);
	}

	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView login() {
		ShiroDbRealm.ShiroUser currentUser = getCurrentUser();
		ModelAndView mav;
		if (currentUser == null) {
			mav = new ModelAndView("login.jsp");
		} else {
			mav = new ModelAndView("redirect:maintains/home.jsp");
		}
		return mav;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView fail(@RequestParam(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM) String username) {
		ShiroDbRealm.ShiroUser currentUser = getCurrentUser();
		if (currentUser != null) {
			return new ModelAndView("redirect:maintains/home.jsp");
		}
		ModelAndView mav = new ModelAndView("login.jsp");
		ModelMap modelMap = new ModelMap();
		modelMap.addAttribute(FormAuthenticationFilter.DEFAULT_USERNAME_PARAM, username);
		Object error = getHttpRequest().getAttribute(FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (error != null) {
			if(error.toString().equals("CaptchaException")){
				modelMap.put("loginInfo", "验证码错误");
			}else{
				modelMap.put("loginInfo", "用户名或者密码错误");
			}
		}
		mav.addAllObjects(modelMap);
		return mav;
	}

	@RequestMapping(value="/pcrimg", method = RequestMethod.GET)
	public void pcrimg(HttpServletRequest request, HttpServletResponse response) throws IOException {
		switch (random.nextInt(5)) {
		case 0:
			cs.setFilterFactory(new CurvesRippleFilterFactory(cs.getColorFactory()));
			break;
		case 1:
			cs.setFilterFactory(new MarbleRippleFilterFactory());
			break;
		case 2:
			cs.setFilterFactory(new DoubleRippleFilterFactory());
			break;
		case 3:
			cs.setFilterFactory(new WobbleRippleFilterFactory());
			break;
		case 4:
			cs.setFilterFactory(new DiffuseRippleFilterFactory());
			break;
		}
		HttpSession session = request.getSession(false);
		if (session == null) {
			session = request.getSession();
		}
		setResponseHeaders(response);
		String token = EncoderHelper.getChallangeAndWriteImage(cs, "png", response.getOutputStream());
		session.setAttribute("captcha", token);
	}

	protected void setResponseHeaders(HttpServletResponse response) {
		response.setContentType("image/png");
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
	}

}
