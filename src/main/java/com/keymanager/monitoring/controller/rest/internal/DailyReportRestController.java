package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.excel.operator.CustomerKeywordDailyReportExcelWriter;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.DailyReport;
import com.keymanager.monitoring.service.CustomerService;
import com.keymanager.monitoring.service.DailyReportService;
import com.keymanager.util.PortTerminalTypeMapping;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordVO;
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
	private CustomerService customerService;

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

	@RequestMapping(value = "/downloadSingleCustomerReport/{customerUuid}", method = RequestMethod.GET)
	public ResponseEntity<?> downloadSingleCustomerReport(@PathVariable("customerUuid")Long customerUuid, HttpServletRequest request,
														  HttpServletResponse response) throws Exception {
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		CustomerKeywordManager customerKeywordManager = new CustomerKeywordManager();
		String condition = String.format(" and ck.fStatus = 1 and ck.fCustomerUuid = %d and ck.fTerminalType = '%s' ", customerUuid, terminalType);
		List<CustomerKeywordVO> customerKeywords = customerKeywordManager.searchCustomerKeywords("keyword", 10000, 1, condition,
				"order by ck.fSequence, ck.fKeyword ", 1);
		if (!Utils.isEmpty(customerKeywords)) {
			CustomerKeywordDailyReportExcelWriter excelWriter = new CustomerKeywordDailyReportExcelWriter(terminalType, customerUuid + "", 0);
			excelWriter.writeDataToExcel(customerKeywords);

			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				Customer customer = customerService.selectById(customerUuid);
				String fileName = customer.getContactPerson() + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd") + ".xls";
				fileName = new String(fileName.getBytes("gb2312"), "ISO8859-1");
				// 以流的形式下载文件。
				byte[] buffer = excelWriter.getExcelContentBytes();
				// 清空response
				response.reset();
				// 设置response的Header
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);//new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
				response.addHeader("Content-Length", "" + buffer.length);
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream;charset=utf-8");
				response.setCharacterEncoding("UTF-8");
				toClient.write(buffer);
				toClient.flush();
				toClient.close();
			} catch (Exception e) {
				logger.error(e.getMessage());;
				e.printStackTrace();
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						logger.error(e.getMessage());;
						e.printStackTrace();
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						logger.error(e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
