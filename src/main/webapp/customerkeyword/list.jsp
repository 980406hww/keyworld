<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*,java.net.URLEncoder"%>
<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
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
		String type = (String) session.getAttribute("entry");

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
		String customerUuid = request.getParameter("customerUuid");	
		CustomerVO customer = cm.getCustomerByUuid(datasourceName, customerUuid);
		
		UserVO user = um.login(datasourceName,username,password);
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
		
		String condition = " and fTerminalType = '" + terminalType + "' and fType = '" + type + "' ";
		
		String curPage = request.getParameter("pg");
		
		if (curPage == null || curPage.equals(""))
		{
		    curPage = "1";
		}
		
		int iCurPage = Integer.parseInt(curPage);		
		String invalidRefreshCount  = request.getParameter("invalidRefreshCount");
	  	String keyword = request.getParameter("keyword");
	  	String url = request.getParameter("url");
	  	String creationFromTime = request.getParameter("creationFromTime");
	  	String creationToTime = request.getParameter("creationToTime");
	  	String orderElement = request.getParameter("orderElement");
	  	String status = request.getParameter("status");
	  	String serviceProvider  = request.getParameter("serviceProvider");
	  	String optimizeGroupName  = request.getParameter("optimizeGroupName");
	  	String positionType  = "全部";
	  	
	  	String position = request.getParameter("position");
	  	String[] unPaidAll = request.getParameterValues("unPaidAll");
	  	String[] pushPay = request.getParameterValues("pushPay");
	  	
	  	String pageUrl = "customerUuid=" + customerUuid;
	  	condition = condition + " and fCustomerUuid=" + customerUuid;
	  	
	  	if (!Utils.isNullOrEmpty(keyword)){
	  		condition = condition + " and fKeyword like '%" + keyword.trim() + "%' ";
	  		pageUrl = pageUrl + "&keyword=" + java.net.URLEncoder.encode(keyword,"UTF-8");
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
  			condition = condition + " and (fCurrentPosition > 0 and fCurrentPosition <= " + position.trim() + ") ";
	  		pageUrl = pageUrl + "&position=" + position;
	  	}else{
	  		position = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(optimizeGroupName)){
	  		condition = condition + " and fOptimizeGroupName= '" + optimizeGroupName.trim() + "' ";
	  		pageUrl = pageUrl + "&optimizeGroupName=" + java.net.URLEncoder.encode(optimizeGroupName,"UTF-8");
	  	}else{
	  		optimizeGroupName = "";	
	  	}
	  	
	  	if (!Utils.isNullOrEmpty(serviceProvider)){
	  		condition = condition + " and fServiceProvider = '" + serviceProvider.trim() + "' ";
	  		pageUrl = pageUrl + "&serviceProvider=" + serviceProvider;
	  	}else{
	  		serviceProvider = "";	
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
	  	
	  	if (!Utils.isNullOrEmpty(status)){
	  		condition = condition + " and ck.fStatus = " + status.trim() + " ";
	  		pageUrl = pageUrl + "&status=" + status;
	  	}else{
	  		status = "";	
	  	}
	  	
		if (!Utils.isNullOrEmpty(invalidRefreshCount)){
			condition = condition + " and fInvalidRefreshCount >= " + invalidRefreshCount.trim() + " ";
			pageUrl = pageUrl + "&invalidRefreshCount=" + invalidRefreshCount;
		}else{
			invalidRefreshCount = "";	
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
	      
		  List itemList = ckm.searchCustomerKeywords(datasourceName, 100 , iCurPage , condition , order,1);
		  	
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
		<!--
		#div1{ 
			display:none; 
			background-color :#f6f7f7; 
			color:#333333; 
			font-size:12px; 
			line-height:18px; 
			border:1px solid #e1e3e2; 
			width:450px;
			height:50px;
		}
		#div2 {
			display: none;
			background-color: #ACF106;
			color: #E80404;
			font-size: 20px;
			line-height: 18px;
			border: 2px solid #104454;
			width: 100px;
			height: 22px;
		}
		#changeOptimizationGroupDialog {
			display: none;
			margin: -30px 0px 0px -130px;
			background-color: white;
			color: #2D2A2A;
			font-size: 12px;
			line-height: 12px;
			border: 2px solid #104454;
			width: 260px;
			height: 60px;
			left: 50%;
			top: 50%;
			z-index: 25;
			position: fixed;
		}
		-->
	  </style>
	  <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	  <script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	  <script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	  <link href="/css/menu.css" rel="stylesheet" type="text/css" />	
  	</head>	
