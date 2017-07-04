package com.keymanager.servlet;

import com.keymanager.excel.operator.CustomerKeywordDailyReportExcelWriter;
import com.keymanager.excel.operator.CustomerKeywordInfoExcelWriter;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.manager.CustomerManager;
import com.keymanager.util.PortTerminalTypeMapping;
import com.keymanager.util.Utils;
import com.keymanager.util.common.StringUtil;
import com.keymanager.value.CustomerKeywordVO;
import com.keymanager.value.CustomerVO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.sql.Timestamp;
import java.util.List;

@SuppressWarnings("serial")
public class DownloadCustomerKeywordDailyReport extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String customerUuid = request.getParameter("customerUuid");
		String invalidRefreshCount = request.getParameter("invalidRefreshCount");
		String keyword = request.getParameter("keyword");
		String url = request.getParameter("url");
		String creationFromTime = request.getParameter("creationFromTime");
		String creationToTime = request.getParameter("creationToTime");
		String status = request.getParameter("status");
		String optimizeGroupName = request.getParameter("optimizeGroupName");

		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());

		String position = request.getParameter("position");

		String condition = "";
		condition = condition + " and fTerminalType = '" + terminalType + "' and fCustomerUuid=" + customerUuid;

		if (!Utils.isNullOrEmpty(keyword)) {
			condition = condition + " and fKeyword like '%" + keyword.trim() + "%' ";
		}

		if (!Utils.isNullOrEmpty(url)) {
			condition = condition + " and fUrl = '" + url.trim() + "' ";
		}

		if (!Utils.isNullOrEmpty(position)) {
			condition = condition
					+ " and ((fCurrentPosition > 0 and fCurrentPosition <= "
					+ position.trim() + ") ";
		}

		if (!Utils.isNullOrEmpty(optimizeGroupName)) {
			condition = condition + " and fOptimizeGroupName= '"
					+ optimizeGroupName.trim() + "' ";
		}

		if (!Utils.isNullOrEmpty(creationFromTime)) {
			condition = condition + " and ck.fCreateTime >= STR_TO_DATE('"
					+ creationFromTime.trim() + "', '%Y-%m-%d') ";
		}
		if (!Utils.isNullOrEmpty(creationToTime)) {
			condition = condition + " and ck.fCreateTime <= STR_TO_DATE('"
					+ creationToTime.trim() + "', '%Y-%m-%d') ";
		}

		if (!Utils.isNullOrEmpty(status)) {
			condition = condition + " and ck.fStatus = " + status.trim() + " ";
		}

		if (!Utils.isNullOrEmpty(invalidRefreshCount)) {
			condition = condition + " and fInvalidRefreshCount >= "
					+ invalidRefreshCount.trim() + " ";
		}

		download(customerUuid, condition, request, response);
	}

	public HttpServletResponse download(String customerUuid, String condition, HttpServletRequest request, HttpServletResponse response) {
		try {
			CustomerKeywordManager customerKeywordManager = new CustomerKeywordManager();
			List<CustomerKeywordVO> customerKeywords = customerKeywordManager.searchCustomerKeywords("keyword", 100000 , 1 , condition, " order by " +
					"ck.fSequence, ck.fKeyword ", 1);
			if(!Utils.isEmpty(customerKeywords)){
				CustomerKeywordDailyReportExcelWriter excelWriter = new CustomerKeywordDailyReportExcelWriter(customerUuid);
				excelWriter.writeDataToExcel(customerKeywords);

				CustomerManager customerManager = new CustomerManager();
				CustomerVO customerVO = customerManager.getCustomerByUuid("keyword", customerUuid);
				String fileName = customerVO.getContactPerson() + Utils.formatDatetime(Utils.getCurrentTimestamp(), "yyyy.MM.dd") + ".xls";
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
			}
			
//	    response.setContentType("Application/msexcel");
	    
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}
}
