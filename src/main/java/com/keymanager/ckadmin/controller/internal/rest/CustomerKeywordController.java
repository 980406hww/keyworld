package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.CustomerKeywordCleanTitleCriteria;
import com.keymanager.ckadmin.criteria.CustomerKeywordUpdateStatusCriteria;
import com.keymanager.ckadmin.criteria.KeywordCriteria;
import com.keymanager.ckadmin.criteria.KeywordStandardCriteria;
import com.keymanager.ckadmin.criteria.PTKeywordCountCriteria;
import com.keymanager.ckadmin.criteria.RefreshStatisticsCriteria;
import com.keymanager.ckadmin.criteria.base.BaseCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.entity.CustomerKeyword;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.enums.EntryTypeEnum;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordAndUrlCvsExportWriter;
import com.keymanager.ckadmin.excel.operator.CustomerKeywordInfoExcelWriter;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.CustomerKeywordService;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import com.keymanager.ckadmin.util.ReflectUtils;
import com.keymanager.ckadmin.util.Utils;
import com.keymanager.ckadmin.vo.CustomerKeywordUploadVO;
import com.keymanager.ckadmin.vo.KeywordStandardVO;
import com.keymanager.ckadmin.vo.KeywordStatusBatchUpdateVO;
import com.keymanager.ckadmin.vo.MachineGroupQueueVO;
import com.keymanager.ckadmin.vo.PTkeywordCountVO;
import com.keymanager.ckadmin.common.shiro.ShiroUser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.collections.CollectionUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @RequestMapping(value = "/toMachineGroupAndSize", method = RequestMethod.GET)
    public ModelAndView toSearchWarnLists() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("customerKeywords/MachineGroupQueue2");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/toMachineGroupAndSize")
    @RequestMapping(value = "/searchMachineGroupAndSize", method = RequestMethod.POST)
    public ResultBean searchMachineGroupAndSize(@RequestBody BaseCriteria criteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(criteria.getInit())) {
            return resultBean;
        }
        try {
            List<MachineGroupQueueVO> machineGroupQueueVos = customerKeywordService.getMachineGroupAndSize();
            resultBean.setData(machineGroupQueueVos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/toKeywords")
    @GetMapping(value = "/toKeywords")
    public ModelAndView toCustomers() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/keyword");
        return mv;
    }


    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @PostMapping(value = "/getKeywords")
    public ResultBean searchKeywords(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(keywordCriteria.getInit())) {
            return resultBean;
        }
        try {
//            Set<String> roles = getCurrentUser().getRoles();
//            if (!roles.contains("DepartmentManager")) {
//                keywordCriteria.setUserName((String) request.getSession().getAttribute("username"));
//            }
            UserInfo userInfo = userInfoService.getUserInfo(getCurrentUser().getLoginName());
            if (userInfo.getDataAuthority().equals("self")){
                keywordCriteria.setUserName(userInfo.getLoginName());
            }else if (userInfo.getDataAuthority().equals("department")){
                keywordCriteria.setOrganizationID(userInfo.getOrganizationID());
            }
            Page<CustomerKeyword> page = new Page<>(keywordCriteria.getPage(), keywordCriteria.getLimit());
            String orderByField = ReflectUtils.getTableFieldValue(CustomerKeyword.class, keywordCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
                if (keywordCriteria.getOrderMode() != null && keywordCriteria.getOrderMode() == 0) {
                    page.setAsc(false);
                }
            }
            page = customerKeywordService.searchKeywords(page, keywordCriteria);
            List<CustomerKeyword> keywords = page.getRecords();
            resultBean.setCount(page.getTotal());
            resultBean.setData(keywords);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @PostMapping(value = "/changeCustomerKeywordStatus2")
    public ResultBean changeCustomerKeywordStatus(@RequestBody Map<String, Object> requestMap) {
        try {
            String customerUuid = (String) requestMap.get("customerUuid");
            String entryType = (String) requestMap.get("entryType");
            String status = (String) requestMap.get("status");
            String terminalType = (String) requestMap.get("terminalType");
            customerKeywordService.changeCustomerKeywordStatus(terminalType, entryType, Long.parseLong(customerUuid), Integer.parseInt(status));
            return new ResultBean(200, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, e.getMessage());
        }
    }

    /**
     * 根据用户id获取关键字统计信息
     */
    @GetMapping("/getCustomerKeywordsCount/{customerUuid}/{type}")
    public ResultBean getCustomerKeywordsCount(@PathVariable Long customerUuid, @PathVariable String type, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            resultBean.setData(customerKeywordService.getCustomerKeywordsCountByCustomerUuid(customerUuid, type));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/resetInvalidRefreshCount")
    @RequestMapping(value = "/resetInvalidRefresh", method = RequestMethod.POST)
    public ResultBean resetInvalidRefresh(@RequestBody RefreshStatisticsCriteria criteria) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
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
            resultBean.setMsg(e.getMessage());
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
            resultBean.setMsg(e.getMessage());
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
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
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
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
    @PostMapping(value = "/updCustomerKeywordFormQz")
    public ResultBean updCustomerKeywordFormQz(@RequestBody Map<String, Object> map) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<Long> ckUuids = (List<Long>) map.get("customerKeywordUuids");
            String qzUuid = (String) map.get("qzSttingUuid");
            if (CollectionUtils.isNotEmpty(ckUuids) && StringUtils.isNotEmpty(qzUuid)) {
                customerKeywordService.updCustomerKeywordFormQz(ckUuids, Long.parseLong(qzUuid));
            } else {
                resultBean.setCode(400);
                resultBean.setMsg("数据错误");
            }
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
            resultBean.setMsg(e.getMessage());
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
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/toCustomerKeywords")
    @GetMapping(value = {"/toCustomerKeywords/{businessType}/{terminalType}/{customerUuid}", "/toCustomerKeywords/{businessType}/{terminalType}/{customerUuid}/{pauseType}"})
    public ModelAndView toCustomerKeywords(@PathVariable(name = "businessType") String businessType, @PathVariable(name = "terminalType") String terminalType,
        @PathVariable(name = "customerUuid") Long customerUuid, @PathVariable(name = "pauseType", required = false) String pauseType) {
        ModelAndView mv = new ModelAndView();
        Customer customer = customerService.getCustomerByCustomerUuid(terminalType, businessType, customerUuid);
        mv.setViewName("keywords/customerKeyword");
        mv.addObject("businessType", businessType);
        //取名叫terminalType会与session中存在的terminalType同名，值会被覆盖成session中的值
        mv.addObject("terminalType2", terminalType);
        mv.addObject("customer", customer);
        mv.addObject("customerUuid", customerUuid);
        if (null != pauseType){
            if (pauseType.equals("optimizeStopCount")){
                mv.addObject("optimizeStatus", 0);
            }else if (pauseType.equals("invalidDaysStopCount")){
                mv.addObject("gtInvalidDays", 4);
            }else if (pauseType.equals("noEffectStopCount")){
                mv.addObject("gtNoEffectDays", 30);
            }
        }
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/toKeywords")
    @PostMapping(value = "/getCustomerKeywords")
    public ResultBean searchCustomerKeywords(@RequestBody KeywordCriteria keywordCriteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        try {
            Page<CustomerKeyword> page = new Page<>(keywordCriteria.getPage(), keywordCriteria.getLimit());
            String orderByField = ReflectUtils.getTableFieldValue(CustomerKeyword.class, keywordCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
                if (keywordCriteria.getOrderMode() != null && keywordCriteria.getOrderMode() == 0) {
                    page.setAsc(false);
                }
            }
            page = customerKeywordService.searchCustomerKeywords(page, keywordCriteria);
            List<CustomerKeyword> keywords = page.getRecords();
            resultBean.setCount(page.getTotal());
            resultBean.setData(keywords);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
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
            resultBean.setMsg(ex.getMessage());
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
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toUploadKeywords")
    public ModelAndView toUploadKeywords() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/uploadKeywordByExcel");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toDownKeywords")
    public ModelAndView toDownKeywords() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/downKeywordByExcel");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @PostMapping(value = "/batchDownKeywords")
    public ResultBean batchDownKeywords(CustomerKeywordUploadVO customerKeywordUploadVo, String loginName) {
        ResultBean resultBean = new ResultBean(200, "success");
        if(StringUtils.isEmpty(loginName)){
            loginName = getCurrentUser().getLoginName();
        }
        try {
            boolean uploaded = customerKeywordService.batchDownKeywordsForExcel(customerKeywordUploadVo, loginName);
            if (uploaded) {
                resultBean.setMsg("批量下架成功");
            } else {
                resultBean.setMsg("文件解析异常");
            }
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    /**
     * 关键字Excel上传(简化版)
     */
    @RequiresPermissions("/internal/customerKeyword/uploadCustomerKeywords")
    @PostMapping(value = "/uploadCustomerKeywords2")
    public ResultBean uploadCustomerKeywords(CustomerKeywordUploadVO customerKeywordUploadVo, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        String userName = (String) request.getSession().getAttribute("username");
        try {
            boolean uploaded = customerKeywordService.handleExcel(customerKeywordUploadVo.getFile().getInputStream(), customerKeywordUploadVo.getExcelType(),
                customerKeywordUploadVo.getCustomerUuid(), customerKeywordUploadVo.getQzUuid(), customerKeywordUploadVo.getEntryType(), customerKeywordUploadVo.getTerminalType(), userName);
            if (uploaded) {
                resultBean.setMsg("文件上传成功");
            } else {
                resultBean.setMsg("文件解析异常");
            }
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
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
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    /**
     * 导出成Excel文件
     */
    @RequiresPermissions("/internal/customerKeyword/downloadCustomerKeywordInfo")
    @PostMapping(value = "/downloadCustomerKeywordInfo2")
    public ResultBean downloadCustomerKeywordInfo(HttpServletResponse response, KeywordCriteria keywordCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            String orderByField = ReflectUtils.getTableFieldValue(CustomerKeyword.class, keywordCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                keywordCriteria.setOrderingElement(orderByField);
                if (keywordCriteria.getOrderMode() != null && keywordCriteria.getOrderMode() == 0) {
                    keywordCriteria.setOrderingElement(orderByField + " DESC");
                }
            } else {
                keywordCriteria.setOrderingElement("fKeyword DESC");
            }
            List<CustomerKeyword> customerKeywords = customerKeywordService.searchCustomerKeywordInfo(keywordCriteria);
            if (CollectionUtils.isNotEmpty(customerKeywords)) {
                CustomerKeywordInfoExcelWriter excelWriter = new CustomerKeywordInfoExcelWriter();
                excelWriter.writeDataToExcel(customerKeywords);
                Customer customer = customerService.selectById(keywordCriteria.getCustomerUuid());
                String fileName;
                if (EntryTypeEnum.qz.name().equals(keywordCriteria.getType()) && null != keywordCriteria.getQzUuid() && keywordCriteria.getQzUuid() > 0) {
                    QZSetting qzSetting = qzSettingService.selectById(keywordCriteria.getQzUuid());
                    fileName = customer.getContactPerson() + "_" + qzSetting.getDomain() + "_" + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd") + ".xls";
                } else {
                    fileName = customer.getContactPerson() + "_" + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd") + ".xls";
                }
                fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
                byte[] buffer = excelWriter.getExcelContentBytes();
                downExcelFile(response, fileName, buffer);
            }
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/customerKeyword/cleanTitle")
    @PostMapping(value = "/keywordUrlExport2")
    public ResultBean keywordUrlExport(HttpServletRequest request, HttpServletResponse response, KeywordCriteria keywordCriteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<Map> dataList = customerKeywordService.searchAllKeywordAndUrl(keywordCriteria.getCustomerUuid(), keywordCriteria.getTerminalType(), keywordCriteria.getType());
            Customer customer = customerService.selectById(keywordCriteria.getCustomerUuid());
            CustomerKeywordAndUrlCvsExportWriter.exportCsv(dataList);
            CustomerKeywordAndUrlCvsExportWriter.downloadZip(response, customer.getContactPerson(), keywordCriteria.getTerminalType());
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
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
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }


    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @PostMapping(value = "/changeCustomerKeywordStatus3")
    public ResultBean changeCustomerKeywordStatusInCKPage(@RequestBody CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria) {
        try {
            customerKeywordService.changeCustomerKeywordStatusInCKPage(customerKeywordUpdateStatusCriteria);
            return new ResultBean(200, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, e.getMessage());
        }
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @PostMapping(value = "/updCKStatusFromQZ")
    public ResultBean updCKStatusFromQZ(@RequestBody Map<String, Object> condition) {
        try {
            customerKeywordService.updCKStatusFromQZ(condition);
            return new ResultBean(200, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, e.getMessage());
        }
    }

    //重采标题
    @RequiresPermissions("/internal/customerKeyword/cleanTitle")
    @PostMapping(value = "/cleanTitle2")
    public ResultBean cleanTitle(@RequestBody CustomerKeywordCleanTitleCriteria customerKeywordCleanTitleCriteria) {
        try {
            customerKeywordService.cleanTitle(customerKeywordCleanTitleCriteria);
            return new ResultBean(200, "success");
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            return new ResultBean(400, ex.getMessage());
        }
    }

    /**
     * 删除重复关键词
     */
    @RequiresPermissions("/internal/customerKeyword/deleteCustomerKeywords")
    @PostMapping(value = "/deleteDuplicateCustomerKeywords2")
    public ResultBean deleteDuplicateCustomerKeyword(@RequestBody CustomerKeywordUpdateStatusCriteria customerKeywordUpdateStatusCriteria) {
        try {
            customerKeywordService.deleteDuplicateKeywords(customerKeywordUpdateStatusCriteria);
            return new ResultBean(200, "success");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResultBean(400, e.getMessage());
        }
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
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
            resultBean.setMsg(e.getMessage());
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
            return new ResultBean(400, e.getMessage());
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
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toPTKeywordCount")
    public ModelAndView toPTKeywordCount() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/ptKeywordCount");
        return mv;
    }

    @PostMapping("/getSearchData")
    public ResultBean returnSelectData(HttpServletRequest request, @RequestBody PTKeywordCountCriteria ptKeywordCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            HttpSession session = request.getSession();
            String loginName = (String) session.getAttribute("username");
            UserInfo user = userInfoService.getUserInfo(loginName);
//            List<UserInfo> activeUsers = userInfoService.findActiveUsers();
            List<UserInfo> activeUsers = userInfoService.getActiveUsersByAuthority(loginName);
            Map<String, String> searchEngineMap = configService.getSearchEngineMap(ptKeywordCriteria.getTerminalType());
            Map<String, Object> data = new HashMap<>(3);
            data.put("user", user);
            data.put("activeUsers", activeUsers);
            data.put("searchEngineMap", searchEngineMap);
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(data);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
    }

    @PostMapping(value = "/getPTKeywords")
    public ResultBean getPTKeywords(@RequestBody PTKeywordCountCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(keywordCriteria.getInit())) {
            return resultBean;
        }
        try {
            Page<PTkeywordCountVO> page = new Page<>(keywordCriteria.getPage(), keywordCriteria.getLimit());
            String orderByField = ReflectUtils.getTableFieldValue(CustomerKeyword.class, keywordCriteria.getOrderBy());
            if (StringUtils.isNotEmpty(orderByField)) {
                page.setOrderByField(orderByField);
                if (keywordCriteria.getOrderMode() != null && keywordCriteria.getOrderMode() == 0) {
                    page.setAsc(false);
                }
            }
            UserInfo userInfo = userInfoService.getUserInfo(getCurrentUser().getLoginName());
            if (userInfo.getDataAuthority().equals("self")){
                keywordCriteria.setUserName(userInfo.getLoginName());
            }else if (userInfo.getDataAuthority().equals("department")){
                keywordCriteria.setOrganizationID(userInfo.getOrganizationID());
            }
            page = customerKeywordService.searchPTKeywordCount(page, keywordCriteria);
            List<PTkeywordCountVO> keywords = page.getRecords();
            resultBean.setCount(page.getTotal());
            resultBean.setData(keywords);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/saveCustomerKeywords")
    @RequestMapping(value = "/saveCustomerKeywords2", method = RequestMethod.POST)
    public ResultBean saveCustomerKeywords(@RequestBody Map<String,Object> map, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<CustomerKeyword> customerKeywords = new ObjectMapper().convertValue(map.get("customerKeywords"), new TypeReference<List<CustomerKeyword>>() {
            });
            if (null == customerKeywords || customerKeywords.isEmpty()) {
                return resultBean;
            }
            String userName = (String) request.getSession().getAttribute("username");
            String terminalType = (String) map.get("terminalType");
            customerKeywordService.addCustomerKeywordsFromSimpleUI(customerKeywords, terminalType, "qt", userName);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(ex.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = {"/toKeywordsFromPT/{businessType}/{terminalType}/{keyword}/{searchEngine}/{belongUser}",
        "/toKeywordsFromPT/{businessType}/{terminalType}/{keyword}/{searchEngine}", "/toKeywordsFromPT/{businessType}/{terminalType}/{keyword}/{belongUser}",
        "/toKeywordsFromPT/{businessType}/{terminalType}/{keyword}"})
    public ModelAndView toKeywords(@PathVariable(name = "businessType") String businessType, @PathVariable(name = "terminalType") String terminalType,
        @PathVariable(name = "keyword") String keyword, @PathVariable(name = "searchEngine", required = false) String searchEngine,
        @PathVariable(name = "belongUser", required = false) String belongUser) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/keyword");
        mv.addObject("businessType", businessType);
        //取名叫terminalType会与session中存在的terminalType同名，值会被覆盖成session中的值
        mv.addObject("terminalType2", terminalType);
        if (null != searchEngine) {
            mv.addObject("SearchEngine", searchEngine);
        }
        if (null != belongUser) {
            mv.addObject("belongUser", belongUser);
        }
        mv.addObject("Keyword", keyword);
        mv.addObject("notLike", "on");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toKeywordsFromPTWithPosition/{businessType}/{terminalType}/{searchEngine}/{belongUser}/{keyword}/{position}")
    public ModelAndView toKeywordsWithPosition(@PathVariable(name = "businessType") String businessType,
        @PathVariable(name = "terminalType") String terminalType, @PathVariable(name = "searchEngine") String searchEngine,
        @PathVariable(name = "belongUser") String belongUser,
        @PathVariable(name = "keyword") String keyword, @PathVariable(name = "position") Integer position) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/keyword");
        mv.addObject("businessType", businessType);
        //取名叫terminalType会与session中存在的terminalType同名，值会被覆盖成session中的值
        mv.addObject("terminalType2", terminalType);
        mv.addObject("Keyword", keyword);
        mv.addObject("ltPosition", position);
        mv.addObject("gtPosition", "1");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toKeywordsWithQZ/{businessType}/{terminalType}/{customerUuid}/{searchEngine}/{status}/{qzUuid}")
    public ModelAndView toKeywordsWithQZ(@PathVariable(name = "businessType") String businessType, @PathVariable(name = "terminalType") String terminalType,
        @PathVariable(name = "customerUuid") Long customerUuid, @PathVariable(name = "searchEngine") String searchEngine,
        @PathVariable(name = "qzUuid") String qzUuid, @PathVariable(name = "status") int status) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/customerKeyword");
        mv.addObject("businessType", businessType);
        mv.addObject("terminalType2", terminalType);
        mv.addObject("customerUuid", customerUuid);
        mv.addObject("status", status);
        mv.addObject("searchEngine", searchEngine);
        mv.addObject("qzUuid", qzUuid);
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toKeywordsWithQZForSpecial/{businessType}/{terminalType}/{customerUuid}/{searchEngine}/{specialType}/{qzUuid}")
    public ModelAndView toKeywordsWithQZForSpecial(@PathVariable(name = "businessType") String businessType, @PathVariable(name = "terminalType") String terminalType,
            @PathVariable(name = "customerUuid") Long customerUuid, @PathVariable(name = "searchEngine") String searchEngine,
            @PathVariable(name = "qzUuid") String qzUuid, @PathVariable(name = "specialType") String specialType) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/customerKeyword");
        mv.addObject("businessType", businessType);
        mv.addObject("terminalType2", terminalType);
        mv.addObject("customerUuid", customerUuid);
        mv.addObject("status", 1);
        mv.addObject("searchEngine", searchEngine);
        mv.addObject("qzUuid", qzUuid);
        if (null != specialType){
            if (specialType.equals("optimizeStopCount")){
                mv.addObject("optimizeStatus", 0);
            }else if (specialType.equals("importantCount")){
                mv.addObject("keywordEffect", "Important");
            }else if (specialType.equals("invalidDaysStopCount")){
                mv.addObject("gtInvalidDays", 4);
            }else if (specialType.equals("noEffectStopCount")){
                mv.addObject("gtNoEffectDays", 30);
            }
        }
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
    @GetMapping(value = "/toUpdateBelongCustomer")
    public ModelAndView toUpdateBelongCustomer() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/updateBelongCustomer");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/saveCustomerKeyword")
    @RequestMapping(value = "/updateKeywordCustomerUuid2", method = RequestMethod.POST)
    public ResultBean updateKeywordCustomerUuid(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<String> keywordUuids = (List<String>) requestMap.get("uuids");
            String customerUuid = (String) requestMap.get("customerUuid");
            String terminalType = (String) requestMap.get("terminalType");
            customerKeywordService.updateKeywordCustomerUuid(keywordUuids, customerUuid, terminalType);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toUpdateOptimizePlanCount")
    public ModelAndView toUpdateOptimizePlanCount() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/updateOptimizePlanCount");
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/editOptimizePlanCount")
    @RequestMapping(value = "/editOptimizePlanCount2", method = RequestMethod.POST)
    public ResultBean editOptimizePlanCount(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<String> uuids = (List<String>) requestMap.get("uuids");
            String customerUuid = (String) requestMap.get("customerUuid");
            String settingType = (String) requestMap.get("settingType");
            String optimizePlanCount = (String) requestMap.get("optimizePlanCount");
            if (customerUuid != null) {
                String terminalType = (String) requestMap.get("terminalType");
                String entryType = (String) requestMap.get("entryType");
                customerKeywordService
                    .editOptimizePlanCountByCustomerUuid(terminalType, entryType, Long.parseLong(customerUuid), Integer.parseInt(optimizePlanCount),
                        settingType);
            } else {
                customerKeywordService.editCustomerOptimizePlanCount(Integer.parseInt(optimizePlanCount), settingType, uuids);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresRoles("Operation")
    @PostMapping(value = "/clearFailReason")
    public ResultBean clearFailReason(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (CollectionUtils.isNotEmpty(keywordCriteria.getUuids()) && keywordCriteria.getCustomerUuid() == null) {
                resultBean.setCode(400);
                resultBean.setMsg(null);
                return resultBean;
            }
            String userName = (String) request.getSession().getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if (!isDepartmentManager) {
                keywordCriteria.setUserName(userName);
            }
            customerKeywordService.updateSelectFailReason(keywordCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = "/toCustomerKeywordFromQZ/{businessType}/{terminalType}/{customerUuid}/{qzUuid}")
    public ModelAndView toCustomerKeywordFromQZ(@PathVariable(name = "businessType") String businessType,
        @PathVariable(name = "terminalType") String terminalType, @PathVariable(name = "customerUuid") Long customerUuid,
        @PathVariable(name = "qzUuid") String qzUuid, @PathVariable(name = "searchEngine") String searchEngine) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("keywords/customerKeyword");
        mv.addObject("businessType", businessType);
        mv.addObject("terminalType2", terminalType);
        mv.addObject("customerUuid", customerUuid);
        mv.addObject("qzUuid", qzUuid);
        mv.addObject("searchEngine", searchEngine);
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = {"/toKeywordsFromRS/{businessType}/{terminalType}/{irc}/{group}", "/toKeywordsFromRS/{businessType}/{terminalType}/{irc}", "/toKeywordsFromUS/{businessType}/{terminalType}/{irc}/{userName}",
            "/toKeywordsFromRS/{businessType}/{terminalType}/{irc}/{group}/{gtInvalidDays}/{ltInvalidDays}/{optimizeStatus}"})
    public ModelAndView toKeywordsFromRS(@PathVariable(name = "businessType") String businessType, @PathVariable(name = "terminalType") String terminalType,
            @PathVariable(name = "group", required = false) String group, @PathVariable(name = "irc", required = false) Integer irc,
            @PathVariable(name="userName",required = false) String userName, @PathVariable(name="gtInvalidDays",required = false) String gtInvalidDays,
            @PathVariable(name="ltInvalidDays",required = false) String ltInvalidDays, @PathVariable(name="optimizeStatus",required = false) String optimizeStatus) {
        ModelAndView mv = null;
        try {
            mv = new ModelAndView();
            mv.setViewName("keywords/keyword");
            mv.addObject("businessType", businessType);
            mv.addObject("terminalType2", terminalType);
            mv.addObject("status", "1");
            if (null != group) {
                mv.addObject("groupTmp", group);
            }
            if(null != userName){
                mv.addObject("userName",userName);
            }
            if (null != irc) {
                mv.addObject("irc", irc == 0 ? "" : irc);
            }
            if (null != gtInvalidDays) {
                mv.addObject("gtInvalidDays", gtInvalidDays.equals("null") ? "" : gtInvalidDays);
            }
            if (null != ltInvalidDays) {
                mv.addObject("ltInvalidDays", ltInvalidDays.equals("null") ? "" : ltInvalidDays);
            }
            if (null != optimizeStatus) {
                mv.addObject("optimizeStatus", optimizeStatus.equals("null") ? "" : optimizeStatus);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return mv;
    }

    @RequiresPermissions("/internal/customerKeyword/searchCustomerKeywords")
    @GetMapping(value = {"/toKeywordsFromMGS/{businessType}/{terminalType}/{irc}/{machineGroup}", "/toKeywordsFromMGS/{businessType}/{terminalType}/{irc}"})
    public ModelAndView toKeywordsFromMGS(@PathVariable(name = "businessType") String businessType, @PathVariable(name = "terminalType") String terminalType,
        @PathVariable(name = "irc", required = false) Integer irc, @PathVariable(name = "machineGroup", required = false) String machineGroup) {
        ModelAndView mv = null;
        try {
            mv = new ModelAndView();
            mv.setViewName("keywords/keyword");
            mv.addObject("businessType", businessType);
            mv.addObject("terminalType2", terminalType);
            mv.addObject("status", "1");
            if (null != machineGroup) {
                mv.addObject("machineGroupTmp", machineGroup);
            }
            if (null != irc) {
                mv.addObject("irc", irc == 0 ? "" : irc);
                if (irc < 0 && machineGroup != null) {
                    Integer maxInvalidCount = customerKeywordService.getMaxInvalidCountByMachineGroup(machineGroup);
                    mv.addObject("irc", maxInvalidCount == null ? 8 : maxInvalidCount);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return mv;
    }

    @PostMapping("/getCustomerKeywordStatusCount")
    public ResultBean getCustomerKeywordStatusCount(HttpSession session) {
        ResultBean resultBean = new ResultBean(200, "获取关键词各状态下的数量成功");
        try {
            String loginName = null;
            Set<String> roles = getCurrentUser().getRoles();
            if (!roles.contains("DepartmentManager")) {
                loginName = (String) session.getAttribute("username");
            }
            resultBean.setData(customerKeywordService.getCustomerKeywordStatusCount(loginName));
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            resultBean.setMsg(e.getMessage());
            resultBean.setCode(400);
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/customerKeyword/updateCustomerKeywordStatus")
    @PostMapping(value = "/resetInvalidDays")
    public ResultBean resetInvalidDays(@RequestBody KeywordCriteria keywordCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            if (CollectionUtils.isEmpty(keywordCriteria.getUuids()) && keywordCriteria.getCustomerUuid() == null) {
                resultBean.setCode(400);
                resultBean.setMsg(null);
                return resultBean;
            }
            String userName = (String) request.getSession().getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if (!isDepartmentManager) {
                keywordCriteria.setUserName(userName);
            }
            customerKeywordService.resetInvalidDays(keywordCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
