<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

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
		List serviceProviders = spm.getAllActiveServiceProvider(datasourceName);
		
		UserVO user = um.login(datasourceName,username,password);
		
		String customerUuid = request.getParameter("customerUuid");	
		CustomerVO customer = cm.getCustomerByUuid(datasourceName, customerUuid);
		
		String condition = "";
		
		String curPage = request.getParameter("pg");
		
		if (curPage == null || curPage.equals(""))
		{
		    curPage = "1";
		}
		
		int iCurPage = Integer.parseInt(curPage);		
	    
	  	String keyword = request.getParameter("keyword");
	  	String url = request.getParameter("url");
	  	String creationFromTime = request.getParameter("creationFromTime");
	  	String creationToTime = request.getParameter("creationToTime");
	  	String searchEngine = request.getParameter("searchEngine");
	  	String orderElement = request.getParameter("orderElement");
	  	String isDelete = request.getParameter("isDelete");
	  	String serviceProvider  = request.getParameter("serviceProvider");
	  	String optimizeGroupName  = request.getParameter("optimizeGroupName");
	  	
	  	String position = request.getParameter("position");
	  	String[] unPaidAll = request.getParameterValues("unPaidAll");
	  	String[] pushPay = request.getParameterValues("pushPay");
	  	
	  	String pageUrl = "customerUuid=" + customerUuid;
	  	condition = condition + " and fCustomerUuid=" + customerUuid;
	  	
	  	if (!Utils.isNullOrEmpty(keyword)){
	  		condition = condition + " and fKeyword like '%" + keyword.trim() + "%' ";
	  		pageUrl = pageUrl + "&keyword=" + keyword;
	  	}else{
	  		keyword = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(url)){
	  		condition = condition + " and fUrl = '" + url.trim() + "' ";
	  		pageUrl = pageUrl + "&url=" + url;
	  	}else{
	  		url = "";	
	  	}
			
	  	if (!Utils.isNullOrEmpty(position)){
	  		condition = condition + " and ((fCurrentPosition > 0 and fCurrentPosition <= " + position.trim() + ") ";
	  		condition = condition + " or (fJisuCurrentPosition > 0 and fJisuCurrentPosition <= " + position.trim() + ") ";
	  		condition = condition + " or (fChupingCurrentPosition > 0 and fChupingCurrentPosition <= " + position.trim() + ")) ";
	  		pageUrl = pageUrl + "&position=" + position;
	  	}else{
	  		position = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(optimizeGroupName)){
	  		condition = condition + " and fOptimizeGroupName= '" + optimizeGroupName.trim() + "' ";
	  		pageUrl = pageUrl + "&optimizeGroupName=" + optimizeGroupName;
	  	}else{
	  		optimizeGroupName = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(serviceProvider)){
	  		condition = condition + " and fServiceProvider = '" + serviceProvider.trim() + "' ";
	  		pageUrl = pageUrl + "&serviceProvider=" + serviceProvider;
	  	}else{
	  		serviceProvider = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(searchEngine)){
	  		condition = condition + " and fSearchEngine= '" + searchEngine.trim() + "' ";
	  		pageUrl = pageUrl + "&searchEngine=" + searchEngine;
	  	}else{
	  		searchEngine = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(creationFromTime)){
	  		condition = condition + " and ck.fCreateTime >= STR_TO_DATE('" + creationFromTime.trim() + "', '%Y-%m-%d') ";
	  		pageUrl = pageUrl + "&creationFromTime=" + creationFromTime;
	  	}else{
	  		creationFromTime = "";	
	  	}
	  	if (!Utils.isNullOrEmpty(creationToTime)){
	  		condition = condition + " and ck.fCreateTime <= STR_TO_DATE('" + creationToTime.trim() + "', '%Y-%m-%d') ";
	  		pageUrl = pageUrl + "&creationToTime=" + creationToTime;
	  	}else{
	  		creationToTime = "";	
	  	}	
	  	
	  	if (!Utils.isNullOrEmpty(isDelete)){
	  		condition = condition + " and ck.fStatus = " + isDelete.trim() + " ";
	  		pageUrl = pageUrl + "&isDelete=" + isDelete;
	  	}else{
	  		isDelete = "";
	  	}
	  	
	  	if (unPaidAll != null){
	  		condition = condition + " AND EXISTS (SELECT 1 FROM t_ck_account_log l WHERE l.fCustomerKeywordUuid = ck.fUuid AND l.fStatus IN ('UnPaid', 'PaidPartially')) ";
	  		pageUrl = pageUrl + "&unPaidAll=unPaidAll";
	  	}
	  	
	  	
	  	if (pushPay != null){
	  		condition = condition + " AND ((fCollectMethod = 'PerDay' and ck.fEffectiveToTime is not null) or ck.fEffectiveToTime <= STR_TO_DATE('" + Utils.formatDatetime(Utils.addDay(Utils.getCurrentTimestamp(), 3), "yyyy-MM-dd") + "', '%Y-%m-%d')) ";
	  		pageUrl = pageUrl + "&pushPay=pushPay";
	  	}
	      
	      String order = " order by ck.fCreateTime desc ";
	      if ("账单日期".equals(orderElement)){    
	      	order = " order by ck.fEffectiveFromTime desc ";
	      } else if("当前排名".equals(orderElement)){
	      	order = " order by ck.fCurrentPosition ";
	      }
	      pageUrl = pageUrl + "&orderElement=" + orderElement;
	      
	      //itemList = ckm.searchCustomerKeywordAssociations(datasourceName, 30 , iCurPage , condition , order,1);
	    	
	     /* int recordCount = ckm.getRecordCount();
	      int pageCount = ckm.getPageCount();
	      
	      String fileName = "/customerkeyword/list.jsp?" + pageUrl;
	      String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);
	      */
	      
		  List itemList = ckm.searchCustomerKeywords(datasourceName, 30 , iCurPage , condition , order,1);
		  	
		  int recordCount = ckm.getRecordCount();
		  int pageCount = ckm.getPageCount();
		    
		  String fileName = "/customerkeyword/list.jsp?" + pageUrl;
		  String pageInfo = Utils.getPageInfo(iCurPage, 30, recordCount, pageCount, "", fileName);		    	      
