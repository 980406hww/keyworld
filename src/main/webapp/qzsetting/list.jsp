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
	<div class="mytabs">
		<ul class="link">
			<c:forEach items="${existSearchEngineMap}" var="entry">
				<c:if test="${entry.value eq 'PC'}">
					<c:choose>
						<c:when test="${entry.key eq qzSettingSearchCriteria.searchEngine and entry.value eq qzSettingSearchCriteria.terminalType}">
							<li name="${entry.key}${entry.value}" class="active">
								<a href="javascript:;" onclick="checkTerminalType('${entry.key}', '${entry.value}');">${entry.key}${entry.value}</a>
							</li>
						</c:when>
						<c:otherwise>
							<li name="${entry.key}${entry.value}">
								<a href="javascript:;" onclick="checkTerminalType('${entry.key}', '${entry.value}');">${entry.key}${entry.value}</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:if>
				<c:if test="${entry.value eq 'Phone'}">
                    <c:set var="criteriaSearchEngine" value="${qzSettingSearchCriteria.searchEngine}Phone" />
					<c:choose>
						<c:when test="${entry.key eq criteriaSearchEngine and entry.value eq qzSettingSearchCriteria.terminalType}">
							<li name="${entry.key}" class="active">
								<a href="javascript:;" onclick="checkTerminalType('${entry.key}', '${entry.value}');">${entry.key}</a>
							</li>
						</c:when>
						<c:otherwise>
							<li name="${entry.key}">
								<a href="javascript:;" onclick="checkTerminalType('${entry.key}', '${entry.value}');">${entry.key}</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:if>
			</c:forEach>
		</ul>
		<div class="conn">
			<ul>
                <li class="selectedAllQZSetting">
                    <input type="checkbox" onclick="qzSettingSelectAll(this);" />
                </li>
				<li>
					<input type="text" title="请输入网站域名" name="domain" placeholder="请输入网站域名" onkeydown="enterIn(event);" value="${qzSettingSearchCriteria.domain}">
				</li>

				<li>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="trimSearchCondition('1')" value=" 搜索 " >
					&nbsp;&nbsp;
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showMoreSearchCondition()" value=" 更多搜索 " >
				</li>
				<shiro:hasPermission name="/internal/qzsetting/save">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showSettingDialog($(this))" value=" 增加 " >&nbsp;
					</li>
				</shiro:hasPermission>

				<shiro:hasPermission name="/internal/qzsetting/save">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showQZCategoryTagsDialog()" value=" 修改分组标签 " >&nbsp;
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/updateImmediately">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="immediatelyUpdateQZSettings('updateSettings')" value=" 马上更新 " >&nbsp;
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/startMonitorImmediately">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="immediatelyUpdateQZSettings('startMonitor')" value=" 达标监控 " >&nbsp;
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/updateQZKeywordEffectImmediately">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" title="更改操作词的作用为指定词" onclick="immediatelyUpdateQZSettings('updateQZKeywordEffect')" value=" 更改为指定词 " >&nbsp;
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/updateStatus">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateQZSettingStatus(0)" value=" 暂停整站 " >
						&nbsp;&nbsp;
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateQZSettingStatus(1)" value=" 激活整站 " >
						&nbsp;&nbsp;
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateQZSettingRenewalStatus(0)" value=" 暂停续费 " >
						&nbsp;&nbsp;
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateQZSettingRenewalStatus(1)" value=" 续费 " >
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/deleteQZSettings">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="delSelectedQZSettings(this)" value=" 删除所选 " >&nbsp;
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/searchQZSettings">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="getAvailableQZSettings()" value="查看更新队列(${availableQZSettingCount})">&nbsp;
					</li>
				</shiro:hasPermission>
				<li>
					<label name="lower" title="网站关键词(PC,Phone)一星期排名趋势涨幅&lt;${qzSettingSearchCriteria.lowerValue}且下降至少${qzSettingSearchCriteria.upOneWeekDiff}个词">
						<input type="radio" <c:if test="${qzSettingSearchCriteria.checkStatus == 1}">checked</c:if>>
						骤降 (${qzSettingSearchCriteria.downNum})
					</label>
				</li>
				<li>
					<label name="unchanged" title="网站关键词(PC,Phone)一星期排名趋势涨幅不变">
						<input type="radio" <c:if test="${qzSettingSearchCriteria.checkStatus == 6}">checked</c:if>>
						无变化 (${qzSettingSearchCriteria.unchangedNum})
					</label>
				</li>
				<li>
					<label name="upper" title="网站关键词(PC,Phone)一星期排名趋势涨幅&gt;${qzSettingSearchCriteria.upperValue}且上涨至少${qzSettingSearchCriteria.upOneWeekDiff}个词">
						<input type="radio" <c:if test="${qzSettingSearchCriteria.checkStatus == 2}">checked</c:if>>
						暴涨 (${qzSettingSearchCriteria.upNum})
					</label>
				</li>
				<li>
					<label name="lowerDifference" title="网站关键词(PC,Phone)今天前10下降">
						<input type="radio" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 7}">checked</c:if>>
						下降 (${qzSettingSearchCriteria.downDifferenceNum})
					</label>
				</li>
				<li>
					<label name="unchangedDifference" title="网站关键词(PC,Phone)今天前10不变">
						<input type="radio" <c:if test="${qzSettingSearchCriteria.checkStatus == 8}">checked</c:if>>
						不变 (${qzSettingSearchCriteria.unchangedDifferenceNum})
					</label>
				</li>
				<li>
					<label name="upperDifference" title="网站关键词(PC,Phone)今天前10上升">
						<input type="radio" <c:if test="${qzSettingSearchCriteria.checkStatus == 9}">checked</c:if>>
						上升 (${qzSettingSearchCriteria.upDifferenceNum})
					</label>
				</li>
				<li>
					<label name="atLeastStandard" title="标识最少有一条规则达标">
						<input type="radio" <c:if test="${qzSettingSearchCriteria.checkStatus == 3}">checked</c:if>>
						达标 (${qzSettingSearchCriteria.atLeastStandardNum})
					</label>
				</li>
				<li>
					<label name="neverStandard" title="标识一条规则都未达标">
						<input type="radio" <c:if test="${qzSettingSearchCriteria.checkStatus == 4}">checked</c:if>>
						未达标 (${qzSettingSearchCriteria.neverStandardNum})
					</label>
				</li>
				<li>
					<label name="closeStandard" title="标识下条规则接近达标,未完成度&lt;${qzSettingSearchCriteria.differenceValue}">
						<input type="radio" <c:if test="${qzSettingSearchCriteria.checkStatus == 5}">checked</c:if>>
						接近达标 (${qzSettingSearchCriteria.closeStandardNum})
					</label>
				</li>
			</ul>
		</div>
		<div class="conn" name="moreSearchCondition" style="display: none;">
			<ul>
				<li class="customerInfo condition">
					<span>客户: </span>
					<input type="text" list="customer_list" name="customerInfo" value="${qzSettingSearchCriteria.customerInfo}">
				</li>
				<shiro:hasPermission name="/internal/qzsetting/startMonitorImmediately">
				<li class="userName condition">
					<span>用户名称: </span>
					<span>
						<select id="userNameTree" name="userName" style="width: 120px; height: 29px;" class="easyui-validatebox" data-options="required:true"></select>
					</span>
				</li>
				</shiro:hasPermission>
				<li class="category condition">
					<span>分类标签: </span>
					<input type="text" list="categoryTag_list" name="categoryTag" value="${qzSettingSearchCriteria.categoryTag}">
				</li>
				<li class="group condition">
					<span>组名: </span>
					<input type="text" name="group" value="${qzSettingSearchCriteria.group}">
				</li>
				<li>
					<span>操作类型: </span>
					<span>
						<select name="operationType" style="width: 150px;">
							<option value="">全部</option>
							<c:forEach items="${operationTypeValues}" var="operationType">
								<c:choose>
									<c:when test="${operationType eq qzSettingSearchCriteria.operationType}"><option selected>${operationType}</option></c:when>
									<c:otherwise><option>${operationType}</option></c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</span>
				</li>
				<li>
					<span>采集状态: </span>
					<select name="status">
						<option value="" <c:if test="${qzSettingSearchCriteria.status == null}">selected</c:if>>全部</option>
						<option value="1" <c:if test="${qzSettingSearchCriteria.status == 1}">selected</c:if>>激活</option>
						<option value="0" <c:if test="${qzSettingSearchCriteria.status == 0}">selected</c:if>>暂停</option>
						<option value="2" <c:if test="${qzSettingSearchCriteria.status == 2}">selected</c:if>>新增</option>
					</select>
				</li>
				<li>
					<span>续费状态: </span>
					<select name="renewalStatus">
						<option value="" <c:if test="${qzSettingSearchCriteria.renewalStatus == null}">selected</c:if>>全部</option>
						<option value="1" <c:if test="${qzSettingSearchCriteria.renewalStatus == 1}">selected</c:if>>续费</option>
						<option value="0" <c:if test="${qzSettingSearchCriteria.renewalStatus == 0}">selected</c:if>>暂停续费</option>
					</select>
				</li>
				<li>
					<span>达标种类: </span>
					<select name="standardSpecies">
						<c:forEach items="${standardSpeciesMap}" var="entry">
							<c:choose>
								<c:when test="${entry.value eq qzSettingSearchCriteria.standardSpecies}"><option selected value="${entry.value}">${entry.key}</option></c:when>
								<c:otherwise><option value="${entry.value}">${entry.key}</option></c:otherwise>
							</c:choose>
						</c:forEach>
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
				<li class="createTime condition">
					<span>创建时间: </span>
					<input type="text" name="createTime" id="createTime" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt: 'yyyy-MM-dd', maxDate: '%y-%M-%d' })" placeholder=">=" value="${qzSettingSearchCriteria.createTime}">
                    <span>--</span>
                    <input type="text" name="createTimePrefix" id="createTimePrefix" class="Wdate" onclick="WdatePicker({skin:'whyGreen',dateFmt: 'yyyy-MM-dd', maxDate: '%y-%M-%d' })" placeholder="<=" value="${qzSettingSearchCriteria.createTimePrefix}">
				</li>
				<li>
					<span>达标优化类型: </span>
					<select name="optimizationType">
						<c:forEach items="${optimizationTypeMap}" var="entry">
							<c:choose>
								<c:when test="${entry.value eq (qzSettingSearchCriteria.optimizationType == null ? '' : qzSettingSearchCriteria.optimizationType)}"><option selected value="${entry.value}">${entry.key}</option></c:when>
								<c:otherwise><option value="${entry.value}">${entry.key}</option></c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</li>
				<shiro:hasPermission name="/internal/qzsetting/startMonitorImmediately">
					<li>
						<span>达标监控: </span>
						<select name="hasMonitor" style="width: 80px;">
							<option value="" <c:if test="${qzSettingSearchCriteria.hasMonitor == null}">selected</c:if>>全部</option>
							<option value="1" <c:if test="${qzSettingSearchCriteria.hasMonitor == true}">selected</c:if>>是</option>
							<option value="0" <c:if test="${qzSettingSearchCriteria.hasMonitor == false}">selected</c:if>>否</option>
						</select>
					</li>
				</shiro:hasPermission>
				<li>
					<span>达标计划: </span>
					<select name="hasReady" style="width: 80px;">
						<option value="" <c:if test="${qzSettingSearchCriteria.hasReady == null}">selected</c:if>>全部</option>
						<option value="1" <c:if test="${qzSettingSearchCriteria.hasReady == true}">selected</c:if>>是</option>
						<option value="0" <c:if test="${qzSettingSearchCriteria.hasReady == false}">selected</c:if>>否</option>
					</select>
				</li>
				<%--<shiro:hasPermission name="/internal/qzsetting/searchQZSettings">
					<li><a href="javascript:resetSearchCondition('-1')">过期未收费(${chargeRemindDataMap['expiredChargeSize']})</a></li>
					<li><a target="_blank" href="javascript:resetSearchCondition('0')">当天收费提醒(${chargeRemindDataMap['nowChargeSize']})</a></li>
					<li><a target="_blank" href="javascript:resetSearchCondition('3')">三天收费提醒(${chargeRemindDataMap['threeChargeSize']})</a></li>
					<li><a target="_blank" href="javascript:resetSearchCondition('7')">七天收费提醒(${chargeRemindDataMap['sevenChargeSize']})</a></li>
				</shiro:hasPermission>--%>
			</ul>
		</div>
	</div>
