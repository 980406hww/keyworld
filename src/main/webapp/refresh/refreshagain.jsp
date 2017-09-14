<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />

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
		String entryType = (String) session.getAttribute("entry");
		
		UserVO user = um.login(datasourceName,username,password);		
		if(!user.isVipType()){
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
		
		String url = request.getParameter("url");
		String groupName = request.getParameter("groupName");
		String customerName = request.getParameter("customerName");
		try{
			ckm.updateInvalidRefreshCount(datasourceName, entryType, groupName, customerName);			
		 	out.print("1");
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>

