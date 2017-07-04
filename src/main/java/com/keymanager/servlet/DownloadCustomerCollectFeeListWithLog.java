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

import com.keymanager.excel.operator.CustomerCollectFeeWithLogExcelWriter;
import com.keymanager.manager.CustomerKeywordPositionViewManager;
import com.keymanager.util.Utils;
import com.keymanager.value.CustomerKeywordPositionHistoryLogView;
import com.keymanager.value.CustomerKeywordPositionView;

@SuppressWarnings("serial")
public class DownloadCustomerCollectFeeListWithLog extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getParameter("fileName");
		path = URLDecoder.decode(path,"GBK");
		String customerUuid = request.getParameter("customerUuid");
		String customerName = request.getParameter("customerName");
		String date = request.getParameter("date");
		String statType = request.getParameter("statType");
		download(path, customerUuid, customerName, date, statType, request, response);
	}

	public HttpServletResponse download(String fileName, String customerUuid, String customerName, String date, String statType, HttpServletRequest request, HttpServletResponse response) {
		try {
			CustomerKeywordPositionViewManager manager = new CustomerKeywordPositionViewManager();
			List<CustomerKeywordPositionView> customerKeywordPositionViews = manager.searchCustomerKeywordPositionViews("keyword", 100000 , 1 , customerUuid, customerName, date, statType, 1);
			List<CustomerKeywordPositionHistoryLogView> customerKeywordPositionHistoryLogViews = manager.searchCustomerKeywordPositionHistoryLogViews("keyword", 100000 , 1 , customerUuid, customerName, date, statType, 1);
			manager.supplementPositionLog(customerKeywordPositionViews, customerKeywordPositionHistoryLogViews);
			if(!Utils.isEmpty(customerKeywordPositionViews) || !Utils.isEmpty(customerKeywordPositionHistoryLogViews)){
				CustomerCollectFeeWithLogExcelWriter excelWriter = new CustomerCollectFeeWithLogExcelWriter();
				excelWriter.writeCollectFeeDataToExcel(customerKeywordPositionViews);
				excelWriter.writeLogDataToExcel(customerKeywordPositionHistoryLogViews);
				
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
