<%@page contentType="text/html;charset=utf-8"%>
<%@page import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*,java.net.URLEncoder"%>
<%@ page import="com.keymanager.monitoring.entity.QZSetting" %>
<%@ page import="com.keymanager.monitoring.service.QZSettingService" %>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="cm" scope="page" class="com.keymanager.manager.CustomerManager" />
<jsp:useBean id="sch" scope="page" class="com.keymanager.util.SpringContextHolder" />
	
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
		String condition = " ";
		
		String curPage = request.getParameter("pg");
		
		if (curPage == null || curPage.equals(""))
		{
		    curPage = "1";
		}
		
		int iCurPage = Integer.parseInt(curPage);		
	  	String uuid = request.getParameter("uuid");
		String customer = request.getParameter("customer");
	    String customerUuid = null;
	    if(customer != null){
			String []customerArray = customer.split("_____");
			if(customerArray.length == 2){
				customerUuid = customerArray[1];
			}
		}
		String domain = request.getParameter("domain");
		String group = request.getParameter("group");
		String updateStatus = request.getParameter("updateStatus");

	  	String pageUrl = "";
	  	
	  	if (!Utils.isNullOrEmpty(uuid)){
	  		pageUrl = pageUrl + "&uuid=" + uuid;
	  	}
		if (!Utils.isNullOrEmpty(customerUuid)){
			pageUrl = pageUrl + "&customerUuid=" + customerUuid;
		}
		if (!Utils.isNullOrEmpty(domain)){
			pageUrl = pageUrl + "&domain=" + domain;
		}else{
			domain = "";
		}
		if (!Utils.isNullOrEmpty(group)){
			pageUrl = pageUrl + "&group=" + group;
		}else{
			group = "";
		}
		if (!Utils.isNullOrEmpty(updateStatus)){
			pageUrl = pageUrl + "&updateStatus=" + updateStatus;
		}else{
			updateStatus = "";
		}

		QZSettingService qzss = (QZSettingService)SpringContextHolder.getBean("QZSettingService");
	  	List itemList = qzss.searchQZSettings(uuid != null ? Long.parseLong(uuid) : null, customerUuid != null ? Long.parseLong(customerUuid) : null,
				domain, group, updateStatus);

	  	int recordCount = itemList != null ? itemList.size() : 0;
	  	int pageCount = recordCount / 100 + (recordCount % 100 > 0 ? 1 : 0);

	  	String fileName = "/qzsetting/list.jsp?" + pageUrl;
	  	String pageInfo = Utils.getPageInfo(iCurPage, 100, recordCount, pageCount, "", fileName);

	    List<CustomerVO> customerVOs = cm.searchCustomer(datasourceName, " AND fStatus = 1 ");
%>

<html>
	<head>
	  <title>全站设置清单</title>
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
		#changeSettingDialog {
			display: none;
			margin: -125px 0px 0px -160px;
			background-color: white;
			color: #2D2A2A;
			font-size: 12px;
			line-height: 12px;
			border: 2px solid #104454;
			width: 320px;
			height: 250px;
			left: 50%;
			top: 50%;
			z-index: 25;
			position: fixed;
		}
		-->
	  </style>
	  <script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	  <script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	  <link href="/css/menu.css" rel="stylesheet" type="text/css" />	
  	</head>	
