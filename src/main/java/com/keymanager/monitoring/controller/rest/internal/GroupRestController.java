package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.*;
import com.keymanager.monitoring.service.GroupService;
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
 * @Date 2019/4/29 18:16
 **/
@RestController
@RequestMapping(value = "/internal/group")
public class GroupRestController {

    private static Logger logger = LoggerFactory.getLogger(GroupSettingRestController.class);

    @Autowired
    private GroupService groupService;

    @RequiresPermissions("/internal/group/batchAddGroups")
    @PostMapping("/batchAddGroups")
    public ResponseEntity<?> batchAddGroups(@RequestBody OperationCombineCriteria operationCombineCriteria, HttpServletRequest request) {
        try {
            operationCombineCriteria.setCreator((String) request.getSession().getAttribute("username"));
            groupService.batchAddGroups(operationCombineCriteria);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequiresPermissions("/internal/group/getAvailableOptimizationGroups")
    @PostMapping("/getAvailableOptimizationGroups")
    public ResponseEntity<?> getAvailableOptimizationGroups(@RequestBody GroupSettingCriteria groupSettingCriteria) {
        try {
            List<String> availableOptimizationGroups = groupService.getAvailableOptimizationGroups(groupSettingCriteria);
            return new ResponseEntity<Object>(availableOptimizationGroups, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/saveGroupsBelowOperationCombine")
    public ResponseEntity<?> saveGroupsBelowOperationCombine(@RequestBody OperationCombineCriteria operationCombineCriteria, HttpServletRequest request) {
        try {
            String userName = (String) request.getSession().getAttribute("username");
            operationCombineCriteria.setCreator(userName);
            groupService.saveGroupsBelowOperationCombine(operationCombineCriteria);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/updateGroupsBelowOperationCombine")
    public ResponseEntity<?> updateGroupsBelowOperationCombine(@RequestBody Map<String, Object> requestMap) {
        try {
            List<Long> groupUuids = (List<Long>) requestMap.get("groupUuids");
            groupService.updateGroupsBelowOperationCombine(groupUuids, null);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
