package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.service.MachineInfoService;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/machineinfo")
public class MachineInfoController {

    private static Logger logger = LoggerFactory.getLogger(MachineInfoController.class);

    @Resource(name = "machineInfoService2")
    private MachineInfoService machineInfoService;

    @RequiresPermissions("/internal/machineInfo/machineInfoStat")
    @RequestMapping(value = "/toMachineInfo", method = RequestMethod.GET)
    public ModelAndView toMachineInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineInfo/machineInfo");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/machineInfoStat")
    @RequestMapping(value = "/getMachineInfos", method = RequestMethod.GET)
    public ResultBean getMachineInfos() {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            resultBean.setData(machineInfoService.getMachineInfos());
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/machineInfoStat")
    @RequestMapping(value = "/getMachineInfoBody", method = RequestMethod.POST)
    public ResultBean getMachineInfoBody(@RequestBody Map<String, String> map) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        try {
            resultBean.setData(machineInfoService.getMachineInfoBody(map.get("name")));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
