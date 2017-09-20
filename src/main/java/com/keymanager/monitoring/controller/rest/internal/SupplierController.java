package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.criteria.SupplierCriteria;
import com.keymanager.monitoring.dao.SupplierNexusDao;
import com.keymanager.monitoring.entity.Supplier;
import com.keymanager.monitoring.entity.SupplierNexus;
import com.keymanager.monitoring.entity.SupplierServiceType;
import com.keymanager.monitoring.service.SupplierService;
import com.keymanager.monitoring.service.SupplierServiceTypeService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by shunshikj22 on 2017/9/5.
 */
@RestController
@RequestMapping(value = "/internal/supplier")
public class SupplierController {
    private static Logger logger = LoggerFactory.getLogger(SupplierController.class);

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private SupplierServiceTypeService supplierServiceTypeService;

    @RequiresPermissions("/internal/supplier/searchSuppliers")
    @RequestMapping(value = "/searchSuppliers", method = RequestMethod.GET)
    public ModelAndView searchSuppliers(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize,
                                        HttpServletRequest request) {
        return constructCustomerModelAndView(request,new SupplierCriteria(),currentPageNumber, pageSize);
    }

    @RequiresPermissions("/internal/supplier/searchSuppliers")
    @RequestMapping(value = "/searchSuppliers", method = RequestMethod.POST)
    public ModelAndView searchSuppliersPost(HttpServletRequest request , SupplierCriteria supplierCriteria) {
        String currentPageNumber = request.getParameter("currentPageNumber");
        String pageSize = request.getParameter("pageSize");
        if (null == currentPageNumber && null == pageSize) {
            currentPageNumber = "1";
            pageSize = "50";
        }
        return constructCustomerModelAndView(request,supplierCriteria , Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
    }

    private ModelAndView constructCustomerModelAndView(HttpServletRequest request,SupplierCriteria supplierCriteria , int currentPageNumber, int pageSize) {
        ModelAndView modelAndView = new ModelAndView("/supplier/supplierList");
        List<SupplierServiceType> supplierServiceTypes = supplierServiceTypeService.searchSupplierServiceType();
        Page<Supplier> page = supplierService.searchSupplier(new Page<Supplier>(currentPageNumber,pageSize),supplierCriteria);
        modelAndView.addObject("supplierServiceTypes",supplierServiceTypes);
        modelAndView.addObject("supplierCriteria",supplierCriteria);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequiresPermissions("/internal/supplier/delSupplier")
    @RequestMapping(value = "/delSupplier/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> delSupplier(@PathVariable("uuid") Long uuid) {
        try {
            supplierService.deleteSupplier(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/supplier/deleteSuppliers")
    @RequestMapping(value = "/deleteSuppliers" , method = RequestMethod.POST)
    public ResponseEntity<?> deleteSuppliers(@RequestBody Map<String, Object> requestMap){
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            supplierService.deleteAll(uuids);
            return new ResponseEntity<Object>(true , HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Object>(false , HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getSupplier/{uuid}" , method = RequestMethod.GET)
    public ResponseEntity<?> getSupplier(@PathVariable("uuid")Long uuid){
        return new ResponseEntity<Object>(supplierService.getSupplier(uuid), HttpStatus.OK);
    }

    @RequiresPermissions("/internal/supplier/saveSupplier")
    @RequestMapping(value = "/saveSupplier", method = RequestMethod.POST)
    public ResponseEntity<?> saveSupplier(@RequestBody Supplier supplier) {
        try {
            supplierService.saveSupplier(supplier);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
