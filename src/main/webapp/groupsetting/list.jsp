<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>分组设置</title>
	<script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" href="${staticPath}/groupsetting/groupsetting.css">
	<style>
		#changeSettingDialog, #updateGroupSettingDialog {
			display: none;
			margin: 5px 0px 0px 5px;
		}
		#changeSettingDialog tr, #updateGroupSettingDialog tr {
			height : 24px;
		}
		#changeSettingDialog th, #updateGroupSettingDialog th {
			text-align : right;
		}
		#changeSettingDialog select, #updateGroupSettingDialog select {
			width : 152px;
		}
		#changeSettingDialog input[type=text], #updateGroupSettingDialog input[type=text] {
			width : 152px;
		}
		#td_2 {
			margin-right: 10px;
		}
		#td_2 input[type=text] {
			width : 50px;
		}
	</style>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@include file="/menu.jsp" %>
	<div class="mytabs">
		<ul class="link">
			<li name="PC"><a href="javascript:;" onclick="checkTerminalType('PC', true)">PC</a></li>
			<li name="Phone"><a href="javascript:;" onclick="checkTerminalType('Phone', true)">Phone</a></li>
		</ul>
		<div class="conn">
			<ul>
				<li class="operationCombineName">
					操作组合名称: <input type="text" title="请输入操作组合名称" name="operationCombineName" placeholder="请输入操作组合名称" onkeydown="enterIn(event);" value="${groupSettingCriteria.operationCombineName}">
				</li>
				<li class="optimizedGroupName">
					优化组名: <input type="text" title="请输入优化分组名" name="optimizedGroupName" placeholder="请输入优化分组名" onkeydown="enterIn(event);" value="${groupSettingCriteria.optimizedGroupName}">
				</li>
				<li>
					<span>操作类型: </span>
					<span>
						<select name="operationType" onchange="changeOperationType();" style="width: 150px;">
                            <option value=""></option>
							<c:forEach items="${operationTypeValues}" var="operationType">
								<c:choose>
									<c:when test="${operationType eq groupSettingCriteria.operationType}">
                                        <option selected>${operationType}</option>
                                    </c:when>
									<c:otherwise>
                                        <option>${operationType}</option>
                                    </c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</span>
				</li>
				<li>
					<label name="hasOperation" title="有操作类型">
						<input type="checkbox" name="checkbox" <c:if test="${groupSettingCriteria.hasOperation == true}">checked</c:if>>
						有操作类型
					</label>
				</li>
				<li>
					<label name="noOperation" title="无操作类型">
						<input type="checkbox" name="checkbox" <c:if test="${groupSettingCriteria.hasOperation == false}">checked</c:if>>
						无操作类型
					</label>
				</li>
				<li>
					<label name="hasRemainingAccount" title="分组设置占比未分配完成">
						<input type="checkbox" name="checkbox" <c:if test="${groupSettingCriteria.hasRemainingAccount == true}">checked</c:if>>
						剩分组设置占比
					</label>
				</li>
				<shiro:hasPermission name="/internal/groupsetting/searchGroupSettings">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="trimSearchCondition('1')" value=" 搜索 " >&nbsp;
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/operationCombine/saveOperationCombine">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showOperationCombineDialog()" value=" 增加 " >&nbsp;
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/group/getAvailableOptimizationGroups">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="getAvailableOptimizationGroups()" value="查询需要添加的优化组">&nbsp;
					</li>
				</shiro:hasPermission>
			</ul>
		</div>
	</div>
</div>

<form method="post" id="chargeForm" action="/internal/groupsetting/searchGroupSettings">
	<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
	<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
	<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
	<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
	<input type="hidden" name="operationCombineName" id="operationCombineName" value="${groupSettingCriteria.operationCombineName}"/>
	<input type="hidden" name="optimizedGroupName" id="optimizedGroupName" value="${groupSettingCriteria.optimizedGroupName}"/>
	<input type="hidden" name="operationType" id="operationType" value="${groupSettingCriteria.operationType}"/>
	<input type="hidden" name="terminalType" id="terminalType" value="${groupSettingCriteria.terminalType}"/>
    <input type="hidden" name="hasOperation" id="hasOperation" value="${groupSettingCriteria.hasOperation}"/>
	<input type="hidden" name="hasRemainingAccount" id="hasRemainingAccount" value="${groupSettingCriteria.hasRemainingAccount}"/>
</form>

