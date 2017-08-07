<%@page contentType="text/html;charset=utf-8"%>
<%@page
	import="com.keymanager.manager.*,com.keymanager.util.*,com.keymanager.value.*,java.util.*"%>
<%@ page import="com.keymanager.monitoring.enums.TerminalTypeEnum" %>
<%@ page import="com.keymanager.util.PortTerminalTypeMapping" %>
<%@ page import="java.lang.reflect.Array" %>

<jsp:useBean id="um" scope="page"
	class="com.keymanager.manager.UserManager" />
<jsp:useBean id="csm" scope="page"
	class="com.keymanager.manager.ClientStatusManager" />

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

	String clientID = request.getParameter("clientID");
	String groupName = request.getParameter("groupName");
	String noGroup = request.getParameter("noGroup");
	String version = request.getParameter("version");
	String city = request.getParameter("city");
	String upgradeFailedReason = request.getParameter("upgradeFailedReason");
	String valid = request.getParameter("valid");
	String hasProblem = request.getParameter("hasProblem");
	String showFetchKeywordStatus = request.getParameter("showFetchKeywordStatus");

	String renewal = request.getParameter("renewal");
	String operationType = request.getParameter("operationType");
	String noOperationType = request.getParameter("noOperationType");
	String noVNC = request.getParameter("noVNC");
	String noUpgrade = request.getParameter("noUpgrade");
	String orderBy = request.getParameter("orderBy");

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

	String condition = " and fTerminalType = '" + terminalType + "' ";

	String curPage = request.getParameter("pg");

	if (curPage == null || curPage.equals("")) {
		curPage = "1";
	}

	int iCurPage = Integer.parseInt(curPage);

	String pageUrl = "prefix=1";

	if (!Utils.isNullOrEmpty(clientID)) {
		condition = condition + " and fClientID like '%"
				+ clientID.trim() + "%' ";
		pageUrl = pageUrl + "&clientID="
				+ java.net.URLEncoder.encode(clientID, "UTF-8");
	} else {
		clientID = "";
	}

	if (!Utils.isNullOrEmpty(groupName)) {
		condition = condition + " and fGroup like '%" + groupName.trim()
				+ "%' ";
		pageUrl = pageUrl + "&groupName=" + groupName;
	} else {
		groupName = "";
	}

	if (!Utils.isNullOrEmpty(upgradeFailedReason)) {
		condition = condition + " and fUpgradeFailedReason like '%" + upgradeFailedReason.trim()
				+ "%' ";
		pageUrl = pageUrl + "&upgradeFailedReason=" + upgradeFailedReason;
	} else {
		upgradeFailedReason = "";
	}

	if (!Utils.isNullOrEmpty(version)) {
		condition = condition + " and fVersion like '" + version.trim()
				+ "%' ";
		pageUrl = pageUrl + "&version=" + version;
	} else {
		version = "";
	}
	if (noOperationType != null){
		condition = condition + " AND fOperationType is null ";
		pageUrl = pageUrl + "&noOperationType=noOperationType";
	}else{
		if (!Utils.isNullOrEmpty(operationType)) {
			condition = condition + " and fOperationType = '" + operationType + "' ";
			pageUrl = pageUrl + "&operationType=" + operationType;
		} else {
			operationType = "";
		}
	}
	if (!Utils.isNullOrEmpty(city)) {
		condition = condition + " and fCity like '" + city.trim()
				+ "%' ";
		pageUrl = pageUrl + "&city=" + java.net.URLEncoder.encode(city,  "UTF-8");
	} else {
		city = "";
	}

	if (!Utils.isNullOrEmpty(valid)) {
		condition = condition + " and fValid = " + valid.trim() + " ";
		pageUrl = pageUrl + "&valid=" + valid;
	} else {
		valid = "";
	}

	if (hasProblem != null){
		condition = condition + " AND (fContinuousFailCount > 5 OR DATE_ADD(fLastVisitTime, INTERVAL IF((10 > (fPageNo*3)), 10, (fPageNo * 3)) MINUTE) < NOW()) and fValid = 1 ";
		pageUrl = pageUrl + "&hasProblem=hasProblem";
	}

	if (renewal != null){
		condition = condition + " AND DATE_ADD(fRenewalDate, INTERVAL -3 DAY) < CURRENT_DATE() and fValid = 1  ";
		pageUrl = pageUrl + "&renewal=renewal";
	}

	if (noGroup != null){
		condition = condition + " AND fGroup is null ";
		pageUrl = pageUrl + "&noGroup=noGroup";
	}
	
	if (noVNC != null){
		condition = condition + " AND (fHost is null or fHost = '') ";
		pageUrl = pageUrl + "&noVNC=noVNC";
	}

	if (showFetchKeywordStatus != null){
		pageUrl = pageUrl + "&showFetchKeywordStatus=showFetchKeywordStatus";
	}

	if (noUpgrade != null){
		condition = condition + " AND (fVersion <> fTargetVersion) ";
		pageUrl = pageUrl + "&noUpgrade=noUpgrade";
	}

	pageUrl = pageUrl + "&orderBy=" + orderBy;

	List itemList = csm.searchClientStatusVOs(datasourceName, 50,
			iCurPage, condition, " ORDER BY " + orderBy + " ", 1);

	int recordCount = csm.getRecordCount();
	int pageCount = csm.getPageCount();

	String fileName = "/client/clientlist.jsp?" + pageUrl;
	String pageInfo = Utils.getPageInfo(iCurPage, 50, recordCount, pageCount, "", fileName);
%>

<html>
<head>
<title>客户端列表</title>
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
	margin: -255px 0px 0px -295px;
	background-color: white;
    color: #2D2A2A;
    font-size: 12px;
    line-height: 12px;
    border: 2px solid #104454;
    width: 590px;
    height: 510px;
    left: 50%;
    top: 50%;
    z-index: 25;
    position: fixed;
}
#changeSettingDialog tr {
	height : 24px;
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

#td_2 input[type=text] {
	width : 50px;
}

#targetVersionSettingDialog {
	display: none;
	margin: -30px 0px 0px -110px;
	background-color: white;
    color: #2D2A2A;
    font-size: 12px;
    line-height: 12px;
    border: 2px solid #104454;
    width: 220px;
    height: 60px;
    left: 50%;
    top: 50%;
    z-index: 25;
    position: fixed;
}

#renewalSettingDialog {
	display: none;
	margin: -40px 0px 0px -100px;
	background-color: white;
	color: #2D2A2A;
	font-size: 12px;
	line-height: 12px;
	border: 2px solid #104454;
	width: 200px;
	height: 80px;
	left: 50%;
	top: 50%;
	z-index: 25;
	position: fixed;
}
-->
</style>
<script language="javascript" type="text/javascript" src="/common.js"></script>
<script language="javascript" type="text/javascript"
	src="/js/My97DatePicker/WdatePicker.js"></script>
