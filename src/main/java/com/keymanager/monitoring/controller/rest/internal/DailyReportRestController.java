package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.DailyReport;
import com.keymanager.monitoring.service.DailyReportService;
import com.keymanager.monitoring.service.UserService;
import com.keymanager.util.PortTerminalTypeMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/internal/dailyReport")
public class DailyReportRestController extends SpringMVCBaseController {
	private static Logger logger = LoggerFactory.getLogger(DailyReportRestController.class);

	@Autowired
	private DailyReportService dailyReportService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/triggerReportGeneration", method = RequestMethod.POST)
	public ResponseEntity<?> triggerReportGeneration(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception{
		String customerUuids = (String) requestMap.get("customerUuids");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		String returnValue = null;
		try {
			dailyReportService.triggerReportGeneration(terminalType, customerUuids);
			returnValue = "{\"status\":true}";
		}catch(Exception ex){
			logger.error(ex.getMessage());
			ex.printStackTrace();
			returnValue = "{\"status\":false}";
		}
		return new ResponseEntity<Object>(returnValue, HttpStatus.OK);
	}

	@RequestMapping(value = "/searchCurrentDateCompletedReports", method = RequestMethod.GET)
	public ResponseEntity<?> searchCurrentDateCompletedReports(HttpServletRequest request) throws Exception{
		try {
			String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
			List<DailyReport> dailyReports = dailyReportService.searchCurrentDateCompletedReports(terminalType);
			return new ResponseEntity<Object>(dailyReports, HttpStatus.OK);
		}catch (Exception ex) {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}
}
