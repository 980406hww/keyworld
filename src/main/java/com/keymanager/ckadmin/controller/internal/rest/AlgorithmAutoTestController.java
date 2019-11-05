package com.keymanager.ckadmin.controller.internal.rest;


import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.AlgorithmTestCriteria;
import com.keymanager.ckadmin.criteria.AlgorithmTestTaskCriteria;
import com.keymanager.ckadmin.entity.AlgorithmTestDataStatistics;
import com.keymanager.ckadmin.entity.AlgorithmTestPlan;
import com.keymanager.ckadmin.entity.AlgorithmTestTask;
import com.keymanager.ckadmin.service.AlgorithmTestPlanService;
import com.keymanager.ckadmin.service.AlgorithmTestResultStatisticsService;
import com.keymanager.ckadmin.service.AlgorithmTestTaskService;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.vo.AlgorithmTestDataStatisticsVo;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    private AlgorithmTestPlanService algorithmTestPlanService;

    @Resource(name = "configService2")
    private ConfigService configService;

    @Resource(name = "algorithmTestResultStatisticsService2")
    private AlgorithmTestResultStatisticsService algorithmTestResultStatisticsService;

    @Resource(name = "algorithmTestTaskService2")
    private AlgorithmTestTaskService algorithmTestTaskService;

    @RequiresPermissions("/internal/algorithmAutoTest/searchAlgorithmTestPlans")
    @RequestMapping(value = "/toAlgorithmTestPlans", method = RequestMethod.GET)
    public ModelAndView toAlgorithmTestPlans() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("algorithmAutoTests/algorithmTestPlan");
        return mv;
    }

    /**
     * 前往添加修改页面
     * @return
     */
    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "/toAlgorithmTestPlanAdd", method = RequestMethod.GET)
    public ModelAndView toAlgorithmTestPlanAdd() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("algorithmAutoTests/algorithmTestPlanAdd");
        return mv;
    }

    /**
     * 获得初始数据
     * @param request
     * @return
     */
    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "/getAlgorithmTestPlanAddData", method = RequestMethod.GET)
    public ResultBean getAlgorithmTestPlanAddData(HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        try{
            Map<String, Object> mapData = new HashMap<>();
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            mapData.put("terminalType", terminalType);
            mapData.put("searchEngineMap", configService.getSearchEngineMap(terminalType));
            resultBean.setCode(200);
            resultBean.setData(mapData);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    /**
     * 添加
     * @param algorithmTestPlan
     * @param request
     * @param result
     * @return
     */
    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "/postAlgorithmTestPlanAddData", method = RequestMethod.POST)
    public ResultBean toAlgorithmTestPlanAddData(@Valid @RequestBody AlgorithmTestPlan algorithmTestPlan, HttpServletRequest request, BindingResult result) {
        ResultBean resultBean = new ResultBean(200,"success");
        if (result.hasFieldErrors()) {
            resultBean.setCode(400);
            resultBean.setMsg("数据校验失败");
            return resultBean;
        }
        try{
            HttpSession session = request.getSession();
            String loginName = (String) session.getAttribute("username");
            algorithmTestPlan.setCreateBy(loginName);
            algorithmTestPlanService.saveAlgorithmTestPlan(algorithmTestPlan);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/searchAlgorithmTestPlans")
    @RequestMapping(value = "getAlgorithmTestPlans")
    public ResultBean getAlgorithmTestPlans(@RequestBody AlgorithmTestCriteria algorithmTestCriteria) {
        ResultBean resultBean = new ResultBean();
        try{
            Page<AlgorithmTestPlan> page = new Page<>(algorithmTestCriteria.getPage(), algorithmTestCriteria.getLimit());
            page = algorithmTestPlanService.searchAlgorithmTestPlans(page, algorithmTestCriteria);
            List<AlgorithmTestPlan> algorithmTestPlans = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(algorithmTestPlans.size());
            resultBean.setMsg("");
            resultBean.setData(algorithmTestPlans);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "getAlgorithmTestPlan2/{uuid}")
    public ResultBean getAlgorithmTestPlan2(@PathVariable(name = "uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try{
            AlgorithmTestPlan algorithmTestPlan = algorithmTestPlanService.getAlgorithmTestPlanByUuid(uuid);
            resultBean.setCode(200);
            resultBean.setMsg("success");
            resultBean.setData(algorithmTestPlan);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }


    @RequiresPermissions("/internal/algorithmAutoTest/deleteAlgorithmTestPlan")
    @RequestMapping(value = "/deleteAlgorithmTestPlan2/{uuid}", method = RequestMethod.POST)
    public ResultBean deleteAlgorithmTestPlan(@PathVariable(name = "uuid") Long uuid) {
        ResultBean resultBean = new ResultBean();
        try {
            algorithmTestPlanService.deletePlanAndTaskByPlanId(uuid);
            resultBean.setCode(200);
            resultBean.setMsg("success");
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/deleteAlgorithmTestPlan")
    @RequestMapping(value = "/deleteAlgorithmTestPlans2", method = RequestMethod.POST)
    public ResultBean deleteAlgorithmTestPlans(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            algorithmTestPlanService.deletePlanAndTaskByPlanIds((List<Long>) requestMap.get("uuids"));
            resultBean.setCode(200);
            resultBean.setMsg("success");
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/changeAlgorithmTestPlanStatus")
    @RequestMapping(value = "/changeAlgorithmTestPlanStatus2", method = RequestMethod.POST)
    public ResultBean changeAlgorithmTestPlanStatus(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            Integer uuid =(Integer) requestMap.get("uuid");
            Integer status = (Integer)requestMap.get("status");
            algorithmTestPlanService.changeAlgorithmTestPlanStatus(uuid,status);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "/updateAlgorithmTestPlansStatus2", method = RequestMethod.POST)
    public ResultBean updateAlgorithmTestPlansStatus(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            List<Integer> uuids = (List<Integer>) requestMap.get("uuids");
            Integer status = (Integer)requestMap.get("status");
            algorithmTestPlanService.updateAlgorithmTestPlansStatus(uuids, status);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "/toShowTestPlanDataStatistics", method = RequestMethod.GET)
    public ModelAndView toShowTestPlanDataStatistics() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("algorithmAutoTests/testPlanDataStatistics");
        return mv;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/showTestDataStatistics")
    @RequestMapping(value = "/getTestDataStatisticsByPlanUuid", method = RequestMethod.POST)
    public ResultBean getTestDataByPlanUuid(@RequestBody AlgorithmTestTaskCriteria algorithmTestTaskCriteria) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            Page<AlgorithmTestDataStatisticsVo> page = new Page<>(algorithmTestTaskCriteria.getPage(), algorithmTestTaskCriteria.getLimit());
            page = algorithmTestResultStatisticsService.selectAlgorithmTestHistoryByAlgorithmTestPlanUuid(page, algorithmTestTaskCriteria.getAlgorithmTestPlanUuid());
            List<AlgorithmTestDataStatisticsVo> algorithmTestDataStatistics = page.getRecords();

            resultBean.setCode(0);
            resultBean.setCount(algorithmTestDataStatistics.size());
            resultBean.setMsg("success");
            resultBean.setData(algorithmTestDataStatistics);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/saveAlgorithmTestPlan")
    @RequestMapping(value = "/toShowTaskData", method = RequestMethod.GET)
    public ModelAndView toShowTaskData() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("algorithmAutoTests/taskData");
        return mv;
    }

    @RequiresPermissions("/internal/algorithmAutoTest/showTestDataStatistics")
    @RequestMapping(value = "/getTaskDataByPlanUuid", method = RequestMethod.POST)
    public ResultBean getTaskDataByPlanUuid(@RequestBody AlgorithmTestTaskCriteria algorithmTestTaskCriteria) {
        ResultBean resultBean = new ResultBean(200,"success");
        try {
            Page<AlgorithmTestTask> page = new Page<>(algorithmTestTaskCriteria.getPage(), algorithmTestTaskCriteria.getLimit());
            page = algorithmTestTaskService.selectAlgorithmTestTasksByAlgorithmTestPlanUuid(page, algorithmTestTaskCriteria.getAlgorithmTestPlanUuid());
            List<AlgorithmTestTask> algorithmTestTasks = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(algorithmTestTasks.size());
            resultBean.setMsg("success");
            resultBean.setData(algorithmTestTasks);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

}