<script language="javascript" type="text/javascript"
	src="/js/jquery142.js"></script>
<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
<link href="/css/menu.css" rel="stylesheet" type="text/css" />
</head>
<body>
	<table width=100% style="font-size: 12px;" cellpadding=3>
		<tr>
			<td colspan=16 align="left"><%@include file="/menu.jsp"%>
			</td>
		</tr>
		<tr>
			<td colspan=16>
				<form method="post" action="clientlist.jsp">
					<table style="font-size: 12px;width:100%">
						<tr>
							<td align="left" colspan="2">
								客户端ID:<input type="text" name="clientID" id="clientID" value="<%=clientID%>" style="width: 90px;">
								&nbsp;&nbsp;&nbsp;
								优化组:<input type="text" name="groupName" id="groupName" value="<%=groupName%>" style="width: 120px;">
								&nbsp;&nbsp;&nbsp;
								版本:<input type="text" name="version" id="version" value="<%=version%>" style="width: 60px;">
								&nbsp;&nbsp;&nbsp;
								城市:<input type="text" name="city" id="city" value="<%=city%>" style="width: 120px;">
								&nbsp;&nbsp;&nbsp;
								失败原因:<input type="text" name="upgradeFailedReason" id="upgradeFailedReason" value="<%=upgradeFailedReason%>"
											  style="width: 50px;">
								&nbsp;&nbsp;&nbsp;
								状态:<select name="valid" id="valid">
									<%
										String[] valids = {"全部", "暂停", "监控中"};
										String[] validValues = {"", "0", "1"};
										for (int i = 0; i < valids.length; i++) {
											if (validValues[i].equals(valid)) {
												out.println("<option selected value='" + validValues[i]
														+ "'>" + valids[i] + "</option>");
											} else {
												out.println("<option value='" + validValues[i] + "'>"
														+ valids[i] + "</option>");
											}
										}
									%>
							</select>
							&nbsp;&nbsp;&nbsp;
							操作类型:<select name="operationType" id="operationType">
									<%
										String[] operationTypeValues = {"",
												"pc_pm","pc_pm2","pc_pm3","pc_xg","pc_xg2","pc_xg3","pc_xl","pc_pm_sogou","pc_pm_360","pc_pm_58",
												"pc_pm_zhidao", "pc_pm_wenku", "pc_tieba"};
										if(TerminalTypeEnum.Phone.name().equals(terminalType)){
											operationTypeValues = new String[]{"", "m_pm", "m_xl", "m_xg", "m_pm_sm", "m_xl2"};
										}
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
							</select>
								&nbsp;&nbsp;&nbsp;
							排序:<select name="orderBy" id="orderBy">
								<%
									String[] orderByNames = {"ID", "最后工作时间", "发通知时间", "重启时间", "重启排序时间", "重启次数"};
									String[] orderByValues = {"fClientID", "fLastVisitTime", "fLastSendNotificationTime", "fRestartTime",
											"fRestartOrderingTime", "fRestartCount desc"};
									for (int orderByIndex = 0; orderByIndex < orderByValues.length; orderByIndex++) {
										if (orderByValues[orderByIndex].equals(orderBy)) {
											out.println("<option selected value='" + orderByValues[orderByIndex]
													+ "'>" + orderByNames[orderByIndex] + "</option>");
										} else {
											out.println("<option value='" + orderByValues[orderByIndex] + "'>"
													+ orderByNames[orderByIndex] + "</option>");
										}
									}
								%>
							</select></td>
						</tr>
						<tr>
						<td align="left">
							<input id="hasProblem" name="hasProblem" type="checkbox" value="hasProblem" <%if (hasProblem != null){
						out.print(" checked=true");}%>>停了</input>
							&nbsp;&nbsp;&nbsp;
							<input id="renewal" name="renewal" type="checkbox" value="renewal" <%if (renewal != null){
							out.print(" checked=true");}%>>续费</input>
							&nbsp;&nbsp;&nbsp;
							<input id="noGroup" name="noGroup" type="checkbox" value="noGroup" <%if (noGroup != null){ out.print(" checked=true");}%>>没分组</input>
							&nbsp;&nbsp;&nbsp;
							<input id="noOperationType" name="noOperationType" type="checkbox" value="noOperationType" <%if (noOperationType != null){ out.print(" checked=true");}%>>没操作类型</input>
							&nbsp;&nbsp;&nbsp;
							<input id="noVNC" name="noVNC" type="checkbox" value="noVNC" <%if (noVNC != null){ out.print(" checked=true");}%>>没VNC</input>
							&nbsp;&nbsp;&nbsp;
							<input id="noUpgrade" name="noUpgrade" type="checkbox" value="noUpgrade" <%if (noUpgrade != null){ out.print(" checked=true");}%>>没升级</input>
							&nbsp;&nbsp;&nbsp;
							<input id="showFetchKeywordStatus" name="showFetchKeywordStatus" type="checkbox" value="showFetchKeywordStatus"
								<%if (showFetchKeywordStatus != null){ out.print(" checked=true");}%>>显示取词状态</input>
						</td>
						<td align="right" width="30px"><input type="submit"
															  name="btnFilter" id="btnFilter" value=" 查询 "></td>
						</tr>
						<tr>
						<td colspan="2">
						</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
								<a target="_blank" href="javascript:showTargetVersionSettingDialog(this)">设定目标版本</a>
								|<a target="_blank" href="javascript:showRenewalSettingDialog(this)">续费</a>
								|<a target="_blank" href="javascript:delAllItems()">删除所选</a>
								|<a target="_blank" href="javascript:resetRestartStatus()">重置重启状态</a>
								|<a target="_blank" href="/client/uploadvnc.jsp">上传VNC文件</a>
								|<a target="_blank" href="/client/downloadvnc.jsp">下载VNC连接压缩文件</a>
							</td>
						</tr>
					</table>
				</form>
			</td>
		</tr>
		<tr bgcolor="#eeeeee" height=30>
			<td align="center" width=10><input type="checkbox" onclick="selectAll(this)" /></td>
			<td align="center" width=40>客户端ID</td>
			<td align="center" width=100>优化组</td>
			<td align="center" width=60>操作类型</td>
			<td align="center" width=40>续费</br>日期</td>
			<td align="center" width=40>现版本</br>目标版本</td>
			<td align="center" width=90>重启数/重启状态</br>页码/失败次数</td>
			<td align="center" width=100 style="word-break: break-all">所在城市</br>终端状态</td>
			<td align="center" width=50>剩余空间</td>
			<td align="center" width=80>最新工作时间</br>重启时间</td>
			<td align="center" width=80>重启排序时间</br>发通知时间</td>
			<td align="center" width=50>成功次数</br>操作次数</td>
			<td align="center" width=40>状态</td>
			<td align="center" width=40>失败原因</td>
			<td align="center" width=50>服务器ID</td>
			<td align="center" width=90>操作</td>
			<div id="div1"></div>
			<div id="div2"></div>
			<div id="div3"></div>
		</tr>
		<%
			String trClass = "";
			String webUrl = "";
			String keywordColor = "";
			boolean validClient = true;
			for (int i = 0; i < itemList.size(); i++) {
				ClientStatusVO value = (ClientStatusVO) itemList.get(i);
				trClass = "";
				validClient = true;
				keywordColor = "";
				if (value.isValid()) {
					if(value.isRed()){
						keywordColor = "#FF0000";
						validClient = false;
					}else if(value.isYellow()){
						keywordColor = "#ef00ff";
						validClient = false;
					}else{
						keywordColor = "green";
					}
				} else {
					keywordColor = "green";
				}
				if ((i % 2) != 0) {
					trClass = "bgcolor='#eeeeee'";
				}
		%>
		<tr <%=trClass%> onmouseover="doOver(this);" onmouseout="doOut(this);">
			<td><input type="checkbox" name="clientID" value="<%=value.getClientID()%>"/></td>
			<td>
				<font color="<%=keywordColor%>"><%=value.getClientID()%></font>
				<%if(!validClient){%>
					<span name="invalidClient" id="span_<%=value.getClientID()%>"></span>
				<%}%>
			</td>
			<td><input type="text" value="<%=value.getGroup() == null ? "" : value.getGroup()%>"
				name="group" id="<%=value.getClientID()%>"  onBlur="updateGroup(this)"
				style="width: 100%; height: 100%" /></td>
			<td>
				<select name="operationType<%=value.getClientID()%>" id="operationType<%=value.getClientID()%>" onChange="updateOperationType(this)"
				 style="width: 100%; height: 100%" />
				<%
					String[] operationTypes = {"",
							"pc_pm","pc_pm2","pc_pm3","pc_xg","pc_xg2","pc_xg3","pc_xl","pc_pm_sogou","pc_pm_360","pc_pm_58",
							"pc_pm_zhidao", "pc_pm_wenku", "pc_tieba"};
					if(TerminalTypeEnum.Phone.name().equals(terminalType)){
						operationTypes = new String[]{"", "m_pm", "m_xl", "m_xg", "m_pm_sm", "m_xl2"};
					}
					for (int operationTypeIndex = 0; operationTypeIndex < operationTypes.length; operationTypeIndex++) {
						if (operationTypes[operationTypeIndex].equals(value.getOperationType())) {
							out.println("<option selected value='" + operationTypes[operationTypeIndex]
									+ "'>" + operationTypes[operationTypeIndex] + "</option>");
						} else {
							out.println("<option value='" + operationTypes[operationTypeIndex] + "'>"
									+ operationTypes[operationTypeIndex] + "</option>");
						}
					}
				%>	
				</select>
			</td>
			<td><font color="<%=keywordColor%>"><%=Utils.formatDatetime(value.getRenewalDate(), "MM-dd")%></font></td>
			<td><font color="<%=keywordColor%>"><%=value.getVersion() == null ? "" : value.getVersion()%></br><%=value.getTargetVersion() == null ? "" : value.getTargetVersion()%></font></td>
			<td><font color="<%=keywordColor%>"><%=value.getRestartCount()%>/<%=value.getRestartStatus() == null ? "" : value.getRestartStatus()
			%></br><%=value.getPageNo()%>/<%=value.getContinuousFailCount()%></font></td>
			<td  style="word-break: break-all"><font color="<%=keywordColor%>"><%=value.getCity() == null ? "" : value.getCity()%></br><%=value.getStatus() == null ? "" :
					value.getStatus()%></font></td>
			<td><font color="<%=keywordColor%>"><%=value.getFreeSpace()%></font></td>
			<td><font color="<%=keywordColor%>"><%=Utils.formatDatetime(value.getLastVisitTime(),
						"MM-dd HH:mm")%></br><%=Utils.formatDatetime(
					value.getRestartTime(), "MM-dd HH:mm")%></font></td>
			<td><font color="<%=keywordColor%>"><%=Utils.formatDatetime(
						value.getRestartOrderingTime(), "MM-dd HH:mm")%></br><%=Utils.formatDatetime(
					value.getLastSendNotificationTime(), "MM-dd HH:mm")%></font></td>
			<td><font color="<%=keywordColor%>"><%=value.getOptimizationSucceedCount()%></br><%=value.getOptimizationTotalCount()%></font></td>
			<td><font color="<%=keywordColor%>"><%=value.isValid() ? "监控中" : "暂停监控"%></font></td>
			<td><input type="text" value="<%=value.getUpgradeFailedReason() == null ? "" : value.getUpgradeFailedReason()%>"
					   name="upgradeFailedReason" id="<%=value.getClientID()%>"  onBlur="updateUpgradeFailedReason(this)"
					   style="width: 100%; height: 100%" /></td>
			<td><font color="<%=keywordColor%>"><%=value.getVpsBackendSystemComputerID()%></font></td>
			<td>	
				<%if(!Utils.isNullOrEmpty(value.getHost())){ %>	
				<a href="javascript:connectVNC('<%=value.getClientID()%>')">VNC</a>
				<%}else{%>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<%}%>
				&nbsp;		
				<a href="javascript:showSettingDialog('<%=value.getClientID()%>', this)">设置</a>
				&nbsp;
				<a href="javascript:delItem('<%=value.getClientID()%>')">删除</a>
				</br>
				<%
				if (value.isValid()) {
				%> <a href="javascript:stopMonitor('<%=value.getClientID()%>')">暂停监控</a> <%
 				} else {
 %>
				<a href="javascript:startMonitor('<%=value.getClientID()%>')">开始监控</a>
				<%
				}
				%>
				&nbsp;
				<a href="javascript:changeTerminalType('<%=value.getClientID()%>')">变更终端类型</a>
				
			</td>
		</tr>
		<%
			}
		%>

		<tr>
			<td colspan=16 align="right"><br>
				<%=pageInfo%></td>
		</tr>
	</table>
	</form>
	<br>
	<br>

	<script language="javascript">
		function selectAll(self){
			var a = document.getElementsByName("clientID");
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
		function delItem(clientID)
		{
		   if (confirm("确实要删除这台终端吗?") == false) return;
		   document.location = "delete.jsp?clientID=" + clientID;
		}
		function getSelectedClientIDs(){
			var a = document.getElementsByName("clientID");
			var clientIDs = '';
			for(var i = 0;i<a.length;i++){
				//alert(a[i].value);
				if(a[i].checked){
					if(clientIDs === ''){
						clientIDs = "'" + a[i].value + "'";
					}else{
						clientIDs = clientIDs + ",'" + a[i].value + "'";	
					}
				}
			}
			return clientIDs;
		}
		function delAllItems()
		{
		   if (confirm("确实要删除选中的终端吗?") == false) return;
		   var clientIDs = getSelectedClientIDs();
		   
		   $$$.ajax({
		        url: '/client/deleteclientstatuses.jsp?clientIDs=' + clientIDs ,
		        type: 'Get',
		        success: function (data) {
		        	data = data.replace(/\r\n/gm,"");
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
		function resetRestartStatus()
		{
			if (confirm("确实要重设状态为Processing或者Logging终端的重启状态吗?") == false) return;
			$$$.ajax({
				url: '/client/resetRestartStatus.jsp' ,
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
		function stopMonitor(clientID) {
			if (confirm("确实要暂停监控这台终端吗?") == false)
				return;
			document.location = "stopMonitor.jsp?clientID=" + clientID;
		}
		function startMonitor(clientID) {
			if (confirm("确实要开始监控这台终端吗?") == false)
				return;
			document.location = "startMonitor.jsp?clientID=" + clientID;
		}
		function updateGroup(self){
			var clientID = self.id;
			var groupName = self.value.trim();
		    $$$.ajax({
		        url: '/client/updateGroup.jsp',
		        data: "data=" + JSON.stringify(clientID + '=' + groupName),
		        type: 'POST',
		        success: function (data) {
		        	data = data.replace(/\r\n/gm,"");
		        	if(data === "1"){
		        		showInfo("更新成功！", self);
		        	}else{
		        		showInfo("更新失败！", self);
		        	}
		        },
		        error: function () {
		        	showInfo("更新失败！", self);
		        }
		    });
		}
		function changeTerminalType(clientID){
			var postData = {};
			postData.clientID = clientID;
			$$$.ajax({
				url: '/spring/clientStatus/changeTerminalType',
				data: JSON.stringify(postData),
				headers: {
					'Accept': 'application/json',
					'Content-Type': 'application/json'
				},
				timeout: 5000,
				type: 'POST',
				success: function (status) {
					if(status){
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

		function updateUpgradeFailedReason(self){
			var clientID = self.id;
			var upgradeFailedReason = self.value.trim();
			$$$.ajax({
				url: '/client/updateUpgradeFailedReason.jsp',
				data: "data=" + JSON.stringify(clientID + '=' + upgradeFailedReason),
				type: 'POST',
				success: function (data) {
					data = data.replace(/\r\n/gm,"");
					if(data === "1"){
						showInfo("更新成功！", self);
					}else{
						showInfo("更新失败！", self);
					}
				},
				error: function () {
					showInfo("更新失败！", self);
				}
			});
		}
		function updateOperationType(self){
			var clientID = self.id.replace("operationType", "");
			var operationType = self.value.trim();
		    $$$.ajax({
		        url: '/client/updateOperationType.jsp',
		        data: "data=" + JSON.stringify(clientID + '=' + operationType),
		        type: 'POST',
		        success: function (data) {
		        	data = data.replace(/\r\n/gm,"");
		        	if(data === "1"){
		        		showInfo("更新成功！", self);
		        	}else{
		        		showInfo("更新失败！", self);
		        	}
		        },
		        error: function () {
		        	showInfo("更新失败！", self);
		        }
		    });
		}
		function showSettingDialog(clientID, self){
		    $$$.ajax({
		        url: '/client/getclientstatus.jsp?username=<%=username%>&password=<%=password%>&clientID=' + clientID,
		        type: 'Get',
		        success: function (data) {
		        	data = data.replace(/\r\n/gm,"");
		        	if(data === "0"){
		        		showInfo("获取信息失败！", self);
		        	}else{
		        		var clientStatusVO = JSON.parse(data);
		        		initSettingDialog(clientStatusVO, self);
		        	}
		        },
		        error: function () {
		        	showInfo("获取信息失败！", self);
		        }
		    });
		}
		function initSettingDialog(clientStatusVO, self){
			var settingDialogDiv = $$$("#changeSettingDialog");
			settingDialogDiv.find("#settingClientID").val(clientStatusVO.clientID);
			settingDialogDiv.find("#settingGroup").val(clientStatusVO.group != null ? clientStatusVO.group : "");
			settingDialogDiv.find("#settingOperationType").val(clientStatusVO.operationType != null ? clientStatusVO.operationType : "");
			if(clientStatusVO.pageSize != null){
				settingDialogDiv.find("#pageSize").val(clientStatusVO.pageSize);
			}
			if(clientStatusVO.page != null){
				settingDialogDiv.find("#page").val(clientStatusVO.page);
			}
			if(clientStatusVO.zhanneiPercent != null){
				settingDialogDiv.find("#zhanneiPercent").val(clientStatusVO.zhanneiPercent);
			}
			if(clientStatusVO.dragPercent  != null){
				settingDialogDiv.find("#dragPercent ").val(clientStatusVO.dragPercent );
			}
			if(clientStatusVO.kuaizhaoPercent != null){
				settingDialogDiv.find("#kuaizhaoPercent").val(clientStatusVO.kuaizhaoPercent);
			}
			if(clientStatusVO.baiduSemPercent != null){
				settingDialogDiv.find("#baiduSemPercent").val(clientStatusVO.baiduSemPercent);
			}
			if(clientStatusVO.multiBrowser != null){
				settingDialogDiv.find("#multiBrowser").val(clientStatusVO.multiBrowser);
			}
			if(clientStatusVO.clearCookie != null){
				settingDialogDiv.find("#clearCookie").val(clientStatusVO.clearCookie);
			}
			settingDialogDiv.find("#allowSwitchGroup ").val(clientStatusVO.allowSwitchGroup );
			settingDialogDiv.find("#disableStatistics ").val(clientStatusVO.disableStatistics );

			settingDialogDiv.find("#entryPageMinCount").val(clientStatusVO.entryPageMinCount);
			settingDialogDiv.find("#entryPageMaxCount").val(clientStatusVO.entryPageMaxCount);
			settingDialogDiv.find("#pageRemainMinTime").val(clientStatusVO.pageRemainMinTime);
			settingDialogDiv.find("#pageRemainMaxTime").val(clientStatusVO.pageRemainMaxTime);
			settingDialogDiv.find("#inputDelayMinTime").val(clientStatusVO.inputDelayMinTime);
			settingDialogDiv.find("#inputDelayMaxTime").val(clientStatusVO.inputDelayMaxTime);
			settingDialogDiv.find("#slideDelayMinTime").val(clientStatusVO.slideDelayMinTime);
			settingDialogDiv.find("#slideDelayMaxTime").val(clientStatusVO.slideDelayMaxTime);
			settingDialogDiv.find("#titleRemainMinTime").val(clientStatusVO.titleRemainMinTime);
			settingDialogDiv.find("#titleRemainMaxTime").val(clientStatusVO.titleRemainMaxTime);
			settingDialogDiv.find("#waitTimeAfterOpenBaidu").val(clientStatusVO.waitTimeAfterOpenBaidu);
			settingDialogDiv.find("#waitTimeBeforeClick").val(clientStatusVO.waitTimeBeforeClick);
			settingDialogDiv.find("#waitTimeAfterClick").val(clientStatusVO.waitTimeAfterClick);
			settingDialogDiv.find("#maxUserCount").val(clientStatusVO.maxUserCount);
			settingDialogDiv.find("#optimizeKeywordCountPerIP").val(clientStatusVO.optimizeKeywordCountPerIP);

			settingDialogDiv.find("#oneIPOneUser")[0].checked = (clientStatusVO.oneIPOneUser == 1) ? true : false;
			settingDialogDiv.find("#randomlyClickNoResult")[0].checked = (clientStatusVO.randomlyClickNoResult == 1) ? true : false;
			settingDialogDiv.find("#justVisitSelfPage")[0].checked = (clientStatusVO.justVisitSelfPage == 1) ? true : false;
			settingDialogDiv.find("#sleepPer2Words")[0].checked = (clientStatusVO.sleepPer2Words == 1) ? true : false;
			settingDialogDiv.find("#supportPaste")[0].checked = (clientStatusVO.supportPaste == 1) ? true : false;
			settingDialogDiv.find("#moveRandomly")[0].checked = (clientStatusVO.moveRandomly == 1) ? true : false;
			settingDialogDiv.find("#parentSearchEntry")[0].checked = (clientStatusVO.parentSearchEntry == 1) ? true : false;
			settingDialogDiv.find("#clearLocalStorage")[0].checked = (clientStatusVO.clearLocalStorage == 1) ? true : false;
			settingDialogDiv.find("#lessClickAtNight")[0].checked = (clientStatusVO.lessClickAtNight == 1) ? true : false;
			settingDialogDiv.find("#sameCityUser")[0].checked = (clientStatusVO.sameCityUser == 1) ? true : false;
			settingDialogDiv.find("#locateTitlePosition")[0].checked = (clientStatusVO.locateTitlePosition == 1) ? true : false;
			settingDialogDiv.find("#baiduAllianceEntry")[0].checked = (clientStatusVO.baiduAllianceEntry == 1) ? true : false;
			settingDialogDiv.find("#justClickSpecifiedTitle")[0].checked = (clientStatusVO.justClickSpecifiedTitle == 1) ? true : false;
			settingDialogDiv.find("#randomlyClickMoreLink")[0].checked = (clientStatusVO.randomlyClickMoreLink == 1) ? true : false;
			settingDialogDiv.find("#moveUp20")[0].checked = (clientStatusVO.moveUp20 == 1) ? true : false;

			settingDialogDiv.find("#waitTimeAfterOpenBaidu").val(clientStatusVO.waitTimeAfterOpenBaidu);
			settingDialogDiv.find("#waitTimeBeforeClick").val(clientStatusVO.waitTimeBeforeClick);
			settingDialogDiv.find("#waitTimeAfterClick").val(clientStatusVO.waitTimeAfterClick);
			settingDialogDiv.find("#maxUserCount").val(clientStatusVO.maxUserCount);

			settingDialogDiv.find("#host").val(clientStatusVO.host != null ? clientStatusVO.host : "");
			settingDialogDiv.find("#port").val(clientStatusVO.port != null ? clientStatusVO.port : "");
			settingDialogDiv.find("#userName").val(clientStatusVO.userName != null ? clientStatusVO.userName : "Administrator");
			settingDialogDiv.find("#password").val(clientStatusVO.password != null ? clientStatusVO.password : "doshows123");
			settingDialogDiv.find("#vpsBackendSystemComputerID").val(clientStatusVO.vpsBackendSystemComputerID != null ? clientStatusVO.vpsBackendSystemComputerID :
					"");
			settingDialogDiv.find("#vpsBackendSystemPassword").val(clientStatusVO.vpsBackendSystemPassword != null ? clientStatusVO.vpsBackendSystemPassword : "doshows123");
			settingDialogDiv[0].style.left = getTop(self); //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
			settingDialogDiv.show();
		}
		function saveChangeSetting(self){
			var settingDialogDiv = $$$("#changeSettingDialog");
			var clientStatusVO = {};
			clientStatusVO.clientID = settingDialogDiv.find("#settingClientID").val();
			clientStatusVO.group = settingDialogDiv.find("#settingGroup").val();
			clientStatusVO.operationType = settingDialogDiv.find("#settingOperationType").val();
			clientStatusVO.pageSize = settingDialogDiv.find("#pageSize").val();
			clientStatusVO.page = settingDialogDiv.find("#page").val();
			clientStatusVO.dragPercent  = settingDialogDiv.find("#dragPercent ").val();
			clientStatusVO.zhanneiPercent = settingDialogDiv.find("#zhanneiPercent").val();
			clientStatusVO.kuaizhaoPercent = settingDialogDiv.find("#kuaizhaoPercent").val();
			clientStatusVO.baiduSemPercent = settingDialogDiv.find("#baiduSemPercent").val();
			clientStatusVO.multiBrowser = settingDialogDiv.find("#multiBrowser").val();
			clientStatusVO.clearCookie = settingDialogDiv.find("#clearCookie").val();
			clientStatusVO.allowSwitchGroup = settingDialogDiv.find("#allowSwitchGroup").val();
			clientStatusVO.disableStatistics = settingDialogDiv.find("#disableStatistics").val();
			clientStatusVO.host = settingDialogDiv.find("#host").val();
			clientStatusVO.port = settingDialogDiv.find("#port").val();
			clientStatusVO.userName = settingDialogDiv.find("#userName").val();
			clientStatusVO.password = settingDialogDiv.find("#password").val();
			clientStatusVO.vpsBackendSystemComputerID = settingDialogDiv.find("#vpsBackendSystemComputerID").val();
			clientStatusVO.vpsBackendSystemPassword = settingDialogDiv.find("#vpsBackendSystemPassword").val();

			clientStatusVO.entryPageMinCount = settingDialogDiv.find("#entryPageMinCount").val();
			clientStatusVO.entryPageMaxCount = settingDialogDiv.find("#entryPageMaxCount").val();
			clientStatusVO.disableVisitWebsite = settingDialogDiv.find("#disableVisitWebsite").val();
			clientStatusVO.pageRemainMinTime = settingDialogDiv.find("#pageRemainMinTime").val();
			clientStatusVO.pageRemainMaxTime = settingDialogDiv.find("#pageRemainMaxTime").val();
			clientStatusVO.inputDelayMinTime = settingDialogDiv.find("#inputDelayMinTime").val();
			clientStatusVO.inputDelayMaxTime = settingDialogDiv.find("#inputDelayMaxTime").val();
			clientStatusVO.slideDelayMinTime = settingDialogDiv.find("#slideDelayMinTime").val();
			clientStatusVO.slideDelayMaxTime = settingDialogDiv.find("#slideDelayMaxTime").val();
			clientStatusVO.titleRemainMinTime = settingDialogDiv.find("#titleRemainMinTime").val();
			clientStatusVO.titleRemainMaxTime = settingDialogDiv.find("#titleRemainMaxTime").val();
			clientStatusVO.waitTimeAfterOpenBaidu = settingDialogDiv.find("#waitTimeAfterOpenBaidu").val();
			clientStatusVO.waitTimeBeforeClick = settingDialogDiv.find("#waitTimeBeforeClick").val();
			clientStatusVO.waitTimeAfterClick = settingDialogDiv.find("#waitTimeAfterClick").val();
			clientStatusVO.maxUserCount = settingDialogDiv.find("#maxUserCount").val();
			clientStatusVO.optimizeKeywordCountPerIP = settingDialogDiv.find("#optimizeKeywordCountPerIP").val();

//			clientStatusVO.disableVisitWebsite = settingDialogDiv.find("#disableVisitWebsite:checked").val() === '1' ? 1 : 0;
			clientStatusVO.oneIPOneUser = settingDialogDiv.find("#oneIPOneUser:checked").val() === '1' ? 1 : 0;
			clientStatusVO.randomlyClickNoResult = settingDialogDiv.find("#randomlyClickNoResult:checked").val() === '1' ? 1 : 0;
			clientStatusVO.justVisitSelfPage = settingDialogDiv.find("#justVisitSelfPage:checked").val() === '1' ? 1 : 0;
			clientStatusVO.sleepPer2Words = settingDialogDiv.find("#sleepPer2Words:checked").val() === '1' ? 1 : 0;
			clientStatusVO.supportPaste = settingDialogDiv.find("#supportPaste:checked").val() === '1' ? 1 : 0;
			clientStatusVO.moveRandomly = settingDialogDiv.find("#moveRandomly:checked").val() === '1' ? 1 : 0;
			clientStatusVO.parentSearchEntry = settingDialogDiv.find("#parentSearchEntry:checked").val() === '1' ? 1 : 0;
			clientStatusVO.clearLocalStorage = settingDialogDiv.find("#clearLocalStorage:checked").val() === '1' ? 1 : 0;
			clientStatusVO.lessClickAtNight = settingDialogDiv.find("#lessClickAtNight:checked").val() === '1' ? 1 : 0;
			clientStatusVO.sameCityUser = settingDialogDiv.find("#sameCityUser:checked").val() === '1' ? 1 : 0;
			clientStatusVO.locateTitlePosition = settingDialogDiv.find("#locateTitlePosition:checked").val() === '1' ? 1 : 0;
			clientStatusVO.baiduAllianceEntry = settingDialogDiv.find("#baiduAllianceEntry:checked").val() === '1' ? 1 : 0;
			clientStatusVO.justClickSpecifiedTitle = settingDialogDiv.find("#justClickSpecifiedTitle:checked").val() === '1' ? 1 : 0;
			clientStatusVO.randomlyClickMoreLink = settingDialogDiv.find("#randomlyClickMoreLink:checked").val() === '1' ? 1 : 0;
			clientStatusVO.moveUp20 = settingDialogDiv.find("#moveUp20:checked").val() === '1' ? 1 : 0;


			$$$.ajax({
		        url: '/client/updateClientStatus.jsp',
		        data: "data=" + JSON.stringify(clientStatusVO),
		        type: 'POST',
		        success: function (data) {
		        	data = data.replace(/\r\n/gm,"");
		        	settingDialogDiv.hide();
		        	if(data === "1"){
		        		showInfo("更新成功！", self);
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
		
		function showTargetVersionSettingDialog(self){
			var settingDialogDiv = $$$("#targetVersionSettingDialog");
			settingDialogDiv.find("#settingTargetVersion").val("");
			settingDialogDiv.show();
		}
		function saveTargetVersionSetting(self){
			var settingDialogDiv = $$$("#targetVersionSettingDialog");
			var clientStatusVO = {};
			clientStatusVO.clientIDs = getSelectedClientIDs();
			clientStatusVO.targetVersion = settingDialogDiv.find("#settingTargetVersion").val();
			if(clientStatusVO.targetVersion.trim() === ''){
				alert("请输入目标版本！");
				return;
			}
			if(clientStatusVO.clientIDs.trim() === ''){
				alert("请选择要更新的终端！");
				return;
			}
		    $$$.ajax({
		        url: '/client/updateClientStatusTargetVersion.jsp',
		        data: "data=" + JSON.stringify(clientStatusVO),
		        type: 'POST',
		        success: function (data) {
		        	data = data.replace(/\r\n/gm,"");
		        	settingDialogDiv.hide();
		        	if(data === "1"){
		        		showInfo("更新成功！", self);
		        		settingDialogDiv.hide();
		        		window.location.reload();
		        	}else{
		        		settingDialogDiv.hide();
		        		showInfo("更新失败！", self);
		        	}
		        },
		        error: function () {
		        	showInfo("更新失败！", self);
		        	settingDialogDiv.hide();
		        }
		    });
		}
		function cancelTargetVersionSetting(){
			var settingDialogDiv = $$$("#targetVersionSettingDialog");
			settingDialogDiv.hide();
		}

		function showRenewalSettingDialog(self){
			var settingDialogDiv = $$$("#renewalSettingDialog");
			settingDialogDiv.find("#renewalSettingDialog").val("");
			settingDialogDiv.show();
		}
		function saveRenewalSetting(self){
			var settingDialogDiv = $$$("#renewalSettingDialog");
			var clientStatusVO = {};
			clientStatusVO.clientIDs = getSelectedClientIDs();
			clientStatusVO.settingType = settingDialogDiv.find("input[name=settingType][checked]").val();
			clientStatusVO.renewalDate = settingDialogDiv.find("#renewalDate").val();
			if(clientStatusVO.settingType.trim() === 'specificDate'){
				if(clientStatusVO.renewalDate.trim() === ''){
					alert("请输入日期！");
					return;
				}
				if(!isDate(clientStatusVO.renewalDate.trim())){
					alert("日期格式不对！");
					return;
				}
			}
			if(clientStatusVO.clientIDs.trim() === ''){
				alert("请选择要更新的终端！");
				return;
			}
			$$$.ajax({
				url: '/client/updateClientStatusRenewalDate.jsp',
				data: "data=" + JSON.stringify(clientStatusVO),
				type: 'POST',
				success: function (data) {
					data = data.replace(/\r\n/gm,"");
					settingDialogDiv.hide();
					if(data === "1"){
						showInfo("更新成功！", self);
						settingDialogDiv.hide();
						window.location.reload();
					}else{
						settingDialogDiv.hide();
						showInfo("更新失败！", self);
					}
				},
				error: function () {
					showInfo("更新失败！", self);
					settingDialogDiv.hide();
				}
			});
		}
		function cancelRenewalSetting(){
			var settingDialogDiv = $$$("#renewalSettingDialog");
			settingDialogDiv.hide();
		}

		function connectVNC(clientID){
			var obj = new ActiveXObject("wscript.shell");
			obj.run("file:///C:/vnc/" + clientID + ".vnc"); 
			obj = null;
		}
		function checkinput() {
			var a = document.getElementsByName("group");
			var keyValues = '';
			for (var i = 0; i < a.length; i++) {
				if (a[i].value === '') {
					a[i].focus();
					alert('请输入分组名称！');
					return;
				}
				if (keyValues === '') {
					keyValues = a[i].id + '=' + a[i].value;
				} else {
					keyValues = keyValues + '--row--' + a[i].id + '=' + a[i].value;
				}
			}
			var data = document.getElementsByName("data");
			data[0].value = keyValues;
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
			if($$$("#showFetchKeywordStatus").attr("checked") === "checked"){
				$$$("span[name=invalidClient]").each(function(){
					var span = $$$(this);
					$$$.ajax({
						url: '/customerkeyword/getcustomerkeyword.jsp?username=<%=username%>&password=<%=password%>&clientID=' + this.id.replace("span_", ""),
						type: 'Get',
						success: function (data) {
							data = data.replace(/\r\n/gm,"");
							if(data.indexOf("keyword") > 0){
								span.html("</br>取词正常");
								span.css("color", "green");
							}else{
								span.html("</br>取不到词" + data);
								span.css("color", "red");
							}
						},
						error: function () {
							span.html("</br>取词异常");
							span.css("color", "red");
						}
					});
				});
			}
	    });
	</script>


	<div style="display: none;">
		<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660"
			language="JavaScript"></script>

	</div>
	<div id="changeSettingDialog">
		<table>
			<tr>
				<td>
					<table id="td_1" style="font-size:12px">
						<tr>
							<th>分组</th>
							<td>
								<input type="hidden" id="settingClientID" />
								<input type="text" name="settingGroup" id="settingGroup" />
							</td>
						</tr>
						<tr>
							<th>允许换组</th>
							<td>
								<select name="allowSwitchGroup" id="allowSwitchGroup">
									<option value="0">不允许</option>
									<option value="1">允许</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>关闭统计</th>
							<td>
								<select name="disableStatistics" id="disableStatistics">
									<option value="0">开放</option>
									<option value="1">关闭</option>
								</select>
							</td>
						</tr>

						<tr>
							<th>操作类型</th>
							<td>
								<select name="settingOperationType" id="settingOperationType">
									<%if(TerminalTypeEnum.PC.name().equals(terminalType)){%>
									<option value="pc_pm">pc_pm</option>
									<option value="pc_pm2">pc_pm2</option>
									<option value="pc_pm3">pc_pm3</option>
									<option value="pc_xg">pc_xg</option>
									<option value="pc_xg2">pc_xg2</option>
									<option value="pc_xg3">pc_xg3</option>
									<option value="pc_xl">pc_xl</option>
									<option value="pc_pm_sogou">pc_pm_sogou</option>
									<option value="pc_pm_360">pc_pm_360</option>
									<option value="pc_pm_58">pc_pm_58</option>
									<option value="pc_pm_zhidao">pc_pm_zhidao</option>
									<option value="pc_pm_wenku">pc_pm_wenku</option>
									<option value="pc_tieba">pc_tieba</option>
									<%}else{%>
									<option value="m_pm">m_pm</option>
									<option value="m_xl">m_xl</option>
									<option value="m_xg">m_xg</option>
									<option value="m_pm_sm">m_pm_sm</option>
									<option value="m_xl2">m_xl2</option>
									<%}%>
								</select>
							</td>
						</tr>
						<tr>
							<th>页数</th>
							<td>
								<%if(TerminalTypeEnum.Phone.name().equals(terminalType)){%>
									<input type="text" name="page" id="page" value="3"/>
								<%}else{%>
									<input type="text" name="page" id="page" value="5"/>
								<%}%>
							</td>
						</tr>
						<tr>
							<th>每页</th>
							<td>
								<select name="pageSize" id="pageSize">
									<option value="0">10</option>
									<option value="1">20</option>
									<option value="2">50</option>
								</select>条
							</td>
						</tr>
						<tr>
							<th>站内搜索</th>
							<td>
								<select name="zhanneiPercent" id="zhanneiPercent">
									<option value="0">0%</option>
									<option value="1">10%</option>
									<option value="2">30%</option>
									<option value="3">50%</option>
									<option value="4">100%</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>快照点击比例</th>
							<td>
								<select name="kuaizhaoPercent" id="kuaizhaoPercent">
									<option value="0">0%</option>
									<option value="1">10%</option>
									<option value="2">30%</option>
									<option value="3">50%</option>
									<option value="4">100%</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>竞价点击比例</th>
							<td>
								<select name="baiduSemPercent" id="baiduSemPercent">
									<option value="0">0%</option>
									<option value="1">10%</option>
									<option value="2">30%</option>
									<option value="3">50%</option>
									<option value="4">100%</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>拖动标题的点击比例</th>
							<td>
								<select name="dragPercent" id="dragPercent">
									<option value="0">0%</option>
									<option value="1">10%</option>
									<option value="2">30%</option>
									<option value="3">50%</option>
									<option value="4">100%</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>浏览器设置</th>
							<td>
								<select name="multiBrowser" id="multiBrowser">
									<option value="0">命令多浏览器</option>
									<option value="1"  selected>模拟多浏览器</option>
									<option value="2">单个浏览器</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>Cookie设置</th>
							<td>
								<select name="clearCookie" id="clearCookie">
									<option value="0">不清理Cookie</option>
									<option value="1">每次都清理Cookie</option>
									<option value="2">随机清理Cookie</option>
									<option value="3">N次操作清理Cookie</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>主机</th>
							<td>
								<input type="text" name="host" id="host"/>
							</td>
						</tr>
						<tr>
							<th>端口</th>
							<td>
								<input type="text" name="port" id="port"/>
							</td>
						</tr>
						<tr>
							<th>VNC和操作系统用户名</th>
							<td>
								<input type="text" name="userName" id="userName" value="Administrator"/>
							</td>
						</tr>
						<tr>
							<th>VNC和操作系统密码</th>
							<td>
								<input type="text" name="password" id="password" value="doshows123"/>
							</td>
						</tr>
						<tr>
							<th>VPS后台系统电脑ID</th>
							<td>
								<input type="text" name="vpsBackendSystemComputerID" id="vpsBackendSystemComputerID"/>
							</td>
						</tr>
						<tr>
							<th>VPS后台系统密码</th>
							<td>
								<input type="text" name="vpsBackendSystemPassword" id="vpsBackendSystemPassword"/>
							</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
								<input type="button" id="saveChangeSetting" onClick="saveChangeSetting(this)" value="保存"/>&nbsp;&nbsp;&nbsp;<input type="button" onClick="cancelChangeSetting()" id="cancelChangeSetting" value="取消"/>
							</td>
						</tr>
					</table>
				</td>


				<td style="vertical-align:top;">
					<table id="td_2" style="font-size:12px">
						<tr>
							<th>进入页</th>
							<td>
								<input type="text" name="entryPageMinCount" id="entryPageMinCount" value="0" /> -
								<input type="text" name="entryPageMaxCount" id="entryPageMaxCount" value="0" />次
							</td>
						</tr>
						<tr>
							<th>页面停留</th>
							<td>
								<input type="text" name="pageRemainMinTime" id="pageRemainMinTime" value="3000"/> -
								<input type="text" name="pageRemainMaxTime" id="pageRemainMaxTime" value="5000"/>毫秒
							</td>
						</tr>
						<tr>
							<th>输入延时</th>
							<td>
								<input type="text" name="inputDelayMinTime" id="inputDelayMinTime" value="50"/> -
								<input type="text" name="inputDelayMaxTime" id="inputDelayMaxTime" value="80"/>毫秒
							</td>
						</tr>

						<tr>
							<th>滑动延时</th>
							<td>
								<input type="text" name="slideDelayMinTime" id="slideDelayMinTime" value="700"/> -
								<input type="text" name="slideDelayMaxTime" id="slideDelayMaxTime" value="1500"/>毫秒
							</td>
						</tr>
						<tr>
							<th>标题停留</th>
							<td>
								<input type="text" name="titleRemainMinTime" id="titleRemainMinTime" value="1000"/> -
								<input type="text" name="titleRemainMaxTime" id="titleRemainMaxTime" value="3000"/>毫秒
							</td>
						</tr>
						<tr>
							<th>刷多少</th>
							<td>
								<input type="text" name="optimizeKeywordCountPerIP" id="optimizeKeywordCountPerIP" value="1"/>个词换IP
							</td>
						</tr>
						<tr>
							<th>最大用户数</th>
							<td>
								<input type="text" name="maxUserCount" id="maxUserCount" value="300"/>
							</td>
						</tr>
						<tr>
							<th>打开百度等待</th>
							<td>
								<input type="text" name="waitTimeAfterOpenBaidu" id="waitTimeAfterOpenBaidu" value="1000"/>秒
							</td>
						</tr>
						<tr>
							<th>点击目标等待</th>
							<td>
								<input type="text" name="waitTimeBeforeClick" id="waitTimeBeforeClick" value="1000"/>秒
							</td>
						</tr>
						<tr>
							<th>点击目标后等待</th>
							<td>
								<input type="text" name="waitTimeAfterClick" id="waitTimeAfterClick" value="5000"/>秒
							</td>
						</tr>

						<tr>
							<td>
								<input id="oneIPOneUser" name="oneIPOneUser" type="checkbox" value="1">每IP对每用户</input>
							</td>
							<td>
								<input id="randomlyClickNoResult" name="randomlyClickNoResult" type="checkbox" value="1">没结果则随机点</input>
							</td>
						</tr>
						<tr>
							<td>
								<input id="justVisitSelfPage" name="justVisitSelfPage" type="checkbox" value="1">在域名下访问</input>
							</td>
							<td>
								<input id="sleepPer2Words" name="sleepPer2Words" type="checkbox" value="1">输入2字稍微停顿</input>
							</td>
						</tr>

						<tr>
							<td>
								<input id="supportPaste" name="supportPaste" type="checkbox" value="1">支持粘贴输入</input>
							</td>
							<td>
								<input id="moveRandomly" name="moveRandomly" type="checkbox" value="1">随机移动</input>
							</td>
						</tr>

						<tr>
							<td>
								<input id="parentSearchEntry" name="parentSearchEntry" type="checkbox" value="1">爸妈搜索入口</input>
							</td>
							<td>
								<input id="clearLocalStorage" name="clearLocalStorage" type="checkbox" value="1">清除LocalStorage</input>
							</td>
						</tr>

						<tr>
							<td>
								<input id="lessClickAtNight" name="lessClickAtNight" type="checkbox" value="1">晚上减少点击</input>
							</td>
							<td>
								<input id="sameCityUser" name="sameCityUser" type="checkbox" value="1">同城用户</input>
							</td>
						</tr>

						<tr>
							<td>
								<input id="locateTitlePosition" name="locateTitlePosition" type="checkbox" value="1">直接获取标题位置</input>
							</td>
							<td>
								<input id="baiduAllianceEntry" name="baiduAllianceEntry" type="checkbox" value="1">百度联盟入口</input>
							</td>
						</tr>

						<tr>
							<td>
								<input id="justClickSpecifiedTitle" name="justClickSpecifiedTitle" type="checkbox" value="1">随机只点指定标题</input>
							</td>
							<td>
								<input id="randomlyClickMoreLink" name="randomlyClickMoreLink" type="checkbox" value="1">随机多点一些链接</input>
							</td>
						</tr>

						<tr>
							<td>
								<input id="moveUp20" name="moveUp20" type="checkbox" value="1">向上偏移20</input>
							</td>
							<td>
							</td>
						</tr>
					</table>
				</td>

			</tr>
		</table>

	</div>
	
	<div id="targetVersionSettingDialog">
		<table style="font-size:12px">
			<tr>
				<th>目标版本</th>
				<td>					
					<input type="text" name="settingTargetVersion" id="settingTargetVersion" />
				</td>
			</tr>	
			<tr>
				<td colspan="2" align="right">
					<input type="button" value="保存" id="saveTargetVersionSetting" onClick="saveTargetVersionSetting(this)"/> &nbsp;&nbsp;&nbsp;<input type="button" onClick="cancelTargetVersionSetting()" id="cancelTargetVersionSetting" value="取消"/>
				</td>
			</tr>	
		</table>
	</div>

	<div id="renewalSettingDialog">
		<table style="font-size:12px">
			<tr>
				<th>类型</th>
				<td>
					<input type="radio" name="settingType" value="increaseOneMonth" checked />增加一个月
					<input type="radio" name="settingType" value="specificDate" />指定日期
				</td>
			</tr>
			<tr>
				<th>日期</th>
				<td>
					<input name="renewalDate" id="renewalDate" class="Wdate" type="text" style="width:160px;"  onClick="WdatePicker()" value="">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right">
					<input type="button" value="保存" id="saveRenewalSetting" onClick="saveRenewalSetting(this)"/> &nbsp;&nbsp;&nbsp;<input
						type="button" onClick="cancelRenewalSetting()" id="cancelRenewalSetting" value="取消"/>
				</td>
			</tr>
		</table>
	</div>
</body>
</html>