<div class="datalist">
	<div class="datalist-list">
		<ul>
			<c:forEach items="${page.records}" var="operationCombine">
				<li title="请把分组下的分组设置占比分配完成！">
					<div class="header">
						<input type="hidden" name="operationCombineUuid" value="${operationCombine.uuid}">
						<span class="operationCombineName" title="${operationCombine.operationCombineName}——${operationCombine.remainingAccount}%">
							<a href="javascript:;">${operationCombine.operationCombineName}</a>
						</span>
						<span class="userName"><a href="javascript:;"></a>${operationCombine.creator}</span>
                        <span class="maxInvalidCount" ondblclick="editMaxInvalidCountStr($(this).find('label.maxInvalidCountStr')[0], true)">
							<label>最大无效点击数:</label>&nbsp;&nbsp;<label class="maxInvalidCountStr" operationCombineId="${operationCombine.uuid}">${operationCombine.maxInvalidCount}</label>
						</span>
						<span><a href="javascript:;"></a>&nbsp;&nbsp;&nbsp;</span>
						<span class="groupNames" ondblclick="editGroupNameStr($(this).find('label.groupNameStr')[0], true, '${operationCombine.maxInvalidCount}')">
							<label>分组:&nbsp;&nbsp;</label><label class="groupNameStr">暂无</label>
						</span>
						<div class="handle">
							<a class="blue" href="javascript:showGroupQueueDialog('${operationCombine.uuid}', '${operationCombine.maxInvalidCount}')">分组详情</a>
							<shiro:hasPermission name="/internal/groupsetting/saveGroupSetting">
								<a class="blue" href="javascript:showGroupSettingDialog('add', '', '${operationCombine.operationCombineName}', '${operationCombine.remainingAccount}', '${operationCombine.uuid}')">新增操作组设置</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/group/updateGroup">
								<a class="blue" href="javascript:showUpdateGroupDialog('${operationCombine.uuid}', '${operationCombine.operationCombineName}')">批量修改操作组设置</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/group/delGroup">
								<a class="blue" href="javascript:delOperationCombine(${operationCombine.uuid})">删除操作组合</a>
							</shiro:hasPermission>
						</div>
					</div>
					<div class="body" listsize="${operationCombine.groupSettings.size()}">
						<c:if test="${operationCombine.groupSettings.size() > 0}">
							<div class="data-info-head">
								<div class="other-rank">
									<div class="row1">
										<div>
										<span>
											<a href="javascript:;">操作类型</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">分组设置占比</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">网站统计</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">目标网站</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">页数</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">每页条数</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">Cookie设置</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">进入页次数</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">刷多少个词换IP</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">没结果随机点</a>
										</span>
										</div>
										<div>
										<span>
											<a href="javascript:;">操作</a>
										</span>
										</div>
									</div>
								</div>
							</div>
						</c:if>
						<c:forEach items="${operationCombine.groupSettings}" var="groupSetting">
							<div class="data-info-wrap">
								<div class="other-rank">
									<div class="row">
										<div>
											<span class="line1">
												<a href="javascript:;" title="${groupSetting.operationType}">${groupSetting.operationType}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.machineUsedPercent == null ? 0 : groupSetting.machineUsedPercent}%</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.disableStatistics == 0 ? "开放" : "关闭"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.disableVisitWebsite == 0 ? "访问" : "不访问"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.page}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.pageSize == 0 ? "10" : groupSetting.pageSize == 1 ? "20" : "50"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;" title="${groupSetting.clearCookie == 0 ? "不清理Cookie" : groupSetting.clearCookie == 1 ? "每次都清理Cookie" : groupSetting.clearCookie == 2 ? "随机操作清理Cookie" : "N次操作清理Cookie"}">${groupSetting.clearCookie == 0 ? "不清理Cookie" : groupSetting.clearCookie == 1 ? "每次都清理Cookie" : groupSetting.clearCookie == 2 ? "随机操作清理Cookie" : "N次操作清理Cookie"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.entryPageMinCount} - ${groupSetting.entryPageMaxCount}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.optimizeKeywordCountPerIP}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.randomlyClickNoResult == 1 ? "是" : "否"}</a>
											</span>
										</div>
										<div>
											<span class="operation">
												<shiro:hasPermission name="/internal/groupsetting/updateGroupSetting">
													<a href="javascript:showGroupSettingDialog('update', '${groupSetting.uuid}', '${operationCombine.operationCombineName}', '${operationCombine.remainingAccount}', '${operationCombine.uuid}')" title="修改操作类型">修改</a>&nbsp;&nbsp;
												</shiro:hasPermission>
												<shiro:hasPermission name="/internal/groupsetting/delGroupSetting">
													<a href="javascript: void(0);" onclick="delGroupSetting(this, ${groupSetting.uuid})" title="删除操作类型">删除</a>
												</shiro:hasPermission>
											</span>
										</div>
									</div>
								</div>
							</div>
						</c:forEach>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
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
        <option>500</option>
        <option>1000</option>
	</select>
	</div>
