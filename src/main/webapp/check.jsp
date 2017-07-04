<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
<%
		int loginState = 0;
		String datasourceName = "keyword";
		String loginStatus = (String) session.getAttribute("loginstate");
		
		if (loginStatus != null && loginStatus.equals("1"))
		{
		    loginState = 1;
		}

		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
%>