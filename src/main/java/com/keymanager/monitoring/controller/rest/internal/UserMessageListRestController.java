package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.UserMessageListCriteria;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.entity.UserMessageList;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.service.UserMessageListService;
import com.keymanager.monitoring.vo.UserMessageListVO;
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
public class UserMessageListRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(UserMessageListRestController.class);

    @Autowired
    private UserMessageListService userMessageListService;

    @Autowired
    private IUserInfoService userInfoService;

    @RequestMapping(value = "/getUserMessageList", method = RequestMethod.POST)
    public ResponseEntity<?> getUserMessageList(@RequestBody UserMessageListCriteria userMessageListCriteria, HttpServletRequest request) {
        try {
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")){
                userMessageListCriteria.setUserName((String) request.getSession().getAttribute("username"));
            }
            UserMessageListVO userMessageListVo = userMessageListService.getUserMessageList(userMessageListCriteria);
            List<UserInfo> userInfoList = userInfoService.findActiveUsers();
            userMessageListVo.setUserInfoList(userInfoList);
            return new ResponseEntity<Object>(userMessageListVo, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getUserMessage/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> getUserMessage(@PathVariable Integer uuid) {
        try {
            UserMessageList userMessageList = userMessageListService.getUserMessageByUuid(uuid);
            return new ResponseEntity<Object>(userMessageList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/saveUserMessages", method = RequestMethod.POST)
    public ResponseEntity<?> saveUserMessages(@RequestBody UserMessageListCriteria userMessageListCriteria, HttpServletRequest request) {
        try {
            String userName = (String) request.getSession().getAttribute("username");
            userMessageListService.saveUserMessages(userMessageListCriteria, userName);
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
            Integer messageInboxCount = userMessageListService.checkMessageInboxCount(userName);
            return new ResponseEntity<Object>(messageInboxCount, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
    }
}