<%@page contentType="text/html;charset=utf-8"  %> 
<%@page import="com.keymanager.manager.*,java.util.*,com.keymanager.value.*,com.keymanager.util.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="rkm" scope="page" class="com.keymanager.manager.RelatedKeywordManager" />
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

		String relatedKeywordUuid = request.getParameter("uuid");
		String count = request.getParameter("count");
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if (relatedKeywordUuid == null || count == null || username == null || password == null)
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
	    	rkm.updateRelatedKeywordCompletedCount(datasourceName, Integer.parseInt(relatedKeywordUuid.trim()), Integer.parseInt(count.trim()));
			out.println("1");
		}
		catch(Exception e)
		{
		    out.println("0");
		}
%>
