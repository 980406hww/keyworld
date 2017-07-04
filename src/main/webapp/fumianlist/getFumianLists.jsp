<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="flm" scope="page" class="com.keymanager.manager.FumianListManager" />
	
<%		  
	try{
		 String keyword = request.getParameter("keyword");
		 String result = flm.getFumianLists("keyword", keyword);
	 	 out.print(result);
	 }catch(Exception ex){
	 	ex.printStackTrace();
	 }  
%>





