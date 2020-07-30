package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.entity.UserOnline;
import com.keymanager.monitoring.service.SessionService;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping(value = "/internal/useronline")
public class SessionController {

    private static Logger logger = LoggerFactory.getLogger(SessionController.class);

    @Autowired
    private SessionService sessionService;

    @RequiresRoles("admin")
    @RequestMapping("/list")
    public ModelAndView list() {
        return constructUserOnlineModelAndView();
    }

    private ModelAndView constructUserOnlineModelAndView() {
        ModelAndView modelAndView = new ModelAndView("/views/online");
        List<UserOnline> list = sessionService.list();
        modelAndView.addObject("list", list);
        return modelAndView;
    }

    @RequiresRoles("admin")
    @RequestMapping(value = "/forceLogout/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> forceLogout(@PathVariable("id") String id) {
        try {
            sessionService.forceLogout(id);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
