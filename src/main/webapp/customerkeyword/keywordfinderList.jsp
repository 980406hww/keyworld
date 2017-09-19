<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <link href="/css/menu.css" rel="stylesheet" type="text/css" />
    <script language="javascript" type="text/javascript" src="/js/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript" src="/js/jquery142.js"></script>
    <script language="javascript" type="text/javascript" src="/js/slide1.12.4.js"></script>
    <head>
        <style>
            #div1 {
                display: none;
                background-color: #f6f7f7;
                color: #333333;
                font-size: 12px;
                line-height: 18px;
                border: 1px solid #e1e3e2;
                width: 450px;
                height: 50px;
            }

            #customerKeywordTopDiv {
                position: fixed;
                top: 0px;
                left: 0px;
                background-color: white;
                width: 100%;
            }

            #customerKeywordDiv {
                /*overflow: scroll;*/
                width: 100%;
                margin-bottom: 47PX;
            }

            #customerKeywordTable {
                width: 100%;
            }
            #customerKeywordTable tr:nth-child(odd){background:#EEEEEE;}

            #customerKeywordTable td{
                text-align: left;
            }

            #showCustomerBottomPositioneDiv{
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

            body{
                margin: 0;
                padding: 0;
            }
        </style>

        <script language="javascript">
            $(function () {
                $("#customerKeywordDiv").css("margin-top",$("#customerKeywordTopDiv").height());
                initPaging();
                initNoPositionChecked();
                alignTableHeader();
                $("#userName").val("${customerKeywordCrilteria.userName}");
                window.onresize = function(){
                    alignTableHeader();
                }
            });
            function initPaging() {
                var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
                var searchCustomerKeywordTable = searchCustomerKeywordForm.find("#searchCustomerKeywordTable");
                searchCustomerKeywordTable.find("#orderElement").val('${orderElement}');
                searchCustomerKeywordTable.find("#status").val(${customerKeywordCrilteria.status});
                var pages = searchCustomerKeywordForm.find('#pagesHidden').val();
                var currentPageNumber = searchCustomerKeywordForm.find('#currentPageNumberHidden').val();
                var showCustomerBottomDiv = $('#showCustomerBottomDiv');
                showCustomerBottomDiv.find("#chooseRecords").val(${page.size});
                if(parseInt(currentPageNumber) > 1 && parseInt(currentPageNumber) < parseInt(pages)) {
                    showCustomerBottomDiv.find("#firstButton").removeAttr("disabled");
                    showCustomerBottomDiv.find("#upButton").removeAttr("disabled");
                    showCustomerBottomDiv.find("#nextButton").removeAttr("disabled");
                    showCustomerBottomDiv.find("#lastButton").removeAttr("disabled");
                } else if (parseInt(pages) <= 1) {
                    showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                    showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
                    showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
                    showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
                } else if (parseInt(currentPageNumber) <= 1) {
                    showCustomerBottomDiv.find("#fisrtButton").attr("disabled", "disabled");
                    showCustomerBottomDiv.find("#upButton").attr("disabled", "disabled");
                } else {
                    showCustomerBottomDiv.find("#nextButton").attr("disabled", "disabled");
                    showCustomerBottomDiv.find("#lastButton").attr("disabled", "disabled");
                }

            }

            function doOver(obj) {
                obj.style.backgroundColor = "#73B1E0";
            }

            function doOut(obj) {
                var rowIndex = obj.rowIndex;
                if ((rowIndex % 2) == 0) {
                    obj.style.backgroundColor = "#eeeeee";
                } else {
                    obj.style.backgroundColor = "#ffffff";
                }
            }

            function showTip(content, e) {
                var event = e || window.event;
                var pageX = event.pageX;
                var pageY = event.pageY;
                if (pageX == undefined) {
                    pageX = event.clientX + document.body.scrollLeft || document.documentElement.scrollLeft;
                }
                if (pageY == undefined) {
                    pageY = event.clientY + document.body.scrollTop || document.documentElement.scrollTop;
                }
                var div1 = document.getElementById('div1'); //将要弹出的层
                div1.innerText = content;
                div1.style.display = "block"; //div1初始状态是不可见的，设置可为可见
                div1.style.left = pageX + 10; //鼠标目前在X轴上的位置，加10是为了向右边移动10个px方便看到内容
                div1.style.top = pageY + 5;
                div1.style.position = "absolute";
            }

            //关闭层div1的显示
            function closeTip() {
                var div1 = document.getElementById('div1');
                div1.style.display = "none";
            }

            //重构部分
            //查询
            function changePaging(currentPage, pageSize) {
                var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
                searchCustomerKeywordForm.find("#currentPageNumberHidden").val(currentPage);
                searchCustomerKeywordForm.find("#pageSizeHidden").val(pageSize);
                searchCustomerKeywordForm.submit();
            }

            function resetPageNumber() {
                var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
                searchCustomerKeywordForm.find("#currentPageNumberHidden").val(1);
            }

            //显示排名为0
            function noPositionValue() {
                if($("#noPosition").is(":checked")){
                    $("#noPosition").val("1")
                }else {
                    $("#noPosition").val("0");
                }
            }
            //催缴
            function pushPayValue() {
                if($("#pushPay").is(":checked")){
                    $("#pushPay").val("1")
                }else {
                    $("#pushPay").val("0");
                }
            }
            //显示下架
            function displayStopValue() {
                if($("#displayStop").is(":checked")){
                    $("#displayStop").val("1")
                }else {
                    $("#displayStop").val("0");
                }
            }

            function initNoPositionChecked() {
                if(${customerKeywordCrilteria.noPosition == 1}){
                    $("#noPosition").prop("checked",true);
                }else{
                    $("#noPosition").prop("checked",false);
                }
                if(${customerKeywordCrilteria.displayStop == 1}){
                    $("#displayStop").prop("checked",true);
                }else{
                    $("#displayStop").prop("checked",false);
                }
                if(${customerKeywordCrilteria.pushPay == 1}){
                    $("#pushPay").prop("checked",true);
                }else{
                    $("#pushPay").prop("checked",false);
                }
                noPositionValue();
                displayStopValue();
                pushPayValue();
            }

            function alignTableHeader() {
                var td = $("#customerKeywordTable tr:first td");
                var ctd = $("#headerTable tr:first td");
                $.each(td, function (idx, val) {
                    ctd.eq(idx).width($(val).width());
                });
            }
        </script>

    </head>
