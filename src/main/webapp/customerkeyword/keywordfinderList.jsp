<%@ include file="/commons/basejs.jsp" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <script language="JavaScript" type="text/javascript" src="/js/jquery.poshytip.js"></script>
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
            .tip-yellow {
                z-index:1000;
                text-align:left;
                padding:7px;
                font-size: 12px;
                min-width:300px;
                max-width:550px;
                color:#8c3901;
                background-color: #fff;
            }
        </style>

        <script language="javascript">
            $(function () {
                $("#customerKeywordDiv").css("margin-top",$("#customerKeywordTopDiv").height());
                $(".floatTd").poshytip();
                initPaging();
                initNoPositionChecked();
                alignTableHeader();
                if(${isDepartmentManager}) {
                    $("#userName").val("${customerKeywordCriteria.userName}");
                }
                window.onresize = function(){
                    alignTableHeader();
                }
            });
            function initPaging() {
                var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
                var searchCustomerKeywordTable = searchCustomerKeywordForm.find("#searchCustomerKeywordTable");
                searchCustomerKeywordTable.find("#orderElement").val('${orderElement}');
                searchCustomerKeywordTable.find("#status").val(${customerKeywordCriteria.status});
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

            function selectAll(self) {
                var a = document.getElementsByName("uuid");
                if (self.checked) {
                    for (var i = 0; i < a.length; i++) {
                        a[i].checked = true;
                    }
                } else {
                    for (var i = 0; i < a.length; i++) {
                        a[i].checked = false;
                    }
                }
            }

            function decideSelectAll() {
                var a = document.getElementsByName("uuid");
                var select=0;
                for(var i = 0; i < a.length; i++){
                    if (a[i].checked == true){
                        select++;
                    }
                }
                if(select == a.length){
                    $("#selectAllChecked").prop("checked",true);
                }else {
                    $("#selectAllChecked").prop("checked",false);
                }
            }

            function initNoPositionChecked() {
                if(${customerKeywordCriteria.noPosition == 1}){
                    $("#noPosition").prop("checked",true);
                }else{
                    $("#noPosition").prop("checked",false);
                }
                if(${customerKeywordCriteria.displayStop == 1}){
                    $("#displayStop").prop("checked",true);
                }else{
                    $("#displayStop").prop("checked",false);
                }
                if(${customerKeywordCriteria.pushPay == 1}){
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

            function getSelectedIDs() {
                var uuids = '';
                $.each($("input[name=uuid]:checkbox:checked"), function () {
                    if (uuids === '') {
                        uuids = $(this).val();
                    } else {
                        uuids = uuids + "," + $(this).val();
                    }
                });
                return uuids;
            }

            function updateCustomerKeywordStatus(status) {
                var customerKeyword = {};
                var customerKeywordUuids = getSelectedIDs();
                if (customerKeywordUuids === '') {
                    alert('请选择要操作的关键字');
                    return;
                }
                if(status == 0) {
                    if (confirm("确认要暂停选中的关键字吗?") == false) return;
                } else {
                    if (confirm("确认要上线选中的关键字吗?") == false) return;
                }
                customerKeyword.uuids = customerKeywordUuids.split(",");
                customerKeyword.status = status;
                $.ajax({
                    url: '/internal/customerKeyword/updateCustomerKeywordStatus',
                    data: JSON.stringify(customerKeyword),
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    timeout: 5000,
                    type: 'POST',
                    success: function (status) {
                        if (status) {
                            $().toastmessage('showSuccessToast', "操作成功");
                            window.location.reload();
                        } else {
                            $().toastmessage('showErrorToast', "操作失败");
                        }
                    },
                    error: function () {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                });
            }
            
            function searchCustomerKeywords(url) {
                window.open(url);
            }

            function historyPositionAndIndex(url) {
                window.open(url);
            }
        </script>

    </head>
<body>
<div id="customerKeywordTopDiv">
    <%@include file="/menu.jsp" %>
    <form id="searchCustomerKeywordForm" style="font-size:12px; width: 100%;margin-top: 40px;" action="/internal/customerKeyword/searchCustomerKeywordLists" method="post" >
        <div id="searchCustomerKeywordTable">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            <input id="customerUuid" name="customerUuid" type="hidden" value="${customerKeywordCriteria.customerUuid}">
            关键字:&nbsp;<input type="text" name="keyword" id="keyword" value="${customerKeywordCriteria.keyword}" style="width:100px;">&nbsp;
            QQ:<input type="text" name="qq" id="qq" value="${customerKeywordCriteria.qq}" style="width:100px;">&nbsp;
            URL:<input type="text" name="url" id="url" value="${customerKeywordCriteria.url}" style="width:100px;">&nbsp;
            关键字状态:
            <select name="status" id="status">
                <option value=''>所有</option>
                <option value='1'>激活</option>
                <option value='2'>新增</option>
                <option value='0'>过期</option>
            </select>&nbsp;
            优化组名:
            <input type="text" name="optimizeGroupName" id="optimizeGroupName"
                   value="${customerKeywordCriteria.optimizeGroupName}" style="width:100px;">&nbsp;
            <c:if test="${isDepartmentManager}">
                用户名称:
                <select name="userName" id="userName">
                    <option value="">所有</option>
                    <option value="${user.loginName}">只显示自己</option>
                    <c:forEach items="${activeUsers}" var="activeUser">
                        <option value="${activeUser.loginName}">${activeUser.userName}</option>
                    </c:forEach>
                </select>
            </c:if>
            <input id="pushPay" name="pushPay" type="checkbox"  onclick="pushPayValue()" value="${customerKeywordCriteria.pushPay}"/>催缴 &nbsp;
            <input id="displayStop" name="displayStop" type="checkbox"  onclick="displayStopValue()" value="${customerKeywordCriteria.displayStop}"/>显示下架 &nbsp;
            <br/>
            已刷:<input type="text" name="optimizedCount" id="optimizedCount" value="${customerKeywordCriteria.optimizedCount}" style="width:40px;"/>
            显示前:
            <input type="text" name="position" id="position" value="${customerKeywordCriteria.position}" style="width:40px;"/>
            <input id="noPosition" name="noPosition" type="checkbox"  onclick="noPositionValue()"/>显示排名为0 &nbsp;
            订单号:
            <input type="text" name="orderNumber" id="orderNumber" value="${customerKeywordCriteria.orderNumber}" style="width:100px;">
            无效点击数:
            <input type="text" name="invalidRefreshCount" id="invalidRefreshCount" value="${customerKeywordCriteria.invalidRefreshCount}" style="width:40px;">
            创建日期:<input name="creationFromTime" id="creationFromTime" class="Wdate" type="text" style="width:90px;" onClick="WdatePicker()"
                        value="${customerKeywordCriteria.creationFromTime}">
            到&nbsp;<input name="creationToTime" id="creationToTime" class="Wdate" type="text" style="width:90px;"
                onClick="WdatePicker()" value="${customerKeywordCriteria.creationToTime}">&nbsp;
            排序:
            <select name="orderElement" id="orderElement">
                <option value="0">关键字</option>
                <option value="1">创建日期</option>
                <option value="2">当前排名</option>
                <option value="3">添加序号</option>
            </select>
            &nbsp;&nbsp;
            <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
                <input type="submit" onclick="resetPageNumber()" value=" 查询 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordStatus">
                <input type="button" onclick="updateCustomerKeywordStatus(0)" value=" 暂停选择关键字 ">&nbsp;&nbsp;
                <input type="button" onclick="updateCustomerKeywordStatus(1)" value=" 激活选中关键字 ">
            </shiro:hasPermission>
        </div>
    </form>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td width="10"><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
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

<div id="customerKeywordDiv" style="margin-bottom: 30px">
    <table id="customerKeywordTable" style="font-size:12px;">
        <c:forEach items="${page.records}" var="customerKeyword">
            <tr style="" height=30 onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width="10">
                    <input type="checkbox" name="uuid" value="${customerKeyword.uuid}" onclick="decideSelectAll()"/>
                </td>
                <td align="center" width=80>
                    <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywords">
                    <a href="#" onclick="searchCustomerKeywords('/internal/customerKeyword/searchCustomerKeywords/${customerKeyword.customerUuid}')">${customerKeyword.contactPerson}</a>
                    </shiro:hasPermission>
                </td>
                <td align="center" width=80>
                    ${customerKeyword.keyword}
                </td>

                <td  align="center" width=100 class="floatTd" title="原始URL:${customerKeyword.originalUrl != null ?customerKeyword.originalUrl : customerKeyword.url}" >
                    <div style="height:16;">
                        ${customerKeyword.url==null?'':customerKeyword.url};
                    </div>
                </td>

                <td align="center" width=150>
                    ${customerKeyword.title == null ? "" : customerKeyword.title.trim()}
                </td>
                <td align="center" width=30>
                    <div style="height:16;">
                        <shiro:hasPermission name="/internal/customerKeywordPositionIndexLog/historyPositionAndIndex">
                        <a href="#" onclick="historyPositionAndIndex('/internal/customerKeywordPositionIndexLog/historyPositionAndIndex/${customerKeyword.uuid}/30')"
                             title="查看历史排名" class="floatTd">${customerKeyword.currentIndexCount}</a>
                        </shiro:hasPermission>
                    </div>
                </td>
                <td align="center" width=50>
                    <div style="height:16;">${customerKeyword.initialPosition}
                    </div>
                </td>
                <td align="center" width=50>
                    <div style="height:16;"><a
                            href="${customerKeyword.searchEngineUrl}${customerKeyword.keyword}&pn=${customerKeyword.getPrepareBaiduPageNumber(customerKeyword.currentPosition)}"
                            target="_blank" >${customerKeyword.currentPosition}</a>
                    </div>
                </td>

                <td align="center" class="floatTd" width=30 title="优化日期：<fmt:formatDate value="${customerKeyword.optimizeDate}" pattern="yyyy-MM-dd"/> ，要刷：${customerKeyword.optimizePlanCount}，已刷：${customerKeyword.optimizedCount}" >
                        ${customerKeyword.collectMethodName}
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
