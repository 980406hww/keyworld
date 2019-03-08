package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.UserMessageCriteria;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.entity.UserMessage;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.service.UserMessageService;
import com.keymanager.monitoring.vo.UserMessageVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * @Author zhoukai
 * @Date 2019/2/28 17:05
 **/
@RestController
@RequestMapping(value = "/internal/usermessage")
public class UserMessageRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(UserMessageRestController.class);

    @Autowired
    private UserMessageService userMessageService;

    @Autowired
    private IUserInfoService userInfoService;

    @RequestMapping(value = "/getUserMessageList", method = RequestMethod.POST)
    public ResponseEntity<?> getUserMessageList(@RequestBody UserMessageCriteria userMessageCriteria, HttpServletRequest request) {
        try {
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")){
                userMessageCriteria.setUserName((String) request.getSession().getAttribute("username"));
            }
            UserMessageVO userMessageVo = userMessageService.getUserMessageList(userMessageCriteria);
            List<UserInfo> userInfoList = userInfoService.findActiveUsers();
            userMessageVo.setUserInfoList(userInfoList);
            return new ResponseEntity<Object>(userMessageVo, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getUserMessage/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> getUserMessage(@PathVariable Integer uuid) {
        try {
            UserMessage userMessage = userMessageService.getUserMessageByUuid(uuid);
            return new ResponseEntity<Object>(userMessage, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/saveUserMessages", method = RequestMethod.POST)
    public ResponseEntity<?> saveUserMessages(@RequestBody UserMessageCriteria userMessageCriteria, HttpServletRequest request) {
        try {
            String userName = (String) request.getSession().getAttribute("username");
            userMessageService.saveUserMessages(userMessageCriteria, userName);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/checkMessageInbox", method = RequestMethod.POST)
    public ResponseEntity<?> checkMessageInbox(HttpServletRequest request) {
        try {
            String userName = null;
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")){
                userName = (String) request.getSession().getAttribute("username");
            }
            Integer messageInboxCount = userMessageService.checkMessageInboxCount(userName);
            return new ResponseEntity<Object>(messageInboxCount, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
}