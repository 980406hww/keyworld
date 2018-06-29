package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.CustomerChargeLog;
import com.keymanager.monitoring.service.CustomerChargeLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/customerChargeLog")
public class CustomerChargeLogRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(CustomerChargeLogRestController.class);

    @Autowired
    private CustomerChargeLogService customerChargeLogService;

    @RequiresPermissions("/internal/customerChargeLog/addCustomerChargeLog")
    @RequestMapping(value = "/addCustomerChargeLog" , method = RequestMethod.POST)
    public ResponseEntity<?> addCustomerChargeLog(@RequestBody Map<String, Object> requestMap){
        try {
            List<String> customerUuids = (List<String>) requestMap.get("customerUuids");
            Integer planChargeAmount = (Integer) requestMap.get("planChargeAmount");
            Integer actualChargeAmount = (Integer) requestMap.get("actualChargeAmount");
            String cashier = (String) requestMap.get("cashier");
            String nextChargeDate = (String) requestMap.get("nextChargeDate");
            customerChargeLogService.addCustomerChargeLog(customerUuids, planChargeAmount, actualChargeAmount, cashier, nextChargeDate);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/customerChargeLog/findCustomerChargeLogs")
    @RequestMapping(value = "/findCustomerChargeLogs/{customerUuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> findCustomerChargeLogs(@PathVariable("customerUuid")Integer customerUuid){
        try {
            List<CustomerChargeLog> customerChargeLogs = customerChargeLogService.findCustomerChargeLogs(customerUuid);
            return new ResponseEntity<Object>(customerChargeLogs , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }
}
