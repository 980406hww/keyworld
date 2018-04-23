<%@ include file="/commons/global.jsp" %>
<html>
    <head>
        <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
        <%@ include file="/commons/basejs.jsp" %>
        <style>
            td{
                display: table-cell;
                vertical-align: inherit;
            }
            #showCustomerTableDiv {
                width: 100%;
                margin: auto;
            }
            #customerKeywordTopDiv {
                position: fixed;
                top: 0px;
                left: 0px;
                background-color: white;
                width: 100%;
            }
            #customerKeywordTable {
                width: 100%;
            }
            #customerKeywordTable tr:nth-child(odd){background:#EEEEEE;}

            #customerKeywordTable td{
                text-align: left;
            }
        </style>
        <title>关键字统计</title>
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
            <input type="hidden" id="sevenDaysNoReachStandard" name="sevenDaysNoReachStandard" value="${customerKeywordCriteria.sevenDaysNoReachStandard}"/>
            <input type="hidden" id="fifteenDaysNoReachStandard" name="fifteenDaysNoReachStandard" value="${customerKeywordCriteria.fifteenDaysNoReachStandard}"/>
            <input type="hidden" id="thirtyDaysNoReachStandard" name="thirtyDaysNoReachStandard" value="${customerKeywordCriteria.thirtyDaysNoReachStandard}"/>
            <input id="customerUuid" name="customerUuid" type="hidden" value="${customerKeywordCriteria.customerUuid}">
            关键字:&nbsp;<input type="text" name="keyword" id="keyword" value="${customerKeywordCriteria.keyword}" style="width:100px;">&nbsp;
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
            排序:
            <select name="orderingElement" id="orderingElement">
                <option value="0">关键字</option>
                <option value="1">创建日期</option>
                <option value="2">当前排名</option>
                <option value="3">添加序号</option>
            </select>
            &nbsp;
            备注:<input type="text" id="remarks" name="remarks" style="width: 90px;" value="${customerKeywordCriteria.remarks}">&nbsp;
            <input id="pushPay" name="pushPay" type="checkbox"  onclick="pushPayValue()" value="${customerKeywordCriteria.pushPay}"/>催缴 &nbsp;
            <input id="displayStop" name="displayStop" type="checkbox"  onclick="displayStopValue()" value="${customerKeywordCriteria.displayStop}"/>显示下架 &nbsp;
            <input id="noPosition" name="noPosition" type="checkbox"  onclick="noPositionValue()"/>显示排名为0 &nbsp;
            <br/>
            要刷:<input type="text" name="gtOptimizePlanCount" id="gtOptimizePlanCount" placeholder=">=" value="${customerKeywordCriteria.gtOptimizePlanCount}" style="width:40px;"/>
            <input type="text" name="ltOptimizePlanCount" id="ltOptimizePlanCount" placeholder="<="  value="${customerKeywordCriteria.ltOptimizePlanCount}" style="width:40px;"/>
            已刷:<input type="text" name="gtOptimizedCount" id="gtOptimizedCount" placeholder=">=" value="${customerKeywordCriteria.gtOptimizedCount}" style="width:40px;"/>
            <input type="text" name="ltOptimizedCount" id="ltOptimizedCount" placeholder="<="  value="${customerKeywordCriteria.ltOptimizedCount}" style="width:40px;"/>
            排名:
            <input type="text" name="gtPosition" id="gtPosition" placeholder=">=" value="${customerKeywordCriteria.gtPosition}" style="width:40px;"/>
            <input type="text" name="ltPosition" id="ltPosition" placeholder="<=" value="${customerKeywordCriteria.ltPosition}" style="width:40px;"/>
            订单号:
            <input type="text" name="orderNumber" id="orderNumber" value="${customerKeywordCriteria.orderNumber}" style="width:100px;">
            无效点击数:
            <input type="text" name="invalidRefreshCount" id="invalidRefreshCount" value="${customerKeywordCriteria.invalidRefreshCount}" style="width:40px;">
            <c:if test="${customerKeywordCriteria.entryType eq 'pt' or customerKeywordCriteria.entryType eq 'bc'}">
            未达标天数:
            <input type="text" name="noReachStandardDays" id="noReachStandardDays" value="${customerKeywordCriteria.noReachStandardDays}" style="width:40px;">
            </c:if>
            搜索引擎:
            <select name="searchEngine" id="searchEngine">
                <option value="">全部</option>
                <option value="百度">百度</option>
                <option value="360">360</option>
                <option value="搜狗">搜狗</option>
                <option value="神马">神马</option>
            </select>
            创建日期:<input name="creationFromTime" id="creationFromTime" class="Wdate" type="text" style="width:90px;" onClick="WdatePicker()"
            value="${customerKeywordCriteria.creationFromTime}">
            到&nbsp;<input name="creationToTime" id="creationToTime" class="Wdate" type="text" style="width:90px;"
            onClick="WdatePicker()" value="${customerKeywordCriteria.creationToTime}">&nbsp;
            <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
                <input type="submit" onclick="resetPageNumber(0)" value=" 查询 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordStatus">
                <input type="button" onclick="updateCustomerKeywordStatus(0)" value=" 暂停关键字 ">&nbsp;&nbsp;
                <input type="button" onclick="updateCustomerKeywordStatus(1)" value=" 激活关键字 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordGroupName">
                <input type="button" onclick="updateOptimizeGroupName(${page.total})" value=" 修改当前关键字优化组 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/deleteCustomerKeywords">
            <input type="button" onclick="deleteCustomerKeywords()" value=" 删除所选 ">
            </shiro:hasPermission>
            <br/>
            <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
                <c:if test="${customerKeywordCriteria.entryType eq 'pt' or customerKeywordCriteria.entryType eq 'bc'}">
                <div id="noReachStandardDiv" align="right">
                    未达标统计:
                    <a target="_blank" href="javascript:resetPageNumber(30)">超过30天(${customerKeywordCriteria.thirtyDaysNoReachStandard})</a>
                    | <a target="_blank" href="javascript:resetPageNumber(15)">超过15天(${customerKeywordCriteria.fifteenDaysNoReachStandard})</a>
                    | <a target="_blank" href="javascript:resetPageNumber(7)">超过7天(${customerKeywordCriteria.sevenDaysNoReachStandard})</a>
                </div>
                </c:if>
            </shiro:hasPermission>
        </div>
    </form>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td width="10" style="padding-left: 7px;"><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=80>联系人</td>
            <td align="center" width=80>关键字</td>
            <td align="center" width=100>URL</td>
            <td align="center" width=150>标题</td>
            <td align="center" width=30>指数</td>
            <td align="center" width=50>原排名</td>
            <td align="center" width=50>现排名</td>
            <td align="center" width=50>搜索引擎</td>
            <td align="center" width=30>计价方式</td>
            <td align="center" width=30>要刷</td>
            <td align="center" width=30>已刷</td>
            <td align="center" width=30>无效</td>
            <td align="center" width=60>报价</td>
            <td align="center" width=100>订单号</td>
            <td align="center" width=60>付费状态</td>
            <td align="center" width=100>备注</td>
        </tr>
    </table>
