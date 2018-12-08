<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>全站设置</title>
	<style>
		#changeSettingDialog {
			font-size: 12px;
			line-height: 12px;
			width: 340px;
			height: 250px;
		}
		#chargeDialogTable {
			font-size:12px;
			margin: 10px 10px 0px 30px;
		}
		#chargeDialog {
			width: 375px;
			height: 145px;
			font-size: 12px;
			line-height: 12px;
		}
		#PCChargeInfo {
			display: none;
		}
		#PhoneChargeInfo {
			display: none;
		}
	</style>
	<script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" href="${staticPath}/qzsetting/base.css">
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@include file="/menu.jsp" %>
</div>

<div class="mytabs">
	<ul class="link">
		<li class="active"><a href="javascript:;" onclick="checkTerminalType(this, 'PC')">百度PC</a></li>
		<li class=""><a href="javascript:;" onclick="checkTerminalType(this, 'Phone')">百度移动</a></li>
	</ul>
	<div class="conn">
		<ul>
			<li>
				<div class="search-wrap">
					<input type="text" title="请输入网站域名" placeholder="请输入网站域名" value="${qzSettingSearchCriteria.domain}">
					<a href="javascript:;" onclick="trimSearchCondition('1');">搜索</a>
				</div>
			</li>

			<li>
				<a href="javascript:;" onclick="showMoreSearchCondition();">更多搜索条件</a>
			</li>
			<shiro:hasPermission name="/internal/qzsetting/save">
				<li>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showSettingDialog(null, this)" value=" 增加 " >
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/internal/qzsetting/updateImmediately">
				<li>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateImmediately(this)" value=" 马上更新 " >
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/internal/qzsetting/updateStatus">
				<li>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateQZSettingStatus(0)" value=" 暂停整站 " >
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateQZSettingStatus(1)" value=" 激活整站 " >
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/internal/qzsetting/deleteQZSettings">
				<li>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="delSelectedQZSettings(this)" value=" 删除所选 " >
				</li>
			</shiro:hasPermission>
			<shiro:hasPermission name="/internal/qzsetting/searchQZSettings">
				<li>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="getAvailableQZSettings()" value="查看更新队列(${availableQZSettingCount})">
				</li>
			</shiro:hasPermission>
			<li>
				<label title="网站关键词一星期排名趋势涨幅&gt;0.0">
					<input type="checkbox">
					<i class="icon-rank-down"></i>骤降 (0)
				</label>
			</li>
			<li>
				<label title="网站关键词一星期排名趋势涨幅&lt;0.0">
					<input type="checkbox">
					<i class="icon-rank-up"></i>暴涨 (0)
				</label>
			</li>
		</ul>
	</div>
	<div class="conn" name="moreSearchCondition" style="display: none;">
		<ul>
			<li class="condition">
				<span>客户: </span>
				<input type="text" list="customer_list" name="customerInfo" value="${qzSettingSearchCriteria.customerInfo}">
			</li>
			<li class="condition">
				<span>组名: </span>
				<input type="text" name="group" value="${qzSettingSearchCriteria.group}" style="width:150px;">
			</li>
			<li>
				<span>状态: </span>
				<select name="status">
					<option value="" <c:if test="${qzSettingSearchCriteria.status == null}">selected</c:if>></option>
					<option value="1" <c:if test="${qzSettingSearchCriteria.status == 1}">selected</c:if>>激活</option>
					<option value="0" <c:if test="${qzSettingSearchCriteria.status == 0}">selected</c:if>>暂停</option>
					<option value="2" <c:if test="${qzSettingSearchCriteria.status == 2}">selected</c:if>>新增</option>
				</select>
			</li>
			<li>
				<span>更新状态: </span>
				<select name="updateStatus">
					<c:forEach items="${statusList}" var="status">
						<c:choose>
							<c:when test="${status eq qzSettingSearchCriteria.updateStatus}"><option selected>${status}</option></c:when>
							<c:otherwise><option>${status}</option></c:otherwise>
						</c:choose>
					</c:forEach>
				</select>
			</li>
			<shiro:hasPermission name="/internal/qzsetting/searchQZSettings">
				<li><a href="javascript:resetSearchCondition('-1')">过期未收费(${chargeRemindDataMap['expiredChargeSize']})</a></li>
				<li><a target="_blank" href="javascript:resetSearchCondition('0')">当天收费提醒(${chargeRemindDataMap['nowChargeSize']})</a></li>
				<li><a target="_blank" href="javascript:resetSearchCondition('3')">三天收费提醒(${chargeRemindDataMap['threeChargeSize']})</a></li>
				<li><a target="_blank" href="javascript:resetSearchCondition('7')">七天收费提醒(${chargeRemindDataMap['sevenChargeSize']})</a></li>
			</shiro:hasPermission>
		</ul>
	</div>
