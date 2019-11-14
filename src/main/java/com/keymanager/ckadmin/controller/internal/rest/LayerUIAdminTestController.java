package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.Menu;
import com.keymanager.ckadmin.service.ResourceService;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(required = false) String url, @RequestParam(required = false) String tit, HttpSession session) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/index");
        mv.addObject("url", url);
        if (null == url || "".equals(url)) {
            mv.addObject("first", "1-1");
            return mv;
        }
        List menus = (List) session.getAttribute("menus");
        if (null == menus || menus.isEmpty()) {
            menus = resourceService.selectAuthorizationResource((String) session.getAttribute("username"), null);
        }
        int i = 0, j = 0;
        String key = null;
            outFor:
        for (Object obj : menus) {
            Menu menu = (Menu) obj;
            i++;
            for (Menu m : menu.getChildren()) {
                j++;
                if (url.equals(m.getHref())) {
                    key = m.getTitle();
                    break outFor;
                }
            }
            j = 0;
        }
        mv.addObject("key", key);
        mv.addObject("tit", tit);
        mv.addObject("id", i + "-" + j);
        return mv;
    }

    @RequestMapping("/home")
    public ModelAndView toConsole() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("home/home");
        return mv;
    }

    @RequestMapping("/menu")
    public ResponseEntity<?> selectMenus(HttpServletRequest request) {
        try {
            String loginName = (String) request.getSession().getAttribute("username");
            List menus = (List) request.getSession().getAttribute("menus");
            if (null == menus || menus.isEmpty()) {
                menus = resourceService.selectAuthorizationResource(loginName, null);
                request.getSession().setAttribute("menus", menus);
            }
            return new ResponseEntity<Object>(menus, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }
}
