package com.keymanager.ckadmin.controller.external.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.base.ExternalBaseCriteria;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/external/customer")
public class ExternalCustomerController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalCustomerController.class);

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    /**
     * 获得指定客户列表
     *
     * @param baseCriteria 用户名密码
     * @return .data [CustomerObj] 数据类型
     */
    @RequestMapping(value = "/getActiveCustomerSimpleInfo2", method = RequestMethod.POST)
    public ResultBean getCustomerList(@RequestBody ExternalBaseCriteria baseCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (validUser(baseCriteria.getUserName(), baseCriteria.getPassword())) {
                boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(baseCriteria.getUserName()));
                if (isDepartmentManager) {
                    baseCriteria.setUserName(null);
                }
                resultBean.setData(customerService.getCustomerListByUser(baseCriteria.getUserName(), "fm"));
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("账号密码无效");
            }
        } catch (Exception e) {
            logger.error("ExternalCustomerController.getCustomerList()" + e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
