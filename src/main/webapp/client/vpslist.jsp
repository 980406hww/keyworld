<%@page contentType="text/html;charset=utf-8"%>
<%@page
	import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>

<jsp:useBean id="um" scope="page" class="com.keymanager.manager.UserManager" />
<jsp:useBean id="voim" scope="page" class="com.keymanager.manager.VPSOpenInfoManager" />
<jsp:useBean id="vpim" scope="page" class="com.keymanager.manager.VPSProviderInfoManager" />

<%@include file="/check.jsp"%>

<%
	if (loginState == 0) {
%>
<script language="javascript">
	window.location.href = "/bd.html";
</script>
<%
	return;
	}

	String username = (String) session.getAttribute("username");
	String password = (String) session.getAttribute("password");
	String clientIDPrefix = request.getParameter("clientIDPrefix");
	String city = request.getParameter("city");
	String openStatus = request.getParameter("openStatus");
	String operationType = request.getParameter("operationType");

	username = Utils.parseParam(username);
	password = Utils.parseParam(password);

	if (username == null || username.equals("")) {
%>
<script language="javascript">
	window.location.href = "/bd.html";
</script>
<%
	return;
	}
	UserVO user = um.login(datasourceName, username, password);
	if ((user.getStatus() == 0) || !(user.isVipType())) {
%>
<script language="javascript">
	alert("你没有该权限！");
	window.history.back();
</script>
<%
	return;
	}

	String condition = "";

	String curPage = request.getParameter("pg");

	if (curPage == null || curPage.equals("")) {
		curPage = "1";
	}

	int iCurPage = Integer.parseInt(curPage);

	String pageUrl = "prefix=1";

	if (!Utils.isNullOrEmpty(clientIDPrefix)) {
		condition = condition + " and fClientIDPrefix = '"+ clientIDPrefix.trim() + "' ";
		pageUrl = pageUrl + "&clientIDPrefix=" + clientIDPrefix;
	} else {
		clientIDPrefix = "";
	}

	if (!Utils.isNullOrEmpty(openStatus)) {
		condition = condition + " and fOpenStatus = '" + openStatus.trim() + "' ";
		pageUrl = pageUrl + "&openStatus=" + openStatus;
	} else {
		openStatus = "";
	}
	
	if (!Utils.isNullOrEmpty(operationType)) {
		condition = condition + " and fOperationType = '" + operationType.trim() + "' ";
		pageUrl = pageUrl + "&operationType=" + operationType;
	} else {
		operationType = "";
	}

	if (!Utils.isNullOrEmpty(city)) {
		condition = condition + " and fCity like '" + city.trim()
				+ "%' ";
		pageUrl = pageUrl + "&city=" + java.net.URLEncoder.encode(city,  "UTF-8");
	} else {
		city = "";
	}

	List itemList = voim.searchVPSOpenInfos(datasourceName, 50, iCurPage, condition, " ORDER BY voi.fCreateTime desc ", 1);

	List vpsProviderInfoList = vpim.getAllVPSProviderInfoVOs(datasourceName);
	String vpsProviderInfoString = vpim.getAllVPSProviderInfoString(datasourceName);

	int recordCount = voim.getRecordCount();
	int pageCount = voim.getPageCount();

	String fileName = "/client/vpslist.jsp?" + pageUrl;
	String pageInfo = Utils.getPageInfo(iCurPage, 50, recordCount, pageCount, "", fileName);
%>

<html>
<head>
<title>开机信息列表</title>
<style>
.wrap {
	word-break: break-all;
	word-wrap: break-word;
}

<!--
#div1 {
	display: none;
	background-color: #f6f7f7;
	color: #333333;
	font-size: 12px;
	line-height: 18px;
	border: 1px solid #e1e3e2;
	width: 250px;
	height: 50px;
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
	margin: -290px 0px 0px -150px;
	background-color: white;
    color: #2D2A2A;
    font-size: 12px;
    line-height: 12px;
    border: 2px solid #104454;
    width: 300px;
    height: 580px;
    left: 50%;
    top: 50%;
    z-index: 25;
    position: fixed;
}
#changeSettingDialog tr {
	height : 30px;
}
#changeSettingDialog th {
	text-align : right;
}

