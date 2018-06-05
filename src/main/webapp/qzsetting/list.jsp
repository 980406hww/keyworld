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
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@include file="/menu.jsp" %>
	<table width="100%" style="font-size:12px; margin-top:40px;" cellpadding=3>
		<tr>
			<td colspan="14" align="right">
				<shiro:hasPermission name="/internal/qzsetting/searchQZSettings">
				<a href="javascript:resetSearchCondition('-1')">过期未收费(${chargeRemindDataMap['expiredChargeSize']})</a>
				| <a target="_blank" href="javascript:resetSearchCondition('0')">当天收费提醒(${chargeRemindDataMap['nowChargeSize']})</a>
				| <a target="_blank" href="javascript:resetSearchCondition('3')">三天收费提醒(${chargeRemindDataMap['threeChargeSize']})</a>
				| <a target="_blank" href="javascript:resetSearchCondition('7')">七天收费提醒(${chargeRemindDataMap['sevenChargeSize']})</a>
				</shiro:hasPermission>
			</td>
		</tr>
		<tr>
			<td colspan="11">
				<form method="post" id="chargeForm" action="/internal/qzsetting/searchQZSettings">
					<input type="hidden" id="dateRangeType" name="dateRangeType" value="${qzSettingSearchCriteria.dateRangeType}"/>
					<table style="font-size:12px;">
						<tr>
							<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
							<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
							<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
							<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
							<input type="hidden" name="customerUuid" id="customerUuid" />
							<td align="right">客户:</td> <td><input type="text" list="customer_list" name="customerInfo" id="customerInfo" value="${qzSettingSearchCriteria.customerInfo}" style="width:200px;"></td>
							<td align="right">域名:</td> <td><input type="text" name="domain" id="domain" value="${qzSettingSearchCriteria.domain}" style="width:200px;"></td>
							<td align="right">组名:</td> <td><input type="text" name="group" id="group" value="${qzSettingSearchCriteria.group}" style="width:200px;"></td>
							<td align="right">状态:</td>
							<td>
								<select name="updateStatus" id="updateStatus" style="width:200px;">
									<c:forEach items="${statusList}" var="status">
										<c:choose>
											<c:when test="${status eq qzSettingSearchCriteria.updateStatus}"><option selected>${status}</option></c:when>
											<c:otherwise><option>${status}</option></c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</td>
							<td align="right" width="50">
								<shiro:hasPermission name="/internal/qzsetting/searchQZSettings">
									<input class="ui-button ui-widget ui-corner-all" type="submit" onclick="resetSearchCondition('1')" value=" 查询 " >
								</shiro:hasPermission>
							</td>
							<td align="right" width="50">
								<shiro:hasPermission name="/internal/qzsetting/save">
									<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showSettingDialog(null, this)" value=" 增加 " >
								</shiro:hasPermission>
							</td>
							<td align="right" width="70">
								<shiro:hasPermission name="/internal/qzsetting/updateImmediately">
									<input class="ui-button ui-widget ui-corner-all" type="button" onclick="updateImmediately(this)" value=" 马上更新 " >
								</shiro:hasPermission>
							</td>
							<td align="right" width="70" >
								<shiro:hasPermission name="/internal/qzsetting/deleteQZSettings">
									<input class="ui-button ui-widget ui-corner-all" type="button" onclick="delSelectedQZSettings(this)" value=" 删除所选 " >
								</shiro:hasPermission>
							</td>

						</tr>
					</table>
				</form>
			</td>
		</tr>
	</table>
	<table id="headerTable" style="width: 100%">
		<tr bgcolor="#eeeeee" height=30>
			<td align="center" width=25><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
			<c:if test="${isDepartmentManager == true}">
				<td align="center" width=60>用户名称</td>
			</c:if>
			<td align="center" width=150>客户</td>
			<td align="center" width=100>域名</td>
			<td align="center" width=50>入口类型</td>
			<td align="center" width=120>分组</td>
			<td align="center" width=80>去掉没指数</td>
			<td align="center" width=80>去掉没排名</td>
			<td align="center" width=60>更新间隔</td>
			<td align="center" width=60>更新状态</td>
			<td align="center" width=80>更新开始时间</td>
			<td align="center" width=80>更新结束时间</td>
			<td align="center" width=80>更新时间</td>
			<td align="center" width=80>添加时间</td>
			<td align="center" width=100>操作</td>
		</tr>
	</table>
