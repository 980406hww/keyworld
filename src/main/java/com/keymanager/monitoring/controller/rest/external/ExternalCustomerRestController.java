package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.CustomerService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/external/customer")
public class ExternalCustomerRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalCustomerRestController.class);


    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "getActiveCustomerSimpleInfo" , method = RequestMethod.POST)
    public ResponseEntity<?> getActiveCustomerSimpleInfo(@RequestBody CustomerCriteria customerCriteria) throws Exception{
        try {
            if (validUser(customerCriteria.getUserName(), customerCriteria.getPassword())) {
                List<Customer> customers = customerService.getActiveCustomerSimpleInfo();
                return new ResponseEntity<Object>(customers, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
}
