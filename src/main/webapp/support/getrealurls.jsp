<%@page contentType="text/html;charset=utf-8" errorPage="/error.jsp"%>
<jsp:useBean id="cru" scope="page" class="com.keymanager.util.CaptureRealUrl" />
<%
	String sourceUrlJson = request.getParameter("data");
	String realUrlJson = cru.fetch(sourceUrlJson);
	out.print(realUrlJson);
%>