</div>

<form method="post" id="chargeForm" action="/internal/qzsetting/searchQZSettings">
	<input type="hidden" name="dateRangeType" id="dateRangeType" value="${qzSettingSearchCriteria.dateRangeType}"/>
	<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
	<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
	<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
	<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
	<input type="hidden" name="domain" id="domain" value="${qzSettingSearchCriteria.domain}">
	<input type="hidden" name="group" id="group" value="${qzSettingSearchCriteria.group}">
	<input type="hidden" name="customerUuid" id="customerUuid" value="${qzSettingSearchCriteria.customerUuid}"/>
	<input type="hidden" name="statusHidden" id="statusHidden" value="${qzSettingSearchCriteria.status}"/>
	<input type="hidden" name="customerInfo" id="customerInfo" value="${qzSettingSearchCriteria.customerInfo}">
	<input type="hidden" name="status" id="status" value="${qzSettingSearchCriteria.status}"/>
	<input type="hidden" name="updateStatus" id="updateStatus" value="${qzSettingSearchCriteria.updateStatus}"/>
</form>


<div class="datalist">
	<div class="datalist-list">
		<ul>
			<c:forEach items="${page.records}" var="qzSetting" varStatus="status">
				<c:if test="${qzSetting.pcGroup != null}">
					<li class="pcGroup">
						<div class="header">
							<span>
								<input type="checkbox" name="uuid" value="${qzSetting.uuid}" onclick="decideSelectAll();"/>
							</span>
							<span class="site-name">客户名称: ${qzSetting.contactPerson}</span>
							<span style="padding: 0 10px"> —— </span>
							<a href="javascript:;" title="网站域名">${qzSetting.domain}</a>

							<div class="handle">
								<shiro:hasPermission name="/internal/qzchargelog/save">
									<a class="blue" href="javascript:showChargeDialog('${qzSetting.uuid}','${qzSetting.contactPerson}','${qzSetting.domain}',this)">收费</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/qzsetting/save">
									<a class="blue" href="javascript:showSettingDialog('${qzSetting.uuid}', this)">修改</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/qzsetting/delete">
									<a class="blue" href="javascript:delQZSetting(${qzSetting.uuid})">删除</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/qzchargelog/chargesList">
									<a class="blue" href="javascript:showChargeLog('${qzSetting.uuid}', this)">收费记录</a>
								</shiro:hasPermission>
							</div>
						</div>

						<div class="body">
							<div class="data-info-wrap">
								<div class="baidu-rank">
									<a href="javascript:;">
										<span class="line1">${qzSetting.userID}</span>
										<span title="用户名称">用户名称</span>
									</a>
								</div>

								<div class="other-rank">
									<div class="row">
										<div>
									<span class="line1">
										<a href="javascript:;">8 </a>
									</span>
											<span title="录入时前10数">
										<a href="javascript:;">录入时前10数 </a>
									</span>
										</div>
										<div>
									<span class="line1">
										<a href="javascript:;">8 </a>
									</span>
											<span title="录入时前50数">
										<a href="javascript:;">录入时前50数 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">
											${qzSetting.pcGroup == null or qzSetting.pcGroup == "" ? "无" : qzSetting.pcGroup}
										</a>
									</span>
											<span title="优化分组">
										<a href="javascript:;">优化分组</a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.type} </a>
									</span>
											<span title="入口类型">
										<a href="javascript:;">入口类型 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.updateStatus == null ? "无" : qzSetting.updateStatus} </a>
									</span>
											<span title="更新状态">
										<a href="javascript:;">更新状态 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.updateInterval} </a>
									</span>
											<span title="更新间隔">
										<a href="javascript:;">更新间隔 </a>
									</span>
										</div>
									</div>

									<div class="row">
										<div>
										<span class="line1">
											<a href="javascript:;">10 </a>
										</span>
										<span title="前10数">
										<a href="javascript:;">前10数 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">10 </a>
									</span>
											<span title="前50数">
										<a href="javascript:;">前50数 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.ignoreNoIndex == true ? "是" : "否"} </a>
									</span>
											<span title="去掉没指数">
										<a href="javascript:;">去掉没指数 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.ignoreNoOrder == true ? "是" : "否"} </a>
									</span>
											<span title="去掉没排名">
										<a href="javascript:;">去掉没排名 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">
										   <c:choose>
											   <c:when test="${qzSetting.status == 1}">激活</c:when>
											   <c:when test="${qzSetting.status == 2}">
												   <span style="color: green;">新增</span>
											   </c:when>
											   <c:otherwise>
												   <span style="color: red;">暂停</span>
											   </c:otherwise>
										   </c:choose>
										</a>
									</span>
											<span title="状态">
										<a href="javascript:;">状态</a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;"> </a>
									</span>
											<span title="">
										<a href="javascript:;"></a>
									</span>
										</div>
									</div>
								</div>
							</div>

							<div class="rank-wrap">
								<a class="col-8 chart-ranking" href="javascript:;" data-highcharts-chart="4">
									<div class="highcharts-container" id="highcharts-16" style="position: relative; overflow: visible; width: 450px; height: 140px; text-align: left; line-height: normal; z-index: 0; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);">

										<div class="highcharts-tooltip" style="position: absolute; left: -65px; top: -9999px; opacity: 0; visibility: visible;">
									<span style="position: absolute; font-family: 'Lucida Grande', 'Lucida Sans Unicode', Arial, Helvetica, sans-serif; font-size: 12px; white-space: nowrap; color: rgb(51, 51, 51); margin-left: 0px; margin-top: 0px; left: 8px; top: 8px;" zindex="1">
										<span>日期：11/09</span><br>
										<span style="color:#F79C27">●</span> 前100名数量: <b>36 693</b><br>
										<span style="color:#35BCE9">●</span> 前50名数量: <b>22 651</b><br>
										<span style="color:#4F99F0">●</span> 前20名数量: <b>4 088</b><br>
										<span style="color:#5FC848">●</span> 前10名数量: <b>1 386</b><br>
									</span>
										</div>
									</div>
								</a>

								<div class="col-1">
								</div>

								<div class="col-3 top50-link">
									<div class="row4 line1">
										<span>前50</span>
										<span class="top50">
											<a class="red" href="javascript:;">-11167</a>
										</span>
									</div>

									<div class="row4">
										<span>前40</span>
										<span class="top40">
									<a class="red" href="javascript:;">-3638</a>
								</span>
									</div>

									<div class="row4">
										<span>前30</span>
										<span class="top30">
									<a class="red" href="javascript:;">-224</a>
								</span>
									</div>

									<div class="row4">
										<span>前20</span>
										<span class="top20">
									<a class="red" href="javascript:;">-224</a>
								</span>
									</div>

									<div class="row4">
										<span>前10</span>
										<span class="top10">
									<a class="red" href="javascript:;">-28</a>
								</span>
									</div>
								</div>
							</div>
						</div>
					</li>
					<!--v-if-->
				</c:if>
				<c:if test="${qzSetting.phoneGroup != null}">
					<li class="phoneGroup" style="display: none;">
						<div class="header">
							<span>
								<input type="checkbox" name="uuid" value="${qzSetting.uuid}" onclick="decideSelectAll();"/>
							</span>
							<span class="site-name">客户名称: ${qzSetting.contactPerson}</span>
							<span style="padding: 0 10px"> —— </span>
							<a href="javascript:;" title="网站域名">${qzSetting.domain}</a>
						</div>

						<div class="body">
							<div class="data-info-wrap">
								<div class="baidu-rank">
									<a href="javascript:;">
										<span class="line1">${qzSetting.userID}</span>
										<span title="用户名称">用户名称</span>
									</a>
								</div>

								<div class="other-rank">
									<div class="row">
										<div>
											<span class="line1">
												<a href="javascript:;">8 </a>
											</span>
											<span title="录入时前10数">
												<a href="javascript:;">录入时前10数 </a>
											</span>
										</div>
										<div>
											<span class="line1">
												<a href="javascript:;">8 </a>
											</span>
											<span title="录入时前50数">
												<a href="javascript:;">录入时前50数 </a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">
													${qzSetting.phoneGroup == null or qzSetting.phoneGroup == "" ? "无" : qzSetting.phoneGroup}
												</a>
											</span>
											<span title="优化分组">
												<a href="javascript:;">优化分组</a>
											</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.type} </a>
									</span>
											<span title="入口类型">
										<a href="javascript:;">入口类型 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.updateStatus == null ? "无" : qzSetting.updateStatus} </a>
									</span>
											<span title="更新状态">
										<a href="javascript:;">更新状态 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.updateInterval} </a>
									</span>
											<span title="更新间隔">
										<a href="javascript:;">更新间隔 </a>
									</span>
										</div>
									</div>

									<div class="row">
										<div>
									<span class="line1">
										<a href="javascript:;">10 </a>
									</span>
											<span title="前10数">
										<a href="javascript:;">前10数 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">10 </a>
									</span>
											<span title="前50数">
										<a href="javascript:;">前50数 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.ignoreNoIndex == true ? "是" : "否"} </a>
									</span>
											<span title="去掉没指数">
										<a href="javascript:;">去掉没指数 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">${qzSetting.ignoreNoOrder == true ? "是" : "否"} </a>
									</span>
											<span title="去掉没排名">
										<a href="javascript:;">去掉没排名 </a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;">
										   <c:choose>
											   <c:when test="${qzSetting.status == 1}">激活</c:when>
											   <c:when test="${qzSetting.status == 2}">
												   <span style="color: green;">新增</span>
											   </c:when>
											   <c:otherwise>
												   <span style="color: red;">暂停</span>
											   </c:otherwise>
										   </c:choose>
										</a>
									</span>
											<span title="状态">
										<a href="javascript:;">状态</a>
									</span>
										</div>

										<div>
									<span class="line1">
										<a href="javascript:;"> </a>
									</span>
											<span title="">
										<a href="javascript:;"></a>
									</span>
										</div>
									</div>
								</div>
							</div>

							<div class="rank-wrap">
								<a class="col-8 chart-ranking" href="javascript:;" data-highcharts-chart="4">
									<div class="highcharts-container" id="highcharts-16" style="position: relative; overflow: visible; width: 450px; height: 140px; text-align: left; line-height: normal; z-index: 0; -webkit-tap-highlight-color: rgba(0, 0, 0, 0);">
										<div class="highcharts-tooltip" style="position: absolute; left: -65px; top: -9999px; opacity: 0; visibility: visible;">
									<span style="position: absolute; font-family: 'Lucida Grande', 'Lucida Sans Unicode', Arial, Helvetica, sans-serif; font-size: 12px; white-space: nowrap; color: rgb(51, 51, 51); margin-left: 0px; margin-top: 0px; left: 8px; top: 8px;" zindex="1">
										<span>日期：11/09</span><br>
										<span style="color:#F79C27">●</span> 前100名数量: <b>36 693</b><br>
										<span style="color:#35BCE9">●</span> 前50名数量: <b>22 651</b><br>
										<span style="color:#4F99F0">●</span> 前20名数量: <b>4 088</b><br>
										<span style="color:#5FC848">●</span> 前10名数量: <b>1 386</b><br>
									</span>
										</div>
									</div>
								</a>

								<div class="col-1">
								</div>

								<div class="col-3 top50-link">
									<div class="row4 line1">
										<span>前50</span>
										<span class="top50">
									<a class="red" href="javascript:;">-11167</a>
								</span>
									</div>

									<div class="row4">
										<span>前40</span>
										<span class="top40">
									<a class="red" href="javascript:;">-3638</a>
								</span>
									</div>

									<div class="row4">
										<span>前30</span>
										<span class="top30">
									<a class="red" href="javascript:;">-224</a>
								</span>
									</div>

									<div class="row4">
										<span>前20</span>
										<span class="top20">
									<a class="red" href="javascript:;">-224</a>
								</span>
									</div>

									<div class="row4">
										<span>前10</span>
										<span class="top10">
									<a class="red" href="javascript:;">-28</a>
								</span>
									</div>
								</div>
							</div>
						</div>
					</li>
					<!--v-if-->
				</c:if>
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

