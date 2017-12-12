<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <style>
        td{
            display: table-cell;
            vertical-align: inherit;
        }
        #showSupplierTableDiv {
            position: fixed;
            top: 0px;
            left: 0px;
            background-color: white;
            width: 100%;
        }
    </style>
    <title>供应商列表</title>
</head>
<body>
<%@ include file="/commons/basejs.jsp" %>
<div id="showSupplierTableDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px; margin-top: 40px;" cellpadding=3>
        <tr>
            <td colspan=14>
                <form method="post" id="searchSupplierForm" action="/internal/supplier/searchSuppliers">
                    <table style="font-size:12px;">
                        <tr>
                            <td align="right">联系人: </td>
                            <td><input type="text" name="contactPerson"
                                       value="${supplierCriteria.contactPerson}"
                                       style="width:200px;"></td>
                            <td align="right">&nbsp;&nbsp;QQ:&nbsp;</td>
                            <td><input type="text" name="qq" value="${supplierCriteria.qq}"
                                       style="width:200px;"></td>
                            <td align="right">&nbsp;&nbsp;联系电话:&nbsp;</td>
                            <td><input type="text" name="phone" value="${supplierCriteria.phone}"
                                       style="width:200px;">
                            </td>
                            <td align="right" width="60">
                                <input type="hidden" name="currentPageNumber" id="currentPageNumberHidden" value="${page.current}"/>
                                <input type="hidden" name="pageSize" id="pageSizeHidden" value="${page.size}"/>
                                <input type="hidden" name="pages" id="pagesHidden" value="${page.pages}"/>
                                <input type="hidden" name="total" id="totalHidden" value="${page.total}"/>
                                <shiro:hasPermission name="/internal/supplier/searchSuppliers">
                                    <input type="submit" onclick="resetPageNumber()" name="btnQuery" id="btnQuery" value=" 查询 ">&nbsp;&nbsp;
                                </shiro:hasPermission>
                            </td>
                            <td>
                                <shiro:hasPermission name="/internal/supplier/saveSupplier">
                                    <input type="button" onclick="showSupplierDialog()" value=" 添加 ">&nbsp;&nbsp;
                                </shiro:hasPermission></td>
                            <td>
                                <shiro:hasPermission name="/internal/supplier/deleteSuppliers">
                                    <input type="button" onclick="deleteSuppliers(this)" value=" 删除所选 ">
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
            <td align="center" width=100>供应商名称</td>
            <td align="center" width=80>联系人</td>
            <td align="center" width=80>电话</td>
            <td align="center" width=80>QQ</td>
            <td align="center" width=80>微信</td>
            <td align="center" width=180>地址</td>
            <td align="center" width=150>服务类型</td>
            <td align="center" width=130>URL</td>
            <td align="center" width=80>邮箱</td>
            <td align="center" width=130 >备注</td>
            <td align="center" width=100 >更新时间</td>
            <td align="center" width=100 >添加时间</td>
            <td align="center" width=100>操作</td>
        </tr>
    </table>
</div>
<div id="showSupplierListDiv" style="margin-bottom: 30px">
    <table id="showSupplierListTable"  width="100%">
        <c:forEach items="${page.records}" var="supplier" varStatus="status">
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30 <c:if test="${status.index%2==0}">bgcolor="#eeeeee" </c:if> >
                <td width="10" style="padding-left: 7px;"><input type="checkbox" name="uuid" value="${supplier.uuid}" onclick="decideSelectAll()"/></td>
                <td width="100">${supplier.supplierName}</td>
                <td width="80">${supplier.contactPerson}</td>
                <td width="80">${supplier.phone}</td>
                <td width="80">${supplier.qq}</td>
                <td width="80">${supplier.weChat} </td>
                <td width="180">${supplier.address}</td>
                <td width="150">
                    <c:forEach items="${supplier.supplierServiceTypeMappings}" var="supplierServiceTypeMappings">
                        ${supplierServiceTypeMappings.supplierServiceType.name} ${" "}
                    </c:forEach>
                </td>
                <td width="130">${supplier.url}</td>
                <td width="80">${supplier.email}</td>
                <td width="130">${supplier.remark}</td>
                <td width="100"><fmt:formatDate value="${supplier.updateTime}" pattern="MM-dd HH:mm" /></td>
                <td width="100"><fmt:formatDate value="${supplier.createTime}" pattern="MM-dd HH:mm" /></td>
                <td style="text-align: center;" width="100">
                    <shiro:hasPermission name="/internal/supplier/saveSupplier">
                    <a href="javascript:modifySupplier(${supplier.uuid})">修改</a> |
                    </shiro:hasPermission>
                    <shiro:hasPermission name="/internal/supplier/deleteSupplier">
                    <a href="javascript:deleteSupplier('${supplier.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<div id="supplierDialog" title="供应商信息" class="easyui-dialog" style="left: 35%;">
    <form id="supplierForm" method="post" action="supplierList.jsp">
        <table style="font-size:14px;" cellpadding=10 cellspacing="5">
            <tr>
                <td align="right">供应商名称:</td>
                <td><input type="text" name="supplierName" id="supplierName" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">联系人:</td>
                <td><input type="text" name="contactPerson" id="contactPerson" style="width:200px;"></td>
            </tr>
            <tr>
                <td align="right">电话</td>
                <td><input type="text" name="phone" id="phone" style="width:200px;">
                </td>
            </tr>

            <tr>
                <td align="right">QQ</td>
                <td><input type="text" name="qq" id="qq" style="width:200px;">
                </td>
            </tr>

            <tr>
                <td align="right">微信</td>
                <td><input type="text" name="weChat" id="weChat" style="width:200px;">
                </td>
            </tr>

            <tr>
                <td align="right">地址</td>
                <td><textarea name="address" id="address" cols="20" rows="4" style="width:200px; resize: none"></textarea>
                </td>
            </tr>

            <tr>
                <td align="right">URL</td>
                <td><input type="text" name="url" id="url" cols="20" rows="4" style="width:200px;"></input>
                </td>
            </tr>

            <tr>
                <td align="right">邮箱</td>
                <td><input type="text" name="email" id="email" cols="20" rows="4" style="width:200px;"/>
                </td>
            </tr>

            <tr>
                <td align="right" >备注</td>
                <td><textarea name="remark" id="remark" cols="20" rows="4" style="width:200px; resize: none"></textarea>
                </td>
            </tr>

            <tr>
                <td align="right">服务类型:</td>
                <td>
                    <select name="supplierNexuss" id="supplierServiceTypeMappings" style="width:200px;height:30px;"></select>
                        <div id="supplierServiceTypeDiv">
                        <input type="checkbox" onclick="tickAllCheckboxForSupplierServiceType(this)" id="SupplierNexusAll"/>全选<br>
                            <hr>
                        <c:forEach items="${supplierServiceTypes}" var="supplierServiceType">
                            <input type="checkbox" name="supplierServiceTypeMappings" value="${supplierServiceType.uuid}" onclick="tickAllCheckboxWhenAllItemsSelected()">
                            <span>${supplierServiceType.name}</span>
                            <br>
                        </c:forEach>
                        </div>
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
<script src="${staticPath }/supplier/supplierList.js"></script>
</body>
</html>
