<%@page language="java" contentType="application/octet-stream" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.fileupload.servlet.*,org.apache.commons.fileupload.*,org.apache.commons.fileupload.disk.*,jxl.*,jxl.read.biff.BiffException,com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*,java.io.*,com.keymanager.monitoring.excel.operator.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page" class="com.keymanager.manager.ClientStatusManager" />
<%@include file="/check.jsp" %>
<%
	if (loginState == 0) {
%>
        <script language="javascript">
        	alert("没有登录或登录超时,请重新登录");
			window.location.href="/bd.html";
		</script>
<%
	return;
	}

	String username = (String) session.getAttribute("username");
	String password = (String) session.getAttribute("password");
	UserVO user = um.login(datasourceName, username, password);

	if (user == null) {
%>
        <script language="javascript">
        	alert("没有登录或登录超时,请重新登录");
			window.location.href="/bd.html";
		</script>
<%
	return;
	}
	//关于文件下载时采用文件流输出的方式处理：
	//加上response.reset()，并且所有的％>后面不要换行，包括最后一个；

	response.reset();//可以加也可以不加
	response.setContentType("application/x-download");

	//application.getRealPath("/main/mvplayer/CapSetup.msi");获取的物理路径

	String filedownload = csm.downloadVNCInfo(datasourceName);
	String filedisplay = "vnc.zip";
	response.addHeader("Content-Disposition", "attachment;filename=" + filedisplay);

	java.io.OutputStream outp = null;
	java.io.FileInputStream in = null;
	try {
		outp = response.getOutputStream();
		in = new FileInputStream(filedownload);

		byte[] b = new byte[1024];
		int i = 0;

		while ((i = in.read(b)) > 0) {
			outp.write(b, 0, i);
		}
		//  
		outp.flush();
		//要加以下两句话，否则会报错
		//java.lang.IllegalStateException: getOutputStream() has already been called for //this response  
		out.clear();
		out = pageContext.pushBody();
	} catch (Exception e) {
		System.out.println("Error!");
		e.printStackTrace();
	} finally {
		if (in != null) {
			in.close();
			in = null;
		}
		//这里不能关闭  
		//if(outp != null)
		//{
		//outp.close();
		//outp = null;
		//}
	}
%>