<body>
      <table width=100% style="font-size:12px;" cellpadding=3>
      	  	<tr>
				<td colspan=18 align="left">
					<%@include file="/menu.jsp" %>	
				</td>
			</tr>
      	  <tr>
      	  	<td colspan=18>
      	  		<%@include file="/customer/customerinfo.jsp" %>	
      	  	</td>
      	  </tr>
      	  <tr>
      	  	 <td colspan=18 align="right"><a href="updateCustomerKeywordGroupName.jsp?customerUuid=<%=customerUuid.trim()%>">修改该用户关键字组名</a>
      	  	 	| <a href="stopCustomerKeyword.jsp?customerUuid=<%=customerUuid.trim()%>">下架该客户关键字</a>   
      	  	 	| <a href="add.jsp?customerUuid=<%=customerUuid.trim()%>">增加新关键字</a>   
      	  	 	| <a target="_blank" href="/customerkeyword/uploadsimple.jsp?customerUuid=<%=customerUuid.trim()%>">关键字Excel上传(简化版)</a>
      	  	 	| <a target="_blank" href="/SuperUserSimpleKeywordList.xls">简化版模板下载</a>
      	  	 	| <a target="_blank" href="/customerkeyword/uploadfull.jsp?customerUuid=<%=customerUuid.trim()%>">关键字Excel上传(完整版)</a>
      	  	 	| <a target="_blank" href="/SuperUserFullKeywordList.xls">完整版模板下载</a>
				 | <a target="_blank" href="/customer/uploaddailyreporttemplate.jsp?customerUuid=<%=customerUuid.trim()%>">上传日报表模板</a>
				 | <a target="_blank" href='/internal/dailyReport/downloadSingleCustomerReport/<%=customerUuid.trim()%>'>导出日报表</a>
      	  	 	| <a target="_blank" href='/customerkeyword/DownloadCustomerKeywordInfo.jsp?fileName=CustomerKeywordInfo<%="_" + Utils.getCurrentDate()%>.xls&<%=pageUrl%>'>导出结果</a>
      	  	 	</td>
      	  </tr>
      	  <tr> 
      	  	 <td colspan=18>
      	  	 	<form method="post" action="list.jsp">
	      	  	 	<table style="font-size:12px;">
	      	  	 		<tr>
	      	  	 			
	      	  	 			<td align="right">关键字:</td> <td><input type="text" name="keyword" id="keyword" value="<%=keyword%>" style="width:90px;"></td>
	      	  	 			<td align="right">URL:</td> <td><input type="text" name="url" id="url" value="<%=url%>" style="width:120px;"></td>
	      	  	 			<td>
	      	  	 			  <input id="customerUuid" name="customerUuid" type="hidden" value="<%=customerUuid%>">
			          	  </td>	      	  	 		  
				 	   	  <td align="right">添加时间:<input name="creationFromTime" id="creationFromTime" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=creationFromTime %>">到<input name="creationToTime" id="creationToTime" class="Wdate" type="text" style="width:90px;"  onClick="WdatePicker()" value="<%=creationToTime %>"></td>
				 	   	  <td align="right">关键字状态:</td>
	      	  	 			<td>
	      	  	 			  <select name="status" id="status">		
			          	  	 	   <%
			          	  	 	  	 String []statusNames = {"", "新增","激活","过期"};	
			          	  	 		 String []statusValues = {"","2","1","0"};
			          	  	 		 for (int i = 0; i < statusNames.length; i ++)
				          	  	     {
				          	  	         if (statusValues[i].equals(status))
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
			          	  <td align="right">优化组名:</td><td><input type="text" name="optimizeGroupName" id="optimizeGroupName" value="<%=optimizeGroupName%>" style="width:60px;"></td>
	      	  	 		  
	      	  	 		  
			          	  <%if (!user.isVipType()){ %>
				 	   	  <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
				 	   	  <%} %> 
	      	  	 		</tr>
	      	  	 		</table>
	      	  	 		<%if (user.isVipType()){%>
	      	  	 		<table style="font-size:12px;">
	      	  	 		<tr>
	      	  	 		  
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
				 	   	  <td align="right">无效点击数:</td> <td><input type="text" name="invalidRefreshCount" id="invalidRefreshCount" value="<%=invalidRefreshCount%>" style="width:160px;"></td>
			          	  <td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
			          	  <td align="right"><a href="javascript:delAllItems(null)">删除所选关键字</a> |
							  <a href="javascript:delAllItems('emptyTitleAndUrl')">删除标题和网址为空的关键字</a> |
							  <a href="javascript:delAllItems('emptyTitle')">删除标题为空的关键字</a> |
							  <a href="javascript:resetTitle()">清空结果采集标题标志</a> |
							  <a href="javascript:clearTitle('<%=customerUuid%>', null)">清空所选关键字标题</a> |
							  <a href="javascript:clearTitle('<%=customerUuid%>', 'all')">清空当前客户下关键字标题</a> |
							 <a target="_blank" href="javascript:showChangeOptimizationGroupDialog(this)">修改选中关键词分组</a>
						  </td>
	      	  	 		</tr>
		      	  	 	</table>
	      	  	 		<%} %>
      	  	 	</form>
      	  	 </td>
      	  </tr>    
          <tr bgcolor="#eeeeee" height=30>
              <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" /></td>
              <td align="center" width=100>关键字</td>
              <td align="center" width=200>URL</td>
              <td align="center" width=250>标题</td>
              <td align="center" width=30>指数</td>
		      <td align="center" width=50>原排名</td>
		      <td align="center" width=50>现排名</td>
              <td align="center" width=30>计价方式</td>
              <td align="center" width=30>要刷</td>
		      <td align="center" width=30>已刷</td>
		      <td align="center" width=30>无效</td>
              <td align="center" width=60>报价</td>
              <td align="center" width=80>开始优化日期</td>
			  <td align="center" width=80>最后优化时间</td>
              <td align="center" width=50>订单号</td>
		   	  <td align="center" width=100>备注</td>	
              <%if(user.isVipType()){ %>
              <td align="center" width=60>优化组名</td>
              <td align="center" width=80>操作</td>
              <%}else if(user.getUserLevel() == 1){ %>
			  <td align="center" width=80>操作</td>
			  <%} %>                            
			  <div id="div1"></div>
			  <div id="div2"></div>
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
				  /**                 
                  if (value.isUnpaid()){
                  	keywordColor = "#FF0000";
                  }else if(value.isPushPay()){
                  	keywordColor = "#FF9966";
                  }else{
                  	keywordColor = "#669900";
                  }
				  */
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">     
                      <td><input type="checkbox" name="uuid" value=<%=value.getUuid()%> /></td>                                       
                      <td>
                     	<font color="<%=keywordColor%>"><%=value.getKeyword()%></font>
                      </td>
                      <td class="wrap"  onMouseMove="showTip('原始URL:<%=value.getOriginalUrl() != null ? value.getOriginalUrl() : value.getUrl()%>')" onMouseOut="closeTip()">
			         	<div style="height:16;"><%=Utils.isNullOrEmpty(value.getUrl())? "" : "" + value.getUrl()%></div>
			         </td>
			         
			         <td>
			         	<%=(value.getTitle() == null) ? "" : value.getTitle().trim()%>
			         </td>
			         
			         <td>
			         	<div style="height:16;"><a href="/customerkeyword/historyPositionAndIndex.jsp?type=PC&uuid=<%=value.getUuid()%>" target="_blank"><%=value.getCurrentIndexCount()%></a></div>
			         </td>
			         
			         <td>
			         	<div style="height:16;"><%=value.getInitialPosition()%></div>
			         </td>
			         
			         <td>
			         	<div style="height:16;"><a href="<%=value.searchEngineUrl()%><%=value.getKeyword()%>&pn=<%=Utils.prepareBaiduPageNumber(value.getCurrentPosition())%>" target="_blank"><%=value.getCurrentPosition()%></a></div>
			         </td>
			         
			         <td onMouseMove="showTip('优化日期：<%=Utils.formatDatetime(value.getOptimizeDate(), "yyyy-MM-dd")%>，要刷：<%=value.getOptimizePlanCount()%>，已刷：<%=value.getOptimizedCount()%>')" onMouseOut="closeTip()"><%=value.getCollectMethodName()%></td>
			            
			         <td><%=value.getOptimizePlanCount()%></td>
			         <td><%=value.getOptimizedCount()%></td>
			         <td><%=value.getInvalidRefreshCount()%></td>
			           
                      <td><%=value.feeString()%>
                      </td>
                      <td><%=Utils.formatDatetime(value.getStartOptimizedTime(), "yyyy-MM-dd")%></td>
					 <td><%=Utils.formatDatetime(value.getLastOptimizeDateTime(), "yyyy-MM-dd HH:mm")%></td>
                      <td><%=Utils.trimValue(value.getOrderNumber())%></td>
			         <td><%=Utils.trimValue(value.getRemarks())%></td>
                      <%if(user.isVipType()){ %>                                          
                      <td><%=value.getOptimizeGroupName() == null ? "" : value.getOptimizeGroupName()%></td>
                      <td>
                      	  <a href="modify.jsp?uuid=<%=value.getUuid()%>">修改</a> |
                      	  <a href="javascript:delItem('<%=value.getUuid()%>')">删除</a>
                      </td>
                      <%}else if(user.getUserLevel() == 1){ %>
					  <td>                      	  
                      	  <a href="modify.jsp?uuid=<%=value.getUuid()%>">修改</a> | 
                      	  <a href="javascript:delItem('<%=value.getUuid()%>')">删除</a>
                      </td>
					  <%} %>                            
                  </tr>
          <%
              }
          
          %>
           <tr>
          	<td colspan=18 align="right">
          		  <br>
          		  <a href="javascript:delAllItems()">删除所选关键字</a>
          	</td>
          </tr>           
          <tr>
          	<td colspan=18>
          		  <br>
          		  <%=pageInfo%>
          	</td>
          </tr>          
      </table>
   <br><br><br>
  <br>
  
  <br><br><br><br><br><br><br><br>
