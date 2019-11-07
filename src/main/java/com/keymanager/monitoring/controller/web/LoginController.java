package com.keymanager.monitoring.controller.web;

import com.keymanager.monitoring.common.base.BaseController;
import com.keymanager.monitoring.common.csrf.CsrfToken;
import com.keymanager.monitoring.common.result.Tree;
import com.keymanager.monitoring.common.shiro.captcha.DreamCaptcha;
import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.service.IResourceService;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.vo.ExtendedUsernamePasswordToken;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
import java.util.Enumeration;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.session.Session;
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
import org.springframework.web.servlet.ModelAndView;

/**
 * @description：登录退出
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private DreamCaptcha dreamCaptcha;

    @Autowired
    private IResourceService resourceService;

    @Autowired
    private IUserInfoService userInfoService;

    /**
     * 首页
     */
    @GetMapping("/")
    public String index() {
        return "redirect:/index";
    }

    /**
     * 首页
     */
    @GetMapping("/index")
    public String index(Model model) {
        return "/views/index";
    }

    /**
     * GET 登录
     *
     * @return {String}
     */
    @GetMapping("login")
    @CsrfToken(create = true)
    public String login(HttpSession session) {
        logger.info("GET请求登录");
        if (SecurityUtils.getSubject().isAuthenticated()) {
            if (Constants.OLD_PERMISSION_VERSION.equals(session.getAttribute("version"))) {
                return "/customerkeyword/keywordfinderList";
            } else {
                return "/index";
            }
        }
        return "/newLogin";
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
    public Object loginPost(HttpServletRequest request, HttpServletResponse response, String username, String password, String captcha, String entryType,
        @RequestParam(value = "rememberMe", defaultValue = "0") Integer rememberMe, String version) {
        logger.info("POST请求登录");
        // 改为全部抛出异常，避免ajax csrf token被刷新
        if (StringUtils.isBlank(username)) {
            return renderError("用户名不能为空");
        }
        username = username.trim();
        if (StringUtils.isBlank(password)) {
            return renderError("密码不能为空");
        }
        if (StringUtils.isBlank(captcha)) {
            return renderError("验证码不能为空");
        }
        if (!dreamCaptcha.validate(request, response, captcha)) {
            return renderError("验证码错误");
        }
        Subject user = SecurityUtils.getSubject();
        List<Tree> menus = null;
        if (Constants.OLD_PERMISSION_VERSION.equals(version)) {
            menus = resourceService.selectAuthorizationMenu(username, version);
        }
        ExtendedUsernamePasswordToken token = new ExtendedUsernamePasswordToken(username, password);
        token.setEntryType(entryType);
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        token.setTerminalType(terminalType);
        token.setVersion(version);
        // 设置记住密码
        token.setRememberMe(1 == rememberMe);
        try {
            user.login(token);
            Session session = user.getSession();
            String organizationName = userInfoService.getOrganizationNameByLoginName(username);
            session.setAttribute("entryType", entryType);
            session.setAttribute("terminalType", terminalType);
            session.setAttribute("username", username);
            session.setAttribute("name", token.getName());
            session.setAttribute("password", password);
            session.setAttribute("organizationName", organizationName);
            session.setAttribute("version", version);
            session.setAttribute("menus", menus);
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
     *
     * @return {String}
     */
    @GetMapping("/unauth")
    public String unauth() {
        if (!SecurityUtils.getSubject().isAuthenticated()) {
            return "redirect:/views/login";
        }
        return "unauth";
    }

    /**
     * 退出
     *
     * @return {Result}
     */
    @PostMapping("/logout")
    @ResponseBody
    public Object logout(HttpSession session) {
        logger.info("登出");
        Subject subject = SecurityUtils.getSubject();
        session.invalidate();
        subject.logout();
        return renderSuccess();
    }

    /**
     * 旧版本登录
     * @return
     */
    @GetMapping("/loginOldVersion")
    @ResponseBody
    public ModelAndView loginOldVersion(HttpSession session) {
        Enumeration<String> e = session.getAttributeNames();
        if (e.hasMoreElements()){
            Subject subject = SecurityUtils.getSubject();
            session.invalidate();
            subject.logout();
        }
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/views/login");
        return mv;
    }
}
