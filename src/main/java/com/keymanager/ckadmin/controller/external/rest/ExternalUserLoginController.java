package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.UserInfoService;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shunshikj40
 */
@RestController
@RequestMapping("/external/userlogin")
public class ExternalUserLoginController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(ExternalUserLoginController.class);

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @PostMapping("/getLoginUserInfo")
    public ResultBean getLoginUserInfo(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200, "登录成功");
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                UserInfo userInfo = userInfoService.getUserInfo(userName);
                resultBean.setData(userInfo.getUserName());
                return resultBean;
            }
            resultBean.setCode(400);
            resultBean.setMsg("账号或密码错误");
            return resultBean;
        } catch (Exception ex) {
            logger.error("getLoginUserInfo:     " + ex.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(ex.getMessage());
            return resultBean;
        }
    }
}
