package com.keymanager.ckadmin.controller.internal.rest;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.RequsetBean;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;
import com.keymanager.ckadmin.service.AlgorithmTestPlanInterface;
import java.util.List;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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


    @RequiresPermissions("/internal/algorithmAutoTest/searchAlgorithmTestPlans")
    @RequestMapping(value = "/toAlgorithmTestPlans", method = RequestMethod.GET)
    public ModelAndView toAlgorithmTestPlans() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("algorithmAutoTests/algorithmTestPlan");
        return mv;
    }

    @RequestMapping(value = "getAlgorithmTestPlans")
    public String getAlgorithmTestPlans(@RequestBody RequsetBean requsetBean){
        Page page = new Page(requsetBean.getPage(),requsetBean.getLimit());
        //List<AlgorithmTestPlan> algorithmTestPlans = algorithmTestPlanService2.selectList(null);
        page = algorithmTestPlanService2.selectPage(page);
        List<AlgorithmTestPlan> algorithmTestPlans = page.getRecords();
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        resultBean.setCount(algorithmTestPlans.size());
        resultBean.setMsg("");
        resultBean.setData(algorithmTestPlans);
        String s = JSON.toJSONString(resultBean);
        return s;
    }

}

