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
            #saveCustomerKeywordDialog ul{list-style: none;margin: 0px;padding: 0px;}
            #saveCustomerKeywordDialog li{margin: 5px 0;}
            #saveCustomerKeywordDialog .customerKeywordSpanClass{width: 70px;display: inline-block;text-align: right;}
        </style>
        <title>单词关键字数量统计</title>
    </head>
<body>
<div id="customerKeywordTopDiv">
    <%@include file="/menu.jsp" %>
    <form id="searchCustomerKeywordForm" style="font-size:12px; width: 100%;" action="/internal/customerKeyword/searchKeywordAmountCount" method="post">
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

            搜索引擎:
            <select name="searchEngine" id="searchEngine">
                <option value="">全部</option>
                <c:forEach items="${searchEngineMap}" var="entry">
                    <option value="${entry.value}">${entry.key}</option>
                </c:forEach>
            </select>

            <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
                <input type="submit" onclick="resetPageNumber(0)" value=" 查询 ">&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordStatus">
                <input type="button" onclick="updateCustomerKeywordStatus(0)" value=" 暂停关键字 ">&nbsp;
                <input type="button" onclick="updateCustomerKeywordStatus(1)" value=" 激活关键字 ">&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordGroupName">
                <input type="button" onclick="updateOptimizeGroupName('total')" value=" 修改当前关键字优化组 ">&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordGroupName">
                <input type="button" onclick="updateOptimizeGroupName('selected')" value=" 修改选中关键字优化组 ">&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordMachineGroup">
                <input type="button" onclick="updateMachineGroupName('total')" value=" 修改当前关键字机器分组 ">&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordMachineGroup">
                <input type="button" onclick="updateMachineGroupName('selected')" value=" 修改选中关键字机器分组 ">&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/deleteCustomerKeywords">
                <input type="button" onclick="deleteCustomerKeywords()" value=" 删除所选 ">
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/deleteCustomerKeywords">
                <c:if test="${sessionScope.get('entryType') eq 'qz'}">
                    <input type="button" onclick="deleteDuplicateQZKeyword()" value="删除所有重复关键字">
                </c:if>
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/saveCustomerKeyword">
                <input type="button" onclick="CustomerKeywordBatchUpdate('${sessionScope.entryType}')" value=" 批量修改 ">
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/saveCustomerKeyword">
                <input type="button" onclick="updateBearPawNumber('total','${customerKeywordCriteria.customerUuid}')" value=" 修改当前熊掌号 ">
                <input type="button" onclick="updateBearPawNumber('selected','${customerKeywordCriteria.customerUuid}')" value=" 修改选中熊掌号 ">
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
            <c:if test="${isDepartmentManager == true}">
            <td align="center" width=60>用户名称</td>
            </c:if>
            <td align="center" width=80>联系人</td>
            <td align="center" width=80>关键字</td>
            <td align="center" width=100>URL</td>
            <td align="center" width=100>熊掌号</td>
            <td align="center" width=150>标题</td>
            <td align="center" width=30>指数</td>
            <td align="center" width=50>原排名</td>
            <td align="center" width=50>现排名</td>
            <td align="center" width=50>搜索引擎</td>
            <td align="center" width=40>计价方式</td>
            <td align="center" width=30>要刷</td>
            <td align="center" width=30>已刷</td>
            <td align="center" width=30>无效</td>
            <td align="center" width=60>报价</td>
            <td align="center" width=100>订单号</td>
            <td align="center" width=60>付费状态</td>
            <td align="center" width=100>备注</td>
            <td align="center" width=100>失败原因</td>
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
                <c:if test="${isDepartmentManager == true}">
                <td align="center" width=60>
                    ${customerKeyword.userID}
                </td>
                </c:if>
                <td align="center" width=80>
                    <a href="#" onclick="searchCustomerKeywords('/internal/customerKeyword/searchCustomerKeywords/${customerKeyword.customerUuid}')">${customerKeyword.contactPerson}</a>
                </td>
                <td align="center" width=80>
                    ${customerKeyword.keyword}
                </td>

                <td align="center" width=100 class="floatTd" title="原始URL:${customerKeyword.originalUrl != null ?customerKeyword.originalUrl : customerKeyword.url}" >
                        ${customerKeyword.url==null?'':customerKeyword.url}
                </td>
                <td align="center" width=100>
                        ${customerKeyword.bearPawNumber == null ? "" : customerKeyword.bearPawNumber}
                </td>
                <td align="center" width=150>
                    ${customerKeyword.title == null ? "" : customerKeyword.title.trim()}
                </td>
                <td align="center" width=30>
                    <div style="height:16px;">
                        <a href="/internal/customerKeywordPositionIndexLog/historyPositionAndIndex/${customerKeyword.uuid}/30"
                             target="_blank" title="查看历史排名" class="floatTd">${customerKeyword.currentIndexCount}</a>
                    </div>
                </td>
                <td align="center" width=50>
                    <div style="height:16px;">${customerKeyword.initialPosition}
                    </div>
                </td>
                <td align="center" width=50 class="floatTd" title="排名采集城市: ${customerKeyword.capturePositionCity}">
                    <div style="height:16px;">
                        <a href="${customerKeyword.searchEngineUrl}" target="_blank">${customerKeyword.currentPosition}</a>
                    </div>
                </td>
                <td align="center" width=50>${customerKeyword.searchEngine}</td>
                <td align="center" class="floatTd" width=40 title="优化日期：<fmt:formatDate value="${customerKeyword.optimizeDate}" pattern="yyyy-MM-dd"/> ，要刷：${customerKeyword.optimizePlanCount}，已刷：${customerKeyword.optimizedCount}" >
                        ${customerKeyword.collectMethodName}
                </td>

                <td align="center" width=30>${customerKeyword.optimizePlanCount}</td>
                <td align="center" width=30>${customerKeyword.optimizedCount} </td>
                <td align="center" width=30>${customerKeyword.invalidRefreshCount}</td>
                <td align="center" width=60>${customerKeyword.feeString}</td>
                <td align="center" width=100>${customerKeyword.orderNumber}</td>
                <td align="center" width="60">${customerKeyword.paymentStatus}</td>
                <td align="center" width=100>${customerKeyword.remarks==null?"":customerKeyword.remarks} </td>
                <td align="center" width=100>${customerKeyword.failedCause == null ? "" : customerKeyword.failedCause} </td>
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
        <option>500</option>
        <option>1000</option>
    </select>
    </div>
