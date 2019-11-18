package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.CustomerChargeType;
import com.keymanager.ckadmin.service.CustomerChargeTypeService;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/internal/customerChargeTypes")
public class CustomerChargeTypeController {

    private static Logger logger = LoggerFactory.getLogger(CustomerChargeTypeController.class);

    @Resource(name = "customerChargeTypeService2")
    private CustomerChargeTypeService customerChargeTypeService;

    @RequiresPermissions("/internal/customerChargeType/saveCustomerChargeType")
    @RequestMapping(value = "/saveCustomerChargeType", method = RequestMethod.POST)
    public ResultBean saveCustomerChargeType(@RequestBody CustomerChargeType customerChargeType) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            customerChargeTypeService.saveCustomerChargeType(customerChargeType);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequestMapping(value = "/getCustomerChargeType/{uuid}", method = RequestMethod.GET)
    public ResultBean getCustomerChargeType(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            resultBean.setData(customerChargeTypeService.getCustomerChargeType(uuid));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
