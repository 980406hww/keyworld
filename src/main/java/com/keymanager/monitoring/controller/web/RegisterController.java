package com.keymanager.monitoring.controller.web;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/register")
public class RegisterController extends SpringMVCBaseController {
	
	@Autowired
	private IUserInfoService userInfoService;
}
