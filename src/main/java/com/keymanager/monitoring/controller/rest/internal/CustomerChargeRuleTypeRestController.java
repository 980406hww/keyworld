package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.CustomerChargeType;
import com.keymanager.monitoring.service.CustomerChargeRuleTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by shunshikj20 on 2017/8/21.
 */
@RestController
@RequestMapping(value = "/internal/customerChargeRuleType")
public class CustomerChargeRuleTypeRestController extends SpringMVCBaseController {

    @Autowired
    private CustomerChargeRuleTypeService customerChargeRuleTypeService;

    @RequestMapping(value = "/saveAndUpdateCustomerChargeRule" , method = RequestMethod.POST)
    public ResponseEntity<?> saveCustomerChargeRule(@RequestBody CustomerChargeType customerChargeType){
       try{
           customerChargeRuleTypeService.saveAndUpdateCustomerChargeRule(customerChargeType);
           return new ResponseEntity<Object>(true, HttpStatus.OK);
       }catch (Exception e){
           e.printStackTrace();
           return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
       }
    }

    @RequestMapping(value = "/getCustomerChargeRule/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerChargeRule(@PathVariable("uuid") Long uuid){
            return new ResponseEntity<Object>(customerChargeRuleTypeService.getCustomerChargeRule(uuid), HttpStatus.OK);
        }
}
