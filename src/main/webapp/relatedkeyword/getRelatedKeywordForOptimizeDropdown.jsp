
<%@page contentType="text/html;charset=utf-8"  %> <%@page import="com.keymanager.manager.*,java.util.*,com.keymanager.value.*,com.keymanager.util.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="rkmu" scope="page" class="com.keymanager.manager.RelatedKeywordUrlManager" />
<%
	String ip = request.getHeader("X-Forwarded-For");
if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    ip = request.getHeader("Proxy-Client-IP");
}

if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    ip = request.getHeader("WL-Proxy-Client-IP");
}

if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    ip = request.getHeader("HTTP_CLIENT_IP");
}
if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    ip = request.getHeader("HTTP_X_FORWARDED_FOR");
}

if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
    ip = request.getRemoteAddr();
}

	String username = request.getParameter("username");
	String password = request.getParameter("password");
    username = Utils.parseParam(username);
    password = Utils.parseParam(password);
    
	String dsName = "keyword";
	
	UserVO user = um.login(dsName,username,password);
	
	String returnInfo = "Invalid User"; //invalid user
	if(user != null){
		returnInfo = rkmu.getRelatedKeywordForOptimize(dsName);
	}
	out.println(returnInfo);
%>