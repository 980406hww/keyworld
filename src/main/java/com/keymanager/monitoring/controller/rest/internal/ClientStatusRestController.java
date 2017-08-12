package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.PortTerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/clientStatus")
public class ClientStatusRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ClientStatusRestController.class);

	@Autowired
	private ClientStatusService clientStatusService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/changeTerminalType", method = RequestMethod.POST)
	public ResponseEntity<?> changeTerminalType(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception{
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		String clientID = (String) requestMap.get("clientID");
		try{
			clientStatusService.changeTerminalType(clientID, TerminalTypeEnum.PC.name().equals(terminalType) ? TerminalTypeEnum.Phone.name() :
					TerminalTypeEnum.PC.name());
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception ex){
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}
}
