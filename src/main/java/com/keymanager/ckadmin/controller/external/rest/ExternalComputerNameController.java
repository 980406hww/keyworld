package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.service.ComputerNameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/external/computerName")
public class ExternalComputerNameController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalComputerNameController.class);

    @Resource(name = "computerNameService")
    private ComputerNameService computerNameService;

    @RequestMapping(value = "/getSequence", method = RequestMethod.GET)
    public ResponseEntity<?> getSequence(HttpServletRequest request) throws Exception {
        String namePrefix = request.getParameter("namePrefix");
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        try {
            if (validUser(userName, password)) {
                return new ResponseEntity<Object>(computerNameService.getSequence(namePrefix), HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error("ExternalComputerNameController.getSequence()" + e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
