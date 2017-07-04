package com.keymanager.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.keymanager.excel.operator.CustomerCollectFeeExcelWriter;
import com.keymanager.manager.KeywordContactManager;
import com.keymanager.util.KeywordContactUtils;
import com.keymanager.util.Utils;
import com.keymanager.value.KeywordContactVO;

@SuppressWarnings("serial")
public class DownloadKeywordContactResult extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getParameter("fileName");
		String condition = KeywordContactUtils.prepareCondition(request);
		download(path, condition, request, response);
	}

	public HttpServletResponse download(String fileName, String condition, HttpServletRequest request, HttpServletResponse response) {
		try {
			KeywordContactManager manager = new KeywordContactManager();
			List<KeywordContactVO> views = manager.searchKeywordContactVOs("weibo", 100000 , 1 , condition, "", 1);
			if(!Utils.isEmpty(views)){
				CustomerCollectFeeExcelWriter excelWriter = new CustomerCollectFeeExcelWriter();
//				excelWriter.writeDataToExcel(views);
				
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
