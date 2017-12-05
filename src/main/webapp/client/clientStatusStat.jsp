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
     客户端ID前缀:&nbsp;<input type="text" name="clientIDPrefix" value="${clientIDPrefix}">
        &nbsp;&nbsp;城市: <input type="text" name="city" value="${city}">&nbsp;&nbsp;
        <shiro:hasPermission name="/internal/clientstatus/clientStatusStat">
            <input type="submit" value=" 查询 ">
        </shiro:hasPermission>
    </form>
    <table width=60% style="font-size: 12px;" cellpadding=3 id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=100>客户端ID前缀</td>
            <td align="center" width=100>总数</td>
            <td align="center" width=120>类型</td>
            <td align="center" width=120>小计</td>
            <td align="center" width=100>城市</td>
            <td align="center" width=50>数量</td>
        </tr>
    </table>
</div>
<div id="centerDiv">
<table width=60% style="font-size: 12px" id="clientStatusSummaryTable" cellpadding=3>
    <c:forEach items="${clientStatusSummaryVOs}" var="clientStatusSummaryVO" varStatus="status">

        <tr align="left" <c:if test="${status.index%2==0}">bgcolor="#eeeeee" </c:if>>
            <c:if test="${clientStatusSummaryVO.clientIDPrefixCount>0}">
                <td style="padding-left: 7px;" rowspan="${clientStatusSummaryVO.clientIDPrefixCount}">${clientStatusSummaryVO.clientIDPrefix}</td>
                <td rowspan="${clientStatusSummaryVO.clientIDPrefixCount}">${clientStatusSummaryVO.clientIDPrefixTotalCount}</td>
            </c:if>
            <c:if test="${clientStatusSummaryVO.typeCount>0}">
                <td rowspan="${clientStatusSummaryVO.typeCount}">${clientStatusSummaryVO.type}</td>
                <td rowspan="${clientStatusSummaryVO.typeCount}">${clientStatusSummaryVO.typeTotalCount}</td>
            </c:if>
            <td>${clientStatusSummaryVO.city}</td>
            <td>${clientStatusSummaryVO.count}</td>
        </tr>
    </c:forEach>
</table>
</div>
<script language="javascript" type="text/javascript">
    document.write("<scr"+"ipt src=\"${staticPath }/static/toastmessage/jquery.toastmessage.js\"></sc"+"ript>");
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
</script>
</body>
</html>

