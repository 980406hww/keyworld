<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>机器分组工作统计列表</title>
	<%@ include file="/commons/basejs.jsp" %>
	<%--<style>
		td{
			display: table-cell;
			vertical-align: inherit;
		}
		#showMachineGroupWorkInfoDiv {
			width: 100%;
			margin: auto;
		}
		#topDiv {
			top: 0px;
			left: 0px;
			background-color: white;
			width: 100%;
		}
		#showMachineGroupWorkInfoTable {
			width: 100%;
		}
		#showMachineGroupWorkInfoTable tr:nth-child(odd){background:#EEEEEE;}


		#saveCustomerKeywordDialog ul{list-style: none;margin: 0px;padding: 0px;}
		#saveCustomerKeywordDialog li{margin: 5px 0;}
		#saveCustomerKeywordDialog .customerKeywordSpanClass{width: 70px;display: inline-block;text-align: right;}
	</style>--%>

</head>
<body>

<div id="topDiv">
	<%@include file="/menu.jsp" %>
	<form method="post" id="searchMachineGroupInfoForm" action="/internal/machinegroupworkinfo/searchMachineGroupWorkInfos">
		<table style="font-size:12px;">
			<tr>
				<td align="right">分组名称:<input name="machineGroup" id="machineGroup" type="text" style="width:200px;"
											  value="${machineGroupWorkInfoCriteria.machineGroup}">
					<input id="machineGroupFuzzyQuery" name="machineGroupFuzzyQuery" type="checkbox" value="machineGroupFuzzyQuery" ${machineGroupWorkInfoCriteria.machineGroupFuzzyQuery != null ? "checked=true" : ""}/>模糊查询 &nbsp;
					&nbsp;&nbsp;
				</td>
				<td align="right">客户名称:<input name="customerName" id="customerName" type="text" style="width:200px;"
											  value="${machineGroupWorkInfoCriteria.customerName}"></td>
				<td>
					<select id="dayNum" name="dayNum" value="${machineGroupWorkInfoCriteria.dayNum}">
						<option value="0" <c:if test="${0 eq machineGroupWorkInfoCriteria.dayNum}">selected</c:if>> 请选择要查询的历史记录 </option>
						<option value="1" <c:if test="${1 eq machineGroupWorkInfoCriteria.dayNum}">selected</c:if>> 一天前 </option>
						<option value="2" <c:if test="${2 eq machineGroupWorkInfoCriteria.dayNum}">selected</c:if>> 两天前 </option>
						<option value="3" <c:if test="${3 eq machineGroupWorkInfoCriteria.dayNum}">selected</c:if>> 三天前 </option>
						<option value="4" <c:if test="${4 eq machineGroupWorkInfoCriteria.dayNum}">selected</c:if>> 四天前 </option>
						<option value="5" <c:if test="${5 eq machineGroupWorkInfoCriteria.dayNum}">selected</c:if>> 五天前 </option>
						<option value="6" <c:if test="${6 eq machineGroupWorkInfoCriteria.dayNum}">selected</c:if>> 六天前 </option>
					</select>
					&nbsp;&nbsp;
				</td>
				<td align="right">
					&nbsp;
					<shiro:hasPermission name="/internal/refreshstatinfo/searchRefreshStatInfos">
						<input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " onclick="trimSearchCondition()">&nbsp;&nbsp;
					</shiro:hasPermission>

				</td>
			</tr>
		</table>
	</form>
	<table style="font-size:12px;" id="headerTable" width=100%">
		<tr bgcolor="#eeeeee" height=30>
			<td align="center" width=30 rowspan="2"><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
			<td align="center" width=100 rowspan="2">类型</td>
			<td align="center" width=80 colspan="7">关键字</td>
			<td align="center" width=80 colspan="6">刷的次数</td>
			<td align="center" width=100 colspan="3">机器数</td>
		</tr>
		<tr bgcolor="#eeeeee" height=30>
			<td align="center" width=80>总数</td>
			<td align="center" width=80>达标数（金额）</td>
			<td align="center" width=80>达标率</td>
			<td align="center" width=80>没有刷量</td>
			<td align="center" width=80>待刷数</td>
			<td align="center" width=80>无效刷量</td>
			<td align="center" width=80>无效占比</td>
			<td align="center" width=60>总次数</td>
			<td align="center" width=80>已刷次数</td>
			<td align="center" width=80>待刷次数</td>
			<td align="center" width=80>平均有效刷量</td>
			<td align="center" width=80>取词次数</td>
			<td align="center" width=80>无效占比</td>
			<td align="center" width=50>总数</td>
			<td align="center" width=50>空闲率</td>
			<td align="center" width=60>已停数</td>
		</tr>
	</table>
