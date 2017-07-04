<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
	
<%		  
	try {
		String data = request.getParameter("customerKeywordJson");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		String operation = request.getParameter("operation");
		if ("Save".equals(operation)) {
			ckm.saveCustomerKeywords("keyword", terminalType, data);
		}else{
			ckm.updateCustomerKeywords("keyword", terminalType, data);
		}
		out.print("1");
	 }catch(Exception ex){
	 	out.print("0");
	 }  
%>





