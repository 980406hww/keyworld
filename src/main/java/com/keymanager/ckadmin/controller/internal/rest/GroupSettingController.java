package com.keymanager.ckadmin.controller.internal.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName GroupSettingController
 * @Description 分组信息设置
 * @Author lhc
 * @Date 2019/10/10 14:20
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/internal/groupsetting")
public class GroupSettingController {

    @GetMapping("/toGroupSettings")
    public ModelAndView toGroupSettings(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("groupSettings/groupSetting");
        return mv;
    }

}
