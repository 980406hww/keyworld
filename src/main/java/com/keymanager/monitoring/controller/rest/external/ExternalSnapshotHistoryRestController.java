package com.keymanager.monitoring.controller.rest.external;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.criteria.SnapshotHistoryCriteria;
import com.keymanager.monitoring.entity.SnapshotHistory;
import com.keymanager.monitoring.service.SnapshotHistoryService;
import com.keymanager.monitoring.vo.SnapshotHistoryVO;
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
import java.util.Map;

@RestController
@RequestMapping(value = "/external/snapshotHistory")
public class ExternalSnapshotHistoryRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(ExternalSnapshotHistoryRestController.class);

	@Autowired
	private SnapshotHistoryService snapshotHistoryService;

	@RequestMapping(value = "/saveSnapshotInfo", method = RequestMethod.POST)
	public ResponseEntity<?> saveSnapshotInfo(@RequestBody SnapshotHistoryCriteria snapshotHistoryCriteria, HttpServletRequest request) throws Exception {
		try {
			if (validUser(snapshotHistoryCriteria.getUserName(), snapshotHistoryCriteria.getPassword())) {
				snapshotHistoryService.saveSnapshotInfos(snapshotHistoryCriteria.getSnapshotList());
				return new ResponseEntity<Object>(true, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/searchSnapshotHistories", method = RequestMethod.POST)
	public ResponseEntity<?> searchSnapshotHistories(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception {
		try {
			String userName = (String) requestMap.get("userName");
			String password = (String) requestMap.get("password");
			String searchDate = (String) requestMap.get("searchDate");
			if (validUser(userName, password)) {
				SnapshotHistoryVO snapshotHistorieVO = snapshotHistoryService.searchSnapshotHistories(searchDate);
				return new ResponseEntity<Object>(snapshotHistorieVO, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

	@RequestMapping(value = "/getNegativeHistoryRanks", method = RequestMethod.POST)
	public ResponseEntity<?> getNegativeHistoryRanks(@RequestBody SnapshotHistoryCriteria snapshotHistoryCriteria, HttpServletRequest request) throws Exception {
		try {
			if (validUser(snapshotHistoryCriteria.getUserName(), snapshotHistoryCriteria.getPassword())) {
				SnapshotHistoryVO snapshotHistoryVO = snapshotHistoryService.getNegativeHistoryRanks(snapshotHistoryCriteria);
				return new ResponseEntity<Object>(snapshotHistoryVO, HttpStatus.OK);
			}
		}catch (Exception ex){
			logger.error(ex.getMessage());
		}
		return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
	}

}
