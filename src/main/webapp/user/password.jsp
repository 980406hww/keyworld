<%@page contentType="text/html;charset=utf-8"   errorPage="error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,java.util.*"%>
<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />

<%
	String username = request.getParameter("user");
	String passwordSerial = request.getParameter("ps");

	if (username == null || username.equals("")) {
%>
        <script language="javascript">
				   alert("用户账号不能为空");
				   window.location.href="/";
			  </script>
<%
	return;
	}

	if (passwordSerial == null || passwordSerial.equals("")) {
%>
        <script language="javascript">
				   alert("密钥不对，不能修改密码");
				   window.location.href="/";
			  </script>
<%
	return;
	}

	boolean existPasswordSerial = um.existPasswordSerial("keyword", username, passwordSerial);

	if (existPasswordSerial = false) {
%>
        <script language="javascript">
				   alert("密钥不对，不能修改密码");
				   window.location.href="/";
			  </script>
<%
	return;
	}
%>

<html>
	<head>
		  <title>修改密码</title>
		  
  </head>	
  
  <script language="javascript">
	 	      
				  function checkinput()
				  {
				  	  var password = document.getElementById("password");
				  	  if (password.value == "")
				  	  {
				  	  	  alert("请输入新密码");
				  	  	  password.focus();
				  	  	  return false;
				  	  }
				  	  
				  	  var password2 = document.getElementById("password2");
				  	  if (password2.value == "")
				  	  {
				  	  	  alert("请再次输入新密码");
				  	  	  password2.focus();
				  	  	  return false;
				  	  }
				  	  
				  	  if (password.value != password2.value)
				  	  {
				  	  	  alert("两次输入的密码不一致，请重新输入");
				  	  	  password.focus();
				  	  	  return false;
				  	  }
				  	  
				  	  
				  	  var btnsub = document.getElementById("btnsub");
				  	  btnsub.disabled = true;
				  	  return true; 
				  }
				  
				  function isNum(s)
					{
					   var r,re;
					   re = /\d*/i; 
					   r = s.match(re);
					   return (r==s)?true:false;
					}
					
					function checkIsChinese(str) 
					{   
							var  index = escape(str).indexOf("%u");  
              if(index < 0){return false;}else{return true}
					}
					
					function trim(val)
					{
						  var re = /\s*((\S+\s*)*)/;
				      return val.replace(re, "$1"); 
					}
					
			</script>
			
<body>
	
<form method="post" onsubmit="return checkinput();" action="/user/password2.jsp">
	<input type="hidden" name="ps" value="<%=passwordSerial%>">
<table width="100%" style="font-size:12px;" >
	<tr>
		<td align="center">
	     
	 	   <div style="text-align:right;width:700px;"><a href="/">回到首页</a> | <a href="/bd.html">登录</a></div>
	 	   
			 	   <table width=700 style="font-size:12px;" cellpadding=5>
			 	   	  
			 	   	  <tr><td align="right">注册账号:</td> <td><input type="text" name="username" id="username" value="<%=username%>" disable readonly style="width:200px;"> 账号不能修改</td></tr>
			 	   	  <tr><td align="right">新密码:</td> <td><input type="password" name="password" id="password" value="" style="width:200px;"> </td></tr>
			 	   	  <tr><td align="right">确认密码:</td> <td><input type="password" name="password2" id="password2" value="" style="width:200px;"> </td></tr>
			 	   	  
		           <tr><td align="right"></td> 
		            	<td>
		            	    <input type="submit" name="btnsub" id="btnsub" value=" 提交 ">
		            </td>
		           </tr>
		       </table>
  </td>
  </tr>
</table>

<center>
	  
</center>
</form>	 
</body>
<html>




