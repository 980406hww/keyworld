<%@ include file="/commons/basejs.jsp" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>客户端统计</title>
    <style>
        #topDiv {
            position: fixed;
            top: 0px;
            left: 0px;
            background-color: white;
            width: 100%;
        }
        input[type="submit"]{
            padding: 2px;
            border-radius: 5px;
            border: 1px solid #bbb;
            background-color: #F0F0EE;
        }
        #ClientStatusGroupSummaryTable tr:nth-child(even){background-color: #eeeeee}
        #ClientStatusGroupSummaryTable tr:hover{background-color: green;}
    </style>

    <script language="javascript" type="text/javascript">

        $(function () {
            $("#centerDiv").css("margin-top",$("#topDiv").height());
            alignTableHeader();
            window.onresize = function(){
                alignTableHeader();
            }
        });
        function alignTableHeader() {
            var td = $("#ClientStatusGroupSummaryTable tr:first td");
            var ctd = $("#headerTable tr:first td");
            $.each(td, function (idx, val) {
                ctd.eq(idx).width($(val).width());
            });
        }
    </script>
</head>
<body>
<div id="topDiv">
    <%@include file="/menu.jsp"%>
    <form action="/internal/clientstatus/clientStatusGroupStat" method="post" id="searchClientStatusSummaryVOForm" style="margin: 35px 0 5px 10px;">
        分组名称:<input type="text" name="group" value="${group}">&nbsp;&nbsp;
        终端类型:
        <select name="terminalType">
            <option value="">请选择终端类型</option>
            <option value="PC" <c:if test="${requestScope.terminalType.equals('PC')}">selected="selected"</c:if>>PC</option>
            <option value="Phone" <c:if test="${requestScope.terminalType.equals('Phone')}">selected="selected"</c:if>>Phone</option>
        </select>&nbsp;&nbsp;
        <shiro:hasPermission name="/internal/clientstatus/clientStatusGroupStat">
            <input type="submit" value=" 查询 ">
        </shiro:hasPermission>
    </form>
    <table width=40% style="font-size: 12px;" cellpadding=3 id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=100>分组名称</td>
            <td align="center" width=100>终端类型</td>
            <td align="center" width=50>数量</td>
            <div id="div1"></div>
            <div id="div2"></div>
        </tr>
    </table>
</div>
<div id="centerDiv">
    <table width=40% style="font-size: 12px;" cellpadding=3 id="ClientStatusGroupSummaryTable">
    <c:forEach items="${clientStatusGroupSummaryVOs}" var="ClientStatusGroupSummaryVO" varStatus="status">
        <tr align="left" height="30px">
        <td style="padding-left: 7px;">${ClientStatusGroupSummaryVO.group}</td>
        <td>${ClientStatusGroupSummaryVO.terminalType}</td>
        <td>${ClientStatusGroupSummaryVO.count}</td>
        </tr>
    </c:forEach>
</table>
</div>
</body>
</html>

