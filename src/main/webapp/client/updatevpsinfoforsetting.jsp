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
				String isDelete = request.getParameter("isDelete");

				vpsOpenInfoVO.setUuid(Integer.parseInt(uuid));
				vpsOpenInfoVO.setSettingStatus(isDelete);
				voim.updateCompleteSetting(datasourceName, vpsOpenInfoVO);
				out.print("1");
			}else{
				out.print("0");
			}
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>