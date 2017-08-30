package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.CustomerService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.PortTerminalTypeMapping;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerVO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/customer")
public class CustomerRestController {
    private static Logger logger = LoggerFactory.getLogger(CustomerRestController.class);

    @Autowired
    private CustomerService customerService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/searchCustomers", method = RequestMethod.GET)
    public ModelAndView searchCustomers(@RequestParam(defaultValue = "1") int currentPage, @RequestParam(defaultValue = "30") int displaysRecords, HttpServletRequest request) {
        return constructCustomerModelAndView(request, new CustomerCriteria(), currentPage + "", displaysRecords + "");
    }

    @RequestMapping(value = "/searchCustomers", method = RequestMethod.POST)
    public ModelAndView searchCustomersPost(HttpServletRequest request, CustomerCriteria customerCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");//
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == currentPageNumber) {
            currentPageNumber = "1";
            pageSize = "30";
        }
        return constructCustomerModelAndView(request, customerCriteria, currentPageNumber, pageSize);
    }

    private ModelAndView constructCustomerModelAndView(HttpServletRequest request, CustomerCriteria customerCriteria, String currentPage, String displaysRecords) {
        ModelAndView modelAndView = new ModelAndView("/customer/customerlist");
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("username");
        User user = userService.getUser(userID);
        Page<Customer> page = customerService.searchCustomers(new Page<Customer>(Integer.parseInt(currentPage), Integer.parseInt(displaysRecords)), customerCriteria);
        String entryType = (String) session.getAttribute("entry");
        String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
        modelAndView.addObject("entryType", entryType);
        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("customerCriteria", customerCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("user", user);
        return modelAndView;
    }


    @RequestMapping(value = "/addCustomer", method = RequestMethod.POST)
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer) {
        try {
            customerService.addCustomer(customer);
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

    @RequestMapping(value = "/uploadDailyReportTemplate" , method = RequestMethod.POST)
    public boolean uploadDailyReportTemplate(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
       String customerUuid = request.getParameter("customerUuid");
       String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
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
}