%>

<html>
	<head>
	  <title>关键字列表</title>
	  <style>
		.wrap {word-break: break-all; word-wrap:break-word;}   
	  </style>
	  <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	  
	  <style>
		<!--
		#div1{ 
			display:none; 
			background-color :#f6f7f7; 
			color:#333333; 
			font-size:12px; 
			line-height:18px; 
			border:1px solid #e1e3e2; 
			width:250; 
			height:50;
		}
		-->
</style>
  	</head>	
<body>
      <table width=100% style="font-size:12px;" cellpadding=3>
      	  <tr>
      	  	<td colspan=13>
      	  		<%@include file="/customer/customerinfo.jsp" %>	
      	  	</td>
      	  </tr>
      	  <tr>
      	  	 <td colspan=13 align="right"><a href="keywordfinder.jsp?isDelete=1">关键字统计</a>
      	  	 	| <a href="customerReceive.jsp?customerUuid=<%=customerUuid.trim()%>">收款</a>       	  	 	
      	  	 	| <a href="add.jsp?customerUuid=<%=customerUuid.trim()%>">增加新关键字</a>   
      	  	 	| <a target="_blank" href="/customerkeyword/uploadsimple.jsp?customerUuid=<%=customerUuid.trim()%>">关键字Excel上传(简化版)</a>
      	  	 	| <a target="_blank" href="/SuperUserSimpleKeywordList.xls">简化版模板下载</a>
      	  	 	| <a href="/customerkeyword/customercollectfeelist.jsp?customerUuid=<%=customerUuid.trim()%>">客户收费清单</a>  
      	  	 	| <a href="/customer/customerlist.jsp">客户清单</a> | <a href="/user/updatepassword.jsp">修改密码</a> | <a href="/user/logout.jsp">退出</a>
      	  	 	</td>
      	  </tr>
      	  <tr> 
      	  	 <td colspan=13>
      	  	 	<form method="post" action="list.jsp">
	      	  	 	<table style="font-size:12px;">
	      	  	 		<tr>
	      	  	 			
	      	  	 			<td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="<%=keyword%>" style="width:90px;"></td>
	      	  	 			<td align="right">URL:</td> <td><input type="text" name="url" id="url" value="<%=url%>" style="width:120px;"></td>
	      	  	 			<td align="right">搜索引擎:</td>
	      	  	 			<td>
	      	  	 			  <input id="customerUuid" name="customerUuid" type="hidden" value="<%=customerUuid%>">
	      	  	 			  <select name="searchEngine" id="searchEngine">		
			          	  	 	   <%
				          	  	     String []searchEngineNames = {"","百度","搜狗","360","谷歌"};						          	  	     
				          	  	     for (int i = 0; i < searchEngineNames.length; i ++)
				          	  	     {
				          	  	         if (searchEngineNames[i].equals(searchEngine))
				          	  	         {
				          	  	              out.println("<option selected value='" + searchEngineNames[i] + "'>" + searchEngineNames[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + searchEngineNames[i] + "'>" + searchEngineNames[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>
			          	  	 </select>
			          	  </td>	      	  	 		  
				 	   	  <td align="right">添加时间:<input name="creationFromTime" id="creationFromTime" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=creationFromTime %>">到<input name="creationToTime" id="creationToTime" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=creationToTime %>"></td>
				 	   	  <td align="right">关键字状态:</td>
	      	  	 			<td>
	      	  	 			  <select name="isDelete" id="isDelete">
			          	  	 	   <%
			          	  	 	  	 String []statusNames = {"", "新增","激活","过期"};	
			          	  	 		 String []statusValues = {"","2","1","0"};
			          	  	 		 for (int i = 0; i < statusNames.length; i ++)
				          	  	     {
				          	  	         if (statusValues[i].equals(isDelete))
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
			          	  <td align="right">显示前:</td> <td><input type="text" name="position" id="position" value="<%=position%>" style="width:20px;" ></td>
				 	   	  <td align="right">排序:</td>
	      	  	 			<td>
	      	  	 			  <select name="orderElement" id="orderElement">		
			          	  	 	   <%
				          	  	     String []orderElements = {"创建日期","当前排名","账单日期"};						          	  	     
				          	  	     for (int i = 0; i < orderElements.length; i ++)
				          	  	     {
				          	  	         if (orderElements[i].equals(orderElement))
				          	  	         {
				          	  	              out.println("<option selected value='" + orderElements[i] + "'>" + orderElements[i] + "</option>");
				          	  	         }
				          	  	         else
				          	  	       	 {
				          	  	       	      out.println("<option value='" + orderElements[i] + "'>" + orderElements[i] + "</option>");
				          	  	       	 }						          	
				          	  	     }
						          %>
			          	  	 </select>
			          	  </td>
			          	  <%if (!user.isVipType()){ %>
				 	   	  <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
				 	   	  <%} %> 
	      	  	 		</tr>
	      	  	 		</table>
	      	  	 		<%if (user.isVipType()){%>
	      	  	 		<table style="font-size:12px;">
	      	  	 		<tr>
	      	  	 		  <td align="right">优化组名:</td><td><input type="text" name="optimizeGroupName" id="optimizeGroupName" value="<%=optimizeGroupName%>" style="width:120px;"></td>
	      	  	 		  
	      	  	 		  <td align="right">服务提供商:</td>
	      	  	 			<td>
	      	  	 			  <select name="serviceProvider" id="serviceProvider">
			          	  	 		<option value=""></option>
			          	  	 		<%			          	  	     						          	  	     
					          	  	     for (int i = 0; i < serviceProviders.size(); i++)				          	  	    
					          	  	     {
					          	  	    	 ServiceProviderVO serviceProviderVO = (ServiceProviderVO)serviceProviders.get(i);
					          	  	         if (serviceProviderVO.getServiceProviderName().equals(serviceProvider))
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
	      	  	 		  <td align="right"><input id="unPaidAll" name="unPaidAll" type="checkbox" value="unPaidAll" <%if (unPaidAll != null){ out.print(" checked=true");}%>>欠费</input></td>
				 	   	  <td align="right"><input id="pushPay" name="pushPay" type="checkbox" value="pushPay" <%if (pushPay != null){ out.print(" checked=true");}%>>催缴</input></td>
			          	  <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
	      	  	 		</tr>
		      	  	 	</table>
	      	  	 		<%} %>
      	  	 	</form>
      	  	 </td>
      	  </tr>    
          <tr bgcolor="#eeeeee" height=30>
              <td align="center" width=130>关键字</td>
              <td align="center" width=150>URL</td>
              <td align="center" width=40>引擎</td>
              <td align="center" width=90>指数/初始/当前</td>
              <td align="center" width=30>计价方式</td>
              <%if(user.isVipType()){ %>
              <td align="center" width=100>成本</td>
              <%} %>
              <td align="center" width=100>报价</td>
              <td align="center" width=70>优化日期</td>
              <td align="center" width=100><div style="height:16;">开始日期</div><div style="height:16;">结束日期</div></td>
              <%if(user.isVipType()){ %>
              <td align="center" width=60>优化组名</td>
              <td align="center" width=156>操作</td>
              <%}else if(user.getUserLevel() > 1){ %>
			  <td align="center" width=156>操作</td>			  
			  <%} %>                            
			  <div id="div1"></div> 
          </tr>
          <%
              String trClass = "";
              String webUrl = "";
              String keywordColor = "";
              for (int i = 0; i < itemList.size(); i ++)
              {
                  CustomerKeywordVO value = (CustomerKeywordVO) itemList.get(i);                  
                  trClass= "";
                  keywordColor = "";
                  if (value.isUnpaid()){
                  	keywordColor = "#FF0000";
                  }else if(value.isPushPay()){
                  	keywordColor = "#FF9966";
                  }else{
                  	keywordColor = "#669900";
                  }
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">                                            
                      <td><font color="<%=keywordColor%>"><%=value.getKeyword()%></font></td>
                      <td class="wrap"  onMouseMove="showTip('PC原始URL:<%=value.getOriginalUrl() != null ? value.getOriginalUrl() : value.getUrl()%>" onMouseOut="closeTip()">
			         	<div style="height:16;"><%=Utils.isNullOrEmpty(value.getUrl())? "" : "PC:" + value.getUrl()%></div>
			         </td>
			         <td onMouseMove="showTip('PC:  优化日期：<%=Utils.formatDatetime(value.getOptimizeDate(), "yyyy-MM-dd HH:mm")%>，要刷：<%=value.getOptimizePlanCount()%>，已刷：<%=value.getOptimizedCount()%>')" onMouseOut="closeTip()"><%=value.getSearchEngine()%></td>
			         <td>
			         	<div style="height:16;"><a href="/customerkeyword/historyPositionAndIndex.jsp?type=PC&uuid=<%=value.getUuid()%>" target="_blank"><%=value.getCurrentIndexCount()%></a>/<%=value.getInitialPosition()%>/<a href="<%=value.searchEngineUrl()%><%=value.getKeyword()%>&pn=<%=Utils.prepareBaiduPageNumber(value.getCurrentPosition())%>" target="_blank"><%=value.getCurrentPosition()%></a></div>
			         </td>
			            
                      <td><%=value.getCollectMethodName()%></td>
                      <%if(user.isVipType()){ %>                 
                      <td nowrap=true><%=value.costString()%>
                      </td nowrap=true>
                      <%} %>
                      <td><%=value.feeString()%>
                      </td>
                      <td><%=Utils.formatDatetime(value.getStartOptimizedTime(), "yyyy-MM-dd")%></td>
                      <td><%=value.effectiveString() %>
                      </td>
                      <%if(user.isVipType()){ %>                                          
                      <td><%=value.getOptimizeGroupName() == null ? "" : value.getOptimizeGroupName()%></td>
                      <td>
                      	  <a href="modify.jsp?uuid=<%=value.getUuid()%>">修改</a> | 
                      	  <a href="receive.jsp?customerKeywordUuid=<%=value.getUuid()%>">收款</a> |
                      	  <a href="pay.jsp?customerKeywordUuid=<%=value.getUuid()%>">付款</a> |
                      	  <a href="javascript:delItem('<%=value.getUuid()%>')">删除</a>
                      </td>
                      <%}else if(user.getUserLevel() > 1){ %>
					  <td>                      	  
                      	  <a href="receive.jsp?customerKeywordUuid=<%=value.getUuid()%>">收款</a>
                      </td>
					  <%} %>                            
                  </tr>
          <%
              }
          
          %>
          
          <tr>
          	<td colspan=9>
          		  <br>
          		  <%=pageInfo%>
          	</td>
          </tr>          
      </table>
   <br><br><br>
  <br>
  
  <br><br><br><br><br><br><br><br>
<script language="javascript">
function doOver(obj) {
	obj.style.backgroundColor = "green";
}

function doOut(obj) {
	var rowIndex = obj.rowIndex;
	if ((rowIndex % 2) == 0) {
		obj.style.backgroundColor = "#eeeeee";
	} else {
		obj.style.backgroundColor = "#ffffff";
	}
}
function delItem(uuid)
{
   if (confirm("确实要删除这个关键字吗?") == false) return;
   document.location = "delete.jsp?uuid=" + uuid + "&customerUuid=<%=customerUuid%>";
}
function showTip(content,e) 
{
	e = e||window.event;
	var div1 = document.getElementById('div1'); //将要弹出的层 
	div1.innerText = content;
	div1.style.display="block"; //div1初始状态是不可见的，设置可为可见  
	div1.style.left=e.clientX+10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容 
	div1.style.top=e.clientY+5; 
	div1.style.position="absolute"; 
 }

//关闭层div1的显示 
function closeTip() 
{ 
	 var div1 = document.getElementById('div1'); 
	 div1.style.display="none"; 
} 
</script>


<div style="display:none;">
<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>

</div>
</body>
</html>

