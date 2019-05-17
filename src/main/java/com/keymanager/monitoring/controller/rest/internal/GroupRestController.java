package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.GroupBatchCriteria;
import com.keymanager.monitoring.criteria.GroupCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
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

    @RequiresPermissions("/internal/group/saveGroup")
    @PostMapping("/saveGroup")
    public ResponseEntity<?> saveGroup(@RequestBody GroupCriteria groupCriteria, HttpServletRequest request) {
        try {
            groupCriteria.setCreateBy((String) request.getSession().getAttribute("username"));
            groupService.saveGroup(groupCriteria);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/group/updateGroup")
    @PostMapping("/updateGroup/{groupUuid}")
    public ResponseEntity<?> updateGroup(@PathVariable("groupUuid") long groupUuid, @RequestBody UpdateGroupSettingCriteria updateGroupSettingCriteria) {
        try {
            groupService.updateGroupSettings(groupUuid, updateGroupSettingCriteria);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/group/delGroup")
    @PostMapping("/delGroup/{uuid}")
    public ResponseEntity<?> deleteGroup(@PathVariable("uuid") long uuid) {
        try {
            groupService.deleteGroup(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/group/batchAddGroups")
    @PostMapping("/batchAddGroups")
    public ResponseEntity<?> batchAddGroups(@RequestBody GroupBatchCriteria groupBatchCriteria, HttpServletRequest request) {
        try {
            String userName = (String) request.getSession().getAttribute("username");
            groupService.batchAddGroups(groupBatchCriteria, userName);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequiresPermissions("/internal/group/getAvailableOptimizationGroups")
    @PostMapping("/getAvailableOptimizationGroups/{terminalType}")
    public ResponseEntity<?> getAvailableOptimizationGroups(@PathVariable("terminalType") String terminalType) {
        try {
            List<String> availableOptimizationGroups = groupService.getAvailableOptimizationGroups(terminalType);
            return new ResponseEntity<Object>(availableOptimizationGroups, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
        }
    }
}
