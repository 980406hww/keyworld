<%@page contentType="text/html;charset=utf-8"  %>
<%@page import="com.keymanager.manager.*,java.util.*,com.keymanager.value.*" %>
<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<%
	String username = (String) request.getParameter("userID");
	String password = (String) request.getParameter("password");

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

	int state = 0;
	UserVO user = um.login("keyword", username, password, ip);

	if (user != null) {
		session.setAttribute("terminalType", PortTerminalTypeMapping.getTerminalType(request.getServerPort()));
		session.setAttribute("loginstate", "1");
		session.setAttribute("username", user.getUserID());
		session.setAttribute("password", user.getPassword());
		if(user.getUserLevel() <= 1){
%>
		<script language="javascript">
			  window.location.href="/internal/customerKeyword/searchCustomerKeywordLists";
		</script>
<%
		}else{
%>
			<script language="javascript">
				  window.location.href="/customerkeyword/keywordList.jsp";
			</script>
<%			
		}
	} else {
%>
<script language="javascript">
	alert("密码错误，没有权限！");
	window.location.href="/bd.html";
</script>
<%
	}
%>
