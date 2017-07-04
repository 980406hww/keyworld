<%@page contentType="text/html;charset=utf-8"  %> 
<%@page import="com.keymanager.manager.*,java.util.*,com.keymanager.value.*,com.keymanager.util.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="km" scope="page" class="com.keymanager.manager.KeywordManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<%
	String data = request.getParameter("data");
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	
	if (data == null || username == null || password == null)
	{
	    out.println("Invalid Data");
	    return;
	}
	String datasourceName = "keyword";
	UserVO user = um.login(datasourceName, username, password);
	if (user == null){
		out.println("Invalid Data");
    	return;
	}
    try
    {
		ckm.updateCustomerKeywordIndex(datasourceName, data);
		out.println("1");
	}
	catch(Exception e)
	{
		out.println(e.getMessage());
	    out.println("0");
	}
%>
