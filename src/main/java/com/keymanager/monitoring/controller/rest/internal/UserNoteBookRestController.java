package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.entity.UserNoteBook;
import com.keymanager.monitoring.service.UserNoteBookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @RequestMapping(value = "/searchUserNoteBooks/{customerUuid}")
    public ResponseEntity<?> getUserNoteBooks(@PathVariable("customerUuid") long customerUuid) {
        try {
            List<UserNoteBook> userNoteBooks = userNoteBookService.findUserNoteBooksByCustomerUuid(customerUuid);
            return new ResponseEntity<Object>(userNoteBooks, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }


}
