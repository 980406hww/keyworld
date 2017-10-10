package com.keymanager.monitoring.controller.web;

import com.keymanager.monitoring.common.base.BaseController;
import com.keymanager.monitoring.common.csrf.CsrfToken;
import com.keymanager.monitoring.common.result.Tree;
import com.keymanager.monitoring.common.shiro.captcha.DreamCaptcha;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.service.IResourceService;
import com.keymanager.monitoring.vo.ExtendedUsernamePasswordToken;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @description：登录退出
 */
@Controller
public class ForgetPasswordController extends BaseController {
	@Autowired
	private DreamCaptcha dreamCaptcha;


	/**
	 * POST 登录 shiro 写法
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @return {Object}
	 */
	@PostMapping("/forgetPassword")
	@CsrfToken(remove = true)
	@ResponseBody
	public Object loginPost(HttpServletRequest request, HttpServletResponse response, String captcha) {
		logger.info("POST请求登录");
		// 改为全部抛出异常，避免ajax csrf token被刷新
		if (StringUtils.isBlank(captcha)) {
		   return 	renderError("验证码不能为空");
		}
		if (!dreamCaptcha.validate(request, response, captcha)) {
			return 	renderError("验证码错误");
		}else {
			return null;
		}
	}

}
