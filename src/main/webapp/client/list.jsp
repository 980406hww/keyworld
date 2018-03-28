<%@ include file="/commons/global.jsp" %>
<html>
<head>
<title>终端监控</title>
<style>
#topDiv {
	position: fixed;
	top: 0px;
	left: 0px;
	background-color: white;
	width: 100%;
}
<!--
#changeSettingDialog {
	display: none;
	margin: 5px 0px 0px 5px;
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

#renewalSettingDialog {
	margin: 5px 0px 0px 5px;
}

#targetVersionSettingDialog{
	margin: 15px 0px 0px 5px;
}

#clientStatusDiv {
	width: 100%;
}

#showClientStatusBottomDiv {
	float: right;
	width: 580px;
}
-->
</style>
<script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@ include file="/menu.jsp"%>
				<form id="searchClientStatusForm" method="post" action="/internal/clientstatus/searchClientStatuses" style="font-size: 12px;margin-top: 35px" cellpadding=3>
					<table style="font-size: 12px;width:100%">
						<tr>
							<td align="left" colspan="2">
								<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
								<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
								<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
								<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
								客户端ID:<input type="text" name="clientID" id="clientID" value="${clientStatusCriteria.clientID}" style="width: 90px;">
								&nbsp;&nbsp;
								优化组:<input type="text" name="groupName" id="groupName" value="${clientStatusCriteria.groupName}" style="width: 120px;">
								&nbsp;&nbsp;
								版本:<input type="text" name="version" id="version" value="${clientStatusCriteria.version}" style="width: 60px;">
								&nbsp;&nbsp;
								城市:<input type="text" name="city" id="city" value="${clientStatusCriteria.city}" style="width: 120px;">
								&nbsp;&nbsp;
								失败原因:<input type="text" name="upgradeFailedReason" id="upgradeFailedReason" value="${clientStatusCriteria.upgradeFailedReason}"
											  style="width: 50px;">
							&nbsp;&nbsp;
							操作类型:<select name="operationType" id="operationType">
								<c:forEach items="${operationTypeValues}" var="operationType">
									<c:choose>
										<c:when test="${operationType eq clientStatusCriteria.operationType}"><option selected value="${operationType}">${operationType}</option></c:when>
										<c:otherwise><option value="${operationType}">${operationType}</option></c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							&nbsp;&nbsp;
							服务器ID:<input type="text" name="vpsBackendSystemComputerID" id="vpsBackendSystemComputerID" value="${clientStatusCriteria.vpsBackendSystemComputerID}" style="width: 80px;">
							&nbsp;&nbsp;
							流转分组:<input type="text" name="switchGroupName" id="switchGroupName" value="${clientStatusCriteria.switchGroupName}" style="width: 100px;">
							开机状态:<input type="text" name="startUpStatus" id="startUpStatus" value="${clientStatusCriteria.startUpStatus}" style="width: 100px;">
							</td>
						</tr>
						<tr>
						<td align="left">
							<input id="hasProblem" name="hasProblem" type="checkbox" value="hasProblem" ${clientStatusCriteria.hasProblem != null ? "checked=true" : ""}>停了</input>
							&nbsp;&nbsp;
							<input id="renewal" name="renewal" type="checkbox" value="renewal" ${clientStatusCriteria.renewal != null ? "checked=true" : ""}>续费</input>
							&nbsp;&nbsp;
							<input id="hasGroup" name="hasGroup" type="checkbox" value="hasGroup" ${clientStatusCriteria.hasGroup != null ? "checked=true" : ""}>有分组</input>
							&nbsp;&nbsp;
							<input id="noGroup" name="noGroup" type="checkbox" value="noGroup" ${clientStatusCriteria.noGroup != null ? "checked=true" : ""}>没分组</input>
							&nbsp;&nbsp;
							<input id="noOperationType" name="noOperationType" type="checkbox" value="noOperationType" ${clientStatusCriteria.noOperationType != null ? "checked=true" : ""}>没操作类型</input>
							&nbsp;&nbsp;
							<input id="noVNC" name="noVNC" type="checkbox" value="noVNC" ${clientStatusCriteria.noVNC != null ? "checked=true" : ""}>没VNC</input>
							&nbsp;&nbsp;
							<input id="noUpgrade" name="noUpgrade" type="checkbox" value="noUpgrade" ${clientStatusCriteria.noUpgrade != null ? "checked=true" : ""}>没升级</input>
							&nbsp;&nbsp;
							<input id="startUpClient" name="startUpClient" type="checkbox" value="startUpClient" ${clientStatusCriteria.startUpClient != null ? "checked=true" : ""}>开机机器</input>
							&nbsp;&nbsp;
							<input id="showFetchKeywordStatus" name="showFetchKeywordStatus" type="checkbox" value="showFetchKeywordStatus"
							${clientStatusCriteria.showFetchKeywordStatus != null ? "checked=true" : ""}>显示取词状态</input>
							&nbsp;&nbsp;
							状态:<select name="valid" id="valid">
							<c:forEach items="${validMap}" var="entry">
								<c:choose>
									<c:when test="${entry.value eq clientStatusCriteria.valid}"><option selected value="${entry.value}">${entry.key}</option></c:when>
									<c:otherwise><option value="${entry.value}">${entry.key}</option></c:otherwise>
								</c:choose>
							</c:forEach>
							</select>
							&nbsp;&nbsp;
							排序:<select name="orderBy" id="orderBy">
							<c:forEach items="${orderByMap}" var="entry">
								<c:choose>
									<c:when test="${entry.key eq clientStatusCriteria.orderBy}"><option selected value="${entry.key}">${entry.value}</option></c:when>
									<c:otherwise><option value="${entry.key}">${entry.value}</option></c:otherwise>
								</c:choose>
							</c:forEach>
							</select>

							<shiro:hasPermission name="/internal/clientstatus/searchClientStatuses">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" onclick="resetPageNumber()" value=" 查询 ">
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/clientstatus/deleteClientStatuses">
								&nbsp;&nbsp;<input type="button" onclick="delAllItems(this)" value=" 删除 ">
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/clientstatus/saveClientStatus">
								&nbsp;&nbsp;<input type="button" onclick="showReopenClientDialog()" value=" 重开机器 ">
								&nbsp;&nbsp;<input type="button" onclick="finishStartUp()" value=" 完成开机 ">
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/clientstatus/uploadVPSFile">
								&nbsp;&nbsp;<input type="button" name="btnFilter" onclick="showUploadVPSDialog('common')" value=" 导入普通终端 ">
								&nbsp;&nbsp;<input type="button" onclick="showUploadVPSDialog('startUp')" value=" 导入开机终端 ">
							</shiro:hasPermission>
						</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
							<shiro:hasPermission name="/internal/clientstatus/updateClientStatusTargetVersion">
								<a target="_blank" href="javascript:showTargetVersionSettingDialog(this)">设定目标版本</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/clientstatus/updateClientStatusRenewalDate">
								|<a target="_blank" href="javascript:showRenewalSettingDialog(this)">续费</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/clientstatus/resetRestartStatusForProcessing">
								|<a target="_blank" href="javascript:resetRestartStatus()">重置重启状态</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/clientstatus/uploadVNCFile">
								|<a target="_blank" href="javascript:showUploadVNCDialog()">上传VNC文件</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/clientstatus/downloadVNCFile">
								|<a target="_blank" href="javascript:downloadVNCFile()">下载VNC连接压缩文件</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/clientstatus/downloadFullVNCFile">
								|<a target="_blank" href="javascript:downloadFullVNCFile()">下载完整版VNC文件</a>
							</shiro:hasPermission>
							</td>
						</tr>
					</table>
				</form>
	<table  width=100% style="font-size: 12px" id="headerTable">
		<tr bgcolor="#eeeeee" height=30>
			<td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
			<td align="center" width=40>客户端ID</td>
			<td align="center" width=60>优化组</td>
			<td align="center" width=60>操作类型</td>
			<td align="center" width=20>续费<br>日期</td>
			<td align="center" width=30>现版本<br>目标版本</td>
			<td align="center" width=40>重启数/重启状态<br>页码/失败次数</td>
			<td align="center" width=100>所在城市<br>终端状态</td>
			<td align="center" width=30>剩余空间</td>
			<td align="center" width=40>最新工作时间<br>重启时间</td>
			<td align="center" width=40>重启排序时间<br>发通知时间</td>
			<td align="center" width=30>成功次数<br>操作次数</td>
			<td align="center" width=50>宽带账号<br>宽带密码</td>
			<td align="center" width=40>开机状态<br>程序类型</td>
			<td align="center" width=20>状态</td>
			<td align="center" width=40>失败原因</td>
			<td align="center" width=40>服务器ID</td>
			<td align="center" width=80>操作</td>
		</tr>
	</table>
