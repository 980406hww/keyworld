package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.criteria.QZSettingSearchCriteria;
import com.keymanager.ckadmin.entity.*;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.criteria.QZSettingSaveCustomerKeywordsCriteria;
import com.keymanager.ckadmin.service.*;
import com.keymanager.ckadmin.enums.KeywordEffectEnum;
import com.keymanager.ckadmin.vo.QZChargeRuleStandardInfoVO;
import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.vo.QZSettingCountVO;
import com.keymanager.util.TerminalTypeMapping;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @ClassName QZSettingController
 * @Description 新后台全站入口
 * @Author lhc
 * @Date 2019/9/6 17:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/internal/qzsetting")
public class QZSettingController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(QZSettingController.class);

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Resource(name = "qzCategoryTagService2")
    private QZCategoryTagService qzCategoryTagService;

    @Resource(name = "qzChargeRuleService2")
    private QZChargeRuleService qzChargeRuleService;

    @Resource(name = "qzChargeMonService2")
    private QzChargeMonService qzChargeMonService;

    /**
     * 获取网站分类标签
     */
    @GetMapping(value = "/getCategoryTag")
    public ResultBean getCategoryTag(HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            String userName = null;
            if (!getCurrentUser().getRoles().contains("DepartmentManager")) {
                userName = (String) request.getSession().getAttribute("username");
            }
            resultBean.setData(qzCategoryTagService.findTagNameByUserName(userName));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 获取客户列表
     */
    @GetMapping(value = "/getActiveCustomer")
    public ResultBean getActiveCustomer(HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            CustomerCriteria customerCriteria = new CustomerCriteria();
            if (!getCurrentUser().getRoles().contains("DepartmentManager")) {
                customerCriteria.setLoginName((String) request.getSession().getAttribute("username"));
            }
            resultBean.setData(customerService.getActiveCustomerSimpleInfo(customerCriteria));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 跳转添加或修改用户页面
     */
    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @GetMapping(value = "/toQZSetttings")
    public ModelAndView toQZSetttings() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsetting");
        int isSEOSales = 0;
        if (getCurrentUser().getRoles().contains("SEOSales") || getCurrentUser().getRoles().contains("DepartmentManager")) {
            isSEOSales = 1;
        }
        mv.addObject("isSEOSales", isSEOSales);
        return mv;
    }

    /**
     * 跳转整站
     */
    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @GetMapping(value = "/toQzSetting/{domain}/{terminal}/{customerUuidTmp}")
    public ModelAndView toQzSetting(@PathVariable String domain, @PathVariable String terminal, @PathVariable String customerUuidTmp) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsetting");
        try {
            domain = URLDecoder.decode(domain, "UTF-8");
            mv.addObject("domain", domain);
            mv.addObject("terminal", terminal);
            mv.addObject("customerUuidTmp", customerUuidTmp);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        int isSEOSales = 0;
        if (getCurrentUser().getRoles().contains("SEOSales")) {
            isSEOSales = 1;
        }
        mv.addObject("isSEOSales", isSEOSales);
        return mv;
    }

    /**
     * 跳转添加或修改全站页面
     */
    @RequiresPermissions("/internal/qzsetting/save")
    @GetMapping(value = "/toQZSettingAdd")
    public ModelAndView toQZSettingAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsettingAdd");
        return mv;
    }

    /**
     * 跳转添加或修改全站收费页面
     */
    @GetMapping(value = "/toQZSettingCharge")
    public ModelAndView toQZSettingCharge() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsettingCharge");
        return mv;
    }

    /**
     * 获取全站数据
     */
    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @PostMapping("/getQZSettings")
    public ResultBean getQZSettings(@RequestBody QZSettingSearchCriteria qzSettingCriteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<QZSetting> page = new Page<>(qzSettingCriteria.getPage(),
                qzSettingCriteria.getLimit());
            if (!getCurrentUser().getRoles().contains("DepartmentManager")) {
                String loginName = (String) request.getSession().getAttribute("username");
                qzSettingCriteria.setLoginName(loginName);
            }
            request.getSession().getAttribute("username");
            page = qzSettingService.searchQZSetting(page, qzSettingCriteria);
            resultBean.setCode(0);
            resultBean.setMsg("success");
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    /**
     * 获取搜索引擎映射列表
     */
    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @GetMapping("/getQZSettingSearchEngineMap/{terminal}")
    public ResultBean getQZSettingSearchEngineMap(@PathVariable String terminal) {
        ResultBean resultBean = new ResultBean();
        try {
            List<QZSearchEngineVO> qzSearchEngine = qzSettingService.searchQZSettingSearchEngineMap(new QZSettingCriteria(), 0);
            List<QZSearchEngineVO> data = new ArrayList<>();
            for (QZSearchEngineVO qz : qzSearchEngine) {
                if (terminal.equals(qz.getTerminalType())) {
                    data.add(qz);
                }
            }
            resultBean.setCode(200);
            resultBean.setMsg("获取搜索引擎映射列表成功");
            resultBean.setData(data);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    /**
     * 获取曲线信息
     */
    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @PostMapping("/getQZKeywordRankInfo")
    public ResultBean getQZKeywordRankInfo(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            long uuid = Long.parseLong(requestMap.get("uuid").toString());
            String terminalType = (String) requestMap.get("terminalType");
            String optimizeGroupName = (String) requestMap.get("optimizeGroupName");
            Long customerUuid = Long.parseLong(requestMap.get("customerUuid").toString());
            Map<String, Object> rankMap = qzSettingService.getQZKeywordRankInfo(uuid, terminalType, optimizeGroupName, customerUuid);
            resultBean.setCode(200);
            resultBean.setMsg("获取曲线信息成功");
            resultBean.setData(rankMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    /**
     * 跳转添加或修改排除关键字页面
     */
    @GetMapping(value = "/toExcludeCustomerKeyword")
    public ModelAndView toExcludeCustomerKeyword() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/excludeCustomerKeyword");
        return mv;
    }


    /**
     * 排除词回显
     */
    @PostMapping(value = "/echoExcludeKeyword2")
    public ResultBean echoExcludeKeyword(HttpServletRequest request,
        @RequestBody QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            CustomerExcludeKeyword customerExcludeKeyword = qzSettingService.echoExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria);
            resultBean.setCode(200);
            resultBean.setMsg("获取排除词成功");
            resultBean.setData(customerExcludeKeyword);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;

    }

    /**
     * 指定排除词
     */
    @RequiresPermissions("/internal/qzsetting/save")
    @PostMapping(value = "/excludeQZSettingCustomerKeywords2")
    public ResultBean excludeQZSettingCustomerKeywords2(HttpServletRequest request,
        @RequestBody QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            String entryType = (String) request.getSession().getAttribute("entryType");
            qzSettingExcludeCustomerKeywordsCriteria.setType(entryType);
            qzSettingService.excludeQZSettingCustomerKeywords(qzSettingExcludeCustomerKeywordsCriteria);
            resultBean.setCode(200);
            resultBean.setMsg("更新排除词成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;

    }

    /**
     * 跳转添加或修改指定关键字页面
     */
    @RequiresPermissions("/internal/qzsetting/save")
    @GetMapping(value = "/toAddCustomerKeyword")
    public ModelAndView toAddCustomerKeyword() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/addCustomerKeyword");
        return mv;
    }

    /**
     * 添加关键词
     */
    @RequiresPermissions("/internal/qzsetting/save")
    @PostMapping(value = "/saveQZSettingCustomerKeywords2")
    public ResultBean saveQZSettingCustomerKeywords(HttpServletRequest request,
        @RequestBody QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            String userName = (String) request.getSession().getAttribute("username");
            qzSettingService.saveQZSettingCustomerKeywords(qzSettingSaveCustomerKeywordsCriteria, userName);
            resultBean.setCode(200);
            resultBean.setMsg("添加关键字成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;

    }

    /**
     * 删除站点信息 —— 单个
     */
    @RequiresPermissions("/internal/qzsetting/delete")
    @PostMapping(value = "/delete2/{uuid}")
    public ResultBean deleteQZSetting(@PathVariable("uuid") Long uuid, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        try {
            // 日志监控
            saveQzChargeMon(uuid, (String) session.getAttribute("name"));
            qzSettingService.deleteOne(uuid);
            resultBean.setCode(200);
            resultBean.setMsg("删除站点成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 删除站点信息 —— 批量
     */
    @RequiresPermissions("/internal/qzsetting/deleteQZSettings")
    @PostMapping(value = "/deleteQZSettings2")
    public ResultBean deleteQZSettings(@RequestBody Map<String, Object> requestMap, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            // 日志监控
            updQzStatusMonLows(uuids, (String) session.getAttribute("name"));
            qzSettingService.deleteAll(uuids);
            resultBean.setCode(200);
            resultBean.setMsg("删除站点成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 获得初始用户列表
     */
    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @GetMapping(value = "/getSaveQZSettingsMsg")
    public ResultBean getSaveQZSettingsMsg(HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        CustomerCriteria customerCriteria = new CustomerCriteria();
        String entryType = (String) request.getSession().getAttribute("entryType");
        customerCriteria.setEntryType(entryType);
        if (!getCurrentUser().getRoles().contains("DepartmentManager")) {
            String loginName = (String) request.getSession().getAttribute("username");
            customerCriteria.setLoginName(loginName);
        }
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        Map<String, Object> map = new HashMap<>(2);
        try {
            map.put("customers", customerService.getActiveCustomerSimpleInfo(customerCriteria));
            map.put("search", configService.getSearchEngineMap(terminalType));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        resultBean.setData(map);
        return resultBean;
    }

    /**
     * 站点信息  修改 || 保存
     */
    @RequiresPermissions("/internal/qzsetting/save")
    @PostMapping(value = "/saveQZSetting")
    public ResultBean saveQZSetting(@RequestBody QZSetting qzSetting, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        String userName = (String) session.getAttribute("username");
        try {
            if (qzSetting.getUuid() == null) {
                if (getCurrentUser().getRoles().contains("DepartmentManager")) {
                    qzSetting.setStatus(1);
                } else {
                    qzSetting.setStatus(2);
                }
            }
            qzSettingService.saveQZSetting(qzSetting, userName);
            saveQzChargeMon(qzSetting, (String) session.getAttribute("name"));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 站点信息回显
     */
    @GetMapping(value = "/getQZSettingsMsg/{uuid}")
    public ResultBean getQZSettingsMsg(@PathVariable Long uuid) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            resultBean.setData(qzSettingService.getQZSetting(uuid));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 获取关键字作用类别
     */
    @GetMapping(value = "/getKeywordEffect")
    public ResultBean getKeywordEffect() {
        ResultBean resultBean = new ResultBean(200, "查询成功");
        try {
            resultBean.setData(KeywordEffectEnum.toList());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 马上更新 - 批量
     */
    @RequiresPermissions("/internal/qzsetting/updateImmediately")
    @PostMapping(value = "/batchUpdateQZSettingUpdateStatus")
    public ResultBean batchUpdateQZSettingUpdateStatus(
        @RequestBody Map<String, String> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            String uuids = requestMap.get("uuids");
            qzSettingService.batchUpdateQZSettingUpdateStatus(uuids);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 暂停续费 || 下架整站 - 批量
     */
    @RequiresPermissions("/internal/qzsetting/updateRenewalStatus")
    @PostMapping(value = "/batchUpdateRenewalStatus")
    public ResultBean batchUpdateRenewalStatus(@RequestBody Map<String, String> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            String uuids = requestMap.get("uuids");
            int renewalStatus = Integer.parseInt(requestMap.get("renewalStatus"));
            qzSettingService.batchUpdateRenewalStatus(uuids, renewalStatus);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 修改站点状态 - 单个
     */
    @PostMapping(value = "/updRenewalStatus")
    public ResultBean updRenewalStatus(@RequestBody Map<String, Integer> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            long uuid = requestMap.get("uuid");
            Integer renewalStatus = requestMap.get("renewalStatus");
            qzSettingService.updRenewalStatus(uuid, renewalStatus);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    /**
     * 修改站点关联的标签
     */
    @RequiresPermissions("/internal/qzsetting/save")
    @PostMapping(value = "/updQzCategoryTags")
    public ResultBean updateQzCategoryTags(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            String userName = (String) request.getSession().getAttribute("username");
            List<String> uuids = (List<String>) map.get("uuids");
            List<QZCategoryTag> targetQZCategoryTags = new ObjectMapper()
                .convertValue(map.get("targetQZCategoryTags"), new TypeReference<List<QZCategoryTag>>() {
                });
            qzSettingService.updateQzCategoryTags(uuids, targetQZCategoryTags, userName);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            resultBean.setMsg(ex.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @GetMapping(value = "/toQZSettingsWithCustomerUuid/{customerUuid}/{terminalType}")
    public ModelAndView toQZSettingsWithCustomerUuid(@PathVariable(name = "customerUuid") Long customerUuid, @PathVariable(name = "terminalType") String terminalType) {
        ModelAndView mv = new ModelAndView();
        int isSEOSales = 0;
        if (getCurrentUser().getRoles().contains("SEOSales") || getCurrentUser().getRoles().contains("DepartmentManager")) {
            isSEOSales = 1;
        }
        mv.addObject("isSEOSales", isSEOSales);
        mv.addObject("customerUuidTmp", customerUuid);
        mv.addObject("terminalTypeTmp", terminalType);
        mv.addObject("searchEngineTmp", "All");
        mv.setViewName("qzsettings/qzsetting");
        return mv;
    }

    /**
     * 根据用户id获取全站统计信息
     */
    @GetMapping("/getQZSettingsCount/{customerUuid}")
    public ResultBean getOperationCombines(@PathVariable(name = "customerUuid") Long customerUuid) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            resultBean.setData(qzSettingService.getQZSettingsCountByCustomerUuid(customerUuid));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        return resultBean;
    }

    @GetMapping(value = "/toUpdateQZSettingFrom")
    public ModelAndView toUpdateQZSettingFrom() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/updateQZSettingFrom");
        return mv;
    }

    @PostMapping(value = "/updateQZSettingFrom")
    public ResultBean updateQZSettingFrom(@RequestBody Map<String, Object> map) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            Long customerUuid = Long.parseLong((String) map.get("customerUuid"));
            List<Long> uuids = (List<Long>) map.get("uuids");
            qzSettingService.updateQZSettingFrom(customerUuid, uuids);
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            resultBean.setMsg(ex.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @RequestMapping(value = "/getChargeRuleMsg", method = RequestMethod.POST)
    public ResultBean getChargeRules(@RequestBody QZSettingSearchCriteria criteria) {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            List<QZChargeRuleStandardInfoVO> data = qzChargeRuleService.searchChargeRules(criteria);
            if (null == data || data.isEmpty()) {
                resultBean.setCode(300);
            } else {
                resultBean.setData(data);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            resultBean.setMsg(ex.getMessage());
            resultBean.setCode(400);
        }
        return resultBean;
    }

    private void saveQzChargeMon(QZSetting qzSetting, String userName) {
        try {
            if (qzSetting.getRenewalStatus() == 4) {
                return;
            }
            Customer customer = customerService.selectById(qzSetting.getCustomerUuid());
            String terminal = "";
            if (null == qzSetting.getPcGroup() || "".equals(qzSetting.getPcGroup())) {
                if (null != qzSetting.getPhoneGroup() && !"".equals(qzSetting.getPhoneGroup())) {
                    terminal = "Phone";
                }
            } else {
                terminal = "PC";
                if (null != qzSetting.getPhoneGroup() && !"".equals(qzSetting.getPhoneGroup())) {
                    terminal += ",Phone";
                }
            }
            QzChargeMon qzChargeMon = new QzChargeMon();
            qzChargeMon.setOperationType(2); // 2:新增状态
            qzChargeMon.setQzDomain(qzSetting.getDomain());
            qzChargeMon.setQzCustomer(customer.getContactPerson());
            qzChargeMon.setSearchEngine(qzSetting.getSearchEngine());
            qzChargeMon.setTerminalType(terminal);
            qzChargeMon.setOperationUser(userName);
            qzChargeMon.setQzzSettingUuid(qzSetting.getUuid());
            qzChargeMonService.insert(qzChargeMon);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void saveQzChargeMon(Long uuid, String userName) {
        try {
            QZSetting qzSetting = qzSettingService.selectById(uuid);
            if (qzSetting.getRenewalStatus() == 4) {
                return;
            }

            Customer customer = customerService.selectById(qzSetting.getCustomerUuid());
            String terminal = "";
            if (null == qzSetting.getPcGroup() || "".equals(qzSetting.getPcGroup())) {
                if (null != qzSetting.getPhoneGroup() && !"".equals(qzSetting.getPhoneGroup())) {
                    terminal = "Phone";
                }
            } else {
                terminal = "PC";
                if (null != qzSetting.getPhoneGroup() && !"".equals(qzSetting.getPhoneGroup())) {
                    terminal += ",Phone";
                }
            }
            QzChargeMon qzChargeMon = new QzChargeMon();
            qzChargeMon.setOperationType(4); // 4:删除状态
            qzChargeMon.setQzDomain(qzSetting.getDomain());
            qzChargeMon.setQzCustomer(customer.getContactPerson());
            qzChargeMon.setSearchEngine(qzSetting.getSearchEngine());
            qzChargeMon.setTerminalType(terminal);
            qzChargeMon.setOperationUser(userName);
            qzChargeMon.setQzzSettingUuid(uuid);
            qzChargeMonService.insert(qzChargeMon);
            QzChargeMon qzStatus = new QzChargeMon();
            qzStatus.setIsDel(0);
            Wrapper<QzChargeMon> wrapper = new EntityWrapper<>();
            wrapper.eq("fQzSettingUuid", qzChargeMon.getQzzSettingUuid());
            qzChargeMonService.update(qzStatus, wrapper);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private void updQzStatusMonLows(List<Integer> uuids, String userName) {
        try {
            if (null == uuids || uuids.isEmpty()) {
                return;
            }
            List<QZSetting> qzSettings = qzSettingService.selectByUuids(uuids);
            for (QZSetting qzSetting : qzSettings) {
                if (qzSetting.getRenewalStatus() == 4) {
                    continue;
                }
                String terminal = "";
                if (null == qzSetting.getPcGroup() || "".equals(qzSetting.getPcGroup())) {
                    if (null != qzSetting.getPhoneGroup() && !"".equals(qzSetting.getPhoneGroup())) {
                        terminal += "Phone";
                    }
                } else {
                    terminal += "PC";
                    if (null != qzSetting.getPhoneGroup() && !"".equals(qzSetting.getPhoneGroup())) {
                        terminal += ",Phone";
                    }
                }
                QzChargeMon qzChargeMon = new QzChargeMon();
                qzChargeMon.setOperationType(4); // 4:删除状态
                qzChargeMon.setQzDomain(qzSetting.getDomain());
                qzChargeMon.setQzCustomer(qzSetting.getContactPerson());
                qzChargeMon.setSearchEngine(qzSetting.getSearchEngine());
                qzChargeMon.setTerminalType(terminal);
                qzChargeMon.setOperationUser(userName);
                qzChargeMon.setQzzSettingUuid(qzSetting.getUuid());
                qzChargeMonService.insert(qzChargeMon);
            }
            QzChargeMon qzStatus = new QzChargeMon();
            qzStatus.setIsDel(0);
            Wrapper<QzChargeMon> wrapper = new EntityWrapper<>();
            wrapper.in("fQzSettingUuid", uuids);
            qzChargeMonService.update(qzStatus, wrapper);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @PostMapping("/getQzSettingRenewalStatusCount")
    public ResultBean getQzSettingRenewalStatusCount() {
        ResultBean resultBean = new ResultBean(200, "获取站点各续费状态下的数量成功");
        try {
            resultBean.setData(qzSettingService.getQzSettingRenewalStatusCount());
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
