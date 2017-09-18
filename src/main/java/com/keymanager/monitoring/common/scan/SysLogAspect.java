package com.keymanager.monitoring.common.scan;

import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.entity.SysLog;
import com.keymanager.monitoring.service.ISysLogService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.util.Date;
import java.util.Enumeration;

/**
 * @description：AOP 日志
 * @author：zhixuan.wang
 * @date：2015/10/28 18:04
 */
@Aspect
@Component
@Order
public class SysLogAspect {
    private static final Logger LOGGER = LoggerFactory.getLogger(SysLogAspect.class);

    @Autowired
    private ISysLogService sysLogService;

    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void cutController() {}

    @Around("cutController()")
    public Object recordSysLog(ProceedingJoinPoint point) throws Throwable {
        String strMethodName = point.getSignature().getName();
        String strClassName = point.getTarget().getClass().getName();
        Object[] params = point.getArgs();
        StringBuffer bfParams = new StringBuffer();
        Enumeration<String> paraNames = null;
        HttpServletRequest request = null;
        if (params != null && params.length > 0) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            paraNames = request.getParameterNames();
            String key;
            String value;
            while (paraNames.hasMoreElements()) {
                key = paraNames.nextElement();
                value = request.getParameter(key);
                bfParams.append(key).append("=").append(value).append("&");
            }
            if (StringUtils.isBlank(bfParams)) {
                bfParams.append(request.getQueryString());
            }
        }

        String strMessage = String
                .format("[类名]:%s,[方法]:%s,[参数]:%s", strClassName, strMethodName, bfParams.toString());
        LOGGER.info(strMessage);
        if (isWriteLog(strMethodName)) {
            try {
                Subject currentUser = SecurityUtils.getSubject();
                PrincipalCollection collection = currentUser.getPrincipals();
                if (null != collection) {
                    String loginName = collection.getPrimaryPrincipal().toString();
                    SysLog sysLog = new SysLog();
                    sysLog.setLoginName(loginName);
                    sysLog.setRoleName(loginName);
                    sysLog.setOptContent(strMessage);
                    InetAddress addr = InetAddress.getLocalHost();
                    sysLog.setClientIp(addr.getHostAddress().toString());
                    LOGGER.info(sysLog.toString());
                    sysLogService.insert(sysLog);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }

        return point.proceed();
    }

    private boolean isWriteLog(String method) {
        String[] pattern = {"login", "logout", "add", "edit", "delete", "grant"};
        for (String s : pattern) {
            if (method.indexOf(s) > -1) {
                return true;
            }
        }
        return false;
    }
}
