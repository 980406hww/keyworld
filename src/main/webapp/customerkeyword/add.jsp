<%@page contentType="text/html;charset=utf-8"   errorPage="/error.jsp"%>
<%@page import="com.keymanager.manager.*,com.keymanager.value.*,com.keymanager.util.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="spm" scope="page" class="com.keymanager.manager.ServiceProviderManager" />
<jsp:useBean id="ckm" scope="page" class="com.keymanager.manager.CustomerKeywordManager" />

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
		
		String customerUuid = request.getParameter("customerUuid");
		CustomerVO customer = cm.getCustomerByUuid(datasourceName, customerUuid);
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
		List serviceProviders = spm.getAllActiveServiceProvider(datasourceName);
%>

<html>
	<head>
		  <title>增加关键字</title>
		  <script type="text/javascript" src="/common.js">
		  </script>
		  <script type="text/javascript" src="add.js">
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
  	<td>
  		<%@include file="/customer/customerinfo.jsp" %>	
  	</td>
  </tr>
	<tr>
		<td align="right">
	     
	 	   <div style="text-align:right;width:100%;font-size:12px;"><a href="list.jsp?isDelete=1&customerUuid=<%=customerUuid%>">返回关键字列表</a></div>
	 	   
	 	   <table width=80% style="font-size:12px;">
	 	   	  <input type="hidden" name="customerUuid" value="<%=customerUuid%>">
	 	   	  <tr height="30"><td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="" style="width:300px;"> 输入您要刷的关键字</td></tr>
	 	   	  <tr height="30"><td align="right">搜索引擎:</td> 
	          	  <td>
	          	  	 <select name="searchEngine" id="searchEngine" onChange="searchEngineChanged();">			          	  	 	  			          	  	 	 
	          	  	 	  <option value="百度" selected>百度</option>
	          	  	 	  <option value="搜狗">搜狗</option>
	          	  	 	  <option value="360">360</option>
	          	  	 	  <option value="谷歌">谷歌</option>
	          	  	 </select>
	          	  	 
	          	  	 <input type="hidden" id="initialPosition" name="initialPosition" value="">
	          	  	 当前指数:<input type="text" id="initialIndexCount" size="6" name="initialIndexCount" value="100"><font color="red"><span id="initialIndexCountSpan"></span></font>， 当前排名:<font color="red"><span id="initialPositionSpan"></span></font>
	              </td>			              
		      </tr>
		      <tr height="1"><td align="right" colspan="2"><hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;" /></td></tr>
	 	   	  <tr height="30"><td align="right">PC域名:</td> <td><input type="text" name="url" id="url" value="" style="width:300px;" ><font color="red"> 格式(www.baidu.com)即可</font></td></tr>
	 	   	  <tr height="30"><td align="right">PC原始域名:</td> <td><input type="text" name="originalUrl" id="originalUrl" value="" style="width:300px;" ><font color="red"> 格式(www.baidu.com)即可</font></td></tr>
	 	   	  
	 	   	  <!-- <tr height="30"><td align="right">网站快照:</td> <td><input type="text" name="snapshotDateTime" id="snapshotDateTime" value="" style="width:300px;" ><font color="red"> 直接从百度结果中拷贝</font></td></tr>	 -->		 	   	  
	 	   	 <!--  <tr height="30"><td align="right">网站标题:</td> <td><input type="text" name="title" id="title" value="" style="width:300px;" ></td></tr>  -->
	 	   	  
	 	   	  
	 	   	  <tr height="30"><td align="right">PC第一报价:</td> <td><input name="positionFirstFee" id="positionFirstFee" value="" onBlur="setSecondThirdDefaultFee();" style="width:100px;" type="text">元 <a href="javascript:void(0);" onclick="showMoreFee();"><span id="feeShowHideLabel">更多</span></a></td></tr>
	 	   	  <tr height="30" class="hiddentr" id="secondFee"><td align="right">PC第二报价:</td> <td><input name="positionSecondFee" onBlur="setThirdDefaultFee();" id="positionSecondFee" value="" style="width:100px;" type="text">元</tr>
	 	   	  <tr height="30" class="hiddentr" id="thirdFee"><td align="right">PC第三报价:</td> <td><input name="positionThirdFee" id="positionThirdFee" value="" style="width:100px;" type="text">元</td></tr>
	 	   	  <tr height="30" class="hiddentr" id="forthFee"><td align="right">PC第四报价:</td> <td><input name="positionForthFee" id="positionForthFee" value="0" style="width:100px;" type="text">元</td></tr>
	 	   	  <tr height="30" class="hiddentr" id="fifthFee"><td align="right">PC第五报价:</td> <td><input name="positionFifthFee" id="positionFifthFee" value="0" style="width:100px;" type="text">元</td></tr>
	 	   	  <tr height="30" class="hiddentr" id="firstPageFee"><td align="right">PC首页报价:</td> <td><input name="positionFirstPageFee" id="positionFirstPageFee" value="0" style="width:100px;" type="text">元</td></tr>
			  <% if (user.isVipType()){ %>			 	   	 
	 	   	  <tr height="30"><td align="right">PC第一成本:</td> <td><input name="positionFirstCost" id="positionFirstCost" onBlur="setSecondThirdDefaultCost();" value="0" style="width:100px;" type="text">元 <a href="javascript:void(0);" onclick="showMoreCost();"><span id="costShowHideLabel">更多</span></a></td></tr>
	 	   	  <tr  height="30" class="hiddentr" id="secondCost"><td align="right">PC第二成本:</td> <td><input name="positionSecondCost" id="positionSecondCost" onBlur="setThirdDefaultCost();" value="0" style="width:100px;" type="text">元</tr>
	 	   	  <tr  height="30" class="hiddentr" id="thirdCost"><td align="right">PC第三成本:</td> <td><input name="positionThirdCost" id="positionThirdCost" value="0" style="width:100px;" type="text">元</td></tr>
	 	   	  <tr height="30" class="hiddentr" id="forthCost"><td align="right">PC第四成本:</td> <td><input name="positionForthCost" id="positionForthCost" value="0" style="width:100px;" type="text">元</td></tr>
	 	   	  <tr height="30" class="hiddentr" id="fifthCost"><td align="right">PC第五成本:</td> <td><input name="positionFifthCost" id="positionFifthCost" value="0" style="width:100px;" type="text">元</td></tr>
	 	   	  <%} %>
	 	   	  
   	  			  <% if (user.isVipType()){ %>	
   	  			   <tr height="30">
     				  <td align="right" width="120">PC服务提供商:</td>			      					 
	          	  	<td align="left">
		          	  	 <select name="serviceProvider" id="serviceProvider">
		          	  	 	<option value=""></option>
	          	  	 		<%			          	  	     						          	  	     
	          	  	 		for (int i = 0; i < serviceProviders.size(); i++)				          	  	    
		          	  	    {
		          	  	    	 ServiceProviderVO serviceProviderVO = (ServiceProviderVO)serviceProviders.get(i);
		          	  	         if (serviceProviderVO.getServiceProviderName().equals(ServiceProviderVO.DEFAULT_SERVICE_PROVIDER))
		          	  	         {
		          	  	              out.println("<option selected value='" + serviceProviderVO.getServiceProviderName() + "'>" + serviceProviderVO.getServiceProviderName() + "</option>");
		          	  	         }
		          	  	         else
		          	  	       	 {
		          	  	       	      out.println("<option value='" + serviceProviderVO.getServiceProviderName() + "'>" + serviceProviderVO.getServiceProviderName() + "</option>");
		          	  	       	 }						          	
			          	  	}
					        %>     	  	 	  
			          	 </select>   
		              </td>
		          </tr>
			  <%} %>
			   <tr height="30"><td align="right">排序:</td> <td><input type="text" name="sequence" id="sequence" value="0" style="width:300px;" ></td></tr>
			  <tr height="30"><td align="right">标题:</td> <td><input type="text" name="title" id="title" value="" style="width:300px;" ></td></tr>

		      <%
			      java.text.SimpleDateFormat formatter = new java.text.SimpleDateFormat("yyyy-MM-dd"); 
				  java.util.Date currentTime = new java.util.Date();//得到当前系统时间 
				  String today = formatter.format(currentTime); //将日期时间格式化 
			  %>
		      <tr height="1"><td align="right" colspan="2"><hr style="height: 1px; border:none; border-top:1px dashed #CCCCCC;" /></td></tr>
		      <input type="hidden" name="startOptimizedTime" id="startOptimizedTime" value="<%=today%>">
		      <!-- <tr height="30"><td align="right">开始优化日期:</td> <td><input name="startOptimizedTime" id="startOptimizedTime" class="Wdate" type="text" style="width:100px;"  onClick="WdatePicker()" value="<%=today %>">格式：2014-10-23</td></tr> -->				      
		      <tr height="30"><td align="right">优化组名:</td> <td><input name="optimizeGroupName" id="optimizeGroupName" type="text" style="width:200px;" value="">比如：shouji</td></tr>
		      <input type="hidden" name="relatedKeywords" id="relatedKeywords" value="">
		      <!-- <tr height="30"><td align="right">关联关键字:</td> <td><input name="relatedKeywords" id="relatedKeywords" type="text" style="width:200px;" value="">多个逗号分隔</td></tr> -->
		      <tr height="30"><td align="right">收费方式:</td>
		      <td>
	      			<table width=50%>
	      				<tr>
	      					<td>
				          	  	 <select name="collectMethod" id="collectMethod" onChange="setEffectiveToTime();">
				          	  	 	  <option value="PerMonth"  selected>按月</option>
				          	  	 	  <option value="PerTenDay">十天</option>			          	  	 	  			          	  	 	 
				          	  	 	  <option value="PerSevenDay">七天</option>
				          	  	 	  <option value="PerDay">按天</option>			          	  	 	  
				          	  	 </select> 
				          	  	 <input type="hidden" id="isDelete" name="isDelete" value="1">
				              </td>
				              <!-- 
						      <td align="left" width="50">状态:</td> 
				          	  <td align="left">
				          	  	 <select naisDeleteatus" isDeleteatus" value="2">
				          	  	 	  <option value="2">新增</option>
				          	  	 	  <%if(user.isVipType()){ %>		          	  	 	  			          	  	 	 			          	  	 	  
				          	  	 	  <option value="1" selected>激活</option>
				          	  	 	  <option value="0">过期</option>
				          	  	 	  <%} %>
				          	  	 </select>   
				              </td>
				              -->
		      				</tr>
	      			</table>	
		      	</td>
			 </tr>   
           <tr height="30"><td align="right"></td> 
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




