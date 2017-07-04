package com.keymanager.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class DownloadKeywordPositionResult extends HttpServlet {

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		download(request, response);
	}

	public HttpServletResponse download(HttpServletRequest request, HttpServletResponse response) {
		try {
//			String fileName = request.getParameter("fileName");
//			String condition = KeywordPositionUtils.prepareCondition(request);
//			String order = KeywordPositionUtils.prepareOrder(request);
//			KeywordPositionManager manager = new KeywordPositionManager();
//			List<KeywordPositionVO> views = manager.searchKeywordPositionVOs("weibo", 100000 , 1 , condition, order, 1);
//			if(!Utils.isEmpty(views)){
//				KeywordPositionExcelWriter excelWriter = new KeywordPositionExcelWriter();
//				excelWriter.writeDataToExcel(views);
//
//				// 以流的形式下载文件。
//				byte[] buffer = excelWriter.getExcelContentBytes();
//				// 清空response
//				response.reset();
//				// 设置response的Header
//				response.addHeader("Content-Disposition", "attachment;filename=" + fileName);//new String(fileName.getBytes("utf-8"), "ISO-8859-1"));
//				response.addHeader("Content-Length", "" + buffer.length);
//				OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//				response.setContentType("application/octet-stream");
//				toClient.write(buffer);
//				toClient.flush();
//				toClient.close();
//			}
			
//	    response.setContentType("Application/msexcel");
	    
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return response;
	}
}
