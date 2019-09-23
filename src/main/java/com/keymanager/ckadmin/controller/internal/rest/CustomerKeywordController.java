package com.keymanager.ckadmin.controller.internal.rest;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.ckadmin.util.ReflectUtils;
import com.keymanager.ckadmin.vo.KeywordCountVO;
import com.keymanager.monitoring.common.shiro.ShiroUser;
import com.keymanager.util.TerminalTypeMapping;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName CustomerKeywordController
 * @Description TODO
 * @Author lhc
 * @Date 2019/9/3 8:56
 * @Version 1.0
 */

@RestController
@RequestMapping(value = "/internal/customerKeyword")
public class CustomerKeywordController {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordController.class);

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toKeywords")
    public ModelAndView toCustomers() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/keyword");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @PostMapping(value = "/getKeywords")
    public ResultBean searchProductKeywords(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<CustomerKeyword> page = new Page(keywordCriteria.getPage(), keywordCriteria.getLimit());
            String orderByField = ReflectUtils
                    .getTableFieldValue(CustomerKeyword.class, keywordCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
            }
            if (keywordCriteria.getOrderMode() != null && keywordCriteria.getOrderMode() == 0) {
                page.setAsc(false);
            }
//            String terminalType = TerminalTypeMapping.getTerminalType(request);
//            keywordCriteria.setTerminalType(terminalType);
            page = customerKeywordService.searchKeywords(page, keywordCriteria);
            List<CustomerKeyword> keywords = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(keywords);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @RequestMapping(value = "/changeCustomerKeywordStatus2", method = RequestMethod.POST)
    public ResultBean changeCustomerKeywordStatus(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String entryType = (String) request.getSession().getAttribute("entryType");
            String customerUuid = (String) requestMap.get("customerUuid");
            String status = (String) requestMap.get("status");
            customerKeywordService.changeCustomerKeywordStatus(terminalType, entryType, Long.parseLong(customerUuid), Integer.parseInt(status));
            return new ResultBean(200,"success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400,"error");
        }
    }

    /**
     * 根据用户id获取关键字统计信息
     * @param customerUuid
     * @return
     */
    @GetMapping("/getCustomerKeywordsCount/{customerUuid}")
    public ResultBean getOperationCombines(@PathVariable Long customerUuid, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            KeywordCountVO keywordCountVO = customerKeywordService.getCustomerKeywordsCountByCustomerUuid(customerUuid, terminalType);
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(keywordCountVO);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @PostMapping(value = "/updateCustomerKeywordStatus2")
    public ResultBean updateCustomerKeywordStatus(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            List<Long> customerKeywordUuids = (List<Long>) requestMap.get("uuids");
            Integer status = (Integer) requestMap.get("status");
            customerKeywordService.updateCustomerKeywordStatus(customerKeywordUuids, status);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordGroupName")
    @RequestMapping(value = "/updateOptimizeGroupName2", method = RequestMethod.POST)
    public ResultBean updateOptimizeGroupName(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            String userName = (String) request.getSession().getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if(!isDepartmentManager) {
                keywordCriteria.setUserName(userName);
            }
            customerKeywordService.updateOptimizeGroupName(keywordCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordMachineGroup")
    @RequestMapping(value = "/updateMachineGroup2", method = RequestMethod.POST)
    public ResultBean updateMachineGroup(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            String userName = (String) request.getSession().getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if(!isDepartmentManager) {
                keywordCriteria.setUserName(userName);
            }
            customerKeywordService.updateMachineGroup(keywordCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
    @RequestMapping(value = "/updateBearPawNumber2", method = RequestMethod.POST)
    public ResultBean updateBearPawNumber(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            customerKeywordService.deleteCustomerKeywordsByDeleteType(keywordCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/deleteCustomerKeywords")
    @RequestMapping(value = "/deleteCustomerKeywords2", method = RequestMethod.POST)
    public ResultBean deleteCustomerKeywords(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            customerKeywordService.deleteCustomerKeywordsByDeleteType(keywordCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/getKeywordTypeByUserRole")
    public ResultBean getKeywordTypeByUserRole(HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            ShiroUser shiroUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
            Set<String> roles = shiroUser.getRoles();
            List<String> businessType = new ArrayList<>();
            if (roles.contains("Operation")) {
                businessType = Arrays.asList(configService.getConfig("BusinessType", "All").getValue().split(","));
            } else if (roles.contains("SEOSales")) {
                businessType = Arrays.asList(configService.getConfig("BusinessType", "SEOSales").getValue().split(","));
            } else if (roles.contains("NegativeSales")) {
                businessType = Arrays.asList(configService.getConfig("BusinessType", "NegativeSales").getValue().split(","));
            }
            resultBean.setData(businessType);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }
}
