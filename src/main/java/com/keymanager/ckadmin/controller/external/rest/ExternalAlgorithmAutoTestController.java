package com.keymanager.ckadmin.controller.external.rest;


import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.ExternalAlgorithmTestTaskCriteria;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;
import com.keymanager.ckadmin.service.AlgorithmTestPlanService;
import com.keymanager.ckadmin.service.AlgorithmTestTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Description 算法自动测试外部接口
 * @Author lhc
 * @Date 2019/8/17 10:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/external/algorithmAutoTest")
public class ExternalAlgorithmAutoTestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalAlgorithmAutoTestController.class);

    @Resource(name = "algorithmTestPlanService2")
    private AlgorithmTestPlanService algorithmTestPlanService;

    @Resource(name = "algorithmTestTaskService2")
    private AlgorithmTestTaskService algorithmTestTaskService;

    @RequestMapping(value = "/getAlgorithmTestPlan2", method = RequestMethod.POST)
    public ResponseEntity<?> getAlgorithmTestPlan(@RequestBody Map<String, Object> requestMap){
        try {
            String userName = (String) requestMap.get("userName");
            String password = (String) requestMap.get("password");
            if (validUser(userName, password)) {
                AlgorithmTestPlan algorithmTestPlan;
                synchronized(ExternalAlgorithmAutoTestController.class){
                     algorithmTestPlan = algorithmTestPlanService.selectOneAvailableAlgorithmTestPlan();
                }
                return new ResponseEntity<Object>(algorithmTestPlan, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/saveAlgorithmTestTask2")
    public ResponseEntity<?> saveAlgorithmTestTask(@RequestBody ExternalAlgorithmTestTaskCriteria externalAlgorithmTestTaskCriteria){
        try {
            String userName = externalAlgorithmTestTaskCriteria.getUserName();
            String password = externalAlgorithmTestTaskCriteria.getPassword();
            if (validUser(userName, password)) {
                algorithmTestTaskService.saveAlgorithmTestTask(externalAlgorithmTestTaskCriteria);
                return new ResponseEntity<Object>(true,HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
    }
}
