package com.keymanager.ckadmin.controller.internal.rest;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.AlgorithmTestCriteria;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;
import com.keymanager.ckadmin.service.AlgorithmTestPlanInterface;
import com.keymanager.ckadmin.service.ConfigInterface;
import com.keymanager.util.TerminalTypeMapping;

import java.util.HashMap;
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
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by lhc on 2019/8/16.
 */
@RestController
@RequestMapping(value = "/internal/algorithmAutoTest")
public class AlgorithmAutoTestController {

    private static Logger logger = LoggerFactory.getLogger(
            AlgorithmAutoTestController.class);

    @Resource(name = "algorithmTestPlanService2")
    private AlgorithmTestPlanInterface algorithmTestPlanService2;

    @Resource(name = "configService2")
    private ConfigInterface configService2;

    @RequiresPermissions("/internal/algorithmAutoTest/searchAlgorithmTestPlans")
    @RequestMapping(value = "/toAlgorithmTestPlans", method = RequestMethod.GET)
    public ModelAndView toAlgorithmTestPlans() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("algorithmAutoTests/algorithmTestPlan");
        return mv;
    }

    // 前往添加修改页面
    @RequestMapping(value = "/toAlgorithmTestPlanAdd", method = RequestMethod.GET)
    public ModelAndView toAlgorithmTestPlanAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("algorithmAutoTests/algorithmTestPlanAdd");
        return mv;
    }

    //获得初始数据
    @RequestMapping(value = "/getAlgorithmTestPlanAddData", method = RequestMethod.GET)
    public ResultBean getAlgorithmTestPlanAddData(HttpServletRequest request) {
        Map<String, Object> mapData = new HashMap<>();
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        mapData.put("terminalType", terminalType);
        mapData.put("searchEngineMap", configService2.getSearchEngineMap(terminalType));
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        resultBean.setData(mapData);
        return resultBean;
    }

    //添加
    @RequestMapping(value = "/postAlgorithmTestPlanAddData", method = RequestMethod.POST)
    public ResultBean toAlgorithmTestPlanAddData(@Valid AlgorithmTestPlan algorithmTestPlan, HttpServletRequest request, BindingResult result) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        if (result.hasFieldErrors()) {
            resultBean.setCode(400);
            resultBean.setMsg("数据校验失败");
            return resultBean;
        }
        HttpSession session = request.getSession();
        String loginName = (String) session.getAttribute("username");
        algorithmTestPlan.setCreateBy(loginName);
        algorithmTestPlanService2.saveAlgorithmTestPlan(algorithmTestPlan);
        return resultBean;
    }

    @RequestMapping(value = "getAlgorithmTestPlans")
    public ResultBean getAlgorithmTestPlans(@RequestBody AlgorithmTestCriteria algorithmTestCriteria) {
        Page<AlgorithmTestPlan> page = new Page(algorithmTestCriteria.getPage(), algorithmTestCriteria.getLimit());
        page = algorithmTestPlanService2.searchAlgorithmTestPlans(page, algorithmTestCriteria);
        List<AlgorithmTestPlan> algorithmTestPlans = page.getRecords();
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setCount(algorithmTestPlans.size());
        resultBean.setMsg("");
        resultBean.setData(algorithmTestPlans);
        return resultBean;
    }
}

