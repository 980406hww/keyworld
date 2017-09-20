package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.CustomerChargeType;
import com.keymanager.monitoring.service.CustomerChargeTypeService;
import com.sun.media.jfxmedia.logging.Logger;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by shunshikj20 on 2017/8/21.
 */
@RestController
@RequestMapping(value = "/internal/customerChargeType")
public class CustomerChargeTypeRestController extends SpringMVCBaseController {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(CustomerKeywordRestController.class);

    @Autowired
    private CustomerChargeTypeService customerChargeTypeService;

    @RequiresPermissions("/internal/customerChargeType/saveCustomerChargeType")
    @RequestMapping(value = "/saveCustomerChargeType" , method = RequestMethod.POST)
    public ResponseEntity<?> saveCustomerChargeType(@RequestBody CustomerChargeType customerChargeType){
       try{
           customerChargeTypeService.saveCustomerChargeType(customerChargeType);
           return new ResponseEntity<Object>(true, HttpStatus.OK);
       }catch (Exception e){
           logger.error(e.getMessage());
           return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
       }
    }

    @RequestMapping(value = "/getCustomerChargeType/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> getCustomerChargeType(@PathVariable("uuid") Long uuid) {
        return new ResponseEntity<Object>(customerChargeTypeService.getCustomerChargeType(uuid), HttpStatus.OK);
    }
}
