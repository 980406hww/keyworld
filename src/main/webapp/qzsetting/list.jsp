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
			<li name="PC"><a href="javascript:;" onclick="checkTerminalType('PC', true)">百度PC</a></li>
			<li name="Phone"><a href="javascript:;" onclick="checkTerminalType('Phone', true)">百度Phone</a></li>
		</ul>
		<div class="conn">
			<ul>
				<li>
					<input type="text" title="请输入网站域名" name="domain" placeholder="请输入网站域名" onkeydown="enterIn(event);" value="${qzSettingSearchCriteria.domain}">
				</li>

				<li>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="trimSearchCondition('1')" value=" 搜索 " >
					&nbsp;&nbsp;
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showMoreSearchCondition()" value=" 更多搜索条件 " >
				</li>
				<shiro:hasPermission name="/internal/qzsetting/save">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showSettingDialog(null, this)" value=" 增加 " >&nbsp;
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/updateImmediately">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateImmediately(this)" value=" 马上更新 " >&nbsp;
					</li>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/updateStatus">
					<li>
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateQZSettingStatus(0)" value=" 暂停整站 " >
						&nbsp;&nbsp;
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateQZSettingStatus(1)" value=" 激活整站 " >
						&nbsp;&nbsp;
						<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateQZSettingStatus(3)" value=" 暂停续费 " >
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
					<label name="lower" title="网站关键词(PC,Phone)一星期排名趋势涨幅&lt;${qzSettingSearchCriteria.lowerValue}">
						<input type="checkbox" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 1}">checked</c:if>>
						<i class="icon-rank-down"></i>骤降 (${qzSettingSearchCriteria.downNum == null ? 0 : qzSettingSearchCriteria.downNum})
					</label>
				</li>
				<li>
					<label name="unchanged" title="网站关键词(PC,Phone)一星期排名趋势涨幅不变">
						<input type="checkbox" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 6}">checked</c:if>>
						无变化 (${qzSettingSearchCriteria.unchangedNum == null ? 0 : qzSettingSearchCriteria.unchangedNum})
					</label>
				</li>
				<li>
					<label name="upper" title="网站关键词(PC,Phone)一星期排名趋势涨幅&gt;${qzSettingSearchCriteria.upperValue}">
						<input type="checkbox" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 2}">checked</c:if>>
						<i class="icon-rank-up"></i>暴涨 (${qzSettingSearchCriteria.upNum == null ? 0 : qzSettingSearchCriteria.upNum})
					</label>
				</li>
				<li>
					<label name="lowerDifference" title="网站关键词(PC,Phone)今天前10下降">
						<input type="checkbox" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 7}">checked</c:if>>
						下降 (${qzSettingSearchCriteria.downDifferenceNum == null ? 0 : qzSettingSearchCriteria.downDifferenceNum})
					</label>
				</li>
				<li>
					<label name="unchangedDifference" title="网站关键词(PC,Phone)今天前10不变">
						<input type="checkbox" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 8}">checked</c:if>>
						不变 (${qzSettingSearchCriteria.unchangedDifferenceNum == null ? 0 : qzSettingSearchCriteria.unchangedDifferenceNum})
					</label>
				</li>
				<li>
					<label name="upperDifference" title="网站关键词(PC,Phone)今天前10上升">
						<input type="checkbox" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 9}">checked</c:if>>
						上升 (${qzSettingSearchCriteria.upDifferenceNum == null ? 0 : qzSettingSearchCriteria.upDifferenceNum})
					</label>
				</li>
				<li>
					<label name="atLeastStandard" title="标识最少有一条规则达标">
						<input type="checkbox" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 3}">checked</c:if>>
						达标 (${qzSettingSearchCriteria.atLeastStandardNum == null ? 0 : qzSettingSearchCriteria.atLeastStandardNum})
					</label>
				</li>
				<li>
					<label name="neverStandard" title="标识一条规则都未达标">
						<input type="checkbox" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 4}">checked</c:if>>
						未达标 (${qzSettingSearchCriteria.neverStandardNum == null ? 0 : qzSettingSearchCriteria.neverStandardNum})
					</label>
				</li>
				<li>
					<label name="closeStandard" title="标识下条规则接近达标,未完成度&lt;${qzSettingSearchCriteria.differenceValue}">
						<input type="checkbox" name="checkbox" <c:if test="${qzSettingSearchCriteria.checkStatus == 5}">checked</c:if>>
						接近达标 (${qzSettingSearchCriteria.closeStandardNum == null ? 0 :qzSettingSearchCriteria.closeStandardNum})
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
				<li class="category condition">
					<span>分类标签: </span>
					<input type="text" list="categoryTag_list" name="categoryTag" value="${qzSettingSearchCriteria.categoryTag}">
				</li>
				<li class="group condition">
					<span>组名: </span>
					<input type="text" name="group" value="${qzSettingSearchCriteria.group}">
				</li>
				<li>
					<span>权重: </span>
					<span>
						<select name="weight" style="width: 50px;">
							<option></option>
							<c:forEach begin="0" end="9" step="1" var="weight">
								<c:choose>
									<c:when test="${weight eq qzSettingSearchCriteria.baiduWeight}"><option selected>${weight}</option></c:when>
									<c:otherwise><option>${weight}</option></c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</span>
				</li>
				<li>
					<span>状态: </span>
					<select name="status">
						<option value="" <c:if test="${qzSettingSearchCriteria.status == null}">selected</c:if>></option>
						<option value="1" <c:if test="${qzSettingSearchCriteria.status == 1}">selected</c:if>>激活</option>
						<option value="0" <c:if test="${qzSettingSearchCriteria.status == 0}">selected</c:if>>暂停</option>
						<option value="2" <c:if test="${qzSettingSearchCriteria.status == 2}">selected</c:if>>新增</option>
						<option value="3" <c:if test="${qzSettingSearchCriteria.status == 3}">selected</c:if>>暂停续费</option>
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
</div>

