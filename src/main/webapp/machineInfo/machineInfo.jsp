<%@ include file="/commons/global.jsp" %>
<html>
<head>
<title>机器管理</title>
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

#machineInfoDiv {
	width: 100%;
}

#showCustomerBottomDiv {
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
				<form id="searchMachineInfoForm" method="post" action="/internal/machineInfo/searchMachineInfos" style="font-size: 12px;" cellpadding=3>
					<table style="font-size: 12px;width:100%">
						<tr>
							<td align="left" colspan="2">
								<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
								<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
								<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
								<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
								<input type="hidden" name="startUpStatusHidden" id="startUpStatusHidden" value="${machineInfoCriteria.startUpStatus}"/>
								<input type="hidden" name="runningProgramTypeHidden" id="runningProgramTypeHidden" value="${machineInfoCriteria.runningProgramType}"/>
								<input type="hidden" name="hiddenColumns" id="hiddenColumns" value="${machineInfoCriteria.hiddenColumns}"/>
								<input type="hidden" name="haveHiddenColumns" id="haveHiddenColumns" value="${machineInfoCriteria.haveHiddenColumns}"/>
								客户端ID:<input type="text" name="clientID" id="clientID" value="${machineInfoCriteria.clientID}" style="width: 90px;">
								&nbsp;&nbsp;
								优化组:<input type="text" name="groupName" id="groupName" value="${machineInfoCriteria.groupName}" style="width: 120px;">
								<input id="groupNameFuzzyQuery" name="groupNameFuzzyQuery" type="checkbox" value="groupNameFuzzyQuery" ${machineInfoCriteria.groupNameFuzzyQuery != null ? "checked=true" : ""}/>模糊查询
                                &nbsp;&nbsp;
								当前操作类型:<input type="text" name="operationType" id="operationType" value="${machineInfoCriteria.operationType}" style="width: 120px;">
								&nbsp;&nbsp;
								版本:<input type="text" name="version" id="version" value="${machineInfoCriteria.version}" style="width: 20px;">
								&nbsp;&nbsp;
								目标版本:<input type="text" name="targetVersion" id="targetVersion" value="${machineInfoCriteria.targetVersion}" style="width: 20px;">
								&nbsp;&nbsp;
								城市:<input type="text" name="city" id="city" value="${machineInfoCriteria.city}" style="width: 120px;">
								&nbsp;&nbsp;
								失败原因:<input type="text" name="upgradeFailedReason" id="upgradeFailedReason" value="${machineInfoCriteria.upgradeFailedReason}"
											  style="width: 50px;">
							&nbsp;&nbsp;
							</select>
							&nbsp;&nbsp;
							服务器ID:<input type="text" name="vpsBackendSystemComputerID" id="vpsBackendSystemComputerID" value="${machineInfoCriteria.vpsBackendSystemComputerID}" style="width: 80px;">
							&nbsp;&nbsp;
							流转分组:<input type="text" name="switchGroupName" id="switchGroupName" value="${machineInfoCriteria.switchGroupName}" style="width: 100px;">
							&nbsp;&nbsp;
							排序:<select name="orderBy" id="orderBy">
							<option value=""></option>
							<c:forEach items="${orderByMap}" var="entry">
								<c:choose>
									<c:when test="${entry.key eq machineInfoCriteria.orderBy}"><option selected value="${entry.key}">${entry.value}</option></c:when>
									<c:otherwise><option value="${entry.key}">${entry.value}</option></c:otherwise>
								</c:choose>
							</c:forEach>
							</select>
							</td>
						</tr>
						<tr>
						<td align="left">
							<input id="hasProblem" name="hasProblem" type="checkbox" value="hasProblem" ${machineInfoCriteria.hasProblem != null ? "checked=true" : ""}>停了</input>
							&nbsp;&nbsp;
							<input id="renewal" name="renewal" type="checkbox" value="renewal" ${machineInfoCriteria.renewal != null ? "checked=true" : ""}>续费</input>
							&nbsp;&nbsp;
							<input id="hasGroup" name="hasGroup" type="checkbox" value="hasGroup" ${machineInfoCriteria.hasGroup != null ? "checked=true" : ""}>有分组</input>
							&nbsp;&nbsp;
							<input id="noGroup" name="noGroup" type="checkbox" value="noGroup" ${machineInfoCriteria.noGroup != null ? "checked=true" : ""}>没分组</input>
							&nbsp;&nbsp;
							<input id="noVNC" name="noVNC" type="checkbox" value="noVNC" ${machineInfoCriteria.noVNC != null ? "checked=true" : ""}>没VNC</input>
							&nbsp;&nbsp;
							<input id="noUpgrade" name="noUpgrade" type="checkbox" value="noUpgrade" ${machineInfoCriteria.noUpgrade != null ? "checked=true" : ""}>没升级</input>
							&nbsp;&nbsp;
							<input id="noChangePassword" name="noChangePassword" type="checkbox" value="noChangePassword" ${machineInfoCriteria.noChangePassword != null ? "checked=true" : ""}>没修改密码</input>
							&nbsp;&nbsp;
							<input id="startUpClient" name="startUpClient" type="checkbox" value="startUpClient" ${machineInfoCriteria.startUpClient != null ? "checked=true" : ""}>开机机器</input>
							&nbsp;&nbsp;
							<input id="showFetchKeywordStatus" name="showFetchKeywordStatus" type="checkbox" value="showFetchKeywordStatus"
							${machineInfoCriteria.showFetchKeywordStatus != null ? "checked=true" : ""}>显示取词状态</input>
							&nbsp;&nbsp;
							状态:<select name="valid" id="valid">
							<c:forEach items="${validMap}" var="entry">
								<c:choose>
									<c:when test="${entry.value eq machineInfoCriteria.valid}"><option selected value="${entry.value}">${entry.key}</option></c:when>
									<c:otherwise><option value="${entry.value}">${entry.key}</option></c:otherwise>
								</c:choose>
							</c:forEach>
							</select>
							&nbsp;&nbsp;
							开机状态:
							<select name="startUpStatus" id="startUpStatus">
								<option value=''>All</option>
								<option value='New'>New</option>
								<option value='Processing'>Processing</option>
								<option value='Setting'>Setting</option>
								<option value='Downloading'>Downloading</option>
								<option value='Completed'>Completed</option>
								<option value='Error'>Error</option>
							</select>&nbsp;&nbsp;
							运行程序类型:
							<select name="runningProgramType" id="runningProgramType">
								<option value=''>All</option>
								<option value='New'>New</option>
								<option value='Old'>Old</option>
							</select>

							<shiro:hasPermission name="/internal/machineInfo/searchMachineInfos">
								&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" onclick="resetPageNumber()" value=" 查询 ">
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/deleteMachineInfos">
								&nbsp;&nbsp;<input type="button" onclick="delAllItems(this)" value=" 删除 ">
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/saveMachineInfo">
								&nbsp;&nbsp;<input type="button" onclick="showReopenClientDialog()" value=" 重开机器 ">
								&nbsp;&nbsp;<input type="button" onclick="finishStartUp()" value=" 完成开机 ">
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/uploadVPSFile">
								&nbsp;&nbsp;<input type="button" name="btnFilter" onclick="showUploadVPSDialog('common')" value=" 导入普通终端 ">
								&nbsp;&nbsp;<input type="button" onclick="showUploadVPSDialog('startUp')" value=" 导入开机终端 ">
							</shiro:hasPermission>
							&nbsp;&nbsp;<input type="button" onclick="headerTableSetting()" value="表格设置">
						</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
							<shiro:hasPermission name="/internal/machineInfo/updateMachineInfoTargetVersion">
								<a target="_blank" href="javascript:showTargetVersionSettingDialog(this)">设定目标版本</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/saveMachineInfo">
								<a target="_blank" href="javascript:showTargetVPSPasswordSettingDialog(this)">设定目标密码</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/updateMachineInfoRenewalDate">
								|<a target="_blank" href="javascript:showRenewalSettingDialog(this)">续费</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/resetRestartStatusForProcessing">
								|<a target="_blank" href="javascript:resetRestartStatus()">重置重启状态</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/uploadVNCFile">
								|<a target="_blank" href="javascript:showUploadVNCDialog()">上传VNC文件</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/saveMachineInfo">
								|<a target="_blank" href="javascript:machineInfoBatchUpdate()">批量设置</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/changeStatus">
								|<a target="_blank" href="javascript:batchChangeStatus(true)">批量开始</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/changeStatus">
								|<a target="_blank" href="javascript:batchChangeStatus(false)">批量暂停</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/machineInfo/changeTerminalType">
								|<a target="_blank" href="javascript:batchChangeTerminalType()">批量变更终端类型</a>
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
			<td align="center" width=60>当前操作类型</td>
			<td align="center" width=20>续费<br>日期</td>
			<td align="center" width=30>现版本<br>目标版本</td>
			<td align="center" width=40>重启数/重启状态<br>失败次数</td>
			<td align="center" width=60>所在城市<br>终端状态</td>
			<td align="center" width=30>剩余空间</td>
			<td align="center" width=40>最新工作时间<br>重启时间</td>
			<td align="center" width=40>重启排序时间<br>发通知时间</td>
			<td align="center" width=30>成功次数<br>操作次数</td>
			<td align="center" width=50>宽带账号<br>宽带密码</td>
			<td align="center" width=30>运行程序<br>类型</td>
			<td align="center" width=40>开机状态<br>下载程序类型</td>
			<td align="center" width=20>状态</td>
			<td align="center" width=40>失败原因</td>
			<td align="center" width=40>服务器ID</td>
			<td align="center" width=80>操作</td>
		</tr>
	</table>
