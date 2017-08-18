package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.DailyReport;
import com.keymanager.monitoring.entity.User;
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
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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

	@RequestMapping(value = "/downloadSingleCustomerReport", method = RequestMethod.POST)
	public ResponseEntity<?> downloadSingleCustomerReport(@RequestBody Map<String, Object> requestMap, HttpServletRequest request,
											HttpServletResponse response){
		Long customerUuid = (Long) requestMap.get("customerUuid");
		Report report = reportService.selectById(reportId);
		User currentUser = userService.selectById(getCurrentUser().getId());
		if(UserTypeEnum.Normal.getValue().equals(currentUser.getType()) && getCurrentUser().getId().longValue() != report.getUserId().longValue()){
			return new ResponseEntity<Object>(request, HttpStatus.BAD_REQUEST);
		}
		String fileName = report.getFileName();
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		try {
			String realPath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + report.getFileLocation();
			File file = new File(realPath, fileName);
			if (file.exists()) {
				response.setContentType("application/octet-stream");
				response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
				byte[] buffer = new byte[1024];

				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer, 0, i);
					i = bis.read(buffer);
				}
				return new ResponseEntity<Object>(bis, HttpStatus.OK);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return new ResponseEntity<Object>(report, HttpStatus.OK);
	}
}
