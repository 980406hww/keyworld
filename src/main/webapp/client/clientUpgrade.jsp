<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>终端升级</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
	<%@include file="/menu.jsp" %>
	<div style="margin-top: 35px">
		<form method="post" id="searchClientUpgradeFrom" action="/internal/ClientUpgrade/searchClientUpgrades" style="margin-bottom:0px">
			<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
			<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
			<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
			<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
		</form>
		<div style="margin: 10px 20px 5px 0px" align="right">
            <shiro:hasPermission name="/internal/clientUpgrade/saveClientUpgrade">
			<a href="#" onclick="showClientUpgrade(null)">新增</a>
            </shiro:hasPermission>
		</div>
	</div>
	<table style="font-size:12px; width: 100%;" id="headerTable">
		<tr bgcolor="" height="30">
			<td align="center" width="10">
				<input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/>
			</td>
			<td align="center" width=60>程序类型</td>
			<td align="center" width=50>当前版本</td>
			<td align="center" width=50>目标版本</td>
			<td align="center" width=50>最大升级数</td>
			<td align="center" width=50>升级情况</td>
			<td align="center" width=40>任务状态</td>
			<td align="center" width=60>创建时间</td>
			<td align="center" width=50>操作</td>
		</tr>
	</table>
</div>

<div id="centerDiv" style="margin-bottom: 30px;">
	<table style="font-size:12px; width: 100%; table-layout:fixed; " id="showClientUpgradeTable">
		<c:forEach items="${page.records}" var="info" varStatus="status">
			<tr align="left" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
				<td align="center" width=13 align="center"><input type="checkbox" name="uuid" value="${info.uuid}" onclick="decideSelectAll()"/></td>
				<td align="center" width=60>${info.programType}</td>
				<td align="center" width=50>${info.version}</td>
				<td align="center" width=50>${info.targetVersion}</td>
				<td align="center" width=50>${info.maxUpgradeCount}</td>
				<td align="center" width=50>${info.residualUpgradeCount > 0 ? "Processing" : "Completed"}</td>
				<td align="center" width=40>${info.status == true ? "开始" : "暂停"}</td>
				<td align="center" width=60><fmt:formatDate  value="${info.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
				<td align="center" width=50>
                    <shiro:hasPermission name="/internal/clientUpgrade/saveClientUpgrade">
					<a href="javascript:updateClientUpgradeStatus('${info.uuid}','${info.status}')">${info.status == true ? "暂停" : "开始"}</a>
					| <a href="javascript:showClientUpgrade(${info.uuid})">修改</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/clientUpgrade/deleteClientUpgrade">
					| <a href="javascript:deleteClientUpgrade(${info.uuid})">删除</a>
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
		<option>500</option>
		<option>1000</option>
	</select>
	</div>
</div>

<div id="clientUpgradeDialog" class="easyui-dialog" style="display: none;left: 40%;">
	<form id="clientUpgradeForm" style="margin-bottom: 0px;" method="post" action="">
		<table style="font-size:14px;" cellpadding="5">
			<tr>
				<td align="right">程序类型<label style="color: red">*</label>：</td>
				<td>
					<select id="programType">
						<option value="New">New</option>
						<option value="Old">Old</option>
					</select>
				</td>
			</tr>
			<tr>
				<td align="right">当前版本<label style="color: red">*</label>：</td>
				<td><input type="text" id="version" style="width:150px;"></td>
			</tr>
			<tr>
				<td align="right">目标版本<label style="color: red">*</label>：</td>
				<td><input type="text" id="targetVersion" style="width:150px;"></td>
			</tr>
			<tr>
				<td align="right">最大升级数<label style="color: red">*</label>：</td>
				<td><input type="text" id="maxUpgradeCount" style="width:150px;"></td>
			</tr>
			<tr>
				<td align="right">升级状态<label style="color: red">*</label>：</td>
				<td>
					<select id="status">
						<option value="true">开始</option>
						<option value="false">暂停</option>
					</select>
				</td>
			</tr>
		</table>
	</form>
</div>
<%@ include file="/commons/loadjs.jsp"%>
<script src="${staticPath}/client/clientUpgrade.js"></script>
</body>
</html>