</div>
<div id="showQZSettingDiv" style="margin-bottom: 30px">
<table id="showQZSettingTable"  style="font-size:12px;width: 100%;" cellpadding=3 >
	<c:forEach items="${page.records}" var="qzSetting" varStatus="status">
		<tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30 <c:if test="${status.index%2==0}">bgcolor="#eeeeee" </c:if> >
			<td width=25 align="center"><input type="checkbox" name="uuid" value="${qzSetting.uuid}" onclick="decideSelectAll()"/></td>
			<c:if test="${isDepartmentManager == true}">
				<td width=60>${qzSetting.userID}</td>
			</c:if>
			<td width=150>${qzSetting.contactPerson}</td>
			<td width=100>${qzSetting.domain}</td>
			<td width=50>${qzSetting.type}</td>
			<td width=80>
				${qzSetting.pcGroup == null ? "" : "pc:"}${qzSetting.pcGroup == null ? "" : qzSetting.pcGroup}<br>
				${qzSetting.phoneGroup == null ? "" : "m:"}${qzSetting.phoneGroup == null ? "" : qzSetting.phoneGroup}
			</td>
			<td width=80>${qzSetting.ignoreNoIndex == true ? "是" : "否"}</td>
			<td width=80>${qzSetting.ignoreNoOrder == true ? "是" : "否"}</td>
			<td width=60>${qzSetting.updateInterval}</td>
			<td width=60>${qzSetting.updateStatus == null ? "" : qzSetting.updateStatus}</td>
			<td width=80><fmt:formatDate value="${qzSetting.updateStartTime}" pattern="MM-dd HH:mm" /></td>
			<td width=80><fmt:formatDate value="${qzSetting.updateEndTime}" pattern="MM-dd HH:mm" /></td>
			<td width=80><fmt:formatDate value="${qzSetting.updateTime}" pattern="MM-dd HH:mm" /></td>
			<td width=80><fmt:formatDate value="${qzSetting.createTime}" pattern="MM-dd HH:mm" /></td>
			<td width=100>
				<shiro:hasPermission name="/internal/qzchargelog/save">
					<a href="javascript:showChargeDialog('${qzSetting.uuid}','${qzSetting.contactPerson}','${qzSetting.domain}',this)">收费</a> |
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/save">
					<a href="javascript:showSettingDialog('${qzSetting.uuid}', this)">修改</a><br>
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzsetting/delete">
					<a href="javascript:delQZSetting(${qzSetting.uuid})">删除</a> |
				</shiro:hasPermission>
				<shiro:hasPermission name="/internal/qzchargelog/chargesList">
					<a href="javascript:showChargeLog('${qzSetting.uuid}', this)">收费记录</a>
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

<div id="changeSettingDialog" class="easyui-dialog" style="display: none;left: 40%;">
	<form id="changeSettingForm">
	<table style="font-size:12px" id="settingTable" align="center" cellspacing="5">
		<tr>
			<td style="width:60x" align="right">客户</td>
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
						<td align="right" style="width:72px;"><span style="margin-right:14;">分组</span></td>
						<td><input type="text" name="group" id="groupPhone" style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px;"><span style="margin-right:14;">手机域名</span></td>
						<td><input type="text" name="subDomainName" id="subDomainNamePhone" style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px"><span style="margin-right:14;">初始词量</span></td>
						<td colspan="4"><input type="text" name="initialKeywordCount" id="initialKeywordCountPhone"  style="width:240px;margin-left: -6;"/></td>
					</tr>
					<tr>
						<td align="right" style="width:72px"><span style="margin-right:14;">当前词量</span></td>
						<td colspan="4"><input type="text" name="currentKeywordCount" id="currentKeywordCountPhone"  style="width:240px;margin-left: -6;" readonly/></td>
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
					<option value="2" selected>2天</option>
					<option value="3">3天</option>
					<option value="5">5天</option>
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