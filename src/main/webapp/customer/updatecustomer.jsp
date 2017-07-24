<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
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
		 
		String uuid = request.getParameter("uuid");
		
		CustomerVO customer = cm.getCustomerByUuid("keyword", uuid);
		if (customer == null)
		{
%>
        <script language="javascript">
        	 alert("没有这个数据，不能修改");
			 window.location.href="customerlist.jsp";
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
		if ((user.getStatus() == 0) || !(user.isVipType() || user.getUserLevel() == 1) || !username.equals(customer.getUserID()))
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
		  <title>修改客户</title>
		  
  </head>	
  
  <script language="javascript">
	 	      
	  function checkinput()
	  {
	  	  var contactPerson = document.getElementById("contactPerson");
	  	  if (trim(contactPerson.value) == "")
	  	  {
	  	  	  alert("没有输入联系人");
	  	  	  contactPerson.focus();
	  	  	  return false;
	  	  }
	  	  
	  	  contactPerson.value = trim(contactPerson.value);
	  	  
	  	  var qq = document.getElementById("qq");
	  	  qq.value = trim(qq.value);
	  	  if (qq.value != "")
	  	  {				  		  
	  		  if (isNum(qq.value) == false)
	  		  {
		  	  	  alert("无效的QQ号码！");
		  		  qq.focus();
		  	  	  return false;				  			  
	  		  }
	  	  }				  	   	  				  	  				  	  
	  	  
	  	  var paidFee = document.getElementById("paidFee");
	  	  if (paidFee.value != "")
	  	  {
	  		  if (isNum(paidFee.value) == false)
		  	  {
		  	  	  alert("金额请用阿拉伯数字！");
		  	  	  paidFee.focus();
		  	  	  return false;
		  	  }
	  	  }
	  }

				  
	function isNum(s)
	{
	   var r,re;
	   re = /\d*/i; 
	   r = s.match(re);
	   return (r==s)?true:false;
	}
  
	function trim(val)
	{
		  var re = /\s*((\S+\s*)*)/;
      return val.replace(re, "$1"); 
	}
					
</script>
			
<body>
	
<form method="post" onsubmit="return checkinput();" action="updatecustomerconfirm.jsp">
	<input type="hidden" name="uuid" value="<%=uuid%>">
<table width="100%">
	<tr>
		<td align="center">
	     
	 	   <div style="text-align:right;width:700px;font-size:12px;"><a href="customerlist.jsp">返回客户列表</a></div>	 	   
			 	   <table width=700 style="font-size:12px;" cellpadding=5>
			 	   	  <input type="hidden" name="userID" value="<%=customer.getUserID()%>">
			 	   	  <tr><td align="right">联系人:</td> <td><input type="text" name="contactPerson" id="contactPerson" value="<%=customer.getContactPerson()%>" style="width:200px;"></td></tr>
			 	   	  <tr><td align="right">QQ:</td> <td><input type="text" name="qq" id="qq" value="<%=customer.getQq()%>" style="width:200px;" ></td></tr>
			 	   	  <tr><td align="right">Email:</td> <td><input name="email" id="email" value="<%=customer.getEmail()%>" style="width:200px;" type="text"></td></tr>
		          	  <tr><td align="right">联系电话：</td><td><input type="text" name="telphone" id="telphone" value="<%=customer.getTelphone()%>" style="width:200px;"></td></tr>
		          	  <tr><td align="right">支付宝：</td><td><input type="text" name="alipay" id="alipay" value="<%=customer.getAlipay()%>" style="width:200px;">用户付款过的支付宝</td></tr>
		          	  <tr><td align="right">已付金额总数：</td><td><input type="text" name="paidFee" id="paidFee" value="<%=customer.getPaidFee()%>" style="width:200px;">曾经付过款项总额</td></tr>
		          	  
		          	  <tr><td align="right">客户类型:</td> 
			          	  <td>
			          	  	 <select name="type" id="type">
			          	  	 	 <%
				          	  	     String []typeNames = {"普通客户","代理"};	
				          	  	     for (int i = 0; i < 2; i ++)
				          	  	     {
				          	  	         if (typeNames[i].equals(customer.getType()))
				          	  	         {
				          	  	              out.println("<option selected value='" + typeNames[i] + "'>" + typeNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + typeNames[i] + "'>" + typeNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>		          	  	 	  
			          	  	 </select>   
			              </td>
			          </tr>    
			          <tr><td align="right">客户状态:</td> 
			          	  <td>
			          	  	 <select name="isDelete" id="isDelete">
			          	  	 	 <%
				          	  	     String []statusNames = {"激活","暂停"};	
			          	  	 	     int []statusValues = {1, 2};
				          	  	     for (int i = 0; i < 2; i ++)
				          	  	     {
				          	  	         if (statusValues[i] == customer.getStatus())
				          	  	         {
				          	  	              out.println("<option selected value='" + statusValues[i] + "'>" + statusNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + statusValues[i] + "'>" + statusNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>		          	  	 	  
			          	  	 </select>   
			              </td>
			           </tr>
				       <tr><td align="right">备注：</td><td><textarea name="remark" id="remark" style="width:200px;height:100px"><% if (customer.getRemark() != null){out.println(customer.getRemark());}%></textarea></td></tr>
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




