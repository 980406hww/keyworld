package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.CaptureRealUrlCriteria;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.CaptureRealUrlService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.monitoring.vo.BaiduUrl;
import com.keymanager.util.TerminalTypeMapping;
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

	@RequestMapping(value = "/getRealUrls", method = RequestMethod.POST)
	public ResponseEntity<?> getRealUrls(@RequestBody CaptureRealUrlCriteria captureRealUrlCriteria) throws Exception{
		try {
			if (validUser(captureRealUrlCriteria.getUserName(), captureRealUrlCriteria.getUserName())) {
				BaiduUrl baiduUrl = captureRealUrlService.fetch(captureRealUrlCriteria.getSourceUrl());
				return new ResponseEntity<Object>(baiduUrl, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