</div>

<div id="showMachineGroupWorkInfoDiv">
	<table id="showMachineGroupWorkInfoTable" width=100%">
		<thead>
		<c:forEach items="${machineGroupWorkInfos}" var="machineGroupWorkInfo" varStatus="tr" end="0">
			<tr onmouseover="doOver(this);" onmouseout="doOut(this);" style="background-color: #eeeeee;" height="30">
				<td width=30 align="center"><input type="checkbox" name="uuid" value="${machineGroupWorkInfo.machineGroup}" onclick="decideSelectAll()"/></td>
				<td width=100><a href="javascript:searchCustomerKeywords('${machineGroupWorkInfo.machineGroup}')">${machineGroupWorkInfo.machineGroup}</a></td>
				<td width=80>${machineGroupWorkInfo.totalKeywordCount}</td>
				<td width=80>
					<c:if test="${machineGroupWorkInfo.reachStandardKeywordCount > 0}">
						<a href="javascript:findKeyword(null , null)">${machineGroupWorkInfo.reachStandardKeywordCount}(${machineGroupWorkInfo.todaySubTotal})</a>
					</c:if>
				</td>
				<td width=80>
					<c:if test="${machineGroupWorkInfo.reachStandardPercentage > 0}">
						<fmt:formatNumber value="${machineGroupWorkInfo.reachStandardPercentage}" pattern="#.##" minFractionDigits="2"/>%
					</c:if>
				</td>
				<td width=80>${machineGroupWorkInfo.zeroOptimizedCount > 0 ? machineGroupWorkInfo.zeroOptimizedCount : ""}</td>
				<td width=80>${machineGroupWorkInfo.needOptimizeKeywordCount > 0 ? machineGroupWorkInfo.needOptimizeKeywordCount : ""}</td>
				<td width=80>
					<c:if test="${machineGroupWorkInfo.invalidKeywordCount > 0}">
						<a href="javascript:findKeyword(null, '${machineGroupWorkInfo.maxInvalidCount}')">${machineGroupWorkInfo.invalidKeywordCount}</a>
					</c:if>
					<shiro:hasPermission name="/internal/customerKeyword/resetInvalidRefreshCount">
						<c:if test="${machineGroupWorkInfo.invalidKeywordCount > 0}">
							<a target="_blank" href="javascript:resetInvaidRefreshCount('${machineGroupWorkInfoCriteria.machineGroup == null ? "" : machineGroupWorkInfoCriteria.machineGroup}', '${machineGroupWorkInfoCriteria.customerName == null ? "" : machineGroupWorkInfoCriteria.customerName}', false, this)">重置</a>
						</c:if>
					</shiro:hasPermission>
				</td>

				<td width=80>
					<font color="${machineGroupWorkInfo.invalidKeywordPercentage > 20 ? "red" : (machineGroupWorkInfo.invalidKeywordPercentage > 10 ? "purple" : "")}">
						<c:if test="${machineGroupWorkInfo.invalidKeywordPercentage > 0}">
							<fmt:formatNumber value="${machineGroupWorkInfo.invalidKeywordPercentage}" pattern="#.##" minFractionDigits="2"/>%
						</c:if>
					</font>
				</td>
				<td width=60>${machineGroupWorkInfo.totalOptimizeCount > 0 ? machineGroupWorkInfo.totalOptimizeCount : ""}</td>
				<td width=80>${machineGroupWorkInfo.totalOptimizedCount > 0 ? machineGroupWorkInfo.totalOptimizedCount : ""}</td>
				<td width=80>${machineGroupWorkInfo.needOptimizeCount > 0 ? machineGroupWorkInfo.needOptimizeCount : ""}</td>
				<td width=80>
					<c:if test="${machineGroupWorkInfo.totalOptimizedCount > 0}">
						<c:set var="avgOptimizedCount" value="${machineGroupWorkInfo.totalOptimizedCount / machineGroupWorkInfo.totalKeywordCount}"/>
						<font color="${avgOptimizedCount <= 30 ? "red" : "black"}">
							<fmt:formatNumber value="${avgOptimizedCount}" pattern="#.##" minFractionDigits="2"/>
						</font>
					</c:if>
				</td>
				<td width=80>${machineGroupWorkInfo.queryCount > 0 ? machineGroupWorkInfo.queryCount : ""}</td>
				<td width=80>
					<font color="${machineGroupWorkInfo.invalidOptimizePercentage > 20 ? "red" : (machineGroupWorkInfo.invalidOptimizePercentage > 10 ? "purple" : "")}">
						<c:if test="${machineGroupWorkInfo.invalidOptimizePercentage > 0}">
							<fmt:formatNumber value="${machineGroupWorkInfo.invalidOptimizePercentage}" pattern="#.##" minFractionDigits="2"/>%
						</c:if>
					</font>
				</td>
				<td width=50>${machineGroupWorkInfo.totalMachineCount > 0 ? machineGroupWorkInfo.totalMachineCount : ""}</td>
				<td width=50>
					<c:if test="${machineGroupWorkInfo.idlePercentage > 0}">
						<fmt:formatNumber value="${machineGroupWorkInfo.idlePercentage}" pattern="#.##" minFractionDigits="2"/>%
					</c:if>
				</td>
				<td width=60>
					<c:if test="${machineGroupWorkInfo.unworkMachineCount > 0}">
						<a target="_blank" href="javascript:findMachineInfo(null)">${machineGroupWorkInfo.unworkMachineCount}</a>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</thead>
		<tbody>
		<c:forEach items="${machineGroupWorkInfos}" var="machineGroupWorkInfo" varStatus="tr" begin="1">
			<c:choose>
				<c:when test="${tr.count % 2 != 0}">
					<tr onmouseover="doOver(this);" onmouseout="doOut(this);" style="background-color: #eeeeee;" height="30">
				</c:when>
				<c:otherwise>
					<tr onmouseover="doOver(this);" onmouseout="doOut(this);" height="30">
				</c:otherwise>
			</c:choose>
			<td width=30 align="center"><input type="checkbox" name="uuid" value="${machineGroupWorkInfo.machineGroup}" onclick="decideSelectAll()"/></td>
			<td width=100><a href="javascript:searchCustomerKeywords('${machineGroupWorkInfo.machineGroup}')">${machineGroupWorkInfo.machineGroup}</a></td>
			<td width=80>${machineGroupWorkInfo.totalKeywordCount}</td>
			<td width=80>
				<c:if test="${machineGroupWorkInfo.reachStandardKeywordCount > 0}">
					<a href="javascript:findKeyword('${machineGroupWorkInfo.machineGroup}', null)">${machineGroupWorkInfo.reachStandardKeywordCount}(${machineGroupWorkInfo.todaySubTotal})</a>
				</c:if>
			</td>
			<td width=80>
				<c:if test="${machineGroupWorkInfo.reachStandardPercentage > 0}">
					<fmt:formatNumber value="${machineGroupWorkInfo.reachStandardPercentage}" pattern="#.##" minFractionDigits="2"/>%
				</c:if>
			</td>
			<td width=80>${machineGroupWorkInfo.zeroOptimizedCount > 0 ? machineGroupWorkInfo.zeroOptimizedCount : ""}</td>
			<td width=80>${machineGroupWorkInfo.needOptimizeKeywordCount > 0 ? machineGroupWorkInfo.needOptimizeKeywordCount : ""}</td>
			<td width=80>
				<c:if test="${machineGroupWorkInfo.invalidKeywordCount > 0}">
					<a href="javascript:findKeyword('${machineGroupWorkInfo.machineGroup}', '${machineGroupWorkInfo.maxInvalidCount}')">${machineGroupWorkInfo.invalidKeywordCount}</a>
				</c:if>
				<shiro:hasPermission name="/internal/customerKeyword/resetInvalidRefreshCount">
					<c:if test="${machineGroupWorkInfo.invalidKeywordCount > 0}">
						<a target="_blank" href="javascript:resetInvaidRefreshCount('${machineGroupWorkInfo.machineGroup}', '${machineGroupWorkInfoCriteria.customerName == null ? "" : machineGroupWorkInfoCriteria.customerName}', true, this)">重置</a>
					</c:if>
				</shiro:hasPermission>
			</td>

			<td width=80>
				<font color="${machineGroupWorkInfo.invalidKeywordPercentage > 20 ? "red" : (machineGroupWorkInfo.invalidKeywordPercentage > 10 ? "purple" : "")}">
					<c:if test="${machineGroupWorkInfo.invalidKeywordPercentage > 0}">
						<fmt:formatNumber value="${machineGroupWorkInfo.invalidKeywordPercentage}" pattern="#.##" minFractionDigits="2"/>%
					</c:if>
				</font>
			</td>
			<td width=60>${machineGroupWorkInfo.totalOptimizeCount > 0 ? machineGroupWorkInfo.totalOptimizeCount : ""}</td>
			<td width=80>${machineGroupWorkInfo.totalOptimizedCount > 0 ? machineGroupWorkInfo.totalOptimizedCount : ""}</td>
			<td width=80>${machineGroupWorkInfo.needOptimizeCount > 0 ? machineGroupWorkInfo.needOptimizeCount : ""}</td>
			<td width=80>
				<c:if test="${machineGroupWorkInfo.totalOptimizedCount > 0}">
					<c:set var="avgOptimizedCount" value="${machineGroupWorkInfo.totalOptimizedCount / machineGroupWorkInfo.totalKeywordCount}"/>
					<font color="${avgOptimizedCount <= 30 ? "red" : "black"}">
						<fmt:formatNumber value="${avgOptimizedCount}" pattern="#.##" minFractionDigits="2"/>
					</font>
				</c:if>
			</td>
			<td width=80>${machineGroupWorkInfo.queryCount > 0 ? machineGroupWorkInfo.queryCount : ""}</td>
			<td width=80>
				<font color="${machineGroupWorkInfo.invalidOptimizePercentage > 20 ? "red" : (machineGroupWorkInfo.invalidOptimizePercentage > 10 ? "purple" : "")}">
					<c:if test="${machineGroupWorkInfo.invalidOptimizePercentage > 0}">
						<fmt:formatNumber value="${machineGroupWorkInfo.invalidOptimizePercentage}" pattern="#.##" minFractionDigits="2"/>%
					</c:if>
				</font>
			</td>
			<td width=50>${machineGroupWorkInfo.totalMachineCount > 0 ? machineGroupWorkInfo.totalMachineCount : ""}</td>
			<td width=50>
				<c:if test="${machineGroupWorkInfo.idlePercentage > 0}">
					<fmt:formatNumber value="${machineGroupWorkInfo.idlePercentage}" pattern="#.##" minFractionDigits="2"/>%
				</c:if>
			</td>
			<td width=60>
				<c:if test="${machineGroupWorkInfo.unworkMachineCount > 0}">
					<a target="_blank" href="javascript:findMachineInfo('${machineGroupWorkInfo.machineGroup}')">${machineGroupWorkInfo.unworkMachineCount}</a>
				</c:if>
			</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>
