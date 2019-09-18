package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.criteria.ExternalQZSettingCriteria;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external/qzsetting")
public class ExternalQZSettingController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalQZSettingController.class);

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @RequestMapping(value = "/getQZSettingForAutoOperate", method = RequestMethod.POST)
    public ResponseEntity<?> getQZSettingForAutoOperate(
        @RequestBody Map<String, String> requestMap) {
        try {
            String userName = requestMap.get("userName");
            String password = requestMap.get("password");
            if (validUser(userName, password)) {
                Map<String, Object>  responseMap = qzSettingService.getQZSettingForAutoOperate();
                return new ResponseEntity<Object>(responseMap, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateQZSettingKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> updateQZSettingKeywords(@RequestBody ExternalQZSettingCriteria qzSettingCriteria) {
        try {
            if (validUser(qzSettingCriteria.getUserName(), qzSettingCriteria.getPassword())) {
                qzSettingService.updateQZSettingKeywords(qzSettingCriteria);
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