</div>

<form method="post" id="chargeForm" action="/internal/qzsetting/searchQZSettings">
	<input type="hidden" name="dateRangeType" id="dateRangeType" value="${qzSettingSearchCriteria.dateRangeType}"/>
	<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
	<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
	<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
	<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
	<input type="hidden" name="resetPagingParam" id="resetPagingParam" value="${qzSettingSearchCriteria.resetPagingParam}"/>
	<input type="hidden" name="domain" id="domain" value="${qzSettingSearchCriteria.domain}"/>
	<input type="hidden" name="searchEngine" id="searchEngine" value="${qzSettingSearchCriteria.searchEngine}"/>
	<input type="hidden" name="group" id="group" value="${qzSettingSearchCriteria.group}"/>
	<input type="hidden" name="customerUuid" id="customerUuid" value="${qzSettingSearchCriteria.customerUuid}"/>
	<input type="hidden" name="statusHidden" id="statusHidden" value="${qzSettingSearchCriteria.status}"/>
	<input type="hidden" name="customerInfo" id="customerInfo" value="${qzSettingSearchCriteria.customerInfo}">
	<input type="hidden" name="userInfoID" id="userInfoID" value="${qzSettingSearchCriteria.userInfoID}">
	<input type="hidden" name="organizationID" id="organizationID" value="${qzSettingSearchCriteria.organizationID}">
	<input type="hidden" name="operationType" id="operationType" value="${qzSettingSearchCriteria.operationType}"/>
	<input type="hidden" name="status" id="status" value="${qzSettingSearchCriteria.status}"/>
	<input type="hidden" name="renewalStatus" id="renewalStatus" value="${qzSettingSearchCriteria.renewalStatus}"/>
	<input type="hidden" name="updateStatus" id="updateStatus" value="${qzSettingSearchCriteria.updateStatus}"/>
	<input type="hidden" name="checkStatus" id="checkStatus" value="${qzSettingSearchCriteria.checkStatus}"/>
	<input type="hidden" name="terminalType" id="terminalType" value="${qzSettingSearchCriteria.terminalType}"/>
	<input type="hidden" name="categoryTag" id="categoryTag" value="${qzSettingSearchCriteria.categoryTag}"/>
	<input type="hidden" name="createTime" id="createTime" value="${qzSettingSearchCriteria.createTime}"/>
    <input type="hidden" name="createTimePrefix" id="createTimePrefix" value="${qzSettingSearchCriteria.createTimePrefix}"/>
    <input type="hidden" name="hasReady" id="hasReady" value="${qzSettingSearchCriteria.hasReady}"/>
    <input type="hidden" name="hasMonitor" id="hasMonitor" value="${qzSettingSearchCriteria.hasMonitor}"/>
    <input type="hidden" name="standardSpecies" id="standardSpecies" value="${qzSettingSearchCriteria.standardSpecies}"/>
    <input type="hidden" name="optimizationType" id="optimizationType" value="${qzSettingSearchCriteria.optimizationType}"/>
</form>

