package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.WarnListCriteria;
import com.keymanager.monitoring.entity.WarnList;
import com.keymanager.monitoring.service.WarnListService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/external/warnlist")
public class ExternalWarnListRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalWarnListRestController.class);

	@Autowired
	private WarnListService warnListService;

	@RequestMapping(value = "/saveWarnLists", method = RequestMethod.POST)
	public ResponseEntity<?> savewWarnLists(@RequestBody WarnListCriteria warnListCriteria) throws Exception{
		try {
			if (validUser(warnListCriteria.getUserName(), warnListCriteria.getPassword())) {
				warnListService.saveWarnLists(warnListCriteria.getWarnLists() , warnListCriteria.getOperationType());
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getSpecifiedKeywordWarnLists", method = RequestMethod.POST)
	public ResponseEntity<?> getSpecifiedKeywordWarnLists(@RequestBody WarnListCriteria warnListCriteria) throws Exception{
		try {
			if (validUser(warnListCriteria.getUserName(), warnListCriteria.getPassword())) {
				List<WarnList> warnLists = warnListService.getSpecifiedKeywordWarnLists(warnListCriteria.getKeyword());
				return new ResponseEntity<Object>(warnLists, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