<script language="javascript">
function selectAll(self){
	var a = document.getElementsByName("uuid");
	if(self.checked){
		for(var i = 0;i<a.length;i++){
			if(a[i].type == "checkbox"){
				a[i].checked = true;
			}
		}
	}else{
		for(var i = 0;i<a.length;i++){
			if(a[i].type == "checkbox"){
				a[i].checked = false;
			}
		}
	}
}

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
   document.location = "delete.jsp?uuid=" + uuid + "&customerUuid=<%=customerUuid%>" + "&pageUrl=<%=pageUrl%>";
}
function delAllItems(deleteType)
{
	if (confirm("确实要删除这些关键字吗?") == false) return;
	var tmpUrl = '/customerkeyword/deleteAll.jsp?';
	if(deleteType == null){
		var uuids = getUuids();
		if(uuids.trim() === ''){
			alert("请选中要操作的关键词！");
			return;
		}
		tmpUrl = tmpUrl + "uuids=" + uuids + "&customerUuid=<%=customerUuid%>";
	}else{
		if (confirm("确实要删除标题和网址为空关键字吗?") == false) return;
		tmpUrl = tmpUrl + "deleteType=" + deleteType + "&customerUuid=<%=customerUuid%>";
	}

	$$$.ajax({
		url: tmpUrl,
		type: 'GET',
		success: function (data) {
			data = data.replace(/\r\n/gm,"");
			data = data.replace(/\n/gm,"");
			if(data === "1"){
				showInfo("删除成功！", self);
				window.location.reload();
			}else{
				showInfo("删除失败！", self);
			}
		},
		error: function () {
			showInfo("删除失败！", self);
		}
	});

}