<div class="datalist">
	<div class="datalist-list">
		<input type="hidden" id="isSEO" value="${isSEO}">
		<input type="hidden" id="isBaiduEngine" value="${isBaiduEngine}">
		<ul>
			<c:forEach items="${page.records}" var="qzSetting" varStatus="status">
				<c:choose>
					<c:when test="${qzSetting.pcGroup != null and qzSetting.qzKeywordRankInfoMap['PC'] != null and
					        (qzSetting.searchEngine eq qzSettingSearchCriteria.searchEngine or
					        qzSettingSearchCriteria.searchEngine eq 'All')}">
					<li>
						<div class="header">
							<input type="hidden" name="contactPerson" value="${qzSetting.contactPerson}">
							<span>
								<input type="checkbox" name="uuid" value="${qzSetting.uuid}" />
							</span>
							<span class="contactPerson-name" title="${qzSetting.contactPerson}"><a href="javascript:;">${qzSetting.contactPerson}</a></span>
							<span class="domain" title="${qzSetting.domain}"><a href="javascript:;">${qzSetting.domain}</a></span>
							<span class="organization-name" title="${qzSetting.organizationName}"><a href="javascript:;">${qzSetting.organizationName}</a></span>
							<span class="to-aizhan"><a href="https://www.aizhan.com/cha/${qzSetting.domain}" target="_blank" title="查看爱站">爱站</a></span>
							<span class="to-5118"><a href="https://www.5118.com/seo/${qzSetting.domain}" target="_blank" title="查看5118,需要登录">5118</a></span>
							<span class="fmtStandardDate" title="<fmt:formatDate value="${qzSetting.standardTime}" pattern="yyyy-MM-dd"></fmt:formatDate>">达标日期:<a href="javascript:;">
								<c:if test="${qzSetting.standardTime == null}">无</c:if>
								<c:if test="${qzSetting.standardTime != null}"><fmt:formatDate value="${qzSetting.standardTime}" pattern="yyyy-MM-dd"></fmt:formatDate></c:if>
							</a></span>
							<span class="tagNames" ondblclick="editTagNameStr($(this).find('label.tagNameStr')[0], true)"><label>分组标签:</label>&nbsp;&nbsp;<label class="tagNameStr">暂无</label></span>
							<span class="fmtCreateDate" title="<fmt:formatDate value="${qzSetting.createTime}" pattern="yyyy-MM-dd"></fmt:formatDate>">创建日期:<a href="javascript:;"><fmt:formatDate value="${qzSetting.createTime}" pattern="yyyy-MM-dd"></fmt:formatDate></a></span>
							<div class="handle">
                                <a class="blue" href="javascript:showExcludeCustomerKeywordDialog('${qzSetting.uuid}','${qzSetting.customerUuid}','${qzSetting.domain.trim()}','${qzSetting.pcGroup}','PC')">排除关键字</a>
                                <shiro:hasPermission name="/internal/qzsetting/save">
                                    <a class="blue" href="javascript:showKeywordDialog('${qzSetting.uuid}','${qzSetting.customerUuid}','${qzSetting.domain.trim()}','${qzSetting.pcGroup}','${qzSetting.bearPawNumber}')">指定关键字</a>
                                </shiro:hasPermission>
								<shiro:hasPermission name="/internal/qzsetting/save">
									<a class="blue" href="javascript:openMessageBox('全站设置', '${qzSetting.customerUuid}', '${qzSetting.contactPerson}')">用户留言</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/usernotebook/searchUserNoteBooks">
									<a class="blue" href="javascript:openNoteBookDialog('${qzSetting.customerUuid}', '${qzSettingSearchCriteria.terminalType}')">记事本</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/qzchargelog/save">
									<a class="blue" href="javascript:showChargeDialog('${qzSetting.uuid}','${qzSetting.contactPerson}','${qzSetting.domain.trim()}',this)">收费</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/qzsetting/save">
									<a class="blue" href="javascript:;" onclick="showSettingDialog($(this))">修改</a>
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
								<div class="user-id">
									<a href="javascript:;">
										<span class="line1" title="${qzSetting.userID}">${qzSetting.userID}</span>
										<span>用户名称</span>
									</a>
								</div>

								<div class="standard-info">
									<a href="javascript:;">
										<span class="line1">
											<a href="javascript:;" qzsettinguuid="${qzSetting.uuid}" <c:if test="${!isSEO}">onmouseover="showChargeRulesDiv($(this))" onmouseout="closeChargeRulesDiv()"</c:if>>
												<c:if test="${qzSetting.organizationName != '优化部'}">
													<c:if test="${qzSetting.totalPrice > 0}">
														<font style="background-color: mediumseagreen;font-size: 14px;">￥ ${qzSetting.totalPrice}</font>
													</c:if>
													<c:if test="${qzSetting.totalPrice == 0}">
														<font style="background-color: palevioletred;font-size: 14px;">￥ ${qzSetting.totalPrice}</font>
													</c:if>
												</c:if>
											</a>
										</span>
										<span>全站达标信息</span>
									</a>
								</div>

								<div class="other-rank">
									<div class="row">
										<div>
											<span class="line1">
												<a href="javascript:;" name="fIsMonitor">
														${qzSetting.fIsMonitor == true ? "是" : "否"}
												</a>
											</span>
												<span>
												<a href="javascript:;">达标监控</a>
											</span>
										</div>
										<div>
										<span class="line1">
											<a href="javascript:;">
												<c:choose>
													<c:when test="${qzSetting.crawlerStatus == 'new'}">
														<span style="color: indianred;">未爬取</span>
													</c:when>
													<c:when test="${qzSetting.crawlerStatus == 'processing'}">
														<span style="color: darkorange;">爬取中</span>
													</c:when>
													<c:otherwise>
														<span style="color: forestgreen;">爬取完成</span>
													</c:otherwise>
												</c:choose>
											</a>
										</span>
											<span>
											<a href="javascript:;">爬取状态</a>
										</span>
										</div>

										<div name="operationKeywordNum" title="点击链接跳转到关键字列表">
										<span class="line1">
											<a target="_blank" href="javascript:searchCustomerKeywords('${qzSetting.customerUuid}', '${qzSetting.pcGroup}');">0</a>
										</span>
											<span>
											<a href="javascript:;">操作词数</a>
										</span>
										</div>
									</div>

									<div class="row">
										<div>
											<span class="line1">
												<a href="javascript:;">
														${qzSetting.fIsReady == true ? "是" : "否"}
												</a>
											</span>
											<span>
												<a href="javascript:;">达标计划</a>
											</span>
										</div>
										<div>
                                            <span class="line1">
                                                <input type="hidden">
                                                <a href="javascript:;">
													<c:choose>
														<c:when test="${qzSetting.renewalStatus == 1}">
															<span style="color: green;">续费</span>
														</c:when>
														<c:otherwise>
															<span style="color: red;">暂停续费</span>
														</c:otherwise>
													</c:choose>
												</a>
                                            </span>
											<span>
                                                <a href="javascript:;">续费状态</a>
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
											<span>
                                                <a href="javascript:;">状态</a>
                                            </span>
										</div>
									</div>
								</div>

								<div class="other-rank_2">
									<div class="row">
										<div title="站点所属优化组">
										<span class="line1">
											<a href="javascript:;">${qzSetting.pcGroup == null or qzSetting.pcGroup == "" ? "暂无" : qzSetting.pcGroup}</a>
										</span>
											<span>
											<a href="javascript:;">优化分组</a>
										</span>
										</div>
									</div>
									<div class="row">
										<div title="销售和SEO没有修改操作组合的权限！！！">
											<div class="showOperationCombineName" name="showOperationCombineName">
												<input type="hidden" name="operationCombineName" value="">
												<select name="operationCombineName" onchange="changeQZSettingGroupOperationCombineUuid(this, '${qzSetting.pcGroup}', '${qzSettingSearchCriteria.loginName}', '${isSEO}');" style="width: 180px;">
                                                    <option value=""></option>
													<c:forEach items="${operationCombines}" var="operationCombine">
                                                        <option>${operationCombine}</option>
													</c:forEach>
												</select>
											</div>
											<div class="operationCombineSpan">
											<span>
												<a href="javascript:;">操作组合</a>
											</span>
											</div>
										</div>
									</div>
								</div>
							</div>
                            <c:if test="${qzSetting.searchEngine == '百度'}">
                                <div class="rank-wrap">
                                    <c:choose>
                                        <c:when test="${qzSetting.qzKeywordRankInfoMap['PC'] != null}">
                                            <c:choose>
                                                <c:when test="${qzSetting.qzKeywordRankInfoMap['PC']['aiZhan'] != null}">
                                                    <div name="rankInfo" style="display: none;">
                                                        <span name="aiZhan">${qzSetting.qzKeywordRankInfoMap['PC']['aiZhan']}</span>
                                                    </div>
                                                    <div class="col-1" id="keywordRecordCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
                                                    <div class="col-2" id="keywordTrendCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
                                                    <div class="col-4 top50-link">
                                                        <div class="row4">
                                                            <span>前10</span>
                                                            <span class="top10">
                                            <a href="javascript:;" id="PCTop10">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>前50</span>
                                                            <span class="top50">
											<a href="javascript:;" id="PCTop50">0</a>
										</span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>初始前10</span>
                                                            <span class="top10">
                                            <a href="javascript:;" id="PCTopCreate10">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>初始前50</span>
                                                            <span class="top50">
                                            <a href="javascript:;" id="PCTopCreate50">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>曲线达标</span>
                                                            <span class="isStandard">
                                            <a href="javascript:;" id="PCIsStandard">否</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>达标时间</span>
                                                            <span class="standardTime">
                                            <a href="javascript:;" id="PCStandardTime">无</a>
                                        </span>
                                                        </div>
                                                    </div>
                                                </c:when>
                                                <c:when test="${qzSetting.qzKeywordRankInfoMap['PC']['5118'] != null}">
                                                    <div name="rankInfo" style="display: none;">
                                                        <span name="5118">${qzSetting.qzKeywordRankInfoMap['PC']['5118']}</span>
                                                    </div>
                                                    <div class="col-1" id="keywordRecordCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
                                                    <div class="col-2" id="keywordTrendCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
                                                    <div class="col-4 top50-link">
                                                        <div class="row4">
                                                            <span>前10</span>
                                                            <span class="top10">
                                            <a href="javascript:;" id="PCTop10">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>前50</span>
                                                            <span class="top50">
											<a href="javascript:;" id="PCTop50">0</a>
										</span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>初始前10</span>
                                                            <span class="top10">
                                            <a href="javascript:;" id="PCTopCreate10">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>初始前50</span>
                                                            <span class="top50">
                                            <a href="javascript:;" id="PCTopCreate50">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>曲线达标</span>
                                                            <span class="isStandard">
                                            <a href="javascript:;" id="PCIsStandard">否</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>达标时间</span>
                                                            <span class="standardTime">
                                            <a href="javascript:;" id="PCStandardTime">无</a>
                                        </span>
                                                        </div>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${qzSetting.qzKeywordRankInfoMap['PC']['aiZhan'] != null or qzSetting.qzKeywordRankInfoMap['PC']['5118'] != null}">
                                                        <div name="rankInfo" style="display: none;"></div>
                                                        <div class="col-1">
                                                            <h1>暂无数据</h1>
                                                        </div>
                                                        <div class="col-2">
                                                            <h1>暂无数据</h1>
                                                        </div>
                                                        <div class="col-4 top50-link">
                                                            <div class="row4">
                                                                <span>前10</span>
                                                                <span class="top10">
                                            <a href="javascript:;" id="PCTop10">0</a>
                                        </span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>前50</span>
                                                                <span class="top50">
											<a href="javascript:;" id="PCTop50">0</a>
										</span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>初始前10</span>
                                                                <span class="top10">
                                            <a href="javascript:;" id="PCTopCreate10">0</a>
                                        </span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>初始前50</span>
                                                                <span class="top50">
                                            <a href="javascript:;" id="PCTopCreate50">0</a>
                                        </span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>曲线达标</span>
                                                                <span class="isStandard">
                                            <a href="javascript:;" id="PCIsStandard">否</a>
                                        </span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>达标时间</span>
                                                                <span class="standardTime">
                                            <a href="javascript:;" id="PCStandardTime">无</a>
                                        </span>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>

                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:if>

							<div class="rank-wrap1">
								<c:choose>
									<c:when test="${qzSetting.qzKeywordRankInfoMap['PC'] != null}">
										<c:choose>
											<c:when test="${qzSetting.qzKeywordRankInfoMap['PC']['designationWord'] != null}">
												<div name="rankInfo" style="display: none;">
													<span name="designationWord">${qzSetting.qzKeywordRankInfoMap['PC']['designationWord']}</span>
												</div>
												<div class="col-1" id="designationWordCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
												<div class="col-5 top50-link">
													<div class="row4">
														<span>前10</span>
														<span class="top10">
											<a href="javascript:;" id="PCTop10">0</a>
										</span>
													</div>
													<div class="row4">
														<span>前50</span>
														<span class="top50">
                                            <a href="javascript:;" id="PCTop50">0</a>
                                        </span>
													</div>
													<div class="row4">
														<span>初始前10</span>
														<span class="top10">
                                            <a href="javascript:;" id="PCTopCreate10">0</a>
                                        </span>
													</div>
													<div class="row4">
														<span>初始前50</span>
														<span class="top50">
                                            <a href="javascript:;" id="PCTopCreate50">0</a>
                                        </span>
													</div>
													<div class="row4">
														<span>曲线达标</span>
														<span class="isStandard">
                                            <a href="javascript:;" id="PCIsStandard">否</a>
                                        </span>
													</div>
													<div class="row4">
														<span>达标时间</span>
														<span class="standardTime">
                                            <a href="javascript:;" id="PCStandardTime">无</a>
                                        </span>
													</div>
												</div>
											</c:when>
											<c:otherwise>
												<c:if test="${qzSetting.qzKeywordRankInfoMap['PC']['designationWord'] != null}">
													<div name="rankInfo" style="display: none;"></div>
													<div class="col-1">
														<h1>暂无数据</h1>
													</div>
													<div class="col-5 top50-link">
														<div class="row4">
															<span>前10</span>
															<span class="top10">
											<a href="javascript:;" id="PCTop10">0</a>
										</span>
														</div>
														<div class="row4">
															<span>前50</span>
															<span class="top50">
                                            <a href="javascript:;" id="PCTop50">0</a>
                                        </span>
														</div>
														<div class="row4">
															<span>初始前10</span>
															<span class="top10">
                                            <a href="javascript:;" id="PCTopCreate10">0</a>
                                        </span>
														</div>
														<div class="row4">
															<span>初始前50</span>
															<span class="top50">
                                            <a href="javascript:;" id="PCTopCreate50">0</a>
                                        </span>
														</div>
														<div class="row4">
															<span>曲线达标</span>
															<span class="isStandard">
                                            <a href="javascript:;" id="PCIsStandard">否</a>
                                        </span>
														</div>
														<div class="row4">
															<span>达标时间</span>
															<span class="standardTime">
                                            <a href="javascript:;" id="PCStandardTime">无</a>
                                        </span>
														</div>
													</div>
												</c:if>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>

									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</li>
					<!--li-end-pc-->
					</c:when>
					<c:when test="${qzSetting.phoneGroup != null and qzSetting.qzKeywordRankInfoMap['Phone'] != null and
					        (qzSetting.searchEngine eq qzSettingSearchCriteria.searchEngine or
					        qzSettingSearchCriteria.searchEngine eq 'All')}">
					<li>
						<div class="header">
							<span>
								<input type="checkbox" name="uuid" value="${qzSetting.uuid}" />
							</span>
							<span class="contactPerson-name" title="${qzSetting.contactPerson}"><a href="javascript:;">${qzSetting.contactPerson}</a></span>
							<span class="domain" title="${qzSetting.domain}"><a href="javascript:;">${qzSetting.domain}</a></span>
							<span class="contactPerson-name" title="${qzSetting.organizationName}"><a href="javascript:;">${qzSetting.organizationName}</a></span>
							<span class="to-aizhan"><a href="https://www.aizhan.com/cha/${qzSetting.domain}" target="_blank" title="查看爱站">爱站</a></span>
							<span class="to-5118"><a  href="https://www.5118.com/seo/${qzSetting.domain}" target="_blank" title="查看5118,需要登录">5118</a></span>
							<span class="fmtStandardDate" title="<fmt:formatDate value="${qzSetting.standardTime}" pattern="yyyy-MM-dd"></fmt:formatDate>">达标时间:<a href="javascript:;">
								<c:if test="${qzSetting.standardTime == null}">无</c:if>
								<c:if test="${qzSetting.standardTime != null}"><fmt:formatDate value="${qzSetting.standardTime}" pattern="yyyy-MM-dd"></fmt:formatDate></c:if>
							</a></span>
							<span class="tagNames" ondblclick="editTagNameStr($(this).find('label.tagNameStr')[0], true)"><label>分组标签:</label>&nbsp;&nbsp;<label class="tagNameStr">暂无</label></span>
							<span class="fmtCreateDate" title="<fmt:formatDate value="${qzSetting.createTime}" pattern="yyyy-MM-dd"></fmt:formatDate>">创建时间:<a href="javascript:;"><fmt:formatDate value="${qzSetting.createTime}" pattern="yyyy-MM-dd"></fmt:formatDate></a></span>
							<div class="handle">
                                <a class="blue" href="javascript:showExcludeCustomerKeywordDialog('${qzSetting.uuid}','${qzSetting.customerUuid}','${qzSetting.domain.trim()}','${qzSetting.phoneGroup}','Phone')">排除关键字</a>
								<shiro:hasPermission name="/internal/qzsetting/save">
									<a class="blue" href="javascript:showKeywordDialog('${qzSetting.uuid}','${qzSetting.customerUuid}','${qzSetting.domain.trim()}','${qzSetting.phoneGroup}','${qzSetting.bearPawNumber}')">指定关键字</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/qzsetting/save">
									<a class="blue" href="javascript:openMessageBox('全站设置', '${qzSetting.customerUuid}', '${qzSetting.contactPerson}')">用户留言</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/usernotebook/searchUserNoteBooks">
									<a class="blue" href="javascript:openNoteBookDialog('${qzSetting.customerUuid}', '${qzSettingSearchCriteria.terminalType}')">记事本</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/qzchargelog/save">
									<a class="blue" href="javascript:showChargeDialog('${qzSetting.uuid}','${qzSetting.contactPerson}','${qzSetting.domain.trim()}',this)">收费</a>
								</shiro:hasPermission>
								<shiro:hasPermission name="/internal/qzsetting/save">
									<a class="blue" href="javascript:;" onclick="showSettingDialog($(this))">修改</a>
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
								<div class="user-id">
									<a href="javascript:;">
										<span class="line1">${qzSetting.userID}</span>
										<span>用户名称</span>
									</a>
								</div>

								<div class="standard-info">
									<a href="javascript:;">
										<span class="line1">
											<a href="javascript:;" qzsettinguuid="${qzSetting.uuid}" <c:if test="${!isSEO}">onmouseover="showChargeRulesDiv($(this))" onmouseout="closeChargeRulesDiv()"</c:if>>
												<c:if test="${qzSetting.organizationName != '优化部'}">
													<c:if test="${qzSetting.totalPrice > 0}">
														<font style="background-color: mediumseagreen;font-size: 14px;">￥ ${qzSetting.totalPrice}</font>
													</c:if>
													<c:if test="${qzSetting.totalPrice == 0}">
														<font style="background-color: palevioletred;font-size: 14px;">￥ ${qzSetting.totalPrice}</font>
													</c:if>
												</c:if>
											</a>
										</span>
										<span>全站达标信息</span>
									</a>
								</div>

								<div class="other-rank">
									<div class="row">
										<div>
											<span class="line1">
												<a href="javascript:;" name="fIsMonitor">
														${qzSetting.fIsMonitor == true ? "是" : "否"}
												</a>
											</span>
											<span>
												<a href="javascript:;">达标监控</a>
											</span>
										</div>
										<div>
										<span class="line1">
											<a href="javascript:;">
												<c:choose>
													<c:when test="${qzSetting.crawlerStatus == 'new'}">
														<span style="color: indianred;">未爬取</span>
													</c:when>
													<c:when test="${qzSetting.crawlerStatus == 'processing'}">
														<span style="color: darkorange;">爬取中</span>
													</c:when>
													<c:otherwise>
														<span style="color: forestgreen;">爬取完成</span>
													</c:otherwise>
												</c:choose>
											</a>
										</span>
											<span>
											<a href="javascript:;">爬取状态</a>
										</span>
										</div>

										<div name="operationKeywordNum" title="点击链接跳转到关键字列表">
										<span class="line1">
											<a target="_blank" href="javascript:searchCustomerKeywords('${qzSetting.customerUuid}', '${qzSetting.phoneGroup}');">0</a>
										</span>
											<span>
											<a href="javascript:;">操作词数</a>
										</span>
										</div>
									</div>

									<div class="row">
										<div>
											<span class="line1">
												<a href="javascript:;">
														${qzSetting.fIsReady == true ? "是" : "否"}
												</a>
											</span>
											<span>
												<a href="javascript:;">达标计划</a>
											</span>
										</div>
										<div>
											<span class="line1">
												<input type="hidden">
												<a href="javascript:;">
													<c:choose>
														<c:when test="${qzSetting.renewalStatus == 1}">
															<span style="color: green;">续费</span>
														</c:when>
														<c:otherwise>
															<span style="color: red;">暂停续费</span>
														</c:otherwise>
													</c:choose>
												</a>
											</span>
												<span>
												<a href="javascript:;">续费状态</a>
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
												<span>
												<a href="javascript:;">采集状态</a>
											</span>
										</div>
									</div>
								</div>

								<div class="other-rank_2">
									<div class="row">
										<div title="站点所属优化组">
										<span class="line1">
											<a href="javascript:;">${qzSetting.phoneGroup == null or qzSetting.phoneGroup == "" ? "暂无" : qzSetting.phoneGroup}</a>
										</span>
											<span>
											<a href="javascript:;">优化分组</a>
										</span>
										</div>
									</div>
									<div class="row">
										<div title="该分组属于此操作组合, 点击链接跳转到分组设置">
											<div class="showOperationCombineName" name="showOperationCombineName">
												<input type="hidden" name="operationCombineName" value="">
												<select name="operationCombineName" onchange="changeQZSettingGroupOperationCombineUuid(this, '${qzSetting.phoneGroup}', '${qzSettingSearchCriteria.loginName}', '${isSEO}');" style="width: 180px;">
													<option value=""></option>
													<c:forEach items="${operationCombines}" var="operationCombine">
														<option>${operationCombine}</option>
													</c:forEach>
												</select>
											</div>
											<div class="operationCombineSpan">
											<span>
												<a href="javascript:;">操作组合</a>
											</span>
											</div>
										</div>
									</div>
								</div>
							</div>

                            <c:if test="${qzSetting.searchEngine == '百度'}">
                                <div class="rank-wrap">
                                    <c:choose>
                                        <c:when test="${qzSetting.qzKeywordRankInfoMap['Phone'] != null}">
                                            <c:choose>
                                                <c:when test="${qzSetting.qzKeywordRankInfoMap['Phone']['aiZhan'] != null}">
                                                    <div name="rankInfo" style="display: none;">
                                                        <span name="aiZhan">${qzSetting.qzKeywordRankInfoMap['Phone']['aiZhan']}</span>
                                                    </div>
                                                    <div class="col-1" id="keywordRecordCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
                                                    <div class="col-2" id="keywordTrendCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
                                                    <div class="col-4 top50-link">
                                                        <div class="row4">
                                                            <span>前10</span>
                                                            <span class="top10">
                                            <a href="javascript:;" id="PhoneTop10">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>前50</span>
                                                            <span class="top50">
                                            <a href="javascript:;" id="PhoneTop50">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>初始前10</span>
                                                            <span class="top10">
                                            <a href="javascript:;" id="PhoneTopCreate10">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>初始前50</span>
                                                            <span class="top50">
                                            <a href="javascript:;" id="PhoneTopCreate50">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>曲线达标</span>
                                                            <span class="isStandard">
                                            <a href="javascript:;" id="PhoneIsStandard">否</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>达标时间</span>
                                                            <span class="standardTime">
                                            <a href="javascript:;" id="PhoneStandardTime">无</a>
                                        </span>
                                                        </div>
                                                    </div>
                                                </c:when>
                                                <c:when test="${qzSetting.qzKeywordRankInfoMap['Phone']['5118'] != null}">
                                                    <div name="rankInfo" style="display: none;">
                                                        <span name="5118">${qzSetting.qzKeywordRankInfoMap['Phone']['5118']}</span>
                                                    </div>
                                                    <div class="col-1" id="keywordRecordCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
                                                    <div class="col-2" id="keywordTrendCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
                                                    <div class="col-4 top50-link">
                                                        <div class="row4">
                                                            <span>前10</span>
                                                            <span class="top10">
                                            <a href="javascript:;" id="PhoneTop10">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>前50</span>
                                                            <span class="top50">
                                            <a href="javascript:;" id="PhoneTop50">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>初始前10</span>
                                                            <span class="top10">
                                            <a href="javascript:;" id="PhoneTopCreate10">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>初始前50</span>
                                                            <span class="top50">
                                            <a href="javascript:;" id="PhoneTopCreate50">0</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>曲线达标</span>
                                                            <span class="isStandard">
                                            <a href="javascript:;" id="PhoneIsStandard">否</a>
                                        </span>
                                                        </div>
                                                        <div class="row4">
                                                            <span>达标时间</span>
                                                            <span class="standardTime">
                                            <a href="javascript:;" id="PhoneStandardTime">无</a>
                                        </span>
                                                        </div>
                                                    </div>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:if test="${qzSetting.qzKeywordRankInfoMap['Phone']['aiZhan'] != null or qzSetting.qzKeywordRankInfoMap['Phone']['5118'] != null}">
                                                        <div name="rankInfo" style="display: none;"></div>
                                                        <div class="col-1">
                                                            <h1>暂无数据</h1>
                                                        </div>
                                                        <div class="col-2">
                                                            <h1>暂无数据</h1>
                                                        </div>
                                                        <div class="col-4 top50-link">
                                                            <div class="row4">
                                                                <span>前10</span>
                                                                <span class="top10">
                                            <a href="javascript:;" id="PhoneTop10">0</a>
                                        </span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>前50</span>
                                                                <span class="top50">
                                            <a href="javascript:;" id="PhoneTop50">0</a>
                                        </span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>初始前10</span>
                                                                <span class="top10">
                                            <a href="javascript:;" id="PhoneTopCreate10">0</a>
                                        </span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>初始前50</span>
                                                                <span class="top50">
                                            <a href="javascript:;" id="PhoneTopCreate50">0</a>
                                        </span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>曲线达标</span>
                                                                <span class="isStandard">
                                            <a href="javascript:;" id="PhoneIsStandard">否</a>
                                        </span>
                                                            </div>
                                                            <div class="row4">
                                                                <span>达标时间</span>
                                                                <span class="standardTime">
                                            <a href="javascript:;" id="PhoneStandardTime">无</a>
                                        </span>
                                                            </div>
                                                        </div>
                                                    </c:if>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:when>
                                        <c:otherwise>

                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </c:if>

                            <div class="rank-wrap1">
								<c:choose>
									<c:when test="${qzSetting.qzKeywordRankInfoMap['Phone'] != null}">
										<c:choose>
											<c:when test="${qzSetting.qzKeywordRankInfoMap['Phone']['designationWord'] != null}">
												<div name="rankInfo" style="display: none;">
													<span name="designationWord">${qzSetting.qzKeywordRankInfoMap['Phone']['designationWord']}</span>
												</div>
												<div class="col-1" id="designationWordCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
												<div class="col-5 top50-link">
													<div class="row4">
														<span>前10</span>
														<span class="top10">
											<a href="javascript:;" id="PhoneTop10">0</a>
										</span>
													</div>
													<div class="row4">
														<span>前50</span>
														<span class="top50">
                                            <a href="javascript:;" id="PhoneTop50">0</a>
                                        </span>
													</div>
													<div class="row4">
														<span>初始前10</span>
														<span class="top10">
                                            <a href="javascript:;" id="PhoneTopCreate10">0</a>
                                        </span>
													</div>
													<div class="row4">
														<span>初始前50</span>
														<span class="top50">
                                            <a href="javascript:;" id="PhoneTopCreate50">0</a>
                                        </span>
													</div>
													<div class="row4">
														<span>曲线达标</span>
														<span class="isStandard">
                                            <a href="javascript:;" id="PhoneIsStandard">否</a>
                                        </span>
													</div>
													<div class="row4">
														<span>达标时间</span>
														<span class="standardTime">
                                            <a href="javascript:;" id="PhoneStandardTime">无</a>
                                        </span>
													</div>
												</div>
											</c:when>
											<c:otherwise>
												<c:if test="${qzSetting.qzKeywordRankInfoMap['Phone']['designationWord'] != null}">
													<div name="rankInfo" style="display: none;"></div>
													<div class="col-1">
														<h1>暂无数据</h1>
													</div>
													<div class="col-5 top50-link">
														<div class="row4">
															<span>前10</span>
															<span class="top10">
											<a href="javascript:;" id="PhoneTop10">0</a>
										</span>
														</div>
														<div class="row4">
															<span>前50</span>
															<span class="top50">
                                            <a href="javascript:;" id="PhoneTop50">0</a>
                                        </span>
														</div>
														<div class="row4">
															<span>初始前10</span>
															<span class="top10">
                                            <a href="javascript:;" id="PhoneTopCreate10">0</a>
                                        </span>
														</div>
														<div class="row4">
															<span>初始前50</span>
															<span class="top50">
                                            <a href="javascript:;" id="PhoneTopCreate50">0</a>
                                        </span>
														</div>
														<div class="row4">
															<span>曲线达标</span>
															<span class="isStandard">
                                            <a href="javascript:;" id="PhoneIsStandard">否</a>
                                        </span>
														</div>
														<div class="row4">
															<span>达标时间</span>
															<span class="standardTime">
                                            <a href="javascript:;" id="PhoneStandardTime">无</a>
                                        </span>
														</div>
													</div>
												</c:if>
											</c:otherwise>
										</c:choose>
									</c:when>
									<c:otherwise>

									</c:otherwise>
								</c:choose>
                            </div>
						</div>
					</li>
					<!--li-end-phone-->
					</c:when>
				</c:choose>
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

