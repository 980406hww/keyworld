package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.CustomerCriteria;
import com.keymanager.ckadmin.criteria.QZSettingCriteria;
import com.keymanager.ckadmin.entity.Customer;
import com.keymanager.ckadmin.criteria.QZSettingExcludeCustomerKeywordsCriteria;
import com.keymanager.ckadmin.entity.CustomerExcludeKeyword;
import com.keymanager.ckadmin.entity.QZSetting;
import com.keymanager.ckadmin.entity.UserInfo;
import com.keymanager.ckadmin.service.CustomerService;
import com.keymanager.ckadmin.service.QZSettingService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.vo.QZSearchEngineVO;
import com.keymanager.monitoring.criteria.QZSettingSaveCustomerKeywordsCriteria;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName QZSettingController
 * @Description 新后台全站入口
 * @Author lhc
 * @Date 2019/9/6 17:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/internal/qzsetting")
public class QZSettingController {

    private static Logger logger = LoggerFactory.getLogger(QZSettingController.class);

    @Resource(name = "qzSettingService2")
    private QZSettingService qzSettingService;

    @Resource(name = "customerService2")
    private CustomerService customerService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    //跳转添加或修改用户页面
//    @RequiresPermissions("/internal/productKeyword/searchProductKeywords")

    @GetMapping(value = "/getActiveCustomer")
    public List<Customer> getActiveCustomer(){
        CustomerCriteria customerCriteria = new CustomerCriteria();
        List<Customer> customerList = customerService.getActiveCustomerSimpleInfo(customerCriteria);
        return customerList;
    }

    @GetMapping(value = "/getUserInfo")
    public List<UserInfo> getUserInfo(){
        List<UserInfo> activeUsers = userInfoService.findActiveUsers();
        return activeUsers;
    }


    /**
     * 跳转添加或修改用户页面
     */
    @GetMapping(value = "/toQZSetttings")
    public ModelAndView toQZSetttings() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/qzsetting");
        return mv;
    }

    /**
     * 跳转添加或修改全站页面
     */
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
    public ResultBean echoExcludeKeyword(HttpServletRequest request, @RequestBody QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            CustomerExcludeKeyword customerExcludeKeyword = qzSettingService.echoExcludeKeyword(qzSettingExcludeCustomerKeywordsCriteria);
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

    @PostMapping(value = "/excludeQZSettingCustomerKeywords2")
    public ResultBean excludeQZSettingCustomerKeywords2(HttpServletRequest request, @RequestBody QZSettingExcludeCustomerKeywordsCriteria qzSettingExcludeCustomerKeywordsCriteria) {
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
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;

    }

    /**
     * 跳转添加或修改指定关键字页面
     */
    @GetMapping(value = "/toAppointCustomerKeyword")
    public ModelAndView toAppointCustomerKeyword() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("qzsettings/appointCustomerKeyword");
        return mv;
    }

    @RequestMapping(value = "/saveQZSettingCustomerKeywords2", method = RequestMethod.POST)
    public ResultBean saveQZSettingCustomerKeywords(HttpServletRequest request, @RequestBody QZSettingSaveCustomerKeywordsCriteria qzSettingSaveCustomerKeywordsCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            String userName = (String) request.getSession().getAttribute("username");
            qzSettingService.saveQZSettingCustomerKeywords(qzSettingSaveCustomerKeywordsCriteria, userName);
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


}