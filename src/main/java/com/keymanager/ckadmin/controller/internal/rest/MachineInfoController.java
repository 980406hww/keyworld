package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.criteria.MachineInfoCriteria;
import com.keymanager.ckadmin.entity.MachineInfo;
import com.keymanager.ckadmin.entity.UserPageSetup;
import com.keymanager.ckadmin.service.MachineInfoService;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.ckadmin.service.UserPageSetupService;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.util.FileUtil;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping(value = "/internal/machineManage")
public class MachineInfoController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(MachineInfoController.class);

    @Resource(name = "machineInfoService2")
    private MachineInfoService machineInfoService;

    @Resource(name = "performanceService2")
    private PerformanceService performanceService;

    @Resource(name = "userPageSetupService2")
    private UserPageSetupService userPageSetupService;

    @RequiresPermissions("/internal/machineInfo/searchMachineInfos")
    @RequestMapping(value = "/toMachineInfos", method = RequestMethod.GET)
    public ModelAndView toMachineInfo(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/machineManage");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/toReopenClient", method = RequestMethod.GET)
    public ModelAndView toReopenClient(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/ReopenClient");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/uploadVPSFile")
    @RequestMapping(value = "/toUploadVPSFile", method = RequestMethod.GET)
    public ModelAndView toUploadVPSFile(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/UploadVPSFile");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/updateMachineGroup")
    @RequestMapping(value = "/toUpdateMachineGroup", method = RequestMethod.GET)
    public ModelAndView toUpdateMachineGroup(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/UpdateMachineGroup");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/uploadVPSFile")
    @RequestMapping(value = "/returnUploadFile", method = RequestMethod.POST)
    public ResultBean returnUploadFile(){
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/updateMachineGroup")
    @RequestMapping(value = "/updateMachineGroup",method = RequestMethod.POST)
    public ResultBean batchUpdateMachineGroup(@RequestBody MachineInfoCriteria machineInfoCriteria) {
        ResultBean resultBean = new ResultBean();
        try{
            machineInfoService.updateMachineGroup(machineInfoCriteria);
            resultBean.setCode(200);
        }catch (Exception e){
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/uploadVPSFile")
    @RequestMapping(value = "/uploadVPSFile", method = RequestMethod.POST)
    public ResultBean uploadVPSFile(@RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam(defaultValue = "common", name = "machineInfoType") String machineInfoType,
                                    @RequestParam(defaultValue = "no", name = "downloadProgramType") String downloadProgramType, HttpServletRequest request){
        ResultBean resultBean = new ResultBean();
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String path = Utils.getWebRootPath() + "vpsfile";
            File targetFile = new File(path, "vps.txt");
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            machineInfoService.uploadVPSFile(machineInfoType, downloadProgramType, targetFile, terminalType);
            FileUtil.delFolder(path);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/reopenMachineInfo", method = RequestMethod.POST)
    public ResultBean reopenMachineInfo(@RequestBody Map<String, Object> requestMap){
        ResultBean resultBean = new ResultBean();
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String downloadProgramType = (String) requestMap.get("downloadProgramType");
            machineInfoService.reopenMachineInfo(clientIDs, downloadProgramType);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/updateStartUpStatusForCompleted", method = RequestMethod.POST)
    public ResultBean updateStartUpStatusForCompleted(@RequestBody Map<String, Object> requestMap){
        ResultBean resultBean = new ResultBean();
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            machineInfoService.updateStartUpStatusForCompleted(clientIDs);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/searchMachineInfos")
    @RequestMapping(value = "/getMachineInfos", method = RequestMethod.POST)
    public ResultBean getMachineInfos(HttpServletRequest request, @RequestBody MachineInfoCriteria machineInfoCriteria){
        ResultBean resultBean = new ResultBean();
        try {
            String loginName = getCurrentUser().getLoginName();
            String requestURI = request.getRequestURI();
            UserPageSetup userPageSetup = userPageSetupService.searchUserPageSetup(loginName,requestURI);
            if(userPageSetup != null){
                machineInfoCriteria.setHiddenColumns(userPageSetup.getHiddenField());
            }else {
                userPageSetupService.addUserPageSetup(loginName,requestURI,machineInfoCriteria.getHiddenColumns());
            }
            Page<MachineInfo> page = new Page<>(machineInfoCriteria.getPage(), machineInfoCriteria.getLimit());
            long startMilleSeconds = System.currentTimeMillis();
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            machineInfoCriteria.setTerminalType(terminalType);
            Set<String> switchGroups = getCurrentUser().getRoles();
            if(!switchGroups.contains("DepartmentManager")) {
                machineInfoCriteria.setSwitchGroups(switchGroups);
            }
            page = machineInfoService.searchMachineInfos(page, machineInfoCriteria, true);
            performanceService.addPerformanceLog(terminalType + ":searchCustomerKeywords", System.currentTimeMillis() - startMilleSeconds, null);
            List<MachineInfo> machineInfos = page.getRecords();
            resultBean.setCode(0);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setData(machineInfos);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/saveMachineInfos", method = RequestMethod.POST)
    public ResultBean saveMachineInfo(@RequestBody MachineInfo machineInfo) {
        ResultBean resultBean = new ResultBean();
        try {
            machineInfoService.saveMachineInfo(machineInfo);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/deleteMachineInfos")
    @RequestMapping(value = "/deleteMachineInfos", method = RequestMethod.POST)
    public ResultBean batchDeleteClientUpgrade(@RequestBody Map<String, Object> requestMap){
        ResultBean resultBean = new ResultBean();
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            machineInfoService.deleteAll(clientIDs);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/deleteMachineInfo")
    @RequestMapping(value = "/deleteMachineInfo/{clientID}", method = RequestMethod.GET)
    public ResultBean deleteClientUpgrade(@PathVariable("clientID") String clientID) {
        ResultBean resultBean = new ResultBean();
        try {
            machineInfoService.deleteMachineInfo(clientID);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

}
