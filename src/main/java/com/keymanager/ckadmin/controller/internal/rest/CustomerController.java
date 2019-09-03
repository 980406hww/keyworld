package com.keymanager.ckadmin.controller.internal.rest;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.CustomerInterface;
import com.keymanager.ckadmin.service.UserInfoInterface;
import com.keymanager.util.TerminalTypeMapping;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.BindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName CustomerController
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/2 9:56
 * @Version 1.0
 */
@RestController
@RequestMapping("/internal/customer")
public class CustomerController {
    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Resource(name = "customerService2")
    private CustomerInterface customerService2;

    @Resource(name = "userInfoSerice2")
    private UserInfoInterface userInfoService2;

    @RequiresPermissions("/internal/customer/toCustomers")
    @RequestMapping(value = "/toCustomers", method = RequestMethod.GET)
    public ModelAndView toAlgorithmTestPlans() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customers/customer");
        return mv;
    }

    @RequestMapping(value = "/getCustomers")
    public String getAlgorithmTestPlans(HttpServletRequest request, @RequestBody CustomerCriteria customerCriteria) {
        HttpSession session = request.getSession();
        String loginName = (String) session.getAttribute("username");

        String entryType = (String) session.getAttribute("entryType");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        customerCriteria.setEntryType(entryType);
        customerCriteria.setTerminalType(terminalType);
        Page<Customer> page = new Page(customerCriteria.getPage(), customerCriteria.getLimit());
        page = customerService2.searchCustomers(page, customerCriteria);
        List<Customer> customers = page.getRecords();
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setEntryType(entryType);
        resultBean.setCount(page.getTotal());
        resultBean.setMsg("");
        resultBean.setData(customers);
        String s = JSON.toJSONString(resultBean);
        return s;
    }

    @RequestMapping("/getActiveUsers")
    public List<UserInfo> getActiveUsers() {
        List<UserInfo> activeUsers = userInfoService2.findActiveUsers();

        return activeUsers;
    }

    //跳转添加或修改用户页面
    @RequiresPermissions("/internal/customer/toCustomers")
    @RequestMapping(value = "/toCustomersAdd", method = RequestMethod.GET)
    public ModelAndView toCustomersAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customers/customerAdd");
        return mv;
    }

    //添加用户
    @RequiresPermissions("/internal/customer/toCustomers")
    @RequestMapping(value = "/postCustomersAdd", method = RequestMethod.POST)
    public ResultBean postCustomersAdd(@Valid Customer customer, BindingResult result, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        if (result.hasFieldErrors()) {
            resultBean.setCode(400);
            resultBean.setMsg("数据校验失败");
            return resultBean;
        }
        String loginName = (String) session.getAttribute("username");
        customerService2.saveCustomer(customer, loginName);
        resultBean.setMsg("添加成功");
        return resultBean;
    }
    @RequiresPermissions("/internal/customer/delCustomer")
    @RequestMapping(value = "/delCustomer2/{uuid}", method = RequestMethod.GET)
    public ResponseEntity<?> delCustomer(@PathVariable("uuid") Long uuid) {
        try {
            customerService2.deleteCustomer(uuid);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.OK);
        }
    }
}
