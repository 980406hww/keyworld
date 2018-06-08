package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.ClientStatusBatchUpdateCriteria;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.service.*;
import com.keymanager.util.Constants;
import com.keymanager.util.FileUtil;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
import com.keymanager.value.ClientStatusGroupSummaryVO;
import com.keymanager.value.ClientStatusSummaryVO;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
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
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/internal/clientstatus")
public class ClientStatusRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ClientStatusRestController.class);

    @Autowired
    private ClientStatusService clientStatusService;

    @Autowired
    private PerformanceService performanceService;

    @RequiresPermissions("/internal/clientstatus/changeTerminalType")
    @RequestMapping(value = "/changeTerminalType", method = RequestMethod.POST)
    public ResponseEntity<?> changeTerminalType(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        String clientID = (String) requestMap.get("clientID");
        try {
            clientStatusService.changeTerminalType(clientID, TerminalTypeEnum.PC.name().equals(terminalType) ? TerminalTypeEnum.Phone.name() :
                    TerminalTypeEnum.PC.name());
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception ex) {
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/searchClientStatuses")
    @RequestMapping(value = "/searchClientStatuses", method = RequestMethod.GET)
    public ModelAndView searchClientStatuses(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
        return constructClientStatusModelAndView(request, new ClientStatusCriteria(), currentPageNumber, pageSize, true);
    }

    @RequiresPermissions("/internal/clientstatus/searchClientStatuses")
    @RequestMapping(value = "/searchClientStatuses", method = RequestMethod.POST)
    public ModelAndView searchClientStatusesPost(HttpServletRequest request, ClientStatusCriteria clientStatusCriteria) {
        try {
            String currentPageNumber = request.getParameter("currentPageNumber");
            String pageSize = request.getParameter("pageSize");
            if (null == currentPageNumber && null == pageSize) {
                currentPageNumber = "1";
                pageSize = "50";
            }
            return constructClientStatusModelAndView(request, clientStatusCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize), true);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/client/list");
        }
    }

    @RequiresPermissions("/internal/clientstatus/searchBadClientStatus")
    @RequestMapping(value = "/searchBadClientStatus", method = RequestMethod.POST)
    public ModelAndView searchBadClientStatus(HttpServletRequest request, ClientStatusCriteria clientStatusCriteria) {
        try {
            return constructClientStatusModelAndView(request, clientStatusCriteria, 1, 50, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/client/list");
        }
    }

    private ModelAndView constructClientStatusModelAndView(HttpServletRequest request, ClientStatusCriteria clientStatusCriteria, int currentPageNumber, int pageSize, boolean normalSearchFlag) {
        long startMilleSeconds = System.currentTimeMillis();
        ModelAndView modelAndView = new ModelAndView("/client/list");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        clientStatusCriteria.setTerminalType(terminalType);
        boolean isDepartmentManager = false;
        Set<String> switchGroups = getCurrentUser().getRoles();
        if(!switchGroups.contains("DepartmentManager")) {
            clientStatusCriteria.setSwitchGroups(switchGroups);
        }
        Page<ClientStatus> page = clientStatusService.searchClientStatuses(new Page<ClientStatus>(currentPageNumber, pageSize), clientStatusCriteria, normalSearchFlag);
        String [] operationTypeValues = clientStatusService.getOperationTypeValues(terminalType);

        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("clientStatusCriteria", clientStatusCriteria);
        modelAndView.addObject("validMap", Constants.CLIENT_STATUS_VALID_MAP);
        modelAndView.addObject("orderByMap", Constants.CLIENT_STATUS_ORDERBY_MAP);
        modelAndView.addObject("operationTypeValues", operationTypeValues);
        modelAndView.addObject("page", page);
        performanceService.addPerformanceLog(terminalType + ":searchCustomerKeywords", System.currentTimeMillis() - startMilleSeconds, null);
        return modelAndView;
    }

    @RequiresPermissions("/internal/clientstatus/updateClientStatusTargetVersion")
    @RequestMapping(value = "/updateClientStatusTargetVersion", method = RequestMethod.POST)
    public ResponseEntity<?> updateClientStatusTargetVersion(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String targetVersion = (String) requestMap.get("targetVersion");
            clientStatusService.updateClientStatusTargetVersion(clientIDs, targetVersion);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
    
    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
    @RequestMapping(value = "/updateClientStatusTargetVPSPassword", method = RequestMethod.POST)
    public ResponseEntity<?> updateClientStatusTargetVPSPassword(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String targetVPSPassword = (String) requestMap.get("targetVPSPassword");
            clientStatusService.updateClientStatusTargetVPSPassword(clientIDs, targetVPSPassword);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/updateClientStatusRenewalDate")
    @RequestMapping(value = "/updateClientStatusRenewalDate", method = RequestMethod.POST)
    public ResponseEntity<?> updateClientStatusRenewalDate(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientIDs = (String) requestMap.get("clientIDs");
            String settingType = (String) requestMap.get("settingType");
            String renewalDate = (String) requestMap.get("renewalDate");
            clientStatusService.updateRenewalDate(clientIDs, settingType, renewalDate);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
    @RequestMapping(value = "/saveClientStatus", method = RequestMethod.POST)
    public ResponseEntity<?> saveClientStatus(@RequestBody ClientStatus clientStatus) {
        try {
            clientStatusService.saveClientStatus(clientStatus);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
    @RequestMapping(value = "/batchUpdateClientStatus", method = RequestMethod.POST)
    public ResponseEntity<?> batchUpdateClientStatus(@RequestBody ClientStatusBatchUpdateCriteria clientStatusBatchUpdateCriteria) {
        try {
            clientStatusService.batchUpdateClientStatus(clientStatusBatchUpdateCriteria);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/updateGroup")
    @RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
    public ResponseEntity<?> updateGroup(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientID = (String)requestMap.get("clientID");
            String group = (String)requestMap.get("group");
            clientStatusService.updateGroup(clientID, group);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/updateOperationType")
    @RequestMapping(value = "/updateOperationType", method = RequestMethod.POST)
    public ResponseEntity<?> updateOperationType(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientID = (String)requestMap.get("clientID");
            String operationType = (String)requestMap.get("operationType");
            clientStatusService.updateOperationType(clientID, operationType);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/updateUpgradeFailedReason")
    @RequestMapping(value = "/updateUpgradeFailedReason", method = RequestMethod.POST)
    public ResponseEntity<?> updateUpgradeFailedReason(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientID = (String)requestMap.get("clientID");
            String upgradeFailedReason = (String)requestMap.get("upgradeFailedReason");
            clientStatusService.updateUpgradeFailedReason(clientID, upgradeFailedReason);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getClientStatus/{clientID}", method = RequestMethod.POST)
    public ResponseEntity<?> getClientStatus(@PathVariable("clientID") String clientID, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            ClientStatus clientStatus = clientStatusService.getClientStatus(clientID, terminalType);
            return new ResponseEntity<Object>(clientStatus, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/deleteClientStatus")
    @RequestMapping(value = "/deleteClientStatus/{clientID}", method = RequestMethod.POST)
    public ResponseEntity<?> deleteClientStatus(@PathVariable("clientID") String clientID) {
        try {
            clientStatusService.deleteClientStatus(clientID);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/deleteClientStatuses")
    @RequestMapping(value = "/deleteClientStatuses", method = RequestMethod.POST)
    public ResponseEntity<?> deleteClientStatuses(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            clientStatusService.deleteAll(clientIDs);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/resetRestartStatusForProcessing")
    @RequestMapping(value = "/resetRestartStatusForProcessing", method = RequestMethod.POST)
    public ResponseEntity<?> resetRestartStatusForProcessing() {
        try {
            clientStatusService.resetRestartStatusForProcessing();
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/changeStatus")
    @RequestMapping(value = "/changeStatus/{clientID}", method = RequestMethod.POST)
    public ResponseEntity<?> changeStatus(@PathVariable("clientID") String clientID) {
        try {
            clientStatusService.changeStatus(clientID);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/uploadVNCFile")
    @RequestMapping(value = "/uploadVNCFile", method = RequestMethod.POST)
    public ResponseEntity<?> uploadVNCFile(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            clientStatusService.uploadVNCFile(file.getInputStream(), terminalType);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/uploadVPSFile")
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
            clientStatusService.uploadVPSFile(clientStatusType, downloadProgramType, targetFile, terminalType);
            FileUtil.delFolder(path);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/downloadVNCFile")
    @RequestMapping(value = "/downloadVNCFile", method = RequestMethod.POST)
    public ResponseEntity<?> downloadVNCFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            clientStatusService.getVNCFileInfo(TerminalTypeMapping.getTerminalType(request));
            downFile("vnc.zip");
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }

    }

    @RequiresPermissions("/internal/clientstatus/downloadFullVNCFile")
    @RequestMapping(value = "/downloadFullVNCFile", method = RequestMethod.POST)
    public ResponseEntity<?> downloadFullVNCFile(HttpServletRequest request, HttpServletResponse response) {
        try {
            clientStatusService.getFullVNCFileInfo(TerminalTypeMapping.getTerminalType(request));
            downFile("vncAll.zip");
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/clientStatusStat")
    @RequestMapping(value = "/clientStatusStat", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView clientStatusStat(String clientIDPrefix, String city, String switchGroupName, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("client/clientStatusStat");
        try {
            if(request.getMethod().equals("GET")){
                return modelAndView;
            }
            List<ClientStatusSummaryVO> clientStatusSummaryVOs = clientStatusService.searchClientStatusSummaryVO(clientIDPrefix, city, switchGroupName);
            modelAndView.addObject("clientIDPrefix", clientIDPrefix);
            modelAndView.addObject("city", city);
            modelAndView.addObject("switchGroupName", switchGroupName);
            modelAndView.addObject("clientStatusSummaryVOs", clientStatusSummaryVOs);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return modelAndView;
    }

    @RequiresPermissions("/internal/clientstatus/clientStatusGroupStat")
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
            List<ClientStatusGroupSummaryVO> clientStatusGroupSummaryVOs = clientStatusService.searchClientStatusGroupSummaryVO(group, terminalType);
            modelAndView.addObject("group", group);
            modelAndView.addObject("terminalType", terminalType);
            modelAndView.addObject("clientStatusGroupSummaryVOs", clientStatusGroupSummaryVOs);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return modelAndView;
    }

    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
    @RequestMapping(value = "/reopenClientStatus", method = RequestMethod.POST)
    public ResponseEntity<?> reopenClientStatus(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            String downloadProgramType = (String) requestMap.get("downloadProgramType");
            clientStatusService.reopenClientStatus(clientIDs, downloadProgramType);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequiresPermissions("/internal/clientstatus/saveClientStatus")
    @RequestMapping(value = "/updateStartUpStatusForCompleted", method = RequestMethod.POST)
    public ResponseEntity<?> updateStartUpStatusForCompleted(@RequestBody Map<String, Object> requestMap) {
        try {
            List<String> clientIDs = (List<String>) requestMap.get("clientIDs");
            clientStatusService.updateStartUpStatusForCompleted(clientIDs);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }
}
