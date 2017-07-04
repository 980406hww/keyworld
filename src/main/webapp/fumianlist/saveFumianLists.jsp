<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="flm" scope="page" class="com.keymanager.manager.FumianListManager" />
	
<%		  
	try{
		 String data = request.getParameter("fumianList");
		 String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		 flm.saveFumianLists("keyword", terminalType, data);
	 	 out.print("1");
	 }catch(Exception ex){
	 	out.print("0");
	 }  
%>





