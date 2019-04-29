package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.GroupCriteria;
import com.keymanager.monitoring.service.GroupService;
import com.keymanager.monitoring.vo.GroupSettingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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

    @PostMapping("/findGroup/{groupUuid}")
    public ResponseEntity<?> findGroup(@PathVariable("groupUuid") long groupUuid) {
        try {
            GroupSettingVO groupSettingVo = groupService.findGroupSettings(groupUuid);
            return new ResponseEntity<Object>(groupSettingVo, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

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
}
