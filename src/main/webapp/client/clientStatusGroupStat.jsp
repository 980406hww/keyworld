<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
        #ClientStatusGroupSummaryTable tr:nth-child(even){background-color: #eeeeee}
        #ClientStatusGroupSummaryTable tr:hover{background-color: #dad55e}
    </style>

    <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
    <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
    <script language="javascript" type="text/javascript" src="/js/slide1.12.4.js"></script>
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

    <form action="/internal/clientstatus/clientStatusGroupStat" method="post" id="searchClientStatusSummaryVOForm" style="margin: 20px 0;">
        分组名称:<input type="text" name="group" value="${group}">
        终端类型:
        <select name="terminalType">
            <option value="">请选择终端类型</option>
            <option value="PC" <c:if test="${requestScope.terminalType.equals('PC')}">selected="selected"</c:if>>PC</option>
            <option value="Phone" <c:if test="${requestScope.terminalType.equals('Phone')}">selected="selected"</c:if>>Phone</option>
        </select>
        <input type="submit" value="查询">
    </form>
    <table width=70% style="font-size: 12px;" cellpadding=3 id="headerTable">
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
    <table width=70% style="font-size: 12px;" cellpadding=3 id="ClientStatusGroupSummaryTable">
    <c:forEach items="${clientStatusGroupSummaryVOs}" var="ClientStatusGroupSummaryVO" varStatus="status">
        <tr align="center">
        <td>${ClientStatusGroupSummaryVO.group}</td>
        <td>${ClientStatusGroupSummaryVO.terminalType}</td>
        <td>${ClientStatusGroupSummaryVO.count}</td>
        </tr>
    </c:forEach>
</table>
</div>

</body>
</html>

