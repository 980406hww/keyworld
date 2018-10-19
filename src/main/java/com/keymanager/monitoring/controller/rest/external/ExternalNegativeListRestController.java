package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.KeywordNegativeCriteria;
import com.keymanager.monitoring.criteria.NegativeListCriteria;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.service.NegativeListService;
import com.keymanager.monitoring.service.PerformanceService;
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
@RequestMapping(value = "/external/negativelist")
public class ExternalNegativeListRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalNegativeListRestController.class);

	@Autowired
	private NegativeListService negativeListService;

	@Autowired
	private PerformanceService performanceService;

	@RequestMapping(value = "/saveNegativeLists", method = RequestMethod.POST)
	public ResponseEntity<?> saveNegativeLists(@RequestBody NegativeListCriteria negativeListCriteria, HttpServletRequest request) throws Exception{
		try {
			if (validUser(negativeListCriteria.getUserName(), negativeListCriteria.getPassword())) {
				for(NegativeList negativeList : negativeListCriteria.getNegativeLists()){
					if(StringUtils.isNotEmpty(negativeList.getDesc())){
						String desc = negativeList.getDesc().replace("\n" , "").replace(" ", "");
						negativeList.setDesc(desc);
					}
				}
				negativeListService.saveNegativeLists(negativeListCriteria.getNegativeLists() , negativeListCriteria.getOperationType());
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getSpecifiedKeywordNegativeLists", method = RequestMethod.POST)
	public ResponseEntity<?> getSpecifiedKeywordNegativeLists(@RequestBody NegativeListCriteria negativeListCriteria, HttpServletRequest request) throws Exception{
		try {
			if (validUser(negativeListCriteria.getUserName(), negativeListCriteria.getPassword())) {
				List<NegativeList> negativeLists = negativeListService.getSpecifiedKeywordNegativeLists(negativeListCriteria.getKeyword());
				return new ResponseEntity<Object>(negativeLists, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getSpecifiedKeywordNegativeLists", method = RequestMethod.GET)
	public ResponseEntity<?> getSpecifiedKeywordNegativeLists(HttpServletRequest request) throws Exception{
		String userName = request.getParameter("userName");
		if(com.keymanager.monitoring.common.utils.StringUtils.isBlank(userName)){
			userName = request.getParameter("username");
		}
		String password = request.getParameter("password");
		String keyword = request.getParameter("keyword");
		try {
			if (validUser(userName, password)) {
				long startMilleSeconds = System.currentTimeMillis();
				List<NegativeList> negativeLists = negativeListService.getSpecifiedKeywordNegativeLists(keyword);
				StringBuilder sb = new StringBuilder(Constants.COLUMN_SPLITTOR);
				if(CollectionUtils.isNotEmpty(negativeLists)){
					for(NegativeList negativeList : negativeLists){
						sb.append(negativeList.getTitle());
						sb.append(Constants.COLUMN_SPLITTOR);
						sb.append(negativeList.getTitle());
						sb.append(Constants.COLUMN_SPLITTOR);
					}
					performanceService.addPerformanceLog("All:getSpecifiedKeywordNegativeLists", System.currentTimeMillis() - startMilleSeconds, "Record Count: " + (negativeLists != null ? negativeLists.size() : 0));
				}
				return new ResponseEntity<Object>(sb.toString(), HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}
	//负面信息同步
	@RequestMapping(value = "/negativeListsSynchronize", method = RequestMethod.POST)
	public ResponseEntity<?> negativeListsSynchronize(@RequestBody KeywordNegativeCriteria keywordNegativeCriteria) throws Exception{
		String userName = keywordNegativeCriteria.getUserName();
		String password = keywordNegativeCriteria.getPassword();
		try {
			if (validUser(userName, password)) {
				NegativeList negativeList = keywordNegativeCriteria.getNegativeList();
				List<NegativeList> existNegativeList = negativeListService.negativeListsSynchronizeOfDelete(negativeList);
				if(existNegativeList!=null){
					for(NegativeList negativeList1 : existNegativeList){
						negativeListService.deleteById(negativeList1.getUuid());
					}
				}
				if(keywordNegativeCriteria.getNegative()){
					negativeListService.insert(negativeList);
				}
				return new ResponseEntity<Object>(true,HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(false,HttpStatus.BAD_REQUEST);
	}
}
