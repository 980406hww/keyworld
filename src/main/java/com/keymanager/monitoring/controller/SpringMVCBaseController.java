package com.keymanager.monitoring.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keymanager.monitoring.common.shiro.ShiroUser;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.vo.ExtendedUsernamePasswordToken;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.ZipCompressor;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class SpringMVCBaseController {

	private static Logger logger = LoggerFactory.getLogger(SpringMVCBaseController.class);

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

	public void downFile(HttpServletResponse response, String fileName) {
		OutputStream outputStream = null;
		FileInputStream fileInputStream = null;
		try {
			response.setContentType("application/octet-stream");
			String path = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath();
			String zipFilePath = path + fileName;
			ZipCompressor.zipMultiFile(path + fileName.substring(0,fileName.indexOf(".")), zipFilePath);
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			outputStream = response.getOutputStream();
			fileInputStream = new FileInputStream(zipFilePath);

			byte[] b = new byte[1024];
			int i = 0;
			while ((i = fileInputStream.read(b)) > 0) {
				outputStream.write(b, 0, i);
			}
			outputStream.flush();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
					fileInputStream = null;
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		}
	}

	public void downExcelFile(HttpServletResponse response, String fileName, byte[] buffer) {
		try {
			response.reset();
			response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
			response.addHeader("Content-Length", "" + buffer.length);
			OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public boolean validUser(String userName, String password) throws Exception{
		Subject user = SecurityUtils.getSubject();
		if (user.isAuthenticated()) {
			return true;
		} else {
			ExtendedUsernamePasswordToken token = new ExtendedUsernamePasswordToken(userName, password);
			try {
				user.login(token);
				return true;
			} catch (Exception ex) {
				throw ex;
			}
		}
	}

}
