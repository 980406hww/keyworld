package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.MachineInfoBatchUpdateCriteria;
import com.keymanager.monitoring.criteria.MachineInfoCriteria;
import com.keymanager.monitoring.criteria.MachineInfoGroupStatCriteria;
import com.keymanager.monitoring.entity.MachineInfo;
import com.keymanager.monitoring.entity.UserPageSetup;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.service.*;
import com.keymanager.monitoring.vo.MachineInfoGroupSummaryVO;
import com.keymanager.monitoring.vo.MachineInfoSummaryVO;
import com.keymanager.util.Constants;
import com.keymanager.util.FileUtil;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/internal/machineInfo")
public class MachineInfoRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(MachineInfoRestController.class);

    @Autowired
    private MachineInfoService machineInfoService;
    
    @Autowired
    private PerformanceService performanceService;

    @Autowired
    private UserPageSetupService userPageSetupService;

    @RequiresPermissions("/internal/machineInfo/changeTerminalType")
    @RequestMapping(value = "/changeTerminalType", method = RequestMethod.POST)
    public ResponseEntity<?> changeTerminalType(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        String clientID = (String) requestMap.get("clientID");
        try {
            machineInfoService.changeTerminalType(clientID, TerminalTypeEnum.PC.name().equals(terminalType) ? TerminalTypeEnum.Phone.name() :
                    TerminalTypeEnum.PC.name());
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/searchMachineInfos")
    @RequestMapping(value = "/searchMachineInfos", method = RequestMethod.GET)
    public ModelAndView searchMachineInfos(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        String loginName = getCurrentUser().getLoginName();
        String requestURI = request.getRequestURI();
        MachineInfoCriteria machineInfoCriteria = new MachineInfoCriteria();
        UserPageSetup userPageSetup = userPageSetupService.searchUserPageSetup(loginName,requestURI);
        if(userPageSetup != null){
            machineInfoCriteria.setHiddenColumns(userPageSetup.getHiddenField());
        }else {
            userPageSetupService.addUserPageSetup(loginName,requestURI,machineInfoCriteria.getHiddenColumns());
        }
        return constructMachineInfoModelAndView(request,machineInfoCriteria, currentPageNumber, pageSize, true);
    }

    @RequiresPermissions("/internal/machineInfo/searchMachineInfos")
    @RequestMapping(value = "/searchMachineInfos", method = RequestMethod.POST)
    public ModelAndView searchMachineInfosPost(HttpServletRequest request, MachineInfoCriteria machineInfoCriteria) {
        try {
            String loginName = getCurrentUser().getLoginName();
            String requestURI = request.getRequestURI();
            if(machineInfoCriteria.getHaveHiddenColumns()){
                userPageSetupService.updateUserPageSetup(loginName,requestURI,machineInfoCriteria.getHiddenColumns());
                machineInfoCriteria.setHaveHiddenColumns(false);
            }
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "50";
            }
            return constructMachineInfoModelAndView(request, machineInfoCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize), true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/machineInfo/machineInfo");
        }
    }

    @RequiresPermissions("/internal/machineInfo/searchBadMachineInfo")
    @RequestMapping(value = "/searchBadMachineInfo", method = RequestMethod.POST)
    public ModelAndView searchBadMachineInfo(HttpServletRequest request, MachineInfoCriteria machineInfoCriteria) {
        try {
            return constructMachineInfoModelAndView(request, machineInfoCriteria, 1, 50, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/machineInfo/machineInfo");
        }
    }

    private ModelAndView constructMachineInfoModelAndView(HttpServletRequest request, MachineInfoCriteria machineInfoCriteria, int currentPageNumber, int pageSize, boolean normalSearchFlag) {
        long startMilleSeconds = System.currentTimeMillis();
        ModelAndView modelAndView = new ModelAndView("/machineInfo/machineInfo");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        machineInfoCriteria.setTerminalType(terminalType);
        Set<String> switchGroups = getCurrentUser().getRoles();
        if(!switchGroups.contains("DepartmentManager")) {
            machineInfoCriteria.setSwitchGroups(switchGroups);
        }
        Page<MachineInfo> page = machineInfoService.searchMachineInfos(new Page<MachineInfo>(currentPageNumber, pageSize), machineInfoCriteria, normalSearchFlag);
        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("machineInfoCriteria", machineInfoCriteria);
        modelAndView.addObject("validMap", Constants.CLIENT_STATUS_VALID_MAP);
        modelAndView.addObject("orderByMap", Constants.CLIENT_STATUS_ORDERBY_MAP);
        modelAndView.addObject("urlPrefix", getUrlPrefix(request));
        modelAndView.addObject("page", page);
        performanceService.addPerformanceLog(terminalType + ":searchCustomerKeywords", System.currentTimeMillis() - startMilleSeconds, null);
        return modelAndView;
    }

    private String getUrlPrefix(HttpServletRequest request){
        String url = request.getRequestURL().toString();
        String urlPrefix = url.split("\\.")[0];
        return urlPrefix.replace("http://", "");
    }

    @RequiresPermissions("/internal/machineInfo/updateMachineInfoTargetVersion")
    @RequestMapping(value = "/updateMachineInfoTargetVersion", method = RequestMethod.POST)
    public ResponseEntity<?> updateMachineInfoTargetVersion(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String targetVersion = (String) requestMap.get("targetVersion");
            machineInfoService.updateMachineInfoTargetVersion(clientIDs, targetVersion);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/updateMachineInfoTargetVPSPassword", method = RequestMethod.POST)
    public ResponseEntity<?> updateMachineInfoTargetVPSPassword(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String targetVPSPassword = (String) requestMap.get("targetVPSPassword");
            machineInfoService.updateMachineInfoTargetVPSPassword(clientIDs, targetVPSPassword);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/updateMachineInfoRenewalDate")
    @RequestMapping(value = "/updateMachineInfoRenewalDate", method = RequestMethod.POST)
    public ResponseEntity<?> updateMachineInfoRenewalDate(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientIDs = (String) requestMap.get("clientIDs");
            String settingType = (String) requestMap.get("settingType");
            String renewalDate = (String) requestMap.get("renewalDate");
            machineInfoService.updateRenewalDate(clientIDs, settingType, renewalDate);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/saveMachineInfo", method = RequestMethod.POST)
    public ResponseEntity<?> saveMachineInfo(@RequestBody MachineInfo machineInfo) {
        try {
            machineInfoService.saveMachineInfo(machineInfo);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/batchUpdateMachineInfo", method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateMachineInfo(@RequestBody MachineInfoBatchUpdateCriteria machineInfoBatchUpdateCriteria) {
        try {
            machineInfoService.batchUpdateMachineInfo(machineInfoBatchUpdateCriteria);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/updateGroup")
    @RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
    public ResponseEntity<?> updateGroup(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientID = (String)requestMap.get("clientID");
            String group = (String)requestMap.get("group");
            machineInfoService.updateGroup(clientID, group);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/updateUpgradeFailedReason")
    @RequestMapping(value = "/updateUpgradeFailedReason", method = RequestMethod.POST)
    public ResponseEntity<?> updateUpgradeFailedReason(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientID = (String)requestMap.get("clientID");
            String upgradeFailedReason = (String)requestMap.get("upgradeFailedReason");
            machineInfoService.updateUpgradeFailedReason(clientID, upgradeFailedReason);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getMachineInfo/{clientID}", method = RequestMethod.POST)
    public ResponseEntity<?> getMachineInfo(@PathVariable("clientID") String clientID, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            MachineInfo machineInfo = machineInfoService.getMachineInfo(clientID, terminalType);
            return new ResponseEntity<Object>(machineInfo, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/deleteMachineInfo")
    @RequestMapping(value = "/deleteMachineInfo/{clientID}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteMachineInfo(@PathVariable("clientID") String clientID) {
        try {
            machineInfoService.deleteMachineInfo(clientID);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/deleteMachineInfos")
    @RequestMapping(value = "/deleteMachineInfos", method = RequestMethod.POST)
    public ResponseEntity<?> deleteMachineInfos(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            machineInfoService.deleteAll(clientIDs);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/resetRestartStatusForProcessing")
    @RequestMapping(value = "/resetRestartStatusForProcessing", method = RequestMethod.POST)
    public ResponseEntity<?> resetRestartStatusForProcessing() {
        try {
            machineInfoService.resetRestartStatusForProcessing();
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/changeStatus")
    @RequestMapping(value = "/changeStatus/{clientID}", method = RequestMethod.POST)
    public ResponseEntity<?> changeStatus(@PathVariable("clientID") String clientID) {
        try {
            machineInfoService.changeStatus(clientID);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/uploadVNCFile")
    @RequestMapping(value = "/uploadVNCFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVNCFile(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            machineInfoService.uploadVNCFile(file.getInputStream(), terminalType);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/uploadVPSFile")
    @RequestMapping(value = "/uploadVPSFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVPSFile(@RequestParam(value = "file", required = false) MultipartFile file,
                                           @RequestParam(defaultValue = "common", name = "machineInfoType") String machineInfoType,
                                           @RequestParam(defaultValue = "no", name = "downloadProgramType") String downloadProgramType, HttpServletRequest request) {
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
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/machineInfoStat")
    @RequestMapping(value = "/machineInfoStat", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView machineInfoStat(String clientIDPrefix, String city, String switchGroupName, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("machineInfo/machineInfoStat");
        try {
            if(request.getMethod().equals("GET")){
                return modelAndView;
            }
            List<MachineInfoSummaryVO> machineInfoSummaryVOs = machineInfoService.searchMachineInfoSummaryVO(clientIDPrefix, city, switchGroupName);
            modelAndView.addObject("clientIDPrefix", clientIDPrefix);
            modelAndView.addObject("city", city);
            modelAndView.addObject("switchGroupName", switchGroupName);
            modelAndView.addObject("machineInfoSummaryVOs", machineInfoSummaryVOs);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return modelAndView;
    }

    @RequiresPermissions("/internal/machineInfo/machineInfoGroupStat")
    @RequestMapping(value = "/machineInfoGroupStat", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView machineInfoGroupStat(MachineInfoGroupStatCriteria machineInfoGroupStatCriteria, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("machineInfo/machineInfoGroupStat");
        try {
            if(request.getMethod().equals("GET")){
                return modelAndView;
            }
            if (null != machineInfoGroupStatCriteria.getGroupName()) {
                machineInfoGroupStatCriteria.setGroupName(machineInfoGroupStatCriteria.getGroupName().trim());
            }
            List<MachineInfoGroupSummaryVO> machineInfoGroupSummaryVOs = machineInfoService.searchMachineInfoGroupSummaryVO(
                    machineInfoGroupStatCriteria.getGroupName(), machineInfoGroupStatCriteria.getTerminalType());
            modelAndView.addObject("machineInfoGroupStatCriteria", machineInfoGroupStatCriteria);
            modelAndView.addObject("machineInfoGroupSummaryVOs", machineInfoGroupSummaryVOs);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return modelAndView;
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/reopenMachineInfo", method = RequestMethod.POST)
    public ResponseEntity<?> reopenMachineInfo(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String downloadProgramType = (String) requestMap.get("downloadProgramType");
            machineInfoService.reopenMachineInfo(clientIDs, downloadProgramType);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/saveMachineInfo")
    @RequestMapping(value = "/updateStartUpStatusForCompleted", method = RequestMethod.POST)
    public ResponseEntity<?> updateStartUpStatusForCompleted(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            machineInfoService.updateStartUpStatusForCompleted(clientIDs);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/changeTerminalType")
    @RequestMapping(value = "/batchChangeTerminalType", method = RequestMethod.POST)
    public ResponseEntity<?> batchChangeTerminalType(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        String clientIDs = (String) requestMap.get("clientID");
        String[] clientID = clientIDs.split(",");
        try {
            machineInfoService.batchChangeTerminalType(clientID, TerminalTypeEnum.PC.name().equals(terminalType) ? TerminalTypeEnum.Phone.name() :
                    TerminalTypeEnum.PC.name());
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/changeStatus")
    @RequestMapping(value = "/batchChangeStatus", method = RequestMethod.POST)
    public ResponseEntity<?> batchChangeStatus(@Param("clientIDs") String clientIDs,@Param("status")Boolean status) {
        try {
            machineInfoService.batchChangeStatus(clientIDs,status);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 批量更新根据检索条件获取到的机器的机器分组
     */
    @RequiresPermissions("/internal/machineInfo/updateMachineGroup")
    @RequestMapping(value = "/updateMachineGroup",method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateMachineGroup(@RequestBody MachineInfoCriteria machineInfoCriteria) {
        try{
            machineInfoService.updateMachineGroup(machineInfoCriteria);
            return new ResponseEntity<Object>(true,HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/machineInfo/updateMachineGroup")
    @RequestMapping(value = "/updateMachineGroupById", method = RequestMethod.POST)
    public ResponseEntity<?> updateMachineGroup(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientID = (String)requestMap.get("clientID");
            String machineGroup = (String)requestMap.get("machineGroup");
            machineInfoService.updateMachineGroupById(clientID, machineGroup);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * 批量更新根据检索条件获取到的机器的机器分组
     */
    @RequiresPermissions("/internal/machineInfo/batchUpdateGroup")
    @RequestMapping(value = "/batchUpdateGroup",method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateGroup(@RequestBody MachineInfoCriteria machineInfoCriteria) {
        try{
            machineInfoService.batchUpdateGroup(machineInfoCriteria);
            return new ResponseEntity<Object>(true,HttpStatus.OK);
        }catch (Exception e){
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
        }
    }



}
