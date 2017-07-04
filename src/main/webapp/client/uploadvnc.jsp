<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />

<style>
   .hiddentr {display:none;}
   .displaytr {dislay:;}
</style>

<script>
	function checkinput(){
		var file = document.getElementById("file");
		if(file.value == ""){
			alert("请选择要上传的VNC配置文件!");
			return false;
		}
		var posIndex = file.value.indexOf(".xml");
		if (posIndex == -1) {
			alert("只能上传的XML文件！");
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
%>

<html>
	<head>
		  <title>VNC配置文件上传</title>
		  <script type="text/javascript" src="/common.js">
		  </script>
  	</head>				
<body>

<form method="post" onsubmit="return checkinput();" action="uploadvncconfirm.jsp" enctype="multipart/form-data">
	
<table width="100%">
	<tr>
		<td align="right">	     
	 	   <div style="text-align:right;width:100%;font-size:12px;"><a href="/client/clientlist.jsp">返回终端列表</a></div>
			 	   <table width=80% style="font-size:14px;">
			 	   	<tr><td>
			 	   	   请选择要上传的VNC配置文件:<input type="file" id="file" name="file" size=50>
                       <input type="submit" value="提交">
                       </td>
		           </tr>
		       </table>
  </td>
  </tr>
</table>
</form>	 
</body>
</html>




