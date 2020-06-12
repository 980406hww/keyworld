package com.keymanager.ckadmin.controller.internal.rest;

import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.ckadmin.entity.MachineGroupWorkInfo;
import com.keymanager.ckadmin.service.MachineGroupWorkInfoService;
import com.keymanager.ckadmin.service.UserInfoService;
import com.keymanager.ckadmin.service.UserRoleService;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.keymanager.ckadmin.util.StringUtil;
import org.apache.commons.lang.time.DateFormatUtils;
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

    private static final Logger logger = LoggerFactory.getLogger(MachineGroupWorkInfoController.class);

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

    @RequiresPermissions("/internal/machinegroupstatistics/toMachineGroupStatistics")
    @RequestMapping(value = "/searchMachineGroupWorkInfos", method = RequestMethod.POST)
    public ResultBean searchMachineGroupWorkInfosPost(@RequestBody MachineGroupWorkInfoCriteria criteria, HttpServletRequest request) {
        ResultBean resultBean = new ResultBean(0, "success");
        if ("init".equals(criteria.getInit())) {
            return resultBean;
        }
        try {
            HttpSession session = request.getSession();
            String userName = (String) session.getAttribute("username");
            boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
            if (!isDepartmentManager) {
                criteria.setUserName(userName);
            }
            List<MachineGroupWorkInfo> machineGroupWorkInfos;
            String nowDate = DateFormatUtils.format(new Date(), "yyyy-MM-dd");
            if (StringUtil.isNotNullNorEmpty(criteria.getHistoryDate()) && !nowDate.equals(criteria.getHistoryDate())) {
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