<body>
<div id="customerKeywordTopDiv">
    <table style="font-size:12px;margin-right: 10px;" cellpadding=3>
        <tr>
            <td colspan=18 align="left">
                <%@include file="/menu.jsp" %>
            </td>
        </tr>
    </table>

    <br/>
    <form id="searchCustomerKeywordForm" style="font-size:12px; width: 100%;" action="/internal/customerKeyword/searchCustomerKeywordLists" method="post">
        <div id="searchCustomerKeywordTable">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            <input id="customerUuid" name="customerUuid" type="hidden" value="${customerKeywordCrilteria.customerUuid}">
            关键字:&nbsp;<input type="text" name="keyword" id="keyword" value="${customerKeywordCrilteria.keyword}" style="width:100px;">&nbsp;
            QQ:<input type="text" name="qq" id="qq" value="${customerKeywordCrilteria.qq}" style="width:100px;">&nbsp;
            URL:<input type="text" name="url" id="url" value="${customerKeywordCrilteria.url}" style="width:100px;">&nbsp;
            关键字状态:
            <select name="status" id="status">
                <option value=''>所有</option>
                <option value='1'>激活</option>
                <option value='2'>新增</option>
                <option value='0'>过期</option>
            </select>&nbsp;
            优化组名:
            <input type="text" name="optimizeGroupName" id="optimizeGroupName"
                   value="${customerKeywordCrilteria.optimizeGroupName}" style="width:100px;">&nbsp;
            用户名称:
                <select name="userName" id="userName">
                    <option value="">所有</option>
                    <option value="${user.userID}">只显示自己</option>
                    <c:forEach items="${activeUsers}" var="activeUser">
                        <option value="${activeUser.userID}">${activeUser.userName}</option>
                    </c:forEach>
                </select>
            <br>
            显示前:
            <input type="text" name="position" id="position" value="${customerKeywordCrilteria.position}" style="width:40px;"/>
            <input id="noPosition" name="noPosition" type="checkbox"  onclick="noPositionValue()"/>显示排名为0 &nbsp;
            <input id="pushPay" name="pushPay" type="checkbox"  onclick="pushPayValue()" value="${customerKeywordCrilteria.pushPay}"/>催缴 &nbsp;
            <input id="displayStop" name="displayStop" type="checkbox"  onclick="displayStopValue()" value="${customerKeywordCrilteria.displayStop}"/>显示下架 &nbsp;
            订单号:
            <input type="text" name="orderNumber" id="orderNumber" value="${customerKeywordCrilteria.orderNumber}" style="width:100px;">
            无效点击数:
            <input type="text" name="invalidRefreshCount" id="invalidRefreshCount" value="${customerKeywordCrilteria.invalidRefreshCount}" style="width:40px;">
            创建日期:<input name="creationFromTime" id="creationFromTime" class="Wdate" type="text" style="width:90px;" onClick="WdatePicker()"
                        value="${customerKeywordCrilteria.creationFromTime}">
            到&nbsp;<input name="creationToTime" id="creationToTime" class="Wdate" type="text" style="width:90px;"
                onClick="WdatePicker()" value="${customerKeywordCrilteria.creationToTime}">&nbsp;
            排序:
            <select name="orderElement" id="orderElement">
                <option value="0">关键字</option>
                <option value="1">创建日期</option>
                <option value="2">当前排名</option>
                <option value="3">添加序号</option>
            </select>
            &nbsp;&nbsp;&nbsp;
            <input type="submit" onclick="resetPageNumber()" value=" 查询 ">&nbsp;&nbsp;
        </div>
    </form>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td align="center" width=80>联系人</td>
            <td align="center" width=80>关键字</td>
            <td align="center" width=100>URL</td>
            <td align="center" width=150>标题</td>
            <td align="center" width=30>指数</td>
            <td align="center" width=50>原排名</td>
            <td align="center" width=50>现排名</td>
            <td align="center" width=30>计价方式</td>
            <td align="center" width=30>要刷</td>
            <td align="center" width=30>已刷</td>
            <td align="center" width=30>无效</td>
            <td align="center" width=60>报价</td>
            <td align="center" width=100>订单号</td>
            <td align="center" width=60>付费状态</td>
            <td align="center" width=100>备注</td>
            <div id="div1"></div>
        </tr>
    </table>
