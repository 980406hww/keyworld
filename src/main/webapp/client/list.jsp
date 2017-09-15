<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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
	display: none;
	margin: 5px 0px 0px 5px;
}

#targetVersionSettingDialog{
	margin: 15px 0px 0px 5px;
}

#clientStatusDiv {
	overflow: scroll;
	width: 100%;
	height: 95%;
	margin: auto;
}

#showClientStatusBottomDiv {
	float: right;
	width: 580px;
}
-->
</style>
	<link href="/ui/jquery-ui.css" rel="stylesheet" type="text/css" />
	<link href="/css/menu.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	<script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="/ui/jquery-ui.js"></script>
	<script language="javascript" type="text/javascript" src="/js/slide.js"></script>
	<script language="javascript" type="text/javascript" src="/common.js"></script>
</head>
<body>
<div id="clientStatusDiv">
	<table width=100% style="font-size: 12px;" cellpadding=3>
		<tr>
			<td colspan=16 align="left"><%@include file="/menu.jsp"%>
			</td>
		</tr>
		<tr>
			<td colspan=16>
				<form id="searchClientStatusForm" method="post" action="/internal/clientstatus/searchClientStatuses">
					<table style="font-size: 12px;width:100%">
						<tr>
							<td align="left" colspan="2">
								<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
								<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
								<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
								<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
								客户端ID:<input type="text" name="clientID" id="clientID" value="${clientStatusCriteria.clientID}" style="width: 90px;">
								&nbsp;&nbsp;&nbsp;
								优化组:<input type="text" name="groupName" id="groupName" value="${clientStatusCriteria.groupName}" style="width: 120px;">
								&nbsp;&nbsp;&nbsp;
								版本:<input type="text" name="version" id="version" value="${clientStatusCriteria.version}" style="width: 60px;">
								&nbsp;&nbsp;&nbsp;
								城市:<input type="text" name="city" id="city" value="${clientStatusCriteria.city}" style="width: 120px;">
								&nbsp;&nbsp;&nbsp;
								失败原因:<input type="text" name="upgradeFailedReason" id="upgradeFailedReason" value="${clientStatusCriteria.upgradeFailedReason}"
											  style="width: 50px;">
								&nbsp;&nbsp;&nbsp;
								状态:<select name="valid" id="valid">
								<c:forEach items="${validMap}" var="entry">
									<c:choose>
										<c:when test="${entry.value eq clientStatusCriteria.valid}"><option selected value="${entry.value}">${entry.key}</option></c:when>
										<c:otherwise><option value="${entry.value}">${entry.key}</option></c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
							&nbsp;&nbsp;&nbsp;
							操作类型:<select name="operationType" id="operationType">
								<c:forEach items="${operationTypeValues}" var="operationType">
									<c:choose>
										<c:when test="${operationType eq clientStatusCriteria.operationType}"><option selected value="${operationType}">${operationType}</option></c:when>
										<c:otherwise><option value="${operationType}">${operationType}</option></c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
								&nbsp;&nbsp;&nbsp;
							排序:<select name="orderBy" id="orderBy">
								<c:forEach items="${orderByMap}" var="entry">
									<c:choose>
										<c:when test="${entry.key eq clientStatusCriteria.orderBy}"><option selected value="${entry.key}">${entry.value}</option></c:when>
										<c:otherwise><option value="${entry.key}">${entry.value}</option></c:otherwise>
									</c:choose>
								</c:forEach>
							</select></td>
						</tr>
						<tr>
						<td align="left">
							<input id="hasProblem" name="hasProblem" type="checkbox" value="hasProblem" ${clientStatusCriteria.hasProblem != null ? "checked=true" : ""}>停了</input>
							&nbsp;&nbsp;&nbsp;
							<input id="renewal" name="renewal" type="checkbox" value="renewal" ${clientStatusCriteria.renewal != null ? "checked=true" : ""}>续费</input>
							&nbsp;&nbsp;&nbsp;
							<input id="noGroup" name="noGroup" type="checkbox" value="noGroup" ${clientStatusCriteria.noGroup != null ? "checked=true" : ""}>没分组</input>
							&nbsp;&nbsp;&nbsp;
							<input id="noOperationType" name="noOperationType" type="checkbox" value="noOperationType" ${clientStatusCriteria.noOperationType != null ? "checked=true" : ""}>没操作类型</input>
							&nbsp;&nbsp;&nbsp;
							<input id="noVNC" name="noVNC" type="checkbox" value="noVNC" ${clientStatusCriteria.noVNC != null ? "checked=true" : ""}>没VNC</input>
							&nbsp;&nbsp;&nbsp;
							<input id="noUpgrade" name="noUpgrade" type="checkbox" value="noUpgrade" ${clientStatusCriteria.noUpgrade != null ? "checked=true" : ""}>没升级</input>
							&nbsp;&nbsp;&nbsp;
							<input id="showFetchKeywordStatus" name="showFetchKeywordStatus" type="checkbox" value="showFetchKeywordStatus"
							${clientStatusCriteria.showFetchKeywordStatus != null ? "checked=true" : ""}>显示取词状态</input>
						</td>
						<td align="right" width="30px"><input type="submit" name="btnFilter" id="btnFilter" onclick="resetPageNumber()" value=" 查询 "></td>
						</tr>
						<tr>
						<td colspan="2">
						</td>
						</tr>
						<tr>
							<td colspan="2" align="right">
								<a target="_blank" href="javascript:showTargetVersionSettingDialog(this)">设定目标版本</a>
								|<a target="_blank" href="javascript:showRenewalSettingDialog(this)">续费</a>
								|<a target="_blank" href="javascript:delAllItems(this)">删除所选</a>
								|<a target="_blank" href="javascript:resetRestartStatus()">重置重启状态</a>
								|<a target="_blank" href="javascript:showUploadVNCDialog()">上传VNC文件</a>
								|<a target="_blank" href="javascript:downloadVNCFile()">下载VNC连接压缩文件</a>
								|<a target="_blank" href="javascript:downloadFullVNCFile()">下载完整版VNC文件</a>
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
		<c:forEach items="${page.records}" var="clientStatus">
			<c:set var="isValidClient" value="true" />
			<c:choose>
				<c:when test="${clientStatus.valid}">
					<c:choose>
						<c:when test="${clientStatus.red}">
							<c:set var="keywordColor" value="#FF0000" />
							<c:set var="isValidClient" value="false" />
						</c:when>
						<c:when test="${clientStatus.yellow}">
							<c:set var="keywordColor" value="#ef00ff" />
							<c:set var="isValidClient" value="false" />
						</c:when>
						<c:otherwise>
							<c:set var="keywordColor" value="green" />
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:set var="keywordColor" value="green" />
				</c:otherwise>
			</c:choose>
		<tr onmouseover="doOver(this);" onmouseout="doOut(this);">
			<td><input type="checkbox" name="clientID" value="${clientStatus.clientID}"/></td>
			<td>
				<font color="${keywordColor}">${clientStatus.clientID}</font>
				<c:if test="${!isValidClient}">
					<span name="invalidClient" id="span_${clientStatus.clientID}"></span>
				</c:if>
			</td>
			<td><input type="text" value="${clientStatus.group == null ? "" : clientStatus.group}"
				name="group" id="${clientStatus.clientID}"  onBlur="updateGroup(this)"
				style="width: 100%; height: 100%" /></td>
			<td>
				<select name="operationType${clientStatus.clientID}" id="operationType${clientStatus.clientID}" onChange="updateOperationType(this)"
				 style="width: 100%; height: 100%" />
				<c:forEach items="${operationTypeValues}" var="operationType">
					<c:choose>
						<c:when test="${operationType eq clientStatus.operationType}"><option selected value="${operationType}">${operationType}</option></c:when>
						<c:otherwise><option value="${operationType}">${operationType}</option></c:otherwise>
					</c:choose>
				</c:forEach>
				</select>
			</td>
			<td><font color="${keywordColor}"><fmt:formatDate value="${clientStatus.renewalDate}" pattern="MM-dd" /></font></td>
			<td><font color="${keywordColor}">${clientStatus.version == null ? "" : clientStatus.version}</br>${clientStatus.targetVersion == null ? "" : clientStatus.targetVersion}</font></td>
			<td><font color="${keywordColor}">${clientStatus.restartCount}/${clientStatus.restartStatus == null ? "" : clientStatus.restartStatus}</br>${clientStatus.pageNo}/${clientStatus.continuousFailCount}</font></td>
			<td style="word-break: break-all"><font color="${keywordColor}">${clientStatus.city == null ? "" : clientStatus.city}</br>${clientStatus.status == null ? "" : clientStatus.status}</font></td>
			<td><font color="${keywordColor}">${clientStatus.freeSpace}</font></td>
			<td><font color="${keywordColor}"><fmt:formatDate value="${clientStatus.lastVisitTime}" pattern="MM-dd HH:mm" /></br><fmt:formatDate value="${clientStatus.restartTime}" pattern="MM-dd HH:mm" /></font></td>
			<td><font color="${keywordColor}"><fmt:formatDate value="${clientStatus.restartOrderingTime}" pattern="MM-dd HH:mm" /></br><fmt:formatDate value="${clientStatus.lastSendNotificationTime}" pattern="MM-dd HH:mm" /></font></td>
			<td><font color="${keywordColor}">${clientStatus.optimizationSucceedCount()}</br>${optimizationTotalCount}</font></td>
			<td><font color="${keywordColor}">${clientStatus.valid ? "监控中" : "暂停监控"}</font></td>
			<td><input type="text" value="${clientStatus.upgradeFailedReason == null ? "" : clientStatus.upgradeFailedReason}"
					   name="upgradeFailedReason" id="${clientStatus.clientID}"  onBlur="updateUpgradeFailedReason(this)"
					   style="width: 100%; height: 100%" /></td>
			<td><font color="${keywordColor}">${clientStatus.vpsBackendSystemComputerID}</font></td>
			<td>	
				<c:choose>
					<c:when test="${null != clientStatus.host and !'' eq clientStatus.host}">
						<a href="javascript:connectVNC('${clientStatus.clientID}')">VNC</a>
					</c:when>
					<c:otherwise>
						&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					</c:otherwise>
				</c:choose>
				&nbsp;		
				<a href="javascript:showSettingDialog('${clientStatus.clientID}', this)">设置</a>
				&nbsp;
				<a href="javascript:delItem('${clientStatus.clientID}')">删除</a>
				</br>
				<c:choose>
					<c:when test="${clientStatus.valid}">
						<a href="javascript:changeMonitorType('${clientStatus.clientID}',false)">暂停监控</a>
					</c:when>
					<c:otherwise>
						<a href="javascript:changeMonitorType('${clientStatus.clientID}',true)">开始监控</a>
					</c:otherwise>
				</c:choose>
				&nbsp;
				<a href="javascript:changeTerminalType('${clientStatus.clientID}')">变更终端类型</a>
				
			</td>
		</tr>
		</c:forEach>
	</table>
