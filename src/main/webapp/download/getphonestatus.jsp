<%@page contentType="text/html;charset=utf-8"  %> <%@page import="com.download.manager.*,java.util.*,com.download.value.*,com.keymanager.value.*,com.keymanager.util.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="rdm" scope="page" class="com.download.manager.RefreshDownloadManager" />
<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String phoneID = request.getParameter("imei");

    username = Utils.parseParam(username);
    password = Utils.parseParam(password);
    
	String dsName = "keyword";
	
	UserVO user = um.login(dsName,username,password);
	
	String returnInfo = "Invalid User"; //invalid user
	if(user != null){
		try{
			returnInfo = rdm.getPhoneStatus(dsName, phoneID);
		}catch(Exception ex){
			returnInfo = "error";
		}
	}
	out.println(returnInfo);
%>
