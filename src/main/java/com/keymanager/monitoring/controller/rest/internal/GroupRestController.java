package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.GroupBatchCriteria;
import com.keymanager.monitoring.criteria.GroupCriteria;
import com.keymanager.monitoring.criteria.GroupSettingCriteria;
import com.keymanager.monitoring.criteria.UpdateGroupSettingCriteria;
import com.keymanager.monitoring.entity.GroupSetting;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.GroupService;
import com.keymanager.util.Constants;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Autowired
    private ConfigService configService;

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
            HttpSession session = request.getSession();
            String entryType = (String) session.getAttribute("entryType");
            String maxInvalidCount = configService.getConfig(Constants.CONFIG_TYPE_MAX_INVALID_COUNT, entryType).getValue();
            groupService.batchAddGroups(groupBatchCriteria, userName, Integer.valueOf(maxInvalidCount));
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

    @RequestMapping("/updateMaxInvalidCount")
    public ResponseEntity<?> updateMaxInvalidCount(@RequestBody Map<String, Object> requestMap) {
        try {
            long uuid = Long.valueOf((String) requestMap.get("id"));
            int maxInvalidCount = Integer.valueOf((String) requestMap.get("maxInvalidCount"));
            groupService.updateMaxInvalidCount(uuid, maxInvalidCount);
            return new ResponseEntity<>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