</div>
<div id="clientStatusDiv" style="margin-bottom: 30px;">
	<table width=100% style="font-size: 12px" id="clientStatusTable">
		<c:forEach items="${page.records}" var="clientStatus" varStatus="tr">
			<c:set var="isValidClient" value="true"/>
			<c:choose>
				<c:when test="${clientStatus.valid}">
					<c:choose>
						<c:when test="${clientStatus.red}">
							<c:set var="keywordColor" value="#FF0000"/>
							<c:set var="isValidClient" value="false"/>
						</c:when>
						<c:when test="${clientStatus.orange}">
							<c:set var="keywordColor" value="#e86e33"/>
							<c:set var="isValidClient" value="false"/>
						</c:when>
						<c:when test="${clientStatus.yellow}">
							<c:set var="keywordColor" value="#ef00ff"/>
							<c:set var="isValidClient" value="false"/>
						</c:when>
						<c:otherwise>
							<c:set var="keywordColor" value="green"/>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:set var="keywordColor" value="green"/>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${tr.count % 2 != 0}">
					<tr onmouseover="doOver(this);" onmouseout="doOut(this);" style="background-color: #eeeeee;">
				</c:when>
				<c:otherwise>
					<tr onmouseover="doOver(this);" onmouseout="doOut(this);">
				</c:otherwise>
			</c:choose>
			<td align="center" width=10><input type="checkbox" name="clientID" value="${clientStatus.clientID}" onclick="decideSelectAll()"/></td>
			<td width=40>
				<font color="${keywordColor}">${clientStatus.clientID}</font>
				<c:if test="${!isValidClient}">
					<span name="invalidClient" id="span_${clientStatus.clientID}"></span>
				</c:if>
			</td>
			<td width=60>
				<shiro:hasPermission name="/internal/clientstatus/updateGroup">
					<input type="text" value="${clientStatus.group == null ? "" : clientStatus.group}"
						   name="group" id="${clientStatus.clientID}" onBlur="updateGroup(this)" style="width: 100%;"/>
				</shiro:hasPermission>
				<shiro:lacksPermission name="/internal/clientstatus/updateGroup">
					<input type="text" value="${clientStatus.group == null ? "" : clientStatus.group}"
						   name="group" id="${clientStatus.clientID}" disabled style="width: 100%;"/>
				</shiro:lacksPermission>
			</td>
			<td width=60>
				<shiro:hasPermission name="/internal/clientstatus/updateOperationType">
					<select name="operationType${clientStatus.clientID}" id="operationType${clientStatus.clientID}"
							onChange="updateOperationType(this)" style="width: 100%;" />
				</shiro:hasPermission>

				<shiro:lacksPermission name="/internal/clientstatus/updateOperationType">
					<select name="operationType${clientStatus.clientID}" id="operationType${clientStatus.clientID}"
							disabled style="width: 100%;" />
				</shiro:lacksPermission>
				<c:forEach items="${operationTypeValues}" var="operationType">
					<c:choose>
						<c:when test="${operationType eq clientStatus.operationType}">
							<option selected value="${operationType}">${operationType}</option>
						</c:when>
						<c:otherwise>
							<option value="${operationType}">${operationType}</option>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</select>
			</td>
			<td width=20><font color="${keywordColor}"><fmt:formatDate value="${clientStatus.renewalDate}"
															  pattern="MM-dd"/></font></td>
			<td width=30><font
					color="${keywordColor}">${clientStatus.version == null ? "" : clientStatus.version}<br>${clientStatus.targetVersion == null ? "" : clientStatus.targetVersion}</font>
			</td>
			<td width=40><font
					color="${keywordColor}">${clientStatus.restartCount}/${clientStatus.restartStatus == null ? "" : clientStatus.restartStatus}<br>${clientStatus.pageNo}/${clientStatus.continuousFailCount}</font>
			</td>
			<td width=100 style="word-break: break-all;"><font
					color="${keywordColor}">${clientStatus.city == null ? "" : clientStatus.city}<br>${clientStatus.status == null ? "" : clientStatus.status}</font>
			</td>
			<td width=30><font color="${keywordColor}">${clientStatus.freeSpace}</font></td>
			<td width=40><font color="${keywordColor}"><fmt:formatDate value="${clientStatus.lastVisitTime}"
															  pattern="MM-dd HH:mm"/><br><fmt:formatDate
					value="${clientStatus.restartTime}" pattern="MM-dd HH:mm"/></font></td>
			<td width=40><font color="${keywordColor}"><fmt:formatDate value="${clientStatus.restartOrderingTime}"
															  pattern="MM-dd HH:mm"/><br><fmt:formatDate
					value="${clientStatus.lastSendNotificationTime}" pattern="MM-dd HH:mm"/></font></td>
			<td width=30><font
					color="${keywordColor}">${clientStatus.optimizationSucceedCount}<br>${clientStatus.optimizationTotalCount}</font>
			</td>
			<td width=50><font color="${keywordColor}">${clientStatus.broadbandAccount}<br>${clientStatus.broadbandPassword}</font></td>
			<td width=40><font color="${keywordColor}">${clientStatus.startUpStatus}<br>${clientStatus.downloadProgramType}</font></td>
			<td width=20><font color="${keywordColor}">${clientStatus.valid ? "监控中" : "暂停监控"}</font></td>
			<td width=40>
				<shiro:hasPermission name="/internal/clientstatus/updateUpgradeFailedReason">
					<input type="text"
						   value="${clientStatus.upgradeFailedReason == null ? "" : clientStatus.upgradeFailedReason}"
						   name="upgradeFailedReason" id="${clientStatus.clientID}" onBlur="updateUpgradeFailedReason(this)"
						   style="width: 100%;"/>
				</shiro:hasPermission>
				<shiro:lacksPermission name="/internal/clientstatus/updateUpgradeFailedReason">
					<input type="text"
						   value="${clientStatus.upgradeFailedReason == null ? "" : clientStatus.upgradeFailedReason}"
						   name="upgradeFailedReason" id="${clientStatus.clientID}" disabled
						   style="width: 100%;"/>
				</shiro:lacksPermission>
			</td>
			<td width=40><font color="${keywordColor}">${clientStatus.vpsBackendSystemComputerID}</font></td>
			<td width=80>
				<c:choose>
					<c:when test="${null != clientStatus.host and '' != clientStatus.host}">
						<shiro:hasPermission name="/internal/clientstatus/saveClientStatus">
							<a href="javascript:connectVNC('${clientStatus.clientID}')">VNC</a>
						</shiro:hasPermission>
					</c:when>
					<c:otherwise>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:otherwise>
				</c:choose>
				&nbsp;
				<shiro:hasPermission name="/internal/clientstatus/saveClientStatus">
					<a href="javascript:showSettingDialog('${clientStatus.clientID}', this)">设置</a>
				</shiro:hasPermission>
				&nbsp;
				<shiro:hasPermission name="/internal/clientstatus/deleteClientStatus">
					<a href="javascript:delItem('${clientStatus.clientID}')">删除</a>
				</shiro:hasPermission>
				<br>
				<shiro:hasPermission name="/internal/clientstatus/changeStatus">
				<c:choose>
					<c:when test="${clientStatus.valid}">
						<a href="javascript:changeStatus('${clientStatus.clientID}',false)">暂停监控</a>
					</c:when>
					<c:otherwise>
						<a href="javascript:changeStatus('${clientStatus.clientID}',true)">开始监控</a>
					</c:otherwise>
				</c:choose>
				</shiro:hasPermission>
				&nbsp;
				<shiro:hasPermission name="/internal/clientstatus/changeTerminalType">
				<a href="javascript:changeTerminalType('${clientStatus.clientID}')">变更终端类型</a>
				</shiro:hasPermission>
			</td>
			</tr>
		</c:forEach>
	</table>