#changeSettingDialog select {
	width : 152px;
}

#changeSettingDialog input[type=text] {
	width : 152px;
}

#openInfoSettingDialog {
	display: none;
	margin: -55px 0px 0px -210px;
	background-color: white;
    color: #2D2A2A;
    font-size: 12px;
    line-height: 12px;
    border: 2px solid #104454;
    width: 440px;
    height: 140px;
    left: 50%;
    top: 50%;
    z-index: 25;
    position: fixed;
}

#vpsConfigSettingDialog {
	display: none;
	margin: -115px 0px 0px -220px;
	background-color: white;
	color: #2D2A2A;
	font-size: 12px;
	line-height: 12px;
	border: 2px solid #104454;
	width: 440px;
	height: 230px;
	left: 50%;
	top: 50%;
	z-index: 25;
	position: fixed;
}

-->
</style>
<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	<script type="text/javascript" src="/ui/jquery.ui.core.js"></script>
	<script type="text/javascript" src="/ui/jquery.ui.widget.js"></script>
	<script type="text/javascript" src="/assets/prettify.js"></script>
	<script type="text/javascript" src="/multiselectSrc/jquery.multiselect.js"></script>

<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
<link href="/css/menu.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="/multiselectSrc/jquery.multiselect.css" />
	<link rel="stylesheet" type="text/css" href="/assets/style.css" />
	<link rel="stylesheet" type="text/css" href="/assets/prettify.css" />
	<link rel="stylesheet" type="text/css" href="/ui/jquery-ui.css" />