<div id="changeSettingDialog" class="easyui-dialog" style="display: none;left: 40%;">
	<form id="changeSettingForm">
	<table style="font-size:12px" id="settingTable" align="center" cellspacing="5">
		<tr>
			<td style="width:60px" align="right">客户</td>
			<td>
				<input type="hidden" id="qzSettingUuid" />
				<input type="text" list="customer_list" name="qzSettingCustomer" id="qzSettingCustomer" style="width:240px" />
			</td>
		</tr>
		<tr>
			<td style="width:60px" align="right">域名</td>
			<td>
				<input type="text" name="qzSettingDomain" id="qzSettingDomain" style="width:240px" />
				<span style="margin-right: 28px;"></span>
			</td>
		</tr>
		<tr>
			<td style="width:60px" align="right">熊掌号</td>
			<td>
				<input type="text" name="bearPawNumber" id="bearPawNumber" style="width:240px" />
			</td>
		</tr>
	</table>
	<table style="font-size:12px" cellspacing="5">
		<tr>
			<td colspan="2">
				<input type="checkbox" name="operationType" id="PC" onclick="dealSettingTable('PC')" style=""/>电脑
			</td>
		</tr>
		<%--电脑分组信息--%>
		<tr>
			<td colspan="2" id="groupHeightPC">
				<table border="0" style="display:none;font-size:12px;" cellspacing="5" cellpadding="0" id="operationTypeSummaryInfoPC">
					<tr>
						<td align="right" style="width:72px">分组</td>
						<td><input type="text" name="group" id="groupPC"  style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px">电脑域名</td>
						<td><input type="text" name="subDomainName" id="subDomainNamePC"  style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px">初始词量</td>
						<td colspan="4"><input type="text" name="initialKeywordCount" id="initialKeywordCountPC" style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px">当前词量</td>
						<td colspan="4"><input type="text" name="currentKeywordCount" id="currentKeywordCountPC" style="width:240px;margin-left: -6;" readonly/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px">限制词量</td>
						<td colspan="4"><input type="text" name="maxKeywordCount" id="maxKeywordCountPC" style="width:240px;margin-left: -6;"/></td>
					</tr>
					<input type="hidden" id="qzSettingUuidPC" name="qzOperationTypeUuid" value="" />
				</table>
			</td>
		</tr>
		<%--电脑规则信息--%>
		<tr id="pcChargeRuleTable">
			<td colspan="2" id="ruleHeightPC">
				<table border="1" style="display:none;font-size:12px;" cellspacing="0" cellpadding="0" id="chargeRulePC">
					<tr>
						<td style="width:50px">序号</td>
						<td style="width:72px">起始词数</td>
						<td style="width:72px">终止词数</td>
						<td style="width:50px">价格</td>
						<td style="width:46px">操作</td>
					</tr>
					<tr>
						<td colspan="5">
							<input name="addRule" type="button" value="增加规则" onclick="addRow('chargeRulePC')" /></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td colspan="2">
				<input type="checkbox" name="operationType" id="Phone" onclick="dealSettingTable('Phone')" style=""/>手机
			</td>
		</tr>
		<%--手机分组信息--%>
		<tr>
			<td colspan="2" id="groupHeightPhone">
				<table border="0" style="display:none;font-size:12px;" cellspacing="5" cellpadding="0" id="operationTypeSummaryInfoPhone">
					<tr>
						<td align="right" style="width:72px;">分组</td>
						<td><input type="text" name="group" id="groupPhone" style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px;">手机域名</td>
						<td><input type="text" name="subDomainName" id="subDomainNamePhone" style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px">初始词量</td>
						<td colspan="4"><input type="text" name="initialKeywordCount" id="initialKeywordCountPhone"  style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px">当前词量</td>
						<td colspan="4"><input type="text" name="currentKeywordCount" id="currentKeywordCountPhone"  style="width:240px;margin-left: -6;" readonly/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px">限制词量</td>
						<td colspan="4"><input type="text" name="maxKeywordCount" id="maxKeywordCountPhone" style="width:240px;margin-left: -6;"/></td>
					</tr>
					<input type="hidden" id="qzSettingUuidPhone" name="qzOperationTypeUuid" value="" />
				</table>
			</td>
		</tr>
		<%--手机规则信息--%>
		<tr id="phoneChargeRuleTable">
			<td colspan="2" id="ruleHeightPhone">
				<table border="1" style="display:none;font-size:12px;" cellspacing="0" cellpadding="5" id="chargeRulePhone">
					<tr>
						<td style="width:50px">序号</td>
						<td style="width:72px">起始词数</td>
						<td style="width:72px">终止词数</td>
						<td style="width:50px">价格</td>
						<td style="width:46px">操作</td>
					</tr>
					<tr>
						<td colspan="5">
							<input name="addRule" type="button" value="增加规则" onclick="addRow('chargeRulePhone')" /></td>
					</tr>
				</table>
			</td>
		</tr>

		<tr>
			<td align="right" style="margin-right:4px;">入口</td>
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
			<td align="right" style="margin-right:4px;">去掉没指数</td>
			<td>
				<select name="qzSettingIgnoreNoIndex" id="qzSettingIgnoreNoIndex"  style="width:240px">
					<option value="1" selected>是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right" style="margin-right:4px;">去掉没排名</td>
			<td>
				<select name="qzSettingIgnoreNoOrder" id="qzSettingIgnoreNoOrder"  style="width:240px">
					<option value="1" selected>是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td align="right" style="margin-right:4px;">更新间隔</td>
			<td>
				<select name="qzSettingInterval" id="qzSettingInterval"  style="width:240px">
					<option value="1">1天</option>
					<option value="2">2天</option>
					<option value="3">3天</option>
					<option value="5">5天</option>
					<option value="7">7天</option>
					<option value="10">10天</option>
					<option value="12">12天</option>
					<option value="15" selected>15天</option>
				</select>
			</td>
		</tr>
	</table>
	</form>
