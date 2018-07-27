<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style>
        td{
            display: table-cell;
            vertical-align: inherit;
        }
        #showApplyInfoTableDiv {
            position: fixed;
            top: 0px;
            left: 0px;
            background-color: white;
            width: 100%;
        }
    </style>
    <title>应用信息列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="showApplyInfoTableDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px; margin-top: 40px;" cellpadding=3>
        <tr>
            <td colspan=14>
                <form method="post" id="searchApplyInfoForm" action="/internal/applyInfo/searchApplyInfo">
                    <table style="font-size:12px;">
                        <tr>
                            <td align="right">应用名: </td>
                            <td><input type="text" name="appName" id="appName" value="${applyInfoCriteria.appName}" style="width:200px;"></td>
                            <td align="right">所属应用市场: </td>
                            <td><input type="text" name="applicationMarketName" id="applicationMarketName" value="${applyInfoCriteria.applicationMarketName}" style="width:200px;"></td>
                            <td align="right" width="60">
                                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                                <shiro:hasPermission name="/internal/applyInfo/searchApplyInfo">
                                    <input type="submit" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">&nbsp;&nbsp;
                                </shiro:hasPermission>
                            </td>
                            <td>
                                <shiro:hasPermission name="/internal/applyInfo/saveApplyInfo">
                                    <input type="button" onclick="showApplyInfoDialog()" value=" 添加 ">&nbsp;&nbsp;
                                </shiro:hasPermission>
                            </td>
                            <td>
                                <shiro:hasPermission name="/internal/applyInfo/deleteApplyInfoList">
                                    <input type="button" onclick="deleteApplyInfos(this)" value=" 删除所选 ">
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
            <td align="center" width=80>应用名</td>
            <td align="center" width=80>所属应用市场</td>
            <td align="center" width=80>包名</td>
            <td align="center" width=80>应用市场ID</td>
            <td align="center" width=80>标志颜色</td>
            <td align="center" width=80>PosandColor</td>
            <td align="center" width=100>操作</td>
        </tr>
    </table>
</div>
<div id="showApplyInfoListDiv" style="margin-bottom: 30px">
    <table id="showApplyInfoListTable"  width="100%">
        <c:forEach items="${page.records}" var="applyInfo" varStatus="status">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30 <c:if test="${status.index%2==0}">bgcolor="#eeeeee" </c:if> >
                <td width="10" style="padding-left: 7px;"><input type="checkbox" name="uuid" value="${applyInfo.uuid}" onclick="decideSelectAll()"/></td>
                <td width="80">${applyInfo.appName}</td>
                <td width="80">${applyInfo.applicationMarketName}</td>
                <td width="80">${applyInfo.packageName}</td>
                <td width="80">${applyInfo.id}</td>
                <td width="80">${applyInfo.color}</td>
                <td width="80">${applyInfo.posandcolor} </td>
                <td style="text-align: center;" width="100">
                    <shiro:hasPermission name="/internal/applyInfo/saveApplyInfo">
                    <a href="javascript:modifyApplyInfo(${applyInfo.uuid})">修改</a> |
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/applyInfo/deleteApplyInfo">
                    <a href="javascript:deleteApplyInfo('${applyInfo.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<div id="applyInfoDialog" title="关键词信息" class="easyui-dialog" style="left: 35%;">
    <form id="applyInfoForm" method="post" action="applyInfoList.jsp">
        <table style="font-size:14px;" cellpadding=10 cellspacing="5">
            <tr>
                <td align="right">应用名:</td>
                <td><input type="text" name="appName" id="appName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">所属应用市场:</td>
                <td>
                    <select name="applicationName" id="applicationName" style="width:200px;">
                        <option value="">==请选择==</option>
                        <c:forEach items="${applicationMarkets}" var="applicationMarket">
                            <option value="${applicationMarket.uuid}">${applicationMarket.marketName}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="right">包  名:</td>
                <td><input type="text" name="packageName" id="packageName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">应用市场ID:</td>
                <td><input type="text" name="id" id="id" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">标志颜色:</td>
                <td><input type="text" name="color" id="color" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">PosandColor:</td>
                <td><input type="text" name="posandcolor" id="posandcolor" style="width:200px;"></td>
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
        <option>500</option>
        <option>1000</option>
    </select>
    </div>
</div>
<%@ include file="/commons/loadjs.jsp" %>
<script src="${staticPath }/applyInfo/applyInfoList.js"></script>
</body>
</html>