<form id="searchCustomerKeywordForm" method="post" target="_blank" action="/internal/customerKeyword/searchCustomerKeywords" style="display: none;">
	<input type="text" name="customerUuid" id="customerUuid">
	<input type="text" name="optimizeGroupName" id="optimizeGroupName">
	<input type="text" name="status" id="status">
</form>

<div id="showAllOperationType" class="easyui-dialog" style="display: none;"></div>
<%--增加,修改对话框--%>
<div id="changeSettingDialog" class="easyui-dialog" style="display: none;left: 40%;">
	<form id="changeSettingForm">
	<table style="font-size:12px" id="settingTable" align="center" cellspacing="5">
		<tr>
			<td style="width:65px" align="right">客户</td>
			<td>
				<input type="hidden" id="qzSettingUuid" />
				<input type="text" list="customer_list" name="qzSettingCustomer" id="qzSettingCustomer" style="width:240px" />
			</td>
			<td style="width:65px" align="right">标签</td>
			<td>
				<input type="text" name="qzCategoryTagNames" id="qzCategoryTagNames" placeholder="按正确方式输入：阿卡索,MBA,算法" style="width:240px">
			</td>
		</tr>
		<tr>
			<td style="width:65px" align="right">域名</td>
			<td>
				<input type="text" name="qzSettingDomain" id="qzSettingDomain" style="width:240px" />
			</td>
			<td style="width:65px" align="right">入口</td>
			<td>
				<select name="qzSettingEntryType" id="qzSettingEntryType" style="width:240px">
					<option value="qz" selected>全站</option>
					<option value="pt">普通</option>
					<option value="fm">负面</option>
					<option value="bc">其他</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="width:65px" align="right">熊掌号</td>
			<td>
				<input type="text" name="bearPawNumber" id="bearPawNumber" style="width:240px" />
			</td>
			<td style="width:65px" align="right"><c:if test="${not isSEO}">达标计划</c:if></td>
			<td>
				<c:if test="${not isSEO}">
					<select name="qzSettingJoinReady" id="qzSettingJoinReady" style="width:240px">
						<option value="1">是</option>
						<option value="0" selected>否</option>
					</select>
				</c:if>
			</td>
		</tr>
        <tr>
            <td style="width:65px" align="right">搜索引擎</td>
            <td>
                <select name="searchEngine" id="searchEngine" style="width:240px">
                    <c:forEach items="${searchEngineMap}" var="entry">
                        <c:choose>
                            <c:when test="${entry.key eq qzSettingSearchCriteria.searchEngine}">
                                <option value="${entry.value}" selected>${entry.key}</option>
                            </c:when>
                            <c:otherwise>
                                <option value="${entry.value}">${entry.key}</option>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </select>
            </td>
            <td style="width:65px" align="right">达标监控</td>
            <td>
				<select name="qzSettingStartMonitor" id="qzSettingStartMonitor"  style="width:240px">
					<option value="1">是</option>
					<option value="0" selected>否</option>
				</select>
			</td>
        </tr>
	</table>
	<table style="font-size:12px" cellspacing="5">
		<tr>
			<td colspan="4" class="split_line"></td>
		</tr>
		<tr id="optimizationType">
			<td colspan="2" width="325px">
				<input type="checkbox" name="operationType" id="PC" status="1" onclick="showOptimizationType('PC')" style=""/>&nbsp;电脑&nbsp;
				<div style="display: none" id="optimizationTypePC">
					达标优化类型&nbsp;&nbsp;
					<input type="checkbox" name="optimizationType" onclick="dealSettingTable(this, 'PC', 1)" value="1" />&nbsp;主优化&nbsp;
					<c:if test="${isBaiduEngine}">
						<input type="checkbox" name="optimizationType" onclick="dealSettingTable(this, 'PC', 1)" value="2" />&nbsp;次优化&nbsp;
						<input type="checkbox" name="optimizationType" onclick="dealSettingTable(this, 'PC', 1)" value="0" />&nbsp;辅助优化&nbsp;
					</c:if>
				</div>
			</td>
			<td colspan="2" width="325px">
				<input type="checkbox" name="operationType" id="Phone" status="1" onclick="showOptimizationType('Phone')" style=""/>&nbsp;手机&nbsp;
				<div style="display: none;" id="optimizationTypePhone">
					达标优化类型&nbsp;&nbsp;
					<input type="checkbox" name="optimizationType" onclick="dealSettingTable(this, 'Phone', 1)" value="1" />&nbsp;主优化&nbsp;
					<c:if test="${isBaiduEngine}">
						<input type="checkbox" name="optimizationType" onclick="dealSettingTable(this, 'Phone', 1)" value="2" />&nbsp;次优化&nbsp;
						<input type="checkbox" name="optimizationType" onclick="dealSettingTable(this, 'Phone', 1)" value="0" />&nbsp;辅助优化&nbsp;
					</c:if>
				</div>
			</td>
		</tr>
		<tr>
			<%--电脑分组信息--%>
			<td colspan="2" id="groupHeightPC" width="325px">
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
						<td align="right" style="width:72px">当前词量</td>
						<td colspan="4"><input type="text" name="currentKeywordCount" id="currentKeywordCountPC" style="width:240px;margin-left: -6;" readonly/></td>
					</tr>
					<shiro:hasPermission name="/internal/qzsetting/delete">
						<c:if test="${not isSEO}">
							<tr>
								<td align="right" style="width:72px">限制词量</td>
								<td colspan="4"><input type="text" name="maxKeywordCount" id="maxKeywordCountPC" style="width:240px;margin-left: -6;"/></td>
							</tr>
						</c:if>
					</shiro:hasPermission>
					<input type="hidden" id="qzSettingUuidPC" name="qzOperationTypeUuid" value="" />
					<c:if test="${not isSEO}">
						<tr>
							<td align="right" style="width:72px" valign="top">达标备注</td>
                            <td><textarea name="monitorRemark" id="monitorRemarkPC" placeholder="请填写电脑端达标备注, 如：首月30-50个词 客供词 3000" style="width:240px; height: 40px; margin-left: -6;resize: none"></textarea></td>
						</tr>
						<tr id="standardSpeciesPC">
							<td align="right" style="width:72px"><label>达标种类</label></td>
							<td title="必选一个">
								<c:if test="${isBaiduEngine}">
									<div style="display: inline-block">
										<input type="checkbox" name="standardSpecies" id="aiZhanPCStandardSpecies" onclick="checkedStandardSpecies(this, 'PC')" value="aiZhan">
									</div>
									<div style="display: inline-block; margin-right: 10px;">
										<label>爱站</label>
									</div>
									<div style="display: inline-block">
										<input type="checkbox" name="standardSpecies" id="5118PCStandardSpecies" onclick="checkedStandardSpecies(this, 'PC')" value="5118">
									</div>
									<div style="display: inline-block; margin-right: 10px;">
										<label>5118</label>
									</div>
								</c:if>
								<div style="display: inline-block">
									<input type="checkbox" name="standardSpecies" id="designationWordPCStandardSpecies" onclick="checkedStandardSpecies(this, 'PC')" value="designationWord">
								</div>
								<div style="display: inline-block; margin-right: 10px;">
									<label>指定词</label>
								</div>
								<c:if test="${isBaiduEngine}">
									<div style="display: inline-block">
										<input type="checkbox" name="standardSpecies" id="otherPCStandardSpecies" onclick="checkedStandardSpecies(this, 'PC')" value="other">
									</div>
									<div style="display: inline-block; margin-right: 10px;">
										<label>其他</label>
									</div>
								</c:if>
							</td>
						</tr>
					</c:if>
				</table>
			</td>
			<%--手机分组信息--%>
			<td colspan="2" id="groupHeightPhone" width="325px">
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
						<td align="right" style="width:72px">当前词量</td>
						<td colspan="4"><input type="text" name="currentKeywordCount" id="currentKeywordCountPhone"  style="width:240px;margin-left: -6;" readonly/></td>
					</tr>
					<shiro:hasPermission name="/internal/qzsetting/delete">
						<c:if test="${not isSEO}">
							<tr>
								<td align="right" style="width:72px">限制词量</td>
								<td colspan="4"><input type="text" name="maxKeywordCount" id="maxKeywordCountPhone" style="width:240px;margin-left: -6;"/></td>
							</tr>
						</c:if>
					</shiro:hasPermission>
					<input type="hidden" id="qzSettingUuidPhone" name="qzOperationTypeUuid" value="" />
					<c:if test="${not isSEO}">
						<tr>
							<td align="right" style="width:72px" valign="top">达标备注</td>
                            <td><textarea name="monitorRemark" id="monitorRemarkPhone" placeholder="请填写移动端达标备注, 如：首月30-50个词 客供词 3000" style="width:240px; height: 40px; margin-left: -6;resize: none"></textarea></td>
						</tr>
						<tr id="standardSpeciesPhone">
							<td align="right" style="width:72px"><label>达标种类</label></td>
							<td title="必选一个">
								<c:if test="${isBaiduEngine}">
									<div style="display: inline-block">
										<input type="checkbox" name="standardSpecies" id="aiZhanPhoneStandardSpecies" onclick="checkedStandardSpecies(this, 'Phone')" value="aiZhan">
									</div>
									<div style="display: inline-block; margin-right: 10px;">
										<label>爱站</label>
									</div>
									<div style="display: inline-block">
										<input type="checkbox" name="standardSpecies" id="5118PhoneStandardSpecies" onclick="checkedStandardSpecies(this, 'Phone')" value="5118">
									</div>
									<div style="display: inline-block; margin-right: 10px;">
										<label>5118</label>
									</div>
								</c:if>
								<div style="display: inline-block">
									<input type="checkbox" name="standardSpecies" id="designationWordPhoneStandardSpecies" onclick="checkedStandardSpecies(this, 'Phone')" value="designationWord">
								</div>
								<div style="display: inline-block; margin-right: 10px;">
									<label>指定词</label>
								</div>
								<c:if test="${isBaiduEngine}">
									<div style="display: inline-block">
										<input type="checkbox" name="standardSpecies" id="otherPhoneStandardSpecies" onclick="checkedStandardSpecies(this, 'Phone')" value="other">
									</div>
									<div style="display: inline-block; margin-right: 10px;">
										<label>其他</label>
									</div>
								</c:if>
							</td>
						</tr>
					</c:if>
				</table>
			</td>
		</tr>
		<c:if test="${not isSEO}">
			<tr id="chargeRuleTable">
				<%--电脑规则信息--%>
				<td colspan="2" id="ruleHeightPC" style="width: 325px;" valign="top">
					<table border="1" style="display:none; width: 325px; font-size:12px;" cellspacing="0" cellpadding="0" id="chargeRulePC">
						<thead>
						<tr>
							<td align="center" colspan="5">爱站收费规则</td>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td style="width:52px">达标阶段</td>
							<td style="width:76px">起始达标词数</td>
							<td style="width:76px">终止达标词数</td>
							<td style="width:66px">电脑端价格</td>
							<td style="width:46px">操作</td>
						</tr>
						<tr>
							<td colspan="5">
								<input name="addRule" type="button" value="增加收费规则" onclick="addRow('chargeRulePC')" />
							</td>
						</tr>
						</tbody>
					</table>
				</td>
				<%--手机规则信息--%>
				<td colspan="2" id="ruleHeightPhone" style="width: 325px;" valign="top">
					<table border="1" style="display:none; width: 325px; font-size:12px;" cellspacing="0" cellpadding="0" id="chargeRulePhone">
						<thead>
						<tr>
							<td align="center" colspan="5">爱站收费规则</td>
						</tr>
						</thead>
						<tbody>
						<tr>
							<td style="width:52px">达标阶段</td>
							<td style="width:76px">起始达标词数</td>
							<td style="width:76px">终止达标词数</td>
							<td style="width:66px">手机端价格</td>
							<td style="width:46px">操作</td>
						</tr>
						<tr>
							<td colspan="5">
								<input name="addRule" type="button" value="增加收费规则" onclick="addRow('chargeRulePhone')" />
							</td>
						</tr>
						</tbody>
					</table>
				</td>
			</tr>
		</c:if>
		<tr>
			<td colspan="4" class="split_line"></td>
		</tr>
	</table>
	<table style="font-size:12px" cellspacing="5">
		<shiro:hasPermission name="/internal/qzsetting/startMonitorImmediately">
			<c:if test="${not isSEO}">
				<c:if test="${isBaiduEngine}">
					<tr>
						<td style="width:65px" align="right">去掉没指数</td>
						<td>
							<select name="qzSettingIgnoreNoIndex" id="qzSettingIgnoreNoIndex"  style="width:240px">
								<option value="1" selected>是</option>
								<option value="0">否</option>
							</select>
						</td>
						<td style="width:65px" align="right">去掉没排名</td>
						<td>
							<select name="qzSettingIgnoreNoOrder" id="qzSettingIgnoreNoOrder"  style="width:240px">
								<option value="1" selected>是</option>
								<option value="0">否</option>
							</select>
						</td>
					</tr>
				</c:if>
				<c:if test="${isBaiduEngine}">
					<tr>
						<td style="width:65px" align="right">爬取关键字</td>
						<td>
							<select name="qzSettingAutoCrawlKeywordFlag" id="qzSettingAutoCrawlKeywordFlag" style="width:240px">
								<option value="1">是</option>
								<option value="0" selected>否</option>
							</select>
						</td>
						<td style="width:65px" align="right">更新间隔</td>
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
				</c:if>
			</c:if>
		</shiro:hasPermission>
	</table>
	</form>
