<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>终端统计</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <form action="/internal/clientstatus/clientStatusStat" method="post" id="searchClientStatusSummaryVOForm" style="margin-top: 35px;margin-left: 20px">
     客户端ID前缀:&nbsp;<input type="text" name="clientIDPrefix" id="clientIDPrefix" value="${clientIDPrefix}">
        &nbsp;&nbsp;城市: <input type="text" name="city" id="city" value="${city}">&nbsp;&nbsp;
        流转分组: <input type="text" name="switchGroupName" id="switchGroupName" value="${switchGroupName}">&nbsp;&nbsp;
        <shiro:hasPermission name="/internal/clientstatus/clientStatusStat">
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
<table width=60% style="font-size: 12px" id="clientStatusSummaryTable" cellpadding=3>
    <c:set var="color" scope="session" value="#eeeeff"/>
    <c:forEach items="${clientStatusSummaryVOs}" var="clientStatusSummaryVO" varStatus="status">
            <c:if test="${clientStatusSummaryVO.clientIDPrefixCount>0}">
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
            <c:if test="${clientStatusSummaryVO.clientIDPrefixCount>0}">
                <td rowspan="${clientStatusSummaryVO.clientIDPrefixCount}">${clientStatusSummaryVO.clientIDPrefix}</td>
                <td rowspan="${clientStatusSummaryVO.clientIDPrefixCount}">${clientStatusSummaryVO.clientIDPrefixTotalCount}</td>
            </c:if>
            <c:if test="${clientStatusSummaryVO.typeCount>0}">
                <td rowspan="${clientStatusSummaryVO.typeCount}">${clientStatusSummaryVO.type}</td>
                <td rowspan="${clientStatusSummaryVO.typeCount}">${clientStatusSummaryVO.typeTotalCount}</td>
            </c:if>
            <td>${clientStatusSummaryVO.city}</td>
            <td>${clientStatusSummaryVO.switchGroupName}</td>
            <td>${clientStatusSummaryVO.count}</td>
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
        var td = $("#clientStatusSummaryTable tr:first td");
        var ctd = $("#headerTable tr:first td");
        $.each(td, function (idx, val) {
            ctd.eq(idx).width($(val).width());
        });
    }
    function trimSearchCondition() {
        var clientStatusSummaryForm = $("#searchClientStatusSummaryVOForm");
        var clientIDPrefix = clientStatusSummaryForm.find("#clientIDPrefix").val();
        var city = clientStatusSummaryForm.find("#city").val();
        var switchGroupName = clientStatusSummaryForm.find("#switchGroupName").val();
        if(clientIDPrefix != "") {
            clientStatusSummaryForm.find("#clientIDPrefix").val($.trim(clientIDPrefix));
        }
        if(city != "") {
            clientStatusSummaryForm.find("#city").val($.trim(city));
        }
        if(switchGroupName != "") {
            clientStatusSummaryForm.find("#switchGroupName").val($.trim(switchGroupName));
        }
    }
</script>
</body>
</html>

