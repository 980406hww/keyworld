<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <link rel="stylesheet" href="${staticPath}/static/select2/select2-4.0.6.min.css">
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
    <%@ include file="/commons/basejs.jsp" %>
    <%@ include file="/commons/global.jsp" %>
    <style type="text/css">
        ul{list-style: none}
        ul li{margin: 5px}
        ul li > span{width: 100px;text-align: right;display: inline-block}
        input[type='radio']{ margin: 0 5px 0 10px;}
        input[type='checkbox']{ margin: 0 5px 0 10px;}
        .panel.window {z-index: 1000 !important;}
        .window-shadow {z-index: 999 !important;}
        .window-mask {z-index: 998 !important;}
    </style>
    <title>算法测试计划管理</title>
</head>
<body>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 5px;margin-bottom: 5px">
        <form method="post" id="searchAlgorithmTestPlanForm" action="/internal/algorithmAutoTest/searchAlgorithmTestPlans" style="margin-bottom:0px ">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}" />
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            &nbsp;&nbsp;测试计划名称:<input type="text" name="algorithmTestPlanName" id="algorithmTestPlanName" value="${algorithmTestPlanSearchCriteria.algorithmTestPlanName}">
            &nbsp;&nbsp;操作组合名称:<input type="text" name="operationCombineName" id="operationCombineName" value="${algorithmTestPlanSearchCriteria.operationCombineName}">
            &nbsp;&nbsp;机器分组:<input type="text" name="machineGroup" id="machineGroup" value="${algorithmTestPlanSearchCriteria.machineGroup}">
            &nbsp;&nbsp;
            <shiro:hasPermission name="/internal/algorithmAutoTest/searchAlgorithmTestPlans">
            <input type="submit" value=" 查询 " onclick="resetPageNumber()">&nbsp;&nbsp;
            </shiro:hasPermission>

            <shiro:hasPermission name="/internal/algorithmAutoTest/saveAlgorithmTestPlan">
                <input type="button" value=" 添加 " onclick="addAlgorithmTestPlans()">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/algorithmAutoTest/deleteAlgorithmTestPlan">
                <input type="button" value=" 删除所选 " onclick="deleteAlgorithmTestPlans()">&nbsp;&nbsp;
            </shiro:hasPermission>

            <shiro:hasPermission name="/internal/algorithmAutoTest/changeAlgorithmTestPlanStatus">
                <input type="button" value=" 启动所选 " onclick="updateAlgorithmTestPlansStatus('1')">&nbsp;&nbsp;
                <input type="button" value=" 暂停所选 " onclick="updateAlgorithmTestPlansStatus('0')">&nbsp;&nbsp;
            </shiro:hasPermission>
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=70>测试计划名称</td>
            <td align="center" width=70>操作组合名称</td>
            <td align="center" width=70>机器分组</td>
            <td align="center" width=70>测试间隔日期(天)</td>
            <td align="center" width=70>测试词数</td>
            <td align="center" width=70>测试词排名区间</td>
            <td align="center" width=70>刷量</td>
            <td align="center" width=70>状态</td>
            <td align="center" width=70>上次执行时间</td>
            <td align="center" width=70>创建时间</td>
            <td align="center" width=70>修改时间</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;"id="showAlgorithmTestPlanTable" >
        <c:forEach items="${page.records}" var="algorithmTestPlan" varStatus="status">
            <tr align="center" onmouseover="doOver(this);" onmouseout="doOut(this);" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
                <td width=10 align="center"><input type="checkbox" name="uuid" value="${algorithmTestPlan.uuid}" onclick="decideSelectAll()"/></td>
                <td width=70>
                    <a href="javascript:searchAlgorithmTestTasks('${algorithmTestPlan.uuid}')">${algorithmTestPlan.algorithmTestPlanName}</a>
                </td>
                <td width=70>
                    <a href="javascript:searchGroupSettings('${algorithmTestPlan.operationCombineName}')">${algorithmTestPlan.operationCombineName}</a>
                </td>
                <td width=70>
                    <a href="javascript:searchMachineInfos('${algorithmTestPlan.machineGroup}')">${algorithmTestPlan.machineGroup}</a>
                </td>
                <td width=70>${algorithmTestPlan.testIntervalDay}</td>
                <td width=70>${algorithmTestPlan.testKeywordCount}</td>
                <td width=70>${algorithmTestPlan.testkeywordRankBegin}-${algorithmTestPlan.testkeywordRankEnd}</td>
                <td width=70>${algorithmTestPlan.optimizePlanCount}</td>
                <td width=70>
                    <c:choose>
                        <c:when test="${algorithmTestPlan.status eq 1}">
                            激活
                        </c:when>
                        <c:otherwise>
                            暂停
                        </c:otherwise>
                    </c:choose>
                </td>
                <td width=70><fmt:formatDate value="${algorithmTestPlan.executeQueryTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td width=70><fmt:formatDate value="${algorithmTestPlan.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td width=70><fmt:formatDate value="${algorithmTestPlan.updateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td width=80>
                    <shiro:hasPermission name="/internal/algorithmAutoTest/saveAlgorithmTestPlan">
                        <a href="javascript:updateAlgorithmTestPlan('${algorithmTestPlan.uuid}')">修改</a>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/algorithmAutoTest/changeAlgorithmTestPlanStatus">
                        <c:choose>
                            <c:when test="${algorithmTestPlan.status eq 1}">
                                <a href="javascript:changeAlgorithmTestPlanStatus('${algorithmTestPlan.uuid}', '0')">暂停</a>
                            </c:when>
                            <c:otherwise>
                                <a href="javascript:changeAlgorithmTestPlanStatus('${algorithmTestPlan.uuid}', '1')">取消暂停</a>
                            </c:otherwise>
                        </c:choose>
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/algorithmAutoTest/deleteAlgorithmTestPlan">
                        <a href="javascript:deleteAlgorithmTestPlan('${algorithmTestPlan.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div id="select2DialogDiv" title="" class="easyui-dialog" style="display: none;">
    <form id="algorithmTestPlanForm">
        <ul id="formData">
            <input type="hidden" name="algorithmTestPlanUuid" id="algorithmTestPlanUuid">
            <li>
                <span>测试计划名称:</span>
                <input id="algorithmTestPlanName" name="algorithmTestPlanName" title="优化组名" style="width: 200px;" value=""/>
            </li>
            <li>
                <span>操作组合名称:</span>
                <input id="operationCombineName" name="operationCombineName" title="优化组名" style="width: 200px;" value=""/>
            </li>
            <li>
                <span>机器分组:</span>
                <input id="machineGroup" name="machineGroup" title="优化组名" style="width: 200px;" value=""/>
            </li>
            <li id="end">
                <span>测试间隔天数:</span>
                <input id="testIntervalDay" name="testIntervalDay" class="easyui-numberspinner"  data-options="min:0,increment:1,max:30" required style="width: 200px">
            </li>
            <li>
                <span>测试词数:</span>
                <input id="testKeywordCount" name="testKeywordCount" class="easyui-numberspinner"  data-options="min:0,max:100,increment:5" required style="width: 200px">
            </li>
            <li>
                <span>测试词排名起始:</span>
                <input id="testkeywordRankBegin" name="testkeywordRankBegin" class="easyui-numberspinner"  data-options="min:0,max:100,increment:10" style="width: 200px" value="">
            </li>
            <li>
                <span>测试词排名结束:</span>
                <input id="testkeywordRankEnd" name="testkeywordRankEnd" class="easyui-numberspinner"  data-options="min:0,max:100,increment:10" style="width: 200px" value="">
            </li>
            <li>
                <span>刷量:</span>
                <input id="optimizePlanCount" name="optimizePlanCount" class="easyui-numberspinner"  data-options="min:0,max:500,increment:10" required style="width: 200px">
            </li>

        </ul>
    </form>
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

<form id="searchMachineInfoForm" style="display: none;" method="post" target="_blank"
      action="/internal/machineInfo/searchMachineInfos">
    <input type="hidden" name="machineGroup" id="machineGroup" value=""/>
</form>
<form id="searchGroupSettingForm" style="display: none;" method="post" target="_blank"
      action="/internal/groupsetting/searchGroupSettings">
    <input type="hidden" name="operationCombineName" id="operationCombineName" value=""/>
</form>
<form id="searchAlgorithmTestTaskForm" style="display: none;" method="post" target="_blank"
      action="/internal/algorithmAutoTest/showAlgorithmTestTask">
    <input type="hidden" name="algorithmTestPlanUuid" id="algorithmTestPlanUuid" value=""/>
    <input type="hidden" name="currentPage" id="currentPage" value="${page.current}"/>
    <input type="hidden" name="pageSize" id="pageSize" value="${page.size}"/>
</form>

<%@ include file="/commons/loadjs.jsp" %>
<script type="text/javascript" src="${staticPath}/static/select2/select2-4.0.6.min.js"></script>
<script src="${staticPath}/algorithmAutoTest/algorithmTestPlan.js"></script>
<script type="text/javascript">
    <shiro:hasPermission name="/internal/machineInfo/searchMachineInfos">
        function searchMachineInfos(machineGroup) {
            $("#searchMachineInfoForm").find("#machineGroup").val(machineGroup);
            $("#searchMachineInfoForm").submit();
        }
    </shiro:hasPermission>

    <shiro:hasPermission name="/internal/groupsetting/searchGroupSettings">
        function searchGroupSettings(operationCombineName) {
            $("#searchGroupSettingForm").find("#operationCombineName").val(operationCombineName);
            $("#searchGroupSettingForm").submit();
        }
    </shiro:hasPermission>


    <shiro:hasPermission name="/internal/algorithmAutoTest/showAlgorithmTestTask">
        function searchAlgorithmTestTasks(uuid) {
            $("#searchAlgorithmTestTaskForm").find("#algorithmTestPlanUuid").val(uuid);
            $("#searchAlgorithmTestTaskForm").submit();
        }
    </shiro:hasPermission>
</script>
</body>
</html>