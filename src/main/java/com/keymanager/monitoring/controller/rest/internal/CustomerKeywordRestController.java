package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.PortTerminalTypeMapping;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/customerkeyword")
public class CustomerKeywordRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(CustomerKeywordRestController.class);

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/clearTitle", method = RequestMethod.POST)
	public ResponseEntity<?> clearTitle(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception{
		String uuids = (String) requestMap.get("uuids");
		String customerUuid = (String) requestMap.get("customerUuid");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		try{
			customerKeywordService.clearTitle(uuids, customerUuid, terminalType);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/saveCustomerKeyword", method = RequestMethod.POST)
	public ResponseEntity<?> saveCustomerKeywordForm(@RequestBody List<CustomerKeyword> customerKeywords, HttpServletRequest request) {
		try{
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			String entryType = (String)request.getSession().getAttribute("entry");
			customerKeywordService.addCustomerKeywordsFromSimpleUI(customerKeywords, terminalType, entryType);
			return new ResponseEntity<Object>(true, HttpStatus.OK);
		}catch (Exception ex){
			logger.error(ex.getMessage());
			return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
		}
	}
}
