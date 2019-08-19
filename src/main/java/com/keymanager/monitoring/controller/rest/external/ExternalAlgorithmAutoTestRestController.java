package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.ExternalAlgorithmTestTaskCriteria;
import com.keymanager.monitoring.entity.AlgorithmTestPlan;
import com.keymanager.monitoring.service.AlgorithmTestPlanService;
import com.keymanager.monitoring.service.AlgorithmTestTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName ExternalAlgorithmAutoTestRestController
 * @Description 算法自动测试外部接口
 * @Author lhc
 * @Date 2019/8/17 10:05
 * @Version 1.0
 */
@RestController
@RequestMapping("/external/algorithmAutoTest")
public class ExternalAlgorithmAutoTestRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalAlgorithmAutoTestRestController.class);

    @Autowired
    private AlgorithmTestPlanService algorithmTestPlanService;

    @Autowired
    private AlgorithmTestTaskService algorithmTestTaskService;

    @RequestMapping(value = "/getAlgorithmTestPlan", method = RequestMethod.POST)
    public ResponseEntity<?> getAlgorithmTestPlan(HttpServletRequest request){
        try {
            String userName = request.getParameter("userName");
            String password = request.getParameter("password");
            if (validUser(userName, password)) {
                AlgorithmTestPlan algorithmTestPlan = algorithmTestPlanService.selectOneAvailableAlgorithmTestPlan();
                return new ResponseEntity<Object>(algorithmTestPlan, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping("/saveAlgorithmTestTasks")
    public ResponseEntity<?> saveAlgorithmTestTasks(@RequestBody ExternalAlgorithmTestTaskCriteria externalAlgorithmTestTaskCriteria){
        try {
            String userName = externalAlgorithmTestTaskCriteria.getUserName();
            String password = externalAlgorithmTestTaskCriteria.getPassword();
            if (validUser(userName, password)) {
                algorithmTestTaskService.saveAlgorithmTestTasks(externalAlgorithmTestTaskCriteria.getAlgorithmTestTasks());
                return new ResponseEntity<Object>(HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
