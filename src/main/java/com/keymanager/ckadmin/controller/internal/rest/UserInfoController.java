package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/user")
public class UserInfoController {

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

    @GetMapping(value = "/checkEmail2/{loginName}")
    public ModelAndView checkEmail(@PathVariable("loginName") String loginName) {
        ModelAndView modelAndView = new ModelAndView("/newForget");
        modelAndView.addObject("loginName", loginName);
        return modelAndView;
    }
}