</div>
<datalist id="customer_list">
	<c:forEach items="${customerList}" var="costomer">
		<option>${costomer.contactPerson} ${'_____'} ${costomer.uuid}</option>
	</c:forEach>
</datalist>
<%--收费Dialog--%>
<div id="chargeDialog" class="easyui-dialog" style="display: none;left: 40%;">
	<table id="chargeDialogTable" cellspacing="5">
		<tr>
			<td align="right">客户</td>
			<td>
				<input type="text" name="qzSettingCustomer" id="qzSettingCustomer" style="width: 280px" readonly/>
			</td>
		</tr>
		<tr>
			<td align="right">域名</td>
			<td>
				<input type="text" name="qzSettingDomain" id="qzSettingDomain" style="width: 280px" readonly/>
			</td>
		</tr>
		<tr>
			<td>
				<table id="checkChargePC" style="display: none;font-size:12px;">
					<tr><td>
						<input type="checkbox" name="operationType" id="PC" style="margin-left: -2px" onclick="dealChargeTable('PC')" />电脑
					</td></tr>
				</table>
			</td>

			<td>
				<table id="checkChargePhone" style="display: none;font-size:12px;">
					<tr><td>
						<input type="checkbox" name="operationType" id="Phone" onclick="dealChargeTable('Phone')" style="margin-left: 140px"/>手机
					</td></tr>
				</table>
			</td>
		</tr>
	</table>
	<table id="chargeInfoTable" style="height:185px;">
		<%--电脑收费信息--%>
		<tr>
			<td style="width: 182px">
				<table style="display:none;font-size:12px;" id="PCChargeInfo">
					<tr>
						<td align="right">初始词量</td>
						<input type="hidden" name="qzOperationTypeUuid" id="qzOperationTypeUuidPC"/>
						<td><input type="text" name="initialKeywordCount" id="initialKeywordCountPC"  style="width:100px" readonly/></td>
					</tr>
					<tr>
						<td align="right">当前词量</td>
						<td><input type="text" name="currentKeywordCount" id="currentKeywordCountPC"  style="width:100px" readonly/></td>
					</tr>
					<tr>
						<td align="right">应收金额</td>
						<td>
							<input type="text" name="receivableAmount" id="receivableAmountPC" style="width:100px" readonly />
						</td>
					</tr>
					<tr>
						<td align="right">计划收费日期</td>
						<td>
							<input type="text" name="planChargeDate" id="planChargeDatePC" style="width:100px" readonly />
						</td>
					</tr>
					<tr>
						<td align="right">下次收费日期</td>
						<td>
							<input type="text" name="nextChargeDate" id="nextChargeDatePC" class="Wdate" onClick="WdatePicker()" style="width:100px" />
						</td>
					</tr>
					<tr>
						<td align="right">实收金额</td>
						<td>
							<input type="text" name="actualAmount" id="actualAmountPC" onkeyup="calTotalAmount()" style="width:100px" />
						</td>
					</tr>
					<tr>
						<td align="right">实际收费日期</td>
						<td>
							<input name="actualChargeDate" id="actualChargeDatePC" class="Wdate" type="text" style="width:100px" onClick="WdatePicker()" value="">
						</td>
					</tr>
				</table>
			</td>
			<%--手机收费信息--%>
			<td style="width: 182px">
				<table style="display:none;font-size:12px;margin-top: 2px;margin-bottom: 2px;" id="PhoneChargeInfo">
					<tr>
						<td align="right">初始词量</td>
						<input type="hidden" name="qzOperationTypeUuid" id="qzOperationTypeUuidPhone"/>
						<td><input type="text" name="initialKeywordCount" id="initialKeywordCountPhone"  style="width:100px" readonly/></td>
					</tr>
					<tr>
						<td align="right">当前词量</td>
						<td><input type="text" name="currentKeywordCount" id="currentKeywordCountPhone"  style="width:100px" readonly/></td>
					</tr>
					<tr>
						<td align="right">应收金额</td>
						<td>
							<input type="text" name="receivableAmount" id="receivableAmountPhone" style="width:100px" readonly />
						</td>
					</tr>
					<tr>
						<td align="right">计划收费日期</td>
						<td>
							<input type="text" name="planChargeDate" id="planChargeDatePhone" style="width:100px" readonly />
						</td>
					</tr>
					<tr>
						<td align="right">下次收费日期</td>
						<td>
							<input type="text" name="nextChargeDate" id="nextChargeDatePhone" class="Wdate" onClick="WdatePicker()" style="width:100px" />
						</td>
					</tr>
					<tr>
						<td align="right">实收金额</td>
						<td>
							<input type="text" name="actualAmount" id="actualAmountPhone" onkeyup="calTotalAmount()" style="width:100px" />
						</td>
					</tr>
					<tr>
						<td align="right">实际收费日期</td>
						<td>
							<input name="actualChargeDate" id="actualChargeDatePhone" class="Wdate" type="text" style="width:100px"  onClick="WdatePicker()" value="">
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
	<p>
		<span style="margin-left: 55px"></span>合计:<sapn id="totalAmount"></sapn>
	</p>
</div>
<div id="getAvailableQZSettings" class="easyui-dialog"  style="display: none">
	<textarea id="getAvailableQZSettingsContent"  style="width:100%;height:100%;resize: none"></textarea>
</div>
<%--收费详情列表--%>
<div id="chargeLogListDiv" class="easyui-dialog" style="display:none;left: 40%;" >
	<table id="chargeLogListTable" border="1" cellpadding="10" style="font-size: 12px;background-color: white;border-collapse: collapse;margin: 10px 10px;width:92%;">
		<tr>
			<td>收费时间</td>
			<td>操作类型</td>
			<td>收费金额</td>
			<td>收费人员</td>
			<td>创建时间</td>
		</tr>
	</table>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/qzsetting/list.js"></script>
<script language="javascript">
    var dateStr = new Date(); // 当天日期
    var m = dateStr.getMonth() + 1 < 10 ? "0" + (dateStr.getMonth() + 1) : (dateStr.getMonth() + 1);
    var d = dateStr.getDate() < 10 ? "0" + dateStr.getDate() : dateStr.getDate();
    var today = dateStr.getFullYear() + "-" + m + "-" + d;
</script>
</body>
</html>