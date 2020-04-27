package com.keymanager.ckadmin.common.shiro;

import com.keymanager.monitoring.common.utils.StringUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class NewShiroAjaxSessionFilter extends FormAuthenticationFilter {

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		if (isLoginRequest(request, response)) {
			if (isLoginSubmission(request, response)) {
				return executeLogin(request, response);
			} else {
				return true;
			}
		} else {
			HttpServletRequest req = WebUtils.toHttp(request);
			String xmlHttpRequest = req.getHeader("X-Requested-With");
			if (StringUtils.isNotBlank(xmlHttpRequest)) {
				HttpServletResponse res = WebUtils.toHttp(response);
				res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
			}else{
				redirectToLogin(request, response);
			}
			return false;
		}
	}

}