<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />

<%@include file="/check.jsp" %>

<%
		if(loginState == 0)
		{
%>
        <script language="javascript">
		   window.location.href="bd.html";
	 	</script>
<%
        return;
		}
		
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		
		UserVO user = um.login(datasourceName,username,password);
		if (user == null)
		{
%>
        <script language="javascript">
        	alert("没有登录或登录超时,请重新登录");
			window.location.href="bd.html";
		</script>
<%		  
        return;  
		}
		if (!user.isVipType())
		{
%>
        <script language="javascript">
        	alert("你没有该权限！");
        	window.history.back();
	  	</script>
<%		  
        return;  
		}				
		String userID = request.getParameter("userID");
		String isDelete = request.getParameter("isDelete");
		
		if(!Utils.isNullOrEmpty(isDelete)){
			um.updateUserStatus("keyword",userID,Integer.parseInt(isDelete.trim()));
		}
%>
<script language="javascript">
    window.location.href="userlist.jsp";
</script>

