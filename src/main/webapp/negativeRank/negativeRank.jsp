
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>关键词负面排名</title>
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
    <div id="topDiv">
        <%@include file="/menu.jsp" %>
         <div style="margin-top: 35px">
            <form method="post" id="searchNegativeRankFrom" action="/internal/negativeRank/searchNegativeRank" style="margin-bottom:0px">
                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                &nbsp;&nbsp;账户名称:<input type="text" name="keyword" id="keyword" value="${negativeRankCriteria.keyword}">&nbsp;&nbsp;
                <input type="hidden" name="searchEngineHidden" id="searchEngineHidden" value="${negativeRankCriteria.searchEngine}"/>&nbsp;&nbsp;
                搜索引擎:
                  <select type="text" name="searchEngine" class="select" id="searchEngine" >
                      <option value="">全部搜索引擎</option>
                      <option value="百度电脑">百度电脑</option>
                      <option value="百度手机">百度手机</option>
                      <option value="搜狗电脑">搜狗电脑</option>
                      <option value="搜狗手机">搜狗手机</option>
                      <option value="360电脑">360电脑</option>
                      <option value="360手机">360手机</option>
                      <option value="神马">神马</option>
                  </select>
                &nbsp;&nbsp;
                创建日期:<input name="createTime" id="createTime" class="Wdate" type="text" style="width:90px;" onClick="WdatePicker()" value="${negativeRankCriteria.createTime}"/>
                <shiro:hasPermission name="/internal/negativeRank/searchNegativeRank">
                    <input type="submit" value="查询" onclick="submitPageNumber()"/>&nbsp;&nbsp;
                </shiro:hasPermission>
            </form>
        </div>
        <table style="font-size:12px; width: 100%;" id="headerTable">
            <tr bgcolor="" height="30">
                <td align="center" width="10">
                    <input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/>
                </td>
                <td align="center" width=60>关键字</td>
                <td align="center" width=50>搜索引擎</td>
                <td align="center" width=40>负面总数</td>
                <td align="center" width=40>首页负面排名</td>
                <td align="center" width=40>第二页负面排名</td>
                <td align="center" width=40>第三页负面排名</td>
                <td align="center" width=40>第四页负面排名</td>
                <td align="center" width=40>其他负面排名</td>
                <td align="center" width=60>创建时间</td>
            </tr>
        </table>
    </div>

    <div id="centerDiv" style="margin-bottom: 30px;">
        <table style="font-size:12px; width: 100%; table-layout:fixed; " id="showNegativeRankTable">
            <c:forEach items="${page.records}" var="NegativeRank" varStatus="status">
                <tr align="left" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
                    <td align="center" width=13 align="center"><input type="checkbox" name="uuid" value="${NegativeRank.uuid}" onclick="decideSelectAll()"/></td>
                    <td align="center" width=59>${NegativeRank.keyword}</td>
                    <td align="center" width=49>${NegativeRank.searchEngine}</td>
                    <td align="center" width=40>${NegativeRank.negativeCount}</td>
                    <td align="center" width=40>${NegativeRank.firstPageRanks}</td>
                    <td align="center" width=40>${NegativeRank.secondPageRanks}</td>
                    <td align="center" width=39>${NegativeRank.thirdPageRanks}</td>
                    <td align="center" width=40>${NegativeRank.fifthPageRanks}</td>
                    <td align="center" width=40>${NegativeRank.otherPageRanks}</td>
                    <td align="center" width=60><fmt:formatDate  value="${NegativeRank.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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

<%@ include file="/commons/loadjs.jsp"%>
<script src="${staticPath}/negativeRank/negativeRank.js"></script>
</body>
</html>


