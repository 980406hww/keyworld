package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CaptureRealUrlCriteria;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.CaptureRealUrlService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.monitoring.vo.BaiduUrl;
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
@RequestMapping(value = "/external/support")
public class ExternalSupportRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalSupportRestController.class);

	@Autowired
	private CaptureRealUrlService captureRealUrlService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/getRealUrls", method = RequestMethod.POST)
	public ResponseEntity<?> getRealUrls(@RequestBody CaptureRealUrlCriteria captureRealUrlCriteria) throws Exception{
		if(captureRealUrlCriteria.getUserName() != null && captureRealUrlCriteria.getPassword() != null){
			User user = userService.getUser(captureRealUrlCriteria.getUserName());
			if(user != null && user.getPassword().equals(captureRealUrlCriteria.getPassword())){
				BaiduUrl baiduUrl = captureRealUrlService.fetch(captureRealUrlCriteria.getSourceUrl());
				return new ResponseEntity<Object>(baiduUrl, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
