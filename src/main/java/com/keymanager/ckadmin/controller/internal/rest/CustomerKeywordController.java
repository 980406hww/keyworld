package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.KeywordStandardCriteria;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.criteria.CustomerKeywordCleanTitleCriteria;
import com.keymanager.ckadmin.criteria.CustomerKeywordUpdateStatusCriteria;
import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordAndUrlCvsExportWriter;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordInfoExcelWriter;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.ckadmin.vo.KeywordStandardVO;
import com.keymanager.ckadmin.vo.MachineGroupQueueVO;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.ckadmin.util.ReflectUtils;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.vo.KeywordCountVO;
import com.keymanager.ckadmin.vo.KeywordStatusBatchUpdateVO;
import com.keymanager.ckadmin.webDo.KeywordCountDO;
import com.keymanager.monitoring.common.shiro.ShiroUser;
import com.keymanager.util.TerminalTypeMapping;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
public class CustomerKeywordController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(CustomerKeywordController.class);

    @Resource(name = "customerKeywordService2")
    private CustomerKeywordService customerKeywordService;

    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "performanceService2")
    private PerformanceService performanceService;

    @RequestMapping(value = "/toMachineGroupAndSize", method = RequestMethod.GET)
    public ModelAndView toSearchWarnLists() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customerKeywords/MachineGroupQueue2");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/showMachineGroupAndSize")
    @RequestMapping(value = "/searchMachineGroupAndSize", method = RequestMethod.POST)
    public ResultBean searchMachineGroupAndSize(HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        long startMilleSeconds = System.currentTimeMillis();
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        try {
            List<MachineGroupQueueVO> machineGroupQueueVOS = customerKeywordService.getMachineGroupAndSize();
            performanceService.addPerformanceLog(terminalType + ":showMachineGroupAndSize", (System.currentTimeMillis() - startMilleSeconds), null);
            resultBean.setCode(0);
            resultBean.setMsg("success");
            resultBean.setData(machineGroupQueueVOS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toKeywords")
    public ModelAndView toCustomers() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/keyword");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @PostMapping(value = "/getKeywords")
    public ResultBean searchKeywords(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<CustomerKeyword> page = new Page(keywordCriteria.getPage(), keywordCriteria.getLimit());
            String orderByField = ReflectUtils.getTableFieldValue(CustomerKeyword.class, keywordCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
                if (keywordCriteria.getOrderMode() != null && keywordCriteria.getOrderMode() == 0) {
                    page.setAsc(false);
                }
            }
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
    @PostMapping(value = "/changeCustomerKeywordStatus2")
    public ResultBean changeCustomerKeywordStatus(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String entryType = (String) request.getSession().getAttribute("entryType");
            String customerUuid = (String) requestMap.get("customerUuid");
            String status = (String) requestMap.get("status");
            customerKeywordService.changeCustomerKeywordStatus(terminalType, entryType, Long.parseLong(customerUuid), Integer.parseInt(status));
            return new ResultBean(200, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "error");
        }
    }

    /**
     * 根据用户id获取关键字统计信息
     */
    @GetMapping("/getCustomerKeywordsCount/{customerUuid}")
    public ResultBean getCustomerKeywordsCount(@PathVariable Long customerUuid, HttpServletRequest request) {
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

    @RequiresPermissions("/internal/customerKeyword/resetInvalidRefreshCount")
    @RequestMapping(value = "/resetInvalidRefresh", method = RequestMethod.POST)
    public ResultBean resetInvalidRefresh(@RequestBody RefreshStatisticsCriteria criteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            criteria.setTerminalType(terminalType);
            customerKeywordService.resetInvalidRefreshCount(criteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @PostMapping(value = "/updateCustomerKeywordStatus2")
    public ResultBean updateCustomerKeywordStatus(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200, "success");
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
    @PostMapping(value = "/updateOptimizeGroupName2")
    public ResultBean updateOptimizeGroupName(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String userName = (String) request.getSession().getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if (!isDepartmentManager) {
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
    @PostMapping(value = "/updateMachineGroup2")
    public ResultBean updateMachineGroup(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String userName = (String) request.getSession().getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if (!isDepartmentManager) {
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
    @PostMapping(value = "/updateBearPawNumber2")
    public ResultBean updateBearPawNumber(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            customerKeywordService.updateBearPawNumber(keywordCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/deleteCustomerKeywords")
    @PostMapping(value = "/deleteCustomerKeywords2")
    public ResultBean deleteCustomerKeywords(@RequestBody KeywordCriteria keywordCriteria) {
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

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toCustomerKeywords/{businessType}/{terminalType}/{customerUuid}")
    public ModelAndView toCustomerKeywords(@PathVariable(name = "businessType") String businessType, @PathVariable(name = "terminalType") String terminalType,
        @PathVariable(name = "customerUuid") Long customerUuid) {
        ModelAndView mv = new ModelAndView();
        Customer customer = customerService.getCustomerByCustomerUuid(terminalType, businessType, customerUuid);
        mv.setViewName("keywords/customerKeyword");
        mv.addObject("businessType", businessType);
        //取名叫terminalType会与session中存在的terminalType同名，值会被覆盖成session中的值
        mv.addObject("terminalType2", terminalType);
        mv.addObject("customer", customer);
        mv.addObject("customerUuid", customerUuid);
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @PostMapping(value = "/getCustomerKeywords")
    public ResultBean searchCustomerKeywords(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<CustomerKeyword> page = new Page(keywordCriteria.getPage(), keywordCriteria.getLimit());
            String orderByField = ReflectUtils
                .getTableFieldValue(CustomerKeyword.class, keywordCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
                if (keywordCriteria.getOrderMode() != null && keywordCriteria.getOrderMode() == 0) {
                    page.setAsc(false);
                }
            }
            page = customerKeywordService.searchCustomerKeywords(page, keywordCriteria);
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

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toCustomerKeywordAdd")
    public ModelAndView toCustomerKeywordAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/customerKeywordAdd");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
    @PostMapping(value = "/saveCustomerKeyword2")
    public ResultBean saveCustomerKeyword(@RequestBody CustomerKeyword customerKeyword, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String userName = (String) request.getSession().getAttribute("username");
            if (customerKeyword.getUuid() == null) {
                customerKeywordService.saveCustomerKeyword(customerKeyword, userName);
            } else {
                customerKeywordService.updateCustomerKeywordFromUI(customerKeyword, userName);
            }
            return resultBean;
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("fail");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordMachineGroup")
    @PostMapping(value = "/updateOptimizePlanCount2")
    public ResultBean updateOptimizePlanCount(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String userName = (String) request.getSession().getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if (!isDepartmentManager) {
                keywordCriteria.setUserName(userName);
            }
            customerKeywordService.updateOptimizePlanCount(keywordCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toUploadKeywords")
    public ModelAndView toUploadKeywords() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/UploadKeywordByExcel");
        return mv;
    }

    //关键字Excel上传(简化版)
    @RequiresPermissions("/internal/customerKeyword/uploadCustomerKeywords")
    @PostMapping(value = "/uploadCustomerKeywords2")
    public ResultBean uploadCustomerKeywords(KeywordCountDO keywordCountDO, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        String userName = (String) request.getSession().getAttribute("username");
        try {
            boolean uploaded = customerKeywordService
                .handleExcel(keywordCountDO.getFile().getInputStream(), keywordCountDO.getExcelType(), keywordCountDO.getCustomerUuid(),
                    keywordCountDO.getEntryType(), keywordCountDO.getTerminalType(), userName);
            if (uploaded) {
                resultBean.setMsg("文件上传成功");
            } else {
                resultBean.setMsg("文件解析异常");
            }
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/customerKeyword/deleteCustomerKeyword")
    @PostMapping(value = "/deleteCustomerKeyword2/{customerKeywordUuid}")
    public ResultBean deleteCustomerKeyword(@PathVariable("customerKeywordUuid") Long customerKeywordUuid, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            customerKeywordService.deleteById(customerKeywordUuid);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
    }

    //导出成Excel文件
    @RequiresPermissions("/internal/customerKeyword/downloadCustomerKeywordInfo")
    @PostMapping(value = "/downloadCustomerKeywordInfo2")
    public ResultBean downloadCustomerKeywordInfo(HttpServletRequest request, HttpServletResponse response, KeywordCriteria keywordCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<CustomerKeyword> customerKeywords = customerKeywordService.searchCustomerKeywordInfo(keywordCriteria);
            if (!Utils.isEmpty(customerKeywords)) {
                CustomerKeywordInfoExcelWriter excelWriter = new CustomerKeywordInfoExcelWriter();
                excelWriter.writeDataToExcel(customerKeywords);
                Customer customer = customerService.selectById(keywordCriteria.getCustomerUuid());
                String fileName = customer.getContactPerson() + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd") + ".xls";
                fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
                byte[] buffer = excelWriter.getExcelContentBytes();
                downExcelFile(response, fileName, buffer);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/cleanTitle")
    @PostMapping(value = "/keywordUrlExport2")
    public ResultBean keywordUrlExport(HttpServletRequest request, HttpServletResponse response, KeywordCriteria keywordCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<Map> dataList = customerKeywordService.searchAllKeywordAndUrl(keywordCriteria.getCustomerUuid(), keywordCriteria.getTerminalType());
            Customer customer = customerService.selectById(keywordCriteria.getCustomerUuid());
            CustomerKeywordAndUrlCvsExportWriter.exportCsv(dataList);
            CustomerKeywordAndUrlCvsExportWriter.downloadZip(response, customer.getContactPerson(), keywordCriteria.getTerminalType());
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toChangeKeywordsSearchEngine")
    public ModelAndView toChangeKeywordsSearchEngine() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/changeKeywordSeachEngine");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
    @PostMapping(value = "/updateSearchEngine2")
    public ResultBean updateSearchEngine2(@RequestBody KeywordCriteria keywordCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            customerKeywordService.updateSearchEngine(keywordCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }


    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @PostMapping(value = "/changeCustomerKeywordStatus3")
    public ResultBean changeCustomerKeywordStatusInCKPage(@RequestBody CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria,
        HttpServletRequest request) {
        try {
            customerKeywordService.changeCustomerKeywordStatusInCKPage(customerKeywordUpdateStatusCriteria);
            return new ResultBean(200, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "error");
        }
    }

    //重采标题
    @RequiresPermissions("/internal/customerKeyword/cleanTitle")
    @PostMapping(value = "/cleanTitle2")
    public ResultBean cleanTitle(@RequestBody CustomerKeywordCleanTitleCriteria customerKeywordCleanTitleCriteria, HttpServletRequest request) {
        try {
            customerKeywordService.cleanTitle(customerKeywordCleanTitleCriteria);
            return new ResultBean(200, "success");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResultBean(400, "error");
        }
    }

    @RequiresPermissions("/internal/customerKeyword/deleteCustomerKeywords")
    @PostMapping(value = "/deleteDuplicateCustomerKeywords2")
    public ResultBean deleteDuplicateCustomerKeyword(@RequestBody CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria) {
        try {
            customerKeywordService.deleteDuplicateKeywords(customerKeywordUpdateStatusCriteria);
            return new ResultBean(200, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "error");
        }
    }

    @RequiresPermissions("/internal/customerKeyword/deleteCustomerKeywords")
    @PostMapping(value = "/getKeywordInfoByUuid/{uuid}")
    public ResultBean getKeywordInfoByUuid(@PathVariable Long uuid) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            CustomerKeyword customerKeyword = customerKeywordService.getKeywordInfoByUuid(uuid);
            resultBean.setData(customerKeyword);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
    @GetMapping(value = "/toCustomerKeywordBatchUpdate")
    public ModelAndView toCustomerKeywordBatchUpdate() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/customerKeywordBatchUpdate");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
    @RequestMapping(value = "batchUpdateKeywords2", method = RequestMethod.POST)
    public ResultBean batchUpdateKeywords(@RequestBody KeywordStatusBatchUpdateVO keywordStatusBatchUpdateVO) {
        try {
            customerKeywordService.batchUpdateKeywords(keywordStatusBatchUpdateVO);
            return new ResultBean(200, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, "error");
        }
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywordLists")
    @RequestMapping(value = "/searchCustomerKeywordForNoReachStandard2", method = RequestMethod.POST)
    public ResultBean searchCustomerKeywordForNoReachStandard(@RequestBody KeywordStandardCriteria keywordStandardCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            keywordStandardCriteria.setStatus(1);
            KeywordStandardVO keywordStandardVO = customerKeywordService.searchCustomerKeywordForNoReachStandard(keywordStandardCriteria);
            resultBean.setData(keywordStandardVO);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("fail");
            return resultBean;
        }
    }
}
