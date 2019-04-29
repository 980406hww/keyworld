<%@ include file="/commons/global.jsp" %>
<html>
<head>
	<title>分组设置</title>
	<script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" href="${staticPath}/groupsetting/groupsetting.css">
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
					优化组名: <input type="text" title="请输入优化分组名" name="optimizedGroupName" placeholder="请输入优化分组名" onkeydown="enterIn(event);" value="${groupSettingCriteria.optimizedGroupName}">
				</li>
				<li>
					<span>操作类型: </span>
					<span>
						<select name="operationType" style="width: 150px;">
							<c:forEach items="${operationTypeValues}" var="operationType">
								<c:choose>
									<c:when test="${operationType eq groupSettingCriteria.operationType}"><option selected>${operationType}</option></c:when>
									<c:otherwise><option>${operationType}</option></c:otherwise>
								</c:choose>
							</c:forEach>
						</select>
					</span>
				</li>
				<li>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="trimSearchCondition('1')" value=" 搜索 " >
					&nbsp;&nbsp;
				</li>
				<li>
					<input class="ui-button ui-widget ui-corner-all" type="button" onclick="showSettingDialog(null, this)" value=" 增加 " >&nbsp;
				</li>
			</ul>
		</div>
	</div>
</div>

<form method="post" id="chargeForm" action="/internal/groupsetting/searchGroupSettings">
	<input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
	<input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
	<input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
	<input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
	<input type="hidden" name="optimizedGroupName" id="optimizedGroupName" value="${groupSettingCriteria.optimizedGroupName}"/>
	<input type="hidden" name="operationType" id="operationType" value="${groupSettingCriteria.operationType}"/>
	<input type="hidden" name="terminalType" id="terminalType" value="${groupSettingCriteria.terminalType}"/>
</form>

<div class="datalist">
	<div class="datalist-list">
		<ul>
			<c:forEach items="${page.records}" var="groupVo" varStatus="status">
				<li>
					<div class="header">
						<input type="hidden" name="groupUuid" value="${groupVo.uuid}">
						<span>
							<input type="checkbox" name="uuid" value="" onclick="decideSelectAll();"/>
						</span>
						<span class="groupName" title="${groupVo.groupName}"><a href="javascript:;">${groupVo.groupName}</a></span>
						<span class="userName" title="${groupVo.userName}"><a href="javascript:;"></a>${groupVo.userName}</span>
						<div class="handle">
							<a class="blue" href="javascript:showGroupSettingDialog(${groupVo.uuid})">修改</a>
							<a class="blue" href="javascript:delGroupSetting()">删除</a>
						</div>
					</div>

					<div class="body" listsize="${groupVo.groupSettings.size()}">
						<div class="data-info-wrap">
							<c:forEach items="${groupVo.groupSettings}" var="groupSetting">
								<div class="other-rank">
									<div class="row">
										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.operationType}</a>
											</span>
											<span>
												<a href="javascript:;">操作类型</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.disableStatistics == 1 ? "开放" : "关闭"}</a>
											</span>
												<span>
												<a href="javascript:;">网站统计</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.disableVisitWebsite == 1 ? "访问" : "不访问"}</a>
											</span>
											<span>
												<a href="javascript:;">目标网站</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.pageNo}</a>
											</span>
												<span>
												<a href="javascript:;">页数</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.pageSize}</a>
											</span>
												<span>
												<a href="javascript:;">每页条数</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.zhanneiPercent}</a>
											</span>
											<span>
												<a href="javascript:;">站内搜索比例</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.zhanwaiPercent}</a>
											</span>
											<span>
												<a href="javascript:;">外链检索比例</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.kuaizhaoPercent}</a>
											</span>
											<span>
												<a href="javascript:;">快照点击比例</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.baiduSemPercent}</a>
											</span>
											<span>
												<a href="javascript:;">竞价点击比例</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.dragPercent}</a>
											</span>
											<span>
												<a href="javascript:;">标题拖动比例</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.specialCharPercent}</a>
											</span>
											<span>
												<a href="javascript:;">特殊字符比例</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.multiBrowser == 0 ? "命令多浏览器" : groupSetting.multiBrowser == 1 ? "模拟多浏览器" : "单个浏览器"}</a>
											</span>
												<span>
												<a href="javascript:;">浏览器设置</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.clearCookie == 0 ? "不清理" : groupSetting.clearCookie == 1 ? "每次都清理" : groupSetting.clearCookie == 2 ? "随机操作清理" : "N次操作清理"}</a>
											</span>
												<span>
												<a href="javascript:;">Cookie设置</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.maxUserCount}</a>
											</span>
											<span>
												<a href="javascript:;">最大用户数</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.entryPageMinCount} - ${groupSetting.entryPageMaxCount}</a>
											</span>
											<span>
												<a href="javascript:;">进入页次数</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.oneIPOneUser == 1 ? "是" : "否"}</a>
											</span>
												<span>
												<a href="javascript:;">每IP对每用户</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.justVisitSelfPage == 1 ? "是" : "否"}</a>
											</span>
												<span>
												<a href="javascript:;">在域名下访问</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.randomlyClickNoResult == 1 ? "是" : "否"}</a>
											</span>
												<span>
												<a href="javascript:;">没结果随机点</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.supportPaste == 1 ? "是" : "否"}</a>
											</span>
											<span>
												<a href="javascript:;">支持粘贴输入</a>
											</span>
										</div>

										<div>
											<span class="line1">
												<a href="javascript:;">${groupSetting.clearLocalStorage == 1 ? "是" : "否"}</a>
											</span>
												<span title="清除LocalStorage">
												<a href="javascript:;">清除本地存储</a>
											</span>
										</div>

										<div>
											<span>
												<input class="ui-button ui-widget ui-corner-all" type="button" onclick="trimSearchCondition('1')" value=" 修改 " >
											</span>
											<span>
												<input class="ui-button ui-widget ui-corner-all" type="button" onclick="trimSearchCondition('1')" value=" 删除 " >
											</span>
										</div>
									</div>
								</div>
							</c:forEach>
						</div>
					</div>
				</li>
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

<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/groupsetting/groupsetting.js"></script>
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
    });
</script>
</body>
</html>