<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style>
        td{
            display: table-cell;
            vertical-align: inherit;
        }
        #showApplicationMarketTableDiv {
            position: fixed;
            top: 0px;
            left: 0px;
            background-color: white;
            width: 100%;
        }
    </style>
    <title>应用市场管理</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="showApplicationMarketTableDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px; margin-top: 5px;" cellpadding=3>
        <tr>
            <td colspan=14>
                <form method="post" id="searchApplicationMarketForm" action="/internal/applicationMarket/searchApplicationMarket">
                    <table style="font-size:12px;">
                        <tr>
                            <td align="right">应用市场名称: </td>
                            <td><input type="text" name="marketName" id="marketName" value="${applicationMarketCriteria.marketName}" style="width:200px;"></td>
                            <td align="right" width="60">
                                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                                <shiro:hasPermission name="/internal/applicationMarket/searchApplicationMarket">
                                    <input type="submit" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">&nbsp;&nbsp;
                                </shiro:hasPermission>
                            </td>
                            <td>
                                <shiro:hasPermission name="/internal/applicationMarket/saveApplicationMarket">
                                    <input type="button" onclick="showApplicationMarketDialog()" value=" 添加 ">&nbsp;&nbsp;
                                </shiro:hasPermission>
                            </td>
                            <td>
                                <shiro:hasPermission name="/internal/applicationMarket/deleteApplicationMarketList">
                                    <input type="button" onclick="deleteApplicationMarkets(this)" value=" 删除所选 ">
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
            <td align="center" width=100>应用市场名称</td>
            <td align="center" width=80>应用市场包名</td>
            <td align="center" width=80>临时文件地址</td>
            <td align="center" width=80>安装包地址</td>
            <td align="center" width=80>App数据库地址</td>
            <td align="center" width=180>App存储数据库地址</td>
            <td align="center" width=150>文件类型</td>
            <td align="center" width=100>操作</td>
        </tr>
    </table>
</div>
<div id="showApplicationMarketListDiv" style="margin-bottom: 30px">
    <table id="showApplicationMarketListTable"  width="100%">
        <c:forEach items="${page.records}" var="applicationMarket" varStatus="status">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30 <c:if test="${status.index%2==0}">bgcolor="#eeeeee" </c:if> >
                <td width="10" style="padding-left: 7px;"><input type="checkbox" name="uuid" value="${applicationMarket.uuid}" onclick="decideSelectAll()"/></td>
                <td width="100">${applicationMarket.marketName}</td>
                <td width="80">${applicationMarket.marketPackageName}</td>
                <td width="80">${applicationMarket.tmpPath}</td>
                <td width="80">${applicationMarket.apkPath}</td>
                <td width="80">${applicationMarket.dataDBPath} </td>
                <td width="180">${applicationMarket.storageDBPath}</td>
                <td width="150">${applicationMarket.fileType}</td>
                <td style="text-align: center;" width="100">
                    <shiro:hasPermission name="/internal/applicationMarket/saveApplicationMarket">
                    <a href="javascript:modifyApplicationMarket(${applicationMarket.uuid})">修改</a> |
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/applicationMarket/deleteApplicationMarket">
                    <a href="javascript:deleteApplicationMarket('${applicationMarket.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<div id="applicationMarketDialog" title="关键词信息" class="easyui-dialog" style="left: 35%;">
    <form id="applicationMarketForm" method="post" action="applicationMarketList.jsp">
        <table style="font-size:14px;" cellpadding=10 cellspacing="5">
            <tr>
                <td align="right">应用市场名称:</td>
                <td><input type="text" name="marketName" id="marketName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">应用市场包名:</td>
                <td><input type="text" name="marketPackageName" id="marketPackageName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">临时文件地址:</td>
                <td><input type="text" name="tmpPath" id="tmpPath" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">安装包地址:</td>
                <td><input type="text" name="apkPath" id="apkPath" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">App数据库地址:</td>
                <td><input type="text" name="dataDBPath" id="dataDBPath" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">App存储数据库地址:</td>
                <td><input type="text" name="storageDBPath" id="storageDBPath" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">文件类型:</td>
                <td><input type="text" name="fileType" id="fileType" style="width:200px;"></td>
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
<script src="${staticPath }/applicationMarket/applicationMarketList.js"></script>
</body>
</html>
