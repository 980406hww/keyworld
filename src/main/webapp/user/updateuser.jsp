<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
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
		  <title>修改用户信息</title>
		  
  </head>	
  
  <script language="javascript">
	 	      
	  function checkinput()
	  {
	  	  var userName = document.getElementById("userName");
	  	  if (trim(userName.value) == "")
	  	  {
	  	  	  alert("没有输入用户名称");
	  	  	  userName.focus();
	  	  	  return false;
	  	  }
	  	  
	  	  userName.value = trim(userName.value);
	  	  
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
	  	  
	  	  var percentage = document.getElementById("percentage");
	  	  if (percentage.value != "")
	  	  {
	  		  if (isNum(percentage.value) == false)
		  	  {
		  	  	  alert("提成比例请用阿拉伯数字！");
		  	  	  percentage.focus();
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
	
<form method="post" onsubmit="return checkinput();" action="updateuserconfirm.jsp">
	<input type="hidden" name="userID" value="<%=userID%>">
<table width="100%">
	<tr>
		<td align="center">
	     
	 	   <div style="text-align:right;width:700px;font-size:12px;"><a href="userlist.jsp">返回用户列表</a></div>	 	   
			 	   <table width=700 style="font-size:12px;" cellpadding=5>
			 	   	  <tr><td align="right">用户名称:</td> <td><input type="text" name="userName" id="userName" value="<%=tmpUserVO.getUserName()%>" style="width:200px;"></td></tr>
		          	  <tr><td align="right">性别：</td>
		          	  	<td>
			          	  	 <select name="gender" id="gender">
			          	  	 	 <%
				          	  	     String []genderNames = {"男","女"};	
				          	  	     for (int i = 0; i < genderNames.length; i ++)
				          	  	     {
				          	  	         if (genderNames[i].equals(tmpUserVO.getGender()))
				          	  	         {
				          	  	              out.println("<option selected value='" + genderNames[i] + "'>" + genderNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + genderNames[i] + "'>" + genderNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>		          	  	 	  
			          	  	 </select>   
			              </td>
		          	  </tr>
			 	   	  <tr><td align="right">QQ:</td> <td><input type="text" name="qq" id="qq" value="<%=tmpUserVO.getQq()%>" style="width:200px;" ></td></tr>			 	   	  
		          	  <tr><td align="right">电话：</td><td><input type="text" name="phone" id="phone" value="<%=tmpUserVO.getPhone()%>" style="width:200px;"></td></tr>
		          	  <tr><td align="right">提成比例：</td><td><input type="text" name="percentage" id="percentage" value="<%=tmpUserVO.getPercentage()%>" style="width:200px;">直接输入整数即可</td></tr>
		          	  
		          	  <tr><td align="right">用户级别:</td> 
			          	  <td>
			          	  	 <select name="userLevel" id="userLevel">
			          	  	 	 <%
				          	  	     String []userLevelNames = {"一级用户","二级用户"};	
			          	  	 		 int []userLevelValues = {1,2};	
				          	  	     for (int i = 0; i < userLevelNames.length; i ++)
				          	  	     {
				          	  	         if (userLevelValues[i] == tmpUserVO.getUserLevel())
				          	  	         {
				          	  	              out.println("<option selected value='" + userLevelValues[i] + "'>" + userLevelNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + userLevelValues[i] + "'>" + userLevelNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>		          	  	 	  
			          	  	 </select>   
			              </td>
			          </tr>   
			          <tr><td align="right">收费方式:</td> 
			          	  <td>
			          	  	 <select name="collectMethod" id="collectMethod">
			          	  	 	 <%
				          	  	     String []collectMethodNames = {" 全收","都有排名，只收电脑","都有排名，只收手机","电脑和手机都收，手机只收一个"};	
				          	  	     for (int i = 0; i < collectMethodNames.length; i ++)
				          	  	     {
				          	  	         if (collectMethodNames[i].equals(tmpUserVO.getCollectMethod()))
				          	  	         {
				          	  	              out.println("<option selected value='" + collectMethodNames[i] + "'>" + collectMethodNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + collectMethodNames[i] + "'>" + collectMethodNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>		          	  	 	  
			          	  	 </select>   
			              </td>
			          </tr> 
			          <tr><td align="right">自动收费:</td> 
			          	  <td>
			          	  	 <select name="autoPay" id="autoPay">
			          	  	 	 <%
				          	  	     String []autoPayNames = {"是","否"};	
			          	  	 		 boolean []autoPayValues = {true,false};	
				          	  	     for (int i = 0; i < autoPayNames.length; i ++)
				          	  	     {
				          	  	         if (autoPayValues[i] == tmpUserVO.isAutoPay())
				          	  	         {
				          	  	              out.println("<option selected value='" + autoPayValues[i] + "'>" + autoPayNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + autoPayValues[i] + "'>" + autoPayNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>		          	  	 	  
			          	  	 </select>   
			              </td>
			          </tr>     
			          <tr><td align="right">用户状态:</td> 
			          	  <td>
			          	  	 <select name="isDelete" id="isDelete">
			          	  	 	 <%
				          	  	     String []statusNames = {"激活","暂停"};	
			          	  	 	     int []statusValues = {1, 0};
				          	  	     for (int i = 0; i < statusNames.length; i ++)
				          	  	     {
				          	  	         if (statusValues[i] == tmpUserVO.getStatus())
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