</div>

<%--分组详情--%>
<div id="showGroupQueueDialog" class="easyui-dialog" style="display: none">
    <form id="showGroupQueueForm">
        <table cellpadding="10" style="font-size: 12px; background-color: white;border-collapse: collapse; width: 100%;">
            <thead style="position: absolute !important;top: 24px !important;width: 100% !important;">
                <tr>
                    <th colspan="2" style="text-align: left;">优化组名: <input type="text" title="请输入优化组名" name="optimizedGroupName" placeholder="请输入优化组名" style="width: 130px;">&nbsp;
                        <input type="hidden" name="operationCombineUuid" value="">
                        <input type="button" onclick="searchGroupsBelowOperationCombine();" value="搜索"  style="width: 34px;">
                    </th>
                </tr>
                <tr>
                    <th style="width: 20px;text-align: center;"><input type="checkbox" name="checkAllGroup" id="checkAllGroup" onclick="selectAllChecked(this, 'checkGroup');" checked='checked'></th>
                    <th>优化组名</th>
                </tr>
            </thead>
            <tbody style="position: absolute !important;bottom: 60px !important; width: 100% !important;height: 420px;overflow-y: scroll;">
            </tbody>
            <tfoot style="position: absolute !important;bottom: 45px !important; width: 100% !important;">
                <tr>
                    <th colspan="2" style="text-align: left;">新增优化组: <input type="text" placeholder="请正确输入:pc_pm,m_pm" name="newOptimizationGroups" style="width: 150px;"></th>
                </tr>
            </tfoot>
        </table>
    </form>
</div>
<%--增加、修改操作类型--%>
<div id="changeSettingDialog" class="easyui-dialog" style="display: none;left: 30%;">
	<form id="changeSettingDialogForm" onsubmit="return false">
		<table>
			<tr>
				<input type="hidden" name="operationCombineUuid" id="operationCombineUuid" value="" >
				<input type="hidden" name="groupSettingUuid" id="groupSettingUuid" value="" >
				<td style="vertical-align: top;">
					<table id="td_1" style="font-size:12px;">
                        <tr name="trItem" onclick="checkItem(this)">
                            <th>操作组合</th>
                            <td>
                                <input type="text" name="settingOperationCombineName" id="settingOperationCombineName"/>
                            </td>
                        </tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>分组</th>
							<td>
								<input type="text" name="settingGroup" id="settingGroup" disabled="disabled" placeholder="分组名称, 多个用逗号分隔" />
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>操作类型</th>
							<td>
								<select name="settingOperationType" id="settingOperationType">
									<option value=""></option>
									<c:forEach items="${operationTypeValues}" var="operationType">
										<option value="${operationType}">${operationType}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>分组设置占比</th>
							<td>
								<input type="hidden" name="remainAccount" id="remainAccount" value="100">
								<input type="hidden" name="currentAccount" id="currentAccount" value="0">
								<input type="text" name="machineUsedPercent" id="machineUsedPercent" value="0" onmouseover="showRemainingAccount(this)" onmouseout="hiddenRemainingAccount(this)" onkeyup="changeRemainingAccount(this)" style="width:152px;"/>%<br>
								<label style="display:none;">剩余<i>100</i>%</label>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>网站统计</th>
							<td>
								<select name="disableStatistics" id="disableStatistics">
									<option value="0">开放</option>
									<option value="1">关闭</option>
								</select>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>目标网站</th>
							<td>
								<select name="disableVisitWebsite" id="disableVisitWebsite">
									<option value="0">访问</option>
									<option value="1">不访问</option>
								</select>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
							<th>每页</th>
							<td>
								<select name="pageSize" id="pageSize">
									<option value="0">10</option>
									<option value="1">20</option>
									<option value="2">50</option>
								</select>条
							</td>
						</tr>
					</table>
				</td>
				<td style="vertical-align:top;">
					<table id="td_2" style="font-size:12px;">
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
							<th>进入页</th>
							<td>
								<input type="text" name="entryPageMinCount" id="entryPageMinCount" value="0" /> - <input type="text" name="entryPageMaxCount" id="entryPageMaxCount" value="0" />次
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>页面停留</th>
							<td>
								<input type="text" name="pageRemainMinTime" id="pageRemainMinTime" value="3000"/> - <input type="text" name="pageRemainMaxTime" id="pageRemainMaxTime" value="5000"/>毫秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>输入延时</th>
							<td>
								<input type="text" name="inputDelayMinTime" id="inputDelayMinTime" value="50"/> - <input type="text" name="inputDelayMaxTime" id="inputDelayMaxTime" value="80"/>毫秒
							</td>
						</tr>

						<tr name="trItem" onclick="checkItem(this)">
							<th>滑动延时</th>
							<td>
								<input type="text" name="slideDelayMinTime" id="slideDelayMinTime" value="700"/> -
								<input type="text" name="slideDelayMaxTime" id="slideDelayMaxTime" value="1500"/>毫秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>标题停留</th>
							<td>
								<input type="text" name="titleRemainMinTime" id="titleRemainMinTime" value="1000"/> - <input type="text" name="titleRemainMaxTime" id="titleRemainMaxTime" value="3000"/>毫秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>刷多少</th>
							<td>
								<input type="text" name="optimizeKeywordCountPerIP" id="optimizeKeywordCountPerIP" value="1"/>个词换IP
							</td>
						</tr>
                        <tr name="trItem" onclick="checkItem(this)">
                            <th>无效点击数限制</th>
                            <td>
                                <input type="text" name="maxInvalidCount" id="maxInvalidCount" value="${maxInvalidCount}"/>
                            </td>
                        </tr>
					</table>
				</td>
				<td style="vertical-align:top;">
					<table id="td_4" style="font-size:12px">
						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="randomlyClickNoResult" name="randomlyClickNoResult" type="checkbox" value="1">没结果则随机点</input>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</div>
