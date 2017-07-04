<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />

<style>
   .hiddentr {display:none;}
   .displaytr {dislay:;}
</style>

<script>
	function checkinput(){
		var groupName = document.getElementById("groupName");
		if(trim(groupName.value) == ""){
			alert("请输入目标组名");
			return false;
		}
	}
</script>


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
		
		String customerUuid = request.getParameter("customerUuid");
		CustomerVO customer = cm.getCustomerByUuid(datasourceName, customerUuid);
		if (customer == null)
		{
%>
        <script language="javascript">
        	 alert("没有登录或登录超时,请重新登录");
			 window.location.href="/bd.html";
		</script>
<%
	return;  
		}
		
		UserVO user = um.login(datasourceName,username,password);
		
		if (user == null)
		{
%>
        <script language="javascript">
        	alert("没有登录或登录超时,请重新登录");
			window.location.href="/bd.html";
		</script>
<%		  
        return;  
		} 
		session.setAttribute("customerUuid", customerUuid);
%>

<html>
	<head>
		  <title>关键字上传</title>
		  <script type="text/javascript" src="/common.js">
		  </script>	  
  	</head>				
<body>

<form method="post" onsubmit="return checkinput();" action="updateCustomerKeywordGroupNameConfirm.jsp">
	
<table width="100%">
	<tr>
		<td align="right">
			 	   <table width=80% style="font-size:14px;">
			 	   	<tr><td>
	 	   		   		<input type="hidden" id="customerUuid" name="customerUuid" value="<%=customerUuid%>">
			 	   	   目标组名称:<input type="text" id="groupName" name="groupName" size=50>
                       <input type="submit" value="修改">
                       </td>
		           </tr>
		       </table>
  </td>
  </tr>
</table>
</form>	 
</body>
</html>




