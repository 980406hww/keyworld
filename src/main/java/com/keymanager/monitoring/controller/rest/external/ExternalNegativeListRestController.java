package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.NegativeListCriteria;
import com.keymanager.monitoring.entity.NegativeList;
import com.keymanager.monitoring.entity.User;
import com.keymanager.monitoring.service.NegativeListService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.Constants;
import com.keymanager.util.PortTerminalTypeMapping;
import com.keymanager.value.FumianListVO;
import org.apache.commons.collections.CollectionUtils;
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

@RestController
@RequestMapping(value = "/external/negativelist")
public class ExternalNegativeListRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalNegativeListRestController.class);

	@Autowired
	private NegativeListService negativeListService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/saveNegativeLists", method = RequestMethod.POST)
	public ResponseEntity<?> saveNegativeLists(@RequestBody NegativeListCriteria negativeListCriteria, HttpServletRequest request) throws Exception{
		if(negativeListCriteria.getUserName() != null && negativeListCriteria.getPassword() != null){
			User user = userService.getUser(negativeListCriteria.getUserName());
			if(user != null && user.getPassword().equals(negativeListCriteria.getPassword())){
				String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
				for(NegativeList negativeList : negativeListCriteria.getNegativeLists()){
					negativeList.setTerminalType(terminalType);
				}
				negativeListService.saveNegativeLists(negativeListCriteria.getNegativeLists());
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(false, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getSpecifiedKeywordNegativeLists", method = RequestMethod.POST)
	public ResponseEntity<?> getSpecifiedKeywordNegativeLists(@RequestBody NegativeListCriteria negativeListCriteria, HttpServletRequest request) throws Exception{
		if(negativeListCriteria.getUserName() != null && negativeListCriteria.getPassword() != null){
			User user = userService.getUser(negativeListCriteria.getUserName());
			if(user != null && user.getPassword().equals(negativeListCriteria.getPassword())){
				String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
				List<NegativeList> negativeLists = negativeListService.getSpecifiedKeywordNegativeLists(terminalType, negativeListCriteria
						.getKeyword());
				return new ResponseEntity<Object>(negativeLists, HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getSpecifiedKeywordNegativeLists", method = RequestMethod.GET)
	public ResponseEntity<?> getSpecifiedKeywordNegativeLists(HttpServletRequest request) throws Exception{
		String userName = request.getParameter("username");
		String password = request.getParameter("password");
		String keyword = request.getParameter("keyword");
		if(userName != null && password != null){
			User user = userService.getUser(userName);
			if(user != null && user.getPassword().equals(password)){
				String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
				List<NegativeList> negativeLists = negativeListService.getSpecifiedKeywordNegativeLists(terminalType, keyword);
				StringBuilder sb = new StringBuilder(Constants.COLUMN_SPLITTOR);
				if(CollectionUtils.isNotEmpty(negativeLists)){
					for(NegativeList negativeList : negativeLists){
						sb.append(negativeList.getTitle());
						sb.append(Constants.COLUMN_SPLITTOR);
						sb.append(negativeList.getTitle());
						sb.append(Constants.COLUMN_SPLITTOR);
					}
				}
				return new ResponseEntity<Object>(sb.toString(), HttpStatus.OK);
			}
		}
		return new ResponseEntity<Object>(null, HttpStatus.BAD_REQUEST);
	}
}
