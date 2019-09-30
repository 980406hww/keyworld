package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.SupplierCriteria;
import com.keymanager.ckadmin.entity.Supplier;
import com.keymanager.ckadmin.service.SupplierService;
import com.keymanager.ckadmin.service.SupplierServiceTypeService;
import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/internal/suppliers")
public class SuppliersController {

    private static Logger logger = LoggerFactory.getLogger(SuppliersController.class);

    @Resource(name = "supplierService2")
    private SupplierService supplierService;

    @Resource(name = "supplierServiceTypeService2")
    private SupplierServiceTypeService supplierServiceTypeService;

    @RequiresPermissions("/internal/supplier/searchSuppliers")
    @GetMapping(value = "/toSuppliers")
    public ModelAndView toSuppliers() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("supplier/supplier");
        return mv;
    }

    @RequiresPermissions("/internal/supplier/searchSuppliers")
    @RequestMapping(value = "/getSuppliersData", method = RequestMethod.POST)
    public ResultBean getSuppliersData(@RequestBody SupplierCriteria criteria) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        try {
//            List<SupplierServiceType> supplierServiceTypes = supplierServiceTypeService.searchSupplierServiceType();
            Page<Supplier> page = supplierService.searchSuppliers(criteria);
//            Map<String, Object> data = new HashMap<>(2);
//            data.put("supplier", page.getRecords());
//            data.put("supplierType", supplierServiceTypes);
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/supplier/deleteSupplier")
    @RequestMapping(value = "/deleteSupplier/{uuid}", method = RequestMethod.GET)
    public ResultBean deleteSupplier(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            supplierService.deleteByUuid(uuid);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }


    @RequiresPermissions("/internal/supplier/deleteSupplier")
    @RequestMapping(value = "/deleteSuppliers", method = RequestMethod.POST)
    public ResultBean deleteSuppliers(@RequestBody List<Integer> uuids) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            for (Integer uuid : uuids) {
                supplierService.deleteByUuid((long) uuid);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
