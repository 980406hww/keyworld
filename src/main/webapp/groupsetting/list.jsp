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

		#td_2 input[type=text] {
			width : 50px;
		}
		#groupSettingVos tr a {
			margin: 0px 0px 0px 10px;
		}
	</style>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@include file="/menu.jsp" %>
	<div class="mytabs">
		<ul class="link">
			<li name="PC"><a href="javascript:;" onclick="checkTerminalType('PC', true)">百度PC</a></li>
			<li name="Phone"><a href="javascript:;" onclick="checkTerminalType('Phone', true)">百度Phone</a></li>
		</ul>
		<div class="conn">
			<ul>
				<li>
					优化组名: <input type="text" title="请输入优化分组名" name="optimizedGroupName" placeholder="请输入优化分组名" onkeydown="enterIn(event);" value="${groupSettingCriteria.optimizedGroupName}">
				</li>
				<li>
					<span>操作类型: </span>
					<span>
						<select name="operationType" style="width: 150px;">
							<c:forEach items="${operationTypeValues}" var="operationType">
								<c:choose>
									<c:when test="${operationType eq groupSettingCriteria.operationType}"><option selected>${operationType}</option></c:when>
									<c:otherwise><option>${operationType}</option></c:otherwise>
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
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="trimSearchCondition('1')" value=" 搜索 " >&nbsp;
				</li>
				<li>
					<shiro:hasPermission name="/internal/group/saveGroup">
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showGroupDialog()" value=" 增加 " >&nbsp;
					</shiro:hasPermission>
				</li>
			</ul>
		</div>
	</div>
</div>

<form method="post" id="chargeForm" action="/internal/groupsetting/searchGroupSettings">
	<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
	<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
	<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
	<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
	<input type="hidden" name="optimizedGroupName" id="optimizedGroupName" value="${groupSettingCriteria.optimizedGroupName}"/>
	<input type="hidden" name="operationType" id="operationType" value="${groupSettingCriteria.operationType}"/>
	<input type="hidden" name="terminalType" id="terminalType" value="${groupSettingCriteria.terminalType}"/>
	<input type="hidden" name="hasOperation" id="hasOperation" value="${groupSettingCriteria.hasOperation}"/>
</form>

