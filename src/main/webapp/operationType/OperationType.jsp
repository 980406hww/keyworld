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
    <title>操作类型管理</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="topDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px;" cellpadding=3>
        <tr>
            <td colspan=13>
                <form method="post" id="searchOperationTypeForm" action="/internal/operationType/searchOperationTypeLists"
                      style="margin-bottom:0 ">
                    <div>
                        <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                        <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                        <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                        <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                        操作类型名称:
                        <input type="text" name="operationTypeName" id="searchOperationTypeName" style="width: 160px;" value="${operationType.operationTypeName}">
                        &nbsp;&nbsp;
                        <input type="submit" class="ui-button ui-widget ui-corner-all" onclick="resetPageNumber()" value=" 查询 ">
                        &nbsp;&nbsp;
                        <input type="button" class="ui-button ui-widget ui-corner-all" value=" 添加 " onclick="showOperationTypeDialog(null)"/>
                        &nbsp;&nbsp;
                        <input type="button" class="ui-button ui-widget ui-corner-all" value=" 删除所选 " onclick="deleteBatchOperationType(this)"/>
                    </div>
                </form>
            </td>
        </tr>
    </table>
    <table style="font-size:12px; width: 100%;" id="headerTable">
        <tr bgcolor="" height="30">
            <td style="padding-left: 7px;" width=10>
                <input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/>
            </td>
            <td align="center" width=80>操作类型名称</td>
            <td align="center" width=80>终端类型</td>
            <td align="center" width=80>操作类型描述</td>
            <td align="center" width=80>备注</td>
            <td align="center" width=80>创建时间</td>
            <td align="center" width=80>是否有效</td>
            <td align="center" width=80>操作</td>
        </tr>
    </table>
</div>

<div id="operationTypeDialog" title="修改操作类型" class="easyui-dialog" style="display: none">
    <form id="operationTypeForm">
        <table style="font-size:12px" id="operationTypeTable" align="center" cellspacing="8">
            <tr>
                <td style="width:100px" align="right">操作类型名称:</td>
                <td>
                    <input type="hidden" name="operationTypeUuid" id="operationTypeUuid" style="width:240px;">
                    <input type="text" name="operationTypeName" id="operationTypeName" style="width:240px;">
                </td>
            </tr>
            <tr>
                <td style="width:100px" align="right">终端类型:</td>
                <td>
                    <input type="radio" name="terminalType" value="PC"/>&nbsp;PC&emsp;&emsp;&nbsp;&nbsp;
                    <input type="radio" name="terminalType" value="Phone"/>&nbsp;Phone
                </td>
            </tr>
            <tr>
                <td style="width:100px" align="right">是否有效:</td>
                <td>
                    <input type="radio" name="status" value="1" checked="checked"/>&nbsp;有效&emsp;&emsp;
                    <input type="radio" name="status" value="0"/>&nbsp;无效
                </td>
            </tr>
            <tr>
                <td style="width:100px" align="right">备注:</td>
                <td>
                    <input type="text" name="remark" id="remark" style="width:240px;">
                </td>
            </tr>
            <tr>
                <td style="width:100px;vertical-align:top" align="right">操作类型描述</td>
                <td>
                    <textarea id="description" style="width:240px; height: 150px; resize: none"></textarea>
                </td>
            </tr>
        </table>
    </form>
</div>

<div id="showOperationTypeTableDiv" style="margin-bottom: 30px">
    <table style="font-size:12px; width: 100%;" id="showOperationTypeTable">
        <c:forEach items="${page.records}" var="operationType">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30>
                <td width=10 style="padding-left: 7px;">
                    <input type="checkbox" name="uuid" value="${operationType.uuid}"/>
                </td>
                <td width=80>${operationType.operationTypeName}</td>
                <td width=80>${operationType.terminalType}</td>
                <td width=80>${operationType.description}</td>
                <td width=80>${operationType.remark}</td>
                <td width=80 style="text-align: center">
                    <fmt:formatDate value="${operationType.createTime}" pattern="yyyy-MM-dd"/>
                </td>
                <td width=80 style="text-align: center">${operationType.status == 1 ? "有效" : "无效"}</td>
                <td width=80>
                    <a href="javascript:modifyOperationType(${operationType.uuid})">修改</a>
                    |
                    <a href="javascript:deleteOperationType('${operationType.uuid}','${operationType.operationTypeName}')">删除</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>
<div id="showCustomerBottomPositioneDiv">
    <div id="showCustomerBottomDiv">
        <input id="fisrtButton" class="ui-button ui-widget ui-corner-all" type="button"
               onclick="changePaging(1,'${page.size}')" value="首页"/>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input id="upButton" type="button" class="ui-button ui-widget ui-corner-all"
               onclick="changePaging('${page.current-1}','${page.size}')" value="上一页"/>
        &nbsp;&nbsp;&nbsp;&nbsp;${page.current}/${page.pages}&nbsp;&nbsp;
        <input id="nextButton" type="button" class="ui-button ui-widget ui-corner-all"
               onclick="changePaging('${page.current+1>=page.pages?page.pages:page.current+1}','${page.size}')"
               value="下一页">
        &nbsp;&nbsp;&nbsp;&nbsp;
        <input id="lastButton" type="button" class="ui-button ui-widget ui-corner-all"
               onclick="changePaging('${page.pages}','${page.size}')" value="末页">
        &nbsp;&nbsp;&nbsp;&nbsp;总记录数:${page.total}&nbsp;&nbsp;&nbsp;&nbsp;每页显示条数:
        <select id="chooseRecords" onchange="changePaging(${page.current},this.value)">
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
<script src="${staticPath}/operationType/operationType.js"></script>
<script type="text/javascript">
    $(function () {
        window.onresize = function () {
            $("#showScreenedWebsiteDiv").css("margin-top", $("#topDiv").height());
        }
    });
</script>
</body>
</html>