package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.ProjectFollow;
import com.keymanager.monitoring.service.ProjectFollowService;
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

@RestController
@RequestMapping(value = "/internal/projectFollow")
public class ProjectFollowRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ProjectFollowRestController.class);

    @Autowired
    private ProjectFollowService projectFollowService;

    @RequestMapping(value = "/findProjectFollows/{customerUuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> findProjectFollows(@PathVariable("customerUuid")Integer customerUuid){
        try {
            List<ProjectFollow> projectFollows = projectFollowService.findProjectFollows(customerUuid);
            return new ResponseEntity<Object>(projectFollows , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(null , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/deleteProjectFollow/{uuid}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteProjectFollow(@PathVariable("uuid") Long uuid) {
        try {
            projectFollowService.deleteProjectFollow(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/saveProjectFollow", method = RequestMethod.POST)
    public ResponseEntity<?> saveProjectFollow(@RequestBody ProjectFollow projectFollow, HttpServletRequest request) {
        try {
            String name = getCurrentUser().getName();
            projectFollow.setFollowPerson(name);
            projectFollowService.saveProjectFollow(projectFollow);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
