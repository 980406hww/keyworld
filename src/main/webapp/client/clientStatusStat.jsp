<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html;charset=utf-8" %>
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
    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>

    <link rel="stylesheet" href="http://jqueryui.com/resources/demos/style.css">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.11.0/themes/smoothness/jquery-ui.css">
    <link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
    <link href="/css/menu.css" rel="stylesheet" type="text/css"/>

    <script language="javascript" type="text/javascript">


        $(function () {
            $("#centerDiv").css("margin-top",$("#topDiv").height());
            alignTableHeader();
            window.onresize = function(){
                alignTableHeader();
            }
        });
        function changePaging(currentPage, pageSize) {
            var searchClientStatusSummaryVOForm = $("#searchClientStatusSummaryVOForm");
            searchClientStatusSummaryVOForm.find("#currentPageNumberHidden").val(currentPage);
            searchClientStatusSummaryVOForm.find("#pageSizeHidden").val(pageSize);
            searchClientStatusSummaryVOForm.submit();
        }
        function resetPageNumber() {
            var searchClientStatusSummaryVOForm = $("#searchClientStatusSummaryVOForm");
            searchClientStatusSummaryVOForm.find("#currentPageNumberHidden").val(1);
        }
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

    <form action="/internal/clientstatus/clientStatusStat" method="post" id="searchClientStatusSummaryVOForm" style="margin: 20px 0;">
        <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
        <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
        <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
        <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
        客户端ID前缀:<input type="text" name="clientIDPrefix" value="${clientIDPrefix}">
        城市:<input type="text" name="city" value="${city}">
        <input type="submit" value="查询">
    </form>
    <table width=50% style="font-size: 12px;" cellpadding=3 id="headerTable">
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
<table width=50% style="font-size: 12px;" cellpadding=3 id="clientStatusSummaryTable">
    <c:forEach items="${page.records}" var="clientStatusSummaryVO">
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
    </select>
    </div>
</div>

<script language="javascript">

</script>
</body>
</html>

