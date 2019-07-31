<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>终端机器分组统计</title>
    <%@ include file="/commons/basejs.jsp" %>
    <style>
        td{
            display: table-cell;
            vertical-align: inherit;
        }
        #centerDiv {
            width: 100%;
            margin: auto;
        }
        #topDiv {
            top: 0px;
            left: 0px;
            background-color: white;
            width: 100%;
        }
        #machineInfoMachineGroupSummaryTable {
            width: 100%;
        }
        #machineInfoMachineGroupSummaryTable tr:nth-child(odd){background:#EEEEEE;}
    </style>
</head>
<body>

<div id="topDiv">
    <%@include file="/menu.jsp"%>
    <form action="/internal/machineInfo/machineInfoMachineGroupStat" method="post" id="searchMachineInfoSummaryVOForm" style="margin: 5px 0 5px 10px;">
        机器分组名称:<input type="text" name="machineGroup" id="machineGroup" value="${machineInfoMachineGroupStatCriteria.machineGroup}">
        &nbsp;
        终端类型:
        <select name="terminalType">
            <option value="">请选择终端类型</option>
            <option value="PC" <c:if test="${machineInfoMachineGroupStatCriteria.terminalType.equals('PC')}">selected="selected"</c:if>>PC</option>
            <option value="Phone" <c:if test="${machineInfoMachineGroupStatCriteria.terminalType.equals('Phone')}">selected="selected"</c:if>>Phone</option>
        </select>&nbsp;&nbsp;
        <shiro:hasPermission name="/internal/machineInfo/machineInfoMachineGroupStat">
            <input type="submit" value=" 查询 " onclick="trimSearchCondition()">
        </shiro:hasPermission>
    </form>
    <table style="font-size: 12px; width: 100%" id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=80>机器分组名称</td>
            <td align="center" width=80>终端类型</td>
            <td align="center" width=80>数量</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px">
    <table style="font-size: 12px;"  id="machineInfoMachineGroupSummaryTable">
    <c:forEach items="${machineInfoMachineGroupSummaryVOS}" var="machineInfoMachineGroupSummaryVO" varStatus="status">
        <tr height="30px" onmouseover="doOver(this);" onmouseout="doOut(this);">
            <td align="center" width=80>
                <a href="#" onclick="showMachineInfoListByMachineGroup('${machineInfoMachineGroupSummaryVO.machineGroup}')">${machineInfoMachineGroupSummaryVO.machineGroup}</a>
            </td>
            <td align="center" width=80>${machineInfoMachineGroupSummaryVO.terminalType}</td>
            <td align="center" width=80>${machineInfoMachineGroupSummaryVO.count}</td>
        </tr>
    </c:forEach>
</table>
</div>
<div id="toMachineInfoDialog" style="text-align: center;left: 40%;display: none;">
    <form id="toMachineInfoForm" style="text-align: center;margin-top: 10px;" action="/internal/machineInfo/searchMachineInfos" method="post" target="_blank" >
        <input type="text" id="machineGroup" name="machineGroup" style="width:150px">
    </form>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script language="javascript" type="text/javascript">
    $(function () {
        $("#centerDiv").css("margin-top",$("#topDiv").height()-5);
    });

    function trimSearchCondition() {
        var machineInfoSummaryForm = $("#searchMachineInfoSummaryVOForm");
        var machineGroup = machineInfoSummaryForm.find("#machineGroup").val();
        if(machineGroup != "") {
            machineInfoSummaryForm.find("#machineGroup").val($.trim(machineGroup));
        }
    }
<shiro:hasPermission name="/internal/machineInfo/searchMachineInfos">
    function showMachineInfoListByMachineGroup(machineGroup){
        $("#toMachineInfoForm").find("#machineGroup").val(machineGroup.trim());
        $("#toMachineInfoForm").submit();
    }
</shiro:hasPermission>
</script>
</body>
</html>

