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

    @RequestMapping(value = "searchCustomers" ,method = RequestMethod.GET)
    public ModelAndView searchCustomers(@RequestParam(defaultValue = "1") int currentPage,@RequestParam(defaultValue="30") int displaysRecords,HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView("/customer/customerlist");
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("username");
        User user = userService.getUser(userID);
        Page<Customer> page = customerService.searchCustomers(new Page<Customer>(currentPage,displaysRecords),new CustomerCriteria());
        String entryType = (String) session.getAttribute("entry");
        String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("entryType",entryType);
        modelAndView.addObject("page",page);
        modelAndView.addObject("user",user);
        return modelAndView;
    }
    @RequestMapping(value = "searchCustomers" ,method = RequestMethod.POST)
    public ModelAndView searchCustomersPost(HttpServletRequest request,CustomerCriteria customerCriteria){
        ModelAndView modelAndView = new ModelAndView("/customer/customerlist");
        HttpSession session = request.getSession();
        String userID = (String) session.getAttribute("username");
        User user = userService.getUser(userID);
        String  currentPage = request.getParameter("currentPageHidden");//
        String  displaysRecords = request.getParameter("displayRerondsHidden");
        if(null==currentPage&&null==currentPage){
            currentPage="1";
            displaysRecords="30";
        }
        Page<Customer> page = customerService.searchCustomers(new Page<Customer>(Integer.parseInt(currentPage),Integer.parseInt(displaysRecords)),customerCriteria);
        String entryType = (String) session.getAttribute("entry");
        String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
        modelAndView.addObject("entryType", entryType);
        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("customerCriteria", customerCriteria);
        modelAndView.addObject("page", page);
        modelAndView.addObject("user", user);
        return modelAndView;
    }


    @RequestMapping(value = "addCustomer" , method = RequestMethod.POST)
    public ResponseEntity<?> addCustomer(@RequestBody Customer customer){
        if(customerService.addCustomer(customer)){
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        }
        return new ResponseEntity<Object>(false, HttpStatus.OK);
    }


    @RequestMapping(value = "getCustomer/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getCustomer(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(customerService.getCustomerWithKeywordCount(uuid), HttpStatus.OK);
    }

    @RequestMapping(value = "delCustomer/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> delCustomer(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(customerService.deleteCustomer(uuid), HttpStatus.OK);
    }

    @RequestMapping(value = "deleteCustomerForms" , method = RequestMethod.POST)
    public ResponseEntity<?> deleteCustomerForms(@RequestBody Map<String, Object> requestMap){
        List<String> uuids = (List<String>) requestMap.get("uuids");
        return new ResponseEntity<Object>(customerService.deleteAll(uuids) , HttpStatus.OK);
    }

    @RequestMapping(value = "uploadTheDailyReportTemplate" , method = RequestMethod.POST)
    public boolean uploadTheDailyReportTemplate(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request){
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
