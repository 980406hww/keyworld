<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="voim" scope="page" class="com.keymanager.manager.VPSOpenInfoManager" />
<%
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String datasourceName = "keyword";
		try{
			UserVO user = um.login(datasourceName,username,password);
			if(user != null){
				String vpsInfo = voim.getVPSForSetup(datasourceName);
				out.print(vpsInfo);
			}
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>