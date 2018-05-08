package com.keymanager.monitoring.controller.rest.internal;

import com.keymanager.monitoring.criteria.CustomerKeywordCriteria;
import com.keymanager.monitoring.entity.Config;
import com.keymanager.monitoring.entity.CustomerKeyword;
import com.keymanager.monitoring.excel.operator.CustomerKeywordDailyReportExcelWriter;
import com.keymanager.monitoring.controller.SpringMVCBaseController;
import com.keymanager.monitoring.entity.Customer;
import com.keymanager.monitoring.entity.DailyReport;
import com.keymanager.monitoring.service.ConfigService;
import com.keymanager.monitoring.service.CustomerKeywordService;
import com.keymanager.monitoring.service.CustomerService;
import com.keymanager.monitoring.service.DailyReportService;
import com.keymanager.util.Constants;
import com.keymanager.util.TerminalTypeMapping;
import com.keymanager.util.Utils;
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

	@Autowired
	private CustomerKeywordService customerKeywordService;

	@Autowired
	private ConfigService configService;

	@RequestMapping(value = "/triggerReportGeneration", method = RequestMethod.POST)
	public ResponseEntity<?> triggerReportGeneration(@RequestBody Map<String, Object> requestMap, HttpServletRequest request) throws Exception{
		int dayOfMonth = Utils.getDayOfMonth();
		String customerUuids = (String) requestMap.get("customerUuids");
		String triggerType = (String) requestMap.get("triggerType");
		String terminalType = TerminalTypeMapping.getTerminalType(request);
		String returnValue = null;
		try {
			if(dayOfMonth == 1) {
				dailyReportService.resetDailyReportExcel(terminalType, customerUuids);
			}
			if(triggerType.equals("saveDailyReportTemplate")) {
				configService.updateCustomerUuidsForDailyReport(customerUuids);
			}
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
			String terminalType = TerminalTypeMapping.getTerminalType(request);
			List<DailyReport> dailyReports = dailyReportService.searchCurrentDateCompletedReports(terminalType);
			return new ResponseEntity<Object>(dailyReports, HttpStatus.OK);
		}catch (Exception ex) {
			return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/downloadSingleCustomerReport/{customerUuid}", method = RequestMethod.GET)
	public ResponseEntity<?> downloadSingleCustomerReport(@PathVariable("customerUuid")Long customerUuid, HttpServletRequest request,
														  HttpServletResponse response) throws Exception {
		String terminalType = TerminalTypeMapping.getTerminalType(request);
		int dayOfMonth = Utils.getDayOfMonth();

		CustomerKeywordCriteria customerKeywordCriteria = new CustomerKeywordCriteria();
		customerKeywordCriteria.setTerminalType(terminalType);
		customerKeywordCriteria.setCustomerUuid(customerUuid);
		customerKeywordCriteria.setStatus("1");
		customerKeywordCriteria.setOrderingElement("fSequence");
		customerKeywordCriteria.setOrderingRule("ASC");
		List<CustomerKeyword> customerKeywords = customerKeywordService.searchCustomerKeywordsForDailyReport(customerKeywordCriteria);
		if (!Utils.isEmpty(customerKeywords)) {
			if(dayOfMonth == 1) {
				String uuids = "" + customerUuid;
				dailyReportService.resetDailyReportExcel(terminalType, uuids);
			}

			CustomerKeywordDailyReportExcelWriter excelWriter = new CustomerKeywordDailyReportExcelWriter(terminalType, customerUuid + "", 0);
			Customer customer = customerService.selectById(customerUuid);
			excelWriter.writeDataToExcel(customerKeywords, customer.getContactPerson(), terminalType);

			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
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