<div class="datalist">
	<div class="datalist-list">
		<ul>
			<c:forEach items="${page.records}" var="groupVo" varStatus="status">
				<li>
					<div class="header">
						<input type="hidden" name="groupUuid" value="${groupVo.uuid}">
						<span class="groupName" title="${groupVo.groupName}"><a href="javascript:;">${groupVo.groupName}</a></span>
						<span class="userName" title="${groupVo.userName}"><a href="javascript:;"></a>${groupVo.userName}</span>
						<div class="handle">
							<shiro:hasPermission name="/internal/groupsetting/saveGroupSetting">
								<a class="blue" href="javascript:showGroupSettingDialog('add', '${groupVo.uuid}', '${groupVo.groupName}')">新增操作类型</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/groupsetting/updateGroupSetting">
								<a class="blue" href="javascript:showUpdateGroupDialog('${groupVo.uuid}', '${groupVo.groupName}')">修改优化分组</a>
							</shiro:hasPermission>
							<shiro:hasPermission name="/internal/group/delGroup">
								<a class="blue" href="javascript:delGroup(${groupVo.uuid})">删除优化分组</a>
							</shiro:hasPermission>
						</div>
					</div>

					<div class="body" listsize="${groupVo.groupSettings.size()}">
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
											<a href="javascript:;">站内搜索比例</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">外链检索比例</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">快照点击比例</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">竞价点击比例</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">标题拖动比例</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">特殊字符比例</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">浏览器设置</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">Cookie设置</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">最大用户数</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">进入页次数</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">每IP对每用户</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">在域名下访问</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">没结果随机点</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">支持粘贴输入</a>
										</span>
									</div>
									<div>
										<span>
											<a href="javascript:;">清除本地存储</a>
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
						<c:forEach items="${groupVo.groupSettings}" var="groupSetting">
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
												<a href="javascript:;">${groupSetting.disableStatistics == 1 ? "开放" : "关闭"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.disableVisitWebsite == 1 ? "访问" : "不访问"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.pageNo}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.pageSize}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.zhanneiPercent == 0 ? '0%' : groupSetting.zhanneiPercent == 1 ? '10%' : groupSetting.zhanneiPercent == 2 ? '30%' : groupSetting.zhanneiPercent == 3 ? '50%' : groupSetting.zhanneiPercent == 4 ? '100%' : '无'}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.zhanwaiPercent == 0 ? '0%' : groupSetting.zhanwaiPercent == 1 ? '10%' : groupSetting.zhanwaiPercent == 2 ? '30%' : groupSetting.zhanwaiPercent == 3 ? '50%' : groupSetting.zhanwaiPercent == 4 ? '100%' : '无'}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.kuaizhaoPercent == 0 ? '0%' : groupSetting.kuaizhaoPercent == 1 ? '10%' : groupSetting.kuaizhaoPercent == 2 ? '30%' : groupSetting.kuaizhaoPercent == 3 ? '50%' : groupSetting.kuaizhaoPercent == 4 ? '100%' : '无'}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.baiduSemPercent == 0 ? '0%' : groupSetting.baiduSemPercent == 1 ? '10%' : groupSetting.baiduSemPercent == 2 ? '30%' : groupSetting.baiduSemPercent == 3 ? '50%' : groupSetting.baiduSemPercent == 4 ? '100%' : '无'}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.dragPercent == 0 ? '0%' : groupSetting.dragPercent == 1 ? '10%' : groupSetting.dragPercent == 2 ? '30%' : groupSetting.dragPercent == 3 ? '50%' : groupSetting.dragPercent == 4 ? '100%' : '无'}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.specialCharPercent == 0 ? '0%' : groupSetting.specialCharPercent == 1 ? '10%' : groupSetting.specialCharPercent == 2 ? '30%' : groupSetting.specialCharPercent == 3 ? '50%' : groupSetting.specialCharPercent == null ? '无' : '100%'}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;" title="${groupSetting.multiBrowser == 0 ? "命令多浏览器" : groupSetting.multiBrowser == 1 ? "模拟多浏览器" : "单个浏览器"}">${groupSetting.multiBrowser == 0 ? "命令多浏览器" : groupSetting.multiBrowser == 1 ? "模拟多浏览器" : "单个浏览器"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;" title="${groupSetting.clearCookie == 0 ? "不清理Cookie" : groupSetting.clearCookie == 1 ? "每次都清理Cookie" : groupSetting.clearCookie == 2 ? "随机操作清理Cookie" : "N次操作清理Cookie"}">${groupSetting.clearCookie == 0 ? "不清理Cookie" : groupSetting.clearCookie == 1 ? "每次都清理Cookie" : groupSetting.clearCookie == 2 ? "随机操作清理Cookie" : "N次操作清理Cookie"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.maxUserCount}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.entryPageMinCount} - ${groupSetting.entryPageMaxCount}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.oneIPOneUser == 1 ? "是" : "否"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.justVisitSelfPage == 1 ? "是" : "否"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.randomlyClickNoResult == 1 ? "是" : "否"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.supportPaste == 1 ? "是" : "否"}</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.clearLocalStorage == 1 ? "是" : "否"}</a>
											</span>
										</div>
										<div>
											<span class="operation">
												<shiro:hasPermission name="/internal/groupsetting/updateGroupSetting">
													<a href="javascript:showGroupSettingDialog('update', '${groupSetting.uuid}', '${groupVo.groupName}')" title="修改操作类型">修改</a>
												</shiro:hasPermission>
												<shiro:hasPermission name="/internal/groupsetting/delGroupSetting">
													<a href="javascript:delGroupSetting(${groupSetting.uuid})" title="删除操作类型">删除</a>
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

