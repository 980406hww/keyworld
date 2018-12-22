package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.ClientCookie;
import com.keymanager.monitoring.entity.Cookie;
import com.keymanager.monitoring.entity.Performance;
import com.keymanager.monitoring.service.CookieService;
import com.keymanager.monitoring.service.PerformanceService;
import com.keymanager.monitoring.vo.CookieVO;
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

@RestController
@RequestMapping(value = "/external/cookie")
public class ExternalCookieRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalCookieRestController.class);

	@Autowired
	private CookieService cookieService;

	@Autowired
	private PerformanceService performanceService;

	@RequestMapping(value = "/getCookieStrForClient", method = RequestMethod.POST)
	public ResponseEntity<?> getCookieStrForClient(@RequestBody CookieVO cookieVO, HttpServletRequest request) throws Exception{
		try {
			String userName = cookieVO.getUserName();
			String password = cookieVO.getPassword();
			if (validUser(userName, password)) {
				long startMilleSeconds = System.currentTimeMillis();
				ClientCookie clientCookie  = cookieService.getCookieStrForClient(cookieVO.getClientID());
				performanceService.addPerformanceLog("getCookieStrForClient", System.currentTimeMillis() - startMilleSeconds, "");
				return new ResponseEntity<Object>(clientCookie, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
