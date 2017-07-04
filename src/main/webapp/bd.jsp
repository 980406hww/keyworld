<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.util.*,java.util.*" %>
<body leftmargin="2" topmargin="0" style="overflow:hidden;" onload="focusin()" style="font-size:12px;" >  

<form action="/user/confirm.jsp" method="post" onsubmit="return checkInput();">
	
<table style="" width="100%" border="0" cellpadding="2" cellspacing="0" style="font-size:12px;" >
<%
	String entry = request.getParameter("rukou");
	if(entry == null || !EntryUtils.validate(entry)){
%>
	<script language="javascript">
     	alert("没有指定入口参数或者值不对！");
	</script>
<%	
		return;
	}
	session.setAttribute("entry", entry);
%>
	<tr>
		<td style="padding-top:8px;">
			<div id="submitInfo">请输入您的帐号!</div>
		</td>
	</tr>
	<tr>
		<td>
			<span class="text1">输入：</span><input name="userID" id="userID" type="text" value=""> 
			<a href="/user/getpassword.jsp">找回密码点这里</a> | 
			<a href="/user/reg.jsp">注册新账号</a> | 
			<a href="/bd.jsp?rukou=fm">负面链接</a> | 
			<a href="/bd.jsp?rukou=qz">全站链接</a> | 
			<a href="/bd.jsp?rukou=pt">普通链接</a>
		</td>
	</tr>
	<tr>
		<td>
			<span class="text1">密码：</span><input name="password" id="password" type="password" value="">
		</td>
	</tr>
	<tr>
		<td style="padding-left:60px">
			<input type="submit" name="btnsub" id="btnsub" value=" 登 录 "/>
		</td>
	</tr>

</table>
</form>
<script language="javascript">
  function focusin()
  {
    user = document.getElementById("userID");
    user.focus();
  }
  
  function checkInput()
  {
  	 user = document.getElementById("userID");
  	 if (user.value == "")
  	 {
  	 	   alert("请输入用户账号");
  	 	   user.focus();
  	 	   return false;
  	 }
  	 
  	 pass = document.getElementById("password");
  	 if (pass.value == "")
  	 {
  	 	   alert("请输入用户密码");
  	 	   pass.focus();
  	 	   return false;
  	 }
  	 
  	 btnsub = document.getElementById("btnsub");
  	 
  	 btnsub.disabled = true;
  	 
  	 return true;
    
  }
</script>