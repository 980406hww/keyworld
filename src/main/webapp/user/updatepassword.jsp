<%@page contentType="text/html;charset=utf-8"   errorPage="error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,java.util.*"%>

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
		 
		 
%>

<html>
	<head>
		  <title>修改密码</title>
		  
  </head>	
  
  <script language="javascript">
	 	      
				  function checkinput()
				  {
				  	  var oldpassword = document.getElementById("oldpassword");
				  	  if (oldpassword.value == "")
				  	  {
				  	  	  alert("请输入旧密码");
				  	  	  oldpassword.focus();
				  	  	  return false;
				  	  }
				  	  
				  	  var newpassword = document.getElementById("newpassword");
				  	  if (newpassword.value == "")
				  	  {
				  	  	  alert("请输入新密码");
				  	  	  newpassword.focus();
				  	  	  return false;
				  	  }
				  	  
				  	  var newpassword2 = document.getElementById("newpassword2");
				  	  if (newpassword2.value == "")
				  	  {
				  	  	  alert("请再次输入新密码");
				  	  	  newpassword2.focus();
				  	  	  return false;
				  	  }
				  	  
				  	  if (newpassword.value != newpassword2.value)
				  	  {
				  	  	  alert("新密码前后两次输入的不一致，请重新输入");
				  	  	  newpassword2.focus();
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
	
<form method="post" onsubmit="return checkinput();" action="/user/updatepasswordconfirm.jsp">
	
<table width="100%" style="font-size:12px;">
	<tr>
		<td align="center">
	     
	 	   <div style="text-align:right;width:700px;"></div>
	 	   
			 	   <table width=700 style="font-size:12px;" cellpadding=5>
			 	   	  <tr><td align="right">旧密码:</td> <td><input type="password" name="oldpassword" id="oldpassword" value="" style="width:200px;"> </td></tr>
			 	   	  <tr><td align="right">新密码:</td> <td><input type="password" name="newpassword" id="newpassword" value="" style="width:200px;" ></td></tr>
			 	   	  <tr><td align="right">再次输入:</td> <td><input name="newpassword2" id="newpassword2"  style="width:200px;" type="password"></td></tr>
		          
		          
		           <tr><td align="right"></td> 
		            	<td>
		            	    <input type="submit" name="btnsub" id="btnsub" value=" 提交 "> &nbsp;&nbsp;&nbsp; <input type="button" onclick="javascript:history.go(-1);" value=" 返回 ">
		            </td>
		           </tr>
		       </table>
  </td>
  </tr>
</table>

<br><br><br>
</form>	 
</body>
<html>




