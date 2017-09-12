package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.common.email.MimeMailService;
import com.keymanager.monitoring.common.sms.SmsService;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserRestController extends SpringMVCBaseController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private MimeMailService mimeMailService;
	
	@Autowired
	private SmsService smsService;
	
	@RequestMapping(value = "/getCurrentUserInfo", method = RequestMethod.GET)
	public ResponseEntity<?> getCurrentUserInfo(){
		String currentUserId = getCurrentUser().getLoginName();
		User user = userService.selectById(currentUserId);
		if(user != null){
			return new ResponseEntity<Object>(user, HttpStatus.OK);
		}else{
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
}