<form method="post" id="chargeForm" action="/internal/qzsetting/searchQZSettings">
	<input type="hidden" name="dateRangeType" id="dateRangeType" value="${qzSettingSearchCriteria.dateRangeType}"/>
	<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
	<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
	<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
	<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
	<input type="hidden" name="domain" id="domain" value="${qzSettingSearchCriteria.domain}"/>
	<input type="hidden" name="group" id="group" value="${qzSettingSearchCriteria.group}"/>
	<input type="hidden" name="customerUuid" id="customerUuid" value="${qzSettingSearchCriteria.customerUuid}"/>
	<input type="hidden" name="statusHidden" id="statusHidden" value="${qzSettingSearchCriteria.status}"/>
	<input type="hidden" name="customerInfo" id="customerInfo" value="${qzSettingSearchCriteria.customerInfo}">
	<input type="hidden" name="status" id="status" value="${qzSettingSearchCriteria.status}"/>
	<input type="hidden" name="updateStatus" id="updateStatus" value="${qzSettingSearchCriteria.updateStatus}"/>
	<input type="hidden" name="checkStatus" id="checkStatus" value="${qzSettingSearchCriteria.checkStatus}"/>
	<input type="hidden" name="terminalType" id="terminalType" value="${qzSettingSearchCriteria.terminalType}"/>
	<input type="hidden" name="categoryTag" id="categoryTag" value="${qzSettingSearchCriteria.categoryTag}"/>
	<input type="hidden" name="baiduWeight" id="baiduWeight" value="${qzSettingSearchCriteria.baiduWeight}"/>
</form>