</div>
<div id="showCustomerBottomPositioneDiv">
	<div id="showCustomerBottomDiv">
		<input id="fisrtButton" class="ui-button ui-widget ui-corner-all" type="button"
			   onclick="changePaging(1,'${page.size}')" value="首页"/>&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="upButton" type="button" class="ui-button ui-widget ui-corner-all"
			   onclick="changePaging('${page.current-1}','${page.size}')" value="上一页"/>&nbsp;&nbsp;&nbsp;&nbsp;
		${page.current}/${page.pages}&nbsp;&nbsp;
		<input id="nextButton" type="button" class="ui-button ui-widget ui-corner-all"
			   onclick="changePaging('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')"
			   value="下一页">&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="lastButton" type="button" class="ui-button ui-widget ui-corner-all"
			   onclick="changePaging('${page.pages}','${page.size}')" value="末页">&nbsp;&nbsp;&nbsp;&nbsp;
		总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;
		每页显示条数:<select id="chooseRecords" onchange="changePaging(${page.current},this.value)">
		<option>10</option>
		<option>25</option>
		<option>50</option>
		<option>75</option>
		<option>100</option>
	</select>
	</div>
</div>

	<div style="display: none;">
		<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>
	</div>
	<div id="changeSettingDialog" class="easyui-dialog" style="left: 30%;">
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
							<th>网站统计</th>
							<td>
								<select name="disableStatistics" id="disableStatistics">
									<option value="0">开放</option>
									<option value="1">关闭</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>目标网站</th>
							<td>
								<select name="disableVisitWebsite" id="disableVisitWebsite">
									<option value="0">访问</option>
									<option value="1">不访问</option>
								</select>
							</td>
						</tr>
						<tr>
							<th>操作类型</th>
							<td>
								<select name="settingOperationType" id="settingOperationType">
								<c:forEach items="${operationTypeValues}" var="operationType">
										<option value="${operationType}">${operationType}</option>
								</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<th>页数</th>
							<td>
								<c:choose>
									<c:when test="${terminalType eq 'Phone'}">
										<input type="text" name="page" id="page" value="3"/>
									</c:when>
									<c:otherwise>
										<input type="text" name="page" id="page" value="5"/>
									</c:otherwise>
								</c:choose>
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
							<th>外链检索</th>
							<td>
								<select name="zhanwaiPercent" id="zhanwaiPercent">
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
					</table>
				</td>


				<td>
					<table id="td_2" style="font-size:12px">
						<tr>
							<th>流转分组</th>
							<td>
								<input type="text" name="switchGroupName" id="switchGroupName"  style="width:110px;"/>
							</td>
						</tr>
						<tr>
							<th>主机</th>
							<td>
								<input type="text" name="host" id="host"  style="width:110px;"/>
							</td>
						</tr>
						<tr>
							<th>端口</th>
							<td>
								<input type="text" name="port" id="port"  style="width:110px;"/>
							</td>
						</tr>
						<tr>
							<th>VNC和操作系统用户名</th>
							<td>
								<input type="text" name="csUserName" id="csUserName" value="Administrator"  style="width:110px;"/>
							</td>
						</tr>
						<tr>
							<th>VNC和操作系统密码</th>
							<td>
								<input type="text" name="password" id="password" value="doshows123"  style="width:110px;"/>
							</td>
						</tr>
						<tr>
							<th>VPS后台系统电脑ID</th>
							<td>
								<input type="text" name="vpsBackendSystemComputerID" id="vpsBackendSystemComputerID"  style="width:110px;"/>
							</td>
						</tr>
						<tr>
							<th>VPS后台系统密码</th>
							<td>
								<input type="text" name="vpsBackendSystemPassword" id="vpsBackendSystemPassword"  style="width:110px;"/>
							</td>
						</tr>
						<tr>
							<th>最大用户数</th>
							<td>
								<input type="text" name="maxUserCount" id="maxUserCount" value="300"  style="width:110px;"/>
							</td>
						</tr>
						<tr>
							<th>宽带账号</th>
							<td>
								<input type="text" name="broadbandAccount" id="broadbandAccount" value="" style="width:110px;"/>
							</td>
						</tr>
						<tr>
							<th>宽带密码</th>
							<td>
								<input type="text" name="broadbandPassword" id="broadbandPassword" value="" style="width:110px;"/>
							</td>
						</tr>
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
					</table>
				</td>

				<td style="vertical-align:top;">
					<table id="td_2" style="font-size:12px">
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
								<input id="optimizeRelatedKeyword" name="optimizeRelatedKeyword" type="checkbox" value="1">操作相关词</input>
							</td>
						</tr>
					</table>
				</td>

			</tr>
		</table>

	</div>
	
	<div id="targetVersionSettingDialog" class="easyui-dialog" style="left: 40%;">
		目标版本：<input type="text" name="settingTargetVersion" id="settingTargetVersion" />
	</div>

	<div id="renewalSettingDialog" class="easyui-dialog" style="left: 40%;">
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
		</table>
	</div>

	<div id="uploadVNCDialog" class="easyui-dialog" style="left: 40%;">
		<form method="post" id="uploadVNCForm" action="" enctype="multipart/form-data">
			<table width="95%" style="margin-top: 10px;margin-left: 10px">
				<tr>
					<td></td>
				</tr>
				<tr>
					<td></td>
				</tr>
				<tr>
					<td align="right">
						<table width="100%" style="font-size:14px;">
							<tr>
								<td>
									<input type="file" id="file" name="file" size=50 height="50px" style="width: 260px;">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="uploadVPSDialog" class="easyui-dialog" style="left: 40%;">
		<form method="post" id="uploadVPSForm" action="" enctype="multipart/form-data">
			<table width="95%" style="margin-top: 10px;margin-left: 10px">
				<tr>
					<td id="programType" style="display: none;">
						下载程序:
						<input type="radio" name="downloadProgramType" value="New" checked /> 新程序
						<input type="radio" name="downloadProgramType" value="Old" /> 旧程序
					</td>
				</tr>
				<tr>
					<td align="right">
						<table width="100%" style="font-size:14px;">
							<tr>
								<td>
									<input type="file" id="file" name="file" size=50 height="50px" style="width: 260px;">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="reopenClientDiv" class="easyui-dialog">
		<table width="95%" style="margin-top: 10px;margin-left: 10px">
			<tr>
				<td>
					下载程序:
					<input type="radio" name="downloadProgramType" value="New" checked /> 新程序
					<input type="radio" name="downloadProgramType" value="Old" /> 旧程序
				</td>
			</tr>
		</table>
	</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/client/list.js"></script>
<script language="javascript">
    <shiro:hasPermission name="/internal/clientstatus/updateUpgradeFailedReason">
    function updateUpgradeFailedReason(self){
        var clientStatus = {};
        clientStatus.clientID = self.id;
        clientStatus.upgradeFailedReason = self.value.trim();
        $.ajax({
            url: '/internal/clientstatus/updateUpgradeFailedReason',
            data: JSON.stringify(clientStatus),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if(result){
                    $().toastmessage('showSuccessToast', "更新成功");

                }else{
                    $().toastmessage('showErrorToast', "更新失败");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "更新失败");
            }
        });
    }
    </shiro:hasPermission>
</script>
</body>
</html>

