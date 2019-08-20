package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.GroupCriteria;
import com.keymanager.monitoring.service.GroupService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName ExternalGroupRestController
 * @Description 外部添加关键字分组
 * @Author lhc
 * @Date 2019/8/20 11:39
 * @Version 1.0
 */
@RestController
@RequestMapping("/external/group")
public class ExternalGroupRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalGroupRestController.class);


    @Autowired
    private GroupService groupService;


    @RequestMapping(value = "/saveExternalGroup" , method = RequestMethod.POST)
    public ResponseEntity<?> saveExternalGroup(@RequestBody GroupCriteria groupCriteria) {
        try {
            if (validUser(groupCriteria.getUserName(), groupCriteria.getPassword())) {
                groupService.saveExternalGroup(groupCriteria);
                return new ResponseEntity<Object>(groupCriteria.getUuid(), HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
