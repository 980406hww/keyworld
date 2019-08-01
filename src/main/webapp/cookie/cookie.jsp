<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>Cookie清单</title>
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
    <div id="topDiv">
        <%@include file="/menu.jsp" %>
         <div style="margin: 5px 0px 0px 5px">
            <form method="post" id="searchCookieFrom" action="/internal/cookie/searchCookies" style="margin-bottom:0px">
                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                <input type="hidden" id="searchEngineHidden" value="${cookieCriteria.searchEngine}"/>
                Cookie内容:<input type="text" name="cookieStr" id="cookieStr" value="${cookieCriteria.cookieStr}" style="width:200px;">
                搜索引擎:
                <select name="searchEngine" id="searchEngine" >
                    <option value="">全部</option>
                    <option value="百度">百度</option>
                    <option value="360">360</option>
                </select>
                创建日期:<input name="createTime" id="createTime" class="Wdate" type="text" style="width:90px;" onClick="WdatePicker()" value="${cookieCriteria.createTime}"/>
                <shiro:hasPermission name="/internal/cookie/searchCookies">
                    <input type="submit" value="查询" onclick="submitPageNumber()"/>&nbsp;&nbsp;
                </shiro:hasPermission>
                <shiro:hasPermission name="/internal/cookie/saveCookieByFile">
                <input type="button" value="上传" onclick="uploadCookieTxt()"/>&nbsp;&nbsp;
                </shiro:hasPermission>
            </form>
        </div>
        <table style="font-size:12px; width: 100%;" id="headerTable">
            <tr bgcolor="" height="30">
                <td align="center" width="10">
                    <input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/>
                </td>
                <td align="center" width=30>搜索引擎</td>
                <td align="center" width=30>cookie数量</td>
                <td align="center" width=200>Cookie</td>
                <td align="center" width=40>创建时间</td>
            </tr>
        </table>
    </div>

    <div id="centerDiv" style="margin-bottom: 30px;">
        <table style="font-size:12px; width: 100%; table-layout:fixed; " id="showCookieTable">
            <c:forEach items="${page.records}" var="c" varStatus="status">
                <tr align="left" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
                    <td align="center" width=10 align="center">
                        <input type="checkbox" name="uuid" value="${c.uuid}" onclick="decideSelectAll()"/>
                    </td>
                    <td align="center" width=30>${c.searchEngine}</td>
                    <td align="center" width=30>${c.cookieCount}</td>
                    <td align="left" style="overflow:hidden;white-space:nowrap;" width=200>${c.cookieStr}</td>
                    <td align="center" width=40><fmt:formatDate value="${c.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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

    <div id="uploadTxtFileDialog" title="上传Cookie" class="easyui-dialog" style="left: 35%;">
        <div id="uploadTxtFileDiv" style="display: none;">
            <form method="post" id="uploadTxtFileForm" action="" enctype="multipart/form-data">
                搜索引擎:
                <select name="searchEngine" id="searchEngine" >
                    <option value="百度">百度</option>
                    <option value="360">360</option>
                </select><br><br>
                <input type="file" id="file" name="file" size=50 height="50px" style="width: 180px;">
            </form>
        </div>
    </div>
<%@ include file="/commons/loadjs.jsp"%>
<script src="${staticPath}/cookie/cookie.js"></script>
</body>
</html>


