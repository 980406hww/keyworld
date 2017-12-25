package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.PositiveListCriteria;
import com.keymanager.monitoring.entity.PositiveList;
import com.keymanager.monitoring.service.PositiveListService;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
import org.apache.commons.collections.CollectionUtils;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping(value = "/external/positivelist")
public class ExternalPositiveListRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalPositiveListRestController.class);

	@Autowired
	private PositiveListService positiveListService;

	@RequestMapping(value = "/savePositiveLists", method = RequestMethod.POST)
	public ResponseEntity<?> savePositiveLists(@RequestBody PositiveListCriteria positiveListCriteria, HttpServletRequest request) throws Exception{
		try {
			if (validUser(positiveListCriteria.getUserName(), positiveListCriteria.getPassword())) {
				String terminalType = TerminalTypeMapping.getTerminalType(request);
				for(PositiveList positiveList : positiveListCriteria.getPositiveLists()){
					positiveList.setTerminalType(terminalType);
					if(StringUtils.isNotEmpty(positiveList.getDesc())){
						String desc = positiveList.getDesc().replace("\n" , "").replace(" ", "");
						positiveList.setDesc(desc);
					}
				}
				positiveListService.savePositiveLists(positiveListCriteria.getPositiveLists() , positiveListCriteria.getOperationType());
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getSpecifiedKeywordPositiveLists", method = RequestMethod.POST)
	public ResponseEntity<?> getSpecifiedKeywordPositiveLists(@RequestBody PositiveListCriteria positiveListCriteria, HttpServletRequest request) throws Exception{
		try {
			if (validUser(positiveListCriteria.getUserName(), positiveListCriteria.getPassword())) {
				//String terminalType = TerminalTypeMapping.getTerminalType(request);
				List<PositiveList> positiveLists = positiveListService.getSpecifiedKeywordPositiveLists(null, positiveListCriteria.getKeyword());
				return new ResponseEntity<Object>(positiveLists, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
}