</div>
<datalist id="customer_list">
	<c:forEach items="${customerList}" var="customer">
		<option>${customer.contactPerson} ${'_____'} ${customer.uuid}</option>
	</c:forEach>
</datalist>
<datalist id="categoryTag_list">
	<c:forEach items="${tagNameList}" var="tagName">
		<option>${tagName}</option>
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
<%--更新队列Dialog--%>
<div id="getAvailableQZSettings" class="easyui-dialog" style="display: none">
	<textarea id="getAvailableQZSettingsContent"  style="width:100%;height:100%;resize: none"></textarea>
</div>
<%--指定关键字Dialog--%>
<div id="customerKeywordDialog" class="easyui-dialog" style="display: none">
	<form id="customerKeywordForm">
		<table style="font-size:12px" id="customerKeywordTable" align="center" cellspacing="5">
			<tr>
				<td style="width:60px" align="right">域名</td>
				<td>
					<input type="hidden" name="qzSettingUuid" id="qzSettingUuid" />
					<input type="hidden" name="customerUuid" id="customerUuid" />
					<input type="text" name="domain" id="domain" style="width:240px" readonly="readonly" />
				</td>
			</tr>
			<tr>
				<td align="right" style="margin-right:4px;">入口</td>
				<td>
					<select name="qzSettingEntryType" id="qzSettingEntryType" style="width:240px">
						<option value="qz" selected>全站</option>
						<option value="pt">普通</option>
						<option value="fm">负面</option>
						<option value="bc">其他</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right" style="margin-right:4px;">引擎</td>
				<td>
					<select name="searchEngine" id="searchEngine" style="width:240px">
						<c:forEach items="${searchEngineMap}" var="entry">
							<c:choose>
								<c:when test="${entry.key eq qzSettingSearchCriteria.searchEngine}">
									<option value="${entry.value}" selected>${entry.key}</option>
								</c:when>
								<c:otherwise>
									<option value="${entry.value}">${entry.key}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
			</tr>
			<tr>
				<td style="width:60px" align="right">熊掌号</td>
				<td>
					<input type="text" name="bearPawNumber" id="bearPawNumber" style="width:240px" placeholder="网站若有熊掌号，请输入熊掌号" />
				</td>
			</tr>
            <tr>
                <td style="width:60px" align="right">关键词作用</td>
                <td>
                    <select name="keywordEffect" id="keywordEffect" style="width: 100px" title="">
						<option value="Common" selected="selected">一般词</option>
						<option value="Appointment">指定词</option>
                        <option value="Curve">曲线词</option>
                        <option value="Present">赠送词</option>
                    </select>
                </td>
            </tr>
			<tr>
				<td style="width:60px" align="right">优化组</td>
				<td>
					<input type="text" name="optimizeGroupName" id="optimizeGroupName" style="width:240px" placeholder="可不填写，默认这个站的分组" />
				</td>
			</tr>
			<tr>
				<td style="width:60px" align="right">机器分组</td>
				<td>
					<input type="text" name="machineGroupName" id="machineGroupName" style="width:240px" value="Default" placeholder="不填写，默认Default" />
				</td>
			</tr>
			<tr>
				<td style="width:60px" align="right"><input type="checkbox" name="synchronousAddition" id="synchronousAddition"></td>
				<td>
					<label style="width: 240px;">PC和Phone同步加词</label>
				</td>
			</tr>
			<tr>
				<td style="width:60px;vertical-align:top" align="right">关键字</td>
				<td><textarea id="customerKeywordDialogContent" style="width:240px; height: 250px; resize: none"></textarea></td>
			</tr>
		</table>
	</form>