</div>
<div id="machineInfoDiv" style="margin-bottom: 30px;">
	<table width=100% style="font-size: 12px" id="machineInfoTable">
		<c:forEach items="${page.records}" var="machineInfo" varStatus="tr">
			<c:set var="isValidClient" value="true"/>
			<c:choose>
				<c:when test="${machineInfo.valid}">
					<c:choose>
						<c:when test="${machineInfo.red}">
							<c:set var="keywordColor" value="#FF0000"/>
							<c:set var="isValidClient" value="false"/>
						</c:when>
						<c:when test="${machineInfo.orange}">
							<c:set var="keywordColor" value="#e86e33"/>
							<c:set var="isValidClient" value="false"/>
						</c:when>
						<c:when test="${machineInfo.yellow}">
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
			<td align="center" width=10><input type="checkbox" name="clientID" value="${machineInfo.clientID}" onclick="decideSelectAll()"/></td>
			<td width=40>
				<font color="${keywordColor}">${machineInfo.clientID}</font>
				<c:if test="${!isValidClient}">
					<span name="invalidClient" id="span_${machineInfo.clientID}"></span>
				</c:if>
			</td>
			<td width=60>
				<shiro:hasPermission name="/internal/machineInfo/updateGroup">
					<input type="text" value="${machineInfo.group == null ? "" : machineInfo.group}"
						   name="group" id="${machineInfo.clientID}" onBlur="updateGroup(this)" style="width: 100%;"/>
				</shiro:hasPermission>
			</td>
			<td width="60">${machineInfo.usingOperationType}</td>
			<td width=20><font color="${keywordColor}"><fmt:formatDate value="${machineInfo.renewalDate}"
															  pattern="MM-dd"/></font></td>
			<td width=30><font
					color="${keywordColor}">${machineInfo.version == null ? "" : machineInfo.version}<br>${machineInfo.targetVersion == null ? "" : machineInfo.targetVersion}</font>
			</td>
			<td width=40><font
					color="${keywordColor}">${machineInfo.restartCount}/${machineInfo.restartStatus == null ? "" : machineInfo.restartStatus}<br>${machineInfo.continuousFailCount}</font>
			</td>
			<td width=60 style="word-break: break-all;"><font
					color="${keywordColor}">${machineInfo.city == null ? "" : machineInfo.city}<br>${machineInfo.status == null ? "" : machineInfo.status}</font>
			</td>
			<td width=30><font color="${keywordColor}">${machineInfo.freeSpace}</font></td>
			<td width=40><font color="${keywordColor}"><fmt:formatDate value="${machineInfo.lastVisitTime}"
															  pattern="MM-dd HH:mm"/><br><fmt:formatDate
					value="${machineInfo.restartTime}" pattern="MM-dd HH:mm"/></font></td>
			<td width=40><font color="${keywordColor}"><fmt:formatDate value="${machineInfo.restartOrderingTime}"
															  pattern="MM-dd HH:mm"/><br><fmt:formatDate
					value="${machineInfo.lastSendNotificationTime}" pattern="MM-dd HH:mm"/></font></td>
			<td width=30><font
					color="${keywordColor}">${machineInfo.optimizationSucceedCount}<br>${machineInfo.optimizationTotalCount}</font>
			</td>
			<td width=50><font color="${keywordColor}">${machineInfo.broadbandAccount}<br>${machineInfo.broadbandPassword}</font></td>
			<td width=30><font color="${keywordColor}">${machineInfo.runningProgramType}</font></td>
			<td width=40><font color="${keywordColor}">${machineInfo.startUpStatus}<br>${machineInfo.downloadProgramType}</font></td>
			<td width=20><font color="${keywordColor}">${machineInfo.valid ? "监控中" : "暂停监控"}</font></td>
			<td width=40>
				<shiro:hasPermission name="/internal/machineInfo/updateUpgradeFailedReason">
					<input type="text"
						   value="${machineInfo.upgradeFailedReason == null ? "" : machineInfo.upgradeFailedReason}"
						   name="upgradeFailedReason" id="${machineInfo.clientID}" onBlur="updateUpgradeFailedReason(this)"
						   style="width: 100%;"/>
				</shiro:hasPermission>
			</td>
			<td width=40><font color="${keywordColor}">${machineInfo.vpsBackendSystemComputerID}</font></td>
			<td width=80>
				<c:choose>
					<c:when test="${null != machineInfo.host and '' != machineInfo.host}">
						<shiro:hasPermission name="/internal/machineInfo/downloadVNCFile">
							<a href="javascript:connectVNC('${machineInfo.host}', '${machineInfo.port}', '${machineInfo.userName}', '${machineInfo.password}')">VNC</a>
						</shiro:hasPermission>
					</c:when>
					<c:otherwise>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:otherwise>
				</c:choose>
				&nbsp;
				<shiro:hasPermission name="/internal/machineInfo/saveMachineInfo">
					<a href="javascript:showSettingDialog('${machineInfo.clientID}', this)">修改</a>
				</shiro:hasPermission>
				&nbsp;
				<shiro:hasPermission name="/internal/machineInfo/deleteMachineInfo">
					<a href="javascript:delItem('${machineInfo.clientID}')">删除</a>
				</shiro:hasPermission>
				<br>
				<shiro:hasPermission name="/internal/machineInfo/changeStatus">
				<c:choose>
					<c:when test="${machineInfo.valid}">
						<a href="javascript:changeStatus('${machineInfo.clientID}',false)">暂停监控</a>
					</c:when>
					<c:otherwise>
						<a href="javascript:changeStatus('${machineInfo.clientID}',true)">开始监控</a>
					</c:otherwise>
				</c:choose>
				</shiro:hasPermission>
				&nbsp;
				<shiro:hasPermission name="/internal/machineInfo/changeTerminalType">
				<a href="javascript:changeTerminalType('${machineInfo.clientID}')">变更终端类型</a>
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
		<input type="text" value="${page.current}" style="width:30px;" onchange="changePaging(this.value,'${page.size}')">/${page.pages}&nbsp;&nbsp;
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
		<option>250</option>
		<option>500</option>
		<option>1000</option>
	</select>
	</div>
