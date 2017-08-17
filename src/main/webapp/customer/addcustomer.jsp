<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />

<style>
   .hiddentr {display:none;}
   .displaytr {dislay:;}
</style>

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
		if ((user.getStatus() == 0) || !(user.isVipType() || user.getUserLevel() == 1))
		{
%>
        <script language="javascript">
        	alert("你没有该权限！");
        	window.history.back();
	  	</script>
<%		  
        return;  
		}		
%>

<html>
	<head>
		<title>增加客户</title>
		<script language="javascript" src="/common.js">
		</script>		  
		<script language="javascript" src="customer.js">
		</script>
		<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	    <script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	    <link href="/css/menu.css" rel="stylesheet" type="text/css" />	
  </head>
			
<body>
	
<form method="post" onsubmit="return checkinput();" action="addcustomerconfirm.jsp">
	
<table width="100%"  style="font-size:12px;" >
     <tr>
		<td colspan=1 align="left">
			<%@include file="/menu.jsp" %>	
		</td>
	</tr>
	<tr>
		<td align="center">
	     
	 	   <div style="text-align:right;width:1200px;"><a href="customerlist.jsp">返回客户列表</a></div>
	 	   
			 	   <table width=700 style="font-size:14px;" cellpadding=5>
			 	   	  
			 	   	  <tr><td align="right">联系人:</td> <td><input type="text" name="contactPerson" id="contactPerson" value="" style="width:200px;"></td></tr>
			 	   	  <tr><td align="right">QQ:</td> <td><input type="text" name="qq" onBlur="checkQQExisting();" id="qq" value="" style="width:200px;" ><div class="hiddentr" id="qqExisting"><font color="red">该QQ在系统中已经存在！</font></div></td></tr>			 	   	  
		          	  <tr><td align="right">联系电话：</td><td><input type="text" name="telphone" id="telphone" onBlur="checkTelphoneExisting();" value="" style="width:200px;"><div class="hiddentr" id="telphoneExisting"><font color="red">该电话在系统中已经存在！</font></div></td></tr>		          	 		          	  
		          	  <tr><td align="right">客户类型:</td> 
			          	  <td>
			          	  	 <select name="type" id="type">			          	  	 		  
			          	  	 	  <option value="普通客户">普通客户</option>
			          	  	 	  <option value="代理">代理</option>			          	  	 	  
			          	  	 </select>   
			              </td>
			           </tr>
			           
			          <tr><td align="right">客户状态:</td> 
			          	  <td>
			          	  	 <select name="status" id="status">
			          	  	 	  <option value="1">激活</option>
			          	  	 	  <option value="2">暂停</option>			          	  	 	  
			          	  	 </select>   
			              </td>
			           </tr>
				       <tr><td align="right">备注：</td><td><textarea name="remark" id="remark" value="" style="width:200px;height:100px"></textarea></td></tr>
		           <tr><td align="right"></td> 
		            	<td>
		            	    <input type="submit" name="btnsub" id="btnsub" value=" 提交 ">
		            </td>
		           </tr>
		       </table>
  </td>
  </tr>
</table>
</form>	 
</body>
<html>