function getUuids(){
	var a = document.getElementsByName("uuid");
	var uuids = '';
	for(var i = 0;i<a.length;i++){
		//alert(a[i].value);
		if(a[i].checked){
			if(uuids === ''){
				uuids = a[i].value;
			}else{
				uuids = uuids + ',' + a[i].value;
			}
		}
	}
	return uuids;
}

function resetTitle(){
	if (confirm("确实要清除当前结果的采集标题标志?") == false) return;
	document.location = "resetTitle.jsp?<%=pageUrl%>";
}

function clearTitle(customerUuid, clearType){
	if (confirm("确认要清空标题吗?") == false) return;
	var uuids = getUuids();
	if(clearType == null){
		if(uuids.trim() === ''){
			alert("请选中要操作的关键词！");
			return;
		}
	}
	var postData = {};
	postData.uuids = uuids;
	postData.clearType = clearType;
	postData.customerUuid = customerUuid;

	$$$.ajax({
		url: '/internal/customerkeyword/clearTitle',
		data: JSON.stringify(postData),
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		timeout: 5000,
		type: 'POST',
		success: function (status) {
			if(status){
				showInfo("操作成功！", self);
				window.location.reload();
			}else{
				showInfo("操作失败！", self);
			}
		},
		error: function () {
			showInfo("操作失败！", self);
		}
	});
}

