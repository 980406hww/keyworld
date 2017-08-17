package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.CustomerService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.PortTerminalTypeMapping;
import com.keymanager.value.CustomerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by shunshikj01 on 2017/8/17.
 */
@RestController
@RequestMapping(value = "/internal/customer")
public class CustomerRestController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "searchCustomers" ,method = RequestMethod.GET)
    public ModelAndView searchCustomers(@RequestParam(defaultValue = "1") int currentPage,@RequestParam(defaultValue="35") int displaysRecords,HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("/customer/customerlist");
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("username");
        User user = userService.getUser(userID);
        Page<Customer> page = customerService.searchCustomers(new Page<Customer>(currentPage,displaysRecords),new CustomerCriteria());
        String entryType = (String) session.getAttribute("entry");
        modelAndView.addObject("entryType",entryType);
        modelAndView.addObject("page",page);
        modelAndView.addObject("user",user);
        return modelAndView;
    }


    @RequestMapping(value = "addCustomer" , method = RequestMethod.GET)
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer){
        return new ResponseEntity<Object>(customerService.insert(customer), HttpStatus.OK);
    }


    @RequestMapping(value = "getCustomer/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getCustomer(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(customerService.getCustomerWithKeywordCount(uuid), HttpStatus.OK);
    }

    @RequestMapping(value = "saveCustomer/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> saveCustomer(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(customerService.getCustomerWithKeywordCount(uuid), HttpStatus.OK);
    }
}
