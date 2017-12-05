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
	  style="margin-top: 35px;">
	<table style="font-size:12px;">
		<tr>
			<td align="right">分组名称:<input name="groupName" id="groupName" type="text" style="width:200px;"
										  value="${refreshStatInfoCriteria.groupName}"></td>
			<td align="right">客户名称:<input name="customerName" id="customerName" type="text" style="width:200px;"
										  value="${refreshStatInfoCriteria.customerName}"></td>
			<td align="right">
				&nbsp;
				<shiro:hasPermission name="/internal/refreshstatinfo/searchRefreshStatInfos">
					<input type="submit" name="btnQuery" id="btnQuery" value=" 查询 ">
				</shiro:hasPermission>
			</td>
		</tr>
	</table>
</form>
	<table style="font-size:12px;" id="headerTable" width=100%">
		<tr bgcolor="#eeeeee" height=30>
			<td align="center" width=100 rowspan="2">类型</td>
			<td align="center" width=80 colspan="4">关键字</td>
			<td align="center" width=80 colspan="6">刷的次数</td>
			<td align="center" width=100 colspan="2">机器数</td>
		</tr>
		<tr bgcolor="#eeeeee" height=30>
			<%--<td align="center" width=80></td>--%>
			<td align="center" width=80>总数</td>
			<td align="center" width=80>待刷数</td>
			<td align="center" width=80>无效刷量</td>
			<td align="center" width=80>无效占比</td>
			<td align="center" width=60>总次数</td>
			<td align="center" width=80>已刷次数</td>
			<td align="center" width=80>待刷次数</td>
			<td align="center" width=80>平均有效刷量</td>
			<td align="center" width=80>取词次数</td>
			<td align="center" width=60>无效占比</td>
			<td align="center" width=100>总数</td>
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
				<td width=100>${refreshStatInfoVO.group}</td>
				<td width=80>${refreshStatInfoVO.totalKeywordCount}</td>
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
									   href="javascript:resetInvaidRefreshCount('${refreshStatInfoCriteria.groupName == null ? "" : refreshStatInfoCriteria.groupName}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', this)">重置</a>
								</c:when>
								<c:otherwise>
									<a target="_blank"
									   href="javascript:resetInvaidRefreshCount('${refreshStatInfoVO.group}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', this)">重置</a>
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
				<td width=100>${refreshStatInfoVO.totalMachineCount > 0 ? refreshStatInfoVO.totalMachineCount : ""}</td>
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
</form>
<script language="javascript">
    $(function () {
        $("#showRefreshStatInfoDiv").css("margin-top",$("#topDiv").height());
        alignTableHeader();
        window.onresize = function(){
            alignTableHeader();
        }
    });
    function alignTableHeader(){
        var td = $("#showRefreshStatInfoTable tr:first td:gt(0)");
        var ctd = $("#headerTable tr:eq(1) td");
        $("#headerTable tr:eq(0) td:eq(0)").width($("#showRefreshStatInfoTable tr:first td:eq(0)").width());
        $.each(td, function (idx, val) {
            ctd.eq(idx).width($(val).width());
        });
    }

    <shiro:hasPermission name="/internal/clientstatus/searchBadClientStatus">
    function findClientStatus(groupName) {
        $("#searchClientStatusForm").find("#groupName").val(groupName);
        $("#searchClientStatusForm").submit();
    }
    </shiro:hasPermission>

    <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
    function findKeyword(optimizeGroupName, invalidRefreshCount) {
        $("#searchCustomerKeywordForm").find("#optimizeGroupName").val(optimizeGroupName);
        $("#searchCustomerKeywordForm").find("#invalidRefreshCount").val(invalidRefreshCount);
        $("#searchCustomerKeywordForm").submit();
    }
    </shiro:hasPermission>

    function resetInvaidRefreshCount(groupName, customerName, self){
        var customerKeyword = {};
        customerKeyword.customerName = customerName;
        customerKeyword.groupName = groupName;
        $.ajax({
            url: '/internal/customerKeyword/resetInvalidRefreshCount',
            data: JSON.stringify(customerKeyword),
            type: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            success: function (result) {
                if(result){
                    $().toastmessage('showSuccessToast', "重置成功！");
                    window.location.reload();
                }else{
                    $().toastmessage('showErrorToast', "重置失败！");
                }
            },
            error: function () {
                $().toastmessage('showErrorToast', "重置失败！");
            }
        });
    }
</script>
</body>
</html>