</div>
<%--指定排除词Dialog--%>
<div id="excludeCustomerKeywordDialog" class="easyui-dialog" style="display: none">
    <form id="excludeCustomerKeywordForm">
        <table style="font-size:12px" id="excludeCustomerKeywordTable" align="center" cellspacing="5">
            <tr>
                <td style="width:60px" align="right">域名</td>
                <td>
					<input type="hidden" name="excludeKeywordUuid" id="excludeKeywordUuid" />
                    <input type="hidden" name="qzSettingUuid" id="qzSettingUuid" />
                    <input type="hidden" name="customerUuid" id="customerUuid" />
                    <input type="text" name="domain" id="domain" style="width:240px" readonly/>
                </td>
            </tr>
            <tr>
                <td align="right" style="margin-right:4px;">终端类型</td>
                <td>
                    <select name="terminalType" id="terminalType" style="width:240px" onchange="echoExcludeKeyword()">
                        <option value="PC">PC</option>
                        <option value="Phone">Phone</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td style="width:60px" align="right">关键字</td>
            </tr>
            <tr>
                <td style="width:60px" align="right"></td>
                <td><textarea id="customerKeywordDialogContent" style="width:240px; height: 260px; resize: none"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<%--收费详情列表--%>
<div id="chargeLogListDiv" class="easyui-dialog" style="display:none;left: 40%;">
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
<%--留言栏Dialog--%>
<div id="showUserMessageDialog" class="easyui-dialog" style="display: none">
    <form id="showUserMessageForm" onsubmit="return false">
        <table cellpadding="10" style="font-size: 12px; background-color: white; border-collapse:separate; border-spacing:0px 10px;">
            <input type="hidden" name="messageUuid" value="">
			<input type="hidden" name="openDialogStatus" value="${qzSettingSearchCriteria.openDialogStatus}">
			<input type="hidden" name="customerUuid" value="${qzSettingSearchCriteria.customerUuid}"/>
            <tr>
                <td width="60px"><span style="width: 60px">写信人:</span></td>
                <td width="80px"><span style="width: 80px;" id="senderUserName">${sessionScope.get("username")}</span></td>
                <td><span style="width: 60px">收信人:</span></td>
                <td width="180px">
                    <select id="user_select" multiple="multiple"></select>
                </td>
            </tr>
            <tr>
                <td><span style="width: 40px">时&nbsp;&nbsp;间:</span></td>
                <td width="80px"><label style="width: 80px;"></label></td>
                <td width="60px"><span style="width: 60px">客&nbsp;&nbsp;户:</span></td>
                <td><span style="width: 200px;" id="contactPerson"></span></td>
            </tr>
			<tr>
				<td><span style="width: 40px">状&nbsp;&nbsp;态:</span></td>
				<td><span style="width: 100px;" id="messageStatus"></span></td>
			</tr>
            <tr>
                <td width="60px"><span style="width: 60px">内&nbsp;&nbsp;容:</span></td>
                <td colspan="3"><input type="text" name="content" style="width: 320px;"></td>
            </tr>
        </table>
		<table id="userMessageListTable" border="1" cellpadding="10"  style="font-size: 12px;background-color: white;border-collapse: collapse;width:100%;">
			<tr>
				<td class="user-message-targetName">
					<span>序号</span>
				</td>
				<td class="user-message-content">
					<span>留言内容</span>
				</td>
				<td class="user-message-status">
					<span>收信人</span>
				</td>
				<td class="user-message-status">
					<span>处理状态</span>
				</td>
			</tr>
		</table>
    </form>
