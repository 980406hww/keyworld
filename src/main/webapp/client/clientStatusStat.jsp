<%@ include file="/commons/basejs.jsp" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>客户端统计</title>
    <style>
        .wrap {
            word-break: break-all;
            word-wrap: break-word;
        }

        #topDiv {
            position: fixed;
            top: 0px;
            left: 0px;
            background-color: white;
            width: 100%;
        }

        #showCustomerBottomPositioneDiv {
            position: fixed;
            bottom: 0px;
            right: 0px;
            background-color: white;
            padding-top: 10px;
            padding-bottom: 10px;
            width: 100%;
        }

        #showCustomerBottomDiv {
            float: right;
            margin-right: 20px;
        }

        body {
            margin: 0;
            padding: 0;
        }
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
            var td = $("#clientStatusSummaryTable tr:first td");
            var ctd = $("#headerTable tr:first td");
            $.each(td, function (idx, val) {
                ctd.eq(idx).width($(val).width());
            });
        }
    </script>
</head>
<body>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <form action="/internal/clientstatus/clientStatusStat" method="post" id="searchClientStatusSummaryVOForm" style="margin-top: 50px;margin-left: 20px">
        客户端ID前缀:<input type="text" name="clientIDPrefix" value="${clientIDPrefix}">
        城市:<input type="text" name="city" value="${city}">
        <shiro:hasPermission name="/internal/clientstatus/clientStatusStat">
            <input type="submit" value="查询">
        </shiro:hasPermission>
    </form>
    <table width=70% style="font-size: 12px;" cellpadding=3 id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=100>客户端ID前缀</td>
            <td align="center" width=100>总数</td>
            <td align="center" width=120>类型</td>
            <td align="center" width=120>小计</td>
            <td align="center" width=100>城市</td>
            <td align="center" width=50>数量</td>
            <div id="div1"></div>
            <div id="div2"></div>
        </tr>
    </table>
</div>
<div id="centerDiv">
<table width=70% style="font-size: 12px;" cellpadding=3 id="clientStatusSummaryTable">
    <c:forEach items="${clientStatusSummaryVOs}" var="clientStatusSummaryVO">
        <tr align="center">
            <c:if test="${clientStatusSummaryVO.clientIDPrefixCount>0}">
                <td rowspan="${clientStatusSummaryVO.clientIDPrefixCount}">${clientStatusSummaryVO.clientIDPrefix}</td>
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
</body>
</html>

