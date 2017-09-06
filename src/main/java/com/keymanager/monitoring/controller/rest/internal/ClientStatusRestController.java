package com.keymanager.monitoring.controller.rest.internal;

import com.baomidou.mybatisplus.plugins.Page;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.ClientStatusCriteria;
import com.keymanager.monitoring.entity.ClientStatus;
import com.keymanager.monitoring.enums.TerminalTypeEnum;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.Constants;
import com.keymanager.util.PortTerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/clientStatus")
public class ClientStatusRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ClientStatusRestController.class);

	@Autowired
	private ClientStatusService clientStatusService;

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

	@RequestMapping(value = "/searchClientStatuses", method = RequestMethod.GET)
	public ModelAndView searchClientStatuses(@RequestParam(defaultValue = "1") int currentPageNumber, @RequestParam(defaultValue = "50") int pageSize, HttpServletRequest request) {
		return constructClientStatusModelAndView(request, new ClientStatusCriteria(), currentPageNumber, pageSize);
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
			return constructClientStatusModelAndView(request, clientStatusCriteria, Integer.parseInt(currentPageNumber), Integer.parseInt(pageSize));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new ModelAndView("/client/clientlist");
		}
	}

	private ModelAndView constructClientStatusModelAndView(HttpServletRequest request, ClientStatusCriteria clientStatusCriteria, int currentPageNumber, int pageSize) {
		ModelAndView modelAndView = new ModelAndView("/client/clientlist");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		clientStatusCriteria.setTerminalType(terminalType);
		Page<ClientStatus> page = clientStatusService.searchClientStatuses(new Page<ClientStatus>(currentPageNumber, pageSize), clientStatusCriteria);
		modelAndView.addObject("terminalType", terminalType);
		modelAndView.addObject("clientStatusCriteria", clientStatusCriteria);
		modelAndView.addObject("validMap", Constants.CLIENT_STATUS_VALID_MAP);
		modelAndView.addObject("page", page);
		return modelAndView;
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

	@RequestMapping(value = "/getClientStatus/{clientID}", method = RequestMethod.GET)
	public ResponseEntity<?> getClientStatus(@PathVariable("clientID") String clientID) {
		try {
			return new ResponseEntity<Object>(clientStatusService.getClientStatus(clientID), HttpStatus.OK);
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
}