</div>
<div id="targetGroupNameDialog" style="text-align: center;left: 40%;display: none;">
    <form id="targetGroupNameFrom" style="text-align: center;margin-top: 10px;">
        目标优化组名:<input type="text" id="groupName" name="groupName" style="width:150px">
    </form>
</div>
<div id="targetMachineGroupDialog" style="text-align: center;left: 40%;display: none;">
    <form id="targetMachineGroupForm" style="text-align: center;margin-top: 10px;">
        目标机器分组名:<input type="text" id="machineGroup" name="machineGroup" style="width:150px">
    </form>
</div>
<%@ include file="/customerkeyword/customerKeywordCommon.jsp" %>

<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/customerkeyword/customerKeywordCommon.js"></script>
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
            $("#showCustomerTableDiv").css("margin-top",$("#customerKeywordTopDiv").height());
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
        if(${customerKeywordCriteria.groupNameFuzzyQuery == 1}){
            $("#groupNameFuzzyQuery").prop("checked",true);
        }else{
            $("#groupNameFuzzyQuery").prop("checked",false);
        }
        noPositionValue();
        displayStopValue();
        pushPayValue();
        groupNameFuzzyQueryValue();
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

    function groupNameFuzzyQueryValue() {
        if ($("#groupNameFuzzyQuery").is(":checked")){
            $("#groupNameFuzzyQuery").val("1");
        } else {
            $("#groupNameFuzzyQuery").val("0");
        }
    }

    /**
     * 检索条件，机器分组是否模糊查询
     */
    function machineGroupFuzzyQueryValue() {
        if ($("#machineGroupFuzzyQuery").is(":checked")){
            $("#machineGroupFuzzyQuery").val("1");
        } else {
            $("#machineGroupFuzzyQuery").val("0");
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
        searchCustomerKeywordTable.find("#customerKeywordSource").val('${customerKeywordCriteria.customerKeywordSource}');
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
