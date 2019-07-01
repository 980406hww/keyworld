<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style type="text/css">
        #showOperationTypeTableDiv {
            width: 100%;
            margin: auto;
        }

        #showOperationTypeTable tr:nth-child(odd) {
            background: #EEEEEE;
        }

        #showOperationTypeTable td {
            text-align: left;
        }

        h6 {
            margin: 0 5px;
        }
    </style>
    <title>销售人员管理</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=13>
                <form method="post" id="searchSalesManageForm" action="/internal/salesManage/searchSalesManageLists" style="margin-bottom:0 ">
                    <div>
                        <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                        <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                        <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                        <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                        <shiro:hasPermission name="/internal/salesManage/searchSalesInfo">
                        销售人员名称:
                        <input type="text" name="salesName" id="salesName" style="width: 160px;" value="${salesManage.salesName}" title="">
                        &nbsp;
                        <input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" value=" 查询 ">
                        </shiro:hasPermission>
                        &nbsp;
                        <shiro:hasPermission name="/internal/salesManage/saveSalesInfo">
                        <input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showSalesManageDialog(null)"/>
                        </shiro:hasPermission>
                        &nbsp;
                        <shiro:hasPermission name="/internal/salesManage/deleteSalesInfo">
                        <input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteBatchOperationType(this)"/>
                        </shiro:hasPermission>
                    </div>
                </form>
            </td>
        </tr>
    </table>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10>
                <input type="checkbox" onclick="selectAll(this)" id="selectAllChecked" title=""/>
            </td>
            <td align="center" width=70>销售人员名称</td>
            <td align="center" width=70>电话号码</td>
            <td align="center" width=70>QQ号码</td>
            <td align="center" width=80>微信号</td>
            <td align="center" width=120>二维码链接</td>
            <td align="center" width=80>邮箱号</td>
            <td align="center" width=70>负责部分</td>
            <td align="center" width=80>创建时间</td>
            <td align="center" width=80>更新时间</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>

<div id="salesManageDialog" class="easyui-dialog" style="display: none">
    <form id="salesManageForm">
        <table style="font-size:12px" id="operationTypeTable" align="center" cellspacing="8">
            <tr>
                <td style="width:70px" align="right">销售名称:</td>
                <td>
                    <input type="hidden" name="uuid" id="uuid" style="width:180px;">
                    <input type="text" name="formSalesName" id="formSalesName" style="width:180px;" title="" autocomplete="off">
                </td>
            </tr>
            <tr>
                <td style="width:70px" align="right">电话号码:</td>
                <td>
                    <input type="text" name="telephone" id="telephone" style="width:180px;" title="" autocomplete="off">
                </td>
            </tr>
            <tr>
                <td style="width:70px" align="right">QQ号:</td>
                <td>
                    <input type="text" name="qq" id="qq" style="width:180px;" title="" autocomplete="off">
                </td>
            </tr>
            <tr>
                <td style="width:70px" align="right">微信号:</td>
                <td>
                    <input type="text" name="weChat" id="weChat" style="width:180px;" title="" autocomplete="off">
                </td>
            </tr>
            <tr>
                <td style="width:70px" align="right">二维码链接:</td>
                <td>
                    <input type="file" id="qrCode" style="filter:alpha(opacity=0);opacity:1;width: 0;height: 0;display: none">
                    <input type="text" name="quickResponseCode" id="quickResponseCode" style="width:180px;" title="" autocomplete="off">
                </td>
            </tr>
            <tr>
                <td style="width:70px" align="right">邮箱:</td>
                <td>
                    <input type="text" name="email" id="email" style="width:180px;" title="" autocomplete="off">
                </td>
            </tr>
            <tr>
                <td style="width:70px" align="right">负责部分:</td>
                <td>
                    <select id="managePart" name="managePart" style="width: 180px" title="">
                        <option value="" selected="selected">请选择</option>
                        <c:forEach items="${websiteTypeMap}" var="websiteType">
                            <option value="${websiteType.key}">${websiteType.value}</option>
                        </c:forEach>
                    </select>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="showOperationTypeTableDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showOperationTypeTable">
        <c:forEach items="${page.records}" var="salesManage">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width=10 style="padding-left: 7px;">
                    <input type="checkbox" name="uuid" value="${salesManage.uuid}" title=""/>
                </td>
                <td width=70>${salesManage.salesName}</td>
                <td width=70>${salesManage.telephone}</td>
                <td width=70>${salesManage.qq}</td>
                <td width=80>${salesManage.weChat}</td>
                <td width=120>${salesManage.quickResponseCode}</td>
                <td width=80>${salesManage.email}</td>
                <td style="text-align: center" width=70>${websiteTypeMap[salesManage.managePart]}</td>
                <td width=80 style="text-align: center">
                    <fmt:formatDate value="${salesManage.createTime}" pattern="yyyy-MM-dd"/>
                </td>
                <td width=80 style="text-align: center">
                    <fmt:formatDate value="${salesManage.updateTime}" pattern="yyyy-MM-dd"/>
                </td>
                <td width=80>
                    <shiro:hasPermission name="/internal/salesManage/updateSalesInfo">
                    <a href="javascript:modifySalesManage(${salesManage.uuid})">修改</a>
                    </shiro:hasPermission>
                    |
                    <shiro:hasPermission name="/internal/salesManage/deleteSalesInfo">
                    <a href="javascript:deleteSalesManage('${salesManage.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div id="showCustomerBottomPositioneDiv">
    <div id="showCustomerBottomDiv">
        <input id="fisrtButton" class="ui-button ui-widget ui-corner-all" type="button" onclick="changePaging(1,'${page.size}')" value="首页"/>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input id="upButton" type="button" class="ui-button ui-widget ui-corner-all" onclick="changePaging('${page.current-1}','${page.size}')" value="上一页"/>
        &nbsp;&nbsp;&nbsp;&nbsp;${page.current}/${page.pages}&nbsp;&nbsp;
        <input id="nextButton" type="button" class="ui-button ui-widget ui-corner-all" onclick="changePaging('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')" value="下一页">
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input id="lastButton" type="button" class="ui-button ui-widget ui-corner-all" onclick="changePaging('${page.pages}','${page.size}')" value="末页">
        &nbsp;&nbsp;&nbsp;&nbsp;总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;每页显示条数:
        <select id="chooseRecords" onchange="changePaging(${page.current},this.value)" title="">
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
<script src="${staticPath}/static/qrCode/reqrcode.js"></script>
<script src="${staticPath}/salesManage/salesManage.js"></script>
<script type="text/javascript">
    $(function () {
        window.onresize = function () {
            $("#showScreenedWebsiteDiv").css("margin-top", $("#topDiv").height());
        }
    });
</script>
</body>
</html>