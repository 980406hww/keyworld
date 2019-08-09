package com.keymanager.monitoring.common.listener;

import com.keymanager.monitoring.service.KeywordManagerSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.*;

public class KeywordManagerHttpSessionListener implements HttpSessionActivationListener, HttpSessionAttributeListener,HttpSessionListener, ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(KeywordManagerHttpSessionListener.class);

    ServletContext context = null;

    @Override
    public void sessionCreated(HttpSessionEvent sessionEvent) {
        logger.info("====创建session中====");
        KeywordManagerSessionService keywordManagerSessionService = new KeywordManagerSessionService();
        keywordManagerSessionService.saveKeywordManagerSessions(sessionEvent.getSession());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent sessionEvent) {
        logger.info("====销毁session中====");
        HttpSession session = sessionEvent.getSession();
        String sessionId = session.getId();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //该方法实现了ServletContextListener接口定义的方法，对ServletContext进行初始化
        this.context = servletContextEvent.getServletContext();//初始化ServletContext对象
        System.out.println("ServletContext初始化......");//打印出该方法的操作信息
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        //监听Servlet上下文被释放
        this.context = null; //释放ServletContext对象
        System.out.println("ServletContext被释放......");//打印出该方法的操作信息
    }

    @Override
    public void sessionWillPassivate(HttpSessionEvent httpSessionEvent) {
        //监听Http会话的passivate情况
        System.out.println("sessionWillPassivate("+httpSessionEvent.getSession().getId()+")");
    }

    @Override
    public void sessionDidActivate(HttpSessionEvent httpSessionEvent) {
        //监听Http会话的active情况
        System.out.println("sessionDidActivate("+httpSessionEvent.getSession().getId()+")");
    }

    @Override
    public void attributeAdded(HttpSessionBindingEvent httpSessionBindingEvent) {
        //监听Http会话中的属性添加
        System.out.println("attributeAdded('"+httpSessionBindingEvent.getSession().getId()+"', '"+httpSessionBindingEvent.getName()+"', '"+httpSessionBindingEvent.getValue()+"')");
    }

    @Override
    public void attributeRemoved(HttpSessionBindingEvent httpSessionBindingEvent) {
        //监听Http会话中的属性删除
        System.out.println("attributeRemoved('"+httpSessionBindingEvent.getSession().getId()+"', '"+httpSessionBindingEvent.getName()+"', '"+httpSessionBindingEvent.getValue()+"')");
    }

    @Override
    public void attributeReplaced(HttpSessionBindingEvent httpSessionBindingEvent) {
        //监听Http会话中的属性更改操作
        System.out.println("attributeReplaced('"+httpSessionBindingEvent.getSession().getId()+"', '"+httpSessionBindingEvent.getName()+"', '"+httpSessionBindingEvent.getValue()+"')");
    }
}
