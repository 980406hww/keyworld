package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.Menu;
import com.keymanager.ckadmin.service.ResourceService;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName LayerUIAdminTestRestController
 * @Description TODO
 * @Author lhc
 * @Date 2019/8/30 9:34
 * @Version 1.0
 */
@RestController
@RequestMapping(value = "/internal/layer")
public class LayerUIAdminTestController {

    private static Logger logger = LoggerFactory.getLogger(LayerUIAdminTestController.class);

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
        mv.setViewName("keywords/keyword");
        return mv;
    }

    @RequestMapping("/menu")
    public ResponseEntity<?> selectMenus(HttpServletRequest request) {
        try {
            String loginName = (String) request.getSession().getAttribute("username");
            List<Menu> menus = resourceService.selectAuthorizationResource(loginName, null);
            return new ResponseEntity<Object>(menus, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }
}
