package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.ClientStatusBatchUpdateCriteria;
import com.keymanager.monitoring.criteria.MachineInfoCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.MachineInfo;
import com.keymanager.monitoring.entity.UserPageSetup;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.service.*;
import com.keymanager.util.Constants;
import com.keymanager.util.FileUtil;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import com.keymanager.value.ClientStatusGroupSummaryVO;
import com.keymanager.value.ClientStatusSummaryVO;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private ConfigService configService;

//    @RequiresPermissions("/internal/clientstatus/changeTerminalType")
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

//    @RequiresPermissions("/internal/clientstatus/searchMachineInfos")
    @RequestMapping(value = "/searchMachineInfos", method = RequestMethod.GET)
    public ModelAndView searchClientStatuses(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
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

//    @RequiresPermissions("/internal/clientstatus/searchMachineInfos")
    @RequestMapping(value = "/searchMachineInfos", method = RequestMethod.POST)
    public ModelAndView searchClientStatusesPost(HttpServletRequest request, MachineInfoCriteria machineInfoCriteria) {
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
            return new ModelAndView("/client/list");
        }
    }

//    @RequiresPermissions("/internal/clientstatus/searchBadClientStatus")
    @RequestMapping(value = "/searchBadMachineInfo", method = RequestMethod.POST)
    public ModelAndView searchBadClientStatus(HttpServletRequest request, MachineInfoCriteria machineInfoCriteria) {
        try {
            return constructMachineInfoModelAndView(request, machineInfoCriteria, 1, 50, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/client/list");
        }
    }

    private ModelAndView constructMachineInfoModelAndView(HttpServletRequest request, MachineInfoCriteria machineInfoCriteria, int currentPageNumber, int pageSize, boolean normalSearchFlag) {
        long startMilleSeconds = System.currentTimeMillis();
        ModelAndView modelAndView = new ModelAndView("/machineInfo/machineInfo");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        machineInfoCriteria.setTerminalType(terminalType);
        boolean isDepartmentManager = false;
        Set<String> switchGroups = getCurrentUser().getRoles();
        if(!switchGroups.contains("DepartmentManager")) {
            machineInfoCriteria.setSwitchGroups(switchGroups);
        }
        Page<MachineInfo> page = machineInfoService.searchMachineInfos(new Page<MachineInfo>(currentPageNumber, pageSize), machineInfoCriteria, normalSearchFlag);
        String [] operationTypeValues = machineInfoService.getOperationTypeValues(terminalType);
        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("machineInfoCriteria", machineInfoCriteria);
        modelAndView.addObject("validMap", Constants.CLIENT_STATUS_VALID_MAP);
        modelAndView.addObject("orderByMap", Constants.CLIENT_STATUS_ORDERBY_MAP);
        modelAndView.addObject("operationTypeValues", operationTypeValues);
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

//    @RequiresPermissions("/internal/clientstatus/updateClientStatusTargetVersion")
    @RequestMapping(value = "/updateClientStatusTargetVersion", method = RequestMethod.POST)
    public ResponseEntity<?> updateClientStatusTargetVersion(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String targetVersion = (String) requestMap.get("targetVersion");
            machineInfoService.updateClientStatusTargetVersion(clientIDs, targetVersion);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
    
//    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
    @RequestMapping(value = "/updateClientStatusTargetVPSPassword", method = RequestMethod.POST)
    public ResponseEntity<?> updateClientStatusTargetVPSPassword(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String targetVPSPassword = (String) requestMap.get("targetVPSPassword");
            machineInfoService.updateClientStatusTargetVPSPassword(clientIDs, targetVPSPassword);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/updateClientStatusRenewalDate")
    @RequestMapping(value = "/updateClientStatusRenewalDate", method = RequestMethod.POST)
    public ResponseEntity<?> updateClientStatusRenewalDate(@RequestBody Map<String, Object> requestMap) {
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

//    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
    @RequestMapping(value = "/saveClientStatus", method = RequestMethod.POST)
    public ResponseEntity<?> saveClientStatus(@RequestBody ClientStatus clientStatus) {
        try {
            machineInfoService.saveClientStatus(clientStatus);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
    @RequestMapping(value = "/batchUpdateClientStatus", method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateClientStatus(@RequestBody ClientStatusBatchUpdateCriteria clientStatusBatchUpdateCriteria) {
        try {
            machineInfoService.batchUpdateClientStatus(clientStatusBatchUpdateCriteria);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/updateGroup")
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

//    @RequiresPermissions("/internal/clientstatus/updateOperationType")
    @RequestMapping(value = "/updateOperationType", method = RequestMethod.POST)
    public ResponseEntity<?> updateOperationType(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientID = (String)requestMap.get("clientID");
            String operationType = (String)requestMap.get("operationType");
            machineInfoService.updateOperationType(clientID, operationType);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/updateUpgradeFailedReason")
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

//    @RequestMapping(value = "/getClientStatus/{clientID}", method = RequestMethod.POST)
    public ResponseEntity<?> getClientStatus(@PathVariable("clientID") String clientID, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            ClientStatus clientStatus = machineInfoService.getClientStatus(clientID, terminalType);
            return new ResponseEntity<Object>(clientStatus, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/deleteClientStatus")
    @RequestMapping(value = "/deleteClientStatus/{clientID}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteClientStatus(@PathVariable("clientID") String clientID) {
        try {
            machineInfoService.deleteClientStatus(clientID);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/deleteClientStatuses")
    @RequestMapping(value = "/deleteClientStatuses", method = RequestMethod.POST)
    public ResponseEntity<?> deleteClientStatuses(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            machineInfoService.deleteAll(clientIDs);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/resetRestartStatusForProcessing")
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

//    @RequiresPermissions("/internal/clientstatus/changeStatus")
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

//    @RequiresPermissions("/internal/clientstatus/uploadVNCFile")
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

//    @RequiresPermissions("/internal/clientstatus/uploadVPSFile")
    @RequestMapping(value = "/uploadVPSFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVPSFile(@RequestParam(value = "file", required = false) MultipartFile file,
                                           @RequestParam(defaultValue = "common", name = "clientStatusType") String clientStatusType,
                                           @RequestParam(defaultValue = "no", name = "downloadProgramType") String downloadProgramType, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String path = Utils.getWebRootPath() + "vpsfile";
            File targetFile = new File(path, "vps.txt");
            if(!targetFile.exists()){
                targetFile.mkdirs();
            }
            file.transferTo(targetFile);
            machineInfoService.uploadVPSFile(clientStatusType, downloadProgramType, targetFile, terminalType);
            FileUtil.delFolder(path);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/downloadVNCFile")
    @RequestMapping(value = "/downloadVNCFile", method = RequestMethod.POST)
    public ResponseEntity<?> downloadVNCFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            machineInfoService.getVNCFileInfo(TerminalTypeMapping.getTerminalType(request));
            Config config = configService.getConfig(Constants.CONFIG_TYPE_ZIP_ENCRYPTION, Constants.CONFIG_KEY_PASSWORD);
            downFile("vnc.zip", config.getValue() + Utils.getCurrentDate());
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }

    }

//    @RequiresPermissions("/internal/clientstatus/downloadFullVNCFile")
    @RequestMapping(value = "/downloadFullVNCFile", method = RequestMethod.POST)
    public ResponseEntity<?> downloadFullVNCFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            machineInfoService.getFullVNCFileInfo(TerminalTypeMapping.getTerminalType(request));
            Config config = configService.getConfig(Constants.CONFIG_TYPE_ZIP_ENCRYPTION, Constants.CONFIG_KEY_PASSWORD);
            downFile("vncAll.zip", config.getValue() + Utils.getCurrentDate());
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/clientStatusStat")
    @RequestMapping(value = "/clientStatusStat", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView clientStatusStat(String clientIDPrefix, String city, String switchGroupName, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("client/clientStatusStat");
        try {
            if(request.getMethod().equals("GET")){
                return modelAndView;
            }
            List<ClientStatusSummaryVO> clientStatusSummaryVOs = machineInfoService.searchClientStatusSummaryVO(clientIDPrefix, city, switchGroupName);
            modelAndView.addObject("clientIDPrefix", clientIDPrefix);
            modelAndView.addObject("city", city);
            modelAndView.addObject("switchGroupName", switchGroupName);
            modelAndView.addObject("clientStatusSummaryVOs", clientStatusSummaryVOs);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return modelAndView;
    }

//    @RequiresPermissions("/internal/clientstatus/clientStatusGroupStat")
    @RequestMapping(value = "/clientStatusGroupStat", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView clientStatusGroupStat(String group, String terminalType, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("client/clientStatusGroupStat");
        try {
            if(request.getMethod().equals("GET")){
                return modelAndView;
            }
            if (null != group) {
                group = group.trim();
            }
            List<ClientStatusGroupSummaryVO> clientStatusGroupSummaryVOs = machineInfoService.searchClientStatusGroupSummaryVO(group, terminalType);
            modelAndView.addObject("group", group);
            modelAndView.addObject("terminalType", terminalType);
            modelAndView.addObject("clientStatusGroupSummaryVOs", clientStatusGroupSummaryVOs);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return modelAndView;
    }

//    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
    @RequestMapping(value = "/reopenClientStatus", method = RequestMethod.POST)
    public ResponseEntity<?> reopenClientStatus(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String downloadProgramType = (String) requestMap.get("downloadProgramType");
            machineInfoService.reopenClientStatus(clientIDs, downloadProgramType);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

//    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
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

//    @RequiresPermissions("/internal/clientstatus/changeTerminalType")
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

//    @RequiresPermissions("/internal/clientstatus/changeStatus")
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
}