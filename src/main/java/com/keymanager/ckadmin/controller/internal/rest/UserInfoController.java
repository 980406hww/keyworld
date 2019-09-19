package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.service.UserInfoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}
