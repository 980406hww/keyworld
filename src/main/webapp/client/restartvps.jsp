<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
<%@ page import="com.keymanager.monitoring.service.VMwareService" %>
<%@ page import="com.keymanager.util.SpringContextHolder" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page" class="com.keymanager.manager.ClientStatusManager" />
<%
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String vmName = request.getParameter("vmname");
		String datasourceName = "keyword";
		try{
			UserVO user = um.login(datasourceName,username,password);
			if(user != null){
				VMwareService wmService = (VMwareService) SpringContextHolder.getBean("VMwareService");
				wmService.restartVPS(vmName);
				out.print("1");
			}
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>