</div>
<%--记事本Dialog--%>
<div id="showUserNoteBookDialog" class="easyui-dialog" style="display: none">
	<form id="showUserNoteBookForm" onsubmit="return false">
		<div id="userNoteBookDialogToolBar">
			<input type="hidden" name="customerUuid" value="">
			<input type="hidden" name="terminalType" value="">
			&nbsp;&nbsp;<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showAddUserNoteDiv()" value=" 增加 " >
			&nbsp;&nbsp;<input class="ui-button ui-widget ui-corner-all" type="button" onclick="searchUserNoteBooks(1)" value=" 查询所有 " >
			&nbsp;&nbsp;<input class="ui-button ui-widget ui-corner-all" type="button" onclick="searchUserNoteBooks(0)" value=" 查询 " >
		</div>
		<table id="userNoteBookTable" cellpadding="10" style="font-size: 12px; background-color: white; border-collapse:separate; border-spacing:0px 8px; width: 100%;">
			<thead>
				<tr>
					<td>序号</td>
                    <td>记录者</td>
                    <td>内容</td>
					<td>日期</td>
					<td>操作</td>
				</tr>
			</thead>
			<tbody>
			</tbody>
		</table>
		<div id="addUserNote" style="display: none;">
			<table id="addUserNoteTable" cellpadding="10" style="font-size: 12px; background-color: white; border-collapse:separate; border-spacing:0px 5px; width: 100%;">
				<input type="hidden" name="userNoteBookUuid" id="userNoteBookUuid" value="">
				<tr>
					<td>内容:</td>
				</tr>
				<tr>
					<td>
						<textarea style="width: 100%;"></textarea>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;<input class="ui-button ui-widget ui-corner-all" type="button" onclick="saveUserNoteBook()" value=" 保存 " >
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
<%--达标信息详情DIV--%>
<div id="chargeRulesDiv" style="display:none;width:300px;">
	<table id="chargeRulesDivTable" border="1" cellpadding="10" style="font-size: 12px;background-color: white;border-collapse: collapse;margin: 10px 10px;width:92%;"></table>
</div>

<div id="targetQzCategoryTagsDialog" class="easyui-dialog" style="left: 30%;display: none;">
	目标分组标签:<input type="text" id="targetQzCategoryTags" name="targetQzCategoryTags" style="width:200px; margin-top: 10px" placeholder="按正确方式输入：阿卡索,MBA,算法">
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath}/js/echarts.min.js"></script>
<script src="${staticPath }/qzsetting/list.js"></script>
<script src="${staticPath }/static/UserMessageCommon.js"></script>
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

        var flag = false;
        window.onclick = function (e) {
            if ($("#showAllOperationType").css("display") === "block") {
                if (flag &&!$("#showAllOperationType").is(e.target) && $("#showAllOperationType").has(e.target).length === 0 ){
                    $("#showAllOperationType").dialog("close");
                    flag = false;
                    return;
                }
                flag = true;
            }
        }
    });

</script>
</body>
</html>