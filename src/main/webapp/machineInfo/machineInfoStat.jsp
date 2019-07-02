<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>终端统计</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <form action="/internal/machineInfo/machineInfoStat" method="post" id="searchMachineInfoSummaryVOForm" style="margin-top: 35px;margin-left: 20px">
     客户端ID前缀:&nbsp;<input type="text" name="clientIDPrefix" id="clientIDPrefix" value="${clientIDPrefix}">
        &nbsp;&nbsp;城市: <input type="text" name="city" id="city" value="${city}">&nbsp;&nbsp;
        流转分组: <input type="text" name="switchGroupName" id="switchGroupName" value="${switchGroupName}">&nbsp;&nbsp;
        <shiro:hasPermission name="/internal/machineInfo/machineInfoStat">
            <input type="submit" value=" 查询 " onclick="trimSearchCondition()">
        </shiro:hasPermission>
    </form>
    <table width=60% style="font-size: 12px;" cellpadding=3 id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=100>客户端ID前缀</td>
            <td align="center" width=100>总数</td>
            <td align="center" width=120>类型</td>
            <td align="center" width=120>小计</td>
            <td align="center" width=100>城市</td>
            <td align="center" width=100>流转分组</td>
            <td align="center" width=50>数量</td>
        </tr>
    </table>
</div>
<div id="centerDiv">
<table width=60% style="font-size: 12px" id="machineInfoSummaryTable" cellpadding=3>
    <c:set var="color" scope="session" value="#eeeeff"/>
    <c:forEach items="${machineInfoSummaryVOs}" var="machineInfoSummaryVO" varStatus="status">
            <c:if test="${machineInfoSummaryVO.clientIDPrefixCount>0}">
                <c:choose>
                    <c:when test="${color == '#eeeeff'}">
                        <c:set var="color" scope="session" value="#ffeeee"/>
                    </c:when>
                    <c:when test="${color == '#ffeeee'}">
                        <c:set var="color" scope="session" value="#eeeeff"/>
                    </c:when>
                </c:choose>
            </c:if>
        <tr align="left" bgcolor="${color}">
            <c:if test="${machineInfoSummaryVO.clientIDPrefixCount>0}">
                <td rowspan="${machineInfoSummaryVO.clientIDPrefixCount}">${machineInfoSummaryVO.clientIDPrefix}</td>
                <td rowspan="${machineInfoSummaryVO.clientIDPrefixCount}">${machineInfoSummaryVO.clientIDPrefixTotalCount}</td>
            </c:if>
            <c:if test="${machineInfoSummaryVO.typeCount>0}">
                <td rowspan="${machineInfoSummaryVO.typeCount}">${machineInfoSummaryVO.type}</td>
                <td rowspan="${machineInfoSummaryVO.typeCount}">${machineInfoSummaryVO.typeTotalCount}</td>
            </c:if>
            <td>${machineInfoSummaryVO.city}</td>
            <td>${machineInfoSummaryVO.switchGroupName}</td>
            <td>${machineInfoSummaryVO.count}</td>
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
        var td = $("#machineInfoSummaryTable tr:first td");
        var ctd = $("#headerTable tr:first td");
        $.each(td, function (idx, val) {
            ctd.eq(idx).width($(val).width());
        });
    }
    function trimSearchCondition() {
        var machineInfoSummary = $("#searchMachineInfoSummaryVOForm");
        var clientIDPrefix = machineInfoSummary.find("#clientIDPrefix").val();
        var city = machineInfoSummary.find("#city").val();
        var switchGroupName = machineInfoSummary.find("#switchGroupName").val();
        if(clientIDPrefix != "") {
            machineInfoSummary.find("#clientIDPrefix").val($.trim(clientIDPrefix));
        }
        if(city != "") {
            machineInfoSummary.find("#city").val($.trim(city));
        }
        if(switchGroupName != "") {
            machineInfoSummary.find("#switchGroupName").val($.trim(switchGroupName));
        }
    }
</script>
</body>
</html>

