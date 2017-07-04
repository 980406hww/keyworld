<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<%		  
	String fumianSupportingData = cm.getCustomers("keyword");
	out.print(fumianSupportingData);   
%>