function showChangeOptimizationGroupDialog(self){
	var settingDialogDiv = $$$("#changeOptimizationGroupDialog");
	settingDialogDiv.find("#optimizationGroup").val("");
	settingDialogDiv.show();
	settingDialogDiv.find("#optimizationGroup").focus();
}
function saveChangeOptimizationGroup(self, closable){
	var settingDialogDiv = $$$("#changeOptimizationGroupDialog");
	var vpsOpenSettingVO = {};
	var optimizationGroup = settingDialogDiv.find("#optimizationGroup").val();

	var uuids = getUuids();
	if(uuids.trim() === ''){
		alert("请选中要操作的关键词！");
		return;
	}

	if(optimizationGroup.trim() === ''){
		alert("请输入分组名称！");
		return;
	}

	$$$.ajax({
		url: '/customerkeyword/changeCustomerKeywordGroup.jsp?uuids=' + uuids + "&group=" + optimizationGroup,
		type: 'GET',
		success: function (data) {
			data = data.replace(/\r\n/gm,"");
			if(closable){
				settingDialogDiv.hide();
			}
			if(data === "1"){
				showInfo("更新成功！", self);
				if(closable){
					window.location.reload();
				}else{
					settingDialogDiv.find("#providerId").val("");
				}
			}else{
				showInfo("更新失败！", self);
			}
		},
		error: function () {
			showInfo("更新失败！", self);
			if(closable){
				settingDialogDiv.hide();
			}
		}
	});
}
function cancelChangeOptimizationGroup(){
	var settingDialogDiv = $$$("#changeOptimizationGroupDialog");
	settingDialogDiv.hide();
}

function showInfo(content, e) {
	e = e || window.event;
	var div1 = document.getElementById('div2'); //将要弹出的层
	div1.innerText = content;
	div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见
	div1.style.left = getLeft(e) + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
	div1.style.top = getTop(e) + 5;
	div1.style.position = "absolute";

	var intervalID = setInterval(function(){
		div1.style.display = "none";
	}, 3000);
}

function getTop(e){
	var offset=e.offsetTop;
	if(e.offsetParent!=null) offset+=getTop(e.offsetParent);
	return offset;
}
//获取元素的横坐标
function getLeft(e){
	var offset=e.offsetLeft;
	if(e.offsetParent!=null) offset+=getLeft(e.offsetParent);
	return offset;
}

function showTip(content,e) 
{
	var event = e||window.event;
	var pageX = event.pageX;
    var pageY = event.pageY;
    if(pageX==undefined)
    {
    	pageX=event.clientX+document.body.scrollLeft||document.documentElement.scrollLeft;
    }
    if(pageY==undefined)
    {
        pageY = event.clientY+document.body.scrollTop||document.documentElement.scrollTop;
    }
	var div1 = document.getElementById('div1'); //将要弹出的层 
	div1.innerText = content;
	div1.style.display="block"; //div1初始状态是不可见的，设置可为可见  
	div1.style.left=pageX+10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容 
	div1.style.top=pageY+5; 
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
<div id="changeOptimizationGroupDialog">
  <table style="font-size:12px">
	  <tr>
		  <th>分组名称</th>
		  <td>
			  <input type="text" name="optimizationGroup" id="optimizationGroup" style="width:200px" />
		  </td>
	  </tr>

	  <tr>
		  <td colspan="2" align="right">
			  <input type="button" value="保存" id="saveChangeOptimizationGroup" onClick="saveChangeOptimizationGroup(this, true)"/>
			  &nbsp;&nbsp;&nbsp;<input
				  type="button" onClick="cancelChangeOptimizationGroup()" id="cancelChangeOptimizationGroup" value="取消"/>
		  </td>
	  </tr>
  </table>
</div>
</body>
</html>

