package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.QZChargeRuleService;
import com.keymanager.monitoring.service.QZOperationTypeService;
import com.keymanager.monitoring.service.QZSettingService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.PortTerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/external/qzsetting")
public class ExternalQZSettingRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalQZSettingRestController.class);

	@Autowired
	private QZSettingService qzSettingService;

	@Autowired
	private QZChargeRuleService qzChargeRuleService;

	@Autowired
	private QZOperationTypeService qzOperationTypeService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/getAvailableQZSetting", method = RequestMethod.POST)
	public ResponseEntity<?> getAvailableQZSetting(@RequestBody QZSettingCriteria qzSettingCriteria) throws Exception{
		if(qzSettingCriteria.getUserName() != null && qzSettingCriteria.getPassword() != null){
			User user = userService.getUser(qzSettingCriteria.getUserName());
			if(user != null && user.getPassword().equals(qzSettingCriteria.getPassword())){
				QZSetting qzSetting = qzSettingService.getAvailableQZSetting();
				return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateQZKeywords", method = RequestMethod.POST)
	public ResponseEntity<?> updateQZKeywords(@RequestBody QZSettingCriteria qzSettingCriteria, HttpServletRequest request) throws Exception{
		if(qzSettingCriteria.getUserName() != null && qzSettingCriteria.getPassword() != null){
			User user = userService.getUser(qzSettingCriteria.getUserName());
			if(user != null && user.getPassword().equals(qzSettingCriteria.getPassword())){
				String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
				qzSettingService.updateResult(qzSettingCriteria, terminalType);
				return new ResponseEntity<Object>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getQZSettingsForCaptureCurrentKeyword", method = RequestMethod.POST)
	public ResponseEntity<?> getQZSettingsForCaptureCurrentKeyword(@RequestBody QZSettingCriteria qzSettingCriteria) throws Exception{
		if(qzSettingCriteria.getUserName() != null && qzSettingCriteria.getPassword() != null){
			User user = userService.getUser(qzSettingCriteria.getUserName());
			if(user != null && user.getPassword().equals(qzSettingCriteria.getPassword())){
				QZSetting qzSetting = qzSettingService.getQZSettingsForCaptureCurrentKeyword();
				return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateCurrentKeywordCount", method = RequestMethod.POST)
	public ResponseEntity<?> updateCurrentKeywordCount(@RequestBody QZSettingCriteria qzSettingCriteria) throws Exception{
		if(qzSettingCriteria.getUserName() != null && qzSettingCriteria.getPassword() != null){
			User user = userService.getUser(qzSettingCriteria.getUserName());
			if(user != null && user.getPassword().equals(qzSettingCriteria.getPassword())){
				qzSettingService.updateCurrentKeywordCount(qzSettingCriteria);
				return new ResponseEntity<Object>(HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
