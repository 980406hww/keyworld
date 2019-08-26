package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.BaseCriteria;
import com.keymanager.monitoring.criteria.QZSettingCriteria;
import com.keymanager.monitoring.entity.QZSetting;
import com.keymanager.monitoring.service.QZSettingService;
import com.keymanager.monitoring.vo.ExternalQzSettingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/external/qzsetting")
public class ExternalQZSettingRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalQZSettingRestController.class);

	@Autowired
	private QZSettingService qzSettingService;

	@RequestMapping(value = "/getAvailableQZSetting", method = RequestMethod.POST)
	public ResponseEntity<?> getAvailableQZSetting(@RequestBody QZSettingCriteria qzSettingCriteria){
		try {
			if (validUser(qzSettingCriteria.getUserName(), qzSettingCriteria.getPassword())) {
				ExternalQzSettingVO externalQzSettingVO = qzSettingService.getAvailableQZSetting();
				return new ResponseEntity<Object>(externalQzSettingVO, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateQZKeywords", method = RequestMethod.POST)
	public ResponseEntity<?> updateQZKeywords(@RequestBody QZSettingCriteria qzSettingCriteria) throws Exception{
		try {
			if (validUser(qzSettingCriteria.getUserName(), qzSettingCriteria.getPassword())) {
				qzSettingService.updateResult(qzSettingCriteria);
				return new ResponseEntity<Object>(HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getQZSettingsForCaptureCurrentKeyword", method = RequestMethod.POST)
	public ResponseEntity<?> getQZSettingsForCaptureCurrentKeyword(@RequestBody BaseCriteria baseCriteria) throws Exception{
		try {
			if (validUser(baseCriteria.getUserName(), baseCriteria.getPassword())) {
				QZSetting qzSetting = qzSettingService.getQZSettingsForCaptureCurrentKeyword();
				return new ResponseEntity<Object>(qzSetting, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/updateCurrentKeywordCount", method = RequestMethod.POST)
	public ResponseEntity<?> updateCurrentKeywordCount(@RequestBody QZSettingCriteria qzSettingCriteria) throws Exception{
		try {
			if (validUser(qzSettingCriteria.getUserName(), qzSettingCriteria.getPassword())) {
				qzSettingService.updateCurrentKeywordCount(qzSettingCriteria);
				return new ResponseEntity<Object>(HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
