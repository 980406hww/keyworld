package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.CustomerBusinessService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CustomerBusinessController
 * @Description 客户业务控制器
 * @author shunshikj40
 */
@RestController
@RequestMapping("/internal/customerbusiness")
public class CustomerBusinessController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerBusinessController.class);

    @Resource(name = "customerBusinessService2")
    private CustomerBusinessService customerBusinessService;

    @PostMapping("/getCustomerBusinessCount")
    public ResultBean getCustomerBusinessCount() {
        ResultBean resultBean = new ResultBean(200, "获取各业务下的客户数成功");
        try {
            resultBean.setData(customerBusinessService.getCustomerBusinessCount());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
            return resultBean;
        }
        return resultBean;
    }
}
