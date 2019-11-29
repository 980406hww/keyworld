package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.base.ExternalBaseCriteria;
import com.keymanager.ckadmin.service.UserInfoService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    /**
     * 外部链接 登录获取用户信息
     *
     * @param criteria 账号密码
     * @return 用户信息
     */
    @RequestMapping(value = "/getCustomerSource2", method = RequestMethod.POST)
    public ResultBean getCustomerSource(@RequestBody ExternalBaseCriteria criteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(criteria.getUserName(), criteria.getPassword())) {
                resultBean.setData(userInfoService.getUserInfo(criteria.getUserName()).getUserName());
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalUserLoginController.getCustomerSource()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
