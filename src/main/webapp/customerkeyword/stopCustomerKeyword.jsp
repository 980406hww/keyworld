<%@ page contentType="text/html;charset=utf-8" %>
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
		String customerUuid = request.getParameter("customerUuid");	
		if (username == null || username.equals(""))
		{
%>
        <script language="javascript">
		   window.location.href="/bd.html";
	  	</script>
<%		  
        return;  
		}
		
		ckm.stopCustomerKeyword(datasourceName, terminalType, Integer.parseInt(customerUuid));
		
%>
<script language="javascript">
    window.location.href="list.jsp?isDelete=1&customerUuid=<%=customerUuid%>";
</script>

