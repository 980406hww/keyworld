package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.UserInfo;
import com.keymanager.monitoring.service.CustomerService;
import com.keymanager.monitoring.service.IUserInfoService;
import com.keymanager.monitoring.service.UserRoleService;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/customer")
public class CustomerRestController {
    private static Logger logger = LoggerFactory.getLogger(CustomerRestController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private IUserInfoService userInfoService;

    @Autowired
    private UserRoleService userRoleService;

    @RequiresPermissions("/internal/customer/searchCustomers")
    @RequestMapping(value = "/searchCustomers", method = RequestMethod.GET)
    public ModelAndView searchCustomers(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "50") int displaysRecords,
                                        HttpServletRequest request) {
        return constructCustomerModelAndView(request, new CustomerCriteria(), currentPage + "", displaysRecords + "");
    }

    @RequiresPermissions("/internal/customer/searchCustomers")
    @RequestMapping(value = "/searchCustomers", method = RequestMethod.POST)
    public ModelAndView searchCustomersPost(HttpServletRequest request, CustomerCriteria customerCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");//
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == currentPageNumber) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructCustomerModelAndView(request, customerCriteria, currentPageNumber, pageSize);
    }

    private ModelAndView constructCustomerModelAndView(HttpServletRequest request, CustomerCriteria customerCriteria, String currentPage, String pageSize) {
        ModelAndView modelAndView = new ModelAndView("/customer/customerlist");
        Subject subject = SecurityUtils.getSubject();
        HttpSession session = request.getSession();
        String loginName = (String) session.getAttribute("username");
        UserInfo user = userInfoService.getUserInfo(loginName);
        List<UserInfo> activeUsers = userInfoService.findActiveUsers();
        String entryType = (String) session.getAttribute("entryType");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        customerCriteria.setEntryType(entryType);
        customerCriteria.setTerminalType(terminalType);
        boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(loginName));
        if(!isDepartmentManager) {
            customerCriteria.setLoginName(loginName);
        }
        Page<Customer> page = customerService.searchCustomers(new Page<Customer>(Integer.parseInt(currentPage), Integer.parseInt(pageSize)), customerCriteria);
        modelAndView.addObject("entryType", entryType);
        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("customerCriteria", customerCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("user", user);
        modelAndView.addObject("isDepartmentManager", isDepartmentManager);
        modelAndView.addObject("activeUsers", activeUsers);
        return modelAndView;
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/saveCustomer", method = RequestMethod.POST)
    public ResponseEntity<?> saveCustomer(@RequestBody Customer customer) {
        try {
            customerService.saveCustomer(customer);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/getCustomer/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getCustomer(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(customerService.getCustomerWithKeywordCount(uuid), HttpStatus.OK);
    }

    @RequiresPermissions("/internal/customer/delCustomer")
    @RequestMapping(value = "/delCustomer/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> delCustomer(@PathVariable("uuid") Long uuid) {
        try {
            customerService.deleteCustomer(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.OK);
        }
    }

    @RequiresPermissions("/internal/customer/deleteCustomers")
    @RequestMapping(value = "/deleteCustomers" , method = RequestMethod.POST)
    public ResponseEntity<?> deleteCustomers(@RequestBody Map<String, Object> requestMap){
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            customerService.deleteAll(uuids);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.OK);
        }
    }

    @RequiresPermissions("/internal/customer/uploadDailyReportTemplate")
    @RequestMapping(value = "/uploadDailyReportTemplate" , method = RequestMethod.POST)
    public boolean uploadDailyReportTemplate(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
       String customerUuid = request.getParameter("customerUuid");
       String terminalType = TerminalTypeMapping.getTerminalType(request);
       String path = Utils.getWebRootPath() + "dailyreport" + File.separator + terminalType + File.separator;
       String fileName = customerUuid  + ".xls";
       File targetFile = new File(path, fileName);
       if(!targetFile.exists()){
           targetFile.mkdirs();
       }
       //保存
       try {
           file.transferTo(targetFile);
           return true;
       } catch (Exception e) {
           logger.error(e.getMessage());
       }
       return false;
    }

    @RequestMapping(value = "/searchCustomersWithKeyword", method = RequestMethod.POST)
    public List<Customer> searchCustomersWithKeyword(String contactPerson) {
        return customerService.searchCustomersWithKeyword(contactPerson);
    }
}
