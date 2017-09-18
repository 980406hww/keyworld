<%@page contentType="text/html;charset=utf-8"  %> 
<%@page import="com.keymanager.manager.*,java.util.*,com.keymanager.value.*,com.keymanager.util.*" %>
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

		String customerKeywordUuid = request.getParameter("uuid");
		String count = request.getParameter("count");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String cookieInfo = request.getParameter("cookieinfo");
		String clientID = request.getParameter("clientID");
		String freeSpace = request.getParameter("freespace");
		String version = request.getParameter("version");
		String city = request.getParameter("city");
		String status = request.getParameter("status");
	    String terminalType = TerminalTypeMapping.getTerminalType(request);

		if (customerKeywordUuid == null || count == null || username == null || password == null)
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
			ckm.updateOptimizedCount(datasourceName, terminalType, Integer.parseInt(customerKeywordUuid.trim()), Integer.parseInt(count.trim()), ip,
					city, cookieInfo, clientID, status, freeSpace, version);
			out.println("1");
		}
		catch(Exception e)
		{
		    out.println("0");
		}
%>
