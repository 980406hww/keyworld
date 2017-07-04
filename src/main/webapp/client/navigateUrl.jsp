<%@page contentType="text/html;charset=utf-8"  %> <%@page import="com.keymanager.manager.*,java.util.*,com.keymanager.value.*,com.keymanager.util.*" %>
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.ConfigManager" />
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	
    username = Utils.parseParam(username);
    password = Utils.parseParam(password);
    
	String dsName = "keyword";
	
	UserVO user = um.login(dsName,username,password);
	
	String returnInfo = "Invalid User"; //invalid user
	if(user != null){
		try{
			ConfigVO configVO = cm.getConfig(dsName, "Client", "UrlPercentage");
			returnInfo = DomainPercentageUtils.randomUrl(configVO.getValue());
		}catch(Exception ex){
			returnInfo = "error";
		}
	}
	out.println(returnInfo);
%>