</div>

<div id="customerKeywordDiv">
    <table id="customerKeywordTable" style="font-size:12px;">
        <c:forEach items="${page.records}" var="customerKeyword">
            <tr style="" height=30 onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td align="center" width=80>
                    <a href="/internal/customerKeyword/searchCustomerKeywords/${customerKeyword.customerUuid}" target="_blank">${customerKeyword.contactPerson}</a></td>
                </td>
                <td align="center" width=80>
                        ${customerKeyword.keyword}
                </td>
                <td  align="center" width=100 class="wrap"
                     onMouseMove="showTip('原始URL:${customerKeyword.originalUrl != null ?customerKeyword.originalUrl : customerKeyword.url}')"
                     onMouseOut="closeTip()">
                    <div style="height:16;">
                            ${customerKeyword.url==null?'':customerKeyword.url};
                    </div>
                </td>

                <td align="center" width=150>
                        ${customerKeyword.title == null ? "" : customerKeyword.title.trim()}
                </td>
                <td align="center" width=30>
                    <div style="height:16;"><a
                            href="/internal/customerKeywordPositionIndexLog/historyPositionAndIndex/${customerKeyword.uuid}/30"
                            target="_blank">${customerKeyword.currentIndexCount}
                    </a></div>
                </td>
                <td align="center" width=50>
                    <div style="height:16;">${customerKeyword.initialPosition}
                    </div>
                </td>
                <td align="center" width=50>
                    <div style="height:16;"><a
                            href="${customerKeyword.searchEngineUrl}${customerKeyword.keyword}&pn=${customerKeyword.getPrepareBaiduPageNumber(customerKeyword.currentPosition)}"
                            target="_blank">${customerKeyword.currentPosition}</a>
                    </div>
                </td>
                <td align="center" width=30 onMouseMove="showTip('优化日期：<fmt:formatDate value="${customerKeyword.optimizeDate}" pattern="yyyy-MM-dd"/> ，要刷：${customerKeyword.optimizePlanCount}，已刷：${customerKeyword.optimizedCount}')"
                    onMouseOut="closeTip()">${customerKeyword.collectMethodName}
                </td>
                <td align="center" width=30>${customerKeyword.optimizePlanCount}</td>
                <td align="center" width=30>${customerKeyword.optimizedCount} </td>
                <td align="center" width=30>${customerKeyword.invalidRefreshCount}</td>
                <td align="center" width=60>${customerKeyword.feeString}</td>
                <td align="center" width=100>${customerKeyword.orderNumber}</td>
                <td align="center" width="60">${customerKeyword.paymentStatus}</td>
                <td align="center" width=100>${customerKeyword.remarks==null?"":customerKeyword.remarks} </td>
            </tr>
        </c:forEach>
    </table>
</div>

<div id="showCustomerBottomPositioneDiv">
    <div id="showCustomerBottomDiv">
        <input id="fisrtButton" type="button" onclick="changePaging(1,'${page.size}')" value="首页"/>&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="upButton" type="button" onclick="changePaging('${page.current-1}','${page.size}')" value="上一页"/>&nbsp;&nbsp;&nbsp;&nbsp;
                ${page.current}/${page.pages}&nbsp;&nbsp;
        <input id="nextButton" type="button" onclick="changePaging('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')"
               value="下一页">&nbsp;&nbsp;&nbsp;&nbsp;
        <input id="lastButton" type="button" onclick="changePaging('${page.pages}','${page.size}')" value="末页">&nbsp;&nbsp;&nbsp;&nbsp;
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

</body>
</html>