package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import com.keymanager.ckadmin.service.ConfigService;
import com.keymanager.ckadmin.service.MachineGroupWorkInfoService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/machinegroupstatistics")
public class MachineGroupWorkInfoController {

    private static Logger logger = LoggerFactory.getLogger(MachineGroupWorkInfoController.class);

    @Resource(name = "configService2")
    private ConfigService configService;
    @Resource(name = "userRoleService2")
    private UserRoleService userRoleService;
    @Resource(name = "userInfoService2")
    private UserInfoService userInfoService;
    @Resource(name = "machineGroupWorkInfoService2")
    private MachineGroupWorkInfoService machineGroupWorkInfoService;


    @RequiresPermissions("/internal/machinegroupstatistics/toMachineGroupStatistics")
    @RequestMapping(value = "/toMachineGroupStatistics", method = RequestMethod.GET)
    public ModelAndView toMachineGroupStatistics() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineGroupStatistics/machineGroupStatistics");
        return mv;
    }

    @RequiresPermissions("/internal/machinegroupworkinfo/searchMachineGroupWorkInfos")
    @RequestMapping(value = "/searchMachineGroupWorkInfos", method = RequestMethod.POST)
    public ResultBean searchMachineGroupWorkInfosPost(@RequestBody MachineGroupWorkInfoCriteria criteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(0);
        try {
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if (!isDepartmentManager) {
                criteria.setUserName(userName);
            }
            List<MachineGroupWorkInfo> machineGroupWorkInfos;
            if (criteria.getDayNum() > 0) {
                machineGroupWorkInfos = machineGroupWorkInfoService.getHistoryMachineGroupWorkInfo(criteria);
            } else {
                machineGroupWorkInfos = machineGroupWorkInfoService.generateMachineGroupWorkInfo(criteria);
            }
            if (null != machineGroupWorkInfos && !machineGroupWorkInfos.isEmpty()) {
                resultBean.setData(machineGroupWorkInfos);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
