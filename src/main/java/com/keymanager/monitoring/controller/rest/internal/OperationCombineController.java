package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.service.OperationCombineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author zhoukai
 * @Date 2019/6/24 15:52
 **/
@RestController
@RequestMapping(value = "/internal/operationCombine")
public class OperationCombineController {

    private static Logger logger = LoggerFactory.getLogger(GroupSettingRestController.class);

    @Autowired
    private OperationCombineService operationCombineService;

    @RequestMapping("/getGroupNames/{uuid}")
    public ResponseEntity<?> getGroupNames (@PathVariable Long uuid) {
        try {
            String groupNames = operationCombineService.getGroupNames(uuid);
            return new ResponseEntity<Object>(groupNames, HttpStatus.OK);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }
}
