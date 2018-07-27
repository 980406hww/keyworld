$(function () {
    $('#serverAddressDialog').dialog("close");
    $("#showServerAddressListDiv").css("margin-top",$("#showServerAddressTableDiv").height());
    alignTableHeader();
    pageLoad();
});
function pageLoad() {
    var searchServerAddressForm = $("#searchServerAddressForm");
    var pages = searchServerAddressForm.find('#pagesHidden').val();
    var pageSize = searchServerAddressForm.find('#pageSizeHidden').val();
    var currentPageNumber = searchServerAddressForm.find('#currentPageNumberHidden').val();
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
function resetPageNumber() {
    var searchServerAddressForm = $("#searchServerAddressForm");
    var serverAddress = searchServerAddressForm.find("#serverAddress").val();
    if(serverAddress != "") {
        searchServerAddressForm.find("#serverAddress").val($.trim(serverAddress));
    }
    searchServerAddressForm.find("#currentPageNumberHidden").val(1);
}
function changePaging(currentPage, pageSize) {
    var searchServerAddressForm = $("#searchServerAddressForm");
    searchServerAddressForm.find("#currentPageNumberHidden").val(currentPage);
    searchServerAddressForm.find("#pageSizeHidden").val(pageSize);
    searchServerAddressForm.submit();
}
function alignTableHeader(){
    var td = $("#showServerAddressListTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function deleteServerAddress(uuid) {
    if (confirm("确实要删除这条记录?") == false) return;
    $.ajax({
        url: '/internal/serverAddress/deleteServerAddress/' + uuid,
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
function deleteServerAddressList(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要操作的设置信息');
        return;
    }
    if (confirm("确实要删除这些记录?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/serverAddress/deleteServerAddressList',
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
function showServerAddressDialog(uuid) {
    if (uuid == null) {
        $('#serverAddressForm')[0].reset();
    }
    $("#serverAddressDialog").show();
    $("#serverAddressDialog").dialog({
        resizable: false,
        width: 310,
        height: 100,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveServerAddress(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#serverAddressForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#serverAddressDialog').dialog("close");
                    $('#serverAddressForm')[0].reset();
                }
            }]
    });
    $("#serverAddressDialog").dialog("open");
    $('#serverAddressDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveServerAddress(uuid) {
    var serverAddressForm = $("#serverAddressDialog").find("#serverAddressForm");
    var serverAddress = {};
    serverAddress.uuid = uuid;
    serverAddress.serverAddress = serverAddressForm.find("#serverAddress").val();
    if(serverAddress.serverAddress == null || serverAddress.serverAddress== '' || serverAddress.serverAddress == ""){
        alert("服务器地址不能为空!");
        return;
    }
    $.ajax({
        url: '/internal/serverAddress/saveServerAddress',
        data: JSON.stringify(serverAddress),
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
    $("#applyInfoDialog").dialog("close");
    $('#applyInfoForm')[0].reset();
}
function modifyServerAddress(uuid) {
    getApplyInfo(uuid, function (serverAddress) {
        if (serverAddress != null) {
            initServerAddressDialog(serverAddress);
            showServerAddressDialog(uuid);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getApplyInfo(uuid, callback) {
    $.ajax({
        url: '/internal/serverAddress/getServerAddress/' + uuid,
        type: 'Get',
        success: function (serverAddress) {
            callback(serverAddress);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function initServerAddressDialog(serverAddress) {
    var serverAddressForm = $("#serverAddressForm");
    serverAddressForm.find("#serverAddress").val(serverAddress.serverAddress);
}