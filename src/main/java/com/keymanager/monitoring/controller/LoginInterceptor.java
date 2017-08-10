package com.keymanager.monitoring.controller;

import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by shunshikj01 on 2017/8/9.
 */
public class LoginInterceptor implements HandlerInterceptor {

  @Autowired
  private UserService userService;

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
      ModelAndView modelAndView) throws Exception {

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {

  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    //获取请求的URL
    String url = request.getRequestURI();
    if(url.indexOf("/bd.html") >= 0 || url.indexOf("/bd.jsp?rukou") >= 0 ||url.equals("/")){
      return true;
    }else {
      HttpSession session = request.getSession();
      String username = (String) session.getAttribute("username");
      String password = (String) session.getAttribute("password");
      if (username != null) {
        User user = userService.getUser(username);
        if (user != null) {
          if(user.getPassword().equals(password)){
            return true;
          }
        }
      }else {
        //不符合条件的，跳转到登录界面
        response.sendRedirect(request.getContextPath() +"/bd.html");
        return false;
      }
      return false;
    }
  }
}
