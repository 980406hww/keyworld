package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.entity.SysLog;
import com.keymanager.ckadmin.service.SysLogService;
import com.keymanager.ckadmin.service.UserInfoService;
import java.util.Map;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/internal/systemLog")
public class SystemLogController {

    private static Logger logger = LoggerFactory.getLogger(SuppliersController.class);

    @Resource(name = "sysLogService2")
    private SysLogService sysLogService;

    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;

    @GetMapping("/toSystemLog")
    public ModelAndView toSystemLog() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("systemLog/systemLog");
        return mv;
    }

    @PostMapping("/getLoginLogData")
    public ResultBean getLoginLogData(@RequestBody Map<String, Object> criteria) {
        ResultBean resultBean = new ResultBean(0, "success");
        try {
            Page<SysLog> page = sysLogService.selectLoginLog(criteria);
            resultBean.setData(page.getRecords());
            resultBean.setCount(page.getTotal());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @GetMapping("/getUserInfoList")
    public ResultBean getUserInfoList() {
        ResultBean resultBean = new ResultBean(200, "success");
        try {
            resultBean.setData(userInfoService.selectUserNames());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
