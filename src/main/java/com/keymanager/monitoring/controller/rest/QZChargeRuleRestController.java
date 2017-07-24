package com.keymanager.monitoring.controller.rest;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.QZChargeRule;
import com.keymanager.monitoring.service.QZChargeRuleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by shunshikj01 on 2017/7/10.
 */
@RestController
@RequestMapping("/spring/chargerule")
public class QZChargeRuleRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(QZChargeRuleRestController.class);

    @Autowired
    private QZChargeRuleService  qzChargeRuleService;

     /*@RequestMapping(value = "/save", method = RequestMethod.POST)
   public ResponseEntity<?> saveQZChargeRule(QZChargeRule qzChargeRule){

        System.out.print("====sdf==");
//        qzChargeRuleService.saveQZChargeRule(qzChargeRule);

        return new ResponseEntity<Object>(request, HttpStatus.OK);QZChargeRule qzChargeRule
    }*/
     @RequestMapping(value = "/save", method = RequestMethod.POST)
     public String saveQZChargeRule(HttpServletRequest request){
         System.out.print(request.getParameter("amount"));
         return null;
     }



}
