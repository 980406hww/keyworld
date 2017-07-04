<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page" class="com.keymanager.manager.ClientStatusManager" />
<%
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String datasourceName = "keyword";
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());
		try{
			UserVO user = um.login(datasourceName,username,password);
			if(user != null){
				String stoppedClientStatuses = csm.getStoppedClientStatuses(datasourceName, terminalType);
				out.print(stoppedClientStatuses);
			}
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>