<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
	
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
	 String customerUuid = request.getParameter("customerUuid");
     String groupName = request.getParameter("groupName");
	 ckm.updateCustomerKeywordGroupName(datasourceName, terminalType, Integer.parseInt(customerUuid), groupName);
%>

<script language="javascript">
	   alert("该用户关键字组名完毕！");
	   document.location.href = "/customerkeyword/list.jsp?isDelete=1&customerUuid=<%=customerUuid%>";
</script>




