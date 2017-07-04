<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
	
<%		  
	try {
		String group = request.getParameter("group");
		ckm.updateAutoUpdateNegativeTime("keyword", group);
		out.print("1");
	 }catch(Exception ex){
	 	out.print("0");
	 }
%>





