package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.monitoring.service.VMwareService;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
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
    private UserService userService;

    @Autowired
    private VMwareService vMwareService;

    @Autowired
    private ClientStatusService clientStatusService;

    @RequestMapping(value = "/updatePageNo", method = RequestMethod.GET)
    public ResponseEntity<?> updatePageNo(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String pageNo = request.getParameter("pageNo");
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            clientStatusService.updatePageNo(clientID, Integer.parseInt(pageNo.trim()));
            return new ResponseEntity<Object>(1, HttpStatus.OK);
        } else {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                user.login(token);
                clientStatusService.updatePageNo(clientID, Integer.parseInt(pageNo.trim()));
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            } catch (UnknownAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (DisabledAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (IncorrectCredentialsException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (Throwable e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "/checkUpgrade", method = RequestMethod.GET)
    public ResponseEntity<?> checkUpgrade(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            String returnValue = clientStatusService.checkUpgrade(clientID);
            return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
        } else {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                user.login(token);
                String returnValue = clientStatusService.checkUpgrade(clientID);
                return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
            } catch (UnknownAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (DisabledAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (IncorrectCredentialsException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (Throwable e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "/getStoppedClientStatuses", method = RequestMethod.GET)
    public ResponseEntity<?> getStoppedClientStatuses(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String terminalType = TerminalTypeMapping.getTerminalType(request);
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            ClientStatus clientStatus = clientStatusService.getStoppedClientStatuses(terminalType);
            return new ResponseEntity<Object>(clientStatus, HttpStatus.OK);
        } else {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                user.login(token);
                ClientStatus clientStatus = clientStatusService.getStoppedClientStatuses(terminalType);
                return new ResponseEntity<Object>(clientStatus, HttpStatus.OK);
            } catch (UnknownAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (DisabledAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (IncorrectCredentialsException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (Throwable e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "/updateClientStatusRestartStatus", method = RequestMethod.GET)
    public ResponseEntity<?> updateClientStatusRestartStatus(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String clientID = request.getParameter("clientID");
        String status = request.getParameter("status");
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            clientStatusService.updateClientStatusRestartStatus(clientID, status);
            return new ResponseEntity<Object>(1, HttpStatus.OK);
        } else {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                user.login(token);
                clientStatusService.updateClientStatusRestartStatus(clientID, status);
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            } catch (UnknownAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (DisabledAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (IncorrectCredentialsException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (Throwable e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "/restartVPS", method = RequestMethod.GET)
    public ResponseEntity<?> restartVPS(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String vmName = request.getParameter("vmname");
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            vMwareService.restartVPS(vmName);
            return new ResponseEntity<Object>(1, HttpStatus.OK);
        } else {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                user.login(token);
                vMwareService.restartVPS(vmName);
                return new ResponseEntity<Object>(1, HttpStatus.OK);
            } catch (UnknownAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (DisabledAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (IncorrectCredentialsException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (Throwable e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            }
        }
    }

    @RequestMapping(value = "/getVPSStatus", method = RequestMethod.GET)
    public ResponseEntity<?> getVPSStatus(HttpServletRequest request) throws Exception {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        String vmName = request.getParameter("vmname");
        Subject user = SecurityUtils.getSubject();
        if (user.isAuthenticated()) {
            String status = vMwareService.getVPSStatus(vmName);
            return new ResponseEntity<Object>(status, HttpStatus.OK);
        } else {
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            try {
                user.login(token);
                String status = vMwareService.getVPSStatus(vmName);
                return new ResponseEntity<Object>(status, HttpStatus.OK);
            } catch (UnknownAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (DisabledAccountException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (IncorrectCredentialsException e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            } catch (Throwable e) {
                return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
            }
        }
    }
}
