package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.criteria.CustomerTypeCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.ckadmin.util.ReflectUtils;
import com.keymanager.ckadmin.util.SQLFilterUtils;
import com.keymanager.ckadmin.vo.CustomerTypeVO;
import com.keymanager.monitoring.common.shiro.ShiroUser;
import com.keymanager.util.TerminalTypeMapping;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
public class CustomerController extends SpringMVCBaseController {

    private Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;

    @RequiresPermissions("/internal/customer/toCustomers")
    @GetMapping(value = "/toCustomers")
    public ModelAndView toCustomers() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customers/customer");
        return mv;
    }

    @RequiresPermissions("/internal/customer/toCustomers")
    @PostMapping(value = "/getCustomers")
    public ResultBean getCustomers(@RequestBody CustomerCriteria customerCriteria, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        if (SQLFilterUtils.sqlInject(customerCriteria.toString())) {
            resultBean.setCode(400);
            resultBean.setMsg("查询参数错误或包含非法字符，请检查后重试！");
            return resultBean;
        }
        try {
            Page<Customer> page = new Page<>(customerCriteria.getPage(), customerCriteria.getLimit());
            String orderByField = ReflectUtils.getTableFieldValue(Customer.class, customerCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
            }
            if (customerCriteria.getOrderMode() != null && customerCriteria.getOrderMode() == 0) {
                page.setAsc(false);
            }
            String loginName = (String) session.getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(loginName));
            if(!isDepartmentManager) {
                customerCriteria.setLoginName(loginName);
            }
            page = customerService.searchCustomers(page, customerCriteria);
            List<Customer> customers = page.getRecords();
            resultBean.setCode(0);
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

    /**
     * 跳转添加或修改用户页面
     *
     * @return
     */
    @RequiresPermissions("/internal/customer/saveCustomer")
    @GetMapping(value = "/toCustomersAdd")
    public ModelAndView toCustomersAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customers/customerAdd");
        return mv;
    }

    /**
     * 获得用户信息
     *
     * @param uuid
     * @param request
     * @return
     */
    @RequiresPermissions("/internal/customer/saveCustomer")
    @GetMapping(value = "/getCustomersMsgById/{uuid}")
    public ResultBean toCustomersAdd(@PathVariable Long uuid, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            HttpSession session = request.getSession();
            String entryType = (String) session.getAttribute("entryType");
            String loginName = (String) session.getAttribute("username");
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            Customer customer = customerService.getCustomerWithKeywordCount(terminalType, entryType, uuid, loginName);
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

    /**
     * 添加用户
     * @param customer
     * @param result
     * @param session
     * @return
     */
    @RequiresPermissions("/internal/customer/saveCustomer")
    @PostMapping(value = "/saveCustomer2")
    public ResultBean saveCustomer2(@RequestBody @Valid Customer customer, BindingResult result,
        HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        if (result.hasFieldErrors()) {
            resultBean.setCode(400);
            resultBean.setMsg("数据校验失败");
            return resultBean;
        }
        String loginName = (String) session.getAttribute("username");
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

    /**
     * 批量删除客户
     * @param requestMap
     * @return
     */
    @RequiresPermissions("/internal/customer/deleteCustomers")
    @PostMapping(value = "/deleteCustomers2")
    public ResultBean deleteCustomers(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
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
    @PostMapping(value = "/updateCustomerDailyReportIdentify2")
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
     * 改变客户状态
     * @param requestMap
     * @return
     */
    @PostMapping(value = "/changeCustomerStatus")
    public ResultBean changeCustomerStatus(@RequestBody Map requestMap) {
        try {
            long uuid = Long.valueOf((String) requestMap.get("customerUuid"));
            int status = (int) requestMap.get("status");
            customerService.changeCustomerStatus(uuid, status);
            return new ResultBean(200, "更新成功");
        } catch (NumberFormatException e) {
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
    @PostMapping(value = "/changeCustomerDailyReportIdentify2")
    public ResultBean changeCustomerDailyReportIdentify(@RequestBody Map requestMap) {
        try {
            long uuid = Long.valueOf((String) requestMap.get("customerUuid"));
            int identify = Integer.valueOf((String) requestMap.get("identify"));
            customerService.changeCustomerDailyReportIdentify(uuid, identify);
            return new ResultBean(200, "更新成功");
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "更新失败");
        }
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @GetMapping(value = "/getActiveCustomers2")
    public ResultBean getActiveCustomers(CustomerCriteria customerCriteria, HttpSession session) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")) {
                String loginName = (String) session.getAttribute("username");
                customerCriteria.setLoginName(loginName);
            }
            List<Customer> customerList = customerService.getActiveCustomerSimpleInfo(customerCriteria);
            resultBean.setData(customerList);
            return resultBean;
        } catch (Exception e) {
            e.printStackTrace();
            resultBean.setCode(400);
            resultBean.setMsg("error");
            return resultBean;
        }
    }


    /**
     * 改变客户标签
     */
    @RequiresPermissions("/internal/customer/saveCustomer")
    @PostMapping(value = "/changeSaleRemark2")
    public ResultBean changeSaleRemark2(@RequestBody Map requestMap) {
        try {
            long uuid = Long.parseLong((String) requestMap.get("uuid"));
            String saleRemark = (String) requestMap.get("saleRemark");
            customerService.changeSaleRemark(uuid, saleRemark);
            return new ResultBean(200, "更新成功");
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "更新失败");
        }
    }

    /**
     * 改变备注
     */
    @RequiresPermissions("/internal/customer/saveCustomer")
    @PostMapping(value = "/changeRemark2")
    public ResultBean changeRemark2(@RequestBody Map requestMap) {
        try {
            long uuid = Long.parseLong((String) requestMap.get("uuid"));
            String remark = (String) requestMap.get("remark");
            customerService.changeRemark(uuid, remark);
            return new ResultBean(200, "更新成功");
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "更新失败");
        }
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @PostMapping(value = "/changeSearchEngine")
    public ResultBean changeSearchEngine(@RequestBody Map requestMap) {
        try {
            long uuid = Long.parseLong((String) requestMap.get("uuid"));
            String searchEngine = (String) requestMap.get("searchEngine");
            customerService.changeSearchEngine(uuid, searchEngine);
            return new ResultBean(200, "更新成功");
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "更新失败");
        }
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @PostMapping(value = "/changeExternalAccount")
    public ResultBean changeExternalAccount(@RequestBody Map requestMap) {
        try {
            long uuid = Long.parseLong((String) requestMap.get("uuid"));
            String externalAccount = (String) requestMap.get("externalAccount");
            customerService.changeExternalAccount(uuid, externalAccount);
            return new ResultBean(200, "更新成功");
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "更新失败");
        }
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywordLists")
    @RequestMapping(value = "/searchCustomerTypeCount2", method = RequestMethod.POST)
    public ResultBean searchCustomerTypeCount2(HttpSession session, @RequestBody CustomerTypeCriteria customerTypeCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String loginName = (String) session.getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(loginName));
            if(!isDepartmentManager) {
                customerTypeCriteria.setLoginName(loginName);
            }
            List<CustomerTypeVO> customerTypeVOList = customerService.searchCustomerTypeCount(customerTypeCriteria);
            resultBean.setData(customerTypeVOList);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @RequestMapping(value = "/searchCustomersWithKeyword2", method = RequestMethod.POST)
    public ResultBean searchCustomersWithKeyword(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            List<String> groupNames = (List<String>) requestMap.get("groupNames");
            resultBean.setData(customerService.searchCustomersWithKeyword(groupNames, terminalType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @GetMapping(value = "/toUpdateBelongUser")
    public ModelAndView toUpdateBelongUser() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customers/updateBelongUser");
        return mv;
    }

    @RequiresPermissions("/internal/customer/saveCustomer")
    @RequestMapping(value = "/updateCustomerUserID2" , method = RequestMethod.POST)
    public ResultBean updateCustomerUserID2(@RequestBody Map<String, Object> requestMap){
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            String userID = (String) requestMap.get("userID");
            customerService.updateCustomerUserID(uuids, userID);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @PostMapping("/getActiveUsersForChangeBelong")
    public ResultBean getActiveUsersForChangeBelong(){
        ResultBean resultBean = new ResultBean();
        try {
            List<UserInfo> activeUsers = userInfoService.findActiveUsers();
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(activeUsers);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @PostMapping("/getTargetCustomers/{entryType}")
    public ResultBean getTargetCustomers(@PathVariable(name = "entryType")String entryType, HttpSession session){
        ResultBean resultBean = new ResultBean();
        try {
            String loginName = (String) session.getAttribute("username");
            String  accountName=null;
            Set<String> roles = getCurrentUser().getRoles();
            if(!roles.contains("DepartmentManager")) {
                accountName=loginName;
            }
            List<Customer> customerList = customerService.searchTargetCustomers(entryType, accountName);
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(customerList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }
}
