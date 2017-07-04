<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="flm" scope="page" class="com.keymanager.manager.FumianListManager" />

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
		  <title>增加关键字负面清单</title>
		  <script type="text/javascript" src="/common.js">
		  </script>	  	
		  <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
		  <script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
		  <script language="javascript" type="text/javascript" src="/js/slide.js"></script>
		  <link href="/css/menu.css" rel="stylesheet" type="text/css" />	  	  
  	</head>	
			
<body>
	
<form method="post" onsubmit="return checkinput();" action="addconfirm.jsp">
	
<table width="100%"  style="font-size:12px;">
	<tr>
		<td colspan=1 align="left">
			<%@include file="/menu.jsp" %>	
		</td>
	</tr>
	<tr>
		<td align="right">
	     
	 	   <div style="text-align:right;width:100%;font-size:12px;"><a href="list.jsp">返回关键字负面清单</a></div>
	 	   
	 	   <table width="95%" style="font-size:12px;">
	 	   	  <tr height="30"><td align="right">关键字<font color="red">*</font>:</td> <td><input type="text" name="keyword" id="keyword" value="" style="width:1000px;"></td></tr>
	 	   	  <tr height="30"><td align="right">URL<font color="red">*</font>:</td> <td><input type="text" name="url" id="url" value="" style="width:1000px;" ></td></tr>
	 	   	  <tr height="30"><td align="right">标题<font color="red">*</font>:</td> <td><input type="text" name="title" id="title" value="" style="width:1000px;" ></td></tr>
	 	   	  <tr height="30"><td align="right">描述<font color="red">*</font>:</td> <td><input type="text" name="desc" id="desc" value="" style="width:1000px;" ></td></tr>
	 	   	  <tr height="30"><td align="right">排名:</td> <td><input name="position" id="position" value="" style="width:1000px;" type="text"></td></tr>
	       </table>	
		</td>
	</tr>   
     <tr height="30"> 
      	<td align="center">
      	    <input type="submit" name="btnsub" id="btnsub" value=" 添加关键字负面清单 ">
      </td>
     </tr>
</table>
</form>	 
</body>
<script>
function checkinput() {
	var keyword = document.getElementById("keyword");
	keyword.value = trim(keyword.value);
	if (keyword.value == "") {
		alert("请输入关键字！");
		keyword.focus();
		return false;
	}

	var url = document.getElementById("url");
	url.value = trim(url.value.toLowerCase());
	if (url.value == "") {
		alert("请输入URL");
		url.focus();
		return false;
	}

	var title = document.getElementById("title");
	title.value = trim(title.value);
	if (title.value == "") {
		alert("请输入URL");
		title.focus();
		return false;
	}
	
	var desc = document.getElementById("desc");
	desc.value = trim(desc.value);
	if (desc.value == "") {
		alert("请输入描述");
		desc.focus();
		return false;
	}
	
	var position = document.getElementById("position");
	position.value = trim(position.value);
	if (position.value == "") {
		alert("请输入排名");
		position.focus();
		return false;
	}
}
</script>
<html>




