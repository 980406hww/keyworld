package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.UserMessageListCriteria;
import com.keymanager.monitoring.service.UserMessageListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author zhoukai
 * @Date 2019/2/28 17:05
 **/
@RestController
@RequestMapping(value = "/usermessage")
public class UserMessageListRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(UserMessageListRestController.class);

    @Autowired
    private UserMessageListService userMessageListService;

    @RequestMapping(value = "/getUserMessageList", method = RequestMethod.POST)
    public ResponseEntity<?> getUserMessageList(@RequestBody UserMessageListCriteria userMessageListCriteria, HttpServletRequest request) {
        try {

            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getUserMessage/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> getUserMessage(@PathVariable Integer uuid) {
        try {

            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getAllUsers", method = RequestMethod.POST)
    public ResponseEntity<?> getAllUsers() {
        try {

            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/saveUserMessages", method = RequestMethod.POST)
    public ResponseEntity<?> saveUserMessages(@RequestBody UserMessageListCriteria userMessageListCriteria, HttpServletRequest request) {
        try {

            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateMessageStatus/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> updateMessageStatus(@PathVariable Integer uuid) {
        try {

            return new ResponseEntity<Object>(HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
