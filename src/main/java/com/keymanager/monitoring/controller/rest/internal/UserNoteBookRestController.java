package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.entity.UserNoteBook;
import com.keymanager.monitoring.service.UserNoteBookService;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Author zhoukai
 * @Date 2019/4/16 13:59
 **/
@RestController
@RequestMapping(value = "/internal/usernotebook")
public class UserNoteBookRestController {

    private static Logger logger = LoggerFactory.getLogger(UserNoteBookRestController.class);

    @Autowired
    private UserNoteBookService userNoteBookService;

    @RequiresPermissions("/internal/usernotebook/searchUserNoteBooks")
    @RequestMapping(value = "/searchUserNoteBooks", method = RequestMethod.POST)
    public ResponseEntity<?> getUserNoteBooks(@RequestBody Map<String, Object> resultMap, HttpServletRequest request) {
        try {
            Long customerUuid = Long.parseLong((String) resultMap.get("customerUuid"));
            Integer searchAll = (Integer) resultMap.get("searchAll");
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            List<UserNoteBook> userNoteBooks = userNoteBookService.findUserNoteBooksByCustomerUuid(customerUuid, terminalType, searchAll);
            return new ResponseEntity<Object>(userNoteBooks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequiresPermissions("/internal/usernotebook/searchUserNoteBooks")
    @RequestMapping(value = "/saveUserNoteBook", method = RequestMethod.POST)
    public ResponseEntity<?> saveUserNoteBook(@RequestBody Map<String, Object> resultMap, HttpServletRequest request) {
        try {
            UserNoteBook userNoteBook = new UserNoteBook();
            userNoteBook.setCustomerUuid(Long.parseLong((String) resultMap.get("customerUuid")));
            userNoteBook.setContent((String) resultMap.get("content"));
            userNoteBook.setNotesPerson((String) request.getSession().getAttribute("username"));
            userNoteBook.setTerminalType(TerminalTypeMapping.getTerminalType(request));
            int affectedRows = userNoteBookService.saveUserNoteBook(userNoteBook);
            if (affectedRows > 0) {
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
