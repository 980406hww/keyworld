package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.internal.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.entity.*;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.criteria.QZSettingSaveCustomerKeywordsCriteria;
import com.keymanager.ckadmin.service.*;
import com.keymanager.ckadmin.enums.KeywordEffectEnum;
import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.util.TerminalTypeMapping;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @Resource(name = "qzCategoryTagService2")
    private QZCategoryTagService qzCategoryTagService;


    @GetMapping(value = "/getCategoryTag")
    public List<String> getCategoryTag(){
        List<String> tagNameList = qzCategoryTagService.findTagNames(null);
        return tagNameList;
    }
    
    @GetMapping(value = "/getActiveCustomer")
    public List<Customer> getActiveCustomer() {
        CustomerCriteria customerCriteria = new CustomerCriteria();
        List<Customer> customerList = customerService.getActiveCustomerSimpleInfo(customerCriteria);
        return customerList;
    }

    @GetMapping(value = "/getUserInfo")
    public List<UserInfo> getUserInfo() {
        List<UserInfo> activeUsers = userInfoService.findActiveUsers();
        return activeUsers;
    }

    /**
     * 跳转添加或修改用户页面
     */
    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @GetMapping(value = "/toQZSetttings")
    public ModelAndView toQZSetttings() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsetting");
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
    public ResultBean getQZSettings(@RequestBody QZSettingCriteria qzSettingCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<QZSetting> page = new Page<>(qzSettingCriteria.getPage(),
                qzSettingCriteria.getLimit());
            page = qzSettingService.searchQZSetting(page, qzSettingCriteria);
            resultBean.setCode(0);
            resultBean.setMsg("success");
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    /**
     * 获取搜索引擎映射列表
     */
    @RequiresPermissions("/internal/qzsetting/searchQZSettings")
    @GetMapping("/getQZSettingSearchEngineMap")
    public ResultBean getQZSettingSearchEngineMap(QZSettingCriteria qzSettingCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            List<QZSearchEngineVO> qzSearchEngine = qzSettingService
                .searchQZSettingSearchEngineMap(qzSettingCriteria, 0);
            resultBean.setCode(0);
            resultBean.setMsg("获取搜索引擎映射列表成功");
            resultBean.setData(qzSearchEngine);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
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
            Map<String, Object> rankMap = qzSettingService
                .getQZKeywordRankInfo(uuid, terminalType, optimizeGroupName);
            resultBean.setCode(200);
            resultBean.setMsg("获取曲线信息成功");
            resultBean.setData(rankMap);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
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

    @PostMapping(value = "/echoExcludeKeyword2")
    public ResultBean echoExcludeKeyword(HttpServletRequest request,
        @RequestBody QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            CustomerExcludeKeyword customerExcludeKeyword = qzSettingService
                .echoExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria);
            resultBean.setCode(200);
            resultBean.setMsg("获取排除词成功");
            resultBean.setData(customerExcludeKeyword);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;

    }

    @RequiresPermissions("/internal/qzsetting/save")
    @PostMapping(value = "/excludeQZSettingCustomerKeywords2")
    public ResultBean excludeQZSettingCustomerKeywords2(HttpServletRequest request,
        @RequestBody QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            String entryType = (String) request.getSession().getAttribute("entryType");
            qzSettingExcludeCustomerKeywordsCriteria.setType(entryType);
            qzSettingService
                .excludeQZSettingCustomerKeywords(qzSettingExcludeCustomerKeywordsCriteria);
            resultBean.setCode(200);
            resultBean.setMsg("更新排除词成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
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

    @RequiresPermissions("/internal/qzsetting/save")
    @PostMapping(value = "/saveQZSettingCustomerKeywords2")
    public ResultBean saveQZSettingCustomerKeywords(HttpServletRequest request,
        @RequestBody QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            String userName = (String) request.getSession().getAttribute("username");
            qzSettingService
                .saveQZSettingCustomerKeywords(qzSettingSaveCustomerKeywordsCriteria, userName);
            resultBean.setCode(200);
            resultBean.setMsg("添加关键字成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
        }
        return resultBean;

    }

    @RequiresPermissions("/internal/qzsetting/delete")
    @PostMapping(value = "/delete2/{uuid}")
    public ResultBean deleteQZSetting(@PathVariable("uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            qzSettingService.deleteOne(uuid);
            resultBean.setCode(200);
            resultBean.setMsg("删除站点成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/qzsetting/deleteQZSettings")
    @PostMapping(value = "/deleteQZSettings2")
    public ResultBean deleteQZSettings(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            qzSettingService.deleteAll(uuids);
            resultBean.setCode(200);
            resultBean.setMsg("删除站点成功");
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
        }
        return resultBean;
    }

    /**
     * 获得初始用户列表
     * @param request
     * @return
     */
    @GetMapping(value = "/getSaveQZSettingsMsg")
    public ResultBean getSaveQZSettingsMsg(HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        CustomerCriteria customerCriteria = new CustomerCriteria();
        String entryType = (String) request.getSession().getAttribute("entryType");
        customerCriteria.setEntryType(entryType);
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        Map<String, Object> map = new HashMap<>(2);
        try {
            map.put("customers", customerService.getActiveCustomerSimpleInfo(customerCriteria));
            map.put("search", configService.getSearchEngineMap(terminalType));
        } catch (Exception e) {
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
            return resultBean;
        }
        resultBean.setData(map);
        return resultBean;
    }
    
    @RequiresPermissions("/internal/qzsetting/save")
    @PostMapping(value = "/saveQZSetting")
    public ResultBean saveQZSetting(@RequestBody QZSetting qzSetting, HttpSession session) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            if (qzSetting.getUuid() == null) {
                Set<String> roles = getCurrentUser().getRoles();
                if (roles.contains("DepartmentManager")) {
                    qzSetting.setStatus(1);
                } else {
                    qzSetting.setStatus(2);
                }
            }
            String userName = (String) session.getAttribute("username");
            qzSettingService.saveQZSetting(qzSetting, userName);
            return resultBean;
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

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
    @GetMapping(value = "/getKyewordEffect")
    public ResultBean getKyewordEffect() {
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
     * @param requestMap
     * @return
     */
    @PostMapping(value = "/batchUpdateQZSettingUpdateStatus")
    public ResultBean batchUpdateQZSettingUpdateStatus(@RequestBody Map<String, String> requestMap) {
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
     * @param requestMap
     * @return
     */
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
}
