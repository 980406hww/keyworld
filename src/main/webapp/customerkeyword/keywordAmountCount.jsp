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
                text-align: center;
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
    <form id="searchCustomerKeywordForm" style="font-size:12px; width: 100%;" action="/internal/customerKeyword/searchKeywordAmountCountLists" method="post">
        <div id="searchCustomerKeywordTable">
            <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
            <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
            <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
            <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
            关键字:&nbsp;<input type="text" name="keyword" id="keyword" value="${keywordAmountCountCriteria.keyword}" style="width:100px;">&nbsp;
                用户名称:
                <select name="userName" id="userName">
                    <option value="">所有</option>
                    <option value="${user.loginName}">只显示自己</option>
                    <c:if test="${isDepartmentManager}">
                        <c:forEach items="${activeUsers}" var="activeUser">
                            <option value="${activeUser.loginName}">${activeUser.userName}</option>
                        </c:forEach>
                    </c:if>
                </select>
            搜索引擎:
            <select name="searchEngine" id="searchEngine">
                <option value="">全部</option>
                <c:forEach items="${searchEngineMap}" var="entry">
                    <option value="${entry.value}">${entry.key}</option>
                </c:forEach>
            </select>

            <shiro:hasPermission name="/internal/customerKeyword/searchKeywordAmountCountLists">
                <input type="submit" onclick="resetPageNumber(0)" value=" 查询 ">&nbsp;
            </shiro:hasPermission>
            <br/>
        </div>
    </form>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="#eeeeee" height=30>
            <td width="10" style="padding-left: 7px;"><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=70>关键字</td>
            <td align="center" width=70>搜索引擎</td>
            <td align="center" width=70>单词总数</td>
            <td align="center" width=70>用户总数</td>
            <td align="center" width=70>客户总数</td>
            <td align="center" width=70>前三数量</td>
            <td align="center" width=70>前十数量</td>
        </tr>
    </table>
</div>

<div id="showCustomerTableDiv" style="margin-bottom: 30px">
    <table id="customerKeywordTable" style="font-size:12px;">
        <c:forEach items="${page.records}" var="customerKeyword">
            <tr height=30 onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width="10" style="padding-left: 7px;">
                    <input type="checkbox" name="uuid" value="${customerKeyword.keyword}" onclick="decideSelectAll()"/>
                </td>
                <td align="center" width=70 style="text-align: left">
                    <a href="javascript:searchCustomerKeywords('${customerKeyword.keyword}','')"> ${customerKeyword.keyword}</a>
                </td>
                <td align="center" width=70>${customerKeyword.searchEngine} </td>
                <td align="center" width=70>${customerKeyword.keywordCount}</td>
                <td align="center" width=70>${customerKeyword.customerCount}</td>
                <td align="center" width=70>${customerKeyword.userCount}</td>
                <td align="center" width=70>
                    <a href="javascript:searchCustomerKeywords('${customerKeyword.keyword}','3')"> ${customerKeyword.topThree}</a>
                </td>
                <td align="center" width="70">
                    <a href="javascript:searchCustomerKeywords('${customerKeyword.keyword}','10')"> ${customerKeyword.topTen}</a>
                </td>
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



<form id="searchKeywordForm" style="display: none;" method="post" target="_blank"
      action="/internal/customerKeyword/searchCustomerKeywordLists">
    <input type="hidden" name="keyword" id="keyword"/>
    <input type="hidden" name="ltPosition" id="ltPosition" value=""/>
</form>

<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/js/jquery.tablesorter.js"></script>
<script language="javascript">
    $(function () {
        $("#showCustomerTableDiv").css("margin-top",$("#customerKeywordTopDiv").height());
        initPaging();
        alignTableHeader();
        $("#userName").val("${keywordAmountCountCriteria.userName}");
        window.onresize = function(){
            $("#showCustomerTableDiv").css("margin-top",$("#customerKeywordTopDiv").height());
            alignTableHeader();
        };
    });

    function initPaging() {
        var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
        var searchCustomerKeywordTable = searchCustomerKeywordForm.find("#searchCustomerKeywordTable");
        searchCustomerKeywordTable.find("#searchEngine").val('${keywordAmountCountCriteria.searchEngine}');
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
        function searchCustomerKeywords(keyword, ltPosition) {
            $("#searchKeywordForm #keyword").val(keyword);
            $("#searchKeywordForm #ltPosition").val(ltPosition);
            $("#searchKeywordForm").submit();
        }
    </shiro:hasPermission>
    function changePaging(currentPage, pageSize) {
        var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
        searchCustomerKeywordForm.find("#currentPageNumberHidden").val(currentPage);
        searchCustomerKeywordForm.find("#pageSizeHidden").val(pageSize);
        searchCustomerKeywordForm.submit();
    }

    function resetPageNumber(days) {
        var searchCustomerKeywordForm = $("#searchCustomerKeywordForm");
        var keyword = searchCustomerKeywordForm.find("#keyword").val();
        searchCustomerKeywordForm.find("#currentPageNumberHidden").val(1);
        if(days != 0) {
            searchCustomerKeywordForm.submit();
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
</script>
</body>
</html>
