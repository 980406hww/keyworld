package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaiduIndexCriteria;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.criteria.CustomerCriteria;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.QZSettingService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.PortTerminalTypeMapping;
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
	private UserService userService;

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
}
