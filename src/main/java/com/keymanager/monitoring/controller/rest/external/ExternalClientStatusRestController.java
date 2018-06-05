package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.VMwareService;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
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
public class ExternalClientStatusRestController extends SpringMVCBaseController {
    private static Logger logger = LoggerFactory.getLogger(ExternalClientStatusRestController.class);

    @Autowired
    private VMwareService vMwareService;

    @Autowired
    private ClientStatusService clientStatusService;

    @Autowired
    private ConfigService configService;

    @RequestMapping(value = "/updatePageNo", method = RequestMethod.GET)
    public ResponseEntity<?> updatePageNo(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String pageNo = request.getParameter("pageNo");
        try {
            if (validUser(userName, password)) {
                clientStatusService.updatePageNo(clientID, Integer.parseInt(pageNo.trim()));
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            ex.printStackTrace();
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/checkUpgrade", method = RequestMethod.GET)
    public ResponseEntity<?> checkUpgrade(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        try {
            if (validUser(userName, password)) {
                String returnValue = clientStatusService.checkUpgrade(clientID);
                return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value = "/checkPassword", method = RequestMethod.GET)
    public ResponseEntity<?> checkPassword(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        try {
            if (validUser(userName, password)) {
                String returnValue = clientStatusService.checkPassword(clientID);
                return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value = "/updatePassword", method = RequestMethod.GET)
    public ResponseEntity<?> updatePassword(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        try {
            if (validUser(userName, password)) {
                String returnValue = clientStatusService.updatePassword(clientID);
                return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getStoppedClientStatuses", method = RequestMethod.GET)
    public ResponseEntity<?> getStoppedClientStatuses(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            if (validUser(userName, password)) {
                ClientStatus clientStatus = clientStatusService.getStoppedClientStatuses();
                return new ResponseEntity<Object>(clientStatus, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateClientStatusRestartStatus", method = RequestMethod.GET)
    public ResponseEntity<?> updateClientStatusRestartStatus(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String status = request.getParameter("status");
        try {
            if (validUser(userName, password)) {
                clientStatusService.updateClientStatusRestartStatus(clientID, status);
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/restartVPS", method = RequestMethod.GET)
    public ResponseEntity<?> restartVPS(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String vmName = request.getParameter("vmname");
        try {
            if (validUser(userName, password)) {
                vMwareService.restartVPS(vmName);
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getVPSStatus", method = RequestMethod.GET)
    public ResponseEntity<?> getVPSStatus(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String vmName = request.getParameter("vmname");
        try {
            if (validUser(userName, password)) {
                String status = vMwareService.getVPSStatus(vmName);
                return new ResponseEntity<Object>(status, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getClientStatusForStartUp", method = RequestMethod.GET)
    public ResponseEntity<?> getClientStatusForStartUp(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        try {
            if (validUser(userName, password)) {
                Integer downloadingClientCount = clientStatusService.getDownloadingClientCount();
                Config config = configService.getConfig(Constants.CONFIG_TYPE_START_UP, Constants.CONFIG_KEY_DOWNLOADING_CLIENT_COUNT);
                int maxDownloadingClientCount = Integer.parseInt(config.getValue());
                if(downloadingClientCount >= maxDownloadingClientCount) {
                    return new ResponseEntity<Object>(null, HttpStatus.OK);
                } else {
                    ClientStatus clientStatus = clientStatusService.getClientStatusForStartUp();
                    return new ResponseEntity<Object>(clientStatus, HttpStatus.OK);
                }
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/getClientStartUpStatus", method = RequestMethod.GET)
    public ResponseEntity<?> getClientStartUpStatus(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        try {
            if (validUser(userName, password)) {
                String clientOpenStatus = clientStatusService.getClientStartUpStatus(clientID);
                return new ResponseEntity<Object>(clientOpenStatus, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/updateClientStartUpStatus", method = RequestMethod.GET)
    public ResponseEntity<?> updateClientStartUpStatus(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String status = request.getParameter("status");
        try {
            if (validUser(userName, password)) {
                clientStatusService.updateClientStartUpStatus(clientID, status);
                return new ResponseEntity<Object>(true, HttpStatus.OK);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
    }
}