<%--增加、修改操作类型--%>
<div id="changeSettingDialog" class="easyui-dialog" style="display: none;left: 30%;">
	<form id="changeSettingDialogForm" onsubmit="return false">
		<table>
			<tr>
				<input type="hidden" name="groupUuid" id="groupUuid" value="" >
				<input type="hidden" name="groupSettingUuid" id="groupSettingUuid" value="" >
				<td>
					<table id="td_1" style="font-size:12px">
						<tr name="trItem" onclick="checkItem(this)">
							<th>分组</th>
							<td>
								<input type="text" name="settingGroup" id="settingGroup"/>
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
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
							<th>特殊字符比例</th>
							<td>
								<select name="specialCharPercent" id="specialCharPercent">
									<option value="0">0%</option>
									<option value="1">10%</option>
									<option value="2">30%</option>
									<option value="3">50%</option>
									<option value="4">100%</option>
								</select>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
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
					</table>
				</td>

				<td style="vertical-align:top;">
					<table id="td_2" style="font-size:12px">
						<tr name="trItem" onclick="checkItem(this)">
							<th>浏览器设置</th>
							<td>
								<select name="multiBrowser" id="multiBrowser">
									<option value="0">命令多浏览器</option>
									<option value="1"  selected>模拟多浏览器</option>
									<option value="2">单个浏览器</option>
								</select>
							</td>
						</tr>
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
							<th>最大用户数</th>
							<td>
								<input type="text" name="maxUserCount" id="maxUserCount" value="300"  style="width:110px;"/>
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
							<th>打开百度等待</th>
							<td>
								<input type="text" name="waitTimeAfterOpenBaidu" id="waitTimeAfterOpenBaidu" value="1000"/>秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>点击目标等待</th>
							<td>
								<input type="text" name="waitTimeBeforeClick" id="waitTimeBeforeClick" value="1000"/>秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>点击目标后等待</th>
							<td>
								<input type="text" name="waitTimeAfterClick" id="waitTimeAfterClick" value="5000"/>秒
							</td>
						</tr>
					</table>
				</td>

				<td style="vertical-align:top;">
					<table id="td_2" style="font-size:12px">
						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="oneIPOneUser" name="oneIPOneUser" type="checkbox" value="1">每IP对每用户</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="randomlyClickNoResult" name="randomlyClickNoResult" type="checkbox" value="1">没结果则随机点</input>
							</td>
						</tr>
						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="justVisitSelfPage" name="justVisitSelfPage" type="checkbox" value="1">在域名下访问</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="sleepPer2Words" name="sleepPer2Words" type="checkbox" value="1">输入2字稍微停顿</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="supportPaste" name="supportPaste" type="checkbox" value="1">支持粘贴输入</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="moveRandomly" name="moveRandomly" type="checkbox" value="1">随机移动</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="parentSearchEntry" name="parentSearchEntry" type="checkbox" value="1">爸妈搜索入口</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="clearLocalStorage" name="clearLocalStorage" type="checkbox" value="1">清除LocalStorage</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="lessClickAtNight" name="lessClickAtNight" type="checkbox" value="1">晚上减少点击</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="sameCityUser" name="sameCityUser" type="checkbox" value="1">同城用户</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="locateTitlePosition" name="locateTitlePosition" type="checkbox" value="1">直接获取标题位置</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="baiduAllianceEntry" name="baiduAllianceEntry" type="checkbox" value="1">百度联盟入口</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="justClickSpecifiedTitle" name="justClickSpecifiedTitle" type="checkbox" value="1">随机只点指定标题</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="randomlyClickMoreLink" name="randomlyClickMoreLink" type="checkbox" value="1">随机多点一些链接</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="moveUp20" name="moveUp20" type="checkbox" value="1">向上偏移20</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="optimizeRelatedKeyword" name="optimizeRelatedKeyword" type="checkbox" value="1">操作相关词</input>
							</td>
						</tr>
					</table>
				</td>

			</tr>
		</table>
	</form>
