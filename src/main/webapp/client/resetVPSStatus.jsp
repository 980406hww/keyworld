<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="voim" scope="page" class="com.keymanager.manager.VPSOpenInfoManager" />

<%@include file="/check.jsp" %>

<%
		if(loginState == 0)
		{
%>
        <script language="javascript">
			window.location.href="/bd.html";
		</script>
<%
        return;
		}
		
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		String uuids = request.getParameter("uuids");
		String stage= request.getParameter("stage");

		UserVO user = um.login(datasourceName,username,password);		
		if ((user.getStatus() == 0) || (user.getUserLevel() > 1))
		{
%>
	      <script language="javascript">
	      	alert("你没有该权限！");
	      	window.history.back();
		  </script>
<%			
			return;
		}
		if (username == null || username.equals(""))
		{
%>
        <script language="javascript">
		   window.location.href="/bd.html";
	  	</script>
<%		  
        return;  
		}
		
		try{
			voim.resetVPSStatus(datasourceName, uuids, stage);
			out.println("1");
		}catch(Exception ex){
			out.println("0");
		}
%>
