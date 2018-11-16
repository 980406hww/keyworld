package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.PositiveListCriteria;
import com.keymanager.monitoring.service.PositiveListService;
import com.keymanager.monitoring.vo.PositiveListVO;
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
@RequestMapping(value = "/external/positivelist")
public class ExternalPositiveListRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalPositiveListRestController.class);

	@Autowired
	private PositiveListService positiveListService;

	@RequestMapping(value = "/savePositiveLists", method = RequestMethod.POST)
	public ResponseEntity<?> savePositiveLists(@RequestBody PositiveListCriteria positiveListCriteria) {
		try {
			if (validUser(positiveListCriteria.getUserName(), positiveListCriteria.getPassword())) {
				positiveListService.savePositiveLists(positiveListCriteria.getPositiveListVOS(), positiveListCriteria.getOperationType(), positiveListCriteria.getBtnType(), positiveListCriteria.getUserName());
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getSpecifiedKeywordPositiveLists", method = RequestMethod.POST)
	public ResponseEntity<?> getSpecifiedKeywordPositiveLists(@RequestBody PositiveListCriteria positiveListCriteria) {
		try {
			if (validUser(positiveListCriteria.getUserName(), positiveListCriteria.getPassword())) {
				List<PositiveListVO> positiveListVOS = positiveListService.getSpecifiedKeywordPositiveLists(positiveListCriteria.getKeyword(), positiveListCriteria.getTerminalType());
				return new ResponseEntity<Object>(positiveListVOS, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
