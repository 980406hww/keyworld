<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>刷量统计列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@include file="/menu.jsp" %>
	<form method="post" id="searchRefreshStatInfoForm" action="/internal/refreshstatinfo/searchRefreshStatInfos">
		<table style="font-size:12px;">
			<tr>
				<td align="right">分组名称:<input name="groupName" id="groupName" type="text" style="width:200px;"
											  value="${refreshStatInfoCriteria.groupName}"></td>
				<td align="right">客户名称:<input name="customerName" id="customerName" type="text" style="width:200px;"
											  value="${refreshStatInfoCriteria.customerName}"></td>
				<td align="right">分类标签:<input name="categoryTag" list="categoryTag_list" type="text" style="width:200px;"
											  value="${refreshStatInfoCriteria.categoryTag}"></td>
				<td>
					&nbsp;&nbsp;
					<select id="dayNum" name="dayNum" value="${refreshStatInfoCriteria.dayNum}">
						<option value="0" <c:if test="${0 eq refreshStatInfoCriteria.dayNum}">selected</c:if>> 请选择要查询的历史记录 </option>
						<option value="1" <c:if test="${1 eq refreshStatInfoCriteria.dayNum}">selected</c:if>> 一天前 </option>
						<option value="2" <c:if test="${2 eq refreshStatInfoCriteria.dayNum}">selected</c:if>> 两天前 </option>
						<option value="3" <c:if test="${3 eq refreshStatInfoCriteria.dayNum}">selected</c:if>> 三天前 </option>
						<option value="4" <c:if test="${4 eq refreshStatInfoCriteria.dayNum}">selected</c:if>> 四天前 </option>
						<option value="5" <c:if test="${5 eq refreshStatInfoCriteria.dayNum}">selected</c:if>> 五天前 </option>
						<option value="6" <c:if test="${6 eq refreshStatInfoCriteria.dayNum}">selected</c:if>> 六天前 </option>
					</select>
					&nbsp;&nbsp;
				</td>
				<td align="right">
					&nbsp;
					<shiro:hasPermission name="/internal/refreshstatinfo/searchRefreshStatInfos">
						<input type="submit" name="btnQuery" id="btnQuery" value=" 查询 " onclick="trimSearchCondition()">&nbsp;&nbsp;
					</shiro:hasPermission>
					<shiro:hasPermission name="/internal/customerKeyword/uploadCustomerKeywords">
					<input type="button" value="导入从爱站抓取排名" onclick="uploadCsv()">&nbsp;&nbsp;
					</shiro:hasPermission>
					<shiro:hasPermission name="/internal/customerKeyword/downloadCustomerKeywordInfo">
					<input type="button" value="导出关键字信息到爱站抓排名" onclick="downloadTxt()">&nbsp;&nbsp;
					<a download="keywordUrl.txt" href="/keywordUrl.txt" target="blank" id="downTXT" style="display: none">点击下载</a>&nbsp;&nbsp;
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
<datalist id="categoryTag_list">
</datalist>
<div id="showRefreshStatInfoDiv">
	<table id="showRefreshStatInfoTable" width=100%">
		<thead>
		<c:forEach items="${refreshStatInfos}" var="refreshStatInfo" varStatus="tr" end="0">
		<tr onmouseover="doOver(this);" onmouseout="doOut(this);" style="background-color: #eeeeee;" height="30">
			<td width=30 align="center"><input type="checkbox" name="uuid" value="${refreshStatInfo.group}" onclick="decideSelectAll()"/></td>
			<td width=100><a href="javascript:searchCustomerKeywords('${refreshStatInfo.group}')">${refreshStatInfo.group}</a></td>
			<td width=80>${refreshStatInfo.totalKeywordCount}</td>
			<td width=80>
				<c:if test="${refreshStatInfo.reachStandardKeywordCount > 0}">
					<a href="javascript:findKeyword(null , null)">${refreshStatInfo.reachStandardKeywordCount}(${refreshStatInfo.todaySubTotal})</a>
				</c:if>
			</td>
			<td width=80>
				<c:if test="${refreshStatInfo.reachStandardPercentage > 0}">
					<fmt:formatNumber value="${refreshStatInfo.reachStandardPercentage}" pattern="#.##" minFractionDigits="2"/>%
				</c:if>
			</td>
			<td width=80>${refreshStatInfo.zeroOptimizedCount > 0 ? refreshStatInfo.zeroOptimizedCount : ""}</td>
			<td width=80>${refreshStatInfo.needOptimizeKeywordCount > 0 ? refreshStatInfo.needOptimizeKeywordCount : ""}</td>
			<td width=80>
				<c:if test="${refreshStatInfo.invalidKeywordCount > 0}">
					<a href="javascript:findKeyword(null, '${refreshStatInfo.maxInvalidCount}')">${refreshStatInfo.invalidKeywordCount}</a>
				</c:if>
				<shiro:hasPermission name="/internal/customerKeyword/resetInvalidRefreshCount">
					<c:if test="${refreshStatInfo.invalidKeywordCount > 0}">
						<a target="_blank" href="javascript:resetInvaidRefreshCount('${refreshStatInfoCriteria.groupName == null ? "" : refreshStatInfoCriteria.groupName}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', false, this)">重置</a>
					</c:if>
				</shiro:hasPermission>
			</td>

			<td width=80>
				<font color="${refreshStatInfo.invalidKeywordPercentage > 20 ? "red" : (refreshStatInfo.invalidKeywordPercentage > 10 ? "purple" : "")}">
					<c:if test="${refreshStatInfo.invalidKeywordPercentage > 0}">
						<fmt:formatNumber value="${refreshStatInfo.invalidKeywordPercentage}" pattern="#.##" minFractionDigits="2"/>%
					</c:if>
				</font>
			</td>
			<td width=60>${refreshStatInfo.totalOptimizeCount > 0 ? refreshStatInfo.totalOptimizeCount : ""}</td>
			<td width=80>${refreshStatInfo.totalOptimizedCount > 0 ? refreshStatInfo.totalOptimizedCount : ""}</td>
			<td width=80>${refreshStatInfo.needOptimizeCount > 0 ? refreshStatInfo.needOptimizeCount : ""}</td>
			<td width=80>
				<c:if test="${refreshStatInfo.totalOptimizedCount > 0}">
					<c:set var="avgOptimizedCount" value="${refreshStatInfo.totalOptimizedCount / refreshStatInfo.totalKeywordCount}"/>
					<font color="${avgOptimizedCount <= 30 ? "red" : "black"}">
						<fmt:formatNumber value="${avgOptimizedCount}" pattern="#.##" minFractionDigits="2"/>
					</font>
				</c:if>
			</td>
			<td width=80>${refreshStatInfo.queryCount > 0 ? refreshStatInfo.queryCount : ""}</td>
			<td width=80>
				<font color="${refreshStatInfo.invalidOptimizePercentage > 20 ? "red" : (refreshStatInfo.invalidOptimizePercentage > 10 ? "purple" : "")}">
					<c:if test="${refreshStatInfo.invalidOptimizePercentage > 0}">
						<fmt:formatNumber value="${refreshStatInfo.invalidOptimizePercentage}" pattern="#.##" minFractionDigits="2"/>%
					</c:if>
				</font>
			</td>
			<td width=50>${refreshStatInfo.totalMachineCount > 0 ? refreshStatInfo.totalMachineCount : ""}</td>
			<td width=50>
				<c:if test="${refreshStatInfo.idlePercentage > 0}">
					<fmt:formatNumber value="${refreshStatInfo.idlePercentage}" pattern="#.##" minFractionDigits="2"/>%
				</c:if>
			</td>
			<td width=60>
				<c:if test="${refreshStatInfo.unworkMachineCount > 0}">
					<a target="_blank" href="javascript:findClientStatus(null)">${refreshStatInfo.unworkMachineCount}</a>
				</c:if>
			</td>
			</tr>
		</c:forEach>
		</thead>
		<tbody>
		<c:forEach items="${refreshStatInfos}" var="refreshStatInfo" varStatus="tr" begin="1">
			<c:choose>
				<c:when test="${tr.count % 2 != 0}">
					<tr onmouseover="doOver(this);" onmouseout="doOut(this);" style="background-color: #eeeeee;" height="30">
				</c:when>
				<c:otherwise>
					<tr onmouseover="doOver(this);" onmouseout="doOut(this);" height="30">
				</c:otherwise>
			</c:choose>
			<td width=30 align="center"><input type="checkbox" name="uuid" value="${refreshStatInfo.group}" onclick="decideSelectAll()"/></td>
			<td width=100><a href="javascript:searchCustomerKeywords('${refreshStatInfo.group}')">${refreshStatInfo.group}</a></td>
			<td width=80>${refreshStatInfo.totalKeywordCount}</td>
			<td width=80>
				<c:if test="${refreshStatInfo.reachStandardKeywordCount > 0}">
					<a href="javascript:findKeyword('${refreshStatInfo.group}', null)">${refreshStatInfo.reachStandardKeywordCount}(${refreshStatInfo.todaySubTotal})</a>
				</c:if>
			</td>
			<td width=80>
				<c:if test="${refreshStatInfo.reachStandardPercentage > 0}">
					<fmt:formatNumber value="${refreshStatInfo.reachStandardPercentage}" pattern="#.##" minFractionDigits="2"/>%
				</c:if>
			</td>
			<td width=80>${refreshStatInfo.zeroOptimizedCount > 0 ? refreshStatInfo.zeroOptimizedCount : ""}</td>
			<td width=80>${refreshStatInfo.needOptimizeKeywordCount > 0 ? refreshStatInfo.needOptimizeKeywordCount : ""}</td>
			<td width=80>
				<c:if test="${refreshStatInfo.invalidKeywordCount > 0}">
					<a href="javascript:findKeyword('${refreshStatInfo.group}', '${refreshStatInfo.maxInvalidCount}')">${refreshStatInfo.invalidKeywordCount}</a>
				</c:if>
				<shiro:hasPermission name="/internal/customerKeyword/resetInvalidRefreshCount">
					<c:if test="${refreshStatInfo.invalidKeywordCount > 0}">
						<a target="_blank" href="javascript:resetInvaidRefreshCount('${refreshStatInfo.group}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', true, this)">重置</a>
					</c:if>
				</shiro:hasPermission>
			</td>

			<td width=80>
				<font color="${refreshStatInfo.invalidKeywordPercentage > 20 ? "red" : (refreshStatInfo.invalidKeywordPercentage > 10 ? "purple" : "")}">
					<c:if test="${refreshStatInfo.invalidKeywordPercentage > 0}">
						<fmt:formatNumber value="${refreshStatInfo.invalidKeywordPercentage}" pattern="#.##" minFractionDigits="2"/>%
					</c:if>
				</font>
			</td>
			<td width=60>${refreshStatInfo.totalOptimizeCount > 0 ? refreshStatInfo.totalOptimizeCount : ""}</td>
			<td width=80>${refreshStatInfo.totalOptimizedCount > 0 ? refreshStatInfo.totalOptimizedCount : ""}</td>
			<td width=80>${refreshStatInfo.needOptimizeCount > 0 ? refreshStatInfo.needOptimizeCount : ""}</td>
			<td width=80>
				<c:if test="${refreshStatInfo.totalOptimizedCount > 0}">
					<c:set var="avgOptimizedCount" value="${refreshStatInfo.totalOptimizedCount / refreshStatInfo.totalKeywordCount}"/>
					<font color="${avgOptimizedCount <= 30 ? "red" : "black"}">
						<fmt:formatNumber value="${avgOptimizedCount}" pattern="#.##" minFractionDigits="2"/>
					</font>
				</c:if>
			</td>
			<td width=80>${refreshStatInfo.queryCount > 0 ? refreshStatInfo.queryCount : ""}</td>
			<td width=80>
				<font color="${refreshStatInfo.invalidOptimizePercentage > 20 ? "red" : (refreshStatInfo.invalidOptimizePercentage > 10 ? "purple" : "")}">
					<c:if test="${refreshStatInfo.invalidOptimizePercentage > 0}">
						<fmt:formatNumber value="${refreshStatInfo.invalidOptimizePercentage}" pattern="#.##" minFractionDigits="2"/>%
					</c:if>
				</font>
			</td>
			<td width=50>${refreshStatInfo.totalMachineCount > 0 ? refreshStatInfo.totalMachineCount : ""}</td>
			<td width=50>
				<c:if test="${refreshStatInfo.idlePercentage > 0}">
					<fmt:formatNumber value="${refreshStatInfo.idlePercentage}" pattern="#.##" minFractionDigits="2"/>%
				</c:if>
			</td>
			<td width=60>
				<c:if test="${refreshStatInfo.unworkMachineCount > 0}">
					<a target="_blank" href="javascript:findClientStatus('${refreshStatInfo.group}')">${refreshStatInfo.unworkMachineCount}</a>
				</c:if>
			</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</div>

