package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.monitoring.service.VMwareService;
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
		if(userName != null && password != null) {
			User user = userService.getUser(userName);
			if (user != null && user.getPassword().equals(password)) {
				try {
					clientStatusService.updatePageNo(clientID, Integer.parseInt(pageNo.trim()));
					return new ResponseEntity<Object>(1, HttpStatus.OK);
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
		}
		return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/checkUpgrade", method = RequestMethod.GET)
	public ResponseEntity<?> checkUpgrade(HttpServletRequest request) throws Exception {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String clientID = request.getParameter("clientID");
		if(userName != null && password != null) {
			User user = userService.getUser(userName);
			if (user != null && user.getPassword().equals(password)) {
				try {
					String returnValue = clientStatusService.checkUpgrade(clientID);
					return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
		}
		return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getStoppedClientStatuses", method = RequestMethod.GET)
	public ResponseEntity<?> getStoppedClientStatuses(HttpServletRequest request) throws Exception {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String terminalType = TerminalTypeMapping.getTerminalType(request);
		if(userName != null && password != null) {
			User user = userService.getUser(userName);
			if (user != null && user.getPassword().equals(password)) {
				try {
					ClientStatus clientStatus = clientStatusService.getStoppedClientStatuses(terminalType);
					return new ResponseEntity<Object>(clientStatus, HttpStatus.OK);
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
		}
		return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateClientStatusRestartStatus", method = RequestMethod.GET)
	public ResponseEntity<?> updateClientStatusRestartStatus(HttpServletRequest request) throws Exception {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String clientID = request.getParameter("clientID");
		String status = request.getParameter("status");
		if(userName != null && password != null) {
			User user = userService.getUser(userName);
			if (user != null && user.getPassword().equals(password)) {
				try {
					clientStatusService.updateClientStatusRestartStatus(clientID, status);
					return new ResponseEntity<Object>(1, HttpStatus.OK);
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
		}
		return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/restartVPS", method = RequestMethod.GET)
	public ResponseEntity<?> restartVPS(HttpServletRequest request) throws Exception {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String vmName = request.getParameter("vmname");
		if(userName != null && password != null) {
			User user = userService.getUser(userName);
			if (user != null && user.getPassword().equals(password)) {
				try {
					vMwareService.restartVPS(vmName);
					return new ResponseEntity<Object>(1, HttpStatus.OK);
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
		}
		return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getVPSStatus", method = RequestMethod.GET)
	public ResponseEntity<?> getVPSStatus(HttpServletRequest request) throws Exception {
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String vmName = request.getParameter("vmname");
		if(userName != null && password != null) {
			User user = userService.getUser(userName);
			if (user != null && user.getPassword().equals(password)) {
				try {
					String status = vMwareService.getVPSStatus(vmName);
					return new ResponseEntity<Object>(status, HttpStatus.OK);
				} catch (Exception ex) {
					logger.error(ex.getMessage());
				}
			}
		}
		return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
	}
}
