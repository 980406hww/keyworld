package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.IndustryDetailCriteria;
import com.keymanager.monitoring.service.IndustryInfoService;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/external/industry")
public class ExternalIndustryRestController extends SpringMVCBaseController{

    private static final Logger logger = LoggerFactory.getLogger(ExternalIndustryRestController.class);

    @Autowired
    private IndustryInfoService industryInfoService;

    @PostMapping("/getValidIndustryInfo")
    public ResponseEntity<?> getValidIndustryInfo(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("username");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                Map map = industryInfoService.getValidIndustryInfo();
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    /**
     * @Author: rwxian
     * @Date 2019/10/14 10:08
     * @param requestMap
     * @return
     */
    @PostMapping("/getValidIndustryInfoByName")
    public ResponseEntity<?> getValidIndustryInfoByName(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("username");
            String password = (String) requestMap.get("password");
            String customer = (String) requestMap.get("customer_name");
            String industry = (String) requestMap.get("industry");
            List<String> industryList = Arrays.asList(industry.split(","));
//            System.out.println(customer + "---" + industryList);
            if (validUser(userName, password)) {
                Map map = industryInfoService.getValidIndustryInfo(customer, industryList);
                return new ResponseEntity<>(map, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updateIndustryInfoDetail")
    public ResponseEntity<?> updateIndustryInfoDetail(@RequestBody IndustryDetailCriteria industryDetailCriteria) {
        try {
            if (validUser(industryDetailCriteria.getUserName(), industryDetailCriteria.getPassword())) {
                industryInfoService.updateIndustryInfoDetail(industryDetailCriteria);
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/updateIndustryInfoStatus")
    public ResponseEntity<?> updateIndustryInfoStatus(@RequestBody Map<String, Object> requestMap) {
        try {
            String userName = (String) requestMap.get("username");
            String password = (String) requestMap.get("password");
            long uuid = Long.valueOf((String) requestMap.get("uuid"));
            if (validUser(userName, password)) {
                industryInfoService.updateIndustryInfoStatus(uuid);
                return new ResponseEntity<>(true, HttpStatus.OK);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
    }
}