</div>

<div id="showCustomerTableDiv" style="margin-bottom: 30px">
    <table id="customerKeywordTable" style="font-size:12px;">
        <c:forEach items="${page.records}" var="customerKeyword">
            <tr height=30 onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width="10" style="padding-left: 7px;">
                    <input type="checkbox" name="uuid" value="${customerKeyword.uuid}" onclick="decideSelectAll()"/>
                </td>
                <td align="center" width=80>
                    <a href="#" onclick="searchCustomerKeywords('/internal/customerKeyword/searchCustomerKeywords/${customerKeyword.customerUuid}')">${customerKeyword.contactPerson}</a>
                </td>
                <td align="center" width=80>
                    ${customerKeyword.keyword}
                </td>

                <td align="center" width=100 class="floatTd" title="原始URL:${customerKeyword.originalUrl != null ?customerKeyword.originalUrl : customerKeyword.url}" >
                        ${customerKeyword.url==null?'':customerKeyword.url}
                </td>

                <td align="center" width=150>
                    ${customerKeyword.title == null ? "" : customerKeyword.title.trim()}
                </td>
                <td align="center" width=30>
                    <div style="height:16;">
                        <a href="/internal/customerKeywordPositionIndexLog/historyPositionAndIndex/${customerKeyword.uuid}/30"
                             target="_blank" title="查看历史排名" class="floatTd">${customerKeyword.currentIndexCount}</a>
                    </div>
                </td>
                <td align="center" width=50>
                    <div style="height:16;">${customerKeyword.initialPosition}
                    </div>
                </td>
                <td align="center" width=50>
                    <div style="height:16;">
                        <a href="${customerKeyword.searchEngineUrl}" target="_blank">${customerKeyword.currentPosition}</a>
                    </div>
                </td>
                <td align="center" width=50>${customerKeyword.searchEngine}</td>
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
<div id="targetGroupNameDialog"  style="text-align: center;left: 40%;display: none;">
    <form id="targetGroupNameFrom" style="text-align: center;margin-top: 10px;">
        目标优化组名:<input type="text" id="groupName" name="groupName" style="width:150px">
    </form>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/customerkeyword/keywordfinderList.js"></script>
<script language="javascript">
    $(function () {
        $("#showCustomerTableDiv").css("margin-top",$("#customerKeywordTopDiv").height());
        initPaging();
        initNoPositionChecked();
        alignTableHeader();
        if(${isDepartmentManager}) {
            $("#userName").val("${customerKeywordCriteria.userName}");
        }
        window.onresize = function(){
            alignTableHeader();
        }
        if(${customerKeywordCriteria.entryType eq 'bc' or customerKeywordCriteria.entryType eq 'pt'}) {
            if(${customerKeywordCriteria.sevenDaysNoReachStandard == null}) {
                searchCustomerKeywordForNoReachStandard('${customerKeywordCriteria.entryType}','${customerKeywordCriteria.terminalType}');
            }
        }
    });
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

    //显示排名为0
    function noPositionValue() {
        if($("#noPosition").is(":checked")){
            $("#noPosition").val("1")
        }else {
            $("#noPosition").val("0");
        }
    }

    function initPaging() {
        var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
        var searchCustomerKeywordTable = searchCustomerKeywordForm.find("#searchCustomerKeywordTable");
        searchCustomerKeywordTable.find("#searchEngine").val('${customerKeywordCriteria.searchEngine}');
        searchCustomerKeywordTable.find("#orderingElement").val("${orderElement == null ? '0' : orderElement}");
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

    function alignTableHeader() {
        var td = $("#customerKeywordTable tr:first td");
        var ctd = $("#headerTable tr:first td");
        $.each(td, function (idx, val) {
            ctd.eq(idx).width($(val).width());
        });
    }

    <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywords">
    function searchCustomerKeywords(url) {
        window.open(url);
    }
    </shiro:hasPermission>
</script>
</body>
</html>
