package com.keymanager.monitoring.controller.rest.external;


import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.ExternalWebsiteUpdateStatusCriteria;
import com.keymanager.monitoring.service.WebsiteService;
import com.keymanager.monitoring.vo.ExternalWebsiteCheckResultVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/external/checkWebsite")
public class ExternalCheckWebsiteStatusController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalCheckWebsiteStatusController.class);

    @Autowired
    private WebsiteService websiteService;

    @RequestMapping(value = "/getAllWebsite", method = RequestMethod.POST)
    public ResponseEntity<?> getAllWebsite(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                return new ResponseEntity<Object>( websiteService.getAllWebsiteForexternalCheckStauts(), HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateWebsiteStatus", method = RequestMethod.POST)
    public ResponseEntity<?> updateWebsiteStatus(@RequestBody ExternalWebsiteUpdateStatusCriteria externalWebsiteUpdateStatusCriteria) {
        try {
            String userName = externalWebsiteUpdateStatusCriteria.getUserName();
            String password = externalWebsiteUpdateStatusCriteria.getPassword();
            if (validUser(userName, password)) {
                List<ExternalWebsiteCheckResultVO> websiteCheckResultVOS = externalWebsiteUpdateStatusCriteria.getWebsiteCheckResultVOS();
                websiteService.updateWebsiteStatus(websiteCheckResultVOS);
                return new ResponseEntity<Object>( true,HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
