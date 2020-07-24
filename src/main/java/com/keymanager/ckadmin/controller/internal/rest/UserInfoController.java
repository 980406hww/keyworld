package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/user")
public class UserInfoController extends SpringMVCBaseController {

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @GetMapping("/trees")
    public Object tree() {
        return userInfoService.selectUserInfoTrees();
    }

    @GetMapping("/trees2")
    public Object tree2() {
        return userInfoService.selectUserInfoTrees2();
    }

    @GetMapping("/treeByAuthority")
    public Object treeByAuthority() {
        String loginName = getCurrentUser().getLoginName();
        return userInfoService.selectUserInfoTreesByAuthority(loginName);
    }

    @GetMapping(value = "/checkEmail2/{loginName}")
    public ModelAndView checkEmail(@PathVariable("loginName") String loginName) {
        ModelAndView modelAndView = new ModelAndView("/newForget");
        modelAndView.addObject("loginName", loginName);
        return modelAndView;
    }

    @RequestMapping("/getActiveUsersByAuthority")
    public ResultBean getActiveUsersByAuthority() {
        String loginName = getCurrentUser().getLoginName();
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            resultBean.setData(userInfoService.getActiveUsersByAuthority(loginName));
        } catch (Exception e) {
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
