package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.service.QZChargeRuleService;
import com.keymanager.monitoring.service.QZOperationTypeService;
import com.keymanager.monitoring.service.QZSettingService;
import com.keymanager.monitoring.service.UserService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
			Subject user = SecurityUtils.getSubject();
			if (user.isAuthenticated()) {
				QZSetting qzSetting = qzSettingService.getAvailableQZSetting();
				return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
			} else {
				UsernamePasswordToken token = new UsernamePasswordToken(qzSettingCriteria.getUserName() , qzSettingCriteria.getPassword());
				try {
					user.login(token);
					QZSetting qzSetting = qzSettingService.getAvailableQZSetting();
					return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
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
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateQZKeywords", method = RequestMethod.POST)
	public ResponseEntity<?> updateQZKeywords(@RequestBody QZSettingCriteria qzSettingCriteria, HttpServletRequest request) throws Exception{
		if(qzSettingCriteria.getUserName() != null && qzSettingCriteria.getPassword() != null){
			Subject user = SecurityUtils.getSubject();
			if (user.isAuthenticated()) {
				String terminalType = TerminalTypeMapping.getTerminalType(request);
				qzSettingService.updateResult(qzSettingCriteria, terminalType);
				return new ResponseEntity<Object>(HttpStatus.OK);
			} else {
				UsernamePasswordToken token = new UsernamePasswordToken(qzSettingCriteria.getUserName() , qzSettingCriteria.getPassword());
				try {
					user.login(token);
					String terminalType = TerminalTypeMapping.getTerminalType(request);
					qzSettingService.updateResult(qzSettingCriteria, terminalType);
					return new ResponseEntity<Object>(HttpStatus.OK);
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
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getQZSettingsForCaptureCurrentKeyword", method = RequestMethod.POST)
	public ResponseEntity<?> getQZSettingsForCaptureCurrentKeyword(@RequestBody BaseCriteria baseCriteria) throws Exception{
		if(baseCriteria.getUserName() != null && baseCriteria.getPassword() != null){
			Subject user = SecurityUtils.getSubject();
			if (user.isAuthenticated()) {
				QZSetting qzSetting = qzSettingService.getQZSettingsForCaptureCurrentKeyword();
				return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
			} else {
				UsernamePasswordToken token = new UsernamePasswordToken(baseCriteria.getUserName() , baseCriteria.getPassword());
				try {
					user.login(token);
					QZSetting qzSetting = qzSettingService.getQZSettingsForCaptureCurrentKeyword();
					return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
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
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateCurrentKeywordCount", method = RequestMethod.POST)
	public ResponseEntity<?> updateCurrentKeywordCount(@RequestBody QZSettingCriteria qzSettingCriteria) throws Exception{
		if(qzSettingCriteria.getUserName() != null && qzSettingCriteria.getPassword() != null){
			Subject user = SecurityUtils.getSubject();
			if (user.isAuthenticated()) {
				qzSettingService.updateCurrentKeywordCount(qzSettingCriteria);
				return new ResponseEntity<Object>(HttpStatus.OK);
			} else {
				UsernamePasswordToken token = new UsernamePasswordToken(qzSettingCriteria.getUserName() , qzSettingCriteria.getPassword());
				try {
					user.login(token);
					qzSettingService.updateCurrentKeywordCount(qzSettingCriteria);
					return new ResponseEntity<Object>(HttpStatus.OK);
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
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
