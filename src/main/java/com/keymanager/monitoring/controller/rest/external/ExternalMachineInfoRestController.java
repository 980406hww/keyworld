package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.common.utils.StringUtils;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.MachineInfo;
import com.keymanager.monitoring.service.MachineInfoService;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.PerformanceService;
import com.keymanager.monitoring.service.VMwareService;
import com.keymanager.monitoring.vo.ClientStatusForOptimization;
import com.keymanager.util.AESUtils;
import com.keymanager.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/external/clientstatus")
public class ExternalMachineInfoRestController extends SpringMVCBaseController {

    private static Logger logger = LoggerFactory.getLogger(ExternalMachineInfoRestController.class);

    @Autowired
    private VMwareService vMwareService;

    @Autowired
    private MachineInfoService machineInfoService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private PerformanceService performanceService;

    @RequestMapping(value = "/updatePageNo", method = RequestMethod.GET)
    public ResponseEntity<?> updatePageNo(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String pageNo = request.getParameter("pageNo");
        try {
            if (validUser(userName, password)) {
                machineInfoService.updatePageNo(clientID, Integer.parseInt(pageNo.trim()));
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/checkUpgrade", method = RequestMethod.GET)
    public ResponseEntity<?> checkUpgrade(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        try {
            if (validUser(userName, password)) {
                String returnValue = machineInfoService.checkUpgrade(clientID);
                return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/checkPassword", method = RequestMethod.GET)
    public ResponseEntity<?> checkPassword(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        try {
            if (validUser(userName, password)) {
                String returnValue = machineInfoService.checkPassword(clientID);
                return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
    public ResponseEntity<?> updatePassword(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        try {
            if (validUser(userName, password)) {
                String returnValue = machineInfoService.updatePassword(clientID);
                return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getStoppedClientStatuses", method = RequestMethod.GET)
    public ResponseEntity<?> getStoppedClientStatuses(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        try {
            if (validUser(userName, password)) {
                MachineInfo machineInfo = machineInfoService.getStoppedMachineInfo();
                return new ResponseEntity<Object>(machineInfo, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getStoppedClientStatusesZip", method = RequestMethod.GET)
    public ResponseEntity<?> getStoppedClientStatusesZip(HttpServletRequest request)
        throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        try {
            if (validUser(userName, password)) {
                MachineInfo machineInfo = machineInfoService.getStoppedMachineInfo();
                byte[] compress = AESUtils.compress(AESUtils.encrypt(machineInfo).getBytes());
                String s = AESUtils.parseByte2HexStr(compress);
                return new ResponseEntity<Object>(s, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateClientStatusRestartStatus", method = RequestMethod.GET)
    public ResponseEntity<?> updateClientStatusRestartStatus(HttpServletRequest request)
        throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String status = request.getParameter("status");
        try {
            if (validUser(userName, password)) {
                machineInfoService.updateMachineInfoRestartStatus(clientID, status);
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/restartVPS", method = RequestMethod.GET)
    public ResponseEntity<?> restartVPS(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String vmName = request.getParameter("vmname");
        try {
            if (validUser(userName, password)) {
                vMwareService.restartVPS(vmName);
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getVPSStatus", method = RequestMethod.GET)
    public ResponseEntity<?> getVPSStatus(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String vmName = request.getParameter("vmname");
        try {
            if (validUser(userName, password)) {
                String status = vMwareService.getVPSStatus(vmName);
                return new ResponseEntity<Object>(status, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getClientStatusForStartUp", method = RequestMethod.GET)
    public ResponseEntity<?> getClientStatusForStartUp(HttpServletRequest request)
        throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        try {
            if (validUser(userName, password)) {
                Integer downloadingClientCount = machineInfoService.getDownloadingMachineCount();
                Config config = configService.getConfig(Constants.CONFIG_TYPE_START_UP,
                    Constants.CONFIG_KEY_DOWNLOADING_CLIENT_COUNT);
                int maxDownloadingClientCount = Integer.parseInt(config.getValue());
                if (downloadingClientCount >= maxDownloadingClientCount) {
                    return new ResponseEntity<Object>(null, HttpStatus.OK);
                } else {
                    MachineInfo machineInfo = machineInfoService.getMachineInfoForStartUp();
                    return new ResponseEntity<Object>(machineInfo, HttpStatus.OK);
                }
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getClientStartUpStatus", method = RequestMethod.GET)
    public ResponseEntity<?> getClientStartUpStatus(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        try {
            if (validUser(userName, password)) {
                long startMilleSeconds = System.currentTimeMillis();
                String clientOpenStatus = machineInfoService.getMachineStartUpStatus(clientID);
                performanceService.addPerformanceLog("getClientStartUpStatus",
                    System.currentTimeMillis() - startMilleSeconds, "");
                return new ResponseEntity<Object>(clientOpenStatus, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateClientStartUpStatus", method = RequestMethod.GET)
    public ResponseEntity<?> updateClientStartUpStatus(HttpServletRequest request)
        throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String status = request.getParameter("status");
        try {
            if (validUser(userName, password)) {
                machineInfoService.updateMachineStartUpStatus(clientID, status);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getClientStatusID", method = RequestMethod.GET)
    public ResponseEntity<?> getClientStartID(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String computerID = request.getParameter("computerID");
        try {
            if (validUser(userName, password)) {
                String clientStatusID = machineInfoService.getMachineInfoID(computerID);
                return new ResponseEntity<Object>(clientStatusID, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }


    @RequestMapping(value = "/updateVersion", method = RequestMethod.GET)
    public ResponseEntity<?> updateVersion(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String version = request.getParameter("version");
        try {
            if (validUser(userName, password)) {
                machineInfoService.updateVersion(clientID, version);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getClientStatusZip", method = RequestMethod.GET)
    public ResponseEntity<?> getClientStart(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        try {
            if (validUser(userName, password)) {
                ClientStatusForOptimization clientStatus = machineInfoService
                    .getClientStatusForOptimization(clientID);
                byte[] compress = AESUtils.compress(AESUtils.encrypt(clientStatus).getBytes());
                String s = AESUtils.parseByte2HexStr(compress);
                return new ResponseEntity<Object>(s, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateMachine", method = RequestMethod.GET)
    public ResponseEntity<?> updateMachine(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("userName");
        if (StringUtils.isBlank(userName)) {
            userName = request.getParameter("username");
        }
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String city = request.getParameter("city");
        String version = request.getParameter("version");
        String freeSpace = request.getParameter("freeSpace");
        String runningProgramType = request.getParameter("runningProgramType");

        int cpuCount = request.getParameter("cpuCount") == null ? 0
            : Integer.parseInt(request.getParameter("cpuCount"));
        int memory = request.getParameter("memory") == null ? 0
            : Integer.parseInt(request.getParameter("memory"));

        try {
            if (validUser(userName, password)) {
                machineInfoService
                    .updateMachine(clientID, city, version, freeSpace, runningProgramType,cpuCount,memory);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        } catch (Exception ex) {
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }


}
