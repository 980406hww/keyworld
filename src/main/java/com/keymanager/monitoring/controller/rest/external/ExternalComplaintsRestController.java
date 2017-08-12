package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.TSMainKeywordCriteria;
import com.keymanager.monitoring.service.TSComplainLogService;
import com.keymanager.monitoring.entity.TSMainKeyword;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.TSMainKeywordService;
import com.keymanager.monitoring.service.TSNegativeKeywordService;
import com.keymanager.monitoring.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by shunshikj08 on 2017/8/1.
 */
@RestController
@RequestMapping(value = "/external/complaints")
public class ExternalComplaintsRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalComplaintsRestController.class);

    @Autowired
    private TSMainKeywordService tsMainKeywordService;

    @Autowired
    private TSNegativeKeywordService tsNegativeKeywordService;

    @Autowired
    private TSComplainLogService tsComplainLogService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/saveWithAuthentication", method = RequestMethod.POST)
    public ResponseEntity<?> saveTSMainKeywords(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria){
        if(tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null) {
            User user = userService.getUser(tsMainKeywordCriteria.getUserName());
            if (user != null && user.getPassword().equals(tsMainKeywordCriteria.getPassword())) {
                tsMainKeywordService.saveTSMainKeyword(tsMainKeywordCriteria.getTsMainKeyword());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }
    //提供爬虫使用
    @RequestMapping(value = "/getTSMainKeywordsForComplaints", method = RequestMethod.POST)
    public ResponseEntity<?> getTSMainKeywordsForComplaints(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria) throws Exception{
        if(tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null){
            User user = userService.getUser(tsMainKeywordCriteria.getUserName());
            if(user != null && user.getPassword().equals(tsMainKeywordCriteria.getPassword())){
                String ipCity = tsMainKeywordCriteria.getIpCity();
                TSMainKeyword tsMainKeyword = tsMainKeywordService.getTsMainKeywordsForComplaints(ipCity);
                return new ResponseEntity<Object>(tsMainKeyword, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateTSMainKeywordsForComplaints", method = RequestMethod.POST)
    public ResponseEntity<?> updateTsMainKeywordsForComplaints(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria){
        if(tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null){
            User user = userService.getUser(tsMainKeywordCriteria.getUserName());
            if(user != null && user.getPassword().equals(tsMainKeywordCriteria.getPassword())){
                // 更新Negative
                tsNegativeKeywordService.updateNegativeKeywords(tsMainKeywordCriteria.getTsMainKeyword().getTsNegativeKeywords());
                // 添加Log
                tsComplainLogService.addComplainLogByNegativeKeywords(tsMainKeywordCriteria.getTsMainKeyword().getTsNegativeKeywords());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }

}
