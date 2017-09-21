<%@ include file="/commons/basejs.jsp" %>
<%@ include file="/commons/global.jsp" %>
<html>
<head>
    <script language="javascript" type="text/javascript" src="/common.js"></script>
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

    <script>
        $(function () {
            $('#supplierDialog').dialog("close");
            $("#showSupplierListDiv").css("margin-top",$("#showSupplierTableDiv").height());
            alignTableHeader();
            pageLoad();
            $('#supplierNexus').combo({
                required : false,
                editable : false,
                multiple : true
            });
            $('#sp').appendTo($('#supplierNexus').combo('panel'));

            $("#sp input")
                .click(
                    function() {
                        var _value = "";
                        var _text = "";
                        $("[name=supplierNexus]:input:checked").each(function() {
                            _value += $(this).val() + ",";
                            _text += $(this).next("span").text() + ",";
                        });
                        //设置下拉选中值
                        $('#supplierNexus').combo('setValue', _value).combo(
                            'setText', _text);
                });
        });

        function pageLoad() {
            var searchSupplierForm = $("#searchSupplierForm");
            var pageSize = searchSupplierForm.find('#pageSizeHidden').val();
            var pages = searchSupplierForm.find('#pagesHidden').val();
            var currentPageNumber = searchSupplierForm.find('#currentPageNumberHidden').val();
            var showCustomerBottomDiv = $('#showCustomerBottomDiv');
            $("#showCustomerBottomDiv").find("#chooseRecords").val(${page.size});
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

        function selectSupplierNexusAll(self) {
            var b = document.getElementsByName("supplierNexus");
            if (self.checked) {
                for (var i = 0; i < b.length; i++) {
                        b[i].checked = true;
                }
            } else {
                for (var i = 0; i < b.length; i++) {
                        b[i].checked = false;
                }
            }
        }
        function decideselectSupplierNexusAll() {
            var a = document.getElementsByName("supplierNexus");
            var select=0;
            for(var i = 0; i < a.length; i++){
                if (a[i].checked == true){
                    select++;
                }
            }
            if(select == a.length){
                $("#SupplierNexusAll").prop("checked",true);
            }else {
                $("#SupplierNexusAll").prop("checked",false);
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

        function delSupplier(uuid) {
            if (confirm("确实要删除这个供应商吗?") == false) return;
            $.ajax({
                url: '/internal/supplier/delSupplier/' + uuid,
                type: 'Get',
                success: function (result) {
                    if (result) {
                        $().toastmessage('showSuccessToast', "删除成功",true);
                    } else {
                        $().toastmessage('showErrorToast', "删除失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "删除失败");
                }
            });
        }

        function deleteSupplier(self) {
            var uuids = getSelectedIDs();
            if (uuids === '') {
                alert('请选择要操作的设置信息');
                return;
            }
            if (confirm("确实要删除这些客户吗?") == false) return;
            var postData = {};
            postData.uuids = uuids.split(",");
            $.ajax({
                url: '/internal/supplier/deleteSuppliers',
                data: JSON.stringify(postData),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (data) {
                    if (data) {
                        $().toastmessage('showSuccessToast', "操作成功",true);
                    } else {
                        $().toastmessage('showErrorToast', "操作失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "操作失败");
                }
            });
        }

        function getSelectedIDs() {
            var uuids = '';
            $.each($("input[name=uuid]:checkbox:checked"), function () {
                if (uuids === '') {
                    uuids = $(this).val();
                } else {
                    uuids = uuids + "," + $(this).val();
                }
            });
            return uuids;
        }

        function resetPageNumber() {
            var searchSupplierForm = $("#searchSupplierForm");
            searchSupplierForm.find("#currentPageNumberHidden").val(1);
        }
        function initSupplierDialog(supplier) {
            var supplierForm = $("#supplierForm");
            supplierForm.find("#supplierName").val(supplier.supplierName);
            supplierForm.find("#contactPerson").val(supplier.contactPerson);
            supplierForm.find("#phone").val(supplier.phone);
            supplierForm.find("#qq").val(supplier.qq);
            supplierForm.find("#weChat").val(supplier.weChat);
            supplierForm.find("#address").val(supplier.address);
            supplierForm.find("#url").val(supplier.url);
            supplierForm.find("#email").val(supplier.email);
            supplierForm.find("#remark").val(supplier.remark);
            var serviceTypeArray = [];
            for(var i=0;i<supplier.supplierNexus.length;i++){
                serviceTypeArray[i]=supplier.supplierNexus[i].supplierServiceTypeCode;
            }
            $("#sp input[name=supplierNexus]").val(serviceTypeArray);
        }

        function modifySupplier(uuid) {
            getSupplier(uuid, function (supplier) {
                if (supplier != null) {
                    initSupplierDialog(supplier);
                    showSupplierDialog(uuid);
                } else {
                    $().toastmessage('showErrorToast', "获取信息失败");
                }
            })
        }
        function getSupplier(uuid, callback) {
            $.ajax({
                url: '/internal/supplier/getSupplier/' + uuid,
                type: 'Get',
                success: function (supplier) {
                    callback(supplier);
                },
                error: function () {
                    $().toastmessage('showErrorToast', "获取信息失败");
                }
            });
        }
        function showSupplierDialog(uuid) {
            if (uuid == null) {
                $('#supplierForm')[0].reset();
            }
            $("#supplierDialog").dialog({
                resizable: false,
                width: 330,
                height: 480,
                modal: true,
                buttons: [{
                    text: '保存',
                    iconCls: 'icon-ok',
                    handler: function () {
                        saveSupplier(uuid);
                    }
                },
                    {
                        text: '清空',
                        iconCls: 'fi-trash',
                        handler: function () {
                            $('#supplierForm')[0].reset();
                        }
                    },
                    {
                        text: '取消',
                        iconCls: 'icon-cancel',
                        handler: function () {
                            $('#supplierDialog').dialog("close");
                            $('#supplierForm')[0].reset();
                        }
                    }]
            });
            $("#supplierDialog").dialog("open");
            $('#supplierDialog').window("resize",{top:$(document).scrollTop() + 100});
        }
        function saveSupplier(uuid) {
            var supplierForm = $("#supplierDialog").find("#supplierForm");
            var supplier = {};
            supplier.uuid = uuid;
            supplier.supplierName = supplierForm.find("#supplierName").val();
            supplier.contactPerson = supplierForm.find("#contactPerson").val();
            supplier.qq = supplierForm.find("#qq").val();
            if (!(/^[1-9]\d{4,14}$/.test(supplier.qq)) && (supplier.qq != '')) {
                alert("请输入正确的QQ");
                return;
            }
            supplier.phone = supplierForm.find("#phone").val();
            if (!(/^1[34578]\d{9}$/.test(supplier.phone)) && (supplier.phone != '')) {
                alert("请输入正确的手机");
                return;
            }
            supplier.weChat = supplierForm.find("#weChat").val();
            if (!(supplier.weChat != '')) {
                alert("微信号不能为空");
                return;
            }
            if (supplier.contactPerson == '') {
                alert("请输入联系人");
                return;
            }

            supplier.address = supplierForm.find("#address").val();
            supplier.remark = supplierForm.find("#remark").val();
            supplier.email = supplierForm.find("#email").val();
            if(!(/^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/.test(supplier.email)) && (supplier.email != '')){
                alert("请输入正确的邮箱!");
                return;
            }

            supplier.url = supplierForm.find("#url").val();
            if(supplier.url ==''){
                alert("网址URL不能为空!");
                return;
            }
            supplier.supplierNexus = [];
            var supplierNexusss=[];

            var count = $("#sp input[name=supplierNexus]:checked").length;
            $("#sp input[name=supplierNexus]:checked").each(function(idx,val){
                supplierNexusss[idx]=$(val).val();
            });
            for(var i=0 ; i<supplierNexusss.length ; i++){
                if(!(supplierNexusss[i] == " " || supplierNexusss[i]=="," || supplierNexusss[i]=="")){
                    supplier.supplierNexus.push({"supplierServiceTypeCode":supplierNexusss[i]});
                }
            }
            $.ajax({
                url: '/internal/supplier/saveSupplier',
                data: JSON.stringify(supplier),
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                timeout: 5000,
                type: 'POST',
                success: function (result) {
                    if (result) {
                        $().toastmessage('showSuccessToast', "保存成功",true);
                    } else {
                        $().toastmessage('showErrorToast', "保存失败");
                    }
                },
                error: function () {
                    $().toastmessage('showErrorToast', "保存失败");
                }
            });
            $("#supplierDialog").dialog("close");
            $('#supplierForm')[0].reset();
        }

        function changePaging(currentPage, pageSize) {
            var searchSupplierForm = $("#searchSupplierForm");
            searchSupplierForm.find("#currentPageNumberHidden").val(currentPage);
            searchSupplierForm.find("#pageSizeHidden").val(pageSize);
            searchSupplierForm.submit();
        }
        function alignTableHeader(){
            var td = $("#showSupplierListTable tr:first td");
            var ctd = $("#headerTable tr:first td");
            $.each(td, function (idx, val) {
                ctd.eq(idx).width($(val).width());
            });
        }

    </script>

</head>
<body>
<div id="showSupplierTableDiv">
    <%@include file="/menu.jsp" %>
    <table width="100%" style="font-size:12px; margin-top: 40px;" cellpadding=3>
        <tr>
            <td colspan=14>
                <form method="post" id="searchSupplierForm" action="/internal/supplier/searchSuppliers">
                    <table style="font-size:12px;">
                        <tr>
                            <td align="right">联系人:</td>
                            <td><input type="text" name="contactPerson"
                                       value="${supplierCriteria.contactPerson}"
                                       style="width:200px;"></td>
                            <td align="right">QQ:</td>
                            <td><input type="text" name="qq" value="${supplierCriteria.qq}"
                                       style="width:200px;"></td>
                            <td align="right">联系电话:</td>
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
                                    <input type="button" onclick="showSupplierDialog()" value="添加供应商">&nbsp;&nbsp;
                                </shiro:hasPermission></td>
                            <td>
                                <shiro:hasPermission name="/internal/supplier/deleteSuppliers">
                                    <input type="button" onclick="deleteSupplier(this)" value="删除所选">
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
            <td align="left" width="10"><input type="checkbox" onclick="selectAll(this)" id="selectAllChecked"/></td>
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
            <tr onmouseover="doOver(this);" onmouseout="doOut(this);" height=30 <c:if test="${status.index%2==0}">bgcolor="#eee" </c:if> >
                <td width="10"><input type="checkbox" name="uuid" value="${supplier.uuid}" onclick="decideSelectAll()"/></td>
                <td width="100">${supplier.supplierName}</td>
                <td width="80">${supplier.contactPerson}</td>
                <td width="80">${supplier.phone}</td>
                <td width="80">${supplier.qq}</td>
                <td width="80">${supplier.weChat} </td>
                <td width="180">${supplier.address}</td>
                <td width="150">
                    <c:forEach items="${supplier.supplierNexus}" var="supplierNexus">
                        ${supplierNexus.supplierServiceType.name} ${" "}
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
                    <shiro:hasPermission name="/internal/supplier/delSupplier">
                    <a href="javascript:delSupplier('${supplier.uuid}')">删除</a>
                    </shiro:hasPermission>
                </td>
            </tr>
        </c:forEach>
    </table>
</div>

<div id="supplierDialog" title="供应商信息" class="easyui-dialog" style="left: 35%;">
    <form id="supplierForm" method="post" action="supplierList.jsp">
        <table style="font-size:14px;" cellpadding=5>
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
                <td><textarea name="address" id="address" cols="20" rows="4" style="width:200px;"></textarea>
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
                <td><textarea name="remark" id="remark" cols="20" rows="4" style="width:200px;"></textarea>
                </td>
            </tr>

            <tr>
                <td align="right">服务类型:</td>
                <td>
                    <select name="supplierNexuss" id="supplierNexus" style="width:200px;height:30px;"></select>
                        <div id="sp">
                        <input type="checkbox" onclick="selectSupplierNexusAll(this)" id="SupplierNexusAll"/>全选<br>
                            <hr>
                        <c:forEach items="${supplierServiceTypes}" var="supplierServiceType">
                            <input type="checkbox" name="supplierNexus" value="${supplierServiceType.uuid}" onclick="decideselectSupplierNexusAll()">
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
</body>
</html>
