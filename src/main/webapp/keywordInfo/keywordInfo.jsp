<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <title>取词清单</title>
    <script type="text/javascript" src="${staticPath}/static/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
    <div id="topDiv">
        <%@include file="/menu.jsp" %>
        <div style="margin-top: 35px">
            <form method="post" id="searchwordInfoForm" action="/internal/keywordInfo/keywordInfos" style="margin-bottom:0px ">

                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>

                用户名:<input type="text" name="userName" id="userName" value="${KeywordInfoCriteria.userName}">&nbsp;&nbsp;
                监控类型:<input type="text" name="monitoring" id="monitoring" value="${KeywordInfoCriteria.monitoring}">&nbsp;&nbsp;
                操作类型:<select type="text" name="operationType" class="select" id="operationType" >
                            <option value="">全部</option>
                            <option value="add">add(添加)</option>
                            <option value="delete">delete(删除)</option>
                        </select>
                        &nbsp;&nbsp;
                网站地址:<input type="text" name="keywordInfo" id="keywordInfo" value="${KeywordInfoCriteria.keywordInfo}">&nbsp;&nbsp;
                <input id="autoflag" type="hidden" value="${KeywordInfoCriteria.operationType}">

                创建日期:<input name="createTime" id="createTime" class="Wdate" type="text" style="width:90px;" onClick="WdatePicker()" value="${KeywordInfoCriteria.createTime}">

                <shiro:hasPermission name="/internal/keywordInfo/keywordInfos">
                <input type="submit" value="查询" onclick="resetPageNumber()">&nbsp;&nbsp;
                </shiro:hasPermission>
                <shiro:hasPermission name="/internal/keywordInfo/keywordInfos">
                    <input type="button" value="清空" onclick="resetDate()">&nbsp;&nbsp;
                </shiro:hasPermission>
            </form>
        </div>

        <table style="font-size:12px; width: 100%;" id="headerTable">
            <tr bgcolor="" height="30">
                <td align="center" width="10">
                    <input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/>
                </td>
                <td align="center" width=60>用户名</td>
                <td align="center" width=60>监控类型</td>
                <td align="center" width=60>操作类型</td>
                <td align="center" width=140>网站地址</td>
                <td align="center" width=60>创建时间</td>
            </tr>
        </table>
    </div>

    <div id="centerDiv" style="margin-bottom: 30px;">
        <table style="font-size:12px; width: 100%; table-layout:fixed; " id="showWebsiteTable">
            <c:forEach items="${page.records}" var="keywordinfo" varStatus="status">
            <tr align="left" height=30  <c:if test="${status.index%2==0}">bgcolor="#eeeeee"</c:if> >
                <td align="center" width=13 align="center"><input type="checkbox" name="uuid" value="${keywordinfo.id}" onclick="decideSelectAll()"/></td>
                <td align="center" width=59>${keywordinfo.userName}</td>
                <td align="center" width=59>${keywordinfo.searchEngine}</td>
                <td align="center" width=60>${keywordinfo.operationType}</td>
                <td align="center" width=99 style=" padding-left: 20px;padding-right: 20px;white-space: nowrap;overflow: hidden;text-overflow: ellipsis;">
                    <a href="javascript:openUrls('${keywordinfo.keywordInfo}','${keywordinfo.spliterStr}')" >${keywordinfo.keywordInfo}</a>
                </td>
                <td align="center" width=60><fmt:formatDate  value="${keywordinfo.createTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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


    <div id="keywordInfos" title="取词清单" class="easyui-dialog" style="left: 30%;display:none;">
        <form >
            <table style="font-size:25px;" cellpadding=5>
                <tr>
                    <td align="right">网站类型:</td>
                    <td>
                        <input type="text" name="typeKeyword" id="typeKeyword" value="" style="width:430px;">
                    </td>
                </tr>
                <tr>
                    <td align="right">网站地址:</td>
                    <td><textarea name="remark" id="remark" value=""
                                  style="width:430px;height:200px;resize: none"></textarea></td>
                </tr>
            </table>
        </form>
    </div>

<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath}/keywordInfo/keywordInfo.js"></script>
</body>
</html>