<%--批量修改优化组设置信息--%>
<div id="updateGroupSettingDialog" class="easyui-dialog" style="display: none; left: 30%;">
	<form id="updateGroupSettingDialogForm" onsubmit="return false">
		<table>
			<tr>
				<input type="hidden" name="groupUuid" id="groupUuid" value="" >
				<input type="hidden" name="groupSettingUuid" id="groupSettingUuid" value="" >
				<td>
					<table id="td_1" style="font-size:12px">
                        <tr name="trItem" onclick="checkItem(this)">
                            <th>操作组合</th>
                            <td>
                                <input type="text" name="settingOperationCombineName" id="settingOperationCombineName"/>
                            </td>
                        </tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>操作类型</th>
							<td>
								<select name="settingOperationType" id="settingOperationType">
									<c:forEach items="${operationTypeValues}" var="operationType">
										<option value="${operationType}">${operationType}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>分组设置占比</th>
							<td>
								<input type="hidden" name="remainAccount" id="remainAccount" value="100">
								<input type="hidden" name="currentAccount" id="currentAccount" value="0">
								<input type="text" name="machineUsedPercent" id="machineUsedPercent" value="0" onmouseover="showRemainingAccount(this)" onmouseout="hiddenRemainingAccount(this)" onkeyup="changeRemainingAccount(this)" style="width:152px;"/>%<br>
								<label style="display:none;">剩余<i>100</i>%</label>&nbsp;<label style="display:none;">修改<strong>2</strong>种类型</label>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>网站统计</th>
							<td>
								<select name="disableStatistics" id="disableStatistics">
									<option value="0">开放</option>
									<option value="1">关闭</option>
								</select>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>目标网站</th>
							<td>
								<select name="disableVisitWebsite" id="disableVisitWebsite">
									<option value="0">访问</option>
									<option value="1">不访问</option>
								</select>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
							<th>每页</th>
							<td>
								<select name="pageSize" id="pageSize">
									<option value="0">10</option>
									<option value="1">20</option>
									<option value="2">50</option>
								</select>条
							</td>
						</tr>
					</table>
				</td>
				<td style="vertical-align:top;">
					<table id="td_2" style="font-size:12px">
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
							<th>进入页</th>
							<td>
								<input type="text" name="entryPageMinCount" id="entryPageMinCount" value="0" /> - <input type="text" name="entryPageMaxCount" id="entryPageMaxCount" value="0" />次
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>页面停留</th>
							<td>
								<input type="text" name="pageRemainMinTime" id="pageRemainMinTime" value="3000"/> - <input type="text" name="pageRemainMaxTime" id="pageRemainMaxTime" value="5000"/>毫秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>输入延时</th>
							<td>
								<input type="text" name="inputDelayMinTime" id="inputDelayMinTime" value="50"/> - <input type="text" name="inputDelayMaxTime" id="inputDelayMaxTime" value="80"/>毫秒
							</td>
						</tr>

						<tr name="trItem" onclick="checkItem(this)">
							<th>滑动延时</th>
							<td>
								<input type="text" name="slideDelayMinTime" id="slideDelayMinTime" value="700"/> -
								<input type="text" name="slideDelayMaxTime" id="slideDelayMaxTime" value="1500"/>毫秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>标题停留</th>
							<td>
								<input type="text" name="titleRemainMinTime" id="titleRemainMinTime" value="1000"/> - <input type="text" name="titleRemainMaxTime" id="titleRemainMaxTime" value="3000"/>毫秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>刷多少</th>
							<td>
								<input type="text" name="optimizeKeywordCountPerIP" id="optimizeKeywordCountPerIP" value="1"/>个词换IP
							</td>
						</tr>
                        <tr name="trItem" onclick="checkItem(this)">
                            <th>无效点击数限制</th>
                            <td>
                                <input type="text" name="maxInvalidCount" id="maxInvalidCount" value="${maxInvalidCount}"/>
                            </td>
                        </tr>
					</table>
				</td>
				<td style="vertical-align:top;">
					<table id="td_4" style="font-size:12px">
						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="randomlyClickNoResult" name="randomlyClickNoResult" type="checkbox" value="1">没结果则随机点</input>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
