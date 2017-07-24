<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page" class="com.keymanager.manager.ClientStatusManager" />
<%
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String clientID = request.getParameter("clientID");
	    String isDelete = request.getParameter("isDelete");
		String terminalType = PortTerminalTypeMapping.getTerminalType(request.getServerPort());

		String datasourceName = "keyword";
		try{
			UserVO user = um.login(datasourceName,username,password);
			if(user != null){
				csm.updateClientStatusRestartCount(datasourceName, terminalType, clientID, isDelete);
				out.print("1");
			}else{
				out.print("0");
			}
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>