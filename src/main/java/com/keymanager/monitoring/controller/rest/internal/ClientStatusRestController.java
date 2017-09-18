package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.value.ClientStatusGroupSummaryVO;
import com.keymanager.value.ClientStatusSummaryVO;
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

@RestController
@RequestMapping(value = "/internal/clientstatus")
public class ClientStatusRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ClientStatusRestController.class);

    @Autowired
    private ClientStatusService clientStatusService;

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

    @RequestMapping(value = "/searchClientStatuses", method = RequestMethod.GET)
    public ModelAndView searchClientStatuses(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request, boolean clientStatusFlag) {
        return constructClientStatusModelAndView(request, new ClientStatusCriteria(), currentPageNumber, pageSize, true);
    }

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

    @RequestMapping(value = "/searchBadClientStatus", method = RequestMethod.POST)
    public ModelAndView searchBadClientStatus(HttpServletRequest request, ClientStatusCriteria clientStatusCriteria) {
        try {
            return constructClientStatusModelAndView(request, clientStatusCriteria, 1, 50, false);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ModelAndView("/client/list");
        }
    }

    private ModelAndView constructClientStatusModelAndView(HttpServletRequest request, ClientStatusCriteria clientStatusCriteria, int currentPageNumber, int pageSize, boolean clientSatausFlag) {
        ModelAndView modelAndView = new ModelAndView("/client/list");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        clientStatusCriteria.setTerminalType(terminalType);
        Page<ClientStatus> page = clientStatusService.searchClientStatuses(new Page<ClientStatus>(currentPageNumber, pageSize), clientStatusCriteria, clientSatausFlag);
        String[] operationTypeValues = Constants.pcOperationTypeValues;
        if (TerminalTypeEnum.Phone.name().equals(terminalType)) {
            operationTypeValues = Constants.phoneOperationTypeValues;
        }
        modelAndView.addObject("terminalType", terminalType);
        modelAndView.addObject("clientStatusCriteria", clientStatusCriteria);
        modelAndView.addObject("validMap", Constants.CLIENT_STATUS_VALID_MAP);
        modelAndView.addObject("orderByMap", Constants.CLIENT_STATUS_ORDERBY_MAP);
        modelAndView.addObject("operationTypeValues", operationTypeValues);
        modelAndView.addObject("page", page);
        return modelAndView;
    }

    @RequestMapping(value = "/updateClientStatusTargetVersion", method = RequestMethod.POST)
    public ResponseEntity<?> updateClientStatusTargetVersion(@RequestBody Map<String, Object> requestMap) {
        try {
            String clientIDs = (String) requestMap.get("clientIDs");
            String targetVersion = (String) requestMap.get("targetVersion");
            clientStatusService.updateClientStatusTargetVersion(clientIDs, targetVersion);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

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

    @RequestMapping(value = "/addClientStatus", method = RequestMethod.POST)
    public ResponseEntity<?> addClientStatus(@RequestBody ClientStatus clientStatus) {
        try {
            clientStatusService.addClientStatus(clientStatus);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateGroup", method = RequestMethod.POST)
    public ResponseEntity<?> updateGroup(@RequestBody ClientStatus clientStatus) {
        try {
            clientStatusService.updateGroup(clientStatus);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateOperationType", method = RequestMethod.POST)
    public ResponseEntity<?> updateOperationType(@RequestBody ClientStatus clientStatus) {
        try {
            clientStatusService.updateOperationType(clientStatus);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/updateUpgradeFailedReason", method = RequestMethod.POST)
    public ResponseEntity<?> updateUpgradeFailedReason(@RequestBody ClientStatus clientStatus) {
        try {
            clientStatusService.updateUpgradeFailedReason(clientStatus);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/getClientStatus/{clientID}", method = RequestMethod.GET)
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

    @RequestMapping(value = "/changeMonitorType/{clientID}", method = RequestMethod.POST)
    public ResponseEntity<?> changeMonitorType(@PathVariable("clientID") String clientID) {
        try {
            clientStatusService.changeMonitorType(clientID);
            return new ResponseEntity<Object>(true, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/uploadVNCFile", method = RequestMethod.POST)
    public boolean uploadVNCFile(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
        try {
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            clientStatusService.uploadVNCFile(file.getInputStream(), terminalType);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    @RequestMapping(value = "/downloadVNCFile", method = RequestMethod.POST)
    public void downloadVNCFile(HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            response.setContentType("application/octet-stream");
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String filedownload = clientStatusService.getVNCFileInfo(terminalType);
            String filedisplay = "vnc.zip";
            response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
            outputStream = response.getOutputStream();
            fileInputStream = new FileInputStream(filedownload);

            byte[] b = new byte[1024];
            int i = 0;
            while ((i = fileInputStream.read(b)) > 0) {
                outputStream.write(b, 0, i);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    fileInputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/downloadFullVNCFile", method = RequestMethod.POST)
    public void downloadFullVNCFile(HttpServletRequest request, HttpServletResponse response) {
        OutputStream outputStream = null;
        FileInputStream fileInputStream = null;
        try {
            response.setContentType("application/octet-stream");
            String terminalType = TerminalTypeMapping.getTerminalType(request);
            String filedownload = clientStatusService.getFullVNCFileInfo(terminalType);
            String filedisplay = "vncAll.zip";
            response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);
            outputStream = response.getOutputStream();
            fileInputStream = new FileInputStream(filedownload);

            byte[] b = new byte[1024];
            int i = 0;
            while ((i = fileInputStream.read(b)) > 0) {
                outputStream.write(b, 0, i);
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                    fileInputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @RequestMapping(value = "/clientStatusStat", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView clientStatusStat(String clientIDPrefix, String city, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("client/clientStatusStat");
        try {
            if (clientIDPrefix != null) {
                clientIDPrefix = clientIDPrefix.trim();
            }
            if (city != null) {
                city = city.trim();
            }
            if (request.getMethod().equals("POST")) {
                List<ClientStatusSummaryVO> clientStatusSummaryVOs = clientStatusService.searchClientStatusSummaryVO(clientIDPrefix, city);
                modelAndView.addObject("clientIDPrefix", clientIDPrefix);
                modelAndView.addObject("city", city);
                modelAndView.addObject("clientStatusSummaryVOs", clientStatusSummaryVOs);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/clientStatusGroupStat", method = {RequestMethod.POST, RequestMethod.GET})
    public ModelAndView clientStatusGroupStat(String group, String terminalType, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("client/clientStatusGroupStat");
        try {
            if (null != group) {
                group = group.trim();
            }
            if (request.getMethod().equals("POST")) {
                List<ClientStatusGroupSummaryVO> clientStatusGroupSummaryVOs = clientStatusService.searchClientStatusGroupSummaryVO(group, terminalType);
                modelAndView.addObject("group", group);
                modelAndView.addObject("terminalType", terminalType);
                modelAndView.addObject("clientStatusGroupSummaryVOs", clientStatusGroupSummaryVOs);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return modelAndView;
    }
}
