package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.common.email.ForgetPasswordMailService;
import com.keymanager.monitoring.common.shiro.PasswordHash;
import com.keymanager.monitoring.common.sms.MD5;
import com.keymanager.monitoring.dao.UserInfoDao;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.util.PropertiesUtil;
import com.keymanager.util.Utils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * Created by shunshikj08 on 2017/9/30.
 */
@RestController
@RequestMapping(value = "/internal/user")
public class UserInfoRestController {

    @Autowired
    private UserInfoDao userInfoDao;

    @Autowired
    private PasswordHash passwordHash;

    @Autowired
    private ForgetPasswordMailService forgetPasswordMailService;

    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            String loginName = (String) requestMap.get("loginName");
            String password = (String) requestMap.get("password");
            UserInfo user = userInfoDao.getUserInfo(loginName);
            String salt = user.getSalt();
            UserInfo userInfo = new UserInfo();
            userInfo.setUuid(user.getUuid());
            userInfo.setPassword(passwordHash.toHex(password, salt));
            userInfoDao.updateById(userInfo);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @RequestMapping(value = "/checkEmail/{loginName}", method = RequestMethod.GET)
    public ModelAndView checkEmail(@PathVariable("loginName")String loginName) {
        ModelAndView modelAndView = new ModelAndView("/checkEmail");
        modelAndView.addObject("loginName",loginName);
        return modelAndView;
    }

    @RequestMapping(value = "/userValidate/{loginName}/{validateCode}", method = RequestMethod.GET)
    public ModelAndView userValidate(@PathVariable("loginName") String loginName,@PathVariable("validateCode") String validateCode) {
        try {
            UserInfo userInfo = userInfoDao.getUserInfo(loginName);
            String md5 = MD5.getMd5String(loginName);
            if(!md5.equals(validateCode) || (Utils.getCurrentTimestamp().getTime() - userInfo.getResetPasswordApplicationTime().getTime() > 5 * 60 * 1000)) {
                return new ModelAndView("/views/login");
            }
            ModelAndView modelAndView = new ModelAndView("/resetPasswordPage");
            modelAndView.addObject("loginName", loginName);
            return modelAndView;
        } catch (Exception ex) {
            return new ModelAndView("/views/login");
        }
    }

    @RequestMapping(value = "/forgetPassword", method = RequestMethod.POST)
    public ResponseEntity<?> forgetPassword(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            String loginName = (String) requestMap.get("loginName");
            String email = (String) requestMap.get("email");
            Date date = Utils.getCurrentTimestamp();
            UserInfo userInfo = userInfoDao.getUserInfo(loginName);

            if(null != userInfo) {
                if(null != userInfo.getEmail() && userInfo.getEmail().equals(email)) {
                    String mailContent = "<a href = '"+ PropertiesUtil.pcWebPath +"/internal/user/userValidate/"
                            + loginName + "/" + MD5.getMd5String(loginName) + "'>" + loginName+"</a>";
                    forgetPasswordMailService.sendForgetPasswordMail(loginName, email, mailContent);
                    userInfo.setResetPasswordApplicationTime(date);
                    userInfoDao.updateById(userInfo);
                    return new ResponseEntity<Object>(true, HttpStatus.OK);
                }
            }
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
