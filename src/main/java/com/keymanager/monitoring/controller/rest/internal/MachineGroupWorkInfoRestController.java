package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.MachineGroupWorkInfoCriteria;
import com.keymanager.monitoring.entity.MachineGroupWorkInfo;
import com.keymanager.monitoring.service.*;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping(value = "/internal/machinegroupworkinfo")
public class MachineGroupWorkInfoRestController {

    private static Logger logger = LoggerFactory.getLogger(MachineGroupWorkInfoRestController.class);

    @Autowired
    private ConfigService configService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private IUserInfoService userInfoService;
    @Autowired
    private MachineGroupWorkInfoService machineGroupWorkInfoService;


    @RequiresPermissions("/internal/machinegroupworkinfo/searchMachineGroupWorkInfos")
    @RequestMapping(value = "/searchMachineGroupWorkInfos", method = RequestMethod.GET)
    public ModelAndView searchMachineGroupWorkInfos(HttpServletRequest request) {
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        ModelAndView modelAndView = new ModelAndView("/machineGroupWorkInfo/list");
        modelAndView.addObject("machineGroupWorkInfoCriteria", new MachineGroupWorkInfoCriteria());
        modelAndView.addObject("searchEngineMap", configService.getSearchEngineMap(terminalType));
        return modelAndView;
    }

    @RequiresPermissions("/internal/machinegroupworkinfo/searchMachineGroupWorkInfos")
    @RequestMapping(value = "/searchMachineGroupWorkInfos", method = RequestMethod.POST)
    public ModelAndView searchMachineGroupWorkInfosPost(HttpServletRequest request, MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria) {
        try {
            return constructNegativeListModelAndView(request, machineGroupWorkInfoCriteria);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/machineGroupWorkInfo/list");
        }
    }

    private ModelAndView constructNegativeListModelAndView(HttpServletRequest request, MachineGroupWorkInfoCriteria machineGroupWorkInfoCriteria) {
        ModelAndView modelAndView = new ModelAndView("/machineGroupWorkInfo/list");
        String entryType = (String) request.getSession().getAttribute("entryType");
        machineGroupWorkInfoCriteria.setEntryType(entryType);
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        machineGroupWorkInfoCriteria.setTerminalType(terminalType);

        HttpSession session = request.getSession();
        String userName = (String) session.getAttribute("username");
        boolean isDepartmentManager = userRoleService.isDepartmentManager(userInfoService.getUuidByLoginName(userName));
        if(!isDepartmentManager) {
            machineGroupWorkInfoCriteria.setUserName(userName);
        }
        List<MachineGroupWorkInfo> machineGroupWorkInfos;
        if (machineGroupWorkInfoCriteria.getDayNum() > 0) {
            machineGroupWorkInfos = machineGroupWorkInfoService.getHistoryMachineGroupWorkInfo(machineGroupWorkInfoCriteria);
        } else {
            machineGroupWorkInfos = machineGroupWorkInfoService.generateMachineGroupWorkInfo(machineGroupWorkInfoCriteria);
        }

        modelAndView.addObject("machineGroupWorkInfoCriteria", machineGroupWorkInfoCriteria);
        modelAndView.addObject("machineGroupWorkInfos", machineGroupWorkInfos);
        modelAndView.addObject("searchEngineMap", configService.getSearchEngineMap(terminalType));
        return modelAndView;
    }



}