<form id="searchClientStatusForm" style="display: none;" method="post" target="_blank"
	  action="/internal/clientstatus/searchBadClientStatus">
	<input type="hidden" name="hasProblem" value="hasProblem"/>
	<input type="hidden" name="groupName" id="groupName" value=""/>
</form>
<form id="searchCustomerKeywordForm" style="display: none;" method="post" target="_blank"
	  action="/internal/customerKeyword/searchCustomerKeywordLists">
	<input type="hidden" name="optimizeGroupName" id="optimizeGroupName" value=""/>
	<input type="hidden" name="invalidRefreshCount" id="invalidRefreshCount" value=""/>
	<input type="hidden" name="noReachStandardDays" id="noReachStandardDays" value=""/>
	<input type="hidden" name="status" id="status" value="1"/>
</form>

<div id="uploadCSVDialog" class="easyui-dialog" style="left: 40%;">
	<form method="post" id="uploadCSVForm" action="" enctype="multipart/form-data" style="display:none;">
		<table width="95%" style="margin-top: 10px;margin-left: 10px">
			<tr>
				<td>
					搜索引擎:
					<select name="searchEngine" id="searchEngine" style="width: 150px;">
						<option value="百度">百度</option>
						<option value="搜狗">搜狗</option>
						<option value="神马">神马</option>
					</select>
				</td>
			</tr>
			<tr></tr>
			<tr>
				<td>
					设置达标:
					前<input type="text" name="reachStandardPosition" id="reachStandardPosition" class="easyui-numberspinner" data-options="min:0,max:10,increment:1" value="0" style="width:130px;">名
				</td>
			</tr>
			<tr>
				<td>
					<input type="file" id="file" name="file" size=50 height="50px" style="width: 260px;">
				</td>
			</tr>
		</table>
	</form>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/refresh/refresh.js"></script>
<script src="${staticPath }/js/jquery.tablesorter.js"></script>
<script language="javascript">
    <shiro:hasPermission name="/internal/clientstatus/searchBadClientStatus">
    function findClientStatus(groupName) {
        $("#searchClientStatusForm").find("#groupName").val(groupName);
        $("#searchClientStatusForm").submit();
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
    function searchCustomerKeywords(optimizeGroupName) {
        $("#searchCustomerKeywordForm").find("#optimizeGroupName").val(optimizeGroupName);
        $("#searchCustomerKeywordForm").submit();
    }
    </shiro:hasPermission>
    $(function() {
        window.onresize = function (ev) {
            $("#showRefreshStatInfoDiv").css("margin-top", $("#topDiv").height()-5);
		}
        $("#showRefreshStatInfoTable").tablesorter({
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