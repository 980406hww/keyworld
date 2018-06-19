$(function () {
    $('#vpnInfoDialog').dialog("close");
    $("#showVpnInfoListDiv").css("margin-top",$("#showVpnInfoTableDiv").height());
    alignTableHeader();
    pageLoad();
});
function pageLoad() {
    var searchVpnInfoForm = $("#searchVpnInfoForm");
    var pages = searchVpnInfoForm.find('#pagesHidden').val();
    var pageSize = searchVpnInfoForm.find('#pageSizeHidden').val();
    var currentPageNumber = searchVpnInfoForm.find('#currentPageNumberHidden').val();
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
    var searchVpnInfoForm = $("#searchVpnInfoForm");
    var userName = searchVpnInfoForm.find("#userName").val();
    if(userName != "") {
        searchVpnInfoForm.find("#userName").val($.trim(userName));
    }
    var imei = searchVpnInfoForm.find("#imei").val();
    if(userName != "") {
        searchVpnInfoForm.find("#imei").val($.trim(imei));
    }
    searchVpnInfoForm.find("#currentPageNumberHidden").val(1);
}
function changePaging(currentPage, pageSize) {
    var searchVpnInfoForm = $("#searchVpnInfoForm");
    searchVpnInfoForm.find("#currentPageNumberHidden").val(currentPage);
    searchVpnInfoForm.find("#pageSizeHidden").val(pageSize);
    searchVpnInfoForm.submit();
}
function alignTableHeader(){
    var td = $("#showApplyInfoListTable tr:first td");
    var ctd = $("#headerTable tr:first td");
    $.each(td, function (idx, val) {
        ctd.eq(idx).width($(val).width());
    });
}
function deleteVpnInfo(uuid) {
    if (confirm("确实要删除这条记录?") == false) return;
    $.ajax({
        url: '/internal/vpnInfo/deleteVpnInfo/' + uuid,
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
function deleteVpnInfos(self) {
    var uuids = getSelectedIDs();
    if (uuids === '') {
        alert('请选择要操作的设置信息');
        return;
    }
    if (confirm("确实要删除这些记录?") == false) return;
    var postData = {};
    postData.uuids = uuids.split(",");
    $.ajax({
        url: '/internal/vpnInfo/deleteVpnInfoList',
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
function showVpnInfoDialog(uuid) {
    if (uuid == null) {
        $('#vpnInfoForm')[0].reset();
    }
    $("#vpnInfoDialog").show();
    $("#vpnInfoDialog").dialog({
        resizable: false,
        width: 310,
        height: 200,
        modal: true,
        buttons: [{
            text: '保存',
            iconCls: 'icon-ok',
            handler: function () {
                saveVpnInfo(uuid);
            }
        },
            {
                text: '清空',
                iconCls: 'fi-trash',
                handler: function () {
                    $('#vpnInfoForm')[0].reset();
                }
            },
            {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#vpnInfoDialog').dialog("close");
                    $('#vpnInfoForm')[0].reset();
                }
            }]
    });
    $("#vpnInfoDialog").dialog("open");
    $('#vpnInfoDialog').window("resize",{top:$(document).scrollTop() + 100});
}
function saveVpnInfo(uuid) {
    var vpnInfoForm = $("#vpnInfoDialog").find("#vpnInfoForm");
    var vpnInfo = {};
    vpnInfo.uuid = uuid;
    vpnInfo.userName = vpnInfoForm.find("#userName").val();
    vpnInfo.password = vpnInfoForm.find("#password").val();
    vpnInfo.imei = vpnInfoForm.find("#imei").val();
    vpnInfo.startTime = vpnInfoForm.find("#startTime").val();
    vpnInfo.stopTime = vpnInfoForm.find("#stopTime").val();
    if(vpnInfo.userName == null || vpnInfo.userName== '' || vpnInfo.userName == ""){
        alert("VPN账号不能为空!");
        return;
    }
    if(vpnInfo.password == null || vpnInfo.password== '' || vpnInfo.password == ""){
        alert("VPN密码不能为空!");
        return;
    }
    if(vpnInfo.startTime == null || vpnInfo.startTime== '' || vpnInfo.startTime == ""){
        alert("启用时间不能为空!");
        return;
    }
    if(vpnInfo.stopTime == null || vpnInfo.stopTime== '' || vpnInfo.stopTime == ""){
        alert("停用时间不能为空!");
        return;
    }
    $.ajax({
        url: '/internal/vpnInfo/saveVpnInfo',
        data: JSON.stringify(vpnInfo),
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
    $("#vpnInfoDialog").dialog("close");
    $('#vpnInfoForm')[0].reset();
}
function modifyVpnInfo(uuid) {
    getApplyInfo(uuid, function (vpnInfo) {
        if (vpnInfo != null) {
            initVpnInfoDialog(vpnInfo);
            showVpnInfoDialog(uuid);
        } else {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    })
}
function getApplyInfo(uuid, callback) {
    $.ajax({
        url: '/internal/vpnInfo/getVpnInfo/' + uuid,
        type: 'Get',
        success: function (vpnInfo) {
            callback(vpnInfo);
        },
        error: function () {
            $().toastmessage('showErrorToast', "获取信息失败");
        }
    });
}
function initVpnInfoDialog(vpnInfo) {
    var vpnInfoForm = $("#vpnInfoForm");
    vpnInfoForm.find("#userName").val(vpnInfo.userName);
    vpnInfoForm.find("#password").val(vpnInfo.password);
    vpnInfoForm.find("#imei").val(vpnInfo.imei);
    vpnInfoForm.find("#startTime").val(userDate(vpnInfo.startTime));
    vpnInfoForm.find("#stopTime").val(userDate(vpnInfo.stopTime));
}
function userDate(uData){
    var myDate = new Date(uData);
    var year = myDate.getFullYear();
    var month = myDate.getMonth() + 1;
    var day = myDate.getDate();
    return year + '-' + month + '-' + day;
}