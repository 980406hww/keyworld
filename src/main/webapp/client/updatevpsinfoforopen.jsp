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
				VPSOpenInfoVO vpsOpenInfoVO = new VPSOpenInfoVO();
				String uuid = request.getParameter("uuid");
				String host = request.getParameter("host");
				String port = request.getParameter("port");
				String vncUser = request.getParameter("user");
				String vncPassword = request.getParameter("pass");
				String connUser = request.getParameter("connuser");
				String connPassword = request.getParameter("connpass");
				String vpsUuid = request.getParameter("vpsuuid");
				String status = request.getParameter("status");
				String trial = request.getParameter("trial");

				vpsOpenInfoVO.setUuid(Integer.parseInt(uuid));
				vpsOpenInfoVO.setVncHost(host);
				vpsOpenInfoVO.setVncPort(port);
				vpsOpenInfoVO.setVncUser(vncUser);
				vpsOpenInfoVO.setVncPassword(vncPassword);
				vpsOpenInfoVO.setConnUser(connUser);
				vpsOpenInfoVO.setConnPassword(connPassword);
				vpsOpenInfoVO.setVpsUuid(vpsUuid);
				vpsOpenInfoVO.setOpenStatus(status);
				vpsOpenInfoVO.setTrival(trial);
				voim.updateCompleteOpenInfo(datasourceName, vpsOpenInfoVO);
				out.print("1");
			}else{
				out.print("0");
			}
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>