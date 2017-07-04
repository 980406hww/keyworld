<%@ page contentType="text/html;charset=utf-8" %>
<%@ page import="java.text.*,java.io.*,java.util.*,com.keymanager.value.*,com.keymanager.manager.*" %>
<%@ page import="com.keymanager.util.common.StringUtil" %>
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
		String data = request.getParameter("data");
	    if(!StringUtil.isNullOrEmpty(data)){
			data = data.replace("\"", "");
		}
	    String [] elements = data.split("=");
	    String clientID = elements[0];
		String upgradeFailedReason = elements[1];
		
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
			csm.updateUpgradeFailedReason(datasourceName, clientID, upgradeFailedReason);
			out.print("1");
		 }catch(Exception ex){
		 	out.print("0");
		 }
%>