<form id="searchMachineInfoForm" style="display: none;" method="post" target="_blank"
	  action="/internal/machineInfo/searchBadMachineInfo">
	<input type="hidden" name="hasProblem" value="hasProblem"/>
	<input type="hidden" name="machineGroup" id="machineGroup" value=""/>
</form>
<form id="searchCustomerKeywordForm" style="display: none;" method="post" target="_blank"
	  action="/internal/customerKeyword/searchCustomerKeywordLists">
	<input type="hidden" name="machineGroup" id="machineGroup" value=""/>
	<input type="hidden" name="invalidRefreshCount" id="invalidRefreshCount" value=""/>
	<input type="hidden" name="noReachStandardDays" id="noReachStandardDays" value=""/>
	<input type="hidden" name="status" id="status" value="1"/>
</form>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/machineGroupWorkInfo/refresh.js"></script>
<script src="${staticPath }/js/jquery.tablesorter.js"></script>
<script language="javascript">
	<shiro:hasPermission name="/internal/machineInfo/searchBadMachineInfo">
	function findMachineInfo(machineGroup) {
		$("#searchMachineInfoForm").find("#machineGroup").val(machineGroup);
		$("#searchMachineInfoForm").submit();
	}
	</shiro:hasPermission>

	<shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
	function findKeyword(optimizeGroupName, invalidRefreshCount) {
		if(invalidRefreshCount == null) {
			$("#searchCustomerKeywordForm").find("#noReachStandardDays").val(-1);
		} else {
			$("#searchCustomerKeywordForm").find("#noReachStandardDays").val(0);
		}
		$("#searchCustomerKeywordForm").find("#invalidRefreshCount").val(invalidRefreshCount);
		$("#searchCustomerKeywordForm").find("#optimizeGroupName").val(optimizeGroupName);
		$("#searchCustomerKeywordForm").submit();
	}
	</shiro:hasPermission>

	<shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
	function searchCustomerKeywords(machineGroup) {
		$("#searchCustomerKeywordForm").find("#machineGroup").val(machineGroup);
		$("#searchCustomerKeywordForm").submit();
	}
	</shiro:hasPermission>
	$(function() {
		window.onresize = function (ev) {
			$("#showMachineGroupWorkInfoDiv").css("margin-top", $("#topDiv").height()-5);
		}
		$("#showMachineGroupWorkInfoTable").tablesorter({
			headers: {0: {sorter: false}},
			sortInitialOrder: 'desc',
			sortReset: true,
			sortRestart : true,
			emptyTo: 'bottom'
		});
	});
</script>
</body>
</html>