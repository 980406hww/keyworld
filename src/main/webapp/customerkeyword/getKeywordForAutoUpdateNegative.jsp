<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<%
//		String username = request.getParameter("username");
//		String password = request.getParameter("password");
		String group = request.getParameter("group");
		String datasourceName = "keyword";
		try{
			String keywords = ckm.fetchCustomerKeywordForAutoUpdateNegative(datasourceName, group);
			out.print(keywords);
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>