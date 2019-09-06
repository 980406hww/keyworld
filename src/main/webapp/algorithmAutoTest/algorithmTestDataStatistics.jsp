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
    <title>算法测试任务</title>
</head>
<body>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <div style="margin-top: 5px;margin-bottom: 5px">
        <form method="post" id="searchAlgorithmTestDataStatisticsForm" action="/internal/algorithmAutoTest/showTestDataStatistics" style="margin-bottom:0px ">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}" />
            <input type="hidden" name="algorithmTestPlanUuid" id="algorithmTestPlanUuid" value="${algorithmTestPlanUuid}" />
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
        </form>
    </div>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td align="center" width=10><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=70>首页个数</td>
            <td align="center" width=70>没刷量个数</td>
            <td align="center" width=70>排名首页率</td>
            <td align="center" width=70>记录日期</td>
            <td align="center" width=70>创建时间</td>
            <td align="center" width=70>修改时间</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>
<div id="centerDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;"id="showAlgorithmTestDataStatisticsTable" >
        <c:forEach items="${page.records}" var="algorithmTestDataStatistics" varStatus="status">
            <tr align="center" onmouseover="doOver(this);" onmouseout="doOut(this);" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
                <td width=10 align="center"><input type="checkbox" name="uuid" value="${algorithmTestDataStatistics.uuid}" onclick="decideSelectAll()"/></td>
                <td width=70>
                        ${algorithmTestDataStatistics.topTenCount}
                </td>
                <td width=70>
                        ${algorithmTestDataStatistics.zeroOptimizedCount}
                </td>
                <td width=70>
                        ${algorithmTestDataStatistics.rankChangeRate}
                </td>
                <td width=70>
                    <fmt:formatDate value="${algorithmTestDataStatistics.recordDate}" pattern="yyyy-MM-dd"/>
                </td>
                <td width=70><fmt:formatDate value="${algorithmTestDataStatistics.createTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td width=70><fmt:formatDate value="${algorithmTestDataStatistics.updateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
                <td width=80>
                    暂无操作
                </td>
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
        <option>500</option>
        <option>1000</option>
    </select>
    </div>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script type="text/javascript" src="${staticPath}/static/select2/select2-4.0.6.min.js"></script>
<script src="${staticPath}/algorithmAutoTest/algorithmTestDataStatistics.js"></script>
</body>
</html>