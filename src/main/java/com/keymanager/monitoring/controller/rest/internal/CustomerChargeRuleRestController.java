package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CustomerChargeRuleCriteria;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerChargeLog;
import com.keymanager.monitoring.entity.CustomerChargeRule;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.service.CustomerChargeLogService;
import com.keymanager.monitoring.service.CustomerChargeRuleService;
import com.keymanager.monitoring.service.CustomerService;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.vo.CustomerChargeStatVO;
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
import java.util.Set;

@RestController
@RequestMapping(value = "/internal/customerChargeRule")
public class CustomerChargeRuleRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(CustomerChargeRuleRestController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private CustomerChargeLogService customerChargeLogService;

    @Autowired
    private CustomerChargeRuleService customerChargeRuleService;

    @RequiresPermissions("/internal/customerChargeRule/searchCustomerChargeRules")
    @RequestMapping(value = "/searchCustomerChargeRules", method = RequestMethod.GET)
    public ModelAndView searchCustomerChargeRules(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructCustomerChargeRuleModelAndView(request, new CustomerChargeRuleCriteria(), currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/customerChargeRule/searchCustomerChargeRules")
    @RequestMapping(value = "/searchCustomerChargeRules", method = RequestMethod.POST)
    public ModelAndView searchCustomerChargeRulesPost(HttpServletRequest request, CustomerChargeRuleCriteria customerChargeRuleCriteria) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "50";
            }
            return constructCustomerChargeRuleModelAndView(request, customerChargeRuleCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/customerChargeRule/customerChargeRule");
        }
    }

    private ModelAndView constructCustomerChargeRuleModelAndView(HttpServletRequest request, CustomerChargeRuleCriteria customerChargeRuleCriteria, int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/customerChargeRule/customerChargeRule");
        CustomerCriteria customerCriteria = new CustomerCriteria();
        String entryType = (String) request.getSession().getAttribute("entryType");
        customerCriteria.setEntryType(entryType);
        Set<String> roles = getCurrentUser().getRoles();
        boolean isDepartmentManager = true;
        if(!roles.contains("DepartmentManager")) {
            isDepartmentManager = false;
            String loginName = (String) request.getSession().getAttribute("username");
            customerCriteria.setLoginName(loginName);
            customerChargeRuleCriteria.setLoginName(loginName);
        }
        if(request.getMethod().equals("GET")) {
            customerChargeRuleService.getChargeRemindCustomer(customerChargeRuleCriteria);
            customerChargeRuleCriteria.setOrderBy("fNextChargeDate");
        }
        List<Customer> customerList = customerService.getActiveCustomerSimpleInfo(customerCriteria);
        List<UserInfo> activeUsers = userInfoService.findActiveUsers();

        Page<CustomerChargeRule> page = customerChargeRuleService.searchCustomerChargeRules(new Page<CustomerChargeRule>(currentPageNumber, pageSize), customerChargeRuleCriteria);
        modelAndView.addObject("customerList", customerList);
        modelAndView.addObject("isDepartmentManager", isDepartmentManager);
        modelAndView.addObject("activeUsers", activeUsers);
        modelAndView.addObject("customerChargeRuleCriteria", customerChargeRuleCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/customerChargeRule/saveCustomerChargeRule")
    @RequestMapping(value = "/saveCustomerChargeRule", method = RequestMethod.POST)
    public ResponseEntity<?> saveCustomerChargeRule(@RequestBody CustomerChargeRule customerChargeRule) {
        try {
            customerChargeRuleService.saveCustomerChargeRule(customerChargeRule);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/customerChargeRule/deleteCustomerChargeRule")
    @RequestMapping(value = "/deleteCustomerChargeRule/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> deleteCustomerChargeRule(@PathVariable("uuid") Long uuid) {
        try {
            customerChargeRuleService.deleteCustomerChargeRule(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/customerChargeRule/deleteCustomerChargeRules")
    @RequestMapping(value = "/deleteCustomerChargeRules" , method = RequestMethod.POST)
    public ResponseEntity<?> deleteCustomerChargeRules(@RequestBody Map<String, Object> requestMap){
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            customerChargeRuleService.deleteCustomerChargeRules(uuids);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/customerChargeStat" , method = RequestMethod.POST)
    public ResponseEntity<?> customerChargeStat(@RequestBody Map<String, Object> requestMap){
        try {
            String beginDate = (String) requestMap.get("beginDate");
            String endDate = (String) requestMap.get("endDate");
            String month = (String) requestMap.get("month");
            String isDepartmentManager = (String) requestMap.get("isDepartmentManager");
            String loginName = getCurrentUser().getLoginName();
            if(isDepartmentManager.equals("true")) {
                loginName = null;
            }
            Integer chargeTotal = customerChargeRuleService.addUpCustomerChargeAmount(loginName, month);
            chargeTotal = chargeTotal == null ? 0 : chargeTotal;
            CustomerChargeStatVO customerChargeStatVO = customerChargeLogService.addUpCustomerChargeLogs(loginName, beginDate, endDate);
            if(customerChargeStatVO == null) {
                customerChargeStatVO = new CustomerChargeStatVO();
            }
            customerChargeStatVO.setChargeTotal(chargeTotal);
            return new ResponseEntity<Object>(customerChargeStatVO , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(null , HttpStatus.BAD_REQUEST);
        }
    }
}