<div class="datalist">
	<div class="datalist-list">
		<ul>
			<c:forEach items="${page.records}" var="qzSetting" varStatus="status">
				<c:choose>
					<c:when test="${qzSetting.pcGroup != null and (qzSetting.crawlerStatus != 'finish' or qzSetting.qzKeywordRankInfoMap['PC'] != null)}">
					<li>
						<div class="header">
							<span>
								<input type="checkbox" name="uuid" value="${qzSetting.uuid}" onclick="decideSelectAll();"/>
							</span>
							<span class="contactPerson-name" title="${qzSetting.contactPerson}"><a href="javascript:;">${qzSetting.contactPerson}</a></span>
							<span class="domain" title="${qzSetting.domain}"><a href="javascript:;">${qzSetting.domain}</a></span>
							<span class="to-aizhan"><a href="https://www.aizhan.com/cha/${qzSetting.domain}/" target="_blank" title="查看爱站">爱站</a></span>
							<span class="to-5118"><a  href="https://www.5118.com/seo/${qzSetting.domain}" target="_blank" title="查看5118,需要登录">5118</a></span>
							<span class="tagNames" ondblclick="editTagNameStr($(this).find('label.tagNameStr')[0], true)"><label>分组标签:</label>&nbsp;&nbsp;<label class="tagNameStr">暂无</label></span>
							<div class="handle">
                                <a class="blue" href="javascript:showExcludeCustomerKeywordDialog('${qzSetting.uuid}','${qzSetting.customerUuid}','${qzSetting.domain}','${qzSetting.pcGroup}','PC')">排除关键字</a>
								<a class="blue" href="javascript:showKeywordDialog('${qzSetting.uuid}','${qzSetting.customerUuid}','${qzSetting.domain}','${qzSetting.pcGroup}')">指定关键字</a>
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
										<span class="line1" title="${qzSetting.userID}">${qzSetting.userID}</span>
										<span>用户名称</span>
									</a>
								</div>

								<div class="other-rank_1">
									<div class="row">
										<span class="line1">
											<c:if test="${qzSetting.qzKeywordRankInfoMap['PC'].sumSeries > 0}">
												<a href="javascript:;" id="${qzSetting.uuid}" level="${qzSetting.qzKeywordRankInfoMap['PC'].achieveLevel}" onmouseover="showChargeRulesDiv($(this))" onmouseout="closeChargeRulesDiv()">
													<c:choose>
														<c:when test="${qzSetting.qzKeywordRankInfoMap['PC'].achieveLevel == qzSetting.qzKeywordRankInfoMap['PC'].sumSeries}">
															<font style="background-color: forestgreen;font-size: 14px;">${qzSetting.qzKeywordRankInfoMap['PC'].achieveLevel} / ${qzSetting.qzKeywordRankInfoMap['PC'].sumSeries} (${qzSetting.qzKeywordRankInfoMap['PC'].currentPrice})</font>
														</c:when>
														<c:when test="${qzSetting.qzKeywordRankInfoMap['PC'].achieveLevel == 0}">
															<font style="background-color: red;font-size: 14px;">${qzSetting.qzKeywordRankInfoMap['PC'].achieveLevel} / ${qzSetting.qzKeywordRankInfoMap['PC'].sumSeries} (${qzSetting.qzKeywordRankInfoMap['PC'].currentPrice})</font>
														</c:when>
														<c:when test="${qzSetting.qzKeywordRankInfoMap['PC'].achieveLevel >= 1 and qzSetting.qzKeywordRankInfoMap['PC'].differenceValue < 1}">
															<font style="background-color: orange;font-size: 14px;">${qzSetting.qzKeywordRankInfoMap['PC'].achieveLevel} / ${qzSetting.qzKeywordRankInfoMap['PC'].sumSeries} (${qzSetting.qzKeywordRankInfoMap['PC'].currentPrice})</font>
														</c:when>
													</c:choose>
												</a>
											</c:if>
											<c:if test="${qzSetting.qzKeywordRankInfoMap['PC'].sumSeries == 0}"><a href="javascript:;">暂无</a></c:if>
										</span>
										<span><a href="javascript:;">全站达标信息</a></span>
									</div>
									<div class="row" title="预计百度来路,点击可跳转到详情页面查看变动">
										<span class="line1"><a href="https://www.5118.com/seo/baidupc/ip/${qzSetting.domain}" target="_blank">${qzSetting.qzKeywordRankInfoMap["PC"].ipRoute == "" ? "暂无" : qzSetting.qzKeywordRankInfoMap["PC"].ipRoute}</a> <sub>IP</sub></span>
										<span><a href="javascript:;">预计百度来路</a></span>
									</div>
								</div>

								<div class="other-rank">
									<div class="row">
										<div>
										<span class="line1">
											<a href="javascript:;">
													${qzSetting.qzKeywordRankInfoMap["PC"].topTenNum == null ? "暂无" : qzSetting.qzKeywordRankInfoMap["PC"].topTenNum}
											</a>
										</span>
											<span>
											<a href="javascript:;">前10</a>
										</span>
										</div>

										<div>
										<span class="line1">
											<a href="javascript:;">
													${qzSetting.qzKeywordRankInfoMap["PC"].topFiftyNum == null ? "暂无" : qzSetting.qzKeywordRankInfoMap["PC"].topFiftyNum}
											</a>
										</span>
											<span>
											<a href="javascript:;">前50</a>
										</span>
										</div>

										<div name="baiduWeight">
										<span class="line1">
											<a href="javascript:;">${qzSetting.qzKeywordRankInfoMap["PC"] == null ? "暂无" : qzSetting.qzKeywordRankInfoMap["PC"].baiduWeight}</a>
										</span>
											<span><a href="javascript:;">百度权重</a></span>
										</div>

										<div>
										<span class="line1">
											<a href="javascript:;">
													${qzSetting.updateStatus == null ? "暂无" : qzSetting.updateStatus}
											</a>
										</span>
											<span>
											<a href="javascript:;">更新状态</a>
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
											<a href="javascript:;">${qzSetting.pcCreateTopTenNum == null ? "暂无" : qzSetting.pcCreateTopTenNum}</a>
										</span>
											<span>
											<a href="javascript:;">初始前10</a>
										</span>
										</div>
										<div>
										<span class="line1">
											<a href="javascript:;">${qzSetting.pcCreateTopFiftyNum == null ? "暂无" : qzSetting.pcCreateTopFiftyNum}</a>
										</span>
											<span>
											<a href="javascript:;">初始前50</a>
										</span>
										</div>

										<div>
										<span class="line1">
											<input type="hidden" name="type" value="${qzSetting.type}">
											<a href="javascript:;">${qzSetting.autoCrawlKeywordFlag == true ? "是" : "否"}</a>
										</span>
											<span>
											<a href="javascript:;">爬取关键字</a>
										</span>
										</div>

										<div>
										<span class="line1">
											<a href="javascript:;" status="${qzSetting.status}">
											   <c:choose>
												   <c:when test="${qzSetting.status == 1}">激活</c:when>
												   <c:when test="${qzSetting.status == 2}">
													   <span style="color: green;">新增</span>
												   </c:when>
												   <c:when test="${qzSetting.status == 3}">
													   <span style="color: red;">暂停续费</span>
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

										<div>
										</div>
									</div>
								</div>

								<div class="other-rank_2">
									<div class="row">
										<div title="该站下该分组所有机器数, 点击链接跳转到终端监控">
										<span class="line1">
											<a target="_blank" href="javascript:searchClientStatus('${qzSetting.pcGroup}', null)">${qzSetting.pcGroup == null or qzSetting.pcGroup == "" ? "暂无" : qzSetting.pcGroup}</a>
										</span>
											<span>
											<a href="javascript:;">优化分组</a>
										</span>
										</div>
									</div>
									<div class="row">
										<div title="该分组下所有操作类型对应的机器数, 点击链接跳转到终端监控">
											<div class="showSomeOperationType" name="showSomeOperationType">
												<span><a href="javascript:;"> 无 </a></span>
											</div>
											<input type="hidden" name="allOperationType">
											<div class="operationTypeSpan">
											<span>
												<a href="javascript:;">操作类型</a>
											</span>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="rank-wrap">
								<c:choose>
									<c:when test="${qzSetting.qzKeywordRankInfoMap['PC'] != null}">
										<div name="rankInfo" style="display: none;">
											<span>${qzSetting.qzKeywordRankInfoMap["PC"]}</span>
										</div>
										<div class="col-1" id="keywordRecordCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
										<div class="col-2" id="keywordTrendCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
									</c:when>
									<c:otherwise>
										<div class="col-1">
											<h1>暂无数据</h1>
										</div>
										<div class="col-2">
											<h1>暂无数据</h1>
										</div>
									</c:otherwise>
								</c:choose>

								<div class="col-3 top50-link">
									<div class="row4">
										<span>前50</span>
										<span class="top50">
										<a href="javascript:;">
												${qzSetting.qzKeywordRankInfoMap["PC"].topFiftyIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["PC"].topFiftyIncrement}
										</a>
									</span>
									</div>

									<div class="row4">
										<span>前40</span>
										<span class="top40">
										<a href="javascript:;">
												${qzSetting.qzKeywordRankInfoMap["PC"].topFortyIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["PC"].topFortyIncrement}
										</a>
									</span>
									</div>

									<div class="row4">
										<span>前30</span>
										<span class="top30">
								<a href="javascript:;">
										${qzSetting.qzKeywordRankInfoMap["PC"].topThirtyIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["PC"].topThirtyIncrement}
								</a>
							</span>
									</div>

									<div class="row4">
										<span>前20</span>
										<span class="top20">
								<a href="javascript:;">
										${qzSetting.qzKeywordRankInfoMap["PC"].topTwentyIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["PC"].topTwentyIncrement}
								</a>
							</span>
									</div>

									<div class="row4">
										<span>前10</span>
										<span class="top10">
								<a href="javascript:;">
										${qzSetting.qzKeywordRankInfoMap["PC"].topTenIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["PC"].topTenIncrement}
								</a>
							</span>
									</div>
								</div>
							</div>
						</div>
					</li>
					<!--li-end-pc-->
					</c:when>
					<c:when test="${qzSetting.phoneGroup != null and (qzSetting.crawlerStatus != 'finish' or qzSetting.qzKeywordRankInfoMap['Phone'] != null)}">
					<li>
						<div class="header">
							<span>
								<input type="checkbox" name="uuid" value="${qzSetting.uuid}" onclick="decideSelectAll();"/>
							</span>
							<span class="contactPerson-name"><a href="javascript:;">${qzSetting.contactPerson}</a></span>
							<span class="domain"><a href="javascript:;">${qzSetting.domain}</a></span>
							<span class="to-aizhan"><a href="https://www.aizhan.com/cha/${qzSetting.domain}/" target="_blank" title="查看爱站">爱站</a></span>
							<span class="to-5118"><a  href="https://www.5118.com/seo/${qzSetting.domain}" target="_blank" title="查看5118,需要登录">5118</a></span>
							<span class="tagNames" ondblclick="editTagNameStr($(this).find('label.tagNameStr')[0], true)"><label>分组标签:</label>&nbsp;&nbsp;<label class="tagNameStr">暂无</label></span>
							<div class="handle">
                                <a class="blue" href="javascript:showExcludeCustomerKeywordDialog('${qzSetting.uuid}','${qzSetting.customerUuid}','${qzSetting.domain}','${qzSetting.phoneGroup}','Phone')">排除关键字</a>
								<a class="blue" href="javascript:showKeywordDialog('${qzSetting.uuid}','${qzSetting.customerUuid}','${qzSetting.domain}','${qzSetting.phoneGroup}')">指定关键字</a>
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
										<span>用户名称</span>
									</a>
								</div>

								<div class="other-rank_1">
									<div class="row">
										<span class="line1">
											<c:if test="${qzSetting.qzKeywordRankInfoMap['Phone'].sumSeries > 0}">
												<a href="javascript:;" id="${qzSetting.uuid}" level="${qzSetting.qzKeywordRankInfoMap['Phone'].achieveLevel}" onmouseover="showChargeRulesDiv($(this))" onmouseout="closeChargeRulesDiv()">
													<c:choose>
														<c:when test="${qzSetting.qzKeywordRankInfoMap['Phone'].achieveLevel == qzSetting.qzKeywordRankInfoMap['Phone'].sumSeries}">
															<font style="background-color: forestgreen;font-size: 14px;">${qzSetting.qzKeywordRankInfoMap['Phone'].achieveLevel} / ${qzSetting.qzKeywordRankInfoMap['Phone'].sumSeries} (${qzSetting.qzKeywordRankInfoMap['Phone'].currentPrice})</font>
														</c:when>
														<c:when test="${qzSetting.qzKeywordRankInfoMap['Phone'].achieveLevel == 0}">
															<font style="background-color: red;font-size: 14px;">${qzSetting.qzKeywordRankInfoMap['Phone'].achieveLevel} / ${qzSetting.qzKeywordRankInfoMap['Phone'].sumSeries} (${qzSetting.qzKeywordRankInfoMap['Phone'].currentPrice})</font>
														</c:when>
														<c:when test="${qzSetting.qzKeywordRankInfoMap['Phone'].achieveLevel >= 1 and qzSetting.qzKeywordRankInfoMap['Phone'].differenceValue < 1}">
															<font style="background-color: orange;font-size: 14px;">${qzSetting.qzKeywordRankInfoMap['Phone'].achieveLevel} / ${qzSetting.qzKeywordRankInfoMap['Phone'].sumSeries} (${qzSetting.qzKeywordRankInfoMap['Phone'].currentPrice})</font>
														</c:when>
													</c:choose>
												</a>
											</c:if>
											<c:if test="${qzSetting.qzKeywordRankInfoMap['Phone'].sumSeries == 0}"><a href="javascript:;">暂无</a></c:if>
										</span>
										<span><a href="javascript:;">全站达标信息</a></span>
									</div>
									<div class="row">
										<span class="line1"><a href="javascript:;">${qzSetting.qzKeywordRankInfoMap["Phone"].ipRoute == "" ? "暂无" : qzSetting.qzKeywordRankInfoMap["Phone"].ipRoute}</a> <sub>IP</sub></span>
										<span><a href="javascript:;">预计百度来路</a></span>
									</div>
								</div>

								<div class="other-rank">
									<div class="row">
										<div>
										<span class="line1">
											<a href="javascript:;">
													${qzSetting.qzKeywordRankInfoMap["Phone"].topTenNum == null ? "暂无" : qzSetting.qzKeywordRankInfoMap["Phone"].topTenNum}
											</a>
										</span>
											<span>
											<a href="javascript:;">前10</a>
										</span>
										</div>

										<div>
										<span class="line1">
											<a href="javascript:;">
													${qzSetting.qzKeywordRankInfoMap["Phone"].topFiftyNum == null ? "暂无" : qzSetting.qzKeywordRankInfoMap["Phone"].topFiftyNum}
											</a>
										</span>
											<span>
											<a href="javascript:;">前50</a>
										</span>
										</div>

										<div name="baiduWeight">
										<span class="line1">
											<a href="javascript:;">${qzSetting.qzKeywordRankInfoMap["Phone"] == null ? "暂无" : qzSetting.qzKeywordRankInfoMap["Phone"].baiduWeight}</a>
										</span>
											<span><a href="javascript:;">百度权重</a></span>
										</div>

										<div>
										<span class="line1">
											<a href="javascript:;">
													${qzSetting.updateStatus == null ? "暂无" : qzSetting.updateStatus}
											</a>
										</span>
											<span>
											<a href="javascript:;">更新状态</a>
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
											<a href="javascript:;">${qzSetting.phoneCreateTopTenNum == null ? "暂无" : qzSetting.phoneCreateTopTenNum}</a>
										</span>
											<span>
											<a href="javascript:;">初始前10</a>
										</span>
										</div>
										<div>
										<span class="line1">
											<a href="javascript:;">${qzSetting.phoneCreateTopFiftyNum == null ? "暂无" : qzSetting.phoneCreateTopFiftyNum}</a>
										</span>
											<span>
											<a href="javascript:;">初始前50</a>
										</span>
										</div>

										<div>
										<span class="line1">
											<input type="hidden" name="type" value="${qzSetting.type}">
											<a href="javascript:;">${qzSetting.autoCrawlKeywordFlag == true ? "是" : "否"}</a>
										</span>
											<span>
											<a href="javascript:;">爬取关键字</a>
										</span>
										</div>

										<div>
										<span class="line1">
											<a href="javascript:;" status="${qzSetting.status}">
											   <c:choose>
												   <c:when test="${qzSetting.status == 1}">激活</c:when>
												   <c:when test="${qzSetting.status == 2}">
													   <span style="color: green;">新增</span>
												   </c:when>
												   <c:when test="${qzSetting.status == 3}">
													   <span style="color: red;">暂停续费</span>
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

										<div>

										</div>

									</div>
								</div>

								<div class="other-rank_2">
									<div class="row">
										<div title="该站下该分组所有机器数, 点击链接跳转到终端监控">
										<span class="line1">
											<a target="_blank" href="javascript:searchClientStatus('${qzSetting.phoneGroup}', null)">${qzSetting.phoneGroup == null or qzSetting.phoneGroup == "" ? "暂无" : qzSetting.phoneGroup}</a>
										</span>
											<span>
											<a href="javascript:;">优化分组</a>
										</span>
										</div>
									</div>
									<div class="row">
										<div title="该分组下所有操作类型对应的机器数, 点击链接跳转到终端监控">
											<div class="showSomeOperationType" name="showSomeOperationType">
												<span><a href="javascript:;"> 无 </a></span>
											</div>
											<input type="hidden" name="allOperationType">
											<div class="operationTypeSpan">
											<span>
												<a href="javascript:;">操作类型</a>
											</span>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="rank-wrap">
								<c:choose>
									<c:when test="${qzSetting.qzKeywordRankInfoMap['Phone'] != null}">
										<div name="rankInfo" style="display: none;">
											<span>${qzSetting.qzKeywordRankInfoMap["Phone"]}</span>
										</div>
										<div class="col-1" id="keywordRecordCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
										<div class="col-2" id="keywordTrendCharts" style="position:static !important; -webkit-tap-highlight-color: transparent; user-select: none;"></div>
									</c:when>
									<c:otherwise>
										<div class="col-1">
											<h1>暂无数据</h1>
										</div>
										<div class="col-2">
											<h1>暂无数据</h1>
										</div>
									</c:otherwise>
								</c:choose>

								<div class="col-3 top50-link">
									<div class="row4">
										<span>前50</span>
										<span class="top50">
										<a href="javascript:;">
												${qzSetting.qzKeywordRankInfoMap["Phone"].topFiftyIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["Phone"].topFiftyIncrement}
										</a>
									</span>
									</div>

									<div class="row4">
										<span>前40</span>
										<span class="top40">
								<a href="javascript:;">
										${qzSetting.qzKeywordRankInfoMap["Phone"].topFortyIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["Phone"].topFortyIncrement}
								</a>
							</span>
									</div>

									<div class="row4">
										<span>前30</span>
										<span class="top30">
								<a href="javascript:;">
										${qzSetting.qzKeywordRankInfoMap["Phone"].topThirtyIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["Phone"].topThirtyIncrement}
								</a>
							</span>
									</div>

									<div class="row4">
										<span>前20</span>
										<span class="top20">
								<a href="javascript:;">
										${qzSetting.qzKeywordRankInfoMap["Phone"].topTwentyIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["Phone"].topTwentyIncrement}
								</a>
							</span>
									</div>

									<div class="row4">
										<span>前10</span>
										<span class="top10">
								<a href="javascript:;">
										${qzSetting.qzKeywordRankInfoMap["Phone"].topTenIncrement == null ? '暂无' : qzSetting.qzKeywordRankInfoMap["Phone"].topTenIncrement}
								</a>
							</span>
									</div>
								</div>
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

