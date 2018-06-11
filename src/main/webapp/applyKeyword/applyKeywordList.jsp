<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style>
        td{
            display: table-cell;
            vertical-align: inherit;
        }
        #showApplyKeywordTableDiv {
            position: fixed;
            top: 0px;
            left: 0px;
            background-color: white;
            width: 100%;
        }
    </style>
    <title>应用关键词管理</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="showApplyKeywordTableDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px; margin-top: 40px;" cellpadding=3>
        <tr>
            <td colspan=14>
                <form method="post" id="searchApplyKeywordForm" action="/internal/applyKeyword/searchApplyKeyword">
                    <table style="font-size:12px;">
                        <tr>
                            <td align="right">关键词: </td>
                            <td><input type="text" name="keyword" id="keyword" value="${applyKeywordCriteria.keyword}" style="width:200px;"></td>
                            <td align="right">应用名: </td>
                            <td><input type="text" name="applyName" id="applyName" value="${applyKeywordCriteria.applyName}" style="width:200px;"></td>
                            <td align="right" width="60">
                                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                                <shiro:hasPermission name="/internal/applyKeyword/searchApplyKeyword">
                                    <input type="submit" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">&nbsp;&nbsp;
                                </shiro:hasPermission>
                            </td>
                            <td>
                                <shiro:hasPermission name="/internal/applyKeyword/saveapplyKeyword">
                                    <input type="button" onclick="showApplyKeywordDialog()" value=" 添加 ">&nbsp;&nbsp;
                                </shiro:hasPermission>
                            </td>
                            <td>
                                <shiro:hasPermission name="/internal/supplier/deleteSuppliers">
                                    <input type="button" onclick="deleteApplyKeywordList(this)" value=" 删除所选 ">
                                </shiro:hasPermission>
                            </td>
                        </tr>
                    </table>
                </form>
            </td>
        </tr>
    </table>
    <table id="headerTable" width="100%">
        <tr bgcolor="#eeeeee" height=30>
            <td align="left" width="10" style="padding-left: 7px;"><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
            <td align="center" width=100>关键词</td>
            <td align="center" width=80>应用名</td>
            <td align="center" width=80>刷量</td>
            <td align="center" width=100>操作</td>
        </tr>
    </table>
</div>
<div id="showApplyKeywordListDiv" style="margin-bottom: 30px">
    <table id="showApplyKeywordListTable"  width="100%">
        <c:forEach items="${page.records}" var="applyKeyword" varStatus="status">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30 <c:if test="${status.index%2==0}">bgcolor="#eeeeee" </c:if> >
                <td width="10" style="padding-left: 7px;"><input type="checkbox" name="uuid" value="${applyKeyword.uuid}" onclick="decideSelectAll()"/></td>
                <td width="100">${applyKeyword.keyword}</td>
                <td width="80">${applyKeyword.applyName}</td>
                <td width="80">${applyKeyword.brushNumber}</td>
                <td style="text-align: center;" width="100">
                    <shiro:hasPermission name="/internal/applyKeyword/saveapplyKeyword">
                    <a href="javascript:modifyApplyKeyword('${applyKeyword.uuid}','edit')">修改</a> |
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/applyKeyword/deleteApplyKeyword">
                    <a href="javascript:deleteApplyKeyword('${applyKeyword.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<div id="applyKeywordDialog" title="关键词信息" class="easyui-dialog" style="left: 35%;">
    <form id="applyKeywordForm" method="post" action="applyKeywordList.jsp">
        <table style="font-size:14px;" cellpadding=10 cellspacing="5">
            <tr>
                <td align="right">关键词:</td>
                <td><input type="text" name="keyword" id="keyword" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">应用名:</td>
                <td>
                    <select name="applyName" id="applyName" style="width:200px;">
                        <option value="">==请选择==</option>
                        <c:forEach items="${applyInfoList}" var="applyInfo">
                        <option value="${applyInfo.uuid}">${applyInfo.appName}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
    </form>
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
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/applyKeyword/applyKeywordList.js"></script>
</body>
</html>