</head>
<body>
	<table width=100% style="font-size: 12px;" cellpadding=3>
		<tr>
			<td colspan=16 align="left"><%@include file="/menu.jsp"%>
			</td>
		</tr>
		<tr>
			<td colspan=17>
				<form method="post" action="vpslist.jsp">
					<table style="font-size: 12px;width:100%">
						<tr>
							<td align="right">客户端前缀:</td>
							<td><input type="text" name="clientIDPrefix" id="clientIDPrefix"
								value="<%=clientIDPrefix%>" style="width: 90px;"></td>
							<td align="right">城市:</td>
							<td><input type="text" name="city" id="city"
								value="<%=city%>" style="width: 120px;"></td>
							<td align="right">开机状态:</td>
							<td><select name="openStatus" id="openStatus">
									<%
										String[] openStatuses = {"", "Processing", "succ", "loginfail", "openurltimeout", "nomachine", "opentimeout"};
										for (int i = 0; i < openStatuses.length; i++) {
											if (openStatuses[i].equals(openStatus)) {
												out.println("<option selected value='" + openStatuses[i]
														+ "'>" + openStatuses[i] + "</option>");
											} else {
												out.println("<option value='" + openStatuses[i] + "'>"
														+ openStatuses[i] + "</option>");
											}
										}
									%>
							</select></td>
							<td align="right">操作类型:</td>
							<td><select name="operationType" id="operationType">
									<%
										String[] operationTypeValues = {"", "PC", "Phone"};
										for (int operationTypeValueIndex = 0; operationTypeValueIndex < operationTypeValues.length; operationTypeValueIndex++) {
											if (operationTypeValues[operationTypeValueIndex].equals(operationType)) {
												out.println("<option selected value='" + operationTypeValues[operationTypeValueIndex]
														+ "'>" + operationTypeValues[operationTypeValueIndex] + "</option>");
											} else {
												out.println("<option value='" + operationTypeValues[operationTypeValueIndex] + "'>"
														+ operationTypeValues[operationTypeValueIndex] + "</option>");
											}
										}
									%>
							</select></td>
							<td align="right" width="30px"><input type="submit" name="btnFilter" id="btnFilter" value=" 查询 "></td>
						</tr>
						<tr>
						<td colspan="17">
						</td>
						</tr>
						<tr>
							<td colspan="17" align="right">
								<a target="_blank" href="javascript:resetVPSStatus(this, 'open')">重设开机状态</a> |
								<a target="_blank" href="javascript:resetVPSStatus(this, 'setting')">重设设置状态</a> |
								<a target="_blank" href="javascript:showopenInfoSettingDialog(this)">设置开机信息</a>
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
		<tr bgcolor="#eeeeee" height=30>
			<td align="center" width=10><input type="checkbox" onclick="selectAll(this)" /></td>
			<td align="center" width=100>VPS信息</td>
			<td align="center" width=50>操作类型</td>
			<td align="center" width=50>分配序号</td>
			<td align="center" width=80>VNC主机</td>
			<td align="center" width=30>VNC端口</td>
			<td align="center" width=40>VNC用户名</td>
			<td align="center" width=40>VNC密码</td>
			<td align="center" width=40>宽带用户名</td>
			<td align="center" width=40>宽带密码</td>
			<td align="center" width=30>开机状态</td>
			<td align="center" width=80>开机开始/结束时间</td>
			<td align="center" width=30>设置状态</td>
			<td align="center" width=40>开始设置时间</td>
			<td align="center" width=50>后台ID</td>
			<td align="center" width=40>状态</td>
			<td align="center" width=30>操作</td>
			<div id="div1"></div>
			<div id="div2"></div>
		</tr>
		<%
			String trClass = "";
			String webUrl = "";
			boolean validClient = true;
			for (int i = 0; i < itemList.size(); i++) {
				VPSOpenInfoVO value = (VPSOpenInfoVO) itemList.get(i);
				trClass = "";
				validClient = true;
				if ((i % 2) != 0) {
					trClass = "bgcolor='#eeeeee'";
				}
		%>
		<tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">
			<td><input type="checkbox" name="vpsID" value="<%=value.getUuid()%>"/></td>
			<td><%=value.formatProviderInfo()%></td>
			<td><%=value.getOperationType()%></td>
			<td><%=value.getCurrentSequence()%></td>
			<td><%=value.getVncHost() != null ? value.getVncHost() : ""%></td>
			<td><%=value.getVncPort() != null ? value.getVncPort() : ""%></td>
			<td><%=value.getVncUser() != null ? value.getVncUser() : ""%></td>
			<td><%=value.getVncPassword() != null ? value.getVncPassword() : ""%></td>

			<td><%=value.getConnUser() != null ? value.getConnUser() : ""%></td>
			<td><%=value.getConnPassword() != null ? value.getConnPassword() : ""%></td>
			<td><%=value.getOpenStatus() != null ? value.getOpenStatus() : ""%></td>

			<td><%=Utils.formatDatetime(value.getStartOpenTime(), "MM-dd HH:mm")%>
				&nbsp;&nbsp;<%=Utils.formatDatetime(value.getCompleteOpenTime(), "MM-dd HH:mm")%></td>
			<td><%=value.getSettingStatus() != null ? value.getSettingStatus() : ""%></td>
			<td><%=Utils.formatDatetime(value.getStartSettingTime(), "MM-dd HH:mm")%>
				&nbsp;&nbsp;<%=Utils.formatDatetime(value.getCompleteSettingTime(), "MM-dd HH:mm")%></td>
			<td><%=value.getVpsUuid() != null ? value.getVpsUuid() : ""%></td>
			<td><%=value.getStatus() == 1 ? "Activie" : "Inactive"%></td>
			<td>
				<a href="javascript:showVPSConfigSettingDialog(this, '<%=value.getUuid()%>')">修改</a>
				<%
					if(value.getOpenStatus() == null){
					if(value.getStatus() == 1){
				%>

					<a href="javascript:stopOpen('<%=value.getUuid()%>')">暂停</a>
				<% }else{ %>
					<a href="javascript:startOpen('<%=value.getUuid()%>')">开启</a>
				<%	}
				%>
					<a href="javascript:deleteVPSOpenInfoVO('<%=value.getUuid()%>')">删除</a>
				<%
					}
				%>
			</td>
		</tr>
		<%
			}
		%>

		<tr>
			<td colspan=17 align="right"><br>
				<%=pageInfo%></td>
		</tr>
	</table>
	</form>
	<br>
	<br>

	<script language="javascript">
		function selectAll(self){
			var a = document.getElementsByName("vpsID");
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
		function getSelectedVPSIDs(){
			var a = document.getElementsByName("vpsID");
			var vpsIDs = '';
			for(var i = 0;i<a.length;i++){
				//alert(a[i].value);
				if(a[i].checked){
					if(vpsIDs === ''){
						vpsIDs = a[i].value;
					}else{
						vpsIDs = vpsIDs + "," + a[i].value;
					}
				}
			}
			return vpsIDs;
		}
		function resetVPSStatus(self, stage)
		{
			var ids = getSelectedVPSIDs();
			if(ids == ''){
				alert("请选择要重置的开机记录！");
				return;
			}
			if (confirm("确实要重设" + (stage === 'open' ? "开机" : "设置") + "状态吗?") == false) return;
			$$$.ajax({
				url: '/client/resetVPSStatus.jsp?stage=' + stage + "&uuids=" + ids ,
				type: 'Get',
				success: function (data) {
					data = data.replace(/\r\n/gm,"");
					data = data.replace(/\n/gm,"");
					if(data === "1"){
						showInfo("更新成功！", self);
						window.location.reload();
					}else{
						showInfo("更新失败！", self);
					}
				},
				error: function () {
					showInfo("更新失败！", self);
				}
			});
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
		function deleteVPSOpenInfoVO(uuid)
		{
		   if (confirm("确实要删除这条开机信息吗?") == false) return;
		   document.location = "deletevps.jsp?uuid=" + uuid;
		}
		function stopOpen(uuid) {
			if (confirm("确实要暂停开这台终端吗?") == false)
				return;
			document.location = "stopOpen.jsp?uuid=" + uuid;
		}
		function startOpen(uuid) {
			if (confirm("确实要重新开这台终端吗?") == false)
				return;
			document.location = "startOpen.jsp?uuid=" + uuid;
		}
		function showopenInfoSettingDialog(self){
			var settingDialogDiv = $$$("#openInfoSettingDialog");
			settingDialogDiv.show();
			settingDialogDiv.find("#providerId").focus();
		}
		function saveOpenInfoSetting(self, closable){
			var settingDialogDiv = $$$("#openInfoSettingDialog");
			var vpsOpenSettingVO = {};
			var vpsInfo = settingDialogDiv.find("#providerId").val();

			if(vpsInfo.trim() === ''){
				var vpsInfos = settingDialogDiv.find("#providerIds");
				var ids = vpsInfos.multiselect("MyValues");
				if(ids === ''){
					alert("请选择机器类型！");
					return;
				}else{
					vpsOpenSettingVO.providerIds = ids;
				}
			}else{
				var vpsInfos = vpsInfo.split("_____");
				if(vpsInfos.length == 2){
					vpsOpenSettingVO.providerId = vpsInfos[1];
				}else{
					alert("请从下拉列表选择机器类型！");
					return;
				}
			}

			vpsOpenSettingVO.operationType = settingDialogDiv.find("#tmpOperationType").val();

			var count = settingDialogDiv.find("#count").val();

			if(count.trim() === ''){
				alert("请输入开机数量！");
				return;
			}else if(isNaN(count)){
				alert("请开机数量必须为数字！");
				return;
			}
			vpsOpenSettingVO.count = count;

		    $$$.ajax({
		        url: '/client/updateVPSOpenSetting.jsp',
		        data: "data=" + JSON.stringify(vpsOpenSettingVO),
		        type: 'POST',
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
		function cancelOpenInfoSetting(){
			var settingDialogDiv = $$$("#openInfoSettingDialog");
			settingDialogDiv.hide();
		}


		function showVPSConfigSettingDialog(self, uuid){
			$$$.ajax({
				url: '/internal/vpsopeninfo/getVPSOpenInfo/' + uuid,
				type: 'Get',
				success: function (data) {
					if(data == null){
						showInfo("获取信息失败！", self);
					}else{
						initSettingDialog(data, self);
					}
				},
				error: function () {
					showInfo("获取信息失败！", self);
				}
			});
		}

		function initSettingDialog(vpsOpenInfo, self){
			var settingDialogDiv = $$$("#vpsConfigSettingDialog");
			settingDialogDiv.find("#vpsOpenInfoUuid").val(vpsOpenInfo.uuid);
			settingDialogDiv.find("#vncHost").val(vpsOpenInfo.vncHost != null ? vpsOpenInfo.vncHost : "");
			settingDialogDiv.find("#vncPort").val(vpsOpenInfo.vncPort != null ? vpsOpenInfo.vncPort : "");
			settingDialogDiv.find("#vncUser").val(vpsOpenInfo.vncUser != null ? vpsOpenInfo.vncUser : "administrator");
			settingDialogDiv.find("#vncPassword").val(vpsOpenInfo.vncPassword != null ? vpsOpenInfo.vncPassword : "doshows123");

			settingDialogDiv.find("#connUser").val(vpsOpenInfo.connUser != null ? vpsOpenInfo.connUser : "");
			settingDialogDiv.find("#connPassword").val(vpsOpenInfo.connPassword != null ? vpsOpenInfo.connPassword : "");
			settingDialogDiv.find("#vpsUuid").val(vpsOpenInfo.vpsUuid != null ? vpsOpenInfo.vpsUuid : "");
			settingDialogDiv.find("#vpsOpenStatus").val(vpsOpenInfo.openStatus != null ? vpsOpenInfo.openStatus : "succ");
			settingDialogDiv[0].style.left = getTop(self); //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
			settingDialogDiv.show();
		}

		function saveVPSConfigSetting(self){
			var settingDialogDiv = $$$("#vpsConfigSettingDialog");
			var vpsOpenSettingVO = {};
			var vpsOpenInfoUuid = settingDialogDiv.find("#vpsOpenInfoUuid").val();
			vpsOpenSettingVO.uuid = vpsOpenInfoUuid;
			vpsOpenSettingVO.vncHost = settingDialogDiv.find("#vncHost").val();
			vpsOpenSettingVO.vncPort = settingDialogDiv.find("#vncPort").val();
			vpsOpenSettingVO.vncUser = settingDialogDiv.find("#vncUser").val();
			vpsOpenSettingVO.vncPassword = settingDialogDiv.find("#vncPassword").val();

			vpsOpenSettingVO.connUser = settingDialogDiv.find("#connUser").val();
			vpsOpenSettingVO.connPassword = settingDialogDiv.find("#connPassword").val();
			vpsOpenSettingVO.vpsUuid = settingDialogDiv.find("#vpsUuid").val();
			vpsOpenSettingVO.openStatus = settingDialogDiv.find("#vpsOpenStatus").val();

			$$$.ajax({
				url: '/internal/vpsopeninfo/saveVPSOpenInfo',
				data: JSON.stringify(vpsOpenSettingVO),
				type: 'POST',
				headers: {
					'Accept': 'application/json',
					'Content-Type': 'application/json'
				},
				success: function (data) {
					settingDialogDiv.hide();
					if(data === ""){
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
		function cancelVPSConfigSetting(){
			var settingDialogDiv = $$$("#vpsConfigSettingDialog");
			settingDialogDiv.hide();
		}



		function showTip(content, e) {
			e = e || window.event;
			var div1 = document.getElementById('div1'); //将要弹出的层 
			div1.innerText = content;
			div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见  
			div1.style.left = e.clientX + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容 
			div1.style.top = e.clientY + 5;
			div1.style.position = "absolute";
		}

		//关闭层div1的显示 
		function closeTip() {
			var div1 = document.getElementById('div1');
			div1.style.display = "none";
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

	    $$$(document).ready(function(){
			var openInfoSettingDialogDiv = $$$('#openInfoSettingDialog');
			openInfoSettingDialogDiv.attr('tabindex', 1).keydown(function(event){
				if(event.keyCode == 13){
					var vpsInfo = openInfoSettingDialogDiv.find("#providerId").val();
					var vpsInfos = vpsInfo.split("_____");
					if(vpsInfos.length == 2){
						saveOpenInfoSetting(openInfoSettingDialogDiv, false);
					}
				}
			});

			$$$("#providerIds").multiselect({
				noneSelectedText: "==请选择==",
				checkAllText: "全选",
				uncheckAllText: '全不选',
				minWidth: 360,
				selectedList:3
			});
			$$$(".ui-multiselect-menu").width("340px")
	    });
	</script>


	<div style="display: none;">
		<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660"
			language="JavaScript"></script>

	</div>
	<div id="openInfoSettingDialog">
		<table style="font-size:12px">
			<tr>
				<th>机器类型</th>
				<td>					
					<input type="text" list="provider_list" name="providerId" id="providerId" style="width:360px" />
				</td>
			</tr>

			<tr>
				<th>机器类型</th>
				<td>
					<select id="providerIds" name="providerIds" multiple="multiple" size="40">
						<%=vpsProviderInfoString%>
					</select>
				</td>
			</tr>


			<tr>
				<th>操作类型</th>
				<td>
					<select name="tmpOperationType" id="tmpOperationType"  style="width:360px">
						<option value="PC">PC</option>
						<option value="Phone">Phone</option>
					</select>
				</td>
			</tr>
			<tr>
				<th>数量</th>
				<td>
					<input type="text" name="count" id="count" value="1" style="width:360px" />
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right">
					<input type="button" value="保存" id="saveOpenInfoSetting" onClick="saveOpenInfoSetting(this, true)"/> &nbsp;&nbsp;&nbsp;<input
						type="button" onClick="cancelOpenInfoSetting()" id="cancelOpenInfoSetting" value="取消"/>
				</td>
			</tr>	
		</table>
	</div>
	<div id="vpsConfigSettingDialog">
		<table style="font-size:12px">
			<tr>
				<th>VNC主机</th>
				<td>
					<input type="hidden" name="vpsOpenInfoUuid" id="vpsOpenInfoUuid" style="width:360px" />
					<input type="text" name="vncHost" id="vncHost" style="width:360px" />
				</td>
			</tr>

			<tr>
				<th>VNC端口</th>
				<td>
					<input type="text" name="vncPort" id="vncPort" style="width:360px" />
				</td>
			</tr>

			<tr>
				<th>VNC用户名</th>
				<td>
					<input type="text" name="vncUser" id="vncUser" style="width:360px" />
				</td>
			</tr>

			<tr>
				<th>VNC密码</th>
				<td>
					<input type="text" name="vncPassword" id="vncPassword" style="width:360px" />
				</td>
			</tr>

			<tr>
				<th>宽带用户名</th>
				<td>
					<input type="text" name="connUser" id="connUser" style="width:360px" />
				</td>
			</tr>

			<tr>
				<th>宽带密码</th>
				<td>
					<input type="text" name="connPassword" id="connPassword" style="width:360px" />
				</td>
			</tr>

			<tr>
				<th>后台ID</th>
				<td>
					<input type="text" name="vpsUuid" id="vpsUuid" style="width:360px" />
				</td>
			</tr>

			<tr>
				<th>开机状态</th>
				<td>
					<input type="text" name="vpsOpenStatus" id="vpsOpenStatus" value="succ" style="width:360px" />
				</td>
			</tr>

			<tr>
				<td colspan="2" align="right">
					<input type="button" value="保存" id="saveVPSConfigSetting" onClick="saveVPSConfigSetting(this)"/> &nbsp;&nbsp;&nbsp;<input
						type="button" onClick="cancelVPSConfigSetting()" id="cancelVPSConfigSetting" value="取消"/>
				</td>
			</tr>
		</table>
	</div>
	<div style="overflow:auto;height: 50px">
		<datalist id="provider_list">
			<%
				for (int i = 0; i < vpsProviderInfoList.size(); i++) {
					VPSProviderInfoVO value = (VPSProviderInfoVO) vpsProviderInfoList.get(i);
					out.println("<option>" + value.format() + "_____" + value.getUuid() + "</option>");
				}
			%>
		</datalist>
	</div>
</body>
</html>

