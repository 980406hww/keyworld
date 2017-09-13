package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaiduIndexCriteria;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.ClientStatusService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.PortTerminalTypeMapping;
import com.keymanager.value.CustomerKeywordForOptimization;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping(value = "/external/customerkeyword")
public class ExternalCustomerKeywordRestController extends SpringMVCBaseController {

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private ClientStatusService clientStatusService;

	@Autowired
	private UserService userService;

	private String getIP(HttpServletRequest request){
		String ip = request.getHeader("X-Forwarded-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}

		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	@RequestMapping(value = "/getKeywordForCaptureTitle", method = RequestMethod.POST)
	public ResponseEntity<?> getKeywordForCaptureTitle(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception{
		String groupName = (String) requestMap.get("group");
		String userName = (String) requestMap.get("userName");
		String password = (String) requestMap.get("password");
		if(userName != null && password != null){
			User user = userService.getUser(userName);
			if(user != null && user.getPassword().equals(password)){
				String returnValue = "";
				String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
				if(StringUtils.isEmpty(groupName)) {
					returnValue = customerKeywordService.searchCustomerKeywordForCaptureTitle(terminalType);
				}else{
					returnValue = customerKeywordService.searchCustomerKeywordForCaptureTitle(groupName, terminalType);
				}
				return new ResponseEntity<Object>(StringUtils.isEmpty(returnValue) ? "{}" : returnValue, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getCustomerKeywordsForCaptureIndex" , method = RequestMethod.POST)
	public ResponseEntity<?> getCustomerKeywordsForCaptureIndex(@RequestBody BaseCriteria baseCriteria) throws Exception{
		if(baseCriteria.getUserName() != null && baseCriteria.getPassword() != null){
			User user = userService.getUser(baseCriteria.getUserName());
			if(user != null && user.getPassword().equals(baseCriteria.getPassword())){
				CustomerKeyword customerKeyword = customerKeywordService.getCustomerKeywordsForCaptureIndex();
				return new ResponseEntity<Object>(customerKeyword, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateKeywordIndex", method = RequestMethod.POST)
	public ResponseEntity<?> updateKeywordIndex(@RequestBody BaiduIndexCriteria baiduIndexCriteria) throws Exception{
		if(baiduIndexCriteria.getUserName() != null && baiduIndexCriteria.getPassword() != null){
			User user = userService.getUser(baiduIndexCriteria.getUserName());
			if(user != null && user.getPassword().equals(baiduIndexCriteria.getPassword())){
				customerKeywordService.updateCustomerKeywordIndex(baiduIndexCriteria);
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getGroups", method = RequestMethod.POST)
	public ResponseEntity<?> getGroups(@RequestBody BaseCriteria baseCriteria) throws Exception{
		if(baseCriteria.getUserName() != null && baseCriteria.getPassword() != null){
			User user = userService.getUser(baseCriteria.getUserName());
			if(user != null && user.getPassword().equals(baseCriteria.getPassword())){
				List<String> groups = customerKeywordService.getGroups();
				return new ResponseEntity<Object>(groups, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getCustomerKeyword", method = RequestMethod.GET)
	public ResponseEntity<?> getCustomerKeywordForOptimization(HttpServletRequest request) throws Exception{
		String clientID = request.getParameter("clientID");
		String userName = request.getParameter("userName");
		String password = request.getParameter("password");
		String version = request.getParameter("version");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		if(userName != null && password != null){
			User user = userService.getUser(userName);
			if(user != null && user.getPassword().equals(password)){
				CustomerKeywordForOptimization customerKeywordForOptimization = customerKeywordService.searchCustomerKeywordsForOptimization(terminalType, clientID, version);
				clientStatusService.updateClientVersion(clientID, version);
				return new ResponseEntity<Object>(customerKeywordForOptimization, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateOptimizedCount", method = RequestMethod.GET)
	public ResponseEntity<?> updateOptimizedCount(HttpServletRequest request) throws Exception{
		String userName = request.getParameter("username");
		String password = request.getParameter("password");

		Long customerKeywordUuid = Long.parseLong(request.getParameter("uuid").trim());
		String count = request.getParameter("count");
		String clientID = request.getParameter("clientID");
		String freeSpace = request.getParameter("freespace");
		String version = request.getParameter("version");
		String city = request.getParameter("city");
		String status = request.getParameter("status");

		String ip = getIP(request);
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());

		if(userName != null && password != null){
			User user = userService.getUser(userName);
			if(user != null && user.getPassword().equals(password)){
				customerKeywordService.updateOptimizationResult(terminalType, customerKeywordUuid, Integer.parseInt(count.trim()), ip, city, clientID,
						status, freeSpace, version);
				return new ResponseEntity<Object>(1, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(0, HttpStatus.BAD_REQUEST);
	}
}
