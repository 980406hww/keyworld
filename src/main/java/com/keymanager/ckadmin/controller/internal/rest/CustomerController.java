package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.util.ReflectUtils;
import com.keymanager.ckadmin.util.SQLFilterUtils;
import com.keymanager.util.TerminalTypeMapping;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
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
    private CustomerService customerService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @RequiresPermissions("/internal/customer/searchCustomers")
    @RequestMapping(value = "/toCustomers", method = RequestMethod.GET)
    public ModelAndView toAlgorithmTestPlans() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customers/customer");
        return mv;
    }

    @RequiresPermissions("/internal/customer/searchCustomers")
    @RequestMapping(value = "/getCustomers")
    public ResultBean getAlgorithmTestPlans(HttpServletRequest request,
        @RequestBody CustomerCriteria customerCriteria) {
        ResultBean resultBean = new ResultBean();
        if (SQLFilterUtils.sqlInject(customerCriteria.toString())) {
            resultBean.setCode(400);
            resultBean.setMsg("查询参数错误或包含非法字符，请检查后重试！");
            return resultBean;
        }
        try {
            HttpSession session = request.getSession();
            String entryType = (String) session.getAttribute("entryType");
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            customerCriteria.setEntryType(entryType);
            customerCriteria.setTerminalType(terminalType);
            Page<Customer> page = new Page(customerCriteria.getPage(), customerCriteria.getLimit());
            String orderByField = ReflectUtils
                .getTableFieldValue(Customer.class, customerCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
            }
            if (customerCriteria.getOrderMode() != null && customerCriteria.getOrderMode() == 0) {
                page.setAsc(false);
            }
            page = customerService.searchCustomers(page, customerCriteria);
            List<Customer> customers = page.getRecords();
            resultBean.setCode(0);
            resultBean.setEntryType(entryType);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("");
            resultBean.setData(customers);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping("/getActiveUsers")
    public List<UserInfo> getActiveUsers() {
        List<UserInfo> activeUsers = userInfoService.findActiveUsers();
        return activeUsers;
    }

    //跳转添加或修改用户页面
    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/toCustomersAdd", method = RequestMethod.GET)
    public ModelAndView toCustomersAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customers/customerAdd");
        return mv;
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/toQzsetting", method = RequestMethod.GET)
    public ModelAndView toQzsetting() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsetting/qzsetting");
        return mv;
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/toQzsettingAdd", method = RequestMethod.GET)
    public ModelAndView toQzsettingAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsetting/qzsettingAdd");
        return mv;
    }

    //获得用户信息
    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/getCustomersMsgById/{uuid}", method = RequestMethod.GET)
    public ResultBean toCustomersAdd(@PathVariable Long uuid, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            HttpSession session = request.getSession();
            String entryType = (String) session.getAttribute("entryType");
            String loginName = (String) session.getAttribute("username");
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            Customer customer = customerService
                .getCustomerWithKeywordCount(terminalType, entryType, uuid, loginName);
            if (customer != null) {
                resultBean.setCode(200);
                resultBean.setData(customer);
                return resultBean;
            }
            resultBean.setCode(400);
            resultBean.setMsg("未找到数据，或出现系统异常");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    //添加用户
    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/postCustomersAdd", method = RequestMethod.POST)
    public ResultBean postCustomersAdd(@Valid Customer customer, BindingResult result,
        HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        if (result.hasFieldErrors()) {
            resultBean.setCode(400);
            resultBean.setMsg("数据校验失败");
            return resultBean;
        }
        String loginName = (String) session.getAttribute("username");
//        String entryType = (String) session.getAttribute("entryType");
//        customer.setEntryType(entryType);
        try {
            customerService.saveCustomer(customer, loginName);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        resultBean.setMsg("添加成功");
        return resultBean;
    }


    @RequiresPermissions("/internal/customer/deleteCustomers")
    @RequestMapping(value = "/deleteCustomers2", method = RequestMethod.POST)
    public ResultBean deleteCustomers(@RequestBody Map<String, Object> requestMap) {
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            customerService.deleteAll(uuids);
            return new ResultBean(200, "删除成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "删除失败");
        }
    }

    /**
     * 更新客户日报表
     *
     * @param requestMap
     * @return
     */
    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/updateCustomerDailyReportIdentify2", method = RequestMethod.POST)
    public ResultBean updateCustomerDailyReportIdentify(@RequestBody Map requestMap) {
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            customerService.updateCustomerDailyReportIdentify(uuids);
            return new ResultBean(200, "更新成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "更新失败");
        }
    }

    /**
     * 改变客户是否产生日报表标志位值
     *
     * @param requestMap
     * @return
     */
    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/changeCustomerDailyReportIdentify2", method = RequestMethod.POST)
    public ResultBean changeCustomerDailyReportIdentify(@RequestBody Map requestMap) {
        try {
            long uuid = Long.valueOf((String) requestMap.get("customerUuid"));
            boolean identify = Boolean.valueOf((String) requestMap.get("identify"));
            customerService.changeCustomerDailyReportIdentify(uuid, identify);
            return new ResultBean(200, "更新成功");
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "更新失败");
        }
    }

}
