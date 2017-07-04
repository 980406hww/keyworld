<%@page contentType="text/html;charset=utf-8"   errorPage="error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,java.util.*"%>

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
		if (!user.isVipType())
		{
%>
        <script language="javascript">
        	alert("你没有该权限！");
        	window.history.back();
	  	</script>
<%		  
        return;  
		}		
		
		String userID = request.getParameter("userID");
		
		UserVO tmpUserVO = um.getUserByUserID("keyword", userID);
		if (tmpUserVO == null)
		{
%>
        <script language="javascript">
        	 alert("没有这个数据，不能修改");
			 window.location.href="userlist.jsp";
		</script>
<%
	return;  
		} 
%>

<html>
	<head>
		  <title>用户充值</title>
		  
  </head>	
  
  <script language="javascript">
	 	      
				  function checkinput()
				  {
				  	  var amount = document.getElementById("amount");
				  	  if (amount.value == "")
				  	  {
				  	  	  alert("请输入充值金额");
				  	  	  amount.focus();
				  	  	  return false;
				  	  }
				  	  
				  	  if (!isNum(amount.value))
				  	  {
				  	  	  alert("请输入数值");
				  	  	  amount.focus();
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
	
<form method="post" onsubmit="return checkinput();" action="/user/rechargeconfirm.jsp">
	
<table width="100%" style="font-size:12px;">
	<tr>
		<td align="center">
	     
	 	   <div style="text-align:right;width:700px;"></div>
	 	   		   <input type="hidden" id="userID" name="userID" value="<%=userID%>">
			 	   <table width=700 style="font-size:12px;" cellpadding=5>
			 	   	  <tr><td align="right">用户ID:</td> <td><%=userID%></td></tr>
			 	   	  <tr><td align="right">余额:</td> <td><%=tmpUserVO.getAccountAmount()%></td></tr>
			 	   	  <tr><td align="right">充值金额:</td> <td><input name="amount" id="amount"  style="width:200px;" type="text"></td></tr>
			 	   	  <tr><td align="right">备注:</td><td><textarea name="remarks" id="remarks" style="width:200px;height:100px"></textarea></td></tr>
		          
		          
		           <tr><td align="right"></td> 
		            	<td>
		            	    <input type="submit" name="btnsub" id="btnsub" value=" 充值 "> &nbsp;&nbsp;&nbsp; <input type="button" onclick="javascript:history.go(-1);" value=" 返回 ">
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




