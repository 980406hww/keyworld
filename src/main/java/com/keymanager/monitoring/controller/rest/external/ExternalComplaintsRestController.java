package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.TSMainKeywordCriteria;
import com.keymanager.monitoring.entity.TSMainKeyword;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.TSComplainLogService;
import com.keymanager.monitoring.service.TSMainKeywordService;
import com.keymanager.monitoring.service.TSNegativeKeywordService;
import com.keymanager.monitoring.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

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
    public ResponseEntity<?> saveTSMainKeywords(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria) {
        if (tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null) {
            Subject user = SecurityUtils.getSubject();
            if (user.isAuthenticated()) {
                tsMainKeywordService.saveTSMainKeyword(tsMainKeywordCriteria.getTsMainKeyword());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            } else {
                UsernamePasswordToken token = new UsernamePasswordToken(tsMainKeywordCriteria.getUserName(), tsMainKeywordCriteria.getPassword());
                try {
                    user.login(token);
                    tsMainKeywordService.saveTSMainKeyword(tsMainKeywordCriteria.getTsMainKeyword());
                    return new ResponseEntity<Object>(true, HttpStatus.OK);
                } catch (UnknownAccountException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (DisabledAccountException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (IncorrectCredentialsException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (Throwable e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }

    //提供爬虫使用
    @RequestMapping(value = "/getTSMainKeywordsForComplaints", method = RequestMethod.POST)
    public ResponseEntity<?> getTSMainKeywordsForComplaints(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria) throws Exception {
        if (tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null) {
            Subject user = SecurityUtils.getSubject();
            if (user.isAuthenticated()) {
                String ipCity = tsMainKeywordCriteria.getIpCity();
                TSMainKeyword tsMainKeyword = tsMainKeywordService.getTsMainKeywordsForComplaints(ipCity);
                return new ResponseEntity<Object>(tsMainKeyword, HttpStatus.OK);
            } else {
                UsernamePasswordToken token = new UsernamePasswordToken(tsMainKeywordCriteria.getUserName(), tsMainKeywordCriteria.getPassword());
                try {
                    user.login(token);
                    String ipCity = tsMainKeywordCriteria.getIpCity();
                    TSMainKeyword tsMainKeyword = tsMainKeywordService.getTsMainKeywordsForComplaints(ipCity);
                    return new ResponseEntity<Object>(tsMainKeyword, HttpStatus.OK);
                } catch (UnknownAccountException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (DisabledAccountException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (IncorrectCredentialsException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (Throwable e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getNegativeKeywords", method = RequestMethod.POST)
    public ResponseEntity<?> getNegativeKeywords(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria) throws Exception {
        if (tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null) {
            Subject user = SecurityUtils.getSubject();
            if (user.isAuthenticated()) {
                String ipCity = tsMainKeywordCriteria.getIpCity();
                Set<String> negativeKeywords = tsMainKeywordService.getNegativeKeywords(tsMainKeywordCriteria.getKeyword());
                return new ResponseEntity<Object>(negativeKeywords, HttpStatus.OK);
            } else {
                UsernamePasswordToken token = new UsernamePasswordToken(tsMainKeywordCriteria.getUserName(), tsMainKeywordCriteria.getPassword());
                try {
                    user.login(token);
                    Set<String> negativeKeywords = tsMainKeywordService.getNegativeKeywords(tsMainKeywordCriteria.getKeyword());
                    return new ResponseEntity<Object>(negativeKeywords, HttpStatus.OK);
                } catch (UnknownAccountException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (DisabledAccountException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (IncorrectCredentialsException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (Throwable e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateTSMainKeywordsForComplaints", method = RequestMethod.POST)
    public ResponseEntity<?> updateTsMainKeywordsForComplaints(@RequestBody TSMainKeywordCriteria tsMainKeywordCriteria) {
        if (tsMainKeywordCriteria.getUserName() != null && tsMainKeywordCriteria.getPassword() != null) {
            Subject user = SecurityUtils.getSubject();
            if (user.isAuthenticated()) {
                // 更新Negative
                tsNegativeKeywordService.updateNegativeKeywords(tsMainKeywordCriteria.getTsMainKeyword().getTsNegativeKeywords());
                // 添加Log
                tsComplainLogService.addComplainLogByNegativeKeywords(tsMainKeywordCriteria.getTsMainKeyword().getTsNegativeKeywords());
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            } else {
                UsernamePasswordToken token = new UsernamePasswordToken(tsMainKeywordCriteria.getUserName(), tsMainKeywordCriteria.getPassword());
                try {
                    // 更新Negative
                    tsNegativeKeywordService.updateNegativeKeywords(tsMainKeywordCriteria.getTsMainKeyword().getTsNegativeKeywords());
                    // 添加Log
                    tsComplainLogService.addComplainLogByNegativeKeywords(tsMainKeywordCriteria.getTsMainKeyword().getTsNegativeKeywords());
                    return new ResponseEntity<Object>(true, HttpStatus.OK);
                } catch (UnknownAccountException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (DisabledAccountException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (IncorrectCredentialsException e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                } catch (Throwable e) {
                    return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
                }
            }
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }

}
