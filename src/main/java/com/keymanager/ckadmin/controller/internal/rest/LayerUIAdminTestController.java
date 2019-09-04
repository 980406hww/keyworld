package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.Menu;
import com.keymanager.ckadmin.service.ResourceService;

import java.util.List;
import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName ExternalLayerUIAdminTestRestController
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/30 9:34
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/internal/layer")
public class LayerUIAdminTestController {

    @Resource(name = "resourceService2")
    private ResourceService resourceService;

    @RequestMapping("/index")
    public ModelAndView toIndex() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("index");
        return mv;
    }

    @RequestMapping("/home")
    public ModelAndView toConsole() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("home/home");
        return mv;
    }

    @RequestMapping("/menu")
    public ResponseEntity selectMenus() {
        List<Menu> menus = resourceService.selectAuthorizationResource("duchengfu", null);
        return new ResponseEntity(menus, HttpStatus.OK);
    }
}