</div>

	<div style="display: none;">
		<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>
	</div>
	<div id="changeSettingDialog" class="easyui-dialog" style="display: none;left: 40%;">
		<table>
			<tr>
				<td>
					<table id="td_1" style="font-size:12px">
						<tr name="trItem" onclick="checkItem(this)">
							<th>分组</th>
							<td>
								<input type="hidden" id="settingClientID" />
								<input type="text" name="settingGroup" id="settingGroup" />
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>允许换组</th>
							<td>
								<select name="allowSwitchGroup" id="allowSwitchGroup">
									<option value="0">不允许</option>
									<option value="1">允许</option>
								</select>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>流转分组</th>
							<td>
								<input type="text" name="switchGroupName" id="switchGroupName"/>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>主机</th>
							<td>
								<input type="text" name="host" id="host"/>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>端口</th>
							<td>
								<input type="text" name="port" id="port"/>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>VNC和操作系统用户名</th>
							<td>
								<input type="text" name="csUserName" id="csUserName" value="Administrator"/>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>VPS后台系统电脑ID</th>
							<td>
								<input type="text" name="vpsBackendSystemComputerID" id="vpsBackendSystemComputerID"/>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>VPS后台系统密码</th>
							<td>
								<input type="text" name="vpsBackendSystemPassword" id="vpsBackendSystemPassword"/>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>宽带账号</th>
							<td>
								<input type="text" name="broadbandAccount" id="broadbandAccount" value=""/>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>宽带密码</th>
							<td>
								<input type="text" name="broadbandPassword" id="broadbandPassword" value=""/>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="targetVersionSettingDialog" class="easyui-dialog" style="display: none;left: 40%;">
		目标版本：<input type="text" name="settingTargetVersion" id="settingTargetVersion" />
	</div>
	<div id="targetVPSPasswordSettingDialog" class="easyui-dialog" style="display: none;left: 40%;">
		目标密码：<input type="text" name="settingTargetVPSPassword" id="settingTargetVPSPassword" />
	</div>

	<div id="renewalSettingDialog" class="easyui-dialog" style="display: none;left: 40%;">
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

	<div id="uploadVNCDialog" class="easyui-dialog" style="display: none;left: 40%;">
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

	<div id="uploadVPSDialog" class="easyui-dialog" style="display: none;left: 40%;">
		<form method="post" id="uploadVPSForm" action="" enctype="multipart/form-data">
			<table width="95%" style="margin-top: 10px;margin-left: 10px">
				<tr>
					<td id="programType" >
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

	<div id="reopenClientDiv" class="easyui-dialog" style="display: none">
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

	<div id="headerTableDialog" class="easyui-dialog" style="display: none">
		<table>
			<tr><td><input type="checkbox" name="columnName" id="1">客户端ID</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="2">优化组</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="3">当前操作类型</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="4">续费日期</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="5">现版本-目标版本</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="6">重启数/重启状态-失败次数</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="7">所在城市-终端状态</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="8">剩余空间</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="9">最新工作时间-重启时间</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="10">重启排序时间-发通知时间</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="11">成功次数-操作次数</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="12">宽带账号-宽带密码</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="13">运行程序-类型</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="14">开机状态-下载程序类型</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="15">状态</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="16">失败原因</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="17">服务器ID</td></tr>
			<tr><td><input type="checkbox" name="columnName" id="18">操作</td></tr>
		</table>
	</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/machineInfo/machineInfo.js"></script>
<script language="javascript">
    <shiro:hasPermission name="/internal/machineInfo/updateUpgradeFailedReason">
    function updateUpgradeFailedReason(self){
        var machineInfo = {};
        machineInfo.clientID = self.id;
        machineInfo.upgradeFailedReason = self.value.trim();
        $.ajax({
            url: '/internal/machineInfo/updateUpgradeFailedReason',
            data: JSON.stringify(machineInfo),
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            timeout: 5000,
            type: 'POST',
            success: function (result) {
                if(result){
                    $().toastmessage('showSuccessToast', "更新成功", true);

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

