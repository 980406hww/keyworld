<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/basejs.jsp" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>刷量统计列表</title>
	<script language="javascript" type="text/javascript" src="/toastmessage/jquery.toastmessage.js"></script>
	<link rel="stylesheet" href="/toastmessage/css/jquery.toastmessage.css">
	<script language="javascript" type="text/javascript" src="/common.js"></script>
	<script language="javascript">
        function doOver(obj) {
            obj.style.backgroundColor = "green";
        }

        function doOut(obj) {
            var rowIndex = obj.rowIndex;
            if ((rowIndex % 2) == 0) {
                obj.style.backgroundColor = "#eeeeee";
            } else {
                obj.style.backgroundColor = "#ffffff";
            }
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

		<shiro:hasPermission name="/internal/customerKeyword/resetInvalidRefreshCount">
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
                        $().toastmessage('showWarningToast', "重置成功！");
                        window.location.reload();
                    }else{
                        $().toastmessage('showWarningToast', "重置失败！");
                    }
                },
                error: function () {
                    $().toastmessage('showWarningToast', "重置失败！");
                }
            });
        }
		</shiro:hasPermission>
	</script>
</head>
<body>
<div>
	<div>
		<%@include file="/menu.jsp" %>
	</div>
	<table>

	</table>
</div>
<div id="showRefreshStatInfoDiv" style="margin-top: 40px;">
      <table width=1240 style="font-size:12px;" cellpadding=3>
      	  <tr>
      	  	 <td colspan=13>
      	  	 	<form method="post" id="searchRefreshStatInfoForm" action="/internal/refreshstatinfo/searchRefreshStatInfos">
      	  	 		<table style="font-size:12px;">
	      	  	 		<tr>
			          	    <td align="right">分组名称:<input name="groupName" id="groupName" type="text" style="width:200px;" value="${refreshStatInfoCriteria.groupName}"></td>
			          	    <td align="right">客户名称:<input name="customerName" id="customerName" type="text" style="width:200px;" value="${refreshStatInfoCriteria.customerName}"></td>
							<shiro:hasPermission name="/internal/refreshstatinfo/searchRefreshStatInfos">
								<td align="right"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 "></td>
							</shiro:hasPermission>
			          	</tr>
			        </table>
	      	  	 	<table style="font-size:12px;">
	      	  	 		 <tr bgcolor="#eeeeee" height=30>
			          	   <td align="center" width=60 rowspan="2">类型</td>
					       <td align="center" width=80 colspan="4">关键字</td>
					       <td align="center" width=60 colspan="5">刷的次数</td>
					       <td align="center" width=100 colspan="2">机器数</td>
		   				</tr>
		   				<tr bgcolor="#eeeeee" height=30>
					       <td align="center" width=80>总数</td>
					       <td align="center" width=80>待刷数</td>
					       <td align="center" width=80>无效刷数</td>
					       <td align="center" width=80>无效占比</td>
					       <td align="center" width=60>总次数</td>
					       <td align="center" width=80>已刷次数</td>
					       <td align="center" width=80>待刷次数</td>
					       <td align="center" width=80>取词次数</td>
					       <td align="center" width=60>无效占比</td>
					       <td align="center" width=100>总数</td>
					       <td align="center" width=60>已停数</td>
		   				</tr>
						<c:forEach items="${refreshStatInfoVOs}" var="refreshStatInfoVO" varStatus="tr">
							 <c:choose>
								 <c:when test="${tr.count % 2 != 0}">
									 <tr onmouseover="doOver(this);" onmouseout="doOut(this);" style="background-color: #eeeeee;" height="30">
								 </c:when>
								 <c:otherwise>
									 <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height="30">
								 </c:otherwise>
							 </c:choose>
								 <td>${refreshStatInfoVO.group}</td>
								 <td>${refreshStatInfoVO.totalKeywordCount}</td>
								 <td>${refreshStatInfoVO.needOptimizeKeywordCount > 0 ? refreshStatInfoVO.needOptimizeKeywordCount : ""}</td>
								 <td>
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
							     <c:if test="${refreshStatInfoVO.invalidKeywordCount > 0}">
									 <c:choose>
										 <c:when test="${'总计' eq refreshStatInfoVO.group}">
											 <a target="_blank" href="javascript:resetInvaidRefreshCount('${refreshStatInfoCriteria.groupName == null ? "" : refreshStatInfoCriteria.groupName}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', this)">重置</a>
										 </c:when>
										 <c:otherwise>
											 <a target="_blank" href="javascript:resetInvaidRefreshCount('${refreshStatInfoVO.group}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', this)">重置</a>
										 </c:otherwise>
									 </c:choose>
								 </c:if>
								 </td>
								 <td>
									<font color="${refreshStatInfoVO.invalidKeywordPercentage > 20 ? "red" : (refreshStatInfoVO.invalidKeywordPercentage > 10 ? "purple" : "")}">
										<c:if test="${refreshStatInfoVO.invalidKeywordPercentage > 0}">
											<fmt:formatNumber value="${refreshStatInfoVO.invalidKeywordPercentage}" pattern="#.##" minFractionDigits="2" />%
										</c:if>
									</font>
								 </td>
								 <td>${refreshStatInfoVO.totalOptimizeCount > 0 ? refreshStatInfoVO.totalOptimizeCount : ""}</td>
								 <td>${refreshStatInfoVO.totalOptimizedCount > 0 ? refreshStatInfoVO.totalOptimizedCount : ""}</td>
								 <td>${refreshStatInfoVO.needOptimizeCount > 0 ? refreshStatInfoVO.needOptimizeCount : ""}</td>
								 <td>${refreshStatInfoVO.queryCount > 0 ? refreshStatInfoVO.queryCount : ""}</td>
								 <td>
									<font color="${refreshStatInfoVO.invalidOptimizePercentage > 20 ? "red" : (refreshStatInfoVO.invalidOptimizePercentage > 10 ? "purple" : "")}">
										<c:if test="${refreshStatInfoVO.invalidOptimizePercentage > 0}">
											<fmt:formatNumber value="${refreshStatInfoVO.invalidOptimizePercentage}" pattern="#.##" minFractionDigits="2" />%
										</c:if>
								 	</font>
								 </td>
								 <td>${refreshStatInfoVO.totalMachineCount > 0 ? refreshStatInfoVO.totalMachineCount : ""}</td>
								 <td>
								 <c:if test="${refreshStatInfoVO.unworkMachineCount > 0}">
									 <c:choose>
										 <c:when test="${'总计' eq refreshStatInfoVO.group}">
											 <a target="_blank" href="javascript:findClientStatus(null)">${refreshStatInfoVO.unworkMachineCount}</a>
										 </c:when>
										 <c:otherwise>
											 <a target="_blank" href="javascript:findClientStatus('${refreshStatInfoVO.group}')">${refreshStatInfoVO.unworkMachineCount}</a>
										 </c:otherwise>
									 </c:choose>
								 </c:if>
								 </td>
							  </tr>
						</c:forEach>
		      	  	</table>
      	  	 	</form>
      	  	 </td>
      	  </tr>
      </table>
</div>
<form id="searchClientStatusForm" style="display: none;" method="post" target="_blank" action="/internal/clientstatus/searchBadClientStatus">
	<input type="hidden" name="hasProblem" value="hasProblem"/>
	<input type="hidden" name="groupName" id="groupName" value=""/>
</form>
<form id="searchCustomerKeywordForm" style="display: none;" method="post" target="_blank" action="/internal/customerKeyword/searchCustomerKeywordLists">
	<input type="hidden" name="optimizeGroupName" id="optimizeGroupName" value=""/>
	<input type="hidden" name="invalidRefreshCount" id="invalidRefreshCount" value=""/>
</form>
</body>
</html>

