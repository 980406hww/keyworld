<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>刷量统计列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@include file="/menu.jsp" %>
	<form method="post" id="searchRefreshStatInfoForm" action="/internal/refreshstatinfo/searchRefreshStatInfos"
		  >
		<table style="font-size:12px;">
			<tr>
				<td align="right">分组名称:<input name="groupName" id="groupName" type="text" style="width:200px;"
											  value="${refreshStatInfoCriteria.groupName}"></td>
				<td align="right">客户名称:<input name="customerName" id="customerName" type="text" style="width:200px;"
											  value="${refreshStatInfoCriteria.customerName}"></td>
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
					<a download="keywordUrl.txt" href="/keywordUrl.txt" target="blank" id="downTXT" style="display: none">点击下载</a>
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
	<div id="showRefreshStatInfoDiv">
		<table id="showRefreshStatInfoTable" width=100%">
			<c:forEach items="${refreshStatInfoVOs}" var="refreshStatInfoVO" varStatus="tr">
				<c:choose>
					<c:when test="${tr.count % 2 != 0}">
						<tr onmouseover="doOver(this);" onmouseout="doOut(this);" style="background-color: #eeeeee;" height="30">
					</c:when>
					<c:otherwise>
						<tr onmouseover="doOver(this);" onmouseout="doOut(this);" height="30">
					</c:otherwise>
				</c:choose>
				<td width=30 align="center"><input type="checkbox" name="uuid" value="${refreshStatInfoVO.group}" onclick="decideSelectAll()"/></td>
				<td width=100><a href="javascript:searchCustomerKeywords('${refreshStatInfoVO.group}')">${refreshStatInfoVO.group}</a></td>
				<td width=80>${refreshStatInfoVO.totalKeywordCount}</td>
				<td width=80>
					<c:if test="${refreshStatInfoVO.reachStandardKeywordCount > 0}">
						<c:choose>
							<c:when test="${'总计' eq refreshStatInfoVO.group}">
								<a href="javascript:findKeyword(null , null)">${refreshStatInfoVO.reachStandardKeywordCount}(${refreshStatInfoVO.todaySubTotal})</a>
							</c:when>
							<c:otherwise>
								<a href="javascript:findKeyword('${refreshStatInfoVO.group}', null)">${refreshStatInfoVO.reachStandardKeywordCount}(${refreshStatInfoVO.todaySubTotal})</a>
							</c:otherwise>
						</c:choose>
					</c:if>
				</td>
				<td width=80>
					<c:if test="${refreshStatInfoVO.reachStandardPercentage > 0}">
						<fmt:formatNumber value="${refreshStatInfoVO.reachStandardPercentage}" pattern="#.##" minFractionDigits="2"/>%
					</c:if>
				</td>
				<td width=80>${refreshStatInfoVO.zeroOptimizedCount > 0 ? refreshStatInfoVO.zeroOptimizedCount : ""}</td>
				<td width=80>${refreshStatInfoVO.needOptimizeKeywordCount > 0 ? refreshStatInfoVO.needOptimizeKeywordCount : ""}</td>
				<td width=80>
					<c:if test="${refreshStatInfoVO.invalidKeywordCount > 0}">
						<c:choose>
							<c:when test="${'总计' eq refreshStatInfoVO.group}">
								<a href="javascript:findKeyword(null, '${refreshStatInfoVO.maxInvalidCount}')">${refreshStatInfoVO.invalidKeywordCount}</a>
							</c:when>
							<c:otherwise>
								<a href="javascript:findKeyword('${refreshStatInfoVO.group}', '${refreshStatInfoVO.maxInvalidCount}')">${refreshStatInfoVO.invalidKeywordCount}</a>
							</c:otherwise>
						</c:choose>
					</c:if>
					<shiro:hasPermission name="/internal/customerKeyword/resetInvalidRefreshCount">
						<c:if test="${refreshStatInfoVO.invalidKeywordCount > 0}">
							<c:choose>
								<c:when test="${'总计' eq refreshStatInfoVO.group}">
									<a target="_blank"
									   href="javascript:resetInvaidRefreshCount('${refreshStatInfoCriteria.groupName == null ? "" : refreshStatInfoCriteria.groupName}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', false, this)">重置</a>
								</c:when>
								<c:otherwise>
									<a target="_blank"
									   href="javascript:resetInvaidRefreshCount('${refreshStatInfoVO.group}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', true, this)">重置</a>
								</c:otherwise>
							</c:choose>
						</c:if>
					</shiro:hasPermission>
				</td>

				<td width=80>
					<font color="${refreshStatInfoVO.invalidKeywordPercentage > 20 ? "red" : (refreshStatInfoVO.invalidKeywordPercentage > 10 ? "purple" : "")}">
						<c:if test="${refreshStatInfoVO.invalidKeywordPercentage > 0}">
							<fmt:formatNumber value="${refreshStatInfoVO.invalidKeywordPercentage}" pattern="#.##"
											  minFractionDigits="2"/>%
						</c:if>
					</font>
				</td>
				<td width=60>${refreshStatInfoVO.totalOptimizeCount > 0 ? refreshStatInfoVO.totalOptimizeCount : ""}</td>
				<td width=80>${refreshStatInfoVO.totalOptimizedCount > 0 ? refreshStatInfoVO.totalOptimizedCount : ""}</td>
				<td width=80>${refreshStatInfoVO.needOptimizeCount > 0 ? refreshStatInfoVO.needOptimizeCount : ""}</td>
				<td width=80>
					<c:if test="${refreshStatInfoVO.totalOptimizedCount > 0}">
						<c:set var="avgOptimizedCount" value="${refreshStatInfoVO.totalOptimizedCount / refreshStatInfoVO.totalKeywordCount}"/>
						<font color="${avgOptimizedCount <= 30 ? "red" : "black"}">
							<fmt:formatNumber value="${avgOptimizedCount}" pattern="#.##" minFractionDigits="2"/>
						</font>
					</c:if>
				</td>
				<td width=80>${refreshStatInfoVO.queryCount > 0 ? refreshStatInfoVO.queryCount : ""}</td>
				<td width=80>
					<font color="${refreshStatInfoVO.invalidOptimizePercentage > 20 ? "red" : (refreshStatInfoVO.invalidOptimizePercentage > 10 ? "purple" : "")}">
						<c:if test="${refreshStatInfoVO.invalidOptimizePercentage > 0}">
							<fmt:formatNumber value="${refreshStatInfoVO.invalidOptimizePercentage}" pattern="#.##"
											  minFractionDigits="2"/>%
						</c:if>
					</font>
				</td>
				<td width=50>${refreshStatInfoVO.totalMachineCount > 0 ? refreshStatInfoVO.totalMachineCount : ""}</td>
				<td width=50>
					<c:if test="${refreshStatInfoVO.idlePercentage > 0}">
						<fmt:formatNumber value="${refreshStatInfoVO.idlePercentage}" pattern="#.##"
										  minFractionDigits="2"/>%
					</c:if>
				</td>
				<td width=60>
					<c:if test="${refreshStatInfoVO.unworkMachineCount > 0}">
						<c:choose>
							<c:when test="${'总计' eq refreshStatInfoVO.group}">
								<a target="_blank"
								   href="javascript:findClientStatus(null)">${refreshStatInfoVO.unworkMachineCount}</a>
							</c:when>
							<c:otherwise>
								<a target="_blank"
								   href="javascript:findClientStatus('${refreshStatInfoVO.group}')">${refreshStatInfoVO.unworkMachineCount}</a>
							</c:otherwise>
						</c:choose>
					</c:if>
				</td>
				</tr>
			</c:forEach>
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
</script>
</body>
</html>