</div>
<%--查询需要添加的优化组--%>
<div id="getAvailableOptimizationGroups" class="easyui-dialog" style="display: none">
	<table cellpadding="10" style="font-size: 12px; background-color: white;border-collapse: collapse; width: 100%;">
        <thead style="background-color: #eeeeee !important;position: absolute !important;top: 24px !important;width: 100% !important;">
            <tr>
                <th colspan="2" style="text-align: left;">组名来源：<input type="radio" name="sourceRadio" value="true">关键字
                    &nbsp;&nbsp;&nbsp;<input type="radio" name="sourceRadio" value="false">全站
                </th>
            </tr>
            <tr>
                <th colspan="2" style="text-align: left;">优化组名：<input type="text" title="请输入优化组名" name="optimizedGroupName" placeholder="请输入优化组名" style="width: 100px;">&nbsp;
                    <input  type="button" onclick="searchAvailableOptimizationGroups()" value="搜索"  style="width: 45px;">
                </th>
            </tr>
			<tr>
				<th style="width: 15px;text-align: center;"><input type="checkbox" name="checkAllOptimizationGroup" id="checkAllOptimizationGroup" onclick="selectAllChecked(this, 'checkOptimizationGroup');" checked='checked'></th>
				<th>优化组</th>
			</tr>
        </thead>
		<tbody style="position: absolute !important;bottom: 60px !important;background-color: #eeeeee !important;width: 100% !important;height: 309px;overflow-y: scroll;">
        </tbody>
        <tfoot style="position: absolute !important;bottom: 40px !important;background-color: #eeeeee !important;width: 100% !important;">
            <tr>
                <th colspan="2" style="text-align: left;">操作组合：<select name="operationCombineName" style="width: 150px;">
                    <c:forEach items="${operationCombineNames}" var="operationCombineName">
                        <c:choose>
                            <c:when test="${operationCombineName eq groupSettingCriteria.operationCombineName}"><option selected>${operationCombineName}</option></c:when>
                            <c:otherwise><option>${operationCombineName}</option></c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select></th>
            </tr>
        </tfoot>
    </table>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/groupsetting/groupsetting.js"></script>
<script language="javascript">
    var dateStr = new Date(); // 当天日期
    var m = dateStr.getMonth() + 1 < 10 ? "0" + (dateStr.getMonth() + 1) : (dateStr.getMonth() + 1);
    var d = dateStr.getDate() < 10 ? "0" + dateStr.getDate() : dateStr.getDate();
    var today = dateStr.getFullYear() + "-" + m + "-" + d;
    $(function () {
        $(".datalist").css("margin-top", $("#topDiv").height()+15);
        window.onresize = function(){
            $(".datalist").css("margin-top", $("#topDiv").height()+15);
        }
    });
</script>
</body>
</html>