</div>
<%--修改优化分组的操作类型--%>
<div id="updateGroupSettingDialog" class="easyui-dialog" style="display: none; left: 30%;">
	<form id="updateGroupSettingDialogForm" onsubmit="return false">
		<table id="groupSettingVos">
			<tr></tr>
		</table>
		<table>
			<tr>
				<input type="hidden" name="groupUuid" id="groupUuid" value="" >
				<input type="hidden" name="groupSettingUuid" id="groupSettingUuid" value="" >
				<td>
					<table id="td_1" style="font-size:12px">
						<tr name="trItem" onclick="checkItem(this)">
							<th>分组</th>
							<td>
								<input type="text" name="settingGroup" id="settingGroup" />
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
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
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
						<tr name="trItem" onclick="checkItem(this)">
							<th>特殊字符比例</th>
							<td>
								<select name="specialCharPercent" id="specialCharPercent">
									<option value="0">0%</option>
									<option value="1">10%</option>
									<option value="2">30%</option>
									<option value="3">50%</option>
									<option value="4">100%</option>
								</select>
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
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
					</table>
				</td>

				<td style="vertical-align:top;">
					<table id="td_2" style="font-size:12px">
						<tr name="trItem" onclick="checkItem(this)">
							<th>浏览器设置</th>
							<td>
								<select name="multiBrowser" id="multiBrowser">
									<option value="0">命令多浏览器</option>
									<option value="1"  selected>模拟多浏览器</option>
									<option value="2">单个浏览器</option>
								</select>
							</td>
						</tr>
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
							<th>最大用户数</th>
							<td>
								<input type="text" name="maxUserCount" id="maxUserCount" value="300"  style="width:110px;"/>
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
							<th>打开百度等待</th>
							<td>
								<input type="text" name="waitTimeAfterOpenBaidu" id="waitTimeAfterOpenBaidu" value="1000"/>秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>点击目标等待</th>
							<td>
								<input type="text" name="waitTimeBeforeClick" id="waitTimeBeforeClick" value="1000"/>秒
							</td>
						</tr>
						<tr name="trItem" onclick="checkItem(this)">
							<th>点击目标后等待</th>
							<td>
								<input type="text" name="waitTimeAfterClick" id="waitTimeAfterClick" value="5000"/>秒
							</td>
						</tr>
					</table>
				</td>

				<td style="vertical-align:top;">
					<table id="td_2" style="font-size:12px">
						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="oneIPOneUser" name="oneIPOneUser" type="checkbox" value="1">每IP对每用户</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="randomlyClickNoResult" name="randomlyClickNoResult" type="checkbox" value="1">没结果则随机点</input>
							</td>
						</tr>
						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="justVisitSelfPage" name="justVisitSelfPage" type="checkbox" value="1">在域名下访问</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="sleepPer2Words" name="sleepPer2Words" type="checkbox" value="1">输入2字稍微停顿</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="supportPaste" name="supportPaste" type="checkbox" value="1">支持粘贴输入</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="moveRandomly" name="moveRandomly" type="checkbox" value="1">随机移动</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="parentSearchEntry" name="parentSearchEntry" type="checkbox" value="1">爸妈搜索入口</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="clearLocalStorage" name="clearLocalStorage" type="checkbox" value="1">清除LocalStorage</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="lessClickAtNight" name="lessClickAtNight" type="checkbox" value="1">晚上减少点击</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="sameCityUser" name="sameCityUser" type="checkbox" value="1">同城用户</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="locateTitlePosition" name="locateTitlePosition" type="checkbox" value="1">直接获取标题位置</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="baiduAllianceEntry" name="baiduAllianceEntry" type="checkbox" value="1">百度联盟入口</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="justClickSpecifiedTitle" name="justClickSpecifiedTitle" type="checkbox" value="1">随机只点指定标题</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="randomlyClickMoreLink" name="randomlyClickMoreLink" type="checkbox" value="1">随机多点一些链接</input>
							</td>
						</tr>

						<tr>
							<td name="trItem" onclick="checkItem(this)">
								<input id="moveUp20" name="moveUp20" type="checkbox" value="1">向上偏移20</input>
							</td>
							<td name="trItem" onclick="checkItem(this)">
								<input id="optimizeRelatedKeyword" name="optimizeRelatedKeyword" type="checkbox" value="1">操作相关词</input>
							</td>
						</tr>
					</table>
				</td>
			</tr>
		</table>
	</form>
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