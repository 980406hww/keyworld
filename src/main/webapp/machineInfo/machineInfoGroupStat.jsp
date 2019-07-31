<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>终端分组统计</title>
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
        #MachineInfoGroupSummaryTable {
            width: 100%;
        }
        #MachineInfoGroupSummaryTable tr:nth-child(odd){background:#EEEEEE;}
    </style>
</head>
<body>

<div id="topDiv">
    <%@include file="/menu.jsp"%>
    <form action="/internal/machineInfo/machineInfoGroupStat" method="post" id="searchMachineInfoSummaryVOForm" style="margin: 5px 0 5px 10px;">
        分组名称:<input type="text" name="groupName" id="groupName" value="${machineInfoGroupStatCriteria.groupName}">
        &nbsp;&nbsp;
        终端类型:
        <select name="terminalType">
            <option value="">请选择终端类型</option>
            <option value="PC" <c:if test="${machineInfoGroupStatCriteria.terminalType.equals('PC')}">selected="selected"</c:if>>PC</option>
            <option value="Phone" <c:if test="${machineInfoGroupStatCriteria.terminalType.equals('Phone')}">selected="selected"</c:if>>Phone</option>
        </select>&nbsp;&nbsp;
        <shiro:hasPermission name="/internal/machineInfo/machineInfoGroupStat">
            <input type="submit" value=" 查询 " onclick="trimSearchCondition()">
        </shiro:hasPermission>
    </form>
    <table  style="font-size: 12px;  width: 100%"  id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=80>分组名称</td>
            <td align="center" width=80>终端类型</td>
            <td align="center" width=80>数量</td>
        </tr>
    </table>
</div>
<div id="centerDiv">
    <table width=40% style="font-size: 12px;"  id="MachineInfoGroupSummaryTable">
    <c:forEach items="${machineInfoGroupSummaryVOs}" var="machineInfoGroupSummaryVOs" varStatus="status">
        <tr align="left" height="30px" onmouseover="doOver(this);" onmouseout="doOut(this);">
            <td align="center" width=80>${machineInfoGroupSummaryVOs.group}</td>
            <td align="center" width=80>${machineInfoGroupSummaryVOs.terminalType}</td>
            <td align="center" width=80>${machineInfoGroupSummaryVOs.count}</td>
        </tr>
    </c:forEach>
</table>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script language="javascript" type="text/javascript">
    $(function () {
        $("#centerDiv").css("margin-top",$("#topDiv").height());
        alignTableHeader();
        window.onresize = function(){
            alignTableHeader();
        }
    });
    function alignTableHeader() {
        var td = $("#MachineInfoGroupSummaryTable tr:first td");
        var ctd = $("#headerTable tr:first td");
        $.each(td, function (idx, val) {
            ctd.eq(idx).width($(val).width());
        });
    }
    function trimSearchCondition() {
        var machineInfoSummaryForm = $("#searchMachineInfoSummaryVOForm");
        var group = machineInfoSummaryForm.find("#group").val();
        if(group != "") {
            machineInfoSummaryForm.find("#group").val($.trim(group));
        }
    }
</script>
</body>
</html>

