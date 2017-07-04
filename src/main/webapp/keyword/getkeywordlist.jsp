<%@page contentType="text/html;charset=utf-8"  %> <%@page import="com.keymanager.manager.*,java.util.*,com.keymanager.value.*,com.keymanager.util.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="km" scope="page" class="com.keymanager.manager.KeywordManager" />
<%
	String username = request.getParameter("username");
	String password = request.getParameter("password");
	String captureIndex = request.getParameter("captureindex");
	String count = request.getParameter("count");

    username = Utils.parseParam(username);
    password = Utils.parseParam(password);
    
	String dsName = "keyword";
	
	UserVO user = um.login(dsName,username,password);
	
	String returnInfo = "Invalid User"; //invalid user
	if(user != null){
		try{
			if ("1".equals(captureIndex)){
				returnInfo = km.searchKeywordForCaptureIndex(dsName, 60*24*2, Utils.isNullOrEmpty(count) ? 30 : Integer.parseInt(count.trim()));
			}else{
				returnInfo = km.searchKeywordForCapturePosition(dsName, 30, Utils.isNullOrEmpty(count) ? 30 : Integer.parseInt(count.trim()));
			}
		}catch(Exception ex){
			returnInfo = "error";
		}
	}
	out.println(returnInfo);
%>
