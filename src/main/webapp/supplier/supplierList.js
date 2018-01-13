$(function () {
    $('#supplierDialog').dialog("close");
    $("#showSupplierListDiv").css("margin-top",$("#showSupplierTableDiv").height());
    alignTableHeader();
    pageLoad();
    $('#supplierServiceTypeMappings').combo({
        required : false,
        editable : false,
        multiple : true
    });
    $('#supplierServiceTypeDiv').appendTo($('#supplierServiceTypeMappings').combo('panel'));

    $("#supplierServiceTypeDiv input").click(
        function() {
            var _value = "";
            var _text = "";
            $("[name=supplierServiceTypeMappings]:input:checked").each(function() {
                _value += $(this).val() + ",";
                _text += $(this).next("span").text() + ",";
            });
            //设置下拉选中值
            $('#supplierServiceTypeMappings').combo('setValue', _value).combo(
                'setText', _text);
        });
});
function pageLoad() {
    var searchSupplierForm = $("#searchSupplierForm");
    var pages = searchSupplierForm.find('#pagesHidden').val();
    var pageSize = searchSupplierForm.find('#pageSizeHidden').val();
    var currentPageNumber = searchSupplierForm.find('#currentPageNumberHidden').val();
    var showCustomerBottomDiv = $('#showCustomerBottomDiv');
    $("#showCustomerBottomDiv").find("#chooseRecords").val(pageSize);
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
function tickAllCheckboxForSupplierServiceType(self) {
    var b = document.getElementsByName("supplierServiceTypeMappings");
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
function tickAllCheckboxWhenAllItemsSelected() {
    var a = document.getElementsByName("supplierServiceTypeMappings");
    var selectedCount=0;
    for(var i = 0; i < a.length; i++){
        if (a[i].checked == true){
            selectedCount++;
        }
    }
    if(selectedCount == a.length){
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
function deleteSupplier(uuid) {
    if (confirm("确实要删除这个供应商吗?") == false) return;
    $.ajax({
        url: '/internal/supplier/deleteSupplier/' + uuid,
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
function deleteSuppliers(self) {
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
    var contactPerson = searchSupplierForm.find("#contactPerson").val();
    var qq = searchSupplierForm.find("#qq").val();
    var phone = searchSupplierForm.find("#phone").val();
    if(contactPerson != "") {
        searchSupplierForm.find("#contactPerson").val($.trim(contactPerson));
    }
    if(qq != "") {
        searchSupplierForm.find("#qq").val($.trim(qq));
    }
    if(phone != "") {
        searchSupplierForm.find("#phone").val($.trim(phone));
    }
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
    for(var i=0;i<supplier.supplierServiceTypeMappings.length;i++){
        serviceTypeArray[i]=supplier.supplierServiceTypeMappings[i].supplierServiceTypeUuid;
    }
    $("#supplierServiceTypeDiv input[name=supplierServiceTypeMappings]").val(serviceTypeArray);

    var _value = "";
    var _text = "";
    $("[name=supplierServiceTypeMappings]:input:checked").each(function() {
        _value += $(this).val() + ",";
        _text += $(this).next("span").text() + ",";
    });
    //设置下拉选中值
    $('#supplierServiceTypeMappings').combo('setValue', _value).combo('setText', _text);
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
        width: 310,
        height: 400,
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
    supplier.supplierServiceTypeMappings = [];
    var supplierServiceTypeMappings =[];

    $("#supplierServiceTypeDiv input[name=supplierServiceTypeMappings]:checked").each(function(idx,val){
        supplierServiceTypeMappings[idx] = $(val).val().trim();
    });
    for(var i=0 ; i<supplierServiceTypeMappings.length ; i++){
        if(!(supplierServiceTypeMappings[i] == "" || supplierServiceTypeMappings[i] == ",")){
            supplier.supplierServiceTypeMappings.push({"supplierServiceTypeUuid" : supplierServiceTypeMappings[i]});
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