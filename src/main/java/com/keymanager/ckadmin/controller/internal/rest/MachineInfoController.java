package com.keymanager.ckadmin.controller.internal.rest;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.ckadmin.common.result.ResultBean;
import com.keymanager.ckadmin.controller.SpringMVCBaseController;
import com.keymanager.ckadmin.criteria.MachineInfoBatchUpdateCriteria;
import com.keymanager.ckadmin.criteria.MachineInfoCriteria;
import com.keymanager.ckadmin.criteria.MachineInfoGroupStatCriteria;
import com.keymanager.ckadmin.entity.MachineInfo;
import com.keymanager.ckadmin.entity.UserPageSetup;
import com.keymanager.ckadmin.enums.TerminalTypeEnum;
import com.keymanager.ckadmin.service.MachineInfoService;
import com.keymanager.ckadmin.service.PerformanceService;
import com.keymanager.ckadmin.service.UserPageSetupService;
import com.keymanager.ckadmin.vo.MachineInfoGroupSummaryVO;
import com.keymanager.ckadmin.vo.MachineInfoSummaryVO;
import com.keymanager.util.FileUtil;
import com.keymanager.util.Utils;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView toMachineInfos() {
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

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/toBatchUpdateMachine", method = RequestMethod.GET)
    public ModelAndView toBatchUpdateMachine(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/BatchUpdateMachine");
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
    public ResultBean returnUploadFile() {
        ResultBean resultBean = new ResultBean();
        resultBean.setCode(200);
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/uploadVPSFile")
    @RequestMapping(value = "/showUploadVNCDialog", method = RequestMethod.GET)
    public ModelAndView showUploadVNCDialog() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/UploadVNCDialog");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/toBatchUpdateFailedReason", method = RequestMethod.GET)
    public ModelAndView toBatchUpdateFailedReason() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/BatchUpdateFailedReason");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/toBatchChangeStatus", method = RequestMethod.GET)
    public ModelAndView toBatchChangeStatus() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/BatchChangeStatus");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/toUpdateMachineInfo", method = RequestMethod.GET)
    public ModelAndView toUpdateMachineInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/UpdateMachineInfo");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/toAllowSwitchGroup", method = RequestMethod.GET)
    public ModelAndView toAllowSwitchGroup() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/AllowSwitchGroup");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/toSwitchGroup", method = RequestMethod.GET)
    public ModelAndView toSwitchGroup() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/SwitchGroup");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/showTargetVersionSettingDialog", method = RequestMethod.GET)
    public ModelAndView showTargetVersionSettingDialog() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/UpdateTargetVersion");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/machineInfoGroupStat")
    @RequestMapping(value = "/toMachineInfoGroupStat", method = RequestMethod.GET)
    public ModelAndView toMachineInfoGroupStat() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/machineInfoGroupStat");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/showTargetVPSPasswordSettingDialog", method = RequestMethod.GET)
    public ModelAndView showTargetVPSPasswordSettingDialog() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/UpdatePassword");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/showRenewalSettingDialog", method = RequestMethod.GET)
    public ModelAndView showRenewalSettingDialog() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/UpdateRenewalSetting");
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/resetRestartStatusForProcessing")
    @RequestMapping(value = "/resetRestartStatusForProcessing", method = RequestMethod.POST)
    public ResultBean resetRestartStatusForProcessing() {
        ResultBean resultBean = new ResultBean();
        try {
            machineInfoService.resetRestartStatusForProcessing();
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/updateMachineInfoRenewalDate")
    @RequestMapping(value = "/updateMachineInfoRenewalDate", method = RequestMethod.POST)
    public ResultBean updateMachineInfoRenewalDate(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String settingType = (String) requestMap.get("settingType");
            String renewalDate = (String) requestMap.get("renewalDate");
            machineInfoService.updateRenewalDate(clientIDs, settingType, renewalDate);
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
    @RequestMapping(value = "/updateMachineInfoTargetVPSPassword", method = RequestMethod.POST)
    public ResultBean updateMachineInfoTargetVPSPassword(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String targetVPSPassword = (String) requestMap.get("targetVPSPassword");
            machineInfoService.updateMachineInfoTargetVPSPassword(clientIDs, targetVPSPassword);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/updateMachineInfoTargetVersion")
    @RequestMapping(value = "/updateMachineInfoTargetVersion", method = RequestMethod.POST)
    public ResultBean updateMachineInfoTargetVersion(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String targetVersion = (String) requestMap.get("targetVersion");
            machineInfoService.updateMachineInfoTargetVersion(clientIDs, targetVersion);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/uploadVNCFile")
    @RequestMapping(value = "/uploadVNCFile", method = RequestMethod.POST)
    public ResultBean uploadVNCFile(@RequestParam(value = "file", required = false) MultipartFile file,
                                    @RequestParam(name = "terminalType") String terminalType) {
        ResultBean resultBean = new ResultBean();
        try {
            machineInfoService.uploadVNCFile(file.getInputStream(), terminalType);
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
    @RequestMapping(value = "/batchUpdateSwitchGroupName", method = RequestMethod.POST)
    public ResultBean batchUpdateSwitchGroupName(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String switchGroupName = (String) requestMap.get("switchGroupName");
            machineInfoService.batchUpdateSwitchGroupName(clientIDs, switchGroupName);
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
    @RequestMapping(value = "/batchUpdateAllowSwitchGroup", method = RequestMethod.POST)
    public ResultBean batchUpdateAllowSwitchGroup(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String allowSwitchGroup = (String) requestMap.get("allowSwitchGroup");
            machineInfoService.batchUpdateAllowSwitchGroup(clientIDs, allowSwitchGroup);
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
    @RequestMapping(value = "/batchUpdateMachine", method = RequestMethod.POST)
    public ResultBean batchUpdateMachine(@RequestBody MachineInfoCriteria machineInfoCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            machineInfoService.batchUpdateMachine(machineInfoCriteria);
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
    @RequestMapping(value = "/batchUpdateMachineInfo", method = RequestMethod.POST)
    public ResultBean batchUpdateMachineInfo(@RequestBody MachineInfoBatchUpdateCriteria machineInfoBatchUpdateCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            machineInfoService.batchUpdateMachineInfo(machineInfoBatchUpdateCriteria);
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
    @RequestMapping(value = "/updateMachineInfo", method = RequestMethod.POST)
    public ResultBean updateMachineInfo(@RequestBody MachineInfo machineInfo) {
        ResultBean resultBean = new ResultBean();
        try {
            machineInfoService.updateMachineInfo(machineInfo);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequestMapping(value = "/getMachineInfo/{clientID}/{terminalType}", method = RequestMethod.POST)
    public ResultBean getMachineInfo(@PathVariable("clientID") String clientID, @PathVariable("terminalType") String terminalType) {
        ResultBean resultBean = new ResultBean();
        try {
            MachineInfo machineInfo = machineInfoService.getMachineInfo(clientID, terminalType);
            resultBean.setCode(200);
            resultBean.setData(machineInfo);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/updateMachineGroup")
    @RequestMapping(value = "/updateMachineGroup", method = RequestMethod.POST)
    public ResultBean batchUpdateMachineGroup(@RequestBody MachineInfoCriteria machineInfoCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            machineInfoService.updateMachineGroup(machineInfoCriteria);
            resultBean.setCode(200);
        } catch (Exception e) {
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
        @RequestParam(defaultValue = "no", name = "downloadProgramType") String downloadProgramType, @RequestParam(name = "terminalType") String terminalType) {
        ResultBean resultBean = new ResultBean();
        try {
            String path = Utils.getWebRootPath() + "vpsfile";
            File targetFile = new File(path, "vps.txt");
            if (!targetFile.exists()) {
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            machineInfoService.uploadVPSFile(machineInfoType, downloadProgramType, targetFile, terminalType);
            FileUtil.delFolder(path);
            resultBean.setCode(200);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/reopenMachineInfo", method = RequestMethod.POST)
    public ResultBean reopenMachineInfo(@RequestBody Map<String, Object> requestMap) {
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
    @RequestMapping(value = "/batchUpdateUpgradeFailedReason", method = RequestMethod.POST)
    public ResultBean batchUpdateUpgradeFailedReason(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String upgradeFailedReason = (String) requestMap.get("upgradeFailedReason");
            machineInfoService.batchUpdateUpgradeFailedReason(clientIDs, upgradeFailedReason);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/changeStatus")
    @RequestMapping(value = "/changeStatus/{clientID}", method = RequestMethod.POST)
    public ResultBean changeStatus(@PathVariable("clientID") String clientID) {
        ResultBean resultBean = new ResultBean();
        try {
            machineInfoService.changeStatus(clientID);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/changeStatus")
    @RequestMapping(value = "/batchChangeStatus", method = RequestMethod.POST)
    public ResultBean batchChangeStatus(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
        boolean valid = (boolean) requestMap.get("valid");
        try {
            machineInfoService.batchChangeStatus(clientIDs, valid);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/changeTerminalType")
    @RequestMapping(value = "/batchChangeTerminalType", method = RequestMethod.POST)
    public ResultBean batchChangeTerminalType(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
        String terminalType = (String) requestMap.get("terminalType");
        try {
            machineInfoService.batchChangeTerminalType(clientIDs, terminalType);
            resultBean.setCode(200);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg("未知错误");
            return resultBean;
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/changeTerminalType")
    @RequestMapping(value = "/changeTerminalType", method = RequestMethod.POST)
    public ResultBean changeTerminalType(@RequestBody Map<String, Object> requestMap) {
        ResultBean resultBean = new ResultBean();
        String clientID = (String) requestMap.get("clientID");
        String terminalType = (String) requestMap.get("terminalType");
        try {
            machineInfoService
                .changeTerminalType(clientID, TerminalTypeEnum.PC.name().equals(terminalType) ? TerminalTypeEnum.Phone.name() : TerminalTypeEnum.PC.name());
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
    public ResultBean updateStartUpStatusForCompleted(@RequestBody Map<String, Object> requestMap) {
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
    public ResultBean getMachineInfos(HttpServletRequest request, @RequestBody MachineInfoCriteria machineInfoCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            String loginName = getCurrentUser().getLoginName();
            String requestURI = request.getRequestURI();
            UserPageSetup userPageSetup = userPageSetupService.searchUserPageSetup(loginName, requestURI);
            if (userPageSetup != null) {
                machineInfoCriteria.setHiddenColumns(userPageSetup.getHiddenField());
            } else {
                userPageSetupService.addUserPageSetup(loginName, requestURI, machineInfoCriteria.getHiddenColumns());
            }
            Page<MachineInfo> page = new Page<>(machineInfoCriteria.getPage(), machineInfoCriteria.getLimit());
            long startMilleSeconds = System.currentTimeMillis();
            Set<String> switchGroups = getCurrentUser().getRoles();
            if (!switchGroups.contains("DepartmentManager")) {
                machineInfoCriteria.setSwitchGroups(switchGroups);
            }
            performanceService.addPerformanceLog(machineInfoCriteria.getTerminalType() + ":searchCustomerKeywords", System.currentTimeMillis() - startMilleSeconds, null);
            page = machineInfoService.searchMachineInfos(page, machineInfoCriteria, true);
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
    public ResultBean batchDeleteClientUpgrade(@RequestBody Map<String, Object> requestMap) {
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

    @RequiresPermissions("/internal/machineInfo/machineInfoStat")
    @RequestMapping(value = "/toMachineInfo", method = RequestMethod.GET)
    public ModelAndView toMachineInfo() {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/machineInfo");
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


    @RequiresPermissions("/internal/machineInfo/searchMachineInfos")
    @RequestMapping(value = "/toMachineInfoFromATP/{terminalType}/{machineGroup}", method = RequestMethod.GET)
    public ModelAndView toMachineInfoFromATP(@PathVariable(name = "terminalType") String terminalType, @PathVariable(name = "machineGroup") String machineGroup)
        throws UnsupportedEncodingException {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("machineManage/machineManage");
        machineGroup = URLDecoder.decode(machineGroup, "UTF-8");
        mv.addObject("machineGroupFromATP", machineGroup);
        mv.addObject("terminalTypeFromATP", terminalType);
        return mv;
    }

    @RequiresPermissions("/internal/machineInfo/machineInfoGroupStat")
    @PostMapping(value = "/machineInfoGroupStat")
    public ResultBean machineInfoGroupStat(@RequestBody MachineInfoGroupStatCriteria machineInfoGroupStatCriteria) {
        ResultBean resultBean = new ResultBean();
        try {
            Page<MachineInfoGroupSummaryVO> page = new Page<>(machineInfoGroupStatCriteria.getPage(), machineInfoGroupStatCriteria.getLimit());
            page = machineInfoService.searchMachineInfoGroupSummaryVO(page, machineInfoGroupStatCriteria);
            List<MachineInfoGroupSummaryVO> machineInfoGroupSummaryVOs = page.getRecords();
            resultBean.setData(machineInfoGroupSummaryVOs);
            resultBean.setCount(page.getTotal());
            resultBean.setMsg("success");
            resultBean.setCode(0);
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }

    @RequiresPermissions("/internal/machineInfo/machineInfoStat")
    @RequestMapping(value = "/machineInfoStat", method = RequestMethod.POST)
    public ResultBean machineInfoStat(@RequestBody Map<String, String> map) {
        ResultBean resultBean = new ResultBean(0, "success");
        try {
            String clientIDPrefix = map.get("clientIDPrefix");
            String city = map.get("city");
            String switchGroupName = map.get("switchGroupName");
            String init = map.get("init");
            resultBean.setData(machineInfoService.searchMachineInfoSummaryVO(clientIDPrefix, city, switchGroupName, init));
        } catch (Exception e) {
            logger.error(e.getMessage());
            resultBean.setCode(400);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
