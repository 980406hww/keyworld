<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="spm" scope="page" class="com.keymanager.manager.ServiceProviderManager" />
	
<%@include file="/check.jsp" %>

<html>
<header>
<style>
.wrap {word-break: break-all; word-wrap:break-word;}

<!--
#div1{ 
	display:none; 
	background-color :#f6f7f7; 
	color:#333333; 
	font-size:12px; 
	line-height:18px; 
	border:1px solid #e1e3e2; 
	width:450; 
	height:50;
}
-->
</style>

	<script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	<link href="/css/menu.css" rel="stylesheet" type="text/css" />	
</header>

<%
	if(loginState == 0)
	{
%>
       <script language="javascript">
		window.location.href="/bd.html";
	   </script>
<%
       return;
	}
	
	
	String username = (String) session.getAttribute("username");
	String password = (String) session.getAttribute("password");
	username = Utils.parseParam(username);
	password = Utils.parseParam(password);
	
	if (username == null || username.equals(""))
	{
%>
       <script language="javascript">
			window.location.href="/bd.html";
		</script>
<%
	return;  
	}
	
	
	UserVO user = new UserVO();
	
	user = um.login(datasourceName,username,password);
	
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


<body>
      <table width="100%" style="font-size:12px;" cellpadding=3>
          <tr>
			<td colspan=13 align="left">
				<%@include file="/menu.jsp" %>	
			</td>
		  </tr>
      	  <tr>
      	  	 <td>
      	  	 	<a href="./getcustomerkeyword.jsp?username=keyadmin&password=S.times123&clientID=codeverification" target="_blank">取刷词</a>
      	  	 	<a href="./updateOptimizedCount.jsp?username=keyadmin&password=S.times123&uuid=244982&count=1&clientID=codeverification&freespace=500&ip=120.0.0.1&version=2.10.2&city=珠海电信" target="_blank">更新刷的结果</a>
      	  	 	<a href="./getkeywordlist.jsp?username=keyadmin&password=S.times123&group=codeverification&type=PC&captureindex=0&count=1"
				   target="_blank">取抓排名词</a>
      	  	 	<a href="./customerkeyword/getFumianName.jsp?group=%E5%8E%9F%E6%96%99%E7%94%9F%E4%BA%A7" target="_blank">取负面词(分组原料生产)</a>
      	  	 </td>
      	  </tr>      	  
          
          <tr>
          	<td colspan=9>
          		  <br>
          	</td>
          </tr>          
      </table>
</body>
</html>