<form id="searchClientStatusForm" method="post" target="_blank" action="/internal/clientstatus/searchClientStatuses" style="display: none">
    <input type="text" name="groupName" id="groupName">
    <input type="text" name="operationType" id="operationType">
</form>

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
            <td style="width:60px" align="right">标签</td>
            <td>
                <input type="text" name="qzCategoryTagNames" id="qzCategoryTagNames" placeholder="按正确方式输入：阿卡索,MBA,算法" style="width:240px">
            </td>
        </tr>
		<tr>
			<td style="width:60px" align="right">入口</td>
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
			<td style="width:60px" align="right">爬取关键字</td>
			<td>
				<select name="qzSettingAutoCrawlKeywordFlag" id="qzSettingAutoCrawlKeywordFlag" style="width:240px">
					<option value="1" selected>是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="width:60px" align="right">去掉没指数</td>
			<td>
				<select name="qzSettingIgnoreNoIndex" id="qzSettingIgnoreNoIndex"  style="width:240px">
					<option value="1" selected>是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="width:60px" align="right">去掉没排名</td>
			<td>
				<select name="qzSettingIgnoreNoOrder" id="qzSettingIgnoreNoOrder"  style="width:240px">
					<option value="1" selected>是</option>
					<option value="0">否</option>
				</select>
			</td>
		</tr>
		<tr>
			<td style="width:60px" align="right">更新间隔</td>
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
	<c:forEach items="${customerList}" var="customer">
		<option>${customer.contactPerson} ${'_____'} ${customer.uuid}</option>
	</c:forEach>
</datalist>
<datalist id="categoryTag_list">
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
					<input type="text" name="domain" id="domain" style="width:240px" />
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
						<option value="百度" selected>百度</option>
						<option value="360">360</option>
						<option value="UC">UC</option>
						<option value="搜狗">搜狗</option>
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
                    <select name="terminalType" id="terminalType" style="width:240px" onchange="getExcludeKeyword()">
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
<%--达标信息详情DIV--%>
<div id="chargeRulesDiv" style="display:none;width:300px;">
	<table id="chargeRulesDivTable" border="1" cellpadding="10" style="font-size: 12px;background-color: white;border-collapse: collapse;margin: 10px 10px;width:92%;">
		<tr>
			<td>序号</td>
			<td>起始词量</td>
			<td>终止词量</td>
			<td>价格</td>
		</tr>
	</table>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath}/js/echarts.min.js"></script>
<script src="${staticPath }/qzsetting/list.js"></script>
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