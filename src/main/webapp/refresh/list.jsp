<%@ page contentType="text/html;charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
<style>
.wrap {word-break: break-all; word-wrap:break-word;}
<!--
#div1{
	display:none;
	background-color :#f6f7f7;
	color:#333333;
	font-size:12px;
	line-height:18px;
	border:1px solid #e1e3e2;
	width:350;
	height:50;
}
#div2 {
	display: none;
	background-color: #ACF106;
	margin: -50 0 0 -11;
    color: #E80404;
    font-size: 20px;
    line-height: 18px;
    border: 2px solid #104454;
    width: 100;
    height: 22;
    left: 50%;
    top: 50%;
    z-index: 25;
    position: fixed;
}
-->
</style>
	<link href="/css/menu.css" rel="stylesheet" type="text/css" />
	<script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
	<script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
	<script language="javascript" type="text/javascript" src="/js/slide1.12.4.js"></script>
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

        function findClientStatus(groupName) {
			$$$("#searchClientStatusForm").find("#groupName").val(groupName);
            $$$("#searchClientStatusForm").submit();
		}
		
		function findKeyword(optimizeGroupName, invalidRefreshCount) {
            $$$("#searchCustomerKeywordForm").find("#optimizeGroupName").val(optimizeGroupName);
            $$$("#searchCustomerKeywordForm").find("#invalidRefreshCount").val(invalidRefreshCount);
            $$$("#searchCustomerKeywordForm").submit();
        }

        function resetInvaidRefreshCount(groupName, customerName, self){
            var customerKeyword = {};
            customerKeyword.customerName = customerName;
            customerKeyword.groupName = groupName;
            $$$.ajax({
                url: '/internal/customerKeyword/resetInvalidRefreshCount',
                data: JSON.stringify(customerKeyword),
                type: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                success: function (result) {
                    if(result){
                        showInfo("重置成功！", self);
                        window.location.reload();
                    }else{
                        showInfo("重置失败！", self);
                    }
                },
                error: function () {
                    showInfo("重置失败！", self);
                }
            });
        }

        function showInfo(content, e) {
            e = e || window.event;
            var div1 = document.getElementById('div2'); //将要弹出的层
            div1.innerText = content;
            div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见
            div1.style.left = getLeft(e) + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
            //div1.style.top = getTop(e) + 5;
            //div1.style.position = "absolute";

            var intervalID = setInterval(function() {
                div1.style.display = "none";
            }, 3000);
        }

        function getTop(e) {
            var offset=e.offsetTop;
            if(e.offsetParent!=null) offset+=getTop(e.offsetParent);
            return offset;
        }
        //获取元素的横坐标
        function getLeft(e) {
            var offset=e.offsetLeft;
            if(e.offsetParent!=null) offset+=getLeft(e.offsetParent);
            return offset;
        }
        function showTip(content,e) {
            e = e||window.event;
            var div1 = document.getElementById('div1'); //将要弹出的层
            div1.innerText = content;
            div1.style.display="block"; //div1初始状态是不可见的，设置可为可见
            div1.style.left=e.clientX+10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
            div1.style.top=e.clientY+5;
            div1.style.position="absolute";
        }

        //关闭层div1的显示
        function closeTip() {
            var div1 = document.getElementById('div1');
            div1.style.display="none";
        }
	</script>
</head>
<body>
<div id="showRefreshStatInfoDiv">
      <table width=1240 style="font-size:12px;" cellpadding=3>
            <tr>
				<td colspan=13 align="left">
					<%@include file="/menu.jsp" %>
				</td>
			</tr>
      	  <tr>
      	  	 <td colspan=13>
      	  	 	<form method="post" id="searchRefreshStatInfoForm" action="/internal/refreshstatinfo/searchRefreshStatInfos">
      	  	 		<table style="font-size:12px;">
	      	  	 		<tr>
			          	  <td align="right">分组名称:<input name="groupName" id="groupName" type="text" style="width:200px;" value="${refreshStatInfoCriteria.groupName}"></td>
			          	  <td align="right">客户名称:<input name="customerName" id="customerName" type="text" style="width:200px;" value="${refreshStatInfoCriteria.customerName}"></td>
			          	  <td align="right"><input type="submit" name="btnQuery" id="btnQuery" value=" 查询 "></td>
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
						<c:forEach items="${refreshStatInfoVOs}" var="refreshStatInfoVO">
							 <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height="30">
								 <td>${refreshStatInfoVO.group}</td>
								 <td>${refreshStatInfoVO.totalKeywordCount}</td>
								 <td>${refreshStatInfoVO.needOptimizeKeywordCount > 0 ? refreshStatInfoVO.needOptimizeKeywordCount : ""}</td>
								 <td>
								 ${refreshStatInfoVO.invalidKeywordCountStr}
							     <c:if test="${refreshStatInfoVO.invalidKeywordCount > 0}">
									 <c:choose>
										 <c:when test="${'总计' eq refreshStatInfoVO.group}">
											 <a href="javascript:resetInvaidRefreshCount('${refreshStatInfoCriteria.groupName == null ? "" : refreshStatInfoCriteria.groupName}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', this)">重置</a>
										 </c:when>
										 <c:otherwise>
											 <a href="javascript:resetInvaidRefreshCount('${refreshStatInfoVO.group}', '${refreshStatInfoCriteria.customerName == null ? "" : refreshStatInfoCriteria.customerName}', this)">重置</a>
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
								 <td>${refreshStatInfoVO.unworkMachineCountStr}</td>
							  </tr>
						</c:forEach>
		      	  	</table>
      	  	 	</form>
      	  	 </td>
      	  </tr>
      </table>
</div>
<div style="display:none;">
</div>
<div id="div2"></div>
<form id="searchClientStatusForm" style="display: none;" method="post" action="/internal/clientstatus/searchClientStatuses">
	<a type="hidden" name="hasProblem" value="hasProblem"/>
	<a type="hidden" name="groupName" id="groupName" value=""/>
</form>
<form id="searchCustomerKeywordForm" style="display: none;" method="post" action="/internal/customerKeyword/searchCustomerKeywords">
	<a type="hidden" name="optimizeGroupName" id="optimizeGroupName" value=""/>
	<a type="hidden" name="invalidRefreshCount" id="invalidRefreshCount" value=""/>
</form>
</body>
</html>