<body>
      <table width=100% style="font-size:12px;" cellpadding=3>
     	  <tr>
			<td colspan="11" align="left">
				<%@include file="/menu.jsp" %>	
			</td>
		  </tr>
		  <tr>
      	  	 <td colspan="11" align="right">
      	  	 	<a href="javascript:showSettingDialog(null, this)">增加全站设置</a>
				 | <a target="_blank" href="javascript:updateImmediately(this)">马上更新</a>
				 | <a target="_blank" href="javascript:delAllItems(this)">删除所选</a>
      	  	 </td>
      	  </tr>
      	  <tr> 
      	  	 <td colspan="11">
      	  	 	<form method="post" action="list.jsp">
	      	  	 	<table style="font-size:12px;">
	      	  	 		<tr>
	      	  	 			<td align="right">客户:</td>
							<td><input type="text" name="customer" id="customer" list="customer_list" value='<%=(customer != null) ? customer : ""%>'
									   style="width:200px;"></td>
	      	  	 			<td align="right">域名:</td> <td><input type="text" name="domain" id="domain" value="<%=domain%>" style="width:200px;"></td>
							<td align="right">组名:</td> <td><input type="text" name="group" id="group" value="<%=group%>" style="width:200px;"></td>
							<td align="right">状态:</td>
							<td>
								<select name="updateStatus" id="updateStatus" style="width:200px;">
									<%
										String []statusNames = {"", "Processing", "Completed", "DownloadTimesUsed"};
										for (int i = 0; i < statusNames.length; i ++)
										{
											if (statusNames[i].equals(updateStatus))
											{
												out.println("<option selected value='" + statusNames[i] + "'>" + statusNames[i] + "</option>");
											}
											else
											{
												out.println("<option value='" + statusNames[i] + "'>" + statusNames[i] + "</option>");
											}
										}
									%>
								</select>
							</td>
			          	  	<td align="right" width="100"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " ></td>
	      	  	 		</tr>
		      	  	 </table>
      	  	 	</form>
      	  	 </td>
      	  </tr>    
          <tr bgcolor="#eeeeee" height=30>
              <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" /></td>
              <td align="center" width=150>客户</td>
              <td align="center" width=100>域名</td>
              <td align="center" width=80>入口类型</td>
			  <td align="center" width=80>电脑分组</td>
			  <td align="center" width=80>手机分组</td>
			  <td align="center" width=80>去掉没指数</td>
			  <td align="center" width=80>去掉没排名</td>
			  <td align="center" width=80>操作类型</td>
              <td align="center" width=60>更新间隔</td>
			  <td align="center" width=60>更新状态</td>
		      <td align="center" width=80>更新开始时间</td>
              <td align="center" width=80>更新结束时间</td>
			  <td align="center" width=80>更新时间</td>
			  <td align="center" width=80>添加时间</td>
              <td align="center" width=100>操作</td>      
			  <div id="div1"></div>
			  <div id="div2"></div>
		  </tr>
          <%
              String trClass = "";
              String webUrl = "";
              String keywordColor = "";
              for (int i = 0; i < itemList.size(); i ++)
              {
				  QZSetting value = (QZSetting) itemList.get(i);
                  trClass= "";
                  keywordColor = "";
                  if ((i % 2) != 0)
                  {
                     trClass = "bgcolor='#eeeeee'";
                  }
          %>
                 <tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">     
                      <td><input type="checkbox" name="uuid" value=<%=value.getUuid()%> /></td>                                       
                      <td>
                     	<%=value.getContactPerson()%>
                      </td>
					 <td>
						 <%=value.getDomain()%>
					 </td>
					 <td>
						 <%=value.getType()%>
					 </td>
					 <td>
						 <%=value.getPcGroup() == null ? "" : value.getPcGroup()%>
					 </td>
					 <td>
						 <%=value.getPhoneGroup() == null ? "" : value.getPhoneGroup()%>
					 </td>
					 <td>
						 <%=value.isIgnoreNoIndex() ? "是" : "否"%>
					 </td>
					 <td>
						 <%=value.isIgnoreNoOrder() ? "是" : "否"%>
					 </td>
					 <td>
						 <%=value.getOperationType()%>
					 </td>
					 <td>
						 <%=value.getUpdateInterval()%>天
					 </td>
					 <td>
						 <%=(value.getUpdateStatus() != null ? value.getUpdateStatus() : "")%>
					 </td>
					 <td>
						 <%=Utils.formatDate(value.getUpdateStartTime(), "MM-dd HH:mm")%>
					 </td>
					 <td>
						 <%=Utils.formatDate(value.getUpdateEndTime(), "MM-dd HH:mm")%>
					 </td>
					 <td>
						 <%=Utils.formatDate(value.getUpdateTime(), "MM-dd HH:mm")%>
					 </td>
					 <td>
						 <%=Utils.formatDate(value.getCreateTime(), "MM-dd HH:mm")%>
					 </td>
                      <td>
                      	  <a href="javascript:showSettingDialog('<%=value.getUuid()%>', this)">修改</a> |
                      	  <a href="javascript:delItem(<%=value.getUuid()%>)">删除</a>
                      </td>                            
                  </tr>
          <%
              }
          
          %>
          <tr>
          	<td colspan="11">
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
   if (confirm("确实要删除这个全站设置吗?") == false) return;
	$$$.ajax({
		url: '/spring/qzsetting/delete/' + uuid,
		type: 'Get',
		success: function (data) {
			if(data){
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

function getSelectedIDs(){
	var a = document.getElementsByName("uuid");
	var uuids = '';
	for(var i = 0;i<a.length;i++){
		//alert(a[i].value);
		if(a[i].checked){
			if(uuids === ''){
				uuids = a[i].value;
			}else{
				uuids = uuids + "," + a[i].value;
			}
		}
	}
	return uuids;
}

function delAllItems(self)
{
	var uuids = getSelectedIDs();
	if(uuids === ''){
		alert('请选择要操作的设置信息！');
		return ;
	}
	if (confirm("确实要删除这些关键字吗?") == false) return;
	var postData = {};
	postData.uuids = uuids.split(",");
	$$$.ajax({
		url: '/spring/qzsetting/deleteQZSettings',
		data: JSON.stringify(postData),
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		timeout: 5000,
		type: 'POST',
		success: function (data) {
			if(data){
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

function updateImmediately(self){
	var uuids = getSelectedIDs();
	if(uuids === ''){
		alert('请选择要操作的设置信息！');
		return ;
	}
	if (confirm("确实要马上更新这些设置吗？") == false) return;
	var postData = {};
	postData.uuids = uuids;
	$$$.ajax({
		url: '/spring/qzsetting/updateImmediately',
		data: JSON.stringify(postData),
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		timeout: 5000,
		type: 'POST',
		success: function (data) {
			if(data != null && data.status){
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

function showSettingDialog(uuid, self){
	if(uuid == null){
		resetSettingDialog(self);
		return;
	}
	$$$.ajax({
		url: '/spring/qzsetting/getQZSetting/' + uuid,
		type: 'Get',
		success: function (data) {
			if(data != null && data.length > 0){
				var qzSetting = data[0];
				initSettingDialog(qzSetting, self);
			}else{
				showInfo("获取信息失败！", self);
			}
		},
		error: function () {
			showInfo("获取信息失败！", self);
		}
	});
}
function resetSettingDialog(self){
	var settingDialogDiv = $$$("#changeSettingDialog");
	settingDialogDiv.find("#qzSettingUuid").val("");
	settingDialogDiv.find("#qzSettingCustomer").val("");
	settingDialogDiv.find("#qzSettingPcGroup").val("");
	settingDialogDiv.find("#qzSettingPhoneGroup").val("");
	settingDialogDiv.find("#qzSettingDomain").val("");
	settingDialogDiv.find("#qzSettingIgnoreNoIndex").val("1");
	settingDialogDiv.find("#qzSettingIgnoreNoOrder").val("1");
	settingDialogDiv.find("#qzSettingOperationType").val("PC");
	settingDialogDiv.find("#qzSettingInterval").val("2");
	settingDialogDiv.find("#qzSettingEntryType").val("<%=type%>");
	settingDialogDiv[0].style.left = getTop(self); //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
	settingDialogDiv.show();
}
function initSettingDialog(qzSetting, self){
	var settingDialogDiv = $$$("#changeSettingDialog");
	settingDialogDiv.find("#qzSettingUuid").val(qzSetting.uuid);
	settingDialogDiv.find("#qzSettingCustomer").val(qzSetting.contactPerson + "_____" + qzSetting.customerUuid);
	settingDialogDiv.find("#qzSettingPcGroup").val(qzSetting.pcGroup != null ? qzSetting.pcGroup : "");
	settingDialogDiv.find("#qzSettingPhoneGroup").val(qzSetting.phoneGroup != null ? qzSetting.phoneGroup : "");
	settingDialogDiv.find("#qzSettingDomain").val(qzSetting.domain != null ? qzSetting.domain : "");
	settingDialogDiv.find("#qzSettingIgnoreNoIndex").val(qzSetting.ignoreNoIndex ? "1" : "0");
	settingDialogDiv.find("#qzSettingIgnoreNoOrder").val(qzSetting.ignoreNoOrder ? "1" : "0");
	settingDialogDiv.find("#qzSettingOperationType").val(qzSetting.operationType != null ? qzSetting.operationType : "PC");
	settingDialogDiv.find("#qzSettingInterval").val(qzSetting.updateInterval != null ? qzSetting.updateInterval : "");
	settingDialogDiv.find("#qzSettingEntryType").val(qzSetting.type != null ? qzSetting.type : "");
	settingDialogDiv[0].style.left = getTop(self); //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
	settingDialogDiv.show();
}
function saveChangeSetting(self){
	var settingDialogDiv = $$$("#changeSettingDialog");
	var qzSetting = {};
	qzSetting.uuid = settingDialogDiv.find("#qzSettingUuid").val();
	qzSetting.domain = settingDialogDiv.find("#qzSettingDomain").val();
	if(qzSetting.domain == null || qzSetting.domain === ""){
		alert("请输入域名");
		settingDialogDiv.find("#qzSettingDomain").focus();
		return;
	}
	qzSetting.pcGroup = settingDialogDiv.find("#qzSettingPcGroup").val();
	qzSetting.phoneGroup = settingDialogDiv.find("#qzSettingPhoneGroup").val();

	qzSetting.ignoreNoIndex = settingDialogDiv.find("#qzSettingIgnoreNoIndex").val() === "1" ? true : false;
	qzSetting.ignoreNoOrder = settingDialogDiv.find("#qzSettingIgnoreNoOrder").val() === "1" ? true : false;

	qzSetting.operationType = settingDialogDiv.find("#qzSettingOperationType").val();
	if("Both" === qzSetting.operationType){
		if(qzSetting.pcGroup == null || qzSetting.pcGroup === ""){
			alert("请输入电脑分组名称");
			settingDialogDiv.find("#qzSettingPcGroup").focus();
			return;
		}
		if(qzSetting.phoneGroup == null || qzSetting.phoneGroup === ""){
			alert("请输入手机分组名称");
			settingDialogDiv.find("#qzSettingPhoneGroup").focus();
			return;
		}
	}else if("PC" === qzSetting.operationType){
		if(qzSetting.pcGroup == null || qzSetting.pcGroup === ""){
			alert("请输入电脑分组名称");
			settingDialogDiv.find("#qzSettingPcGroup").focus();
			return;
		}
	}else if("PC" === qzSetting.operationType){
		if(qzSetting.phoneGroup == null || qzSetting.phoneGroup === ""){
			alert("请输入手机分组名称");
			settingDialogDiv.find("#qzSettingPhoneGroup").focus();
			return;
		}
	}

	qzSetting.updateInterval = settingDialogDiv.find("#qzSettingInterval").val();
	if(qzSetting.updateInterval == null || qzSetting.updateInterval === ""){
		alert("请选择更新间隔");
		settingDialogDiv.find("#qzSettingInterval").focus();
		return;
	}
	var customer = settingDialogDiv.find("#qzSettingCustomer").val();
	if(customer == null || customer === ""){
		alert("请选择客户");
		settingDialogDiv.find("#qzSettingCustomer").focus();
		return;
	}

	if(customer != null && customer != '' ){
		var customerArray = customer.split("_____");
		if(customerArray.length == 2){
			qzSetting.customerUuid = customerArray[1];
		}else{
			alert("请从列表中选择客户");
			settingDialogDiv.find("#qzSettingCustomer").focus();
			return;
		}
	}
	var entryType = settingDialogDiv.find("#qzSettingEntryType").val();
	qzSetting.type = entryType;

	$$$.ajax({
		url: '/spring/qzsetting/save',
		data: JSON.stringify(qzSetting),
		headers: {
			'Accept': 'application/json',
			'Content-Type': 'application/json'
		},
		timeout: 5000,
		type: 'POST',
		success: function (data) {
			settingDialogDiv.hide();
			if(data != null && data != ""){
				showInfo("更新成功！", self);
				window.location.reload();
			}else{
				showInfo("更新失败！", self);
			}
		},
		error: function () {
			showInfo("更新失败！", self);
			settingDialogDiv.hide();
		}
	});
}
function cancelChangeSetting(){
	var settingDialogDiv = $$$("#changeSettingDialog");
	settingDialogDiv.hide();
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

//关闭层div1的显示 
function closeTip() 
{ 
	 var div1 = document.getElementById('div1'); 
	 div1.style.display="none"; 
} 
</script>


<div style="display:none;">
</div>
<div id="changeSettingDialog">
  <table style="font-size:12px">
	  <tr>
		  <th>客户</th>
		  <td>
			  <input type="hidden" id="qzSettingUuid" />
			  <input type="text" list="customer_list" name="qzSettingCustomer" id="qzSettingCustomer" style="width:240px" />
		  </td>
	  </tr>
	  <tr>
		  <th>域名</th>
		  <td>
			  <input type="text" name="qzSettingDomain" id="qzSettingDomain" style="width:240px" />
		  </td>
	  </tr>
	  <tr>
		  <th>电脑分组</th>
		  <td>
			  <input type="text" name="qzSettingPcGroup" id="qzSettingPcGroup"  style="width:240px"/>
		  </td>
	  </tr>
	  <tr>
		  <th>手机分组</th>
		  <td>
			  <input type="text" name="qzSettingPhoneGroup" id="qzSettingPhoneGroup"  style="width:240px"/>
		  </td>
	  </tr>
	  <tr>
		  <th>入口</th>
		  <td>
			  <select name="qzSettingEntryType" id="qzSettingEntryType"  style="width:240px">
				  <option value="qz" selected>全站</option>
				  <option value="bc">bc</option>
				  <option value="pt">普通</option>
				  <option value="fm">负面</option>
			  </select>
		  </td>
	  </tr>
	  <tr>
		  <th>去掉没指数</th>
		  <td>
			  <select name="qzSettingIgnoreNoIndex" id="qzSettingIgnoreNoIndex"  style="width:240px">
				  <option value="1" selected>是</option>
				  <option value="0">否</option>
			  </select>
		  </td>
	  </tr>
	  <tr>
		  <th>去掉没排名</th>
		  <td>
			  <select name="qzSettingIgnoreNoOrder" id="qzSettingIgnoreNoOrder"  style="width:240px">
				  <option value="1" selected>是</option>
				  <option value="0">否</option>
			  </select>
		  </td>
	  </tr>
	  <tr>
		  <th>操作类型</th>
		  <td>
			  <select name="qzSettingOperationType" id="qzSettingOperationType"  style="width:240px">
				  <option value="PC" selected>电脑</option>
				  <option value="Phone">手机</option>
				  <option value="Both">电脑和手机</option>
			  </select>
		  </td>
	  </tr>
	  <tr>
		  <th>更新间隔</th>
		  <td>
			  <select name="qzSettingInterval" id="qzSettingInterval"  style="width:240px">
				  <option value="1">1天</option>
				  <option value="2" selected>2天</option>
				  <option value="3">3天</option>
				  <option value="5">5天</option>
			  </select>
		  </td>
	  </tr>
	  <tr>
		  <td colspan="2" align="right">
			  <input type="button" id="saveChangeSetting" onClick="saveChangeSetting(this)" value="保存"/>&nbsp;&nbsp;&nbsp;<input type="button" onClick="cancelChangeSetting()" id="cancelChangeSetting" value="取消"/>
		  </td>
	  </tr>
  </table>
</div>
	  <datalist id="customer_list">
		  <%
			  for (int i = 0; i < customerVOs.size(); i++) {
				  CustomerVO value = (CustomerVO) customerVOs.get(i);
				  out.println("<option>" + value.getContactPerson() + "_____" + value.getUuid() + "</option>");
			  }
		  %>
	  </datalist>
</body>
</html>