</div>
	<hr>
	<div id="clientStatusBottomDiv" align="right">
		<input id="fisrtButton" type="button" onclick="changePaging(1,'${page.size}')" value="首页"/>
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="upButton" type="button" onclick="changePaging('${page.current-1}','${page.size}')" value="上一页"/>
		&nbsp;&nbsp;&nbsp;&nbsp;${page.current}/${page.pages}&nbsp;&nbsp;
		<input id="nextButton" type="button" onclick="changePaging('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')" value="下一页">
		&nbsp;&nbsp;&nbsp;&nbsp;
		<input id="lastButton" type="button" onclick="changePaging('${page.pages}','${page.size}')" value="末页">
		&nbsp;&nbsp;&nbsp;&nbsp;
		总记录数:${page.total}&nbsp;&nbsp;
		每页显示条数:
		<select id="chooseRecords" onchange="changePaging(${page.current},this.value)" style="margin-right: 10px;">
			<option>10</option>
			<option>25</option>
			<option>50</option>
			<option>75</option>
			<option>100</option>
		</select>
		<input type="hidden" id="currentPageHidden" value="${page.current}"/>
		<input type="hidden" id="pageSizeHidden" value="${page.size}"/>
		<input type="hidden" id="pageCountHidden" value="${page.pages}"/>
	</div>

	<script language="javascript">
        $(function () {
            var clientStatusBottomDiv = $('#clientStatusBottomDiv');
            var pageSize = clientStatusBottomDiv.find('#pageSizeHidden').val();
            clientStatusBottomDiv.find('#chooseRecords').val(pageSize);
            var pageCount = clientStatusBottomDiv.find('#pageCountHidden').val();
            clientStatusBottomDiv.find('#pageCountHidden').val(pageCount);
            var currentPage = clientStatusBottomDiv.find('#currentPageHidden').val();
            clientStatusBottomDiv.find('#currentPageHidden').val(currentPage);
            if(parseInt(currentPage) > 1 && parseInt(currentPage) < parseInt(pageCount)) {
                clientStatusBottomDiv.find("#firstButton").removeAttr("disabled");
                clientStatusBottomDiv.find("#upButton").removeAttr("disabled");
                clientStatusBottomDiv.find("#nextButton").removeAttr("disabled");
                clientStatusBottomDiv.find("#lastButton").removeAttr("disabled");
            } else if (parseInt(pageCount) <= 1) {
                clientStatusBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                clientStatusBottomDiv.find("#upButton").attr("disabled", "disabled");
                clientStatusBottomDiv.find("#nextButton").attr("disabled", "disabled");
                clientStatusBottomDiv.find("#lastButton").attr("disabled", "disabled");
            } else if (parseInt(currentPage) <= 1) {
                clientStatusBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                clientStatusBottomDiv.find("#upButton").attr("disabled", "disabled");
            } else {
                clientStatusBottomDiv.find("#nextButton").attr("disabled", "disabled");
                clientStatusBottomDiv.find("#lastButton").attr("disabled", "disabled");
            }
        });

        function changePaging(currentPageNumber, pageSize) {
            var searchClientStatusForm = $("#searchClientStatusForm");
            searchClientStatusForm.find("#currentPageNumberHidden").val(currentPageNumber);
            searchClientStatusForm.find("#pageSizeHidden").val(pageSize);
            searchClientStatusForm.submit();
        }

        function resetPageNumber() {
            $("#searchClientStatusForm").find("#currentPageNumberHidden").val(1);
        }

        function downloadVNCFile() {
            $("#downloadVNCForm").submit();
        }

        function downloadFullVNCFile() {
            $("#downloadFullVNCForm").submit();
        }

        function showUploadVNCDialog() {
            $('#uploadVNCDialog').dialog({
                resizable: false,
                width: 430,
                modal: true,
                title: '上传VNC文件',
                buttons: {
                    "上传": function () {
                        var fileValue = $("#uploadVNCDialog").find("#file").val();
                        if(fileValue == ""){
                            alert("请选择要上传的VNC配置文件!");
                            return false;
                        }
                        var posIndex = fileValue.indexOf(".xml");
                        if (posIndex == -1) {
                            alert("只能上传的XML文件！");
                            return false;
                        }

                        var formData = new FormData();
                        formData.append('file', $("#uploadVNCDialog").find("#file")[0].files[0]);
						$.ajax({
							url: '/internal/clientstatus/uploadVNCFile',
							type: 'POST',
							cache: false,
							data: formData,
							processData: false,
							contentType: false,
							success: function (result) {
								if (result) {
									showInfo("上传成功", self);
								} else {
									showInfo("上传失败", self);
								}
							},
							error: function () {
								showInfo("上传失败", self);
							}
						});
                        $(this).dialog("close");
                    },
                    "取消": function () {
                        $(this).dialog("close");
                    }
                }
            });
        }

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

        function delItem(clientID) {
            if (confirm("确定要删除这台终端吗?") == false) return;
            $.ajax({
                url: '/internal/clientstatus/deleteClientStatus/' + clientID,
                type: 'POST',
                success: function (result) {
                    if (result) {
                        showInfo("操作成功", self);
                        window.location.reload();
                    } else {
                        showInfo("操作失败", self);
                    }
                },
                error: function () {
                    showInfo("操作失败", self);
                }
            });
        }

        function delAllItems(self) {
            var clientIDs = getSelectedClientIDs();
            if (clientIDs === '') {
                alert('请选择要删除的终端');
                return;
            }
            if (confirm("确定要删除这些负面词吗?") == false) return;
            var postData = {};
            postData.clientIDs = clientIDs.split(",");
            $.ajax({
                url: '/internal/clientstatus/deleteClientStatuses',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result) {
                        showInfo("操作成功", self);
                        window.location.reload();
                    } else {
                        showInfo("操作失败", self);
                    }
                },
                error: function () {
                    showInfo("操作失败", self);
                }
            });
        }

        function getSelectedClientIDs() {
            var clientIDs = '';
            $.each($("input[name=clientID]:checkbox:checked"), function () {
                if (clientIDs === '') {
                    clientIDs = $(this).val();
                } else {
                    clientIDs = clientIDs + "," + $(this).val();
                }
            });
            return clientIDs;
        }

		function resetRestartStatus() {
			if (confirm("确实要重设状态为Processing或者Logging终端的重启状态吗?") == false) return;
			$.ajax({
				url: '/internal/clientstatus/resetRestartStatusForProcessing' ,
				type: 'POST',
				success: function (result) {
					if(result){
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
		function changeMonitorType(clientID, monitorType) {
		    if(monitorType) {
                if (confirm("确实要开始监控这台终端吗?") == false) {
                    return;
				}
			} else {
                if (confirm("确实要暂停监控这台终端吗?") == false) {
					return;
				}
			}
            $.ajax({
                url: '/internal/clientstatus/changeMonitorType/' + clientID,
                type: 'POST',
                success: function (result) {
                    if (result) {
                        showInfo("操作成功", self);
                        window.location.reload();
                    } else {
                        showInfo("操作失败", self);
                    }
                },
                error: function () {
                    showInfo("操作失败", self);
                }
            });

		}

		function updateGroup(self){
		    var clientStatus = {};
            clientStatus.clientID = self.id;
            clientStatus.group = self.value.trim();
            $.ajax({
                url: '/internal/clientstatus/updateGroup',
                data: JSON.stringify(clientStatus),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if(result){
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

		function changeTerminalType(clientID){
			var postData = {};
			postData.clientID = clientID;
			$.ajax({
				url: '/internal/clientstatus/changeTerminalType',
				data: JSON.stringify(postData),
				headers: {
					'Accept': 'application/json',
					'Content-Type': 'application/json'
				},
				timeout: 5000,
				type: 'POST',
				success: function (result) {
					if(result){
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
		function updateOperationType(self){
		    var clientStatus = {};
            clientStatus.clientID = self.id.replace("operationType", "");
            clientStatus.operationType = self.value.trim();
            $.ajax({
                url: '/internal/clientstatus/updateOperationType',
                data: JSON.stringify(clientStatus),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if(result){
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
		function showSettingDialog(clientID, self){
		    $.ajax({
		        url: '/internal/clientstatus/getClientStatus/' + clientID,
		        type: 'Get',
		        success: function (clientStatus) {
		        	if(clientStatus == null) {
		        		showInfo("获取信息失败！", self);
		        	} else {
		        		initSettingDialog(clientStatus, self);
                        $("#changeSettingDialog").dialog({
                            resizable: false,
                            title: "设置",
                            width: 620,
							maxHeight: 510,
                            modal: true,
                            buttons: {
                                "保存": function () {
                                    saveChangeSetting(this);
                                },
                                "取消": function () {
                                    $(this).dialog("close");
                                }
                            }
                        });
		        	}
		        },
		        error: function () {
		        	showInfo("获取信息失败！", self);
		        }
		    });
		}
		function initSettingDialog(clientStatus, self){
			var settingDialogDiv = $("#changeSettingDialog");
			settingDialogDiv.find("#settingClientID").val(clientStatus.clientID);
			settingDialogDiv.find("#settingGroup").val(clientStatus.group != null ? clientStatus.group : "");
			settingDialogDiv.find("#settingOperationType").val(clientStatus.operationType != null ? clientStatus.operationType : "");
			if(clientStatus.pageSize != null){
				settingDialogDiv.find("#pageSize").val(clientStatus.pageSize);
			}
			if(clientStatus.page != null){
				settingDialogDiv.find("#page").val(clientStatus.page);
			}
			if(clientStatus.zhanneiPercent != null){
				settingDialogDiv.find("#zhanneiPercent").val(clientStatus.zhanneiPercent);
			}
			if(clientStatus.dragPercent  != null){
				settingDialogDiv.find("#dragPercent ").val(clientStatus.dragPercent );
			}
			if(clientStatus.kuaizhaoPercent != null){
				settingDialogDiv.find("#kuaizhaoPercent").val(clientStatus.kuaizhaoPercent);
			}
			if(clientStatus.baiduSemPercent != null){
				settingDialogDiv.find("#baiduSemPercent").val(clientStatus.baiduSemPercent);
			}
			if(clientStatus.multiBrowser != null){
				settingDialogDiv.find("#multiBrowser").val(clientStatus.multiBrowser);
			}
			if(clientStatus.clearCookie != null){
				settingDialogDiv.find("#clearCookie").val(clientStatus.clearCookie);
			}
			settingDialogDiv.find("#allowSwitchGroup ").val(clientStatus.allowSwitchGroup );
			settingDialogDiv.find("#disableStatistics ").val(clientStatus.disableStatistics );

			settingDialogDiv.find("#entryPageMinCount").val(clientStatus.entryPageMinCount);
			settingDialogDiv.find("#entryPageMaxCount").val(clientStatus.entryPageMaxCount);
			settingDialogDiv.find("#pageRemainMinTime").val(clientStatus.pageRemainMinTime);
			settingDialogDiv.find("#pageRemainMaxTime").val(clientStatus.pageRemainMaxTime);
			settingDialogDiv.find("#inputDelayMinTime").val(clientStatus.inputDelayMinTime);
			settingDialogDiv.find("#inputDelayMaxTime").val(clientStatus.inputDelayMaxTime);
			settingDialogDiv.find("#slideDelayMinTime").val(clientStatus.slideDelayMinTime);
			settingDialogDiv.find("#slideDelayMaxTime").val(clientStatus.slideDelayMaxTime);
			settingDialogDiv.find("#titleRemainMinTime").val(clientStatus.titleRemainMinTime);
			settingDialogDiv.find("#titleRemainMaxTime").val(clientStatus.titleRemainMaxTime);
			settingDialogDiv.find("#waitTimeAfterOpenBaidu").val(clientStatus.waitTimeAfterOpenBaidu);
			settingDialogDiv.find("#waitTimeBeforeClick").val(clientStatus.waitTimeBeforeClick);
			settingDialogDiv.find("#waitTimeAfterClick").val(clientStatus.waitTimeAfterClick);
			settingDialogDiv.find("#maxUserCount").val(clientStatus.maxUserCount);
			settingDialogDiv.find("#optimizeKeywordCountPerIP").val(clientStatus.optimizeKeywordCountPerIP);

			settingDialogDiv.find("#oneIPOneUser")[0].checked = (clientStatus.oneIPOneUser == 1) ? true : false;
			settingDialogDiv.find("#randomlyClickNoResult")[0].checked = (clientStatus.randomlyClickNoResult == 1) ? true : false;
			settingDialogDiv.find("#justVisitSelfPage")[0].checked = (clientStatus.justVisitSelfPage == 1) ? true : false;
			settingDialogDiv.find("#sleepPer2Words")[0].checked = (clientStatus.sleepPer2Words == 1) ? true : false;
			settingDialogDiv.find("#supportPaste")[0].checked = (clientStatus.supportPaste == 1) ? true : false;
			settingDialogDiv.find("#moveRandomly")[0].checked = (clientStatus.moveRandomly == 1) ? true : false;
			settingDialogDiv.find("#parentSearchEntry")[0].checked = (clientStatus.parentSearchEntry == 1) ? true : false;
			settingDialogDiv.find("#clearLocalStorage")[0].checked = (clientStatus.clearLocalStorage == 1) ? true : false;
			settingDialogDiv.find("#lessClickAtNight")[0].checked = (clientStatus.lessClickAtNight == 1) ? true : false;
			settingDialogDiv.find("#sameCityUser")[0].checked = (clientStatus.sameCityUser == 1) ? true : false;
			settingDialogDiv.find("#locateTitlePosition")[0].checked = (clientStatus.locateTitlePosition == 1) ? true : false;
			settingDialogDiv.find("#baiduAllianceEntry")[0].checked = (clientStatus.baiduAllianceEntry == 1) ? true : false;
			settingDialogDiv.find("#justClickSpecifiedTitle")[0].checked = (clientStatus.justClickSpecifiedTitle == 1) ? true : false;
			settingDialogDiv.find("#randomlyClickMoreLink")[0].checked = (clientStatus.randomlyClickMoreLink == 1) ? true : false;
			settingDialogDiv.find("#moveUp20")[0].checked = (clientStatus.moveUp20 == 1) ? true : false;

			settingDialogDiv.find("#waitTimeAfterOpenBaidu").val(clientStatus.waitTimeAfterOpenBaidu);
			settingDialogDiv.find("#waitTimeBeforeClick").val(clientStatus.waitTimeBeforeClick);
			settingDialogDiv.find("#waitTimeAfterClick").val(clientStatus.waitTimeAfterClick);
			settingDialogDiv.find("#maxUserCount").val(clientStatus.maxUserCount);

			settingDialogDiv.find("#host").val(clientStatus.host != null ? clientStatus.host : "");
			settingDialogDiv.find("#port").val(clientStatus.port != null ? clientStatus.port : "");
			settingDialogDiv.find("#userName").val(clientStatus.userName != null ? clientStatus.userName : "Administrator");
			settingDialogDiv.find("#password").val(clientStatus.password != null ? clientStatus.password : "doshows123");
			settingDialogDiv.find("#vpsBackendSystemComputerID").val(clientStatus.vpsBackendSystemComputerID != null ? clientStatus.vpsBackendSystemComputerID :
					"");
			settingDialogDiv.find("#vpsBackendSystemPassword").val(clientStatus.vpsBackendSystemPassword != null ? clientStatus.vpsBackendSystemPassword : "doshows123");
			settingDialogDiv[0].style.left = getTop(self); //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
			settingDialogDiv.show();
		}
		function saveChangeSetting(self){
			var settingDialogDiv = $("#changeSettingDialog");
			var clientStatus = {};
			clientStatus.clientID = settingDialogDiv.find("#settingClientID").val();
			clientStatus.group = settingDialogDiv.find("#settingGroup").val();
			clientStatus.operationType = settingDialogDiv.find("#settingOperationType").val();
			clientStatus.pageSize = settingDialogDiv.find("#pageSize").val();
			clientStatus.page = settingDialogDiv.find("#page").val();
			clientStatus.dragPercent  = settingDialogDiv.find("#dragPercent ").val();
			clientStatus.zhanneiPercent = settingDialogDiv.find("#zhanneiPercent").val();
			clientStatus.kuaizhaoPercent = settingDialogDiv.find("#kuaizhaoPercent").val();
			clientStatus.baiduSemPercent = settingDialogDiv.find("#baiduSemPercent").val();
			clientStatus.multiBrowser = settingDialogDiv.find("#multiBrowser").val();
			clientStatus.clearCookie = settingDialogDiv.find("#clearCookie").val();
			clientStatus.allowSwitchGroup = settingDialogDiv.find("#allowSwitchGroup").val();
			clientStatus.disableStatistics = settingDialogDiv.find("#disableStatistics").val();
			clientStatus.host = settingDialogDiv.find("#host").val();
			clientStatus.port = settingDialogDiv.find("#port").val();
			clientStatus.userName = settingDialogDiv.find("#userName").val();
			clientStatus.password = settingDialogDiv.find("#password").val();
			clientStatus.vpsBackendSystemComputerID = settingDialogDiv.find("#vpsBackendSystemComputerID").val();
			clientStatus.vpsBackendSystemPassword = settingDialogDiv.find("#vpsBackendSystemPassword").val();

			clientStatus.entryPageMinCount = settingDialogDiv.find("#entryPageMinCount").val();
			clientStatus.entryPageMaxCount = settingDialogDiv.find("#entryPageMaxCount").val();
			clientStatus.disableVisitWebsite = settingDialogDiv.find("#disableVisitWebsite").val();
			clientStatus.pageRemainMinTime = settingDialogDiv.find("#pageRemainMinTime").val();
			clientStatus.pageRemainMaxTime = settingDialogDiv.find("#pageRemainMaxTime").val();
			clientStatus.inputDelayMinTime = settingDialogDiv.find("#inputDelayMinTime").val();
			clientStatus.inputDelayMaxTime = settingDialogDiv.find("#inputDelayMaxTime").val();
			clientStatus.slideDelayMinTime = settingDialogDiv.find("#slideDelayMinTime").val();
			clientStatus.slideDelayMaxTime = settingDialogDiv.find("#slideDelayMaxTime").val();
			clientStatus.titleRemainMinTime = settingDialogDiv.find("#titleRemainMinTime").val();
			clientStatus.titleRemainMaxTime = settingDialogDiv.find("#titleRemainMaxTime").val();
			clientStatus.waitTimeAfterOpenBaidu = settingDialogDiv.find("#waitTimeAfterOpenBaidu").val();
			clientStatus.waitTimeBeforeClick = settingDialogDiv.find("#waitTimeBeforeClick").val();
			clientStatus.waitTimeAfterClick = settingDialogDiv.find("#waitTimeAfterClick").val();
			clientStatus.maxUserCount = settingDialogDiv.find("#maxUserCount").val();
			clientStatus.optimizeKeywordCountPerIP = settingDialogDiv.find("#optimizeKeywordCountPerIP").val();

//			clientStatus.disableVisitWebsite = settingDialogDiv.find("#disableVisitWebsite:checked").val() === '1' ? 1 : 0;
			clientStatus.oneIPOneUser = settingDialogDiv.find("#oneIPOneUser:checked").val() === '1' ? 1 : 0;
			clientStatus.randomlyClickNoResult = settingDialogDiv.find("#randomlyClickNoResult:checked").val() === '1' ? 1 : 0;
			clientStatus.justVisitSelfPage = settingDialogDiv.find("#justVisitSelfPage:checked").val() === '1' ? 1 : 0;
			clientStatus.sleepPer2Words = settingDialogDiv.find("#sleepPer2Words:checked").val() === '1' ? 1 : 0;
			clientStatus.supportPaste = settingDialogDiv.find("#supportPaste:checked").val() === '1' ? 1 : 0;
			clientStatus.moveRandomly = settingDialogDiv.find("#moveRandomly:checked").val() === '1' ? 1 : 0;
			clientStatus.parentSearchEntry = settingDialogDiv.find("#parentSearchEntry:checked").val() === '1' ? 1 : 0;
			clientStatus.clearLocalStorage = settingDialogDiv.find("#clearLocalStorage:checked").val() === '1' ? 1 : 0;
			clientStatus.lessClickAtNight = settingDialogDiv.find("#lessClickAtNight:checked").val() === '1' ? 1 : 0;
			clientStatus.sameCityUser = settingDialogDiv.find("#sameCityUser:checked").val() === '1' ? 1 : 0;
			clientStatus.locateTitlePosition = settingDialogDiv.find("#locateTitlePosition:checked").val() === '1' ? 1 : 0;
			clientStatus.baiduAllianceEntry = settingDialogDiv.find("#baiduAllianceEntry:checked").val() === '1' ? 1 : 0;
			clientStatus.justClickSpecifiedTitle = settingDialogDiv.find("#justClickSpecifiedTitle:checked").val() === '1' ? 1 : 0;
			clientStatus.randomlyClickMoreLink = settingDialogDiv.find("#randomlyClickMoreLink:checked").val() === '1' ? 1 : 0;
			clientStatus.moveUp20 = settingDialogDiv.find("#moveUp20:checked").val() === '1' ? 1 : 0;

			$.ajax({
		        url: '/internal/clientstatus/addClientStatus',
		        data: JSON.stringify(clientStatus),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
		        success: function (result) {
		        	if(result){
                        settingDialogDiv.hide();
                        showInfo("更新成功！", self);
                        window.location.reload();
                    }else{
                        settingDialogDiv.hide();
                        showInfo("更新失败！", self);
		        	}
                    $(this).dialog("close");
		        },
		        error: function () {
                    settingDialogDiv.hide();
                    showInfo("更新失败！", self);
		        }
		    });
		}
		
		function showTargetVersionSettingDialog(self){
            $("#targetVersionSettingDialog").find("#settingTargetVersion").val("");
            $("#targetVersionSettingDialog").dialog({
                resizable: false,
				title: "设定目标版本",
                width: 240,
                modal: true,
                buttons: {
                    "保存": function () {
                        saveTargetVersionSetting(this);
                    },
                    "取消": function () {
                        $(this).dialog("close");
                    }
                }
            });
		}
		function saveTargetVersionSetting(self){
			var settingDialogDiv = $("#targetVersionSettingDialog");
			var clientStatus = {};
			clientStatus.clientIDs = getSelectedClientIDs();
			clientStatus.targetVersion = settingDialogDiv.find("#settingTargetVersion").val();
			if(clientStatus.targetVersion.trim() === ''){
				alert("请输入目标版本！");
				return;
			}
			if(clientStatus.clientIDs.trim() === ''){
				alert("请选择要更新的终端！");
				return;
			}
		    $.ajax({
		        url: '/internal/clientstatus/updateClientStatusTargetVersion',
		        data: JSON.stringify(clientStatus),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
		        type: 'POST',
		        success: function (result) {
		        	if(result){
		        		settingDialogDiv.hide();
                        showInfo("更新成功！", self);
                        window.location.reload();
                    }else{
		        		settingDialogDiv.hide();
		        		showInfo("更新失败！", self);
		        	}
                    $(this).dialog("close");
		        },
		        error: function () {
                    settingDialogDiv.hide();
                    showInfo("更新失败！", self);
		        }
		    });
		}

		function showRenewalSettingDialog(self){
			$("#renewalSettingDialog").find("#renewalSettingDialog").val("");
            $("#renewalSettingDialog").dialog({
                resizable: false,
                title: "续费",
                width: 225,
                modal: true,
                buttons: {
                    "保存": function () {
                        saveRenewalSetting(this);
                    },
                    "取消": function () {
                        $(this).dialog("close");
                    }
                }
            });
		}
		function saveRenewalSetting(self){
			var settingDialogDiv = $("#renewalSettingDialog");
			var clientStatus = {};
            clientStatus.clientIDs = getSelectedClientIDs();
            clientStatus.settingType = settingDialogDiv.find("input[name=settingType]:checked").val();
            clientStatus.renewalDate = settingDialogDiv.find("#renewalDate").val();
			if(clientStatus.settingType.trim() === 'specificDate'){
				if(clientStatus.renewalDate.trim() === ''){
					alert("请输入日期！");
					return;
				}
				if(!isDate(clientStatus.renewalDate.trim())){
					alert("日期格式不对！");
					return;
				}
			}
			if(clientStatus.clientIDs.trim() === ''){
				alert("请先选择要更新的终端！");
				return;
			}
			$.ajax({
				url: '/internal/clientstatus/updateClientStatusRenewalDate',
				data: JSON.stringify(clientStatus),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
				type: 'POST',
				success: function (result) {
					if(result){
                        settingDialogDiv.hide();
                        showInfo("更新成功！", self);
                        window.location.reload();
                    }else{
                        settingDialogDiv.hide();
                        showInfo("更新失败！", self);
					}
                    $(this).dialog("close");
				},
				error: function () {
                    settingDialogDiv.hide();
                    showInfo("更新失败！", self);
				}
			});
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
	    $(document).ready(function(){
			if($("#showFetchKeywordStatus").attr("checked") === "checked"){
				$("span[name=invalidClient]").each(function(){
					var span = $(this);
					$.ajax({
						url: '/internal/customerKeyword/haveCustomerKeywordForOptimization',
                        data: JSON.stringify({"clientID": this.id.replace("span_", "")}),
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        timeout: 5000,
                        type: 'POST',
						success: function (result) {
							if(result){
								span.html("</br>取词正常");
								span.css("color", "green");
							}else{
								span.html("</br>取不到词");
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
		<script src="http://s84.cnzz.com/stat.php?id=4204660&web_id=4204660" language="JavaScript"></script>
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
									<c:choose>
										<c:when test="${terminalType eq 'PC'}">
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
											<option value="pc_kpm">pc_kpm</option>
										</c:when>
										<c:otherwise>
											<option value="m_pm">m_pm</option>
											<option value="m_xl">m_xl</option>
											<option value="m_xg">m_xg</option>
											<option value="m_pm_sm">m_pm_sm</option>
											<option value="m_xl2">m_xl2</option>
											<option value="m_kpm">m_kpm</option>
										</c:otherwise>
									</c:choose>
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
	
	<div id="targetVersionSettingDialog" style="display: none;">
		目标版本：<input type="text" name="settingTargetVersion" id="settingTargetVersion" />
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
		</table>
	</div>

	<div id="uploadVNCDialog" style="display: none;">
		<form method="post" id="uploadVNCForm" action="" enctype="multipart/form-data">
			<table width="100%" style="margin-top: 10px;margin-left: 10px">
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
									<input type="file" id="file" name="file" size=50 height="50px" style="width: 350px;">
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div style="display:none;">
		<form id="downloadVNCForm" action="/internal/clientstatus/downloadVNCFile" method="post"></form>
		<form id="downloadFullVNCForm" action="/internal/clientstatus/downloadFullVNCFile" method="post"></form>
	</div>
</body>
</html>

