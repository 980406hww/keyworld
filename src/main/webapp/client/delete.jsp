<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page" class="com.keymanager.manager.ClientStatusManager" />

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
		String clientID = request.getParameter("clientID");

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
		
		
		csm.deleteClientStatus(datasourceName, clientID);
		
%>
<script language="javascript">
	alert("删除终端成功！");
    window.location.href="clientlist.jsp?isDelete=1";
</script>

