package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.CustomerBusinessService;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shunshikj40
 * @ClassName CustomerBusinessController
 * @Description 客户业务控制器
 */
@RestController
@RequestMapping("/internal/customerbusiness")
public class CustomerBusinessController extends SpringMVCBaseController {

    private static final Logger logger = LoggerFactory.getLogger(CustomerBusinessController.class);

    @Resource(name = "customerBusinessService2")
    private CustomerBusinessService customerBusinessService;

    @PostMapping("/getCustomerBusinessCount")
    public ResultBean getCustomerBusinessCount(HttpSession session) {
        ResultBean resultBean = new ResultBean(200, "获取各业务下的客户数成功");
        try {
            String loginName = null;
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")) {
                loginName = (String) session.getAttribute("username");
            }
            resultBean.setData(customerBusinessService.getCustomerBusinessCount(loginName));
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
