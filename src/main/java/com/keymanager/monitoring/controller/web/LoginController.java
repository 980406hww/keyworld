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
public class LoginController extends BaseController {
	@Autowired
	private DreamCaptcha dreamCaptcha;

	@Autowired
	private IResourceService resourceService;
	/**
	 * 首页
	 *
	 * @return
	 */
	@GetMapping("/")
	public String index() {
		return "redirect:/index";
	}

	/**
	 * 首页
	 *
	 * @param model
	 * @return
	 */
	@GetMapping("/index")
	public String index(Model model) {
		return "/views/index";
	}

	/**
	 * GET 登录
	 * @return {String}
	 */
	@GetMapping("login")
	@CsrfToken(create = true)
	public String login() {
		logger.info("GET请求登录");
		if (SecurityUtils.getSubject().isAuthenticated()) {
			return "/views/index";
		}
		return "/views/login";
}

	/**
	 * POST 登录 shiro 写法
	 *
	 * @param username 用户名
	 * @param password 密码
	 * @return {Object}
	 */
	@PostMapping("/login")
	@CsrfToken(remove = true)
	@ResponseBody
	public Object loginPost(HttpServletRequest request, HttpServletResponse response,
							String username, String password, String captcha,String entryType,
							@RequestParam(value = "rememberMe", defaultValue = "0") Integer rememberMe) {
		logger.info("POST请求登录");
		// 改为全部抛出异常，避免ajax csrf token被刷新
		if (StringUtils.isBlank(username)) {
			return 	renderError("用户名不能为空");
		}
		username = username.trim();
		if (StringUtils.isBlank(password)) {
			return 	renderError("密码不能为空");
		}
		if (StringUtils.isBlank(captcha)) {
		   return 	renderError("验证码不能为空");
		}
		if (!dreamCaptcha.validate(request, response, captcha)) {
			return 	renderError("验证码错误");
		}
		Subject user = SecurityUtils.getSubject();
		List<Tree> menus = resourceService.selectAuthorizationMenu(username);
//		List<Tree> menus = resourceService.selectAllMenu();
		request.getSession().setAttribute("entryType",entryType);
		String terminalType = TerminalTypeMapping.getTerminalType(request);
		request.getSession().setAttribute("terminalType", terminalType);
		request.getSession().setAttribute("username",username);
		request.getSession().setAttribute("password",password);
		request.getSession().setAttribute("menus",menus);
		ExtendedUsernamePasswordToken token = new ExtendedUsernamePasswordToken(username, password);
		token.setEntryType(entryType);
		token.setTerminalType(terminalType);
		// 设置记住密码
		token.setRememberMe(1 == rememberMe);
		try {
			user.login(token);
			return renderSuccess();
		} catch (UnknownAccountException e) {
			throw new RuntimeException("账号不存在！", e);
		} catch (DisabledAccountException e) {
			throw new RuntimeException("账号未启用！", e);
		} catch (IncorrectCredentialsException e) {
			throw new RuntimeException("密码错误！", e);
		} catch (Throwable e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	/**
	 * 未授权
	 * @return {String}
	 */
	@GetMapping("/unauth")
	public String unauth() {
		if (SecurityUtils.getSubject().isAuthenticated() == false) {
			return "redirect:/views/login";
		}
		return "unauth";
	}

	/**
	 * 退出
	 * @return {Result}
	 */
	@PostMapping("/logout")
	@ResponseBody
	public Object logout(HttpServletRequest request) {
		logger.info("登出");
		Subject subject = SecurityUtils.getSubject();
		request.getSession().removeAttribute("username");
		request.getSession().removeAttribute("password");
		request.getSession().removeAttribute("menus");
		subject.logout();
		return renderSuccess();
	}
}
