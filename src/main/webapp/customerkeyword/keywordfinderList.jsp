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
            搜索引擎:
            <select name="searchEngine" id="searchEngine" value="${customerKeywordCriteria.searchEngine}">
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
            排序:
            <select name="orderingElement" id="orderingElement">
                <option value="0">关键字</option>
                <option value="1">创建日期</option>
                <option value="2">当前排名</option>
                <option value="3">添加序号</option>
            </select>
            &nbsp;
            备注:<input type="text" name="remarks" style="width: 90px;" value="${customerKeywordCriteria.remarks}">
            <shiro:hasPermission name="/internal/customerKeyword/searchCustomerKeywordLists">
                <input type="submit" onclick="resetPageNumber()" value=" 查询 ">&nbsp;&nbsp;
            </shiro:hasPermission>
            <shiro:hasPermission name="/internal/customerKeyword/updateCustomerKeywordStatus">
                <input type="button" onclick="updateCustomerKeywordStatus(0)" value=" 暂停关键字 ">&nbsp;&nbsp;
                <input type="button" onclick="updateCustomerKeywordStatus(1)" value=" 激活关键字 ">
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

                <td  align="center" width=100 class="floatTd" title="原始URL:${customerKeyword.originalUrl != null ?customerKeyword.originalUrl : customerKeyword.url}" >
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
        searchCustomerKeywordTable.find("#orderingElement").val('${orderingElement}');
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
