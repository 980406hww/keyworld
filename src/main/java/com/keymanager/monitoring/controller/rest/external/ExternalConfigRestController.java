package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.util.Constants;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/external/config")
public class ExternalConfigRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalConfigRestController.class);

	@Autowired
	private ConfigService configService;

	@RequestMapping(value = "/getZipEncryptionPassword", method = RequestMethod.POST)
	public ResponseEntity<?> getZipEncryptionPassword(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception{
		try {
			String userName = (String) requestMap.get("userName");
			String password = (String) requestMap.get("password");
			if (validUser(userName, password)) {
				Config config = configService.getConfig(Constants.CONFIG_TYPE_ZIP_ENCRYPTION, Constants.CONFIG_KEY_PASSWORD);
				return new ResponseEntity<Object>(config, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getOptimizeWayAndNewsSource", method = RequestMethod.POST)
	public ResponseEntity<?> getOptimizeWayAndNewsSource(@RequestBody Map<String, Object> requestMap) {
		try {
			String userName = (String) requestMap.get("userName");
			String password = (String) requestMap.get("password");
			if (validUser(userName, password)) {
				List<Config> configs = configService.findConfigs(Constants.CONFIG_TYPE_OPTIMIZE_WAY_GROUPNAME);
				return new ResponseEntity<Object>(configs, HttpStatus.OK);
			}
		} catch(Exception ex) {
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
