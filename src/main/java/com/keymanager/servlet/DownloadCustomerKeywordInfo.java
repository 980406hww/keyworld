package com.keymanager.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keymanager.monitoring.excel.operator.CustomerKeywordInfoExcelWriter;
import com.keymanager.manager.CustomerKeywordManager;
import com.keymanager.util.PortTerminalTypeMapping;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordVO;

@SuppressWarnings("serial")
public class DownloadCustomerKeywordInfo extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String path = request.getParameter("fileName");
		path = URLDecoder.decode(path, "UTF-8");

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

		String condition = " and fTerminalType = '" + terminalType + "' ";
		condition = condition + " and fCustomerUuid=" + customerUuid;

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

		download(path, condition, request, response);
	}

	public HttpServletResponse download(String fileName, String condition, HttpServletRequest request, HttpServletResponse response) {
		try {
			CustomerKeywordManager customerKeywordManager = new CustomerKeywordManager();
			List<CustomerKeywordVO> customerKeywords = customerKeywordManager.searchCustomerKeywords("keyword", 100000 , 1 , condition, " order by ck.fKeyword ", 1);
			if(!Utils.isEmpty(customerKeywords)){
				CustomerKeywordInfoExcelWriter excelWriter = new CustomerKeywordInfoExcelWriter();
//				excelWriter.writeDataToExcel(customerKeywords);
				
				// 以流的形式下载文件。
				byte[] buffer = excelWriter.getExcelContentBytes();
				// 清空response
				response.reset();
				// 设置response的Header
				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);//new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
				response.addHeader("Content-Length", "" + buffer.length);
				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
				response.setContentType("application/octet-stream");
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
