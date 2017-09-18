
<%@page contentType="text/html;charset=utf-8"  %> <%@page import="com.keymanager.manager.*,java.util.*,com.keymanager.value.*,com.keymanager.util.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="csm" scope="page" class="com.keymanager.manager.ClientStatusManager" />
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
	String clientID = request.getParameter("clientID");
	String keyword = request.getParameter("keyword");
	String terminalType = TerminalTypeMapping.getTerminalType(request);
    username = Utils.parseParam(username);
    password = Utils.parseParam(password);
    
	String dsName = "keyword";
	
	UserVO user = um.login(dsName,username,password);
	String returnInfo = "Invalid User"; //invalid user
	if(user != null){
		returnInfo = ckm.searchCustomerKeywordsForClientCache(dsName, terminalType, keyword);
	}
	out.println(returnInfo);
%>
