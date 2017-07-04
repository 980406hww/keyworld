<%@page contentType="text/html;charset=utf-8" errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>
<%@include file="/check.jsp" %>
<%
		if(loginState == 0)
		{
%>
        <script language="javascript">
        	alert("没有登录或登录超时,请重新登录");
			window.location.href="/bd.html";
		</script>
<%
        return;
		}
		String username = (String) session.getAttribute("username");
		String password = (String) session.getAttribute("password");
		if (username == null || username.equals(""))
		{
%>
        <script language="javascript">
        	 alert("没有登录或登录超时,请重新登录");
			 window.location.href="/bd.html";
		</script>
<%		  
        return;  
		}
		String condition = "";
		String qq = request.getParameter("qq");
		String telphone = request.getParameter("telphone");
		if(!Utils.isNullOrEmpty(qq)){
			condition = " and fQq = '" + qq + "'";
		}else{
			condition = " and fTelphone = '" + telphone + "'";
		}
		CustomerManager cm = new CustomerManager();
		int existingCustomerCount = cm.getCustomerCount(datasourceName, condition);	
		if(existingCustomerCount > 0){
			out.print("Existing");
		}
%>