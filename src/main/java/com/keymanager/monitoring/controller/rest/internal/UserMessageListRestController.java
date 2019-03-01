package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @Author zhoukai
 * @Date 2019/2/28 17:05
 **/
@Controller
@RequestMapping(value = "/usermessage")
public class UserMessageListRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(UserMessageListRestController.class);

    @RequestMapping(value = "/getUserMessageList", method = RequestMethod.POST)
    public ResponseEntity<?> getUserMessageList() {
        try {

            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
