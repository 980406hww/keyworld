<%@page contentType="text/html;charset=utf-8" errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />
<jsp:useBean id="spm" scope="page" class="com.keymanager.manager.ServiceProviderManager" />

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
		CustomerKeywordVO customerKeyword = ckm.getCustomerKeywordByUuid(datasourceName, uuid);
		if(customerKeyword == null)
		{
	%>
		      <script language="javascript">
		      	alert("你没有该权限！");
		      	window.history.back();
			  </script>
	<%	
		return;
		}
		String customerUuid = customerKeyword.getCustomerUuid() + "";	
		CustomerVO customer = cm.getCustomerByUuid(datasourceName, customerUuid);
		
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
		
		if ((user.getStatus() == 0) || !(user.isVipType() || user.getUserLevel() == 1) || !(customer.getUserID().equals(username)))
		{
%>
	      <script language="javascript">
	      	alert("你没有该权限！");
	      	window.history.back();
		  </script>
<%			
		return;
		}
		List serviceProviders = spm.getAllActiveServiceProvider(datasourceName);
%>

<html>
	<head>
		  <title>修改关键字</title>
		  <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
		  <script type="text/javascript" src="/common.js"></script>
		  <script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
		  <script language="javascript" type="text/javascript" src="/js/slide.js"></script>
		  <link href="/css/menu.css" rel="stylesheet" type="text/css" />
  </head>	
  
  <script language="javascript">
	  function checkinput()
	  {
	  	  var keyword = document.getElementById("keyword");
	  	  keyword.value = trim(keyword.value);
	  	  if (keyword.value == "")
	  	  {
	  	  	  alert("没有输入关键字");
	  	  	  keyword.focus();
	  	  	  return false;
	  	  }
	  	  
	  	  var url = document.getElementById("url");
	  	  var phoneUrl = document.getElementById("phoneUrl");
	  	  if (url.value == "" && phoneUrl.value == "")
	  	  {
	  	  	  alert("请输入PC网址或者手机网址");
	  	  	  url.focus();
	  	  	  return false;
	  	  }
	  	  
	  	  if (url.value.indexOf("http") != -1)
	  	  {
	  	  	  var tmpUrl = url.value;
	  	  	  var posIndex = tmpUrl.indexOf("http");
	  	  	  tmpUrl = tmpUrl.substring(posIndex + 4);
	  	  	  posIndex = tmpUrl.indexOf("//");
	  	  	  if (posIndex != -1)
  	  	  	  {
  	  	  	  	  tmpUrl = tmpUrl.substring(posIndex + 2);
  	  	  	  }				  	  	  
	  	  	  alert("网址不能含有http://,输入" + tmpUrl + "即可");
	  	  	  url.focus();
	  	  	  return false;
	  	  }
	  	  
//	  	  if (checkIsChinese(url.value) == true)
//	  	  {
//	  	  	  alert("不要在网址中输入中文字符,请仔细看，要输入的是网址，不是标题");
//	  	  	  url.focus();
//	  	  	  return false;
//	  	  }
	  	  
	  	  if (phoneUrl.value.indexOf("http") != -1)
	  	  {
	  	  	  var tmpUrl = phoneUrl.value;
	  	  	  var posIndex = tmpUrl.indexOf("http");
	  	  	  tmpUrl = tmpUrl.substring(posIndex + 4);
	  	  	  posIndex = tmpUrl.indexOf("//");
	  	  	  if (posIndex != -1)
  	  	  	  {
  	  	  	  	  tmpUrl = tmpUrl.substring(posIndex + 2);
  	  	  	  }				  	  	  
	  	  	  alert("网址不能含有http://,输入" + tmpUrl + "即可");
	  	  	  phoneUrl.focus();
	  	  	  return false;
	  	  }

	  	  var positionFirstCost = document.getElementById("positionFirstCost");
	  	  if (positionFirstCost.value != "")
	  	  {
		  	  if (isNum(positionFirstCost.value) == false)
		  	  {
		  	  	  alert("请输入排名第一成本金额，不要输入字母或者其他字符");
		  	  	  positionFirstCost.focus();
		  	  	  return false;
		  	  }
	  	  }
	  	  
	  	  var positionSecondCost = document.getElementById("positionSecondCost");
	  	  if (positionSecondCost.value != "")
	  	  {
		  	  if (isNum(positionSecondCost.value) == false)
		  	  {
		  	  	  alert("请输入排名第二成本金额，不要输入字母或者其他字符");
		  	  	  positionSecondCost.focus();
		  	  	  return false;
		  	  }
	  	  }
	  	  
	  	  
	  	  var positionThirdCost = document.getElementById("positionThirdCost");
	  	  if (positionThirdCost.value != "")
	  	  {
		  	  if (isNum(positionThirdCost.value) == false)
		  	  {
		  	  	  alert("请输入排名第三成本金额，不要输入字母或者其他字符");
		  	  	  positionThirdCost.focus();
		  	  	  return false;
		  	  }
	  	  }
	  	  
	  	  
	  	  var positionFirstFee = document.getElementById("positionFirstFee");
	  	  if (positionFirstFee.value != "")
	  	  {
		  	  if (isNum(positionFirstFee.value) == false)
		  	  {
		  	  	  alert("请输入排名第一报价金额，不要输入字母或者其他字符");
		  	  	  positionFirstFee.focus();
		  	  	  return false;
		  	  }
	  	  }
	  	  
	  	  
	  	  
	  	  var positionSecondFee = document.getElementById("positionSecondFee");
	  	  if (positionSecondFee.value != "")
	  	  {
		  	  if (isNum(positionSecondFee.value) == false)
		  	  {
		  	  	  alert("请输入排名第二报价金额，不要输入字母或者其他字符");
		  	  	  positionSecondFee.focus();
		  	  	  return false;
		  	  }
	  	  }
	  	  
	  	  
	  	  var positionThirdFee = document.getElementById("positionThirdFee");
	  	  if (positionThirdFee.value != "")
	  	  {
		  	  if (isNum(positionThirdFee.value) == false)
		  	  {
		  	  	  alert("请输入排名第三报价金额，不要输入字母或者其他字符");
		  	  	  positionThirdFee.focus();
		  	  	  return false;
		  	  }
	  	  }
	  	  
	  	  var optimizePositionFirstPercentage = document.getElementById("optimizePositionFirstPercentage");
	  	  if (isNum(optimizePositionFirstPercentage.value) == false)
	  	  {
	  	  	  alert("第一页优化比例不要输入字母或者其他字符");
	  	  	  optimizePositionFirstPercentage.focus();
	  	  	  return false;
	  	  }
	  	
	  	  var optimizePositionSecondPercentage = document.getElementById("optimizePositionSecondPercentage");
	  	  if (isNum(optimizePositionSecondPercentage.value) == false)
	  	  {
	  	  	  alert("第二页优化比例不要输入字母或者其他字符");
	  	  	  optimizePositionSecondPercentage.focus();
	  	  	  return false;
	  	  }
	  	
	  	  var optimizePositionThirdPercentage = document.getElementById("optimizePositionThirdPercentage");
	  	  if (optimizePositionThirdPercentage.value != "")
	  	  {
		  	  if (isNum(optimizePositionThirdPercentage.value) == false)
		  	  {
		  	  	  alert("第三页优化比例不要输入字母或者其他字符");
		  	  	  optimizePositionThirdPercentage.focus();
		  	  	  return false;
		  	  }
	  	  }
	  	  
	  	  var effectiveFromTime = document.getElementById("effectiveFromTime");
	  	  effectiveFromTime.value = trim(effectiveFromTime.value);
	  	  if (effectiveFromTime.value != "" && !isDate(effectiveFromTime.value))
	  	  {
	  	  	  alert("请输入有效的日期！");
	  	  	  effectiveFromTime.focus();
	  	  	  return false;
	  	  }
	  	  
	  	  var effectiveToTime = document.getElementById("effectiveToTime");
	  	  effectiveToTime.value = trim(effectiveToTime.value);
	  	  if (effectiveToTime.value != "" && !isDate(effectiveToTime.value))
	  	  {
	  	  	  alert("请输入有效的日期！");
	  	  	  effectiveToTime.focus();
	  	  	  return false;
	  	  }

	  	  var phoneOptimizePositionFirstPercentage = document.getElementById("phoneOptimizePositionFirstPercentage");
	  	  if (isNum(phoneOptimizePositionFirstPercentage.value) == false)
	  	  {
	  	  	  alert("第一页优化比例不要输入字母或者其他字符");
	  	  	  phoneOptimizePositionFirstPercentage.focus();
	  	  	  return false;
	  	  }
	  	
	  	  var phoneOptimizePositionSecondPercentage = document.getElementById("phoneOptimizePositionSecondPercentage");
	  	  if (isNum(phoneOptimizePositionSecondPercentage.value) == false)
	  	  {
	  	  	  alert("第二页优化比例不要输入字母或者其他字符");
	  	  	  phoneOptimizePositionSecondPercentage.focus();
	  	  	  return false;
	  	  }
	  	
	  	  var phoneOptimizePositionThirdPercentage = document.getElementById("phoneOptimizePositionThirdPercentage");
	  	  if (phoneOptimizePositionThirdPercentage.value != "")
	  	  {
		  	  if (isNum(phoneOptimizePositionThirdPercentage.value) == false)
		  	  {
		  	  	  alert("第三页优化比例不要输入字母或者其他字符");
		  	  	  phoneOptimizePositionThirdPercentage.focus();
		  	  	  return false;
		  	  }
	  	  }
	  	  
	  	  var serviceProvider = document.getElementById("serviceProvider");
	  	  if (serviceProvider.value == "")
	  	  {
	  	  	  alert("服务商必须选择！");
	  	  	  serviceProvider.focus();
	  	  	  return false;
	  	  }
	  	  return true; 
	  }
	  
	function isStartWith(str,substr1)
	{
		if (str == null || str == "" || substr1 == null || substr1 == "" || str.length < substr1.length)
		    return false;
		
		if (str.substr(0,substr1.length) == substr1)
		{
			  return true;
		}
		else
		{
			  return false;
		}
	} 

	function isDate(s)
	{
	   var r,re;
	   re = /2[0-9]{3}-[0-9]{2}-[0-9]{2}/i; 
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
			
<body width="1280px"> 	
<form method="post" onsubmit="return checkinput();" action="modifyconfirm.jsp">
<input type="hidden" name="uuid" value="<%=uuid%>">	
<table width="100%"  style="font-size:12px;">
	<tr>
		<td colspan=2 align="left">
			<%@include file="/menu.jsp" %>	
		</td>
	</tr>
	<tr>
	  	<td colspan=2>
	  		<%@include file="/customer/customerinfo.jsp" %>	
	  	</td>
  	</tr>
  	<tr>
  		<td colspan=2>
	 	   <div style="text-align:right;width:100%;font-size:12px;"><a href="list.jsp?isDelete=1&customerUuid=<%=customerUuid%>">返回关键字列表</a></div>
	  	</td>
  	</tr>
	<tr>
		<td align="center">
 	   	   <input type="hidden" name="customerUuid" value="<%=customerUuid%>">
	 	   <table width=1000px style="font-size:12px;">
	 	   	  <tr>
	 	   	  	<td colspan="2">
	 	   	  		<table style="font-size:12px;">
	 	   	  		  <tr align="left">
	 	   	  		  	<td align="right">关键字<font color="red">(*)</font>:</td> 
	 	   	  		  	<td><input type="text" name="keyword" id="keyword" value="<%=customerKeyword.getKeyword()%>" style="width:300px;"></td>
	 	   	  		  	<td align="right">优化组名:</td> 
	 	   	  		  	<td><input name="optimizeGroupName" id="optimizeGroupName" type="text" style="width:120px;" value="<%=customerKeyword.getOptimizeGroupName() == null ? "": customerKeyword.getOptimizeGroupName()%>"></td>
	 	   	  		  	<td align="right">订单号:</td> 
	 	   	  		  	<td><input name="orderNumber" id="orderNumber" type="text" style="width:120px;" value="<%=customerKeyword.getOrderNumber() == null ? "": customerKeyword.getOrderNumber()%>"></td>
	 	   	  		  	<td align="right">收费状态:</td> 
			          	  <td>
			          	  	 <select name="paymentStatus" id="paymentStatus">
			          	  	 	<%
				          	  	     String []paymentStatuses = {"", "担保中", "已付费", "未担保", "跑路"};	
				          	  	     for (int i = 0; i < paymentStatuses.length; i ++)
				          	  	     {
				          	  	         if (paymentStatuses[i].equals(customerKeyword.getPaymentStatus()))
				          	  	         {
				          	  	              out.println("<option selected value='" + paymentStatuses[i] + "'>" + paymentStatuses[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + paymentStatuses[i] + "'>" + paymentStatuses[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>		          	  	 	  
			          	  	 </select>   
			              </td>
	 	   	  		  </tr>
	 	   	  		  <input type="hidden" id="searchEngine" name="searchEngine" value="百度">
		  			  <%
					      java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
						  java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
						  String today = formatter.format(currentTime); //将日期时间格式化 
					  %>	
	 	   	  		</table>
	 	   	  	</td>			 	   	  	
	 	   	  </tr>
	 	   	  <tr height="1"><td align="right" colspan="2"><hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;" /></td></tr>      
	 	   	  <tr>
		 	   	  <td>
		 	   	  	<table  style="font-size:12px;">
		 	   	  		  <tr><td align="right">PC网址<font color="red">(*)</font>:</td> <td><input type="text" name="url" id="url" value="<%=customerKeyword.getUrl()%>" style="width:200px;" ></td></tr>
		 	   	  		  <tr><td align="right">PC原始网址:</td> <td><input type="text" name="originalUrl" id="originalUrl" value="<%=Utils.isNullOrEmpty(customerKeyword.getOriginalUrl()) ? "" : customerKeyword.getOriginalUrl().trim()%>" style="width:200px;" ></td></tr>
				 	   	  <!-- 
				 	   	  <tr height="30"><td align="right">网站快照:</td> <td><input type="text" name="snapshotDateTime" id="snapshotDateTime" value="<%=customerKeyword.getSnapshotDateTime() == null ? "" : customerKeyword.getSnapshotDateTime()%>" style="width:300px;" ></td></tr>			 	   	  
				 	   	   -->
				 	   	  <tr height="30"><td align="right">网站标题:</td> <td><input type="text" name="title" id="title" value="<%=customerKeyword.getTitle() == null ? "" : customerKeyword.getTitle()%>" style="width:300px;" ></td></tr>
					      <tr><td align="right">PC要刷数量:</td> <td><input type="text" name="optimizePlanCount" id="optimizePlanCount" value="<%=customerKeyword.getOptimizePlanCount()%>" style="width:100px;"></td></tr>
				 	   	  <tr height="1"><td align="right" colspan="2"><hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;" /></td></tr>
				 	   	  <tr><td align="right">排名第一报价:</td> <td><input name="positionFirstFee" id="positionFirstFee" value="<%=customerKeyword.getPositionFirstFeeString()%>" style="width:100px;" type="text">元</td></tr>
				 	   	  <tr><td align="right">排名第二报价:</td> <td><input name="positionSecondFee" id="positionSecondFee" value="<%=customerKeyword.getPositionSecondFeeString()%>" style="width:100px;" type="text">元</td></tr>
				 	   	  <tr><td align="right">排名第三报价:</td> <td><input name="positionThirdFee" id="positionThirdFee" value="<%=customerKeyword.getPositionThirdFeeString()%>" style="width:100px;" type="text">元</td></tr>
				 	   	  <tr><td align="right">排名第四报价:</td> <td><input name="positionForthFee" id="positionForthFee" value="<%=customerKeyword.getPositionForthFeeString()%>" style="width:100px;" type="text">元</td></tr>
				 	   	  <tr><td align="right">排名第五报价:</td> <td><input name="positionFifthFee" id="positionFifthFee" value="<%=customerKeyword.getPositionFifthFeeString()%>" style="width:100px;" type="text">元</td></tr>
				 	   	  <tr><td align="right">排名首页报价:</td> <td><input name="positionFirstPageFee" id="positionFirstPageFee" value="<%=customerKeyword.getPositionFirstPageFeeString()%>" style="width:100px;" type="text">元</td></tr>
				 	   	  				      
		 	   	  	</table>
		 	   	  </td>
		 	   	  <td valign="top">
		 	   	  	<table  style="font-size:12px;">
		 	   	  	  <tr><td align="right">PC当前指数:</td> <td><input type="text" name="currentIndexCount" id="currentIndexCount" value="<%=customerKeyword.getCurrentIndexCount()%>" style="width:100px;"></td></tr>
			 	   	  <tr><td align="right">PC当前排名:</td> <td><input type="text" name="currentPosition" id="currentPosition" value="<%=customerKeyword.getCurrentPosition()%>" style="width:100px;"></td></tr>
			 	   	  <input type="hidden" id="serviceProvider" name="serviceProvider" value="<%=customerKeyword.getServiceProvider()%>">
				      
			 	   	  <tr height="1"><td align="right" colspan="2"><hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;" /></td></tr>
			 	   	  <tr><td align="right">排名第一成本:</td> <td><input name="positionFirstCost" id="positionFirstCost" value="<%=customerKeyword.getPositionFirstCostString()%>" style="width:100px;" type="text">元</td></tr>
			 	   	  <tr><td align="right">排名第二成本:</td> <td><input name="positionSecondCost" id="positionSecondCost" value="<%=customerKeyword.getPositionSecondCostString()%>" style="width:100px;" type="text">元</td></tr>
			 	   	  <tr><td align="right">排名第三成本:</td> <td><input name="positionThirdCost" id="positionThirdCost" value="<%=customerKeyword.getPositionThirdCostString()%>" style="width:100px;" type="text">元</td></tr>
			 	   	  <tr><td align="right">排名第四成本:</td> <td><input name="positionForthCost" id="positionForthCost" value="<%=customerKeyword.getPositionForthCostString()%>" style="width:100px;" type="text">元</td></tr>
			 	   	  <tr><td align="right">排名第五成本:</td> <td><input name="positionFifthCost" id="positionFifthCost" value="<%=customerKeyword.getPositionFifthCostString()%>" style="width:100px;" type="text">元</td></tr>
			 	   	  
				      <tr height="1"><td align="right" colspan="2"><hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;" /></td></tr>
				      <input type="hidden" name="startOptimizedTime" id="startOptimizedTime" value="<%=Utils.formatDatetime(customerKeyword.getStartOptimizedTime(), "yyyy-MM-dd")%>">
				      <tr><td align="right">有效期开始日期:</td> <td><input class="Wdate" onClick="WdatePicker()" name="effectiveFromTime" id="effectiveFromTime" value="<%=Utils.formatDatetime(customerKeyword.getEffectiveFromTime(), "yyyy-MM-dd")%>" style="width:100px;" type="text">格式：2014-10-23</td></tr>
				      <tr><td align="right">有效期结束日期:</td> <td><input class="Wdate" onClick="WdatePicker()" name="effectiveToTime" id="effectiveToTime" value="<%=Utils.formatDatetime(customerKeyword.getEffectiveToTime(), "yyyy-MM-dd")%>" style="width:100px;" type="text">格式：2014-10-23</td></tr>
						<tr><td align="right">排序:</td> <td><input type="text" name="sequence" id="sequence" value="<%=customerKeyword.getSequence()%>"
																  style="width:100px;" ></td></tr>
		 	   	  	</table>
		 	   	  </td>
	 	   	  </tr>
	 	   	  <tr height="1"><td align="right" colspan="2"><hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;" /></td></tr>
	 	   	  <tr>
	 	   	  	<td colspan="2">
	 	   	  		<table style="font-size:12px;">
			 	   	  <tr><td align="right">收费方式:</td> 
			          	  <td>
			          	  	 <select name="collectMethod" id="collectMethod">
			          	  	 	<%
				          	  	     String []collectMethodNames = {"按月","十天","七天","按天"};	
			          	  	 		 String []collectMethodValues = {"PerMonth","PerTenDay","PerSevenDay","PerDay"};
				          	  	     for (int i = 0; i < collectMethodValues.length; i ++)
				          	  	     {
				          	  	         if (collectMethodValues[i].equals(customerKeyword.getCollectMethod()))
				          	  	         {
				          	  	              out.println("<option selected value='" + collectMethodValues[i] + "'>" + collectMethodNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + collectMethodValues[i] + "'>" + collectMethodNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>		          	  	 	  
			          	  	 </select>   
			              </td>
			 	   	      <td align="right">状态:</td> 
			          	  <td>
			          	  	 <select name="isDelete" id="isDelete">
			          	  	 	<%
				          	  	     String []statusNames = {"新增","激活","过期"};	
			          	  	 		 int []statusValues = {2,1,0};
				          	  	     for (int i = 0; i < statusNames.length; i ++)
				          	  	     {
				          	  	         if (statusValues[i] == customerKeyword.getStatus())
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
				          <td align="right">关联关键字:</td> <td><input name="relatedKeywords" id="relatedKeywords" type="text" style="width:200px;" value="<%=customerKeyword.getRelatedKeywords() == null ? "" : customerKeyword.getRelatedKeywords()%>">多个逗号分隔</td>
				        </tr>
				        <tr height="30"><td align="right">备注:</td> <td colspan="5"><textarea name="remarks" id="remarks" style="width:800px;height:80px"><%=customerKeyword.getRemarks()%></textarea></td></tr>
	 	   	  